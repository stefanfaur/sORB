package InfoClientServer;

import ByteCommunication.Commons.Address;
import ByteCommunication.MessageMarshaller.*;
import ByteCommunication.Registry.*;
import ByteCommunication.RequestReply.*;

class ServerTransformer implements ByteStreamTransformer {
    private InfoMessageServer originalServer;

    public ServerTransformer(InfoMessageServer s) {
        originalServer = s;
    }

    public byte[] transform(byte[] in) {
        Marshaller m = new Marshaller();
        Message msg = m.unmarshal(in);

        Message answer;
        if (msg.data.startsWith("road_info")) {
            int roadId = Integer.parseInt(msg.data.split(" ")[1]);
            answer = originalServer.getRoadInfo(roadId);
        } else if (msg.data.startsWith("temp_info")) {
            String city = msg.data.split(" ")[1];
            answer = originalServer.getTemp(city);
        } else {
            answer = new Message("Server", "Invalid request");
        }

        return m.marshal(answer);
    }
}

class InfoMessageServer {
    public Message getRoadInfo(int roadID) {
        String info = "Road " + roadID + " information: Construction ahead";
        return new Message("Server", info);
    }

    public Message getTemp(String city) {
        String temp = "Temperature in " + city + " is 72°F";
        return new Message("Server", temp);
    }
}

public class InfoServer {
    public static void main(String args[]) {
        new Configuration();
        Address namingServiceAddr = Registry.instance().get("NamingServer");
        Requestor nsRequestor = new Requestor("Server");

        // Server sends its IP address to the Naming Service for registration
        String serverIp = "127.0.0.1";
        Message registerMsg = new Message("Server", "register Server " + serverIp);
        byte[] registerBytes = new Marshaller().marshal(registerMsg);
        byte[] responseBytes = nsRequestor.deliver_and_wait_feedback(namingServiceAddr, registerBytes);
        Message responseMsg = new Marshaller().unmarshal(responseBytes);
        Address myAddr = Entry.fromString(responseMsg.data); // Parse the received address and port and assign it to the current server address

        Replyer replyer = new Replyer("Server", myAddr);
        ByteStreamTransformer transformer = new ServerTransformer(new InfoMessageServer());



        while (true) {
            replyer.receive_transform_and_send_feedback(transformer);
        }
    }
}
