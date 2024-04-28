package me.healthy.hideandseek.commands;

import me.healthy.hideandseek.game.Game;
import me.healthy.hideandseek.game.GameState;
import me.healthy.hideandseek.utils.ColorUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class StartCommand implements CommandExecutor {
    private final Game game;

    public StartCommand(Game game) {
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

        if (!game.getGameState().equals(GameState.OFFLINE)) {
            player.sendMessage(ColorUtil.color("&cThe game already started, you cant start another one."));
            return true;
        }

        if (game.getPlugin().getServer().getOnlinePlayers().size() < 4) {
            player.sendMessage(ColorUtil.color(
                    "&cYou cant start the game while there are only "
                            + game.getPlugin().getServer().getOnlinePlayers().size() +
                            " players on the server.")
            );
            return true;
        }

        if (game.getGameLocation() == null) {
            player.sendMessage(ColorUtil.color("&cYou can't start the game with out that location!"));
            return true;
        }

        game.setState(GameState.COUNTDOWN);

        return true;
    }
}
