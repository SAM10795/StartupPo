package ecellapp.sam10795.com.startuppo;

/**
 * Created by SAM10795 on 21-01-2017.
 */

public class Tower {
    private String name;
    private double lat;
    private double lon;
    private String CapUser;
    private int userscore;
    private String CapSUP;
    private int CapCount;
    private int SUPLv;
    private String CapHostel;

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public String getCapHostel() {
        return CapHostel;
    }

    public String getCapUser() {
        return CapUser;
    }

    public String getName() {
        return name;
    }

    public int getUserscore() {
        return userscore;
    }

    public int getSUPLv() {
        return SUPLv;
    }

    public int getCapCount() {
        return CapCount;
    }

    public void setSUPLv(int SUPLv) {
        this.SUPLv = SUPLv;
    }

    public String getCapSUP() {
        return CapSUP;
    }

    public void setUserscore(int userscore) {
        this.userscore = userscore;
    }

    public void setCapSUP(String capSUP) {
        CapSUP = capSUP;
    }

    public void setCapHostel(String capHostel) {
        CapHostel = capHostel;
    }

    public void setCapUser(String capUser) {
        CapUser = capUser;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCapCount(int capCount) {
        CapCount = capCount;
    }
}
