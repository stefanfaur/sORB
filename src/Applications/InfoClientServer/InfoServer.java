package src.Applications.InfoClientServer;

import src.sORB.ByteCommunication.Commons.Address;
import src.sORB.ByteCommunication.RequestReply.*;
import src.sORB.NamingServiceProxy;
import src.sORB.ServerSideProxy;


public class InfoServer {
    public static void main(String[] args) {
        new Configuration();
        InfoService implementation = new InfoService();
        ServerSideProxy transformer = new ServerSideProxy(implementation);

        Address myAddr = NamingServiceProxy.registerAndRetrieveAddress("InfoServer", "127.0.0.1");

        Replyer replyer = new Replyer("InfoServer", myAddr);
        while (true) {
            replyer.receive_transform_and_send_feedback(transformer);
        }
    }
}
