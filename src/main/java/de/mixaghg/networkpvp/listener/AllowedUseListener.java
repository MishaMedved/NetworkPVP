package de.mixaghg.networkpvp.listener;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.Set;

public class AllowedUseListener implements Listener {

    private static final Set<Material> ALLOWED_USE_ITEMS = Set.of(
            Material.OAK_BOAT,
            Material.SPRUCE_BOAT,
            Material.BIRCH_BOAT,
            Material.JUNGLE_BOAT,
            Material.ACACIA_BOAT,
            Material.DARK_OAK_BOAT,
            Material.MANGROVE_BOAT,
            Material.CHERRY_BOAT,
            Material.BAMBOO_RAFT,

            Material.WATER_BUCKET,
            Material.LAVA_BUCKET
    );

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) {
            return;
        }

        if (event.getPlayer().getGameMode() == GameMode.CREATIVE) {
            return;
        }

        ItemStack item = event.getItem();
        if (item == null) {
            return;
        }

        if (ALLOWED_USE_ITEMS.contains(item.getType())) {
            return;
        }
    }
}