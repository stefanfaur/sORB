package Applications.InfoClientServer;

class InfoService implements InfoServiceInterface {
    public String getRoadInfo(int roadID) {
        String info = "Road " + roadID + " information: Construction ahead";
        return new String(info);
    }

    public String getTemp(String city) {
        String temp = "Temperature in " + city + " is 72°F";
        return new String(temp);
    }
}
