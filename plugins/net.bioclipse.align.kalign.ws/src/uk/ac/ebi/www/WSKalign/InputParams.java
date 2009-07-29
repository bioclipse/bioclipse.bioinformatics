/**
 * InputParams.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package uk.ac.ebi.www.WSKalign;


/**
 * Input parameters for tool, see
 * 						http://www.ebi.ac.uk/Tools/webservices/services/kalign#inputparams
 * 						for details.
 */
public class InputParams  implements java.io.Serializable {
    /* Molecule type: N for
     * 								nucleotide or P for protein. */
    private java.lang.String moltype;

    /* Gap open
     * 								penalty. */
    private float gpo;

    /* Gap extension
     * 								penalty. */
    private float gpe;

    /* Terminal gap
     * 								penalty. */
    private float tgpe;

    /* Bonus score added to
     * 								each pair of aligned residues. */
    private java.lang.Float bonus;

    /* Not
     * 								used. */
    private java.lang.String sequence;

    /* Asynchronous
     * 								submission. */
    private java.lang.Boolean async;

    /* User e-mail
     * 								address. */
    private java.lang.String email;

    public InputParams() {
    }

    public InputParams(
           java.lang.String moltype,
           float gpo,
           float gpe,
           float tgpe,
           java.lang.Float bonus,
           java.lang.String sequence,
           java.lang.Boolean async,
           java.lang.String email) {
           this.moltype = moltype;
           this.gpo = gpo;
           this.gpe = gpe;
           this.tgpe = tgpe;
           this.bonus = bonus;
           this.sequence = sequence;
           this.async = async;
           this.email = email;
    }


    /**
     * Gets the moltype value for this InputParams.
     * 
     * @return moltype   * Molecule type: N for
     * 								nucleotide or P for protein.
     */
    public java.lang.String getMoltype() {
        return moltype;
    }


    /**
     * Sets the moltype value for this InputParams.
     * 
     * @param moltype   * Molecule type: N for
     * 								nucleotide or P for protein.
     */
    public void setMoltype(java.lang.String moltype) {
        this.moltype = moltype;
    }


    /**
     * Gets the gpo value for this InputParams.
     * 
     * @return gpo   * Gap open
     * 								penalty.
     */
    public float getGpo() {
        return gpo;
    }


    /**
     * Sets the gpo value for this InputParams.
     * 
     * @param gpo   * Gap open
     * 								penalty.
     */
    public void setGpo(float gpo) {
        this.gpo = gpo;
    }


    /**
     * Gets the gpe value for this InputParams.
     * 
     * @return gpe   * Gap extension
     * 								penalty.
     */
    public float getGpe() {
        return gpe;
    }


    /**
     * Sets the gpe value for this InputParams.
     * 
     * @param gpe   * Gap extension
     * 								penalty.
     */
    public void setGpe(float gpe) {
        this.gpe = gpe;
    }


    /**
     * Gets the tgpe value for this InputParams.
     * 
     * @return tgpe   * Terminal gap
     * 								penalty.
     */
    public float getTgpe() {
        return tgpe;
    }


    /**
     * Sets the tgpe value for this InputParams.
     * 
     * @param tgpe   * Terminal gap
     * 								penalty.
     */
    public void setTgpe(float tgpe) {
        this.tgpe = tgpe;
    }


    /**
     * Gets the bonus value for this InputParams.
     * 
     * @return bonus   * Bonus score added to
     * 								each pair of aligned residues.
     */
    public java.lang.Float getBonus() {
        return bonus;
    }


    /**
     * Sets the bonus value for this InputParams.
     * 
     * @param bonus   * Bonus score added to
     * 								each pair of aligned residues.
     */
    public void setBonus(java.lang.Float bonus) {
        this.bonus = bonus;
    }


    /**
     * Gets the sequence value for this InputParams.
     * 
     * @return sequence   * Not
     * 								used.
     */
    public java.lang.String getSequence() {
        return sequence;
    }


    /**
     * Sets the sequence value for this InputParams.
     * 
     * @param sequence   * Not
     * 								used.
     */
    public void setSequence(java.lang.String sequence) {
        this.sequence = sequence;
    }


    /**
     * Gets the async value for this InputParams.
     * 
     * @return async   * Asynchronous
     * 								submission.
     */
    public java.lang.Boolean getAsync() {
        return async;
    }


    /**
     * Sets the async value for this InputParams.
     * 
     * @param async   * Asynchronous
     * 								submission.
     */
    public void setAsync(java.lang.Boolean async) {
        this.async = async;
    }


    /**
     * Gets the email value for this InputParams.
     * 
     * @return email   * User e-mail
     * 								address.
     */
    public java.lang.String getEmail() {
        return email;
    }


    /**
     * Sets the email value for this InputParams.
     * 
     * @param email   * User e-mail
     * 								address.
     */
    public void setEmail(java.lang.String email) {
        this.email = email;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof InputParams)) return false;
        InputParams other = (InputParams) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.moltype==null && other.getMoltype()==null) || 
             (this.moltype!=null &&
              this.moltype.equals(other.getMoltype()))) &&
            this.gpo == other.getGpo() &&
            this.gpe == other.getGpe() &&
            this.tgpe == other.getTgpe() &&
            ((this.bonus==null && other.getBonus()==null) || 
             (this.bonus!=null &&
              this.bonus.equals(other.getBonus()))) &&
            ((this.sequence==null && other.getSequence()==null) || 
             (this.sequence!=null &&
              this.sequence.equals(other.getSequence()))) &&
            ((this.async==null && other.getAsync()==null) || 
             (this.async!=null &&
              this.async.equals(other.getAsync()))) &&
            ((this.email==null && other.getEmail()==null) || 
             (this.email!=null &&
              this.email.equals(other.getEmail())));
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
        if (getMoltype() != null) {
            _hashCode += getMoltype().hashCode();
        }
        _hashCode += new Float(getGpo()).hashCode();
        _hashCode += new Float(getGpe()).hashCode();
        _hashCode += new Float(getTgpe()).hashCode();
        if (getBonus() != null) {
            _hashCode += getBonus().hashCode();
        }
        if (getSequence() != null) {
            _hashCode += getSequence().hashCode();
        }
        if (getAsync() != null) {
            _hashCode += getAsync().hashCode();
        }
        if (getEmail() != null) {
            _hashCode += getEmail().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(InputParams.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.ebi.ac.uk/WSKalign", "inputParams"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("moltype");
        elemField.setXmlName(new javax.xml.namespace.QName("", "moltype"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("gpo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "gpo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "float"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("gpe");
        elemField.setXmlName(new javax.xml.namespace.QName("", "gpe"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "float"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tgpe");
        elemField.setXmlName(new javax.xml.namespace.QName("", "tgpe"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "float"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("bonus");
        elemField.setXmlName(new javax.xml.namespace.QName("", "bonus"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "float"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sequence");
        elemField.setXmlName(new javax.xml.namespace.QName("", "sequence"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("async");
        elemField.setXmlName(new javax.xml.namespace.QName("", "async"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("email");
        elemField.setXmlName(new javax.xml.namespace.QName("", "email"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
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
