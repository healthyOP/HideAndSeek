package me.healthy.hideandseek.game;

import lombok.Getter;
import lombok.Setter;
import me.healthy.hideandseek.HideAndSeek;
import me.healthy.hideandseek.listeners.*;
import me.healthy.hideandseek.manager.PlayerManager;
import me.healthy.hideandseek.manager.SeekerManager;
import me.healthy.hideandseek.tasks.CountDownTask;
import me.healthy.hideandseek.tasks.PlayersHidingTask;
import me.healthy.hideandseek.utils.ColorUtil;
import me.healthy.hideandseek.utils.ConfigurationUtility;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;
@Getter
public class Game {
    private final HashMap<UUID, Boolean>
            players,
            spectators;
    private GameState gameState;
    @Setter private Location gameLocation;
    private HideAndSeek plugin;
    private PlayerManager playerManager;
    private ColorUtil colorUtil;
    private CountDownTask countdownTask;
    private PlayersHidingTask playersHidingTask;
    private SeekerManager seekerManager;
    public Game(HideAndSeek plugin) {
        this.plugin = plugin;
        this.players = new HashMap<>();
        this.spectators = new HashMap<>();
        setState(GameState.OFFLINE);

        if (plugin.getConfig().getConfigurationSection("spawn") == null) return;

        gameLocation = ConfigurationUtility.readLocation(plugin.getConfig().getConfigurationSection("spawn"));
    }

    public void setState(GameState gameState){
        this.gameState = gameState;

        switch (gameState){
            case OFFLINE -> cleanup();
            case COUNTDOWN -> {
                plugin.getServer().getOnlinePlayers().forEach(this::addPlayer);
                for (UUID playerUUID : players.keySet()) {
                    Player player = plugin.getServer().getPlayer(playerUUID);
                    if (player == null) continue;
                    player.teleport(Bukkit.getWorld(Bukkit.getWorldType()).getSpawnLocation());
                    //CHANGE LATER!!!
                    if (this.countdownTask != null) countdownTask.cancel();

                    this.countdownTask = new CountDownTask(this, plugin);
                    countdownTask.runTaskTimer(plugin, 0, 20);//change config period later!!!
                }}


            case HIDING_STATE -> {
                //roll seekers
                rollSeekers();

                for (int i = 0;i < 2;i++){
                    seekerManager.giveKit(getSeekers());
                    seekerManager.prepareForStarting(getSeekers());
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
        plugin.getServer().getPluginManager().registerEvents(new BlockPlaceListener(this),plugin);
        plugin.getServer().getPluginManager().registerEvents(new PlayerJoinListener(this),plugin);
        plugin.getServer().getPluginManager().registerEvents(new PlayerQuitListener(this),plugin);
        plugin.getServer().getPluginManager().registerEvents(new PlayerDropItemListener(this),plugin);
        plugin.getServer().getPluginManager().registerEvents(new EntityDamageByEntityListener(this),plugin);
    }

    public void addPlayer(final @NotNull Player player){
        players.put(player.getUniqueId(), false);
        player.getInventory().clear();
        player.setGameMode(GameMode.SURVIVAL);
        player.setFoodLevel(20);
        player.setHealth(20.00);
        playerManager.giveKit();
    }

    public void addSpectator(final @NotNull Player player){
        players.remove(player.getUniqueId());
        spectators.put(player.getUniqueId(), false);
        player.getInventory().clear();
        player.setGameMode(GameMode.SPECTATOR);
    }

    public void removePlayer(final @NotNull Player player){
        players.remove(player.getUniqueId());
        player.getInventory().clear();
    }
    public void removeSpectator(final @NotNull Player player){
        spectators.remove(player.getUniqueId());
        player.getInventory().clear();
    }
    public void sendMessage(@NotNull String str){
        for (UUID playerUUID : players.keySet()){
            Player player = plugin.getServer().getPlayer(playerUUID);
            if (player == null) return;

            player.sendMessage(ColorUtil.color(str));
        }

        for (UUID spectatorUUID : spectators.keySet()){
            Player spectator = plugin.getServer().getPlayer(spectatorUUID);
            if (spectator == null) return;

            spectator.sendMessage(ColorUtil.color(str));
        }
    }

    public boolean isPlaying(final @NotNull UUID playerUUID){
        return players.containsKey(playerUUID);
    }
    public boolean isSpectating(final @NotNull UUID playerUUID){
        return spectators.containsKey(playerUUID);
    }

    public boolean isSeeker(final @NotNull UUID playerUUID) {
        return players.containsKey(playerUUID);
    }

    public boolean isHider(final @NotNull UUID playerUUID){
        return players.containsKey(playerUUID);
    }

    public UUID getSeekers(){
        List<UUID> seekers = new ArrayList<>(players.keySet());
        for (int i = 0; i < players.size(); i++) {
            if (isSeeker(seekers.get(i))){
                return seekers.get(i);
            }
        }
        return null;
    }

    public void rollSeekers(){
        List<UUID> seekers = new ArrayList<>(players.keySet());
        Collections.shuffle(seekers);
        for (int i = 0;i < 2;i++){
            seekers.get(i);
            players.put(seekers.get(i), true);
        }
    }
    public void sendActionBar(@NotNull String str){
        for (UUID playerUUID : players.keySet()){
            Player player = plugin.getServer().getPlayer(playerUUID);
            if (player == null) return;

            player.sendActionBar(ColorUtil.color(str));
        }

        for (UUID spectatorUUID : spectators.keySet()){
            Player spectator = plugin.getServer().getPlayer(spectatorUUID);
            if (spectator == null) return;

            spectator.sendActionBar(ColorUtil.color(str));
        }
    }
    private void cleanup(){
        sendActionBar(plugin.getConfig().getString("game_is_ending"));

        for (UUID playerUUID : players.keySet()) {
            Player player = plugin.getServer().getPlayer(playerUUID);
            if (player == null) continue;

            player.setGameMode(GameMode.SURVIVAL);
            player.getInventory().clear();
            player.teleport(Bukkit.getServer().getWorld("world").getSpawnLocation());
        }

        for (UUID playerUUID : spectators.keySet()) {
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
