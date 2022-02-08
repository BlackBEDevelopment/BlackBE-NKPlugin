package xyz.blackbe.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@SuppressWarnings({"unused", "MismatchedQueryAndUpdateOfCollection"})
public class BlackBECheckBean {
    private boolean success;
    private Integer status;
    private String message;
    private String version;
    private String codename;
    private Long time;
    @SerializedName("data")
    private Data checkData;

    private static final Gson FOR_DISPLAY_GSON_INSTANCE = new GsonBuilder().setPrettyPrinting().create();

    public boolean getSuccess() {
        return success;
    }

    public Integer getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getVersion() {
        return version;
    }

    public String getCodename() {
        return codename;
    }

    public Long getTime() {
        return time;
    }

    public Data getCheckData() {
        return checkData;
    }

    public String toQueryResult() {
        return FOR_DISPLAY_GSON_INSTANCE.toJson(this);
    }

    @Override
    public String toString() {
        return "BlackBECheckBean{" +
                "success='" + success + '\'' +
                ", status=" + status +
                ", message='" + message + '\'' +
                ", version='" + version + '\'' +
                ", codename='" + codename + '\'' +
                ", time='" + time + '\'' +
                ", checkData=" + (checkData != null ? checkData.toString() : null) +
                '}';
    }


    public static class Data {
        private boolean exist;
        private List<InfoBean> info;

        public boolean isExist() {
            return exist;
        }

        public List<InfoBean> getInfo() {
            return info;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "exist=" + exist +
                    ", info=" + info +
                    '}';
        }

        public static class InfoBean {
            private String uuid;
            private String name;
            @SerializedName("black_id")
            private String blackId;
            private String xuid;
            private String info;
            private Integer level;
            private Integer qq;

            public String getUUID() {
                return uuid;
            }

            public String getName() {
                return name;
            }

            public String getBlackId() {
                return blackId;
            }

            public String getXUID() {
                return xuid;
            }

            public String getInfo() {
                return info;
            }

            public Integer getLevel() {
                return level;
            }

            public Integer getQQ() {
                return qq;
            }

            @Override
            public String toString() {
                return "InfoBean{" +
                        "uuid='" + uuid + '\'' +
                        ", name='" + name + '\'' +
                        ", blackId='" + blackId + '\'' +
                        ", xuid='" + xuid + '\'' +
                        ", info='" + info + '\'' +
                        ", level=" + level +
                        ", qq=" + qq +
                        '}';
            }
        }
    }
}

