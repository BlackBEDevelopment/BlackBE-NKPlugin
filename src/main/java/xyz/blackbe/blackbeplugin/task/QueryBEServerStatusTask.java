package xyz.blackbe.blackbeplugin.task;

import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;
import com.google.gson.Gson;
import xyz.blackbe.blackbeplugin.BlackBEMain;
import xyz.blackbe.blackbeplugin.data.BlackBEMotdBEData;
import xyz.blackbe.blackbeplugin.util.BlackBEUtils;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static xyz.blackbe.blackbeplugin.constant.BlackBEApiConstants.BLACKBE_MOTD_API_HOST;

@SuppressWarnings("unused")
public class QueryBEServerStatusTask implements BlackBETask {
    private static final Gson GSON = new Gson();
    private final String host;
    private final int port;
    private final CommandSender sender;
    private BlackBEMotdBEData data;
    private boolean checkSuccess = false;

    public QueryBEServerStatusTask(String host, CommandSender sender) {
        this(host, 19132, sender);
    }

    public QueryBEServerStatusTask(String host, int port, CommandSender sender) {
        this.host = host;
        this.port = port;
        this.sender = sender;
    }

    @Override
    public void invoke() {
        this.sender.sendMessage("正在查询中,请稍后......");
        BufferedReader bufferedReader = null;
        HttpsURLConnection httpsURLConnection = null;
        try {
            URL url = new URL(String.format(BLACKBE_MOTD_API_HOST + "?host=%s:%s",
                    URLEncoder.encode(host, StandardCharsets.UTF_8.name()), // 中文域名表示很赞
                    port
            ));
            httpsURLConnection = BlackBEUtils.initHttpsURLConnection(url, 5000, 5000);
            httpsURLConnection.connect();

            if (httpsURLConnection.getResponseCode() == HttpsURLConnection.HTTP_OK) {

                bufferedReader = new BufferedReader(new InputStreamReader(httpsURLConnection.getInputStream()));
                String inputLine;
                StringBuilder sb = new StringBuilder(147);
                while ((inputLine = bufferedReader.readLine()) != null) {
                    sb.append(inputLine);
                }

                this.data = GSON.fromJson(sb.toString(), BlackBEMotdBEData.class);
                this.checkSuccess = true;
                this.sender.sendMessage(TextFormat.GREEN + "查询结果为:\n" + this.data.toQueryResult());
            } else {
                BlackBEMain.getInstance().getLogger().error("在连接至云黑查询平台时出现问题,状态码=" + httpsURLConnection.getResponseCode() + ",请求URL=" + url.toExternalForm());
                this.sender.sendMessage(TextFormat.RED + "查询失败!在连接至云黑查询平台时出现问题,状态码=" + httpsURLConnection.getResponseCode() + ",请求URL=" + url.toExternalForm());
            }
        } catch (IOException e) {
            e.printStackTrace();
            this.sender.sendMessage(TextFormat.RED + "查询失败!代码运行过程中发生异常.");
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

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public CommandSender getSender() {
        return sender;
    }

    public BlackBEMotdBEData getData() {
        return data;
    }

    public boolean isCheckSuccess() {
        return checkSuccess;
    }
}
