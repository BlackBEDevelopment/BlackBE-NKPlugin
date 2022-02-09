package xyz.blackbe.blackbeplugin.constant;

@SuppressWarnings("unused")
public class BlackBEApiConstants {
    public static final String API_VERSION = "3.0";
    public static final String BLACKBE_API_HOST = "https://api.blackbe.xyz/openapi/v3/";
    public static final String BLACKBE_UTIL_API_HOST = "https://api.blackbe.xyz/openapi/v3/utils/";
    public static final String BLACKBE_MOTD_API_HOST = "https://motdbe.blackbe.xyz/api";

    public static final int CHECK_STATUS_IN_BLACKLIST = 2000;
    public static final int CHECK_STATUS_NOT_IN_BLACKLIST = 2001;
    public static final int CHECK_STATUS_LACK_PARAM = 4000;
    public static final int CHECK_STATUS_SERVER_ERROR = 5000;

    public static final int QUERY_XUID_SUCCESS = 2000;
    public static final int QUERY_XUID_ERROR_PLAYER_NOT_EXISTENCE = 2001;
    public static final int QUERY_XUID_ERROR_NULL_PARAM = 4001;
    public static final int QUERY_XUID_SERVER_ERROR = 5005;

}
