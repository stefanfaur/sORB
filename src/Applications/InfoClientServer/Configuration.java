package src.Applications.InfoClientServer;

import src.sORB.ByteCommunication.Registry.*;

public class Configuration {

    public Configuration() {
        Entry namingServiceEntry = new Entry("0.0.0.0", 9999);
        Registry.instance().put("NamingServer", namingServiceEntry);
    }

}
