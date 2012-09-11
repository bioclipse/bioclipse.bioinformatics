/**
 * InputParameters.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package uk.ac.ebi.jdispatcher.soap;


/**
 * Input parameters for the tool
 */
public class InputParameters  implements java.io.Serializable {
    /* Sequence Type [Indicates if the sequences to align are protein
     * or nucleotide (DNA/RNA).] */
    private java.lang.String stype;

    /* Alignment format [Format for generated multiple sequence alignment.] */
    private java.lang.String format;

    /* Gap Open Penalty [The penalty for opening/closing a gap. Half
     * the value will be subtracted from the alignment score when opening,
     * and half when closing a gap.] */
    private java.lang.Float gapopen;

    /* Gap Extension Penalty [Penalty for extending a gap] */
    private java.lang.Float gapext;

    /* Terminal Gap Penalties [Penalty to extend gaps from the N/C
     * terminal of protein or 5'/3' terminal of nucleotide sequences] */
    private java.lang.Float termgap;

    /* Bonus Score [A bonus score that is added to each pair of aligned
     * residues] */
    private java.lang.Float bonus;

    /* Sequence [Three or more sequences to be aligned can be entered
     * directly into this form. Sequences can be be in GCG, FASTA, EMBL,
     * GenBank, PIR, NBRF, PHYLIP or UniProtKB/Swiss-Prot format. Partially
     * formatted sequences are not accepted. Adding a return to the end of
     * the sequence may help certain applications understand the input. Note
     * that directly using data from word processors may yield unpredictable
     * results as hidden/control characters may be present.
     * 			There is currently a limit of 2000 sequences and 2MB of data.] */
    private java.lang.String sequence;

    public InputParameters() {
    }

    public InputParameters(
           java.lang.String stype,
           java.lang.String format,
           java.lang.Float gapopen,
           java.lang.Float gapext,
           java.lang.Float termgap,
           java.lang.Float bonus,
           java.lang.String sequence) {
           this.stype = stype;
           this.format = format;
           this.gapopen = gapopen;
           this.gapext = gapext;
           this.termgap = termgap;
           this.bonus = bonus;
           this.sequence = sequence;
    }


    /**
     * Gets the stype value for this InputParameters.
     * 
     * @return stype   * Sequence Type [Indicates if the sequences to align are protein
     * or nucleotide (DNA/RNA).]
     */
    public java.lang.String getStype() {
        return stype;
    }


    /**
     * Sets the stype value for this InputParameters.
     * 
     * @param stype   * Sequence Type [Indicates if the sequences to align are protein
     * or nucleotide (DNA/RNA).]
     */
    public void setStype(java.lang.String stype) {
        this.stype = stype;
    }


    /**
     * Gets the format value for this InputParameters.
     * 
     * @return format   * Alignment format [Format for generated multiple sequence alignment.]
     */
    public java.lang.String getFormat() {
        return format;
    }


    /**
     * Sets the format value for this InputParameters.
     * 
     * @param format   * Alignment format [Format for generated multiple sequence alignment.]
     */
    public void setFormat(java.lang.String format) {
        this.format = format;
    }


    /**
     * Gets the gapopen value for this InputParameters.
     * 
     * @return gapopen   * Gap Open Penalty [The penalty for opening/closing a gap. Half
     * the value will be subtracted from the alignment score when opening,
     * and half when closing a gap.]
     */
    public java.lang.Float getGapopen() {
        return gapopen;
    }


    /**
     * Sets the gapopen value for this InputParameters.
     * 
     * @param gapopen   * Gap Open Penalty [The penalty for opening/closing a gap. Half
     * the value will be subtracted from the alignment score when opening,
     * and half when closing a gap.]
     */
    public void setGapopen(java.lang.Float gapopen) {
        this.gapopen = gapopen;
    }


    /**
     * Gets the gapext value for this InputParameters.
     * 
     * @return gapext   * Gap Extension Penalty [Penalty for extending a gap]
     */
    public java.lang.Float getGapext() {
        return gapext;
    }


    /**
     * Sets the gapext value for this InputParameters.
     * 
     * @param gapext   * Gap Extension Penalty [Penalty for extending a gap]
     */
    public void setGapext(java.lang.Float gapext) {
        this.gapext = gapext;
    }


    /**
     * Gets the termgap value for this InputParameters.
     * 
     * @return termgap   * Terminal Gap Penalties [Penalty to extend gaps from the N/C
     * terminal of protein or 5'/3' terminal of nucleotide sequences]
     */
    public java.lang.Float getTermgap() {
        return termgap;
    }


