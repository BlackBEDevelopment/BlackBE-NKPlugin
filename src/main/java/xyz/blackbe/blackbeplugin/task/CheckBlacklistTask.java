package xyz.blackbe.blackbeplugin.task;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.scheduler.AsyncTask;
import com.google.gson.Gson;
import xyz.blackbe.blackbeplugin.util.BlacklistCacheManager;
import xyz.blackbe.blackbeplugin.BlackBEMain;
import xyz.blackbe.blackbeplugin.constant.BlackBEApiConstants;
import xyz.blackbe.blackbeplugin.data.BlackBEBlacklistCheckData;
import xyz.blackbe.blackbeplugin.event.BlackBEKickPlayerEvent;
import xyz.blackbe.blackbeplugin.util.BlackBEUtils;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static xyz.blackbe.blackbeplugin.constant.BlackBEApiConstants.BLACKBE_API_HOST;

@SuppressWarnings({"DuplicatedCode", "unused"})
public class CheckBlacklistTask extends AsyncTask {
    private static final Gson GSON = new Gson();
    private final Player player;
    private BlackBEBlacklistCheckData data;
    private boolean checkSuccess = false;

    public CheckBlacklistTask(Player player) {
        this.player = player;
    }

    @Override
    public void onRun() {
        BufferedReader bufferedReader = null;
        HttpsURLConnection httpsURLConnection = null;
        try {
            URL url = new URL(String.format(BLACKBE_API_HOST + "check?name=%s&xuid=%s", URLEncoder.encode(player.getName(), StandardCharsets.UTF_8.name()), player.getLoginChainData().getXUID()));
            httpsURLConnection = BlackBEUtils.initHttpsURLConnection(url, 5000, 5000);
            httpsURLConnection.connect();

            if (httpsURLConnection.getResponseCode() == HttpsURLConnection.HTTP_OK) {

                bufferedReader = new BufferedReader(new InputStreamReader(httpsURLConnection.getInputStream()));
                String inputLine;
                StringBuilder sb = new StringBuilder(147);
                while ((inputLine = bufferedReader.readLine()) != null) {
                    sb.append(inputLine);
                }

                this.data = GSON.fromJson(sb.toString(), BlackBEBlacklistCheckData.class);
                this.checkSuccess = true;

                switch (this.data.getStatus()) {
                    case BlackBEApiConstants.CHECK_STATUS_IN_BLACKLIST: {
                        StringBuilder reasonStringBuilder = new StringBuilder("查询到您处在云黑名单中,以阻止您游玩本服务器.您的条目信息:\n");
                        if (this.data.getCheckData() != null) {
                            List<BlackBEBlacklistCheckData.Data.InfoBean> infoList = this.data.getCheckData().getInfo();
                            for (BlackBEBlacklistCheckData.Data.InfoBean infoBean : infoList) {
                                reasonStringBuilder.append("    违规等级:").append(infoBean.getLevel());
                                reasonStringBuilder.append(",违规信息:").append(infoBean.getInfo());
                                reasonStringBuilder.append(",name:").append(infoBean.getName());
                                reasonStringBuilder.append(",black_id:").append(infoBean.getBlackId());
                                reasonStringBuilder.append("\n");
                            }
                        }
                        BlackBEKickPlayerEvent blackBEKickPlayerEvent = new BlackBEKickPlayerEvent(player, BlackBEKickPlayerEvent.Reason.IN_BLACKLIST, true);
                        Server.getInstance().getPluginManager().callEvent(blackBEKickPlayerEvent);
                        if (!blackBEKickPlayerEvent.isCancelled() && blackBEKickPlayerEvent.isAutoKick()) {
                            BlackBEUtils.kickPlayer(player, reasonStringBuilder.toString(), "黑名单中玩家进服");
                        }
                        break;
                    }
                    case BlackBEApiConstants.CHECK_STATUS_NOT_IN_BLACKLIST: {
                        // 通过验证, 什么也不做
                        break;
                    }
                    case BlackBEApiConstants.CHECK_STATUS_LACK_PARAM: {
                        BlackBEKickPlayerEvent blackBEKickPlayerEvent = new BlackBEKickPlayerEvent(player, BlackBEKickPlayerEvent.Reason.LACK_PARAM, true);
                        Server.getInstance().getPluginManager().callEvent(blackBEKickPlayerEvent);
                        if (!blackBEKickPlayerEvent.isCancelled() && blackBEKickPlayerEvent.isAutoKick()) {
                            BlackBEUtils.kickPlayer(player, "云黑插件查询Api缺少查询参数,请联系管理员以寻求更多信息.URL=" + url.toExternalForm(), "云黑插件查询Api缺少查询参数");
                        }
                        break;
                    }
                    case BlackBEApiConstants.CHECK_STATUS_SERVER_ERROR: {
                        BlackBEKickPlayerEvent blackBEKickPlayerEvent = new BlackBEKickPlayerEvent(player, BlackBEKickPlayerEvent.Reason.SERVER_ERROR, true);
                        Server.getInstance().getPluginManager().callEvent(blackBEKickPlayerEvent);
                        if (!blackBEKickPlayerEvent.isCancelled() && blackBEKickPlayerEvent.isAutoKick()) {
                            BlackBEUtils.kickPlayer(player, "云黑平台出现内部错误,请联系云黑平台工作人员.", "云黑平台出现内部错误");
                        }
                        break;
                    }
                    default: {
                        BlackBEMain.getInstance().getLogger().notice("在查询时出现了未知状态码:" + this.data.getStatus());
                        BlackBEKickPlayerEvent blackBEKickPlayerEvent = new BlackBEKickPlayerEvent(player, BlackBEKickPlayerEvent.Reason.UNKNOWN_STATUS, true);
                        Server.getInstance().getPluginManager().callEvent(blackBEKickPlayerEvent);
                        if (!blackBEKickPlayerEvent.isCancelled() && blackBEKickPlayerEvent.isAutoKick()) {
                            BlackBEUtils.kickPlayer(player, "云黑平台出现内部错误,请联系云黑平台工作人员.", "未知状态码" + this.data.getStatus());
                        }
                    }
                    BlacklistCacheManager.putRecord(player, data);
                }
            } else {
                BlackBEMain.getInstance().getLogger().error("在连接至云黑查询平台时出现问题,状态码=" + httpsURLConnection.getResponseCode() + ",请求URL=" + url.toExternalForm());
                BlackBEKickPlayerEvent blackBEKickPlayerEvent = new BlackBEKickPlayerEvent(player, BlackBEKickPlayerEvent.Reason.URL_CONNECTION_ERROR, true);
                Server.getInstance().getPluginManager().callEvent(blackBEKickPlayerEvent);
                if (!blackBEKickPlayerEvent.isCancelled() && blackBEKickPlayerEvent.isAutoKick()) {
                    BlackBEUtils.kickPlayer(player, "云黑平台验证失败,请联系管理员以寻求更多帮助.", "云黑平台验证失败");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            BlackBEKickPlayerEvent blackBEKickPlayerEvent = new BlackBEKickPlayerEvent(player, BlackBEKickPlayerEvent.Reason.PLUGIN_EXCEPTION, true);
            Server.getInstance().getPluginManager().callEvent(blackBEKickPlayerEvent);
            if (!blackBEKickPlayerEvent.isCancelled() && blackBEKickPlayerEvent.isAutoKick()) {
                BlackBEUtils.kickPlayer(player, "云黑平台验证失败,请联系管理员以寻求更多帮助.", "云黑插件运行时发生异常");
            }
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (httpsURLConnection != null) {
                httpsURLConnection.disconnect();
            }
        }
    }

    public Player getPlayer() {
        return player;
    }

    public BlackBEBlacklistCheckData getData() {
        return data;
    }

    public boolean isCheckSuccess() {
        return checkSuccess;
    }
}
