package me.healthy.hideandseek.listeners;

import me.healthy.hideandseek.game.Game;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class PlayerDropItemListener implements Listener {
    private final Game game;

    public PlayerDropItemListener(Game game) {
        this.game = game;
    }
    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event){
        if (game.isPlaying(event.getItemDrop().getUniqueId()) || game.isSpectating(event.getPlayer().getUniqueId())) event.setCancelled(true);

    }
}
