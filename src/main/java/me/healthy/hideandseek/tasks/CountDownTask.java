package me.healthy.hideandseek.tasks;

import me.healthy.hideandseek.HideAndSeek;
import me.healthy.hideandseek.game.Game;
import me.healthy.hideandseek.game.GameState;
import org.bukkit.scheduler.BukkitRunnable;

public class CountDownTask extends BukkitRunnable {
    private int timeLeft = 30;
    private final Game game;
    private final HideAndSeek plugin;
    public CountDownTask(Game game, HideAndSeek plugin) {
        this.game = game;
        this.plugin = plugin;
    }

    @Override
    public void run() {
        if (timeLeft <= 0){
            cancel();
            game.setState(GameState.HIDING_STATE);
        }
        game.sendMessage(plugin.getConfig().getString("hiding_will_start_in" + timeLeft));
        timeLeft--;

    }
}
