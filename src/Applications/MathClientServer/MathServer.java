package src.Applications.MathClientServer;

import src.sORB.Activator.ActivatorProxy;
import src.sORB.ByteCommunication.Commons.Address;
import src.sORB.ByteCommunication.RequestReply.Replyer;
import src.sORB.Activator.IActivatorProxy;
import src.sORB.NamingServiceProxy;
import src.sORB.ServerSideProxy;


public class MathServer {
    public static void main(String[] args) {
        new Configuration();
        IActivatorProxy activator = (IActivatorProxy) IActivatorProxy.create(MathServiceInterface.class, MathService::new);
        ServerSideProxy transformer = new ServerSideProxy(activator);

        Address myAddr = NamingServiceProxy.registerAndRetrieveAddress("MathServer", "127.0.0.1");

        Replyer replyer = new Replyer("MathServer", myAddr);
        while (true) {
            replyer.receive_transform_and_send_feedback(transformer);
        }
    }
}
