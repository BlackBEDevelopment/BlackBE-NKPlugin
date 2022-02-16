package icu.cattery.lixworth.blackbe.entity;

public class Api {
    private String success;
    private String message;
    private String status;
    private Data data;
    private String version;

    public String getErrorCode() {
        return this.status;
    }

    public Boolean getExist() {
        return this.data.exist;
    }
}
