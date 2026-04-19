package de.mixaghg.networkpvp.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.*;
import java.util.stream.Collectors;

public class ScoreboardManager {

    private final Map<UUID, Integer> kills = new HashMap<>();

    public int getKills(Player player) {
        return kills.getOrDefault(player.getUniqueId(), 0);
    }

    public void addKill(Player player) {
        kills.put(player.getUniqueId(), getKills(player) + 1);
        updateAllScoreboards();
    }

    public void setKills(Player player, int amount) {
        kills.put(player.getUniqueId(), amount);
        updateAllScoreboards();
    }

    public void updateAllScoreboards() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            updateScoreboard(player);
        }
    }

    public void updateScoreboard(Player viewer) {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();

        Objective objective = scoreboard.registerNewObjective("pvpstats", "dummy",
                ChatColor.RED + "" + ChatColor.BOLD + "NetworkPvP");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        List<Map.Entry<UUID, Integer>> topPlayers = kills.entrySet()
                .stream()
                .sorted((a, b) -> Integer.compare(b.getValue(), a.getValue()))
                .limit(5)
                .collect(Collectors.toList());

        int score = topPlayers.size() + 2;

        objective.getScore(ChatColor.GRAY + "Kills").setScore(score--);
        objective.getScore(" ").setScore(score--);

        if (topPlayers.isEmpty()) {
            objective.getScore(ChatColor.DARK_GRAY + "Noch keine Kills").setScore(score--);
        } else {
            for (Map.Entry<UUID, Integer> entry : topPlayers) {
                Player target = Bukkit.getPlayer(entry.getKey());
                String name = target != null ? target.getName() : "Unbekannt";
                if (name.length() > 10) {
                    name = name.substring(0, 10);
                }

                String line = ChatColor.WHITE + name + ": " + ChatColor.RED + entry.getValue();
                objective.getScore(line).setScore(score--);
            }
        }

        objective.getScore("  ").setScore(score--);
        objective.getScore(ChatColor.DARK_GRAY + "PvP Server").setScore(score);

        viewer.setScoreboard(scoreboard);
    }

    public void remove(Player player) {
        kills.remove(player.getUniqueId());
        updateAllScoreboards();
    }
}