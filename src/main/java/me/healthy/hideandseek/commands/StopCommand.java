package me.healthy.hideandseek.commands;

import me.healthy.hideandseek.game.Game;
import me.healthy.hideandseek.game.GameState;
import me.healthy.hideandseek.utils.ColorUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class StopCommand implements CommandExecutor {
    private final Game game;

    public StopCommand(Game game) {
        this.game = game;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ColorUtil.color("&cYou must to be a player to execute this command."));
            return true;
        }

        if (!player.hasPermission("admin")) {
            player.sendMessage(ColorUtil.color("&cYou don't have permission to execute this command."));
            return true;
        }

        if (game.getGameState() != GameState.ACTIVE) {
            player.sendMessage(ColorUtil.color("&cYou cant end the game because there is no game online"));
            return true;
        }

        game.setState(GameState.OFFLINE);

        return true;
    }
}
