package uk.ac.ebi.jdispatcher.soap;

public class JDispatcherServiceProxy implements uk.ac.ebi.jdispatcher.soap.JDispatcherService_PortType {
  private String _endpoint = null;
  private uk.ac.ebi.jdispatcher.soap.JDispatcherService_PortType jDispatcherService_PortType = null;
  
  public JDispatcherServiceProxy() {
    _initJDispatcherServiceProxy();
  }
  
  public JDispatcherServiceProxy(String endpoint) {
    _endpoint = endpoint;
    _initJDispatcherServiceProxy();
  }
  
  private void _initJDispatcherServiceProxy() {
    try {
      jDispatcherService_PortType = (new uk.ac.ebi.jdispatcher.soap.JDispatcherService_ServiceLocator()).getJDispatcherServiceHttpPort();
      if (jDispatcherService_PortType != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)jDispatcherService_PortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)jDispatcherService_PortType)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (jDispatcherService_PortType != null)
      ((javax.xml.rpc.Stub)jDispatcherService_PortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public uk.ac.ebi.jdispatcher.soap.JDispatcherService_PortType getJDispatcherService_PortType() {
    if (jDispatcherService_PortType == null)
      _initJDispatcherServiceProxy();
    return jDispatcherService_PortType;
  }
  
  public java.lang.String run(java.lang.String email, java.lang.String title, uk.ac.ebi.jdispatcher.soap.InputParameters parameters) throws java.rmi.RemoteException{
    if (jDispatcherService_PortType == null)
      _initJDispatcherServiceProxy();
    return jDispatcherService_PortType.run(email, title, parameters);
  }
  
  public java.lang.String getStatus(java.lang.String jobId) throws java.rmi.RemoteException{
    if (jDispatcherService_PortType == null)
      _initJDispatcherServiceProxy();
    return jDispatcherService_PortType.getStatus(jobId);
  }
  
  public uk.ac.ebi.jdispatcher.soap.WsResultType[] getResultTypes(java.lang.String jobId) throws java.rmi.RemoteException{
    if (jDispatcherService_PortType == null)
      _initJDispatcherServiceProxy();
    return jDispatcherService_PortType.getResultTypes(jobId);
  }
  
  public byte[] getResult(java.lang.String jobId, java.lang.String type, uk.ac.ebi.jdispatcher.soap.WsRawOutputParameter[] parameters) throws java.rmi.RemoteException{
    if (jDispatcherService_PortType == null)
      _initJDispatcherServiceProxy();
    return jDispatcherService_PortType.getResult(jobId, type, parameters);
  }
  
  public java.lang.String[] getParameters() throws java.rmi.RemoteException{
    if (jDispatcherService_PortType == null)
      _initJDispatcherServiceProxy();
    return jDispatcherService_PortType.getParameters();
  }
  
  public uk.ac.ebi.jdispatcher.soap.WsParameterDetails getParameterDetails(java.lang.String parameterId) throws java.rmi.RemoteException{
    if (jDispatcherService_PortType == null)
      _initJDispatcherServiceProxy();
    return jDispatcherService_PortType.getParameterDetails(parameterId);
  }
  
  
}