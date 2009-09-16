/*****************************************************************************
 * Copyright (c) 2008 Bioclipse Project
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *****************************************************************************/

package net.bioclipse.biojava.ui.editors;

import java.util.List;

import net.bioclipse.biojava.business.Activator;
import net.bioclipse.biojava.business.IBiojavaManager;
import net.bioclipse.biojava.ui.views.outline.SequenceOutlinePage;
import net.bioclipse.core.business.BioclipseException;
import net.bioclipse.core.domain.ISequence;
import net.bioclipse.core.util.LogUtils;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

public class SequenceEditor extends MultiPageEditorPart {

    private Logger logger = Logger.getLogger(SequenceEditor.class);

    public static final String SEQUENCE_EDITOR_ID
      = "net.bioclipse.biojava.ui.editors.SequenceEditor";

    private Aligner aligner;
    private SequenceOutlinePage outlinePage;
    private boolean dirty;

    @Override
    protected void createPages() {
        setPartName( getEditorInput().getName() );
        try {
            int pageIndex1 = this.addPage( aligner = new Aligner(),
                                           getEditorInput() ),
                pageIndex2 = this.addPage( new TextEditor(),
                                           getEditorInput() );
            setPageText(pageIndex1, "Sequences");
            setPageText(pageIndex2, "Source");
        } catch ( PartInitException e ) {
            e.printStackTrace();
        }
    }

    public boolean isDirty() {
        return dirty;
    }

    @Override
    public void doSave( IProgressMonitor monitor ) {

        if (!( getEditorInput() instanceof FileEditorInput )) {
            showError( "Sequence editor can only save to file." );
            return;
        }
        FileEditorInput fed = (FileEditorInput) getEditorInput();
        IFile file= fed.getFile();
        IBiojavaManager biojava
          = Activator.getDefault().getJavaBiojavaManager();

        //Save sequences to FASTA format
        try {
            biojava.sequencesToFASTAfile( getSequences(), file, monitor);
            //Clear dirty flag
            dirty = false;
            firePropertyChange( IEditorPart.PROP_DIRTY );

        } catch ( BioclipseException e ) {
            LogUtils.handleException( e, logger,
                                 net.bioclipse.biojava.ui.Activator.PLUGIN_ID );
        }

    }

    @Override
    public void doSaveAs() {
    }

    @Override
    public boolean isSaveAsAllowed() {
        return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object getAdapter(Class required) {

       // Adapter for Outline
       if (IContentOutlinePage.class.equals(required))
           return outlinePage
               = outlinePage == null
                   ? new SequenceOutlinePage(getEditorInput(), this)
                   : outlinePage;

       return super.getAdapter(required);
   }

    public void zoomIn() {
        aligner.zoomIn();
    }

    public void zoomOut() {
        aligner.zoomOut();
    }

    public java.util.List<ISequence> getSequences() {
        return aligner.getSequences();
    }

    public void setSequences(List<ISequence> sequences) {
        aligner.setSequences(sequences);
        dirty = true;
        firePropertyChange( IEditorPart.PROP_DIRTY );
    }

    private void showError(String message) {
        MessageDialog.openError(
            getSite().getShell(),
            "Error",
            message
        );
    }

    public void toggleWrapMode() {
        aligner.toggleWrapMode();
    }

}
