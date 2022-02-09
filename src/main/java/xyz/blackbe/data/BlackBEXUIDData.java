package xyz.blackbe.data;

public class BlackBEXUIDData extends BlackBEData {
    private String success;
    private Integer status;
    private String message;
    private String version;
    private String codename;
    private Long time;
    private Data data;

    @Override
    public String toString() {
        return "BlackBEXUIDBean{" +
                "success='" + success + '\'' +
                ", status=" + status +
                ", message='" + message + '\'' +
                ", version='" + version + '\'' +
                ", codename='" + codename + '\'' +
                ", time=" + time +
                ", data=" + data +
                '}';
    }

    public static class Data {
        private String xuid;

        @Override
        public String toString() {
            return "Data{" +
                    "xuid='" + xuid + '\'' +
                    '}';
        }
    }
}
