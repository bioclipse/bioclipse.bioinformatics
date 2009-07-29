/*******************************************************************************
 * Copyright (c) 2009 Ola Spjuth.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Ola Spjuth - initial API and implementation
 ******************************************************************************/
package net.bioclipse.align.kalign.ws.business;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;

import uk.ac.ebi.www.WSKalign.Data;
import uk.ac.ebi.www.WSKalign.InputParams;
import uk.ac.ebi.www.WSKalign.WSFile;
import uk.ac.ebi.www.WSKalign.WSKalign;
import uk.ac.ebi.www.WSKalign.WSKalignServiceLocator;

import net.bioclipse.core.business.BioclipseException;
import net.bioclipse.core.domain.IDNA;
import net.bioclipse.core.domain.IProtein;
import net.bioclipse.core.domain.ISequence;
import net.bioclipse.managers.business.IBioclipseManager;


/**
 * A manager for invoking KALign at EBI using SOAP 
 * with client stub call generated using Axis 1.4.
 * 
 * KAlign@EBI Web site: http://www.ebi.ac.uk/Tools/webservices/services/kalign
 * WSDL: http://www.ebi.ac.uk/Tools/webservices/wsdl/WSKalign.wsdl
 * 
 * @author ola
 *
 */
public class KalignManager implements IBioclipseManager {

    private static final Logger logger = Logger.getLogger(KalignManager.class);

    public String getManagerName() {
        return "kalignws";
    }
    

    /**
     * Accepts a list of DNA and delegates to generic alignment method.
     * Parses results and returns a list of aligned DNA.
     * @param dnaList List of DNA sequences to align
     * @param monitor a monitor for reporting progress
     * @return List of IDNA with aligned sequences
     * @throws BioclipseException if aligning fails or result is not list 
     * of DNA
     */
    public List<IDNA> alignDNA(List<IDNA> dnalist, 
                               IProgressMonitor monitor) 
                               throws BioclipseException{


        List<IDNA> returnList=new ArrayList<IDNA>();
        List<? extends ISequence> alist = align(dnalist, "N", monitor);
        for (ISequence seq : alist){
            if ( seq instanceof IDNA ) {
                IDNA dna = (IDNA) seq;
                returnList.add( dna );
            }else{
                throw new BioclipseException("Not all results of Kalign " +
                    "WS are DNA.");
            }
        }
        
        return returnList;

    }

    /**
     * Accepts a list of proteins and delegates to generic alignment method.
     * Parses results and returns a list of aligned proteins.
     * @param proteinList List of protein sequences to align
     * @param monitor a monitor for reporting progress
     * @return List of IProtein with aligned sequences
     * @throws BioclipseException if aligning fails or result is not list 
     * of proteins
     */
    public List<IProtein> alignProteins(List<IProtein> proteinList, 
                                       IProgressMonitor monitor) 
                                       throws BioclipseException{
        
        List<IProtein> returnList=new ArrayList<IProtein>();
        List<? extends ISequence> alist = align(proteinList, "P", monitor);
        for (ISequence seq : alist){
            if ( seq instanceof IProtein ) {
                IProtein protein = (IProtein) seq;
                returnList.add( protein );
            }else{
                throw new BioclipseException("Not all results of Kalign " +
                		"WS are proteins.");
            }
        }
        
        return returnList;
    }
    
    /**
     * A generic implementation of KALign WEb service at EBI.
     * @param sequenceList List of sequences to align
     * @param type 'P' for protein, 'N' for nucleotide
     * @param monitor for reporting progress
     * @return List of ISequence with aligned sequences
     * @throws BioclipseException if aligning fails
     */
    private List<? extends ISequence> align(List<? extends ISequence>
                                            sequenceList, 
                                            String type, 
                                            IProgressMonitor monitor) 
                                            throws BioclipseException{

        if (sequenceList==null || sequenceList.isEmpty())
            throw new BioclipseException("SequenceList must not be empty.");
        
        if (!(type.equals( "P" ) || type.equals( "N" )))
            throw new BioclipseException("Type must be either 'P' (for protein)" +
            		" or 'N' (for nucleotide).");
        

        logger.debug( "Starting Kalign WS" );
        monitor.subTask( "Preparing input" );

        WSKalignServiceLocator loc= new WSKalignServiceLocator();

        //Set input params
        InputParams params=new InputParams();
        params.setMoltype( type );
        params.setEmail( "bioclipse@gmail.com" );

        //Set input data
        Data inSeq = new Data();
        inSeq.setType("sequence");
        StringBuffer sequenceBuffer=new StringBuffer();
        int i=1;
        for (ISequence seq : sequenceList){
            //FIXME: Use biojava manager for this serialization when avavilable
            sequenceBuffer.append(">Sequence"+i+"\n" + seq.getPlainSequence()+"\n");
        }
        inSeq.setContent( sequenceBuffer.toString() );

        //Set up out input
        Data[] content = new Data[]{inSeq};

        try {
            WSKalign kalign = loc.getWSKalign();
            
            //Send out job to Kalign@EBI
            monitor.subTask( "Invoking KAlign Web service at EBI" );
            String jobId = kalign.runKalign( params, content );
            logger.debug("Kalign invoked. Job id is: " + jobId);

            monitor.subTask( "Waiting for result..." );
            //Repetitively check status, currently no timeout
            String status=kalign.checkStatus( jobId );
            while(status.equals("RUNNING") || status.equals("PENDING")) {
                status = kalign.checkStatus(jobId);
                monitor.subTask( "Waiting for result..." );
                if(status.equals("RUNNING") || status.equals("PENDING")) {
                    // Wait before polling again.
                    Thread.sleep(3000);
                }
            }

            monitor.subTask( "Getting results..." );
            WSFile[] result = kalign.getResults( jobId );

            logger.debug("\n ** Results ** ");

            byte[] payload = null;
            for (WSFile file : result){
                logger.debug("File type: " + file.getType());
                logger.debug("File extension: " + file.getExt());

                monitor.subTask( "Retrieving results..." );
                payload = kalign.poll( jobId, file.getType() );

                logger.debug(new String(payload));
                logger.debug("====");
            }

            monitor.subTask( "Parsing results..." );
            //Parse in BioJava
            //TODO: IMPLEMENT



        } catch ( Exception e ) {
            throw new BioclipseException("Error in KAlign WS: ", e);
        }finally{
            monitor.done();
        }

        //TODO: return something
        return null;
    }
}

