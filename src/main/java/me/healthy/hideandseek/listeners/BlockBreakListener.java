package me.healthy.hideandseek.listeners;

import me.healthy.hideandseek.game.Game;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakListener implements Listener {
    private final Game game;

    public BlockBreakListener(Game game) {
        this.game = game;
    }

    @EventHandler
    private void onBlockBreak(BlockBreakEvent event){
        event.setCancelled(true);
    }
}
