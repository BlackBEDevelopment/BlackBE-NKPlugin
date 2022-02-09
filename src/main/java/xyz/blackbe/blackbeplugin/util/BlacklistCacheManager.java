package xyz.blackbe.blackbeplugin.util;

import cn.nukkit.Player;
import xyz.blackbe.blackbeplugin.constant.BlackBEApiConstants;
import xyz.blackbe.blackbeplugin.data.BlackBEBlacklistCheckData;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BlacklistCacheManager {
    private static final Map<String, BlackBEBlacklistCheckData> blacklistCheckDataMap = new ConcurrentHashMap<>();

    public static boolean isInBlacklist(Player player) {
        return isInBlacklist(player.getName());
    }

    public static boolean isInBlacklist(String name) {
        BlackBEBlacklistCheckData data = blacklistCheckDataMap.get(name);
        return data != null && data.getStatus() == BlackBEApiConstants.CHECK_STATUS_IN_BLACKLIST;
    }

    public static boolean isInWhitelist(Player player) {
        return isInWhitelist(player.getName());
    }

    public static boolean isInWhitelist(String name) {
        BlackBEBlacklistCheckData data = blacklistCheckDataMap.get(name);
        return data == null || data.getStatus() == BlackBEApiConstants.CHECK_STATUS_NOT_IN_BLACKLIST;
    }

    public static void putRecord(Player player, BlackBEBlacklistCheckData data) {
        putRecord(player.getName(), data);
    }

    public static void putRecord(String name, BlackBEBlacklistCheckData data) {
        if (data != null) {
            if (data.getStatus() == BlackBEApiConstants.CHECK_STATUS_IN_BLACKLIST || data.getStatus() == BlackBEApiConstants.CHECK_STATUS_NOT_IN_BLACKLIST) {
                blacklistCheckDataMap.put(name, data);
            }
        }
    }

    public static BlackBEBlacklistCheckData getRecord(Player player) {
        return getRecord(player.getName());
    }

    public static BlackBEBlacklistCheckData getRecord(String name) {
        return blacklistCheckDataMap.get(name);
    }

    public static void clear() {
        blacklistCheckDataMap.clear();
    }
}
