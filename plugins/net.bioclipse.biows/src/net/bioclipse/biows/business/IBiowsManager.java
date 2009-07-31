/*******************************************************************************
 * Copyright (c) 2009 Ola Spjuth.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Ola Spjuth - initial API and implementation
 ******************************************************************************/
package net.bioclipse.biows.business;

import java.util.List;

import net.bioclipse.core.PublishedClass;
import net.bioclipse.core.PublishedMethod;
import net.bioclipse.core.Recorded;
import net.bioclipse.core.business.BioclipseException;
import net.bioclipse.core.domain.IDNA;
import net.bioclipse.core.domain.IProtein;
import net.bioclipse.managers.business.IBioclipseManager;

@PublishedClass(value = "Controls access to Bioinformatics Webservices.")
/**
 * 
 * @author ola
 *
 */
public interface IBiowsManager extends IBioclipseManager {

    @Recorded
    @PublishedMethod(params="String seqid",
                     methodSummary="Download DNA sequences in FASTA format " +
                     "from the refseq " +
                     "database at EBI. " +
                     "Seqid should be RefSeq ID separated by comma OR space, " +
                     "e.g. NM_")
    public List<IDNA> queryRefseq(String seqid) throws BioclipseException;

    @Recorded
    @PublishedMethod(params="String seqid",
                     methodSummary="Download DNA sequences in FASTA format " +
                        "from the EMBL nucleotide sequence " +
                        "database at EBI. " +
                        "Seqid can be either accession number (e.g. M10051) or " +
                        "entry name (e.g. HSINSR). ")
    public List<IDNA> queryEMBL(String seqid) throws BioclipseException;

    @Recorded
    @PublishedMethod(params="String seqid",
                     methodSummary="Download DNA sequences in FASTA format " +
                        "from the refseq " +
                        "database at EBI. " +
                        "Entries can be retrieved by entry name " +
                        "(e.g. INSR_HUMAN) or by accession number " +
                        "(e.g. P06213). ")
    public List<IProtein> queryUniProtKB(String seqid) throws BioclipseException;



    
}
