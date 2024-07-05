package me.reptiled.crackernetworkcoresurvival.Tpa;

import me.reptiled.crackernetworkcoresurvival.CrackerNetworkCore;
import me.reptiled.crackernetworkcoresurvival.Teams.CreateTeam;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TagAutoComplete implements TabCompleter {

  CrackerNetworkCore plugin;
  public TagAutoComplete(CrackerNetworkCore plugin) {
    this.plugin = plugin;
  }

  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
    if (!(sender instanceof Player player)) return null;
    if (args.length != 1) return null;

    List<String> completions = new ArrayList<>();
    completions.add("role");

    if (!Objects.equals(CreateTeam.getUserTeamId(plugin, player), "-1")) {
      completions.add("team");
    }

    return completions;
  }
}
