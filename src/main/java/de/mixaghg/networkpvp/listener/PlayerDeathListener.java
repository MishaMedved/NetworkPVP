package de.mixaghg.networkpvp.listener;

import de.mixaghg.networkpvp.NetworkPvPPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.entity.Player;

public class PlayerDeathListener implements Listener {

    private final NetworkPvPPlugin plugin;

    public PlayerDeathListener(NetworkPvPPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        if (plugin.getConfig().getBoolean("respawn.clear-drops-on-death", true)) {
            event.getDrops().clear();
            event.setDroppedExp(0);
        }

        Player killer = event.getEntity().getKiller();
        if (killer != null) {
            plugin.getScoreboardManager().addKill(killer);
        }

        Player victim = event.getEntity();
        int deaths = plugin.getKitManager().addDeath(victim);

        if (deaths >= 3) {
            plugin.getKitManager().resetDeaths(victim);
            plugin.getKitManager().resetKitState(victim);
        }

        event.setDeathMessage(null);
        event.setKeepInventory(false);
    }
}