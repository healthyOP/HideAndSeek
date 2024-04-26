package me.healthy.hideandseek.game;

import me.healthy.hideandseek.HideAndSeek;
import me.healthy.hideandseek.listeners.BlockBreakListener;
import me.healthy.hideandseek.listeners.EntityDamageByEntityListener;
import me.healthy.hideandseek.manager.PlayerManager;
import me.healthy.hideandseek.manager.SeekerManager;
import me.healthy.hideandseek.tasks.CountDownTask;
import me.healthy.hideandseek.tasks.PlayersHidingTask;
import me.healthy.hideandseek.utils.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class Game {
    private final List<UUID>
            players,
            spectators;
    private final List<UUID>
            seekers,
            hiders;
    private GameState gameState;
    private HideAndSeek plugin;
    private PlayerManager playerManager;
    private ColorUtil colorUtil;
    private CountDownTask countDownTask;
    private PlayersHidingTask playersHidingTask;
    private SeekerManager seekerManager;
    public Game(HideAndSeek plugin) {
        this.plugin = plugin;
        this.players = new ArrayList<>();
        this.spectators = new ArrayList<>();
        this.seekers = new ArrayList<>();
        this.hiders = new ArrayList<>();
        setState(GameState.OFFLINE);
    }

    public void setState(GameState gameState){
        this.gameState = gameState;

        switch (gameState){
            case OFFLINE -> cleanup();
            case COUNTDOWN -> {
                plugin.getServer().getOnlinePlayers().forEach(this::addPlayer);
                for (UUID playerUUID : players) {
                    Player player = plugin.getServer().getPlayer(playerUUID);
                    if (player == null) continue;
                    player.teleport(Bukkit.getWorld(Bukkit.getWorldType()).getSpawnLocation());
                    //CHANGE LATER!!!
                    if (this.countDownTask != null) countDownTask.cancel();

                    this.countDownTask = new CountDownTask(this, plugin);
                    countDownTask.runTaskTimer(plugin, 0, 20);//change config period later!!!
                }}


            case HIDING_STATE -> {
                //roll seekers
                rollSeekers();

                for (int i = 0;i < 2;i++){
                    seekerManager.giveKit(seekers.get(i));
                    seekerManager.prepareForStarting(seekers.get(i));
                }

                if (this.playersHidingTask != null) playersHidingTask.cancel();
                this.playersHidingTask = new PlayersHidingTask(this, plugin);
                playersHidingTask.runTaskTimer(plugin, 0, 120);//change config period later!!!

            }
            case ACTIVE -> {
                sendMessage(plugin.getConfig().getString("game_is_activated"));
                if (players.size() == 1){
                    sendMessage(plugin.getConfig().getString("game_over"));
                    setState(GameState.OFFLINE);
                }
            }
        }
    }

    public void registerListeners(){
        plugin.getServer().getPluginManager().registerEvents(new BlockBreakListener(this),plugin);
        plugin.getServer().getPluginManager().registerEvents(new EntityDamageByEntityListener(this),plugin);
    }

    public void addPlayer(final @NotNull Player player){
        players.add(player.getUniqueId());
        player.getInventory().clear();
        player.setGameMode(GameMode.SURVIVAL);
        player.setFoodLevel(20);
        player.setHealth(20.00);
        playerManager.giveKit();
    }

    public void addSpectator(final @NotNull Player player){
        players.remove(player.getUniqueId());
        spectators.add(player.getUniqueId());
        player.getInventory().clear();
        player.setGameMode(GameMode.SPECTATOR);
    }
    public void sendMessage(@NotNull String str){
        for (UUID playerUUID : players){
            Player player = plugin.getServer().getPlayer(playerUUID);
            if (player == null) return;

            player.sendMessage(ColorUtil.color(str));
        }

        for (UUID spectatorUUID : spectators){
            Player spectator = plugin.getServer().getPlayer(spectatorUUID);
            if (spectator == null) return;

            spectator.sendMessage(ColorUtil.color(str));
        }
    }

    public boolean isPlaying(final @NotNull Player player){
        return players.contains(player.getUniqueId());
    }

    public boolean isSeeker(final @NotNull Player player) {
        return seekers.contains(player.getUniqueId());
    }

    public boolean isHider(final @NotNull Player player){
        return hiders.contains(player.getUniqueId());
    }

    public void rollSeekers(){
        List<UUID> seekers = new ArrayList<>(players);
        Collections.shuffle(seekers);
        for (int i = 0;i < 2;i++){
            this.seekers.add(players.get(i));
            this.players.remove(players.get(i));
        }
        this.hiders.addAll(players);
    }
    public void sendActionBar(@NotNull String str){
        for (UUID playerUUID : players){
            Player player = plugin.getServer().getPlayer(playerUUID);
            if (player == null) return;

            player.sendActionBar(ColorUtil.color(str));
        }

        for (UUID spectatorUUID : spectators){
            Player spectator = plugin.getServer().getPlayer(spectatorUUID);
            if (spectator == null) return;

            spectator.sendActionBar(ColorUtil.color(str));
        }
    }
    private void cleanup(){
        sendActionBar(plugin.getConfig().getString("game_is_ending"));

        for (UUID playerUUID : players) {
            Player player = plugin.getServer().getPlayer(playerUUID);
            if (player == null) continue;

            player.setGameMode(GameMode.SURVIVAL);
            player.getInventory().clear();
            player.teleport(Bukkit.getServer().getWorld("world").getSpawnLocation());
        }

        for (UUID playerUUID : spectators) {
            Player player = plugin.getServer().getPlayer(playerUUID);
            if (player == null) continue;

            player.setGameMode(GameMode.SURVIVAL);
            player.getInventory().clear();
            player.teleport(Bukkit.getServer().getWorld("world").getSpawnLocation());
        }
        players.clear();
        spectators.clear();
    }
}
