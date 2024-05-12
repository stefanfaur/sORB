package src.sORB;

import src.sORB.ByteCommunication.Commons.Address;
import src.sORB.ByteCommunication.MessageMarshaller.*;
import src.sORB.ByteCommunication.Registry.*;
import src.sORB.ByteCommunication.RequestReply.*;

import java.sql.SQLOutput;

public class NamingServiceProxy {
    private static Address namingServiceAddr = Registry.instance().get("NamingServer");
    private static Marshaller marshaller = new Marshaller();
    private static Requestor requestor = new Requestor("sORB");

    public static Address lookup(String serviceName) {
        Message lookupMsg = new Message("sORB", "lookup " + serviceName);
        byte[] lookupBytes = marshaller.marshal(lookupMsg);
        byte[] responseBytes = requestor.deliver_and_wait_feedback(namingServiceAddr, lookupBytes);
        Message serverAddrMsg = marshaller.unmarshal(responseBytes);
        System.out.println("Lookup response: " + serverAddrMsg.data);
        return Entry.fromString(serverAddrMsg.data);
    }

    public static Address registerAndRetrieveAddress(String serviceName, String serverIp) {
        Message registerMsg = new Message("sORB", "register " + serviceName + " " + serverIp);
        byte[] registerBytes = marshaller.marshal(registerMsg);
        byte[] responseBytes = requestor.deliver_and_wait_feedback(namingServiceAddr, registerBytes);
        Message serverAddrMsg = marshaller.unmarshal(responseBytes);
        return Entry.fromString(serverAddrMsg.data);
    }

    public static void release(String serviceName) {
        Message releaseMsg = new Message("sORB", "release " + serviceName);
        byte[] releaseBytes = marshaller.marshal(releaseMsg);
        byte[] responseBytes = requestor.deliver_and_wait_feedback(namingServiceAddr, releaseBytes);
        Message response = marshaller.unmarshal(responseBytes);
        System.out.println("Released service: " + serviceName + " with response: " + response.data);
    }

    public static void activate(String serviceName) {
        Message activateMsg = new Message("sORB", "activate " + serviceName);
        byte[] activateBytes = marshaller.marshal(activateMsg);
        byte[] responseBytes = requestor.deliver_and_wait_feedback(namingServiceAddr, activateBytes);
        Message response = marshaller.unmarshal(responseBytes);
        System.out.println("Activation response for " + serviceName + ": " + response.data);
    }

    public static void deactivate(String serviceName) {
        Message deactivateMsg = new Message("sORB", "deactivate " + serviceName);
        byte[] deactivateBytes = marshaller.marshal(deactivateMsg);
        byte[] responseBytes = requestor.deliver_and_wait_feedback(namingServiceAddr, deactivateBytes);
        Message response = marshaller.unmarshal(responseBytes);
        System.out.println("Deactivation response for " + serviceName + ": " + response.data);
    }
}
