package de.mixaghg.networkpvp.command;

import de.mixaghg.networkpvp.NetworkPvPPlugin;
import de.mixaghg.networkpvp.kit.KitMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KitCommand implements CommandExecutor {

    private final NetworkPvPPlugin plugin;

    public KitCommand(NetworkPvPPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Dieser Befehl kann nur von Spielern benutzt werden.");
            return true;
        }

        KitMenu.open(player);
        return true;
    }
}