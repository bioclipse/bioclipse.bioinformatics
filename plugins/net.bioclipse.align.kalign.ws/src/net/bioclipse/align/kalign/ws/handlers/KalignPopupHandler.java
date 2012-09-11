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
package net.bioclipse.align.kalign.ws.handlers;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import net.bioclipse.align.kalign.ws.Activator;
import net.bioclipse.align.kalign.ws.business.IKalignManager;
import net.bioclipse.biojava.business.IBiojavaManager;
import net.bioclipse.core.business.BioclipseException;
import net.bioclipse.core.domain.IProtein;
import net.bioclipse.core.domain.ISequence;
import net.bioclipse.core.util.LogUtils;
import net.bioclipse.ui.business.IUIManager;

import org.apache.log4j.Logger;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * 
 * @author ola
 *
 */
public class KalignPopupHandler extends AbstractHandler implements IHandler {
    
    private static final Logger logger = Logger.getLogger(KalignPopupHandler.class);


    public Object execute( ExecutionEvent event ) throws ExecutionException {

        ISelection sel = HandlerUtil.getCurrentSelection( event );
        if (!( sel instanceof IStructuredSelection ))
            return null;

        IStructuredSelection ssel = (IStructuredSelection) sel;
        
        final Shell shell = HandlerUtil.getActiveShell( event );

        final IBiojavaManager biojava=net.bioclipse.biojava.business.Activator.
        getDefault().getJavaBiojavaManager();
        
        final List<IProtein> proteins=new ArrayList<IProtein>();
        final List<String> names=new ArrayList<String>();
        IProject firstProject=null;
        for (Object obj : ssel.toList()){
            if ( obj instanceof IFile ) {
                IFile file=(IFile)obj;
                if (firstProject==null)
                    firstProject=file.getProject();
                try {
                	
                	List<IProtein> readProts = biojava.proteinsFromFile( file );
                	
                    proteins.addAll( readProts);
                    String fname=file.getName();
                    if (fname.lastIndexOf( "." )>0)
                        fname=fname.substring( 0,fname.lastIndexOf( "." ) );
                    names.add( fname );
                } catch ( FileNotFoundException e ) {
                    logger.error("Could not find file: " 
                                 + file.getLocation());
                }
                catch (Exception e) {
                    MessageDialog.openError( 
                            shell,
                            "Kalign",
                            "Error parsing proteins. Make sure you have valid files!");
                    return null;
				}
            }
        }
        
        if (firstProject==null){
            MessageDialog.openError( 
                                    shell,
                                    "Kalign",
                                    "No output project could be located.");
            return null;
        }
        
        if (proteins.size()<=0){
            MessageDialog.openError( 
                                    shell,
                                    "Kalign",
                                    "No proteins could be parsed");
            return null;
        }
        if (proteins.size()==0){
            MessageDialog.openError( 
                                    shell,
                                    "Kalign",
                                    "Only one protein could be parsed, " +
                                    "need a minimum of two proteins to align.");
            return null;
        }
        
        final IProject project = firstProject;

        
        Job job = new Job("Kalign") {
			
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				
		        IKalignManager kalign = Activator.getDefault().getKalignManager();
		        List<IProtein> alignedProteins;
		        try {
		            alignedProteins = kalign.alignProteins(proteins, monitor);
		        } catch (BioclipseException e) {
		            LogUtils.handleException( e, logger, "net.bioclipse.align.kalign.ws" );
		            return null;
		        }
		        List<ISequence> alignedSequences = new ArrayList<ISequence>();
		        for (ISequence seq : alignedProteins)
		            alignedSequences.add(IProtein.class.cast(seq));

		        //Generate a new filename
		        String newFileName="";
		        for (String name : names){
		            newFileName=newFileName.concat( name+"_");
		        }
		        newFileName=newFileName.substring( 0, newFileName.length()-1 );
		        newFileName=newFileName.concat( "_aligned.fasta" );
		        
		        logger.debug( "New alignment file to save: " + newFileName );
		        
		        //Serialize to temp file and open in SeqEditor
		        IFile newfile=project.getFile( newFileName );
		        if (newfile.exists()){
		            boolean ret = MessageDialog.
		                               openConfirm( shell, 
		                               "Overwrite?", 
		                               "Resulting alignment file " 
		                               + newFileName + " exists. Overwrite?");
		            if (ret==false) return null;
		            
		        }

		        //Save aligned proteins to file
		        biojava.proteinsToFASTAfile( alignedProteins, newfile );
		        
		        try {
		        	project.refreshLocal( 1, new NullProgressMonitor() );
		        } catch ( CoreException e ) {
		            //Not much we can do if refresh fails. Should not happen.
		        }

		        //Open saved file
		        IUIManager ui = 
		            net.bioclipse.ui.business.Activator.getDefault().getUIManager();
		        ui.open( newfile );
				
				return Status.OK_STATUS;
			}
		};
		job.setUser(true);
		job.schedule();
        
        return null;
    }

}
