package icu.cattery.lixworth.blackbe;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.plugin.PluginBase;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


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
        this.getServer().getScheduler().scheduleAsyncTask(this,new QueryTask(this,player));

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