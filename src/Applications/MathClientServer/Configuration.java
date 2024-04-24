package src.Applications.MathClientServer;

import src.sORB.ByteCommunication.Registry.Entry;
import src.sORB.ByteCommunication.Registry.Registry;

public class Configuration {

    public Configuration() {
        Entry namingServiceEntry = new Entry("158.101.166.71", 51820);
        Registry.instance().put("NamingServer", namingServiceEntry);
    }

}
