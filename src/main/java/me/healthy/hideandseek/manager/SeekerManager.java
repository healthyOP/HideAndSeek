package me.healthy.hideandseek.manager;

import me.healthy.hideandseek.utils.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.UUID;

public class SeekerManager {
    public void giveKit(final @NotNull UUID playerUUID){
        Player player = Bukkit.getPlayer(playerUUID);
        ItemStack seekerSword = new ItemStack(Material.WOODEN_SWORD);
        ItemMeta itemMeta = seekerSword.getItemMeta();
        itemMeta.setUnbreakable(true);
        itemMeta.displayName(ColorUtil.color("&mSeeker's Sword"));
        seekerSword.setItemMeta(itemMeta);

        player.getInventory().setItem(0,seekerSword);
    }

    public void prepareForStarting(final @NotNull UUID playerUUID){
        Player player = Bukkit.getPlayer(playerUUID);
        player.teleport(player.getRespawnLocation());
        player.addPotionEffects((Collection<PotionEffect>) PotionEffectType.BLINDNESS.createEffect(60,0));
        player.addPotionEffects((Collection<PotionEffect>) PotionEffectType.SLOW.createEffect(60,255));
}
}
