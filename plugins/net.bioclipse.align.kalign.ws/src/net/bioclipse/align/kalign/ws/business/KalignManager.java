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

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.biojava.bio.program.sax.ClustalWAlignmentSAXParser;
import org.biojava.bio.seq.DNATools;
import org.biojava.bio.seq.ProteinTools;
import org.biojava.bio.symbol.Alphabet;
import org.eclipse.core.runtime.IProgressMonitor;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import uk.ac.ebi.www.WSKalign.Data;
import uk.ac.ebi.www.WSKalign.InputParams;
import uk.ac.ebi.www.WSKalign.WSFile;
import uk.ac.ebi.www.WSKalign.WSKalign;
import uk.ac.ebi.www.WSKalign.WSKalignServiceLocator;

import net.bioclipse.align.kalign.ws.util.SequenceCollectionContentHandler;
import net.bioclipse.biojava.business.Activator;
import net.bioclipse.biojava.business.IBiojavaManager;
import net.bioclipse.core.business.BioclipseException;
import net.bioclipse.core.domain.IDNA;
import net.bioclipse.core.domain.IProtein;
import net.bioclipse.core.domain.ISequence;
import net.bioclipse.core.domain.RecordableList;
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

        //Assert DNA is input
        for (Object obj : dnalist){
            if (!( obj instanceof IDNA )) {
                throw new BioclipseException("Input must be list of DNA only");
            }
        }
            

        List<IDNA> returnList=new RecordableList<IDNA>();
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
        
        //Assert DNA is input
        for (Object obj : proteinList){
            if (!( obj instanceof IProtein )) {
            throw new BioclipseException("Input must be list of proteins only");
            }
        }

        
        List<IProtein> returnList=new RecordableList<IProtein>();
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
            throw new BioclipseException("Type must be either 'P' " +
                    "(for protein)" +
                    " or 'N' (for nucleotide).");

        logger.debug( "Starting Kalign WS" );
        monitor.beginTask( "Aligning sequences using KAlign Web service at EBI", 5 );
        monitor.worked( 1 );
        
        monitor.subTask( "Preparing input" );

        WSKalignServiceLocator loc= new WSKalignServiceLocator();

        //Set input params
        InputParams params=new InputParams();
        params.setMoltype( type );
        params.setEmail( "bioclipse@gmail.com" );

        //Set input data
        Data inSeq = new Data();
        inSeq.setType("sequence");

        //Use biojava to serialize list of sequences to FASTA
        IBiojavaManager biojava=Activator.getDefault().getBioJavaManager();

        String fastastring=null;
        if (type.equals( "P" )){
            List<IProtein> proteins=new ArrayList<IProtein>();
            for (ISequence seq : sequenceList){
                proteins.add( (IProtein)seq );
            }
            fastastring=biojava.proteinsToFASTAString( proteins );
        }
        else{
            List<IDNA> dnas=new ArrayList<IDNA>();
            for (ISequence seq : sequenceList){
                dnas.add( (IDNA)seq );
            }
            fastastring=biojava.dnaToFASTAString( dnas );
        }
        
//        String fastastring=biojava.sequencesToFASTAString( sequenceList );
        System.out.println("Sequences to align:\n" + fastastring);
        inSeq.setContent( fastastring );
        
        //Set up out input
        Data[] content = new Data[]{inSeq};

        try {
            WSKalign kalign = loc.getWSKalign();
            
            //Send out job to Kalign@EBI
            monitor.subTask( "Submitting job..." );
            logger.debug("Sending request to Kalign...");
            String jobId = kalign.runKalign( params, content );
            logger.debug("Kalign invoked. Job id is: " + jobId);

            monitor.worked( 1 );
            monitor.subTask( "Job submitted, waiting for results..." );
            //Repetitively check status, currently no timeout
            String status=kalign.checkStatus( jobId );
            while(status.equals("RUNNING") || status.equals("PENDING")) {
                status = kalign.checkStatus(jobId);
                if(status.equals("RUNNING") || status.equals("PENDING")) {
                    // Wait before polling again.
                    Thread.sleep(1000);
                }
            }

            monitor.worked( 1 );
            monitor.subTask( "Requesting results..." );
            WSFile[] result = kalign.getResults( jobId );

            byte[] payload = null;
            //We could get several files in theory, this is not handled yet
            for (WSFile file : result){
                logger.debug("Kalign results: File type: " + file.getType() 
                             + ", File extension: " + file.getExt());

                monitor.worked( 1 );
                monitor.subTask( "Retrieving results..." );
                payload = kalign.poll( jobId, file.getType() );

//                logger.debug(new String(payload));
            }


            monitor.subTask( "Parsing results..." );
            
            //We remove the first line since it caused erorrs with BioJava's
            //ClustalW format parsing, and we know this is the format
            String resstr=new String(payload);
            resstr=resstr.substring( 42 );
            
            //Set up a buffered reader for the contents
            ByteArrayInputStream ins=new ByteArrayInputStream(
                                                             resstr.getBytes());
            BufferedReader contents = new BufferedReader(
                                                    new InputStreamReader(ins));

            if (type.equals( "P" )){
                List<? extends ISequence> res = parseKalignResult(contents, 
                                                    ProteinTools.getAlphabet());
                monitor.done();
                return res;
            }
            else {
                List<? extends ISequence> res = parseKalignResult(contents, 
                                                             DNATools.getDNA());
                monitor.done();
                return res;
            }

        } catch ( Exception e ) {
            throw new BioclipseException("Error in KAlign WS: ", e);
        }

    }


   private List<? extends ISequence> parseKalignResult( BufferedReader contents, 
                                                         Alphabet alphabet) 
    throws IOException, SAXException, BioclipseException {

        List<ISequence> sequences=new RecordableList<ISequence>();

        ClustalWAlignmentSAXParser parser = 
                                           new ClustalWAlignmentSAXParser();
        
        SequenceCollectionContentHandler handler = 
                     new SequenceCollectionContentHandler(sequences, alphabet);

        parser.setContentHandler(handler);
        parser.parse(new InputSource(contents));

        return sequences;
        
    }
}

