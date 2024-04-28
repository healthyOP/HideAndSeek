package me.healthy.hideandseek.listeners;

import me.healthy.hideandseek.game.Game;
import me.healthy.hideandseek.game.GameState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
    private final Game game;
    public PlayerJoinListener(Game game) {
        this.game = game;
    }
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        if (!game.isPlaying(event.getPlayer().getUniqueId()) && game.getGameState() == GameState.COUNTDOWN) {
            game.addPlayer(event.getPlayer());
        }
        if (!game.isPlaying(event.getPlayer().getUniqueId()) && (game.getGameState() == GameState.HIDING_STATE || game.getGameState()== GameState.ACTIVE)) {
            game.addSpectator(event.getPlayer());
        }


    }
}
