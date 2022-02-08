package xyz.blackbe;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerKickEvent;
import cn.nukkit.event.player.PlayerLocallyInitializedEvent;
import xyz.blackbe.runnable.CheckBlacklistTask;

public class EventListener implements Listener {
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Server.getInstance().getScheduler().scheduleAsyncTask(BlackBEMain.getInstance(), new CheckBlacklistTask(player));
    }

    @EventHandler
    public void dd(PlayerLocallyInitializedEvent event) {
        Player p = event.getPlayer();
        System.out.println(p.isConnected() + "eee" + p.loggedIn);
        event.getPlayer().kick(PlayerKickEvent.Reason.KICKED_BY_ADMIN, "草", true);
    }
}
