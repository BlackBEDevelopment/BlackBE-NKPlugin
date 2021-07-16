package icu.cattery.lixworth.blackbe;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.scheduler.AsyncTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import sun.net.www.http.HttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeUnit;


/**
 * BlackBE Nukkit Plugin
 *
 * BlackBE云黑致力于维护MCBE的服务器环境，用最简单粗暴的方式，让广大服主开服省心、放心
 * https://minewiki.net/%E4%BA%91%E9%BB%91:BlackBE%E4%BA%91%E9%BB%91
 *
 * @author LixWorth <lixworth@outlook.com>
 * @website https://github.com/lixworth/BlackBE-NKPlugin
 */
public class BlackBE extends PluginBase implements Listener {

    public static String api_version = "2.0[BETA]";
    public static String api_domain = "http://api.blackbe.xyz/api";

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);
        this.getLogger().info("BlackBE云黑插件加载完成,API版本" + api_version);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent playerJoinEvent) {
        Player player = playerJoinEvent.getPlayer();
        try {
            URL url = new URL(api_domain + "/check?v2=true&id=" + player.getName());

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(5000);
            httpURLConnection.setReadTimeout(5000);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String inputLine;

            while ((inputLine = bufferedReader.readLine()) != null) {
                Gson gson = new Gson();
                Api api = gson.fromJson(inputLine, Api.class);
                if (api.getErrorCode().equals("2002")) {
                    this.getServer().getScheduler().scheduleDelayedTask(this, new KickRunnable(this, player), 10);
                }
            }
            bufferedReader.close();
            httpURLConnection.disconnect();
        } catch (IOException e) {
            this.getLogger().error("云黑可能炸了哦");
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equals("blackbe")) {
            String message = "本服务器使用BlackBE云黑限制违禁玩家 API版本：" + api_version + "。 BlackBE云黑(https://blackbe.xyz)致力于维护MCBE的服务器环境，用最简单粗暴的方式，让广大服主开服省心、放心。";
            if (sender.isPlayer()) {
                this.getServer().getPlayer(sender.getName()).sendMessage(message);
            } else {
                this.getLogger().info(message);
            }
        }
        return true;
    }
}