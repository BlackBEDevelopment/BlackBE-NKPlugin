package xyz.blackbe.blackbeplugin.task;

import cn.nukkit.command.CommandSender;
import cn.nukkit.scheduler.AsyncTask;
import cn.nukkit.utils.TextFormat;
import com.google.gson.Gson;
import xyz.blackbe.blackbeplugin.BlackBEMain;
import xyz.blackbe.blackbeplugin.constant.BlackBEApiConstants;
import xyz.blackbe.blackbeplugin.data.BlackBEBlacklistCheckData;
import xyz.blackbe.blackbeplugin.util.BlackBEUtils;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@SuppressWarnings({"DuplicatedCode", "unused"})
public class CheckBlacklistByNameTask extends AsyncTask {
    private static final Gson GSON = new Gson();
    private final String playerName;
    private final String xuid;
    private final CommandSender sender;
    private BlackBEBlacklistCheckData data;
    private boolean checkSuccess = false;

    public CheckBlacklistByNameTask(String playerName, String xuid, CommandSender sender) {
        this.playerName = playerName;
        this.xuid = xuid;
        this.sender = sender;
    }

    public CheckBlacklistByNameTask(String playerName, CommandSender sender) {
        this.playerName = playerName;
        this.xuid = "";
        this.sender = sender;
    }

    @Override
    public void onRun() {
        sender.sendMessage("正在查询中,请稍后......");
        BufferedReader bufferedReader = null;
        HttpsURLConnection httpsURLConnection = null;
        try {
            URL url = new URL(String.format(BlackBEApiConstants.BLACKBE_API_HOST + "check?name=%s&xuid=%s", URLEncoder.encode(playerName, StandardCharsets.UTF_8.name()), xuid));
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
                sender.sendMessage(TextFormat.GREEN + "查询结果为:\n" + data.toQueryResult());
            } else {
                BlackBEMain.getInstance().getLogger().error("在连接至云黑查询平台时出现问题,状态码=" + httpsURLConnection.getResponseCode() + ",请求URL=" + url.toExternalForm());
                sender.sendMessage(TextFormat.RED + "查询失败!在连接至云黑查询平台时出现问题,状态码=" + httpsURLConnection.getResponseCode() + ",请求URL=" + url.toExternalForm());
            }
        } catch (IOException e) {
            e.printStackTrace();
            sender.sendMessage(TextFormat.RED + "查询失败!代码运行过程中发生异常.");
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

    public static Gson getGSON() {
        return GSON;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getXuid() {
        return xuid;
    }

    public CommandSender getSender() {
        return sender;
    }

    public BlackBEBlacklistCheckData getData() {
        return data;
    }

    public boolean isCheckSuccess() {
        return checkSuccess;
    }
}
