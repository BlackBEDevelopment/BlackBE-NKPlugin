package icu.cattery.lixworth.blackbe;

import cn.nukkit.Player;

public class KickRunnable implements Runnable {
    public BlackBE that;
    public Player player;

    public KickRunnable(BlackBE blackBE, Player that_player) {
        that = blackBE;
        player = that_player;
    }

    @Override
    public void run() {
        player.kick("你在BlackBE云黑存在封禁记录，已踢出");
        this.that.getLogger().notice("玩家 " + player.getName() + "存在云黑记录，已踢出。");
    }
}
