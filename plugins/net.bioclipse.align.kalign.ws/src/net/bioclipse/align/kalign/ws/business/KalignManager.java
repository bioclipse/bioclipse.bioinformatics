/* *****************************************************************************
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
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.rpc.ServiceException;

import org.apache.log4j.Logger;
import org.biojava.bio.program.sax.ClustalWAlignmentSAXParser;
import org.biojava.bio.seq.DNATools;
import org.biojava.bio.seq.ProteinTools;
import org.biojava.bio.symbol.Alphabet;
import org.eclipse.core.runtime.IProgressMonitor;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import uk.ac.ebi.jdispatcher.soap.InputParameters;
import uk.ac.ebi.jdispatcher.soap.JDispatcherService_PortType;
import uk.ac.ebi.jdispatcher.soap.JDispatcherService_ServiceLocator;


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

		//=============================
		//Set up the input to KAlign WS
		//=============================
		monitor.subTask( "Preparing input" );
		InputParameters params = new InputParameters();
		IBiojavaManager biojava
		= Activator.getDefault().getJavaBiojavaManager();

		String fastastring=null;
		if (type.equals( "P" )){
			List<IProtein> proteins=new ArrayList<IProtein>();
			for (ISequence seq : sequenceList){
				proteins.add( (IProtein)seq );
			}
			fastastring=biojava.proteinsToFASTAString( proteins );
			params.setStype( "protein" );
		}
		else{
			List<IDNA> dnas=new ArrayList<IDNA>();
			for (ISequence seq : sequenceList){
				dnas.add( (IDNA)seq );
			}
			fastastring=biojava.dnaToFASTAString( dnas );
			params.setStype( "dna" );
		}

		params.setSequence( fastastring );



		//=============================
		// Start KAlign WS and wait for completion
		//=============================

		monitor.subTask( "Submitting job..." );
		logger.debug("Sending request to Kalign...");
		monitor.worked( 1 );

		JDispatcherService_ServiceLocator loc = new JDispatcherService_ServiceLocator();

		JDispatcherService_PortType kalign;
		String resstr="";
		try {
			kalign = loc.getJDispatcherServiceHttpPort();
			String jobid = kalign.run("bioclipse@bioclipse.net", "kalign with Bioclipse", params);
			logger.debug("Kalign WS invoked. Job id is: " + jobid);

			monitor.subTask( "KAlign running remotely" );
			monitor.worked( 1 );
			logger.debug("KAlign running...");

			// Poll until job has finished
			String status = "RUNNING";
			while (status.equals("RUNNING")) {
				Thread.sleep(2000); 				// Wait 2 seconds
				status = kalign.getStatus(jobid);  // check job status
				logger.debug("KAlign job status: " + status);
			}
			// If the job completed successfully...
			if (status.equals("FINISHED")) {
				// Get the text result
				byte[] resultbytes = kalign.getResult(jobid, "out", null);
				resstr = new String(resultbytes);

				// Output the result
				logger.debug("KAlign results:\n" + resstr);
			}
		} catch (ServiceException e) {
			throw new BioclipseException("KAlign service error: " + 
					e.getMessage());
		} catch (RemoteException e) {
			throw new BioclipseException("KAlign service error: " + 
					e.getMessage());
		} catch (InterruptedException e) {
			throw new BioclipseException("KAlign WS interrupted.");
		}        


		//=============================
		// Parse KAlign results
		//=============================
		monitor.subTask( "Parsing results..." );
		logger.debug( "Parsing KAlign results..." );
		monitor.worked( 1 );

		//We remove the first line since it caused erorrs with BioJava's
		//ClustalW format parsing, and we know this is the format
		resstr = resstr.replaceFirst("CLUSTAL.*","");

		//Set up a buffered reader for the contents
		ByteArrayInputStream ins=new ByteArrayInputStream(
				resstr.getBytes());
		BufferedReader contents = new BufferedReader(
				new InputStreamReader(ins));

		try {

			if (type.equals( "P" )){
				List<? extends ISequence> res;
				res = parseKalignResult(contents,
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

		} catch (IOException e) {
			throw new BioclipseException("KAlign results could not be parsed: " 
		+ e.getMessage());
		} catch (SAXException e) {
			throw new BioclipseException("KAlign results could not be parsed: " 
		+ e.getMessage());
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

