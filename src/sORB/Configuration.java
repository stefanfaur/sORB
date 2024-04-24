package src.sORB;

import src.sORB.ByteCommunication.Registry.Entry;
import src.sORB.ByteCommunication.Registry.Registry;

public class Configuration {

    public Configuration() {
        Entry namingServiceEntry = new Entry("127.0.0.1", 9999);
        Registry.instance().put("NamingServer", namingServiceEntry);
    }

}
