package net.bioclipse.align.kalign.ws.util;

import java.util.Map;

import org.biojava.bio.seq.DNATools;
import org.biojava.bio.seq.ProteinTools;
import org.biojava.bio.seq.RNATools;
import org.biojava.bio.seq.Sequence;
import org.biojava.bio.symbol.Alphabet;
import org.biojava.bio.symbol.IllegalSymbolException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class SequenceCollectionContentHandler extends DefaultHandler {

    private final Map sequenceMap;
    private final Alphabet alphabet;

    private String currentSeqName;
    private String currentSeq;

    /**
     * Creates a new <code>SequenceAlignmentContentHandler</code> instance.
     *
     * @param map
     * The map to be filled with sequences
     * @param alphabet
     * The alphabet to be used
     */
    public SequenceCollectionContentHandler(Map map, Alphabet alphabet) {
        this.sequenceMap = map;
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
        if (this.alphabet.equals(DNATools.getDNA())) {
            try {
                Sequence seq = DNATools.createDNASequence(currentSeq,
                                                          currentSeqName);
                this.sequenceMap.put(currentSeqName, seq);
            } catch (IllegalSymbolException e) {
                System.err.println(this.getClass()
                                   + " - IllegalSymbolException: " + e.getMessage());
            }

        } else if (this.alphabet.equals(RNATools.getRNA())) {
            try {
                Sequence seq = RNATools.createRNASequence(currentSeq,
                                                          currentSeqName);
                this.sequenceMap.put(currentSeqName, seq);
            } catch (IllegalSymbolException e) {
                System.err.println(this.getClass()
                                   + " - IllegalSymbolException: " + e.getMessage());
            }
        } else if (this.alphabet.equals(ProteinTools.getAlphabet())) {
            try {
                Sequence seq = ProteinTools.createProteinSequence(currentSeq,
                                                                  currentSeqName);
                this.sequenceMap.put(currentSeqName, seq);
            } catch (IllegalSymbolException e) {
                System.err.println(this.getClass()
                                   + " - IllegalSymbolException: " + e.getMessage());
            }
        }
    }

}
