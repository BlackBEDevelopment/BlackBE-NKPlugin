package xyz.blackbe.blackbeplugin.event;

import cn.nukkit.Player;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.player.PlayerEvent;

public class BlackBEBanPlayerEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean banned;

    public BlackBEBanPlayerEvent(Player player) {
        this(player, true);
    }

    public BlackBEBanPlayerEvent(Player player, boolean banned) {
        this.player = player;
        this.banned = banned;
    }

    public static HandlerList getHandlers() {
        return handlers;
    }

    public boolean isBanned() {
        return banned;
    }

    public void setBanned(boolean banned) {
        this.banned = banned;
    }
}
