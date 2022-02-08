package xyz.blackbe.runnable;

import cn.nukkit.Player;
import xyz.blackbe.BlackBEMain;

public class KickRunnable implements BlackBERunnable {
    public BlackBEMain that;
    public Player player;

    public KickRunnable(BlackBEMain blackBEMain, Player that_player) {
        that = blackBEMain;
        player = that_player;
    }

    @Override
    public void run() {
        player.kick("你在BlackBE云黑存在封禁记录，已踢出");
        this.that.getLogger().notice("玩家 " + player.getName() + "存在云黑记录，已踢出。");
    }
}
