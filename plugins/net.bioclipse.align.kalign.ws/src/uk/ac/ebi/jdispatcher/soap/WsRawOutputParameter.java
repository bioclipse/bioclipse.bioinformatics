/**
 * WsRawOutputParameter.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package uk.ac.ebi.jdispatcher.soap;


/**
 * A parameter used by the renderer
 */
public class WsRawOutputParameter  implements java.io.Serializable {
    /* The name of the parameter */
    private java.lang.String name;

    /* The parameter value as an array of String */
    private java.lang.String[] value;

    public WsRawOutputParameter() {
    }

    public WsRawOutputParameter(
           java.lang.String name,
           java.lang.String[] value) {
           this.name = name;
           this.value = value;
    }


    /**
     * Gets the name value for this WsRawOutputParameter.
     * 
     * @return name   * The name of the parameter
     */
    public java.lang.String getName() {
        return name;
    }


    /**
     * Sets the name value for this WsRawOutputParameter.
     * 
     * @param name   * The name of the parameter
     */
    public void setName(java.lang.String name) {
        this.name = name;
    }


    /**
     * Gets the value value for this WsRawOutputParameter.
     * 
     * @return value   * The parameter value as an array of String
     */
    public java.lang.String[] getValue() {
        return value;
    }


    /**
     * Sets the value value for this WsRawOutputParameter.
     * 
     * @param value   * The parameter value as an array of String
     */
    public void setValue(java.lang.String[] value) {
        this.value = value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof WsRawOutputParameter)) return false;
        WsRawOutputParameter other = (WsRawOutputParameter) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.name==null && other.getName()==null) || 
             (this.name!=null &&
              this.name.equals(other.getName()))) &&
            ((this.value==null && other.getValue()==null) || 
             (this.value!=null &&
              java.util.Arrays.equals(this.value, other.getValue())));
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
        if (getName() != null) {
            _hashCode += getName().hashCode();
        }
        if (getValue() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getValue());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getValue(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(WsRawOutputParameter.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://soap.jdispatcher.ebi.ac.uk", "wsRawOutputParameter"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("name");
        elemField.setXmlName(new javax.xml.namespace.QName("", "name"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("value");
        elemField.setXmlName(new javax.xml.namespace.QName("", "value"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("", "string"));
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
