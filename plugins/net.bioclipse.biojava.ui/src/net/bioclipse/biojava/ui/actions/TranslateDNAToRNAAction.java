/* *****************************************************************************
 * Copyright (c) 2007-2008 The Bioclipse Project and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * www.eclipse.org—epl-v10.html <http://www.eclipse.org/legal/epl-v10.html>
 * 
 * Contributors:
 *     Ola Spjuth
 *     
 ******************************************************************************/
package net.bioclipse.biojava.ui.actions;

import java.io.IOException;

import net.bioclipse.biojava.domain.BiojavaDNA;
import net.bioclipse.core.domain.IDNA;
import net.bioclipse.core.domain.ISequence;

public class TranslateDNAToRNAAction extends TranslateAction{

    @Override
    public ISequence convert(ISequence sequence) throws IOException {
        //Verify input is IDNASequence
        if (!(sequence instanceof IDNA)){
            showMessage("Input is not a DNA sequence.");
            return null;
        }
            
        IDNA dnaSequence = (IDNA) sequence;
        BiojavaDNA bjDNASeq=null;

        //If not a BioJavaSequence, construct one from PlainString
        if (dnaSequence instanceof BiojavaDNA) {
            bjDNASeq = (BiojavaDNA) dnaSequence;
        }else {
            ISequence crSeq=getBiojava().DNAfromPlainSequence(dnaSequence.getPlainSequence());
            if (crSeq instanceof BiojavaDNA) {
                bjDNASeq = (BiojavaDNA) crSeq;
            }else {
                throw new IllegalArgumentException();
            }
        }

        return getBiojava().DNAtoRNA(bjDNASeq);
    }
    
}
