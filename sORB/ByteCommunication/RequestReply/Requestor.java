
package sORB.ByteCommunication.RequestReply;

import sORB.ByteCommunication.Commons.Address;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;


public class Requestor
{

	private Socket s;
	private OutputStream oStr;
	private InputStream iStr;
	private String myName;
	public Requestor(String theName) { myName = theName; }


	public byte[] deliver_and_wait_feedback(Address theDest, byte[] data)
	{

		byte[] buffer = null;
		int val;
		try
		{
			System.out.println("creating socket to "+theDest.dest()+" "+theDest.port());
			s = new Socket(theDest.dest(), theDest.port());
System.out.println("Requestor: Socket"+s);
			oStr = s.getOutputStream();
			oStr.write(data);
			oStr.flush();
			iStr = s.getInputStream();
			val = iStr.read();
			buffer = new byte[val];
			iStr.read(buffer);
			iStr.close();
                        oStr.close();
			s.close();
			}
		catch (IOException e) { 
                       System.out.println("IOException in deliver_and_wait_feedback");
					   e.printStackTrace();
		}
		return buffer;
	}

}

