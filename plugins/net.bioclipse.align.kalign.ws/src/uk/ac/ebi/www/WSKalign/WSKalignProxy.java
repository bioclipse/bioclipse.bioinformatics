package uk.ac.ebi.www.WSKalign;

public class WSKalignProxy implements uk.ac.ebi.www.WSKalign.WSKalign {
  private String _endpoint = null;
  private uk.ac.ebi.www.WSKalign.WSKalign wSKalign = null;
  
  public WSKalignProxy() {
    _initWSKalignProxy();
  }
  
  public WSKalignProxy(String endpoint) {
    _endpoint = endpoint;
    _initWSKalignProxy();
  }
  
  private void _initWSKalignProxy() {
    try {
      wSKalign = (new uk.ac.ebi.www.WSKalign.WSKalignServiceLocator()).getWSKalign();
      if (wSKalign != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)wSKalign)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)wSKalign)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (wSKalign != null)
      ((javax.xml.rpc.Stub)wSKalign)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public uk.ac.ebi.www.WSKalign.WSKalign getWSKalign() {
    if (wSKalign == null)
      _initWSKalignProxy();
    return wSKalign;
  }
  
  public java.lang.String runKalign(uk.ac.ebi.www.WSKalign.InputParams params, uk.ac.ebi.www.WSKalign.Data[] content) throws java.rmi.RemoteException{
    if (wSKalign == null)
      _initWSKalignProxy();
    return wSKalign.runKalign(params, content);
  }
  
  public java.lang.String checkStatus(java.lang.String jobid) throws java.rmi.RemoteException{
    if (wSKalign == null)
      _initWSKalignProxy();
    return wSKalign.checkStatus(jobid);
  }
  
  public byte[] poll(java.lang.String jobid, java.lang.String type) throws java.rmi.RemoteException{
    if (wSKalign == null)
      _initWSKalignProxy();
    return wSKalign.poll(jobid, type);
  }
  
  public uk.ac.ebi.www.WSKalign.WSFile[] getResults(java.lang.String jobid) throws java.rmi.RemoteException{
    if (wSKalign == null)
      _initWSKalignProxy();
    return wSKalign.getResults(jobid);
  }
  
  
}