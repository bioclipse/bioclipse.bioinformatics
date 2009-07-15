/*******************************************************************************
 * Copyright (c) 2007-2008 The Bioclipse Project and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * www.eclipse.org—epl-v10.html <http://www.eclipse.org/legal/epl-v10.html>
 *
 * Contributors:
 *     Carl Masak
 *
 ******************************************************************************/
package net.bioclipse.biojava.business;

import static org.junit.Assert.assertEquals;
import net.bioclipse.biojava.domain.BiojavaDNA;
import net.bioclipse.biojava.domain.BiojavaProtein;
import net.bioclipse.biojava.domain.BiojavaRNA;
import net.bioclipse.core.domain.IDNA;
import net.bioclipse.core.domain.IProtein;
import net.bioclipse.core.domain.IRNA;

import org.apache.log4j.Logger;
import org.junit.Test;

/**
 * @author masak
 *
 */
public class BiojavaManagerTest {

    private Logger logger = Logger.getLogger(BiojavaManagerTest.class);

    private IBiojavaManager biojava = new BiojavaManager();

    @Test
    public void createDNA() {
        String dnaString = "CGTAGTCGTAGT";
        IDNA seq = biojava.DNAfromString(dnaString);
        assertEquals(BiojavaDNA.class, seq.getClass());
        assertEquals(dnaString.toLowerCase(), seq.getPlainSequence());
        assertEquals("dna:'" + dnaString.toLowerCase() + "'",
                     seq.toString());
    }

    @Test
    public void createRNA() {
        String rnaString = "UUUACGUGACCC";
        IRNA seq = biojava.RNAfromString(rnaString);
        assertEquals(BiojavaRNA.class, seq.getClass());
        assertEquals(rnaString.toLowerCase(), seq.getPlainSequence());
        assertEquals("rna:'" + rnaString.toLowerCase() + "'",
                     seq.toString());
    }

    @Test
    public void createProtein() {
        String proteinString // the hemoglobin beta2 chain, incidentally
            = "MVHLTPEEKSAVTALWGKVNVDEVGGEALGRLLVVYPWTQRFFE"
              + "SFGDLSTPDAVMGNPKVKAHGKKVLGAFSDGLAHLDNLKGTFATLSELHCDKLHVDPE"
              + "NFRLLGNVLVCVLAHHFGKEFTPPVQAAYQKVVAGVANALAHKYH";
        IProtein seq = biojava.proteinFromString(proteinString);
        assertEquals(BiojavaProtein.class, seq.getClass());
        assertEquals(proteinString, seq.getPlainSequence());
        assertEquals("protein:'" + proteinString + "'",
                     seq.toString());
    }

    @Test
    public void DNAtoRNA() {
        IDNA dna = biojava.DNAfromString("CGTAGTCGTAGT");
        IRNA rna = biojava.DNAtoRNA(dna);
        assertEquals(BiojavaRNA.class, rna.getClass());
        assertEquals("CGUAGUCGUAGU", rna.getPlainSequence());
    }
}
