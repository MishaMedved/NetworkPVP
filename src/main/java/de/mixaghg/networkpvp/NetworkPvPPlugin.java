package de.mixaghg.networkpvp;

import de.mixaghg.networkpvp.listener.BuildProtectListener;
import de.mixaghg.networkpvp.command.SpawnCommand;
import de.mixaghg.networkpvp.kit.KitManager;
import de.mixaghg.networkpvp.listener.*;
import de.mixaghg.networkpvp.scoreboard.ScoreboardManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import de.mixaghg.networkpvp.listener.FireProtectListener;
import de.mixaghg.networkpvp.listener.FireballUseListener;
import de.mixaghg.networkpvp.listener.FireballExplodeListener;
import de.mixaghg.networkpvp.command.KitCommand;

import java.util.Objects;

public class NetworkPvPPlugin extends JavaPlugin {

    private static NetworkPvPPlugin instance;
    private KitManager kitManager;
    private ScoreboardManager scoreboardManager;
    private Location pvpSpawn;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();
        loadSpawnFromConfig();
        this.kitManager = new KitManager(this);
        this.scoreboardManager = new ScoreboardManager();

        Objects.requireNonNull(getCommand("spawn")).setExecutor(new SpawnCommand(this));
        Objects.requireNonNull(getCommand("kit")).setExecutor(new KitCommand(this));

        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerRespawnListener(this), this);
        getServer().getPluginManager().registerEvents(new KitSelectorInteractListener(this), this);
        getServer().getPluginManager().registerEvents(new KitSelectorDropListener(this), this);
        getServer().getPluginManager().registerEvents(new KitSelectorMoveListener(this), this);
        getServer().getPluginManager().registerEvents(new KitMenuListener(this), this);
        getServer().getPluginManager().registerEvents(new NoKitDamageListener(this), this);
        getServer().getPluginManager().registerEvents(new BuildProtectListener(), this);
        getServer().getPluginManager().registerEvents(new FireProtectListener(), this);
        getServer().getPluginManager().registerEvents(new FireballUseListener(), this);
        getServer().getPluginManager().registerEvents(new FireballExplodeListener(), this);
        getServer().getPluginManager().registerEvents(new AllowedUseListener(), this);

        getLogger().info("NetworkPvP wurde aktiviert.");
    }

    @Override
    public void onDisable() {
        getLogger().info("NetworkPvP wurde deaktiviert.");
    }
    private void loadSpawnFromConfig() {
        FileConfiguration cfg = getConfig();

        if (!cfg.contains("spawn.world")) {
            cfg.set("spawn.world", "world");
            cfg.set("spawn.x", 0.5);
            cfg.set("spawn.y", 95.0);
            cfg.set("spawn.z", 0.5);
            cfg.set("spawn.yaw", 0.0);
            cfg.set("spawn.pitch", 0.0);
            saveConfig();
        }

        String worldName = cfg.getString("spawn.world");
        World world = Bukkit.getWorld(worldName);

        if (world == null) {
            getLogger().warning("Spawn-Welt '" + worldName + "' wurde nicht gefunden.");
            return;
        }

        double x = cfg.getDouble("spawn.x");
        double y = cfg.getDouble("spawn.y");
        double z = cfg.getDouble("spawn.z");
        float yaw = (float) cfg.getDouble("spawn.yaw");
        float pitch = (float) cfg.getDouble("spawn.pitch");

        pvpSpawn = new Location(world, x, y, z, yaw, pitch);
    }

    public static NetworkPvPPlugin getInstance() {
        return instance;
    }

    public Location getPvpSpawn() {
        return pvpSpawn == null ? null : pvpSpawn.clone();
    }

    public KitManager getKitManager() {
        return kitManager;
    }

    public ScoreboardManager getScoreboardManager() {
        return scoreboardManager;
    }
}