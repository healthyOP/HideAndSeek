package me.healthy.hideandseek.utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Utility;
import org.jetbrains.annotations.NotNull;

public class ColorUtil {

    public static @NotNull Component color(@NotNull String str) {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(str);
    }
}
