package de.mixaghg.networkpvp.command;

import de.mixaghg.networkpvp.NetworkPvPPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCommand implements CommandExecutor {

    private final NetworkPvPPlugin plugin;

    public SpawnCommand(NetworkPvPPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Nur Spieler können diesen Befehl nutzen.");
            return true;
        }

        plugin.getKitManager().teleportToSpawn(player);
        return true;
    }
}