package xyz.blackbe.event;

import cn.nukkit.Player;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.player.PlayerEvent;

public class BlackBEKickEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final Reason kickReason;
    private boolean autoKick;

    public BlackBEKickEvent(Player player, Reason kickReason) {
        this(player, kickReason, true);
    }

    public BlackBEKickEvent(Player player, Reason kickReason, boolean autoKick) {
        this.player = player;
        this.kickReason = kickReason;
        this.autoKick = autoKick;
    }

    public static HandlerList getHandlers() {
        return handlers;
    }

    public Reason getKickReason() {
        return kickReason;
    }

    public boolean isAutoKick() {
        return autoKick;
    }

    public void setAutoKick(boolean autoKick) {
        this.autoKick = autoKick;
    }

    public enum Reason {
        IN_BLACKLIST,
        LACK_PARAM,
        SERVER_ERROR,
        PLUGIN_EXCEPTION,
        URL_CONNECTION_ERROR,
        UNKNOWN_STATUS
    }
}
