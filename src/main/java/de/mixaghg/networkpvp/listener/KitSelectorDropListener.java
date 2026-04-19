package de.mixaghg.networkpvp.listener;

import de.mixaghg.networkpvp.NetworkPvPPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class KitSelectorDropListener implements Listener {

    private final NetworkPvPPlugin plugin;

    public KitSelectorDropListener(NetworkPvPPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        if (plugin.getKitManager().isKitSelector(event.getItemDrop().getItemStack())) {
            event.setCancelled(true);
        }
    }
}