package de.mixaghg.networkpvp.kit;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class KitMenu {

    public static final String TITLE = "§8Kit-Auswahl";

    public static void open(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 27, TITLE);

        inventory.setItem(11, createItem(Material.DIAMOND_SWORD, "§bSword"));
        inventory.setItem(13, createItem(Material.MUSHROOM_STEW, "§aSoup"));
        inventory.setItem(15, createItem(Material.MACE, "§6Mace"));
        inventory.setItem(3, createItem(Material.FIRE_CHARGE, "§cFireball"));
        inventory.setItem(5, createItem(Material.WATER_BUCKET, "§dMLG"));


        player.openInventory(inventory);
    }

    private static ItemStack createItem(Material material, String name) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(name);
            item.setItemMeta(meta);
        }

        return item;
    }
}