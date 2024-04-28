package me.healthy.hideandseek.listeners;

import me.healthy.hideandseek.game.Game;
import me.healthy.hideandseek.game.GameState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {
    private final Game game;
    public PlayerQuitListener(Game game) {
        this.game = game;
    }
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        if (!game.isPlaying(event.getPlayer().getUniqueId())) return;
        if (game.isPlaying(event.getPlayer().getUniqueId()) && (game.getGameState() == GameState.HIDING_STATE
                || game.getGameState()== GameState.ACTIVE
                || game.getGameState() == GameState.COUNTDOWN)) {
            game.removePlayer(event.getPlayer());
        }
        if (game.isSpectating(event.getPlayer().getUniqueId()) && (game.getGameState() == GameState.HIDING_STATE
                || game.getGameState()== GameState.ACTIVE
                || game.getGameState() == GameState.COUNTDOWN)) {
            game.removeSpectator(event.getPlayer());
        }


    }
}
