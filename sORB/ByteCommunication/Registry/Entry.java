package sORB.ByteCommunication.Registry;

import sORB.ByteCommunication.Commons.Address;

public class Entry implements Address
{
	private String destinationId;
	private int portNr;
	public Entry(String theDest, int thePort)
	{
		destinationId = theDest;
		portNr = thePort;
	}

	public String toString()
	{
		return destinationId + " " + portNr;
	}

	public static Address fromString(String data) {
		String[] parts = data.split(" ");
		return new Entry(parts[0], Integer.parseInt(parts[1]));
	}

	public String dest()
	{
		return destinationId;
	}
	public int port()
	{
		return portNr;
	}
}