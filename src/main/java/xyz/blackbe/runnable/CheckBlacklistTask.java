package xyz.blackbe.runnable;

import cn.nukkit.Player;
import cn.nukkit.event.player.PlayerKickEvent;
import cn.nukkit.scheduler.AsyncTask;
import com.google.gson.Gson;
import xyz.blackbe.BlackBEMain;
import xyz.blackbe.constant.BlackBEApiConstants;
import xyz.blackbe.data.BlackBECheckBean;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static xyz.blackbe.constant.BlackBEApiConstants.BLACKBE_API_ADDRESS;

@SuppressWarnings({"DuplicatedCode", "unused"})
public class CheckBlacklistTask extends AsyncTask {
    private static final Gson GSON = new Gson();
    private final Player player;
    private BlackBECheckBean checkBean;
    private boolean checkSuccess = false;

    public CheckBlacklistTask(Player player) {
        this.player = player;
    }

    @Override
    public void onRun() {
        BufferedReader bufferedReader = null;
        HttpsURLConnection httpsURLConnection = null;
        try {
            URL url = new URL(String.format(BLACKBE_API_ADDRESS + "check?name=%s&xuid=%s", URLEncoder.encode(player.getName(), StandardCharsets.UTF_8.name()), player.getLoginChainData().getXUID()));
            System.out.println(url.toString());
            httpsURLConnection = (HttpsURLConnection) url.openConnection();
            httpsURLConnection.setRequestMethod("GET");
            httpsURLConnection.setRequestProperty("Accept-language", "zh-CN,zh;q=0.9");
            httpsURLConnection.setRequestProperty("Cache-control", "max-age=0");
            httpsURLConnection.setRequestProperty("Content-type", "application/json; charset=utf-8");
            httpsURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0");

            httpsURLConnection.setConnectTimeout(5000);
            httpsURLConnection.setReadTimeout(5000);
            httpsURLConnection.connect();

            if (httpsURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {

                bufferedReader = new BufferedReader(new InputStreamReader(httpsURLConnection.getInputStream()));
                String inputLine;
                StringBuilder sb = new StringBuilder(147);
                while ((inputLine = bufferedReader.readLine()) != null) {
                    sb.append(inputLine);
                }
                System.out.println(sb);
                this.checkBean = GSON.fromJson(sb.toString(), BlackBECheckBean.class);
                System.out.println(checkBean);
                this.checkSuccess = true;

                switch (this.checkBean.getStatus()) {
                    case BlackBEApiConstants.CHECK_STATUS_IN_BLACKLIST: {
                        StringBuilder reasonStringBuilder = new StringBuilder("查询到您处在云黑名单中,以阻止您游玩本服务器.您的条目信息:\n");
                        if (this.checkBean.getCheckData() != null) {
                            List<BlackBECheckBean.Data.InfoBean> infoList = this.checkBean.getCheckData().getInfo();
                            for (BlackBECheckBean.Data.InfoBean infoBean : infoList) {
                                reasonStringBuilder.append("    违规等级:").append(infoBean.getLevel());
                                reasonStringBuilder.append(",违规信息:").append(infoBean.getInfo());
                                reasonStringBuilder.append("\n");
                            }
                        }
                        player.kick(PlayerKickEvent.Reason.KICKED_BY_ADMIN, reasonStringBuilder.toString(), true);
                        break;
                    }
                    case BlackBEApiConstants.CHECK_STATUS_NOT_IN_BLACKLIST: {
                        // 通过验证, 什么也不做
                        break;
                    }
                    case BlackBEApiConstants.CHECK_STATUS_LACK_PARAM: {
                        player.kick(PlayerKickEvent.Reason.KICKED_BY_ADMIN, "云黑插件查询Api缺少查询参数,请联系管理员以寻求更多信息.URL=" + url.toExternalForm(), true);
                        break;
                    }
                    case BlackBEApiConstants.CHECK_STATUS_SERVER_ERROR: {
                        player.kick(PlayerKickEvent.Reason.KICKED_BY_ADMIN, "云黑平台出现内部错误,请联系云黑平台工作人员.", true);
                        break;
                    }
                    default: {
                        BlackBEMain.getInstance().getLogger().notice("在查询时出现了未知状态码:" + this.checkBean.getStatus());
                    }
                }
            } else {
                BlackBEMain.getInstance().getLogger().error("在连接至云黑查询平台时出现问题,状态码=" + httpsURLConnection.getResponseCode() + ",请求URL=" + url.toExternalForm());
                player.kick(PlayerKickEvent.Reason.KICKED_BY_ADMIN, "云黑平台验证失败,请联系管理员以寻求更多帮助.", true);
            }
        } catch (Exception e) {
            e.printStackTrace();
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

    public BlackBECheckBean getCheckBean() {
        return checkBean;
    }

    public boolean isCheckSuccess() {
        return checkSuccess;
    }
}
