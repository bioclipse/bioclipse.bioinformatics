/*******************************************************************************
 * Copyright (c) 2007-2008 The Bioclipse Project and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * www.eclipse.org/legal/epl-v10.html <http://www.eclipse.org/legal/epl-v10.html>
 *
 * Contributors:
 *     Ola Spjuth
 *
 ******************************************************************************/
package net.bioclipse.biojava.ui.actions;

import java.io.IOException;

import net.bioclipse.biojava.business.Activator;
import net.bioclipse.biojava.business.IBiojavaManager;
import net.bioclipse.core.domain.ISequence;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionDelegate;

/**
 * Abstract class to be overridden by transalte actions
 * @author ola
 *
 */
public abstract class TranslateAction extends ActionDelegate{

    private ISelection selection;
    private IBiojavaManager biojava;
    private ISequence convertedSeq;

    public ISequence getConvertedSeq() {
        return convertedSeq;
    }

    public void setConvertedSeq(ISequence convertedSeq) {
        this.convertedSeq = convertedSeq;
    }

    public IBiojavaManager getBiojava() {
        if (biojava==null) init(null);
        return biojava;
    }

    public ISelection getSelection() {
        return selection;
    }

    public void setSelection(ISelection selection) {
        this.selection = selection;
    }

    /**
     * Init BiojavaManager
     */
    @Override
    public void init(IAction action) {
        biojava=Activator.getDefault().getBioJavaManager();
    }

    /**
     * Check input, create BJsequence if necessary, invoke convert() and show result.
     */
    @Override
    public void run(IAction action) {

        if (selection instanceof IStructuredSelection) {
            IStructuredSelection sel = (IStructuredSelection) getSelection();
            Object obj=sel.getFirstElement();

            if (!(obj instanceof ISequence)) {
                showMessage("Input is not a Sequence.");
                return;
            }
            ISequence originalSeq = (ISequence) obj;

            //Do conversion
            convertedSeq=null;
            try {
                convertedSeq = convert(originalSeq);
            } catch (IOException e) {
                showMessage("Could not convert: " + e.getMessage());
                return;
            }

            if (convertedSeq==null) throw new IllegalArgumentException();

            String ret;
            ret = "Original sequence: " + originalSeq.getPlainSequence();
            ret = ret + "\nConverted sequence: " + convertedSeq.getPlainSequence();
            showMessage(ret);

            return;
        }
    }

    @Override
    public void selectionChanged(IAction action, ISelection selection) {
        this.selection=selection;
    }

    public void showMessage(String message) {

        //Verify that workbench has been created
        try{
        if (PlatformUI.getWorkbench()==null) return;
        }catch (IllegalStateException e){
            return;
        }
        MessageDialog.openInformation(
                PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
                "Message",
                message);
    }

    /**
     * Do the conversion and return the new BioJavaSequence.
     * @return
     * @throws IOException
     */
    public abstract ISequence convert(ISequence sequence) throws IOException;

    public void setBiojava(IBiojavaManager biojava) {
        this.biojava = biojava;
    }


}
