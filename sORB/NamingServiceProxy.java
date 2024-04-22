package sORB;

import sORB.ByteCommunication.Commons.Address;
import sORB.ByteCommunication.MessageMarshaller.*;
import sORB.ByteCommunication.Registry.*;
import sORB.ByteCommunication.RequestReply.*;

public class NamingServiceProxy {
    private static Address namingServiceAddr = Registry.instance().get("NamingServer");
    private static Marshaller marshaller = new Marshaller();
    private static Requestor requestor = new Requestor("sORB");

    public static Address lookup(String serviceName) {
        Message lookupMsg = new Message("sORB", "lookup " + serviceName);
        byte[] lookupBytes = marshaller.marshal(lookupMsg);
        byte[] responseBytes = requestor.deliver_and_wait_feedback(namingServiceAddr, lookupBytes);
        Message serverAddrMsg = marshaller.unmarshal(responseBytes);
        return Entry.fromString(serverAddrMsg.data);
    }

    public static Address registerAndRetrieveAddress(String serviceName, String serverIp) {
        Message registerMsg = new Message("sORB", "register " + serviceName + " " + serverIp);
        byte[] registerBytes = marshaller.marshal(registerMsg);
        byte[] responseBytes = requestor.deliver_and_wait_feedback(namingServiceAddr, registerBytes);
        Message serverAddrMsg = marshaller.unmarshal(responseBytes);
        return Entry.fromString(serverAddrMsg.data);
    }
}