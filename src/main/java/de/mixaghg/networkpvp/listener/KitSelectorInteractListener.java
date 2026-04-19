package de.mixaghg.networkpvp.listener;

import de.mixaghg.networkpvp.NetworkPvPPlugin;
import de.mixaghg.networkpvp.kit.KitMenu;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class KitSelectorInteractListener implements Listener {

    private final NetworkPvPPlugin plugin;

    public KitSelectorInteractListener(NetworkPvPPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) {
            return;
        }

        Action action = event.getAction();
        if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (!plugin.getKitManager().isKitSelector(item)) {
            return;
        }

        event.setCancelled(true);
        KitMenu.open(player);
    }
}