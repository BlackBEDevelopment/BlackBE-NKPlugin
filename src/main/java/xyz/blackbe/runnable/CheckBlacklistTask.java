package xyz.blackbe.runnable;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.scheduler.AsyncTask;
import com.google.gson.Gson;
import xyz.blackbe.BlackBEMain;
import xyz.blackbe.constant.BlackBEApiConstants;
import xyz.blackbe.data.BlackBECheckData;
import xyz.blackbe.event.BlackBEKickEvent;
import xyz.blackbe.util.BlackBEUtils;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static xyz.blackbe.constant.BlackBEApiConstants.BLACKBE_API_HOST;

@SuppressWarnings({"DuplicatedCode", "unused"})
public class CheckBlacklistTask extends AsyncTask {
    private static final Gson GSON = new Gson();
    private final Player player;
    private BlackBECheckData data;
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

                this.data = GSON.fromJson(sb.toString(), BlackBECheckData.class);
                this.checkSuccess = true;

                switch (this.data.getStatus()) {
                    case BlackBEApiConstants.CHECK_STATUS_IN_BLACKLIST: {
                        StringBuilder reasonStringBuilder = new StringBuilder("查询到您处在云黑名单中,以阻止您游玩本服务器.您的条目信息:\n");
                        if (this.data.getCheckData() != null) {
                            List<BlackBECheckData.Data.InfoBean> infoList = this.data.getCheckData().getInfo();
                            for (BlackBECheckData.Data.InfoBean infoBean : infoList) {
                                reasonStringBuilder.append("    违规等级:").append(infoBean.getLevel());
                                reasonStringBuilder.append(",违规信息:").append(infoBean.getInfo());
                                reasonStringBuilder.append(",name:").append(infoBean.getName());
                                reasonStringBuilder.append(",black_id:").append(infoBean.getBlackId());
                                reasonStringBuilder.append("\n");
                            }
                        }
                        BlackBEKickEvent blackBEKickEvent = new BlackBEKickEvent(player, BlackBEKickEvent.Reason.IN_BLACKLIST, true);
                        Server.getInstance().getPluginManager().callEvent(blackBEKickEvent);
                        if (!blackBEKickEvent.isCancelled() && blackBEKickEvent.isAutoKick()) {
                            BlackBEUtils.kickPlayer(player, reasonStringBuilder.toString(), "黑名单中玩家进服");
                        }
                        break;
                    }
                    case BlackBEApiConstants.CHECK_STATUS_NOT_IN_BLACKLIST: {
                        // 通过验证, 什么也不做
                        break;
                    }
                    case BlackBEApiConstants.CHECK_STATUS_LACK_PARAM: {
                        BlackBEKickEvent blackBEKickEvent = new BlackBEKickEvent(player, BlackBEKickEvent.Reason.LACK_PARAM, true);
                        Server.getInstance().getPluginManager().callEvent(blackBEKickEvent);
                        if (!blackBEKickEvent.isCancelled() && blackBEKickEvent.isAutoKick()) {
                            BlackBEUtils.kickPlayer(player, "云黑插件查询Api缺少查询参数,请联系管理员以寻求更多信息.URL=" + url.toExternalForm(), "云黑插件查询Api缺少查询参数");
                        }
                        break;
                    }
                    case BlackBEApiConstants.CHECK_STATUS_SERVER_ERROR: {
                        BlackBEKickEvent blackBEKickEvent = new BlackBEKickEvent(player, BlackBEKickEvent.Reason.SERVER_ERROR, true);
                        Server.getInstance().getPluginManager().callEvent(blackBEKickEvent);
                        if (!blackBEKickEvent.isCancelled() && blackBEKickEvent.isAutoKick()) {
                            BlackBEUtils.kickPlayer(player, "云黑平台出现内部错误,请联系云黑平台工作人员.", "云黑平台出现内部错误");
                        }
                        break;
                    }
                    default: {
                        BlackBEMain.getInstance().getLogger().notice("在查询时出现了未知状态码:" + this.data.getStatus());
                        BlackBEKickEvent blackBEKickEvent = new BlackBEKickEvent(player, BlackBEKickEvent.Reason.UNKNOWN_STATUS, true);
                        Server.getInstance().getPluginManager().callEvent(blackBEKickEvent);
                        if (!blackBEKickEvent.isCancelled() && blackBEKickEvent.isAutoKick()) {
                            BlackBEUtils.kickPlayer(player, "云黑平台出现内部错误,请联系云黑平台工作人员.", "未知状态码" + this.data.getStatus());
                        }
                    }
                }
            } else {
                BlackBEMain.getInstance().getLogger().error("在连接至云黑查询平台时出现问题,状态码=" + httpsURLConnection.getResponseCode() + ",请求URL=" + url.toExternalForm());
                BlackBEKickEvent blackBEKickEvent = new BlackBEKickEvent(player, BlackBEKickEvent.Reason.URL_CONNECTION_ERROR, true);
                Server.getInstance().getPluginManager().callEvent(blackBEKickEvent);
                if (!blackBEKickEvent.isCancelled() && blackBEKickEvent.isAutoKick()) {
                    BlackBEUtils.kickPlayer(player, "云黑平台验证失败,请联系管理员以寻求更多帮助.", "云黑平台验证失败");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            BlackBEKickEvent blackBEKickEvent = new BlackBEKickEvent(player, BlackBEKickEvent.Reason.PLUGIN_EXCEPTION, true);
            Server.getInstance().getPluginManager().callEvent(blackBEKickEvent);
            if (!blackBEKickEvent.isCancelled() && blackBEKickEvent.isAutoKick()) {
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

    public BlackBECheckData getData() {
        return data;
    }

    public boolean isCheckSuccess() {
        return checkSuccess;
    }
}
