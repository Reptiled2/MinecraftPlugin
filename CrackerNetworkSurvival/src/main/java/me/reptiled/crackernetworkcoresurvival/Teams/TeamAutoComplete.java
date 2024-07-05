package me.reptiled.crackernetworkcoresurvival.Tpa;

import me.reptiled.crackernetworkcoresurvival.CrackerNetworkCore;
import me.reptiled.crackernetworkcoresurvival.Teams.CreateTeam;
import me.reptiled.crackernetworkcoresurvival.Teams.TeamCommands;
import me.reptiled.crackernetworkcoresurvival.Vanish.VanishCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class TeamAutoComplete implements TabCompleter {

  CrackerNetworkCore plugin;
  public TeamAutoComplete(CrackerNetworkCore plugin) {
    this.plugin = plugin;
  }

  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
    if (!(sender instanceof Player player)) return null;
    List<String> completions = new ArrayList<>();

    if (args.length == 1) {
      completions.add("invite");
      completions.add("tag");
      completions.add("accept");
      completions.add("deny");
      completions.add("kick");
    }

    if (args.length == 2) {
      if (args[0].equalsIgnoreCase("invite")) {
        for (Player p : Bukkit.getOnlinePlayers()) {
          if (VanishCommand.invisiblePlayersUuid.contains(p.getUniqueId())) continue;

          if (p.getName().toLowerCase().startsWith(args[1].toLowerCase())) {
            completions.add(p.getName());
          }
        }
      } else if (args[0].equalsIgnoreCase("kick")) {
        JSONObject team = CreateTeam.getTeam(plugin, player);
        if (team == null) return null;

        JSONArray members = (JSONArray) team.get("members");
        for (Object member: members) {
          completions.add(Bukkit.getOfflinePlayer(UUID.fromString((String) member)).getName());
        }
      }
    }

    return completions;
  }
}
