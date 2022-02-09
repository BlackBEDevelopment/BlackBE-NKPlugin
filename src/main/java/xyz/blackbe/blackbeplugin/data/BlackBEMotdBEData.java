package xyz.blackbe.blackbeplugin.data;

import com.google.gson.annotations.SerializedName;

public class BlackBEMotdBEData extends BlackBEData {
    private String status;
    private String host;
    private String motd;
    private Integer agreement;
    private String version;
    private Integer online;
    private Integer max;
    @SerializedName("level_name")
    private String levelName;
    private String gamemode;
    private Integer delay;

    @Override
    public String toString() {
        return "BlackBEMotdPEData{" +
                "status='" + status + '\'' +
                ", host='" + host + '\'' +
                ", motd='" + motd + '\'' +
                ", agreement=" + agreement +
                ", version='" + version + '\'' +
                ", online=" + online +
                ", max=" + max +
                ", levelName='" + levelName + '\'' +
                ", gamemode='" + gamemode + '\'' +
                ", delay=" + delay +
                '}';
    }
}
