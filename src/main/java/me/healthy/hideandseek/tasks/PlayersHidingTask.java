package me.healthy.hideandseek.tasks;

import me.healthy.hideandseek.HideAndSeek;
import me.healthy.hideandseek.game.Game;
import me.healthy.hideandseek.game.GameState;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayersHidingTask extends BukkitRunnable {
    private int timeLeft = 60;
    private final Game game;
    private final HideAndSeek plugin;
    public PlayersHidingTask(Game game, HideAndSeek plugin) {
        this.game = game;
        this.plugin = plugin;
    }
    @Override
    public void run() {
        if (timeLeft <= 0) {
            cancel();
            game.setState(GameState.ACTIVE);
        }
        game.sendMessage(plugin.getConfig().getString("game_will_start_in" + timeLeft));
        timeLeft--;
    }
}
