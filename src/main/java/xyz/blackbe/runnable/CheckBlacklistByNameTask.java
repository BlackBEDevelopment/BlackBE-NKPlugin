package xyz.blackbe.runnable;

import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.event.player.PlayerKickEvent;
import cn.nukkit.scheduler.AsyncTask;
import cn.nukkit.utils.TextFormat;
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
public class CheckBlacklistByNameTask extends AsyncTask {
    private static final Gson GSON = new Gson();
    private final String playerName;
    private final String xuid;
    private final CommandSender sender;
    private BlackBECheckBean checkBean;
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
        BufferedReader bufferedReader = null;
        HttpsURLConnection httpsURLConnection = null;
        try {
            URL url = new URL(String.format(BLACKBE_API_ADDRESS + "check?name=%s&xuid=%s", URLEncoder.encode(playerName, StandardCharsets.UTF_8.name()), xuid));
            System.out.println(url.toString());
            httpsURLConnection = (HttpsURLConnection) url.openConnection();
            httpsURLConnection.setRequestMethod("GET");
            httpsURLConnection.setRequestProperty("User-Agent", "RuMao/1.2");
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
                sender.sendMessage(TextFormat.GREEN + "查询结果为:\n" + checkBean.toQueryResult());
            } else {
                BlackBEMain.getInstance().getLogger().error("在连接至云黑查询平台时出现问题,状态码=" + httpsURLConnection.getResponseCode() + ",请求URL=" + url.toExternalForm());
                sender.sendMessage(TextFormat.RED + "查询失败!在连接至云黑查询平台时出现问题,状态码=" + httpsURLConnection.getResponseCode() + ",请求URL=" + url.toExternalForm());
            }
        } catch (Exception e) {
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

    public BlackBECheckBean getCheckBean() {
        return checkBean;
    }

    public boolean isCheckSuccess() {
        return checkSuccess;
    }
}
