package xyz.blackbe.blackbeplugin.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BlackBEMotdJEData extends BlackBEData {
    @Expose
    private String status;
    @Expose
    private String host;
    @Expose
    private String motd;
    @Expose
    private Integer agreement;
    @Expose
    private String version;
    @Expose
    private Integer online;
    @Expose
    private Integer max;
    @SerializedName("sample")
    @Expose
    private List<Sample> sampleList;
    @Expose(deserialize = false)
    private String favicon = "暂不支持查看";
    @Expose
    private Integer delay;

    @Override
    public String toString() {
        return "BlackBEMotdJEData{" +
                "status='" + status + '\'' +
                ", host='" + host + '\'' +
                ", motd='" + motd + '\'' +
                ", agreement=" + agreement +
                ", version='" + version + '\'' +
                ", online=" + online +
                ", max=" + max +
                ", sampleList=" + sampleList +
                ", favicon='" + favicon + '\'' +
                ", delay=" + delay +
                '}';
    }

    public static class Sample {
        @Expose
        private String id;
        @Expose
        private String name;

        @Override
        public String toString() {
            return "Sample{" +
                    "id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    '}';
        }
    }

}
