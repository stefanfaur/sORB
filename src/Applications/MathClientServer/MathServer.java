package src.Applications.MathClientServer;

import src.sORB.ByteCommunication.Commons.Address;
import src.sORB.ByteCommunication.RequestReply.Replyer;
import src.sORB.NamingServiceProxy;
import src.sORB.ServerSideProxy;


public class MathServer {
    public static void main(String[] args) {
        new Configuration();
        MathService implementation = new MathService();
        ServerSideProxy transformer = new ServerSideProxy(implementation);

        Address myAddr = NamingServiceProxy.registerAndRetrieveAddress("MathServer", "127.0.0.1");

        Replyer replyer = new Replyer("MathServer", myAddr);
        while (true) {
            replyer.receive_transform_and_send_feedback(transformer);
        }
    }
}
