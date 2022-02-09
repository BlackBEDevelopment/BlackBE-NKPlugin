package xyz.blackbe.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BlackBEMotdJEData extends BlackBEData {
    private String status;
    private String host;
    private String motd;
    private Integer agreement;
    private String version;
    private Integer online;
    private Integer max;
    @SerializedName("sample")
    private List<Sample> sampleList;
    @Expose(serialize = false)
    private String favicon = "暂不支持查看";
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
        private String id;
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
