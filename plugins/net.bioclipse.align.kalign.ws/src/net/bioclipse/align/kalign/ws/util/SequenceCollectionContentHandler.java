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
package net.bioclipse.align.kalign.ws.util;

import java.util.List;

import net.bioclipse.biojava.business.Activator;
import net.bioclipse.biojava.business.IBiojavaManager;
import net.bioclipse.core.domain.ISequence;

import org.biojava.bio.seq.DNATools;
import org.biojava.bio.seq.ProteinTools;
import org.biojava.bio.seq.RNATools;
import org.biojava.bio.symbol.Alphabet;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * 
 * @author ola
 *
 */
public class SequenceCollectionContentHandler extends DefaultHandler {

//    private final Map sequenceMap;
    private final List<ISequence> sequences;
    private final Alphabet alphabet;

    private String currentSeqName;
    private String currentSeq;

    /**
     * Creates a new <code>SequenceAlignmentContentHandler</code> instance.
     *
     * @param sequences
     * The map to be filled with sequences
     * @param alphabet
     * The alphabet to be used
     */
    public SequenceCollectionContentHandler(List<ISequence> sequences, Alphabet alphabet) {
        this.sequences = sequences;
        this.alphabet = alphabet;
    }

    // This method is called when an element is encountered
    public final void startElement(String namespaceURI, String localName,
                                   String qName, Attributes atts) {

        if (localName.equals("Sequence")) {
            startCurrentSequence(atts);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.xml.sax.ContentHandler#characters(char[], int, int)
     */
    public final void characters(char[] ch, int start, int length)
    throws SAXException {
        String content = new String(ch, start, length);
        this.currentSeq = content;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.xml.sax.ContentHandler#endElement(java.lang.String,
     * java.lang.String, java.lang.String)
     */
    public final void endElement(String uri, String localName, String qName)
    throws SAXException {
        if (localName.equals("Sequence")) {
            endCurrentSequence();
        }

    }

    private void startCurrentSequence(Attributes atts) {
        String attName = atts.getLocalName(0);
        if (attName.equals("sequenceName")) {
            this.currentSeqName = atts.getValue(0);
        }
    }

    private void endCurrentSequence() {
        IBiojavaManager biojava
          = Activator.getDefault().getJavaBiojavaManager();
        if (this.alphabet.equals(DNATools.getDNA())) {
            sequences.add( biojava.DNAfromPlainSequence( currentSeq,
                                                         currentSeqName ));
        } else if (this.alphabet.equals(RNATools.getRNA())) {
            sequences.add( biojava.RNAfromPlainSequence( currentSeq,
                                                         currentSeqName ));
        } else if (this.alphabet.equals(ProteinTools.getAlphabet())) {
            sequences.add( biojava.proteinFromPlainSequence( currentSeq,
                                                             currentSeqName ));
        }
    }

}
