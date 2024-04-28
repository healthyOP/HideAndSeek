package me.healthy.hideandseek.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

public class ConfigurationUtility {
    public static void setLocation(@NotNull ConfigurationSection section, @NotNull Location location) {
        section.set("worldName", location.getWorld().getName());
        section.set("x", location.getX());
        section.set("y", location.getY());
        section.set("z", location.getZ());
        section.set("yaw", location.getYaw());
        section.set("pitch", location.getPitch());
    }

    public static @NotNull Location readLocation(ConfigurationSection section) {
        if (Bukkit.getWorld(section.getString("worldName")) == null)
            Bukkit.createWorld(new WorldCreator(section.getString("worldName")));

        return new Location(
                Bukkit.getWorld(section.getString("worldName")),
                section.getDouble("x"),
                section.getDouble("y"),
                section.getDouble("z"),
                (float) section.getDouble("yaw"),
                (float) section.getDouble("pitch")
        );
    }
}
