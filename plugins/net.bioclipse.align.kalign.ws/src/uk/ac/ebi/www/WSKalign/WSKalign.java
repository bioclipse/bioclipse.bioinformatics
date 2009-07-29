/**
 * WSKalign.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package uk.ac.ebi.www.WSKalign;

public interface WSKalign extends java.rmi.Remote {

    /**
     * Submit a Kalign analysis job (see
     * 				http://www.ebi.ac.uk/Tools/webservices/services/kalign#runkalign_params_content)
     */
    public java.lang.String runKalign(uk.ac.ebi.www.WSKalign.InputParams params, uk.ac.ebi.www.WSKalign.Data[] content) throws java.rmi.RemoteException;

    /**
     * Get the status of a submited job (see
     * 				http://www.ebi.ac.uk/Tools/webservices/services/kalign#checkstatus_jobid)
     */
    public java.lang.String checkStatus(java.lang.String jobid) throws java.rmi.RemoteException;

    /**
     * Get the results of a job (see
     * 				http://www.ebi.ac.uk/Tools/webservices/services/kalign#poll_jobid_type)
     */
    public byte[] poll(java.lang.String jobid, java.lang.String type) throws java.rmi.RemoteException;

    /**
     * Get the available result types for a completed job (see
     * 				http://www.ebi.ac.uk/Tools/webservices/services/kalign#getresults_jobid)
     */
    public uk.ac.ebi.www.WSKalign.WSFile[] getResults(java.lang.String jobid) throws java.rmi.RemoteException;
}
