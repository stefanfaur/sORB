package sORB.ByteCommunication;

import sORB.ByteCommunication.Commons.Address;
import sORB.ByteCommunication.MessageMarshaller.*;
import sORB.ByteCommunication.Registry.*;
import sORB.ByteCommunication.RequestReply.*;


public class ClientWithRR {
    public static void main(String args[]) {
        new Configuration();

        Address dest = Registry.instance().get("Server");

        Message msg = new Message("Client", "How are you");

        Requestor r = new Requestor("Client");

        Marshaller m = new Marshaller();

        byte[] bytes = m.marshal(msg);

        bytes = r.deliver_and_wait_feedback(dest, bytes);

        Message answer = m.unmarshal(bytes);

        System.out.println("Client received message " + answer.data + " from " + answer.sender);
    }

}