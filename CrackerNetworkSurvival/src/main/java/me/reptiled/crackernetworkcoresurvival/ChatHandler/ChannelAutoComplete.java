package me.reptiled.crackernetworkcoresurvival.ChatHandler;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ChannelAutoComplete implements TabCompleter {

  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
    if (!(sender instanceof Player player)) return null;
    if (args.length != 1) return null;

    List<String> completions = new ArrayList<>();
    completions.add("all");
    completions.add("team");
    completions.add("local");

    if (player.hasPermission("chat.staff")) {
      completions.add("staff");
    }

    return completions;
  }
}
