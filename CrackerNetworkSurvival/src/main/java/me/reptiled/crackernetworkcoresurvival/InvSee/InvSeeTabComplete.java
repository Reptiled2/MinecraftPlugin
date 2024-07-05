package me.reptiled.crackernetworkcoresurvival.Tpa;

import me.reptiled.crackernetworkcoresurvival.CrackerNetworkCore;
import me.reptiled.crackernetworkcoresurvival.Vanish.VanishCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class InvSeeTabComplete implements TabCompleter {

  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
    if (!(sender instanceof Player player)) return null;
    if (args.length == 0) return null;

    List<String> completions = new ArrayList<>();

    for (Player p : Bukkit.getOnlinePlayers()) {
      if (VanishCommand.invisiblePlayersUuid.contains(p.getUniqueId()) && !p.hasPermission("vanish")) continue;

      if (p.getName().toLowerCase().startsWith(args[0].toLowerCase())) {
        completions.add(p.getName());
      }
    }

    return completions;
  }
}
