package de.mixaghg.networkpvp.listener;

import de.mixaghg.networkpvp.NetworkPvPPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private final NetworkPvPPlugin plugin;

    public PlayerJoinListener(NetworkPvPPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (plugin.getConfig().getBoolean("respawn.clear-inventory-on-join", true)) {
            player.getInventory().clear();
            player.getInventory().setArmorContents(null);
        }

        plugin.getKitManager().teleportToSpawn(player);
        plugin.getKitManager().giveKitSelector(player);
        plugin.getScoreboardManager().updateScoreboard(player);
    }
}