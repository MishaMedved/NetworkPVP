package de.mixaghg.networkpvp.listener;

import de.mixaghg.networkpvp.NetworkPvPPlugin;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.entity.Player;

public class PlayerRespawnListener implements Listener {

    private final NetworkPvPPlugin plugin;

    public PlayerRespawnListener(NetworkPvPPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();

        // Spawn setzen
        if (plugin.getPvpSpawn() != null) {
            event.setRespawnLocation(plugin.getPvpSpawn());
        }

        // kleinen Delay nutzen, damit Bukkit fertig ist
        Bukkit.getScheduler().runTaskLater(plugin, () -> {

            // Reset Basics
            player.setHealth(20.0);
            player.setFoodLevel(20);
            player.setSaturation(20);

            // Hat Spieler KEIN Kit → Selector geben
            if (plugin.getKitManager().hasNoKit(player)) {
                plugin.getKitManager().giveKitSelector(player);
                return;
            }

            // Hat Kit → wieder geben
            if (plugin.getConfig().getBoolean("respawn.auto-give-last-kit", true)) {
                plugin.getKitManager().giveLastKit(player);
            } else {
                plugin.getKitManager().giveKitSelector(player);
            }

        }, 2L);
    }
}