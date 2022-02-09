package xyz.blackbe;

import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.TextFormat;
import xyz.blackbe.command.BlackBECommand;
import xyz.blackbe.constant.BlackBEApiConstants;


/**
 * BlackBE Nukkit Plugin
 * <p>
 * BlackBE云黑致力于维护MCBE的服务器环境，用最简单粗暴的方式，让广大服主开服省心、放心
 * https://minewiki.net/%E4%BA%91%E9%BB%91:BlackBE%E4%BA%91%E9%BB%91
 *
 * @author LixWorth <lixworth@outlook.com>
 * @website https://github.com/lixworth/BlackBE-NKPlugin
 */
public class BlackBEMain extends PluginBase {

    public static BlackBEMain instance;

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(new EventListener(), this);
        this.getServer().getCommandMap().register("blackbe", new BlackBECommand());
        this.getLogger().info(TextFormat.GREEN + "BlackBE云黑插件加载完成,当前API版本" + BlackBEApiConstants.API_VERSION);
    }

    public static BlackBEMain getInstance() {
        return instance;
    }
}