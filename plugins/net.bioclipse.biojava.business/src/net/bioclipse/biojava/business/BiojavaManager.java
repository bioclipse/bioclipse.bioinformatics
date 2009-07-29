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
/*******************************************************************************
 * Copyright (c) 2009 Jonathan Alvarsson <jonalv@users.sourceforge.net>
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * <http://www.eclipse.org/legal/epl-v10.html>
 *
 * Contact: http://www.bioclipse.net/
 ******************************************************************************/

package net.bioclipse.biojava.business;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import net.bioclipse.biojava.domain.BiojavaDNA;
import net.bioclipse.biojava.domain.BiojavaProtein;
import net.bioclipse.biojava.domain.BiojavaRNA;
import net.bioclipse.core.domain.IDNA;
import net.bioclipse.core.domain.IProtein;
import net.bioclipse.core.domain.IRNA;
import net.bioclipse.core.domain.ISequence;

import org.apache.log4j.Logger;
import org.biojava.bio.BioException;
import org.biojava.bio.seq.DNATools;
import org.biojava.bio.seq.ProteinTools;
import org.biojava.bio.seq.RNATools;
import org.biojava.bio.symbol.IllegalAlphabetException;
import org.biojava.bio.symbol.IllegalSymbolException;
import org.biojavax.Namespace;
import org.biojavax.RichObjectFactory;
import org.biojavax.bio.seq.RichSequence;
import org.biojavax.bio.seq.RichSequenceIterator;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
/**
 * Manager for BioJava. Performs the actual BioJava calls.
 * @author ola
 *
 */
public class BiojavaManager implements IBiojavaManager {

    private static final Logger logger = Logger.getLogger(BiojavaManager.class);

    private static final String[] FormatReaderClassNames = {
        "org.biojavax.bio.seq.io.EMBLFormat",
        "org.biojavax.bio.seq.io.FastaFormat",
        "org.biojavax.bio.seq.io.GenbankFormat",
        "org.biojavax.bio.seq.io.INSDseqFormat",
        "org.biojavax.bio.seq.io.EMBLxmlFormat",
        "org.biojavax.bio.seq.io.UniProtFormat",
        "org.biojavax.bio.seq.io.UniProtXMLFormat",
        "org.biojavax.bio.seq.io.RichSequenceFormat"
    };

    public BiojavaManager() {
        // Introduce the allowed formats

        for (String name : FormatReaderClassNames) {
           try {
               logger.debug("Loading format reader: " + name);
               Class.forName(name);
           }
           catch(ClassNotFoundException e) {
               logger.error("Class " + name + "not found: " + e);
           }
        }
    }