    /**
     * Sets the termgap value for this InputParameters.
     * 
     * @param termgap   * Terminal Gap Penalties [Penalty to extend gaps from the N/C
     * terminal of protein or 5'/3' terminal of nucleotide sequences]
     */
    public void setTermgap(java.lang.Float termgap) {
        this.termgap = termgap;
    }


    /**
     * Gets the bonus value for this InputParameters.
     * 
     * @return bonus   * Bonus Score [A bonus score that is added to each pair of aligned
     * residues]
     */
    public java.lang.Float getBonus() {
        return bonus;
    }


    /**
     * Sets the bonus value for this InputParameters.
     * 
     * @param bonus   * Bonus Score [A bonus score that is added to each pair of aligned
     * residues]
     */
    public void setBonus(java.lang.Float bonus) {
        this.bonus = bonus;
    }


    /**
     * Gets the sequence value for this InputParameters.
     * 
     * @return sequence   * Sequence [Three or more sequences to be aligned can be entered
     * directly into this form. Sequences can be be in GCG, FASTA, EMBL,
     * GenBank, PIR, NBRF, PHYLIP or UniProtKB/Swiss-Prot format. Partially
     * formatted sequences are not accepted. Adding a return to the end of
     * the sequence may help certain applications understand the input. Note
     * that directly using data from word processors may yield unpredictable
     * results as hidden/control characters may be present.
     * 			There is currently a limit of 2000 sequences and 2MB of data.]
     */
    public java.lang.String getSequence() {
        return sequence;
    }


    /**
     * Sets the sequence value for this InputParameters.
     * 
     * @param sequence   * Sequence [Three or more sequences to be aligned can be entered
     * directly into this form. Sequences can be be in GCG, FASTA, EMBL,
     * GenBank, PIR, NBRF, PHYLIP or UniProtKB/Swiss-Prot format. Partially
     * formatted sequences are not accepted. Adding a return to the end of
     * the sequence may help certain applications understand the input. Note
     * that directly using data from word processors may yield unpredictable
     * results as hidden/control characters may be present.
     * 			There is currently a limit of 2000 sequences and 2MB of data.]
     */
    public void setSequence(java.lang.String sequence) {
        this.sequence = sequence;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof InputParameters)) return false;
        InputParameters other = (InputParameters) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.stype==null && other.getStype()==null) || 
             (this.stype!=null &&
              this.stype.equals(other.getStype()))) &&
            ((this.format==null && other.getFormat()==null) || 
             (this.format!=null &&
              this.format.equals(other.getFormat()))) &&
            ((this.gapopen==null && other.getGapopen()==null) || 
             (this.gapopen!=null &&
              this.gapopen.equals(other.getGapopen()))) &&
            ((this.gapext==null && other.getGapext()==null) || 
             (this.gapext!=null &&
              this.gapext.equals(other.getGapext()))) &&
            ((this.termgap==null && other.getTermgap()==null) || 
             (this.termgap!=null &&
              this.termgap.equals(other.getTermgap()))) &&
            ((this.bonus==null && other.getBonus()==null) || 
             (this.bonus!=null &&
              this.bonus.equals(other.getBonus()))) &&
            ((this.sequence==null && other.getSequence()==null) || 
             (this.sequence!=null &&
              this.sequence.equals(other.getSequence())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getStype() != null) {
            _hashCode += getStype().hashCode();
        }
        if (getFormat() != null) {
            _hashCode += getFormat().hashCode();
        }
        if (getGapopen() != null) {
            _hashCode += getGapopen().hashCode();
        }
        if (getGapext() != null) {
            _hashCode += getGapext().hashCode();
        }
        if (getTermgap() != null) {
            _hashCode += getTermgap().hashCode();
        }
        if (getBonus() != null) {
            _hashCode += getBonus().hashCode();
        }
        if (getSequence() != null) {
            _hashCode += getSequence().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(InputParameters.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://soap.jdispatcher.ebi.ac.uk", "InputParameters"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("stype");
        elemField.setXmlName(new javax.xml.namespace.QName("", "stype"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("format");
        elemField.setXmlName(new javax.xml.namespace.QName("", "format"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("gapopen");
        elemField.setXmlName(new javax.xml.namespace.QName("", "gapopen"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "float"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("gapext");
        elemField.setXmlName(new javax.xml.namespace.QName("", "gapext"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "float"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("termgap");
        elemField.setXmlName(new javax.xml.namespace.QName("", "termgap"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "float"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("bonus");
        elemField.setXmlName(new javax.xml.namespace.QName("", "bonus"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "float"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sequence");
        elemField.setXmlName(new javax.xml.namespace.QName("", "sequence"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
