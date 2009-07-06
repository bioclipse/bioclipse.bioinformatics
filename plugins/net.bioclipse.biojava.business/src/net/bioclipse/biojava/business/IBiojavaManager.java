 /*******************************************************************************
 * Copyright (c) 2007-2008 The Bioclipse Project and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * www.eclipse.org—epl-v10.html <http://www.eclipse.org/legal/epl-v10.html>
 *
 * Contributors:
 *     Jonathan Alvarsson
 *     Ola Spjuth
 *
 ******************************************************************************/

package net.bioclipse.biojava.business;

import net.bioclipse.core.PublishedClass;
import net.bioclipse.core.PublishedMethod;
import net.bioclipse.core.domain.IDNA;
import net.bioclipse.core.domain.IProtein;
import net.bioclipse.core.domain.IRNA;
import net.bioclipse.managers.business.IBioclipseManager;

/**
 * @author jonalv, ola
 */
@PublishedClass("Provides bioinformatics services through the BioJava project.")
public interface IBiojavaManager extends IBioclipseManager {

    /**
     * Sequence formats supported by the <code>BiojavaManager</code>.
     *
     * @author jonalv
     *
     */
    public enum SequenceFormat {
        FASTA,
        EMBL,
        GENBANK,
        UNIPROT;
    }

    /**
     * Returns a new <code>BiojavaDNA</code> sequence from the given
     * <code>String</code>.
     *
     * @param dnaString A DNA sequence to be converted.
     * @return A new <code>BiojavaDNA</code>.
     */
    @PublishedMethod(methodSummary="Returns a new BiojavaDNA sequence from "
                                   + "the given String.")
    public IDNA DNAfromString(String dnaString);

    /**
     * Returns a new <code>BiojavaRNA</code> sequence from the given
     * <code>String</code>.
     *
     * @param rnaString An RNA sequence to be converted.
     * @return A new <code>BiojavaRNA</code>.
     */
    @PublishedMethod(methodSummary="Returns a new BiojavaRNA sequence from "
                                   + "the given String.")
    public IRNA RNAfromString(String rnaString);

    /**
     * Returns a new <code>BiojavaProtein</code> sequence from the given
     * <code>String</code>.
     *
     * @param proteinString An amino acid sequence to be converted.
     * @return A new <code>BiojavaProtein</code>.
     */
    @PublishedMethod(methodSummary="Returns a new BiojavaProtein sequence from "
                                   + "the given String.")
    public IProtein ProteinFromString(String proteinString);
}
