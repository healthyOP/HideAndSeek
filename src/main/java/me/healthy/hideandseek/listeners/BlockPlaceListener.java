package me.healthy.hideandseek.listeners;

import me.healthy.hideandseek.game.Game;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlaceListener implements Listener {
    private final Game game;

    public BlockPlaceListener(Game game) {
        this.game = game;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event){
        if (!game.isPlaying(event.getPlayer().getUniqueId())) return;
        event.setBuild(false);
    }

}
