/*******************************************************************************
 * Copyright (c) 2009 Ola Spjuth.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Ola Spjuth
 *     Carl Mäsak
 ******************************************************************************/
package net.bioclipse.align.kalign.ws.handlers;

import net.bioclipse.biojava.ui.editors.SequenceEditor;

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

        //MASAK: borde väl bli så här typ
        
//        editor.getSequencesToAlign();
//        
//        IKalignJavaManager kalign=Activator.getDefault().getKalignManager();
//        kalign.alignDNA( dnaList )
//        or
//        kalign.alignProteins( proteinList )
//        editor.setSequences(...)
       
        
        return null;
    }

}