package InfoClientServer;

import ByteCommunication.Commons.Address;
import ByteCommunication.MessageMarshaller.*;
import ByteCommunication.Registry.*;
import ByteCommunication.RequestReply.*;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

public class NamingService {
    private static final Map<String, Address> locationMap = new ConcurrentHashMap<>();
    private static int currentPort = 2000; // Start from an arbitrary port

    public static void main(String[] args) {
        new Configuration();
        Address myAddr = Registry.instance().get("NamingServer");
        Replyer replyer = new Replyer("NamingServer", myAddr);

        while (true) {
            replyer.receive_transform_and_send_feedback(NamingService::transform);
        }
    }

    private static byte[] transform(byte[] in) {
        Marshaller marshaller = new Marshaller();
        Message request = marshaller.unmarshal(in);
        String[] requestData = request.data.split(" ");
        Message response;

        switch (requestData[0].toLowerCase()) {
            case "register":
                String serverName = requestData[1];
                String serverIp = requestData[2];
                Address newServerAddress = new Entry(serverIp, getNextFreePort());
                locationMap.put(serverName, newServerAddress);
                response = new Message("NamingService", newServerAddress.toString());
                break;
            case "lookup":
                Address serverAddress = locationMap.get(requestData[1]);
                response = new Message("NamingService", serverAddress != null ? serverAddress.toString() : "Not Found");
                break;
            default:
                response = new Message("NamingService", "Unsupported operation");
                break;
        }
        return marshaller.marshal(response);
    }

    private static int getNextFreePort() {
        return currentPort++; // We're assuming that the ports are always available
    }
}
