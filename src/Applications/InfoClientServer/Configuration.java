package src.Applications.InfoClientServer;

import src.sORB.ByteCommunication.Registry.*;

public class Configuration {

    public Configuration() {
        Entry namingServiceEntry = new Entry("158.101.166.71", 51820);
        Registry.instance().put("NamingServer", namingServiceEntry);
    }

}
