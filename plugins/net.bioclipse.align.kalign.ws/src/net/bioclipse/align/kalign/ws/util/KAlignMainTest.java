package net.bioclipse.align.kalign.ws.util;

import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

import uk.ac.ebi.jdispatcher.soap.InputParameters;
import uk.ac.ebi.jdispatcher.soap.JDispatcherService_PortType;
import uk.ac.ebi.jdispatcher.soap.JDispatcherService_ServiceLocator;



public class KAlignMainTest {


	public static void main(String[] args) {


		JDispatcherService_ServiceLocator loc = new JDispatcherService_ServiceLocator();

		InputParameters params = new InputParameters();

		params.setStype( "P" );
		params.setSequence( ">p1\nASAMPLESEQ\n>p2\nANOTHERSAMPLESEQ" );

		JDispatcherService_PortType kalign;
		try {
			kalign = loc.getJDispatcherServiceHttpPort();
			String jobid = kalign.run("bioclipse@bioclipse.net", "kalign with Bioclipse", params);

			// Poll until job has finished
			String status = "RUNNING";
			while (status.equals("RUNNING")) {
				Thread.sleep(3000); // Wait 3 seconds
				status = kalign.getStatus(jobid); // check job status
				System.out.println(status);
			}
			// If the job completed successfully...
			if (status.equals("FINISHED")) {
				// Get the text result
				byte[] resultbytes = kalign.getResult(jobid, "out", null);
				String result = new String(resultbytes);
				// Output the result
				System.out.println(result);
			}
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}



	}


}
