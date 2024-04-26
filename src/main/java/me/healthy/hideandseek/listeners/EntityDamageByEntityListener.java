package me.healthy.hideandseek.listeners;

import me.healthy.hideandseek.game.Game;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EntityDamageByEntityListener implements Listener {
    private final Game game;

    public EntityDamageByEntityListener(Game game) {
        this.game = game;
    }

    @EventHandler
    private void onEntityDamage(EntityDamageByEntityEvent event){
        Player player = (Player) event.getDamager();
        if (!(event.getDamager() == player)) return;
        if (!game.isPlaying(player)) return;
        if (!game.isSeeker(player)) return;
        if (!game.isHider(player)) event.setCancelled(true);
        if (event.getEntity().equals(game.isSeeker(player)) && event.getDamager() == player) return;

        Player victim = (Player) event.getEntity();
        victim.setHealth(0);
        victim.teleport(victim.getRespawnLocation());
        game.addSpectator(victim);
    }
}
