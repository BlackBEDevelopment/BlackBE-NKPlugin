package xyz.blackbe.runnable;

import cn.nukkit.command.CommandSender;
import cn.nukkit.scheduler.AsyncTask;
import cn.nukkit.utils.TextFormat;
import com.google.gson.Gson;
import xyz.blackbe.BlackBEMain;
import xyz.blackbe.data.BlackBEMotdBEData;
import xyz.blackbe.data.BlackBEMotdJEData;
import xyz.blackbe.util.BlackBEUtils;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static xyz.blackbe.constant.BlackBEApiConstants.BLACKBE_MOTD_API_HOST;

public class QueryJEServerStatusTask extends AsyncTask {
    private static final Gson GSON = new Gson();
    private final String host;
    private final int port;
    private final CommandSender sender;
    private BlackBEMotdJEData data;
    private boolean checkSuccess = false;

    public QueryJEServerStatusTask(String host, CommandSender sender) {
        this(host, 19132, sender);
    }

    public QueryJEServerStatusTask(String host, int port, CommandSender sender) {
        this.host = host;
        this.port = port;
        this.sender = sender;
    }

    @Override
    public void onRun() {
        sender.sendMessage("正在查询中,请稍后......");
        BufferedReader bufferedReader = null;
        HttpsURLConnection httpsURLConnection = null;
        try {
            URL url = new URL(String.format(BLACKBE_MOTD_API_HOST + "/java?host=%s:%s",
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

                this.data = GSON.fromJson(sb.toString(), BlackBEMotdJEData.class);
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
}
