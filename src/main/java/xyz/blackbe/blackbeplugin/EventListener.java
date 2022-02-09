package xyz.blackbe.blackbeplugin;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJoinEvent;
import xyz.blackbe.blackbeplugin.data.BlackBEBlacklistCheckData;
import xyz.blackbe.blackbeplugin.event.BlackBEKickPlayerEvent;
import xyz.blackbe.blackbeplugin.task.CheckBlacklistTask;
import xyz.blackbe.blackbeplugin.util.BlackBEUtils;
import xyz.blackbe.blackbeplugin.util.BlacklistCacheManager;

import java.util.List;

@SuppressWarnings({"DuplicatedCode", "unused"})
public class EventListener implements Listener {
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (BlacklistCacheManager.isInBlacklist(player)) {
            BlackBEKickPlayerEvent blackBEKickPlayerEvent = new BlackBEKickPlayerEvent(player, BlackBEKickPlayerEvent.Reason.IN_BLACKLIST, true);
            Server.getInstance().getPluginManager().callEvent(blackBEKickPlayerEvent);
            if (!blackBEKickPlayerEvent.isCancelled() && blackBEKickPlayerEvent.isAutoKick()) {
                StringBuilder reasonStringBuilder = new StringBuilder("查询到您处在云黑名单中,以阻止您游玩本服务器.您的条目信息:\n");
                List<BlackBEBlacklistCheckData.Data.InfoBean> infoList = BlacklistCacheManager.getRecord(player).getCheckData().getInfo();
                for (BlackBEBlacklistCheckData.Data.InfoBean infoBean : infoList) {
                    reasonStringBuilder.append("    违规等级:").append(infoBean.getLevel());
                    reasonStringBuilder.append(",违规信息:").append(infoBean.getInfo());
                    reasonStringBuilder.append(",name:").append(infoBean.getName());
                    reasonStringBuilder.append(",black_id:").append(infoBean.getBlackId());
                    reasonStringBuilder.append("\n");
                }
                BlackBEUtils.kickPlayer(player, reasonStringBuilder.toString(), "黑名单中玩家进服(读取缓存)");
            }
        } else {
            BlackBEMain.getAsyncTaskWorker().submitTask(new CheckBlacklistTask(player));
        }
    }
}
