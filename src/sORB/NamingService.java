package src.sORB;

import src.sORB.ByteCommunication.Commons.Address;
import src.sORB.ByteCommunication.MessageMarshaller.*;
import src.sORB.ByteCommunication.Registry.*;
import src.sORB.ByteCommunication.RequestReply.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class NamingService {
    private static final Map<String, ServiceRecord> serviceMap = new ConcurrentHashMap<>();
    private static int currentPort = 2000;

    public static void main(String[] args) {
        new Configuration();
        Address myAddr = Registry.instance().get("NamingServer");
        Replyer replyer = new Replyer("NamingServer", myAddr);

        while (true) {
            replyer.receive_transform_and_send_feedback(NamingService::transform);
        }
    }

    private static void sendServiceCommand(Address address, String command) {
        Marshaller marshaller = new Marshaller();
        Message message = new Message("NamingService", command);
        byte[] messageBytes = marshaller.marshal(message);
        Requestor requestor = new Requestor("NamingService");

        byte[] responseBytes = requestor.deliver_and_wait_feedback(address, messageBytes);
        Message responseMessage = marshaller.unmarshal(responseBytes);
        System.out.println("Sent command to server: " + command);
        System.out.println("Response from server: " + responseMessage.data);
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
                serviceMap.put(serverName, new ServiceRecord(newServerAddress, new AtomicInteger(0)));
                response = new Message("NamingService", newServerAddress.toString());
                break;
            case "lookup":
                ServiceRecord record = serviceMap.get(requestData[1]);
                if (record != null) {
                    if (!record.isActive()) {
                        System.out.println("Activating service: " + record.address);
                        sendServiceCommand(record.address, "activate");
                        record.counter.incrementAndGet();
                    }
                    response = new Message("NamingService", record.address.toString());
                } else {
                    response = new Message("NamingService", "Not Found");
                }
                break;
            case "release":
                record = serviceMap.get(requestData[1]);
                if (record != null && record.counter.decrementAndGet() == 0) {
                    sendServiceCommand(record.address, "deactivate");
                    response = new Message("NamingService", "deactivate");
                } else {
                    response = new Message("NamingService", "Service released");
                }
                break;
            default:
                response = new Message("NamingService", "Unsupported operation");
                break;
        }
        return marshaller.marshal(response);
    }

    private static int getNextFreePort() {
        return currentPort++;
    }

    private static class ServiceRecord {
        Address address;
        AtomicInteger counter;

        // service is active if any clients are using it
        boolean isActive() {
            return counter.get() > 0;
        }

        ServiceRecord(Address address, AtomicInteger counter) {
            this.address = address;
            this.counter = counter;
        }
    }
}
