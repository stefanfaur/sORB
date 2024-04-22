package InfoClientServer;

import ByteCommunication.Commons.Address;
import ByteCommunication.MessageMarshaller.*;
import ByteCommunication.Registry.*;
import ByteCommunication.RequestReply.*;

public class InfoClient {
    public static void main(String args[]) {
        new Configuration();
        Address dest = Registry.instance().get("Server");
        Requestor r = new Requestor("Client");

        Marshaller m = new Marshaller();

        // Example request for road information
        Message msg = new Message("Client", "road_info 101");
        byte[] bytes = m.marshal(msg);
        bytes = r.deliver_and_wait_feedback(dest, bytes);
        Message answer = m.unmarshal(bytes);
        System.out.println("Client received message: " + answer.data);

        // Example request for temperature information
        msg = new Message("Client", "temp_info Seattle");
        bytes = m.marshal(msg);
        bytes = r.deliver_and_wait_feedback(dest, bytes);
        answer = m.unmarshal(bytes);
        System.out.println("Client received message: " + answer.data);
    }
}