    public IDNA DNAfromString(String dnaString) {
        if (dnaString.startsWith(">")) {
            dnaString = StringUtils.withoutFirstLine(dnaString);
        }
        try {
            return new BiojavaDNA(DNATools.createDNASequence(
                    dnaString,
                    "seq" + System.currentTimeMillis()
            ));
        } catch (IllegalSymbolException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public IRNA RNAfromString(String rnaString) {
        if (rnaString.startsWith(">")) {
            rnaString = StringUtils.withoutFirstLine(rnaString);
        }
        try {
            return new BiojavaRNA(RNATools.createRNASequence(
                    rnaString,
                    "seq" + System.currentTimeMillis()
            ));
        } catch (IllegalSymbolException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public IProtein proteinFromString(String proteinString) {
        if (proteinString.startsWith(">")) {
            proteinString = StringUtils.withoutFirstLine(proteinString);
        }
        try {
            return new BiojavaProtein(ProteinTools.createProteinSequence(
                    proteinString,
                    "seq" + System.currentTimeMillis()
            ));
        } catch (IllegalSymbolException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public IProtein DNAtoProtein(IDNA dna) {
        String plainSequence = dna.getPlainSequence();
        try {
            return proteinFromString(
                    DNATools.toProtein(
                            DNATools.createDNASequence(plainSequence, "")
                    ).seqString()
                   );
        } catch (IllegalAlphabetException e) {
            throw new IllegalArgumentException(e);
        } catch (IllegalSymbolException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public IRNA DNAtoRNA(IDNA dna) {
        String plainSequence = dna.getPlainSequence();
        try {
            return RNAfromString(
                    DNATools.toRNA(
                            DNATools.createDNASequence(plainSequence, "")
                    ).seqString()
                   );
        } catch (IllegalAlphabetException e) {
            throw new IllegalArgumentException(e);
        } catch (IllegalSymbolException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public IDNA ProteinToDNA(IProtein protein) {
        return null;
    }

    public IRNA ProteinToRNA(IProtein protein) {
        return null;
    }

    public IDNA RNAtoDNA(IRNA rna) {
        return null;
    }

    public IProtein RNAtoProtein(IRNA rna) {
        String plainSequence = rna.getPlainSequence();
        try {
            return proteinFromString(
                    RNATools.translate(
                            RNATools.createRNASequence(plainSequence, "")
                    ).seqString()
                   );
        } catch (IllegalAlphabetException e) {
            throw new IllegalArgumentException(e);
        } catch (IllegalSymbolException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public String getManagerName() {
        return "biojava";
    }

    public List<IDNA> DNAsFromFile( String path ) throws FileNotFoundException {
        //TODO: Change to latest world order and remove this method
        throw new IllegalStateException("This method should not be called");
    }

    public List<IDNA> DNAsFromFile(IFile file)
        throws FileNotFoundException {

        List<IDNA> dnas = new ArrayList<IDNA>();
        for (final ISequence seq : sequencesFromFile(file))
            dnas.add(IDNA.class.cast(seq));
        return dnas;
    }

    public List<IRNA> RNAsFromFile( String path ) throws FileNotFoundException {
        //TODO: Change to latest world order and remove this method
        throw new IllegalStateException("This method should not be called");
    }

    public List<IRNA> RNAsFromFile(IFile file)
        throws FileNotFoundException {

        List<IRNA> rnas = new ArrayList<IRNA>();
        for (final ISequence seq : sequencesFromFile(file))
            rnas.add(IRNA.class.cast(seq));
        return rnas;
    }

    public List<IProtein> proteinsFromFile( String path )
        throws FileNotFoundException {

        //TODO: Change to latest world order and remove this method
        throw new IllegalStateException("This method should not be called");
    }

    public List<IProtein> proteinsFromFile(IFile file)
        throws FileNotFoundException {

        List<IProtein> proteins = new ArrayList<IProtein>();
        for (final ISequence seq : sequencesFromFile(file))
            proteins.add(IProtein.class.cast(seq));
        return proteins;
    }

    public List<ISequence> sequencesFromFile( String string ) {
        //TODO: Change to latest world order and remove this method
        throw new IllegalStateException("This method should not be called");
    }

    public List<ISequence> sequencesFromFile( IFile file )
        throws FileNotFoundException {
        
        BufferedInputStream bufferedStream;
        try {
            bufferedStream = new BufferedInputStream( file.getContents() );
        } catch (CoreException ce) {
            throw new FileNotFoundException(ce.toString());
        }

        Namespace ns = RichObjectFactory.getDefaultNamespace();
        RichSequenceIterator seqit = null;

        try {
            seqit = RichSequence.IOTools.readStream(bufferedStream, ns);
        } catch (IOException e) {
            logger.error("Couldn't read sequences from file", e);
            return Collections.emptyList();
        }

        List<ISequence> sequences = new ArrayList<ISequence>();
        try {
            while ( seqit.hasNext() ) {
                RichSequence rseq;
                    rseq = seqit.nextRichSequence();
                if (rseq == null)
                    continue;
                String alphabet = rseq.getAlphabet().getName();
                sequences.add(
                      "DNA".equals(alphabet) ? new BiojavaDNA(rseq)
                    : "RNA".equals(alphabet) ? new BiojavaRNA(rseq)
                    :                          new BiojavaProtein(rseq) );
            }
        } catch (NoSuchElementException e) {
            logger.error("Read past last sequence", e);
        } catch (BioException e) {
            logger.error(e);
        }

        return sequences;
    }
}
