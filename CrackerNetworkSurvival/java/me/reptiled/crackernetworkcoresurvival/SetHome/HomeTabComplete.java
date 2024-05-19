package me.reptiled.crackernetworkcoresurvival.SetHome;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class HomeTabComplete implements TabCompleter {

  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
    if (!(sender instanceof Player player)) {
      return null;
    }

    if (args.length < 1) {
      return null;
    }

    final JsonObject userData = SetHome.homesJson.getAsJsonObject(player.getUniqueId().toString());
    if (userData == null) {
      return null;
    }

    return new ArrayList<>(userData.keySet());
  }
}
