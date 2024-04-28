package me.healthy.hideandseek.commands;

import me.healthy.hideandseek.HideAndSeek;
import me.healthy.hideandseek.game.Game;
import me.healthy.hideandseek.utils.ColorUtil;
import me.healthy.hideandseek.utils.ConfigurationUtility;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SetGameLocationCommand implements CommandExecutor {
    private final Game game;
    private final HideAndSeek plugin;

    public SetGameLocationCommand(Game game, HideAndSeek plugin) {
        this.game = game;
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        Player player = (Player) commandSender;
        if (!(commandSender instanceof Player)) {
            player.sendMessage(ColorUtil.color("&cYou must to be a player to execute this command."));
            return true;
        }

        if (!player.hasPermission("admin")) {
            player.sendMessage(ColorUtil.color("&cYou don't have permission to execute this command."));
            return true;
        }

        game.setGameLocation(player.getLocation());
        ConfigurationUtility.setLocation(plugin.getConfig().createSection("spawn"), player.getLocation());

        plugin.saveConfig();

        return true;
    }
    }

