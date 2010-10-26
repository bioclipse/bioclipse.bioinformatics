 /* *****************************************************************************
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

import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;

import net.bioclipse.biojava.domain.IFeature;
import net.bioclipse.core.api.BioclipseException;
import net.bioclipse.core.api.domain.IDNA;
import net.bioclipse.core.api.domain.IProtein;
import net.bioclipse.core.api.domain.IRNA;
import net.bioclipse.core.api.domain.ISequence;
import net.bioclipse.core.api.domain.RecordableList;
import net.bioclipse.core.api.managers.IBioclipseManager;
import net.bioclipse.core.api.managers.PublishedClass;
import net.bioclipse.core.api.managers.PublishedMethod;

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
     * <code>String</code>. The <code>String</code> should contain only the
     * sequence and no metadata.
     *
     * @param dnaString A DNA sequence to be converted.
     * @return A new <code>BiojavaDNA</code>.
     */
    @PublishedMethod(
        methodSummary = "Returns a new BiojavaDNA sequence from the given " +
                        "String.",
        params = "String dnaString" )
    public IDNA DNAfromPlainSequence(String dnaString);

    /**
     * Returns a new <code>BiojavaRNA</code> sequence from the given
     * <code>String</code>. The <code>String</code> should contain only the
     * sequence and no metadata.
     *
     * @param rnaString An RNA sequence to be converted.
     * @return A new <code>BiojavaRNA</code>.
     */
    @PublishedMethod(
        methodSummary = "Returns a new BiojavaRNA sequence from the given " +
                        "String.",
        params = "String rnaString" )
    public IRNA RNAfromPlainSequence(String rnaString);

    /**
     * Returns a new <code>BiojavaProtein</code> sequence from the given
     * <code>String</code>. The <code>String</code> should contain only the
     * sequence and no metadata.
     *
     * @param proteinString An amino acid sequence to be converted.
     * @return A new <code>BiojavaProtein</code>.
     */
    @PublishedMethod(
        methodSummary = "Returns a new BiojavaProtein sequence from the given "
                        + "String.",
        params = "String proteinString" )
    public IProtein proteinFromPlainSequence(String proteinString);

    /**
     * Returns a new <code>BiojavaDNA</code> sequence from the given
     * <code>String</code>. The <code>String</code> should contain only the
     * sequence and no metadata.
     *
     * @param dnaString A DNA sequence to be converted.
     * @return A new <code>BiojavaDNA</code>.
     */
    @PublishedMethod(
        methodSummary = "Returns a new BiojavaDNA sequence from the given " +
                        "String.",
        params = "String dnaString, String name" )
    public IDNA DNAfromPlainSequence(String dnaString, String name);

    /**
     * Returns a new <code>BiojavaRNA</code> sequence from the given
     * <code>String</code>. The <code>String</code> should contain only the
     * sequence and no metadata.
     *
     * @param rnaString An RNA sequence to be converted.
     * @return A new <code>BiojavaRNA</code>.
     */
    @PublishedMethod(
        methodSummary = "Returns a new BiojavaRNA sequence from the given " +
                        "String.",
        params = "String rnaString, String name" )
    public IRNA RNAfromPlainSequence(String rnaString, String name);

    /**
     * Returns a new <code>BiojavaProtein</code> sequence from the given
     * <code>String</code>. The <code>String</code> should contain only the
     * sequence and no metadata.
     *
     * @param proteinString An amino acid sequence to be converted.
     * @return A new <code>BiojavaProtein</code>.
     */
    @PublishedMethod(
        methodSummary = "Returns a new BiojavaProtein sequence from the given "
                        + "String.",
        params = "String proteinString, String name" )
    public IProtein proteinFromPlainSequence(String proteinString, String name);

    /**
     * Returns a new <code>BiojavaDNA</code> sequence from the given
     * <code>String</code>. The string should be in a number of sequence
     * data formats, and will in general be treated as if read from a file.
     *
     * @param dnaString A DNA sequence to be converted.
     * @return A new <code>BiojavaDNA</code>.
     */
    @PublishedMethod(
        methodSummary = "Returns a new BiojavaDNA sequence from the given " +
                        "String.",
        params = "String dnaString" )
    public List<IDNA> DNAsFromString(String dnaString);

    /**
     * Returns a new <code>BiojavaRNA</code> sequence from the given
     * <code>String</code>. The string should be in a number of sequence
     * data formats, and will in general be treated as if read from a file.
     *
     * @param rnaString An RNA sequence to be converted.
     * @return A new <code>BiojavaRNA</code>.
     */
    @PublishedMethod(
        methodSummary = "Returns a new BiojavaRNA sequence from the given " +
                        "String.",
        params = "String rnaString" )
    public List<IRNA> RNAsFromString(String rnaString);

    /**
     * Returns a new <code>BiojavaProtein</code> sequence from the given
     * <code>String</code>. The string should be in a number of sequence
     * data formats, and will in general be treated as if read from a file.
     *
     * @param proteinString An amino acid sequence to be converted.
     * @return A new <code>BiojavaProtein</code>.
     */
    @PublishedMethod(
        methodSummary = "Returns a new BiojavaProtein sequence from the given "
                        + "String.",
        params = "String proteinString" )
    public List<IProtein> proteinsFromString(String proteinString);

    /**
     * Returns the <code>IRNA</code> sequence equivalent of the given
     * <code>IDNA</code> sequence. The conversion process is called
     * <em>transcription</em>.
     *
     * @param dna the sequence to be converted
     * @return the result of the conversion
     */
    @PublishedMethod(
        methodSummary = "Returns the RNA sequence equivalent of the given DNA "
                        + "sequence.",
        params = "IDNA dna" )
    public IRNA DNAtoRNA(IDNA dna);

    /**
     * Returns the <code>IRNA</code> sequence equivalents of the given
     * list of <code>IDNA</code> sequences. The conversion process is called
     * <em>transcription</em>.
     *
     * @param dnas the list of sequences to be converted
     * @return the result of the conversion
     */
    @PublishedMethod(
        methodSummary = "Returns a list of RNA sequence equivalents of the "
                        +"given list of DNA sequences.",
        params = "List<IDNA> dnas" )
    public List<IRNA> DNAtoRNA( List<IDNA> dnas );

    /**
     * Returns the <code>IProtein</code> sequence equivalent of the given
     * <code>IDNA</code> sequence. The conversion processes involved are
     * <em>transcription</em> followed by <em>translation</em>. The standard
     * genetic code is used.
     *
     * @param dna the sequence to be converted
     * @return the result of the conversion
     */
    @PublishedMethod(
        methodSummary = "Returns the protein sequence equivalent of the " +
                        "given DNA sequence.",
        params = "IDNA dna" )
    public IProtein DNAtoProtein(IDNA dna);

    /**
     * Returns a list of <code>IProtein</code> sequence equivalents of the given
     * list of <code>IDNA</code> sequences. The conversion processes involved
     * are <em>transcription</em> followed by <em>translation</em>. The standard
     * genetic code is used.
     *
     * @param dnas the list of sequences to be converted
     * @return the result of the conversion
     */
    @PublishedMethod(
      methodSummary = "Returns a list of protein sequence equivalents of the "
                      + "given list of DNA sequences.",
      params = "List<IDNA> dnas" )
    public List<IProtein> DNAtoProtein( List<IDNA> dnas );

    /**
     * Returns the <code>IProtein</code> sequence equivalent of the given
     * <code>IRNA</code> sequence. The conversion process is called
     * <em>translation</em>. The standard genetic code is used.
     *
     * @param rna the sequence to be converted
     * @return the result of the conversion
     */
    @PublishedMethod(
        methodSummary = "Returns the protein sequence equivalent of the " +
                        "given RNA sequence.",
        params = "IRNA rna" )
    public IProtein RNAtoProtein(IRNA rna);

    /**
     * Returns the <code>IProtein</code> sequence equivalents of the given
     * list of <code>IRNA</code> sequences. The conversion process is called
     * <em>translation</em>. The standard genetic code is used.
     *
     * @param rnas the sequence to be converted
     * @return the result of the conversion
     */
    @PublishedMethod(
        methodSummary = "Returns the protein sequence equivalents of the " +
                        "given list of RNA sequence.",
        params = "List<IRNA> rnas" )
    public List<IProtein> RNAtoProtein( List<IRNA> rnas );

    /**
     * Returns the <code>IRNA</code> sequence equivalent of the given
     * <code>IDNA</code> sequence. The conversion process is called
     * <em>transcription</em>.
     *
     * @param dna  the sequence to be converted
     * @param name a description of the sequence
     * @return     the result of the conversion
     */
    @PublishedMethod(
        methodSummary = "Returns the RNA sequence equivalent of the given DNA "
                        + "sequence.",
        params = "IDNA dna, String name)" )
    public IRNA DNAtoRNA(IDNA dna, String name);

    /**
     * Returns the <code>IProtein</code> sequence equivalent of the given
     * <code>IDNA</code> sequence. The conversion processes involved are
     * <em>transcription</em> followed by <em>translation</em>. The standard
     * genetic code is used.
     *
     * @param dna the sequence to be converted
     * @param name a description of the sequence
     * @return     the result of the conversion
     */
    @PublishedMethod(
        methodSummary = "Returns the protein sequence equivalent of the " +
                        "given DNA sequence.",
        params = "IDNA dna, String name)" )
    public IProtein DNAtoProtein(IDNA dna, String name);

    /**
     * Returns the <code>IProtein</code> sequence equivalent of the given
     * <code>IRNA</code> sequence. The conversion process is called
     * <em>translation</em>. The standard genetic code is used.
     *
     * @param rna the sequence to be converted
     * @param name a description of the sequence
     * @return     the result of the conversion
     */
    @PublishedMethod(
        methodSummary = "Returns the protein sequence equivalent of the " +
                        "given RNA sequence.",
        params = "IRNA rna, String name)" )
    public IProtein RNAtoProtein(IRNA rna, String name);

    /**
     * Returns the <code>IRNA</code> sequence equivalent of the given
     * <code>IDNA</code> sequence. The conversion process is called
     * <em>transcription</em>.
     *
     * @param dna the sequence to be converted
     * @return the result of the conversion
     */
    @PublishedMethod(
        methodSummary = "Returns the RNA sequence equivalent of the given DNA "
                        + "sequence.",
        params = "IDNA dna" )
    public IRNA transcriptionOf(IDNA dna);

    /**
     * Returns the <code>IRNA</code> sequence equivalent of the given
     * <code>IDNA</code> sequence. The conversion process is called
     * <em>transcription</em>.
     *
     * @param dna  the sequence to be converted
     * @param name a description of the sequence
     * @return     the result of the conversion
     */
    @PublishedMethod(
        methodSummary = "Returns the RNA sequence equivalent of the given DNA "
                        + "sequence.",
        params = "IDNA dna, String name)" )
    public IRNA transcriptionOf(IDNA dna, String name);

    /**
     * Returns the <code>IRNA</code> sequence equivalents of the given
     * list of <code>IDNA</code> sequences. The conversion process is called
     * <em>transcription</em>.
     *
     * @param dnas the list of sequences to be converted
     * @return the result of the conversion
     */
    @PublishedMethod(
        methodSummary = "Returns a list of RNA sequence equivalents of the "
                        +"given list of DNA sequences.",
        params = "List<IDNA> dnas" )
    public List<IRNA> transcriptionOf( List<IDNA> dnas );

    /**
     * Returns the <code>IProtein</code> sequence equivalent of the given
     * <code>IRNA</code> sequence. The conversion process is called
     * <em>translation</em>. The standard genetic code is used.
     *
     * @param rna the sequence to be converted
     * @return the result of the conversion
     */
    @PublishedMethod(
        methodSummary = "Returns the protein sequence equivalent of the " +
                        "given RNA sequence.",
        params = "IRNA rna" )
    public IProtein translationOf(IRNA rna);

    /**
     * Returns the <code>IProtein</code> sequence equivalent of the given
     * <code>IRNA</code> sequence. The conversion process is called
     * <em>translation</em>. The standard genetic code is used.
     *
     * @param rna the sequence to be converted
     * @param name a description of the sequence
     * @return     the result of the conversion
     */
    @PublishedMethod(
        methodSummary = "Returns the protein sequence equivalent of the " +
                        "given RNA sequence.",
        params = "IRNA rna, String name)" )
    public IProtein translationOf(IRNA rna, String name);

    /**
     * Returns the <code>IProtein</code> sequence equivalents of the given
     * list of <code>IRNA</code> sequences. The conversion process is called
     * <em>translation</em>. The standard genetic code is used.
     *
     * @param rnas the sequence to be converted
     * @return the result of the conversion
     */
    @PublishedMethod(
        methodSummary = "Returns the protein sequence equivalents of the " +
                        "given list of RNA sequence.",
        params = "List<IRNA> rnas" )
    public List<IProtein> translationOf( List<IRNA> rnas );

    @PublishedMethod(
            methodSummary = "Returns an array of DNA sequences from the "
                            + "given file.",
            params = "String path" )
        public List<IDNA> DNAsFromFile(String path)
                              throws FileNotFoundException;

        public List<IDNA> DNAsFromFile(IFile file)
                              throws FileNotFoundException;

    @PublishedMethod(
            methodSummary = "Returns an array of RNA sequences from the "
                            + "given file.",
            params = "String path" )
        public List<IRNA> RNAsFromFile(String path)
                              throws FileNotFoundException;

        public List<IRNA> RNAsFromFile(IFile file)
                              throws FileNotFoundException;

    @PublishedMethod(
        methodSummary = "Returns an array of proteins from the given file.",
        params = "String path" )
    public List<IProtein> proteinsFromFile(String path)
                              throws FileNotFoundException;

    public List<IProtein> proteinsFromFile(IFile file)
                              throws FileNotFoundException;

    @PublishedMethod(
        methodSummary = "Loads sequences from file at path",
        params = "String path" )
    public List<ISequence> sequencesFromFile( String path );

    public List<ISequence> sequencesFromFile( IFile file )
        throws FileNotFoundException;

    @PublishedMethod(
        methodSummary = "Saves an array of proteins to a FASTA file.",
        params = "String path" )
    public void proteinsToFASTAfile(List<IProtein> proteins, String path);

    public void proteinsToFASTAfile(List<IProtein> proteins, IFile file);

    @PublishedMethod(
                 methodSummary = "Saves an array of sequences to a FASTA file.",
                 params = "String path" )
    public void sequencesToFASTAfile( List<? extends ISequence> sequences,
                                      String path );
    public void sequencesToFASTAfile( List<? extends ISequence> sequences,
                                      IFile file,
                                      IProgressMonitor monitor )
                                      throws BioclipseException;

    @PublishedMethod(
           methodSummary = "Serialize a list of sequences to a FASTA String.",
           params = "List<ISequence> sequences" )
    public String sequencesToFASTAString( List<? extends ISequence> sequences );

    @PublishedMethod(
                     methodSummary = "Serialize a list of DNA sequences " +
                             "to a FASTA String.",
                     params = "List<IDNA> dnas" )
    public String dnaToFASTAString( List<IDNA> dnas );

    @PublishedMethod(
                     methodSummary = "Serialize a list of protein sequences " +
                        "to a FASTA String.",
                     params = "List<IProtein> proteins" )
    public String proteinsToFASTAString( List<IProtein> proteins );

    @PublishedMethod(methodSummary = "Create an empty list of Sequences")
    public RecordableList<ISequence> createSequenceList()
        throws BioclipseException, InvocationTargetException;

    @PublishedMethod(methodSummary = "Return a list of features for a Sequence",
                     params = "ISequence sequence")
    public List<IFeature> features(ISequence sequence);

    @PublishedMethod(methodSummary = "Creates sequences from a String",
                     params = "String s")
    public List<ISequence> sequencesFromString(String s);
}
