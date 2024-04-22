package InfoClientServer;

import ByteCommunication.Registry.*;

public class Configuration {

    public Configuration() {
        Entry serverEntry = new Entry("127.0.0.1", 1111);
        Registry.instance().put("Server", serverEntry);

        Entry client1Entry = new Entry("127.0.0.1", 1112);
        Registry.instance().put("Client1", client1Entry);

        Entry client2Entry = new Entry("127.0.0.1", 1113);
        Registry.instance().put("Client2", client2Entry);

    }

}
