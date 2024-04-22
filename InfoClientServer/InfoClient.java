package InfoClientServer;

import ByteCommunication.Commons.Address;
import ByteCommunication.MessageMarshaller.*;
import ByteCommunication.Registry.*;
import ByteCommunication.RequestReply.*;

public class InfoClient {
    public static void main(String args[]) {
        new Configuration();
        Requestor requestor = new Requestor("Client");
        Marshaller marshaller = new Marshaller();

        // Lookup the InfoServer address from the Naming Service
        Address namingServiceAddr = Registry.instance().get("NamingServer");
        Message lookupMsg = new Message("Client", "lookup Server");
        byte[] lookupBytes = marshaller.marshal(lookupMsg);
        byte[] serverAddrBytes = requestor.deliver_and_wait_feedback(namingServiceAddr, lookupBytes);
        Message serverAddrMsg = marshaller.unmarshal(serverAddrBytes);
        Address serverAddr = Entry.fromString(serverAddrMsg.data); // parse the received address and port

        // Actual request to the InfoServer
        Message infoRequest = new Message("Client", "road_info 101");
        byte[] requestBytes = marshaller.marshal(infoRequest);
        byte[] responseBytes = requestor.deliver_and_wait_feedback(serverAddr, requestBytes);
        Message response = marshaller.unmarshal(responseBytes);

        System.out.println("Client received message: " + response.data);
    }
}
