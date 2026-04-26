package de.mixaghg.networkpvp.kit;

import org.bukkit.enchantments.Enchantment;
import de.mixaghg.networkpvp.NetworkPvPPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

public class KitManager {

    private final Set<UUID> playersWithoutKit = new HashSet<>();
    private final NetworkPvPPlugin plugin;
    private final Map<UUID, KitType> lastKit = new HashMap<>();
    private final Map<UUID, Integer> deathCount = new HashMap<>();

    public KitManager(NetworkPvPPlugin plugin) {
        this.plugin = plugin;
    }
    public void markPlayerWithoutKit(Player player) {
        playersWithoutKit.add(player.getUniqueId());
    }

    public void markPlayerWithKit(Player player) {
        playersWithoutKit.remove(player.getUniqueId());
    }

    public boolean hasNoKit(Player player) {
        return playersWithoutKit.contains(player.getUniqueId());
    }

    public void teleportToSpawn(Player player) {
        FileConfiguration config = plugin.getConfig();

        String worldName = config.getString("spawn.world", "world");
        World world = Bukkit.getWorld(worldName);

        if (world == null) {
            player.sendMessage("§cSpawn-Welt nicht gefunden.");
            return;
        }

        double x = config.getDouble("spawn.x", 0.5);
        double y = config.getDouble("spawn.y", 80.0);
        double z = config.getDouble("spawn.z", 0.5);
        float yaw = (float) config.getDouble("spawn.yaw", 0.0);
        float pitch = (float) config.getDouble("spawn.pitch", 0.0);

        Location location = new Location(world, x, y, z, yaw, pitch);
        player.teleport(location);
    }

    public void giveKitSelector(Player player) {
        markPlayerWithoutKit(player);
        Material material = getSelectorMaterial();
        int slot = getSelectorSlot();
        String name = getSelectorName();

        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(name);
            item.setItemMeta(meta);
        }

