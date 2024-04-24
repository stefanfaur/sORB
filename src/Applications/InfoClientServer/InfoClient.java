package src.Applications.InfoClientServer;

import src.sORB.ClientProxy;

public class InfoClient {
    public interface InfoService {
        String getRoadInfo(int roadID);
        String getTemp(String city);
    }

    public static void main(String[] args) {
        new Configuration();
        InfoService service = ClientProxy.getProxy(InfoService.class, "InfoServer");
        System.out.println("Road info: " + service.getRoadInfo(101));
        System.out.println("Temperature info: " + service.getTemp("New York"));
    }
}