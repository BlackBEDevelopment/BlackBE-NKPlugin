package icu.cattery.lixworth.blackbe;

import cn.nukkit.Player;
import cn.nukkit.scheduler.AsyncTask;
import com.google.gson.Gson;
import icu.cattery.lixworth.blackbe.entity.Api;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class QueryTask extends AsyncTask {

    public BlackBE blackBE;
    public Player player;

    public QueryTask(BlackBE blackBE,Player player) {
        this.blackBE = blackBE;
        this.player = player;
    }

    @Override
    public void onRun() {
        try {
            URL url = new URL(BlackBE.api_domain + "/v3/check?name=" + URLEncoder.encode(player.getName()+"&xuid="+player.getLoginChainData().getXUID(),"UTF-8"));

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("User-Agent", "RuMao/1.3");
            httpURLConnection.setConnectTimeout(5000);
            httpURLConnection.setReadTimeout(5000);
            httpURLConnection.connect();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String inputLine;

            while ((inputLine = bufferedReader.readLine()) != null) {
                Gson gson = new Gson();
                Api api = gson.fromJson(inputLine, Api.class);
                if (api.getExist()) {
                    this.blackBE.getServer().getScheduler().scheduleDelayedTask(this.blackBE, new KickRunnable(this.blackBE, player), 10);
                } else {
                    this.blackBE.getLogger().info(player.getName() + "无云黑记录，正常进入");
                }
            }
            bufferedReader.close();
            httpURLConnection.disconnect();
            // IOException MalformedURLException UnsupportedEncodingException
        } catch (Exception exception) {
            this.blackBE.getLogger().error("云黑插件出现错误 请稍后重试"+exception.getMessage());
        }
    }
}
