package me.healthy.hideandseek;

import me.healthy.hideandseek.game.Game;
import org.bukkit.plugin.java.JavaPlugin;

public final class HideAndSeek extends JavaPlugin {
    private Game game;
    @Override
    public void onEnable() {
        // Plugin startup logic
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        game.registerListeners();

    }
}
