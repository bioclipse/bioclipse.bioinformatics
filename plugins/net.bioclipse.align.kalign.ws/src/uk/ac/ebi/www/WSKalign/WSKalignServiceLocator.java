/**
 * WSKalignServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package uk.ac.ebi.www.WSKalign;

public class WSKalignServiceLocator extends org.apache.axis.client.Service implements uk.ac.ebi.www.WSKalign.WSKalignService {

    public WSKalignServiceLocator() {
    }


    public WSKalignServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public WSKalignServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for WSKalign
    private java.lang.String WSKalign_address = "http://www.ebi.ac.uk/Tools/es/ws-servers/WSKalign";

    public java.lang.String getWSKalignAddress() {
        return WSKalign_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String WSKalignWSDDServiceName = "WSKalign";

    public java.lang.String getWSKalignWSDDServiceName() {
        return WSKalignWSDDServiceName;
    }

    public void setWSKalignWSDDServiceName(java.lang.String name) {
        WSKalignWSDDServiceName = name;
    }

    public uk.ac.ebi.www.WSKalign.WSKalign getWSKalign() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(WSKalign_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getWSKalign(endpoint);
    }

    public uk.ac.ebi.www.WSKalign.WSKalign getWSKalign(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            uk.ac.ebi.www.WSKalign.WSKalignSoapBindingStub _stub = new uk.ac.ebi.www.WSKalign.WSKalignSoapBindingStub(portAddress, this);
            _stub.setPortName(getWSKalignWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setWSKalignEndpointAddress(java.lang.String address) {
        WSKalign_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (uk.ac.ebi.www.WSKalign.WSKalign.class.isAssignableFrom(serviceEndpointInterface)) {
                uk.ac.ebi.www.WSKalign.WSKalignSoapBindingStub _stub = new uk.ac.ebi.www.WSKalign.WSKalignSoapBindingStub(new java.net.URL(WSKalign_address), this);
                _stub.setPortName(getWSKalignWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("WSKalign".equals(inputPortName)) {
            return getWSKalign();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://www.ebi.ac.uk/WSKalign", "WSKalignService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://www.ebi.ac.uk/WSKalign", "WSKalign"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("WSKalign".equals(portName)) {
            setWSKalignEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
