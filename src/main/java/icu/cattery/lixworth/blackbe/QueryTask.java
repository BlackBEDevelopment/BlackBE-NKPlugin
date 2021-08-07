package icu.cattery.lixworth.blackbe;

import cn.nukkit.Player;
import cn.nukkit.scheduler.AsyncTask;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

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
            URL url = new URL(BlackBE.api_domain + "/check?v2=true&id=" + player.getName());

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(5000);
            httpURLConnection.setReadTimeout(5000);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String inputLine;

            while ((inputLine = bufferedReader.readLine()) != null) {
                Gson gson = new Gson();
                Api api = gson.fromJson(inputLine, Api.class);
                if (api.getErrorCode().equals("2002")) {
                    this.blackBE.getServer().getScheduler().scheduleDelayedTask(this.blackBE, new KickRunnable(this.blackBE, player), 10);
                } else {
                    this.blackBE.getLogger().info(player.getName() + "无云黑记录，正常进入");
                }
            }
            bufferedReader.close();
            httpURLConnection.disconnect();
        } catch (Exception e) {
            this.blackBE.getLogger().error("云黑可能炸了哦");
        }
    }
}
