package xyz.blackbe.util;

import cn.nukkit.Player;
import cn.nukkit.event.player.PlayerKickEvent;
import cn.nukkit.network.protocol.DisconnectPacket;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.URL;

public class BlackBEUtils {
    public static void kickPlayer(Player player, String displayMessage) {
        kickPlayer(player, displayMessage, "被云黑插件踢出");
    }

    public static void kickPlayer(Player player, String displayMessage, String kickMessage) {
        DisconnectPacket pk = new DisconnectPacket();
        pk.hideDisconnectionScreen = false;
        pk.message = displayMessage;
        player.dataPacket(pk);
        player.kick(PlayerKickEvent.Reason.KICKED_BY_ADMIN, kickMessage, true);
    }

    public static HttpsURLConnection initHttpsURLConnection(URL url, int connectionTimeout, int readTimeout) {
        HttpsURLConnection httpsURLConnection = null;
        try {
            httpsURLConnection = (HttpsURLConnection) url.openConnection();
            httpsURLConnection.setRequestMethod("GET");
            httpsURLConnection.setRequestProperty("Accept-language", "zh-CN,zh;q=0.9");
            httpsURLConnection.setRequestProperty("Cache-control", "max-age=0");
            httpsURLConnection.setRequestProperty("Content-type", "application/json; charset=utf-8");
            httpsURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0");

            httpsURLConnection.setConnectTimeout(connectionTimeout);
            httpsURLConnection.setReadTimeout(readTimeout);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return httpsURLConnection;
    }
}