        player.getInventory().setItem(slot, item);
    }

    public boolean isKitSelector(ItemStack item) {
        if (item == null || item.getType().isAir()) {
            return false;
        }

        if (item.getType() != getSelectorMaterial()) {
            return false;
        }

        if (!item.hasItemMeta()) {
            return false;
        }

        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasDisplayName()) {
            return false;
        }

        return meta.getDisplayName().equals(getSelectorName());
    }

    public Material getSelectorMaterial() {
        String materialName = plugin.getConfig().getString("kit-selector.material", "NETHER_STAR");

        try {
            return Material.valueOf(materialName.toUpperCase());
        } catch (IllegalArgumentException ex) {
            return Material.NETHER_STAR;
        }
    }

    public String getSelectorName() {
        return ChatColor.translateAlternateColorCodes('&',
                plugin.getConfig().getString("kit-selector.name", "&bKit-Auswahl"));
    }

    public int getSelectorSlot() {
        return plugin.getConfig().getInt("kit-selector.slot", 4);
    }

    public KitType getLastKit(Player player) {
        return lastKit.get(player.getUniqueId());
    }

    public void giveLastKit(Player player) {
        KitType kitType = getLastKit(player);

        if (kitType != null) {
            giveKit(player, kitType);
        } else {
            giveKitSelector(player);
        }
    }

    public void giveKit(Player player, KitType kitType) {
        markPlayerWithKit(player);

        player.getInventory().clear();
        player.getInventory().setArmorContents(null);

        player.setHealth(20.0);
        player.setFoodLevel(20);
        player.setSaturation(20f);
        player.setFireTicks(0);
        player.setAbsorptionAmount(0.0);

        switch (kitType) {
            case SWORD -> giveSwordKit(player);
            case SOUP -> giveSoupKit(player);
            case MACE -> giveMaceKit(player);
            case FIREBALL -> giveFireballKit(player);
            case MLG -> giveMLGKit(player);
        }

        lastKit.put(player.getUniqueId(), kitType);
        player.sendMessage("§aDu hast das Kit §e" + formatKitName(kitType) + " §abekommen.");
    }

    private String formatKitName(KitType type) {
        return switch (type) {
            case SWORD -> "Sword";
            case SOUP -> "Soup";
            case MACE -> "Mace";
            case FIREBALL -> "Fireball";
            case MLG -> "MLG";
        };
    }

    private void giveSwordKit(Player player) {

        ItemStack sword = new ItemStack(Material.NETHERITE_SWORD);
        sword.addUnsafeEnchantment(Enchantment.SHARPNESS, 6);
        player.getInventory().addItem(sword);

        ItemStack rod = new ItemStack(Material.FISHING_ROD);
        rod.addUnsafeEnchantment(Enchantment.KNOCKBACK, 1);
        player.getInventory().addItem(rod);

        ItemStack bow = new ItemStack(Material.BOW);
        bow.addEnchantment(Enchantment.PUNCH, 3);
        player.getInventory().addItem(bow);


        player.getInventory().addItem(new ItemStack(Material.LAVA_BUCKET));
        player.getInventory().addItem(new ItemStack(Material.WATER_BUCKET));
        player.getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE, 10));
        player.getInventory().addItem(new ItemStack(Material.ENDER_PEARL, 16));
        player.getInventory().addItem(new ItemStack(Material.WIND_CHARGE, 64));
        player.getInventory().addItem(new ItemStack(Material.SPECTRAL_ARROW, 64));

        ItemStack helmet = new ItemStack(Material.NETHERITE_HELMET);
        helmet.addUnsafeEnchantment(Enchantment.PROTECTION, 20);
        player.getInventory().setHelmet(helmet);

        ItemStack chestplate = new ItemStack(Material.NETHERITE_CHESTPLATE);
        chestplate.addUnsafeEnchantment(Enchantment.PROTECTION, 20);
        player.getInventory().setChestplate(chestplate);

        ItemStack leggings = new ItemStack(Material.DIAMOND_LEGGINGS);
        leggings.addUnsafeEnchantment(Enchantment.PROTECTION, 20);
        player.getInventory().setLeggings(leggings);

        ItemStack boots = new ItemStack(Material.DIAMOND_BOOTS);
        boots.addUnsafeEnchantment(Enchantment.PROTECTION, 20);
        player.getInventory().setBoots(boots);
    }

    private void giveSoupKit(Player player) {
        ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
        sword.addEnchantment(Enchantment.SHARPNESS, 2);
        sword.addUnsafeEnchantment(Enchantment.KNOCKBACK, 2);
        player.getInventory().addItem(sword);

        player.getInventory().addItem(new ItemStack(Material.ENDER_PEARL, 5));

        for (int i = 0; i < 34; i++) {
            player.getInventory().addItem(new ItemStack(Material.MUSHROOM_STEW));
        }
        ItemStack helmet = new ItemStack(Material.NETHERITE_HELMET);
        helmet.addEnchantment(Enchantment.PROTECTION, 3);
        player.getInventory().setHelmet(helmet);

        ItemStack chestplate = new ItemStack(Material.DIAMOND_CHESTPLATE);
        chestplate.addEnchantment(Enchantment.PROTECTION, 2);
        chestplate.addEnchantment(Enchantment.THORNS, 2);
        player.getInventory().setChestplate(chestplate);

        ItemStack leggings = new ItemStack(Material.IRON_LEGGINGS);
        leggings.addEnchantment(Enchantment.PROTECTION, 2);
        player.getInventory().setLeggings(leggings);

        ItemStack boots = new ItemStack(Material.IRON_BOOTS);
        boots.addEnchantment(Enchantment.PROTECTION, 2);
        player.getInventory().setBoots(boots);
    }

    private void giveMaceKit(Player player) {

        ItemStack mace = new ItemStack(Material.MACE);
        mace.addEnchantment(Enchantment.WIND_BURST, 1);
        mace.addEnchantment(Enchantment.DENSITY, 2);
        player.getInventory().addItem(mace);

        ItemStack sword = new ItemStack(Material.STONE_SWORD);
        sword.addUnsafeEnchantment(Enchantment.KNOCKBACK, 3);
        player.getInventory().addItem(sword);

        player.getInventory().setItemInOffHand(new ItemStack(Material.WIND_CHARGE, 64));
        player.getInventory().addItem(new ItemStack(Material.ENDER_PEARL, 16));
        player.getInventory().addItem(new ItemStack(Material.WATER_BUCKET));
        player.getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE, 6));


        /*ItemStack stick = new ItemStack(Material.STICK);
        stick.addUnsafeEnchantment(Enchantment.KNOCKBACK, 2000);
        player.getInventory().addItem(stick);*/



        ItemStack helmet = new ItemStack(Material.CHAINMAIL_HELMET);
        helmet.addEnchantment(Enchantment.PROTECTION, 2);
        player.getInventory().setHelmet(helmet);

        ItemStack chestplate = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
        chestplate.addEnchantment(Enchantment.PROTECTION, 3);
        player.getInventory().setChestplate(chestplate);

        ItemStack leggings = new ItemStack(Material.CHAINMAIL_LEGGINGS);
        leggings.addEnchantment(Enchantment.PROTECTION, 2);
        player.getInventory().setLeggings(leggings);

        ItemStack boots = new ItemStack(Material.NETHERITE_BOOTS);
        boots.addEnchantment(Enchantment.FEATHER_FALLING, 4);
        player.getInventory().setBoots(boots);
    }

    private void giveFireballKit(Player player) {
        ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
        sword.addEnchantment(Enchantment.SHARPNESS, 2);
        sword.addEnchantment(Enchantment.FIRE_ASPECT, 1);
        player.getInventory().addItem(sword);

        player.getInventory().addItem(new ItemStack(Material.FIRE_CHARGE, 20));

        ItemStack bow = new ItemStack(Material.BOW);
        bow.addUnsafeEnchantment(Enchantment.PUNCH, 4);
        bow.addEnchantment(Enchantment.FLAME, 1);
        player.getInventory().addItem(bow);

        player.getInventory().addItem(new ItemStack(Material.WATER_BUCKET));
        player.getInventory().addItem(new ItemStack(Material.ENDER_PEARL, 12));
        player.getInventory().addItem(new ItemStack(Material.WIND_CHARGE, 20));
        player.getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE, 10));
        player.getInventory().setItemInOffHand(new ItemStack(Material.PANDA_SPAWN_EGG, 3));
        ItemStack rod = new ItemStack(Material.FISHING_ROD);
        rod.addUnsafeEnchantment(Enchantment.KNOCKBACK, 10);
        player.getInventory().addItem(rod);

        player.getInventory().addItem(new ItemStack(Material. ARROW, 64));


        ItemStack helmet = new ItemStack(Material.NETHERITE_HELMET);
        helmet.addEnchantment(Enchantment.PROTECTION, 3);
        player.getInventory().setHelmet(helmet);

        ItemStack chestplate = new ItemStack(Material.IRON_CHESTPLATE);
        chestplate.addEnchantment(Enchantment.PROTECTION, 4);
        chestplate.addUnsafeEnchantment(Enchantment.FIRE_PROTECTION, 4);
        player.getInventory().setChestplate(chestplate);

        ItemStack leggings = new ItemStack(Material.IRON_LEGGINGS);
        leggings.addEnchantment(Enchantment.PROTECTION, 2);
        player.getInventory().setLeggings(leggings);

        ItemStack boots = new ItemStack(Material.IRON_BOOTS);
        boots.addEnchantment(Enchantment.PROTECTION, 2);
        boots.addUnsafeEnchantment(Enchantment.FIRE_PROTECTION, 4);
        player.getInventory().setBoots(boots);
    }

    private void giveMLGKit(Player player) {
        ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
        sword.addEnchantment(Enchantment.SHARPNESS, 3);
        sword.addUnsafeEnchantment(Enchantment.KNOCKBACK, 4);
        player.getInventory().addItem(sword);

        player.getInventory().addItem(new ItemStack(Material.TNT, 32));
        player.getInventory().addItem(new ItemStack(Material.WATER_BUCKET, 1));
        player.getInventory().setItemInOffHand(new ItemStack(Material.WIND_CHARGE, 64));
        player.getInventory().addItem(new ItemStack(Material.ENDER_PEARL, 14));
        player.getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE, 6));
        player.getInventory().addItem(new ItemStack(Material.FISHING_ROD));
        player.getInventory().addItem(new ItemStack(Material.SLIME_BLOCK, 16));

        ItemStack bow = new ItemStack(Material.BOW);
        bow.addEnchantment(Enchantment.PUNCH, 1);
        bow.addEnchantment(Enchantment.FLAME, 1);
        player.getInventory().addItem(bow);

        player.getInventory().addItem(new ItemStack(Material.STONE_PRESSURE_PLATE, 6));
        ItemStack stick = new ItemStack(Material.STICK);
        stick.addUnsafeEnchantment(Enchantment.KNOCKBACK, 10);
        player.getInventory().addItem(stick);

        player.getInventory().addItem(new ItemStack(Material.COBWEB, 12));
        player.getInventory().addItem(new ItemStack(Material.FLINT_AND_STEEL));
        player.getInventory().addItem(new ItemStack(Material.ARROW, 64));

        ItemStack helmet = new ItemStack(Material.DIAMOND_HELMET);
        helmet.addEnchantment(Enchantment.PROTECTION, 2);
        player.getInventory().setHelmet(helmet);

        ItemStack chestplate = new ItemStack(Material.IRON_CHESTPLATE);
        chestplate.addEnchantment(Enchantment.PROTECTION, 2);
        player.getInventory().setChestplate(chestplate);

        ItemStack leggings = new ItemStack(Material.IRON_LEGGINGS);
        leggings.addEnchantment(Enchantment.PROTECTION, 2);
        player.getInventory().setLeggings(leggings);

        ItemStack boots = new ItemStack(Material.DIAMOND_BOOTS);
        boots.addEnchantment(Enchantment.FEATHER_FALLING, 4);
        player.getInventory().setBoots(boots);
    }

    public int getDeaths(Player player) {
        return deathCount.getOrDefault(player.getUniqueId(), 0);
    }

    public int addDeath(Player player) {
        int newDeaths = getDeaths(player) + 1;
        deathCount.put(player.getUniqueId(), newDeaths);
        return newDeaths;
    }

    public void resetDeaths(Player player) {
        deathCount.put(player.getUniqueId(), 0);
    }

    public void resetKitState(Player player) {
        lastKit.remove(player.getUniqueId());
        markPlayerWithoutKit(player);
    }
}