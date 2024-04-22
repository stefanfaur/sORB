package InfoClientServer;

import sORB.ByteCommunication.Commons.Address;
import sORB.ByteCommunication.RequestReply.*;
import sORB.NamingServiceProxy;
import sORB.ServerSideProxy;


public class InfoServer {
    public static void main(String[] args) {
        new Configuration();
        InfoMessageServer implementation = new InfoMessageServer();
        ServerSideProxy transformer = new ServerSideProxy(implementation);

        Address myAddr = NamingServiceProxy.registerAndRetrieveAddress("InfoServer", "127.0.0.1");

        Replyer replyer = new Replyer("InfoServer", myAddr);
        while (true) {
            replyer.receive_transform_and_send_feedback(transformer);
        }
    }
}
