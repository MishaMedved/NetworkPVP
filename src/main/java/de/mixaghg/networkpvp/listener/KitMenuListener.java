package de.mixaghg.networkpvp.listener;

import de.mixaghg.networkpvp.NetworkPvPPlugin;
import de.mixaghg.networkpvp.kit.KitMenu;
import de.mixaghg.networkpvp.kit.KitType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class KitMenuListener implements Listener {

    private final NetworkPvPPlugin plugin;

    public KitMenuListener(NetworkPvPPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle() == null) {
            return;
        }

        if (!event.getView().getTitle().equals(KitMenu.TITLE)) {
            return;
        }

        event.setCancelled(true);

        if (!(event.getWhoClicked() instanceof Player player)) {
            return;
        }

        if (event.getCurrentItem() == null || event.getCurrentItem().getType().isAir()) {
            return;
        }

        Material material = event.getCurrentItem().getType();

        switch (material) {
            case DIAMOND_SWORD -> plugin.getKitManager().giveKit(player, KitType.SWORD);
            case MUSHROOM_STEW -> plugin.getKitManager().giveKit(player, KitType.SOUP);
            case MACE -> plugin.getKitManager().giveKit(player, KitType.MACE);
            case FIRE_CHARGE -> plugin.getKitManager().giveKit(player, KitType.FIREBALL);
            case WATER_BUCKET -> plugin.getKitManager().giveKit(player, KitType.MLG);
            default -> {
                return;
            }
        }

        player.closeInventory();
    }
}