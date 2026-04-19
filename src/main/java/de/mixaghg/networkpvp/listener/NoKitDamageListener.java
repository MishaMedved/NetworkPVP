package de.mixaghg.networkpvp.listener;

import de.mixaghg.networkpvp.NetworkPvPPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class NoKitDamageListener implements Listener {

    private final NetworkPvPPlugin plugin;

    public NoKitDamageListener(NetworkPvPPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        if (plugin.getKitManager().hasNoKit(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player victim)) {
            return;
        }

        if (plugin.getKitManager().hasNoKit(victim)) {
            event.setCancelled(true);
            return;
        }

        if (event.getDamager() instanceof Player damager) {
            if (plugin.getKitManager().hasNoKit(damager)) {
                event.setCancelled(true);
            }
        }
    }
}