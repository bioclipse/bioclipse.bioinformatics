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
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import net.bioclipse.biojava.domain.BiojavaDNA;
import net.bioclipse.biojava.domain.BiojavaProtein;
import net.bioclipse.biojava.domain.BiojavaRNA;
import net.bioclipse.core.business.BioclipseException;
import net.bioclipse.core.domain.IDNA;
import net.bioclipse.core.domain.IProtein;
import net.bioclipse.core.domain.IRNA;
import net.bioclipse.core.domain.ISequence;
import net.bioclipse.core.domain.RecordableList;
import net.bioclipse.managers.business.IBioclipseManager;

import org.apache.log4j.Logger;
import org.biojava.bio.BioException;
import org.biojava.bio.seq.DNATools;
import org.biojava.bio.seq.ProteinTools;
import org.biojava.bio.seq.RNATools;
import org.biojava.bio.seq.db.HashSequenceDB;
import org.biojava.bio.seq.db.IllegalIDException;
import org.biojava.bio.seq.db.SequenceDB;
import org.biojava.bio.seq.io.SeqIOTools;
import org.biojava.bio.symbol.IllegalAlphabetException;
import org.biojava.bio.symbol.IllegalSymbolException;
import org.biojava.utils.ChangeVetoException;
import org.biojavax.Namespace;
import org.biojavax.RichObjectFactory;
import org.biojavax.bio.seq.RichSequence;
import org.biojavax.bio.seq.RichSequenceIterator;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
/**
 * Manager for BioJava. Performs the actual BioJava calls.
 * @author ola
 *
 */
@SuppressWarnings("deprecation")
public class BiojavaManager implements IBioclipseManager {

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

    public List<IDNA> DNAsFromString(String dnaString) {
        List<IDNA> dnas = new ArrayList<IDNA>();
        for (final ISequence seq : sequencesFromInputStream(
                new ByteArrayInputStream(dnaString.getBytes())))
            dnas.add(IDNA.class.cast(seq));
        return dnas;
    }

    public List<IRNA> RNAsFromString(String rnaString) {
        List<IRNA> rnas = new ArrayList<IRNA>();
        for (final ISequence seq : sequencesFromInputStream(
                new ByteArrayInputStream(rnaString.getBytes())))
            rnas.add(IRNA.class.cast(seq));
        return rnas;
    }

    public List<IProtein> proteinsFromString(String proteinString) {
        List<IProtein> proteins = new ArrayList<IProtein>();
        for (final ISequence seq : sequencesFromInputStream(
                new ByteArrayInputStream(proteinString.getBytes())))
            proteins.add(IProtein.class.cast(seq));
        return proteins;
    }

    public IDNA DNAfromPlainSequence(String dnaString) {
        return DNAfromPlainSequence(dnaString,
                                    "seq" + System.currentTimeMillis());
    }

    public IRNA RNAfromPlainSequence(String rnaString) {
        return RNAfromPlainSequence(rnaString,
                                    "seq" + System.currentTimeMillis());
    }

    public IProtein proteinFromPlainSequence(String proteinString) {
        return proteinFromPlainSequence(proteinString,
                                        "seq" + System.currentTimeMillis());
    }

    public IDNA DNAfromPlainSequence(String dnaString, String name) {
        try {
            return new BiojavaDNA(DNATools.createDNASequence(
                    dnaString,
                    name
            ));
        } catch (IllegalSymbolException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public IRNA RNAfromPlainSequence(String rnaString, String name) {
        try {
            return new BiojavaRNA(RNATools.createRNASequence(
                    rnaString,
                    name
            ));
        } catch (IllegalSymbolException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public IProtein proteinFromPlainSequence(String proteinString,
                                             String name) {
        try {
            return new BiojavaProtein(ProteinTools.createProteinSequence(
                    proteinString,
                    name
            ));
        } catch (IllegalSymbolException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public IProtein DNAtoProtein(IDNA dna) {
        return DNAtoProtein(dna, "");
    }

    public IProtein DNAtoProtein(IDNA dna, String name) {
        String plainSequence = dna.getPlainSequence();
        try {
            return proteinFromPlainSequence(
                    DNATools.toProtein(
                            DNATools.createDNASequence(plainSequence, name)
                    ).seqString()
                   );
        } catch (IllegalAlphabetException e) {
            throw new IllegalArgumentException(e);
        } catch (IllegalSymbolException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public List<IProtein> DNAtoProtein(List<IDNA> dnas) {

        List<IProtein> proteins = new RecordableList<IProtein>();
        try {
            for (IDNA dna : dnas){
                String plainSequence = dna.getPlainSequence();
                proteins.add(proteinFromPlainSequence(
                                   DNATools.toProtein(
                                   DNATools.createDNASequence(plainSequence, "")
                                   ).seqString()
                ));
            }
        } catch (IllegalAlphabetException e) {
            throw new IllegalArgumentException(e);
        } catch (IllegalSymbolException e) {
            throw new IllegalArgumentException(e);
        }

        return proteins;
    }


    public IRNA DNAtoRNA(IDNA dna) {
        return DNAtoRNA(dna, "");
    }

    public IRNA DNAtoRNA(IDNA dna, String name) {
        String plainSequence = dna.getPlainSequence();
        try {
            return RNAfromPlainSequence(
                    DNATools.toRNA(
                            DNATools.createDNASequence(plainSequence, name)
                    ).seqString()
                   );
        } catch (IllegalAlphabetException e) {
            throw new IllegalArgumentException(e);
        } catch (IllegalSymbolException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public IProtein RNAtoProtein(IRNA rna) {
        return RNAtoProtein(rna, "");
    }

    public IProtein RNAtoProtein(IRNA rna, String name) {
        String plainSequence = rna.getPlainSequence();
        try {
            return proteinFromPlainSequence(
                    RNATools.translate(
                            RNATools.createRNASequence(plainSequence, name)
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

    public List<IDNA> DNAsFromFile(IFile file)
        throws FileNotFoundException {

        List<IDNA> dnas = new ArrayList<IDNA>();
        for (final ISequence seq : sequencesFromFile(file))
            dnas.add(IDNA.class.cast(seq));
        return dnas;
    }

    public List<IRNA> RNAsFromFile(IFile file)
        throws FileNotFoundException {

        List<IRNA> rnas = new ArrayList<IRNA>();
        for (final ISequence seq : sequencesFromFile(file))
            rnas.add(IRNA.class.cast(seq));
        return rnas;
    }

    public List<IProtein> proteinsFromFile(IFile file)
        throws FileNotFoundException {

        List<IProtein> proteins = new ArrayList<IProtein>();
        for (final ISequence seq : sequencesFromFile(file))
            proteins.add(IProtein.class.cast(seq));
        return proteins;
    }

    public List<ISequence> sequencesFromFile( IFile file )
        throws FileNotFoundException {

        try {
            return sequencesFromInputStream(file.getContents());
        } catch (CoreException ce) {
            throw new FileNotFoundException(ce.toString());
        }
    }

    private List<ISequence> sequencesFromInputStream(InputStream stream) {

        BufferedInputStream bufferedStream = new BufferedInputStream(stream);
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

    private SequenceDB addToSequenceDB(
        final List<? extends ISequence> sequences
    ) {

        SequenceDB db = new HashSequenceDB();

        try {
            for (ISequence seq : sequences){
                if ( seq instanceof IProtein ) {
                    IProtein protein = (IProtein) seq;
                    db.addSequence(ProteinTools.createProteinSequence(
                            protein.getPlainSequence(),
                            protein.getName()));
                }
                else if ( seq instanceof IDNA ) {
                    IDNA dna = (IDNA) seq;
                        db.addSequence(DNATools.createDNASequence(
                                // the .replaceAll works around bug #1569
                                dna.getPlainSequence().replace('~', '-'),
                                dna.getName()));
                        logger.debug("Added DNA sequence: " + dna.getName()
                                + " to write.");
                }
                else {
                    throw new IllegalArgumentException(
                        "Only IProtein and IDNA allowed in list of sequences"
                    );
                }
                logger.debug("Added "+ seq.getClass().getName() +" sequence: "
                             + seq.getName() + " to write.");
            }
        } catch ( IllegalIDException e ) {
            throw new IllegalArgumentException(e);
        } catch ( ChangeVetoException e ) {
            throw new IllegalArgumentException(e);
        } catch ( IllegalSymbolException e ) {
            throw new IllegalArgumentException(e);
        } catch ( BioException e ) {
            throw new IllegalArgumentException(e);
        }
        return db;
    }

    public void sequencesToFASTAfile(final List<? extends ISequence> sequences,
                                     final IFile file,
                                     IProgressMonitor monitor)
                                     throws BioclipseException{

        try {
            //Write to byte[]
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            SeqIOTools.writeFasta(bos, addToSequenceDB(sequences));

            //Write byte[] as new content to file
            ByteArrayInputStream bis=new ByteArrayInputStream(bos.toByteArray());
            file.setContents( bis, false, false, monitor );

        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException(e);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        } catch ( CoreException e ) {
            throw new BioclipseException(
                    "Error saving file: " + e.getMessage()
            );
        }
    }

    public String sequencesToFASTAString(
        final List<? extends ISequence> sequences
    ) {

        try {
            //Write to byte[]
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            SeqIOTools.writeFasta(bos, addToSequenceDB(sequences));

            return bos.toString();

        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException(e);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public RecordableList<ISequence> createSequenceList()
                                                    throws BioclipseException,
                                                    InvocationTargetException {
        return new RecordableList<ISequence>();
    }


    public String proteinsToFASTAString(final List<IProtein> proteins){

        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            SeqIOTools.writeFasta(bos, addToSequenceDB(proteins));

            return bos.toString();

        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException(e);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public String dnaToFASTAString(final List<IDNA> dnas){

        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            SeqIOTools.writeFasta(bos, addToSequenceDB(dnas));

            return bos.toString();

        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException(e);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public void proteinsToFASTAfile(List<IProtein> proteins, IFile file) {

        try {
            SeqIOTools.writeFasta(
                new FileOutputStream(file.getFullPath().toFile()),
                addToSequenceDB(proteins)
            );
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException(e);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public List<IRNA> DNAtoRNA(List<IDNA> dnas) {
        List<IRNA> rnas = new ArrayList<IRNA>();
        for (final IDNA dna : dnas)
            rnas.add(DNAtoRNA(dna));
        return rnas;
    }

    public List<IProtein> RNAtoProtein(List<IRNA> rnas) {
        List<IProtein> proteins = new ArrayList<IProtein>();
        for (final IRNA rna : rnas)
            proteins.add(RNAtoProtein(rna));
        return proteins;
    }
}
