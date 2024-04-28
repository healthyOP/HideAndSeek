package me.healthy.hideandseek;

import me.healthy.hideandseek.commands.SetGameLocationCommand;
import me.healthy.hideandseek.commands.StartCommand;
import me.healthy.hideandseek.commands.StopCommand;
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

        getCommand("startgame").setExecutor(new StartCommand(game));
        getCommand("stopgame").setExecutor(new StopCommand(game));
        getCommand("gamesetlocation").setExecutor(new SetGameLocationCommand(game, this));
    }
}
