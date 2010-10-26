/* *****************************************************************************
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
import net.bioclipse.core.api.domain.IDNA;
import net.bioclipse.core.api.domain.IProtein;
import net.bioclipse.core.api.domain.IRNA;

import org.junit.Test;

/**
 * @author masak
 *
 */
public class BiojavaManagerTest {

    private BiojavaManager biojava = new BiojavaManager();

    @Test
    public void createDNA() {
        String dnaString = "CGTAGTCGTAGT";
        IDNA seq = biojava.DNAfromPlainSequence(dnaString);
        assertEquals(BiojavaDNA.class, seq.getClass());
        assertEquals(dnaString.toLowerCase(), seq.getPlainSequence());
        assertEquals(
            "DNA " + seq.getName() + ": '" + dnaString.toLowerCase() +"'",
            seq.toString()
        );
    }

    @Test
    public void createRNA() {
        String rnaString = "UUUACGUGACCC";
        IRNA seq = biojava.RNAfromPlainSequence(rnaString);
        assertEquals(BiojavaRNA.class, seq.getClass());
        assertEquals(rnaString.toLowerCase(), seq.getPlainSequence());
        assertEquals(
            "RNA " + seq.getName() + ": '" + rnaString.toLowerCase() +"'",
            seq.toString()
        );
    }

    @Test
    public void createProtein() {
        String proteinString // the hemoglobin beta2 chain, incidentally
            = "MVHLTPEEKSAVTALWGKVNVDEVGGEALGRLLVVYPWTQRFFE"
              + "SFGDLSTPDAVMGNPKVKAHGKKVLGAFSDGLAHLDNLKGTFATLSELHCDKLHVDPE"
              + "NFRLLGNVLVCVLAHHFGKEFTPPVQAAYQKVVAGVANALAHKYH";
        IProtein seq = biojava.proteinFromPlainSequence(proteinString);
        assertEquals(BiojavaProtein.class, seq.getClass());
        assertEquals(proteinString, seq.getPlainSequence());
        assertEquals(
            "Protein " + seq.getName() + ": '"
                       + proteinString.toLowerCase() +"'",
            seq.toString()
        );
    }

    @Test
    public void createFromFASTAstring() {
        String dnaString = ">test DNA String\nCGTAGTCGTAGT";
        IDNA dnaSeq = biojava.DNAsFromString(dnaString).get(0);
        assertEquals("cgtagtcgtagt", dnaSeq.getPlainSequence());
        String rnaString = ">test RNA String\nUUUACGUGACCC";
        IRNA rnaSeq = biojava.RNAsFromString(rnaString).get(0);
        assertEquals("uuuacgugaccc", rnaSeq.getPlainSequence());
        String proteinString
            = ">test Protein String\n"
              + "MVHLTPEEKSAVTALWGKVNVDEVGGEALGRLLVVYPWTQRFFE"
              + "SFGDLSTPDAVMGNPKVKAHGKKVLGAFSDGLAHLDNLKGTFATLSELHCDKLHVDPE"
              + "NFRLLGNVLVCVLAHHFGKEFTPPVQAAYQKVVAGVANALAHKYH";
        IProtein proteinSeq = biojava.proteinsFromString(proteinString).get(0);
        assertEquals(
            "MVHLTPEEKSAVTALWGKVNVDEVGGEALGRLLVVYPWTQRFFE"
            + "SFGDLSTPDAVMGNPKVKAHGKKVLGAFSDGLAHLDNLKGTFATLSELHCDKLHVDPE"
            + "NFRLLGNVLVCVLAHHFGKEFTPPVQAAYQKVVAGVANALAHKYH",
            proteinSeq.getPlainSequence());
    }

    @Test
    public void DNAtoRNA() {
        IDNA dna = biojava.DNAfromPlainSequence("CGTAGTCGTAGT");
        IRNA rna = biojava.DNAtoRNA(dna);
        assertEquals(BiojavaRNA.class, rna.getClass());
        assertEquals("cguagucguagu", rna.getPlainSequence());
    }

    @Test
    public void DNAtoProtein() {
        IDNA dna = biojava.DNAfromPlainSequence("CGTAGTCGTAGT");
        IProtein protein = biojava.DNAtoProtein(dna);
        assertEquals(BiojavaProtein.class, protein.getClass());
        assertEquals("RSRS", protein.getPlainSequence());
    }

    @Test
    public void RNAtoProtein() {
        IRNA rna = biojava.RNAfromPlainSequence("UUUACGUGACCC");
        IProtein protein = biojava.RNAtoProtein(rna);
        assertEquals(BiojavaProtein.class, protein.getClass());
        assertEquals("FT*P", protein.getPlainSequence());
    }
}
