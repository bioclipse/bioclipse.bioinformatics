/*******************************************************************************
 * Copyright (c) 2009 Ola Spjuth.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Ola Spjuth
 *     Carl MŠsak
 ******************************************************************************/
package net.bioclipse.align.kalign.ws.handlers;

import java.util.ArrayList;
import java.util.List;

import net.bioclipse.align.kalign.ws.Activator;
import net.bioclipse.align.kalign.ws.business.IKalignManager;
import net.bioclipse.biojava.ui.editors.SequenceEditor;
import net.bioclipse.core.business.BioclipseException;
import net.bioclipse.core.domain.IProtein;
import net.bioclipse.core.domain.ISequence;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * 
 * @author ola, masak
 *
 */
public class AlignHandler extends AbstractHandler implements IHandler {

    public Object execute( ExecutionEvent event ) throws ExecutionException {

        IEditorPart editor = HandlerUtil.getActiveEditor(event);
        
        if (!(editor instanceof SequenceEditor))
            return null;

        List<ISequence> sequences = ((SequenceEditor)editor).getSequences();
        IKalignManager kalign = Activator.getDefault().getKalignManager();
        List<IProtein> proteins = new ArrayList<IProtein>();
        for (ISequence seq : sequences)
            proteins.add(IProtein.class.cast(seq));
        List<IProtein> alignedProteins;
        try {
            alignedProteins = kalign.alignProteins(proteins);
        } catch (BioclipseException e) {
            // TODO: Better error handling
            return null;
        }
        List<ISequence> alignedSequences = new ArrayList<ISequence>();
        for (ISequence seq : alignedProteins)
            alignedSequences.add(IProtein.class.cast(seq));
        ((SequenceEditor)editor).setSequences(alignedSequences);

        return null;
    }

}