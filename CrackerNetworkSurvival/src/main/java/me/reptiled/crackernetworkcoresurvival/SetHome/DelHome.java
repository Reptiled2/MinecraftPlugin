package me.reptiled.crackernetworkcoresurvival.SetHome;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.reptiled.crackernetworkcoresurvival.CrackerNetworkCore;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class DelHome implements CommandExecutor {

  CrackerNetworkCore plugin;
  public DelHome(CrackerNetworkCore plugin) {
    this.plugin = plugin;
  }


  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
    if (!(sender instanceof Player player)) {
      return false;
    }

    String homeName = args.length > 0 ? args[0].toLowerCase() : "home";
    final JsonObject userData = SetHome.homesJson.getAsJsonObject(player.getUniqueId().toString());

    if (userData == null || userData.isEmpty()) {
      player.sendMessage(plugin.chatPrefix.append(MiniMessage.miniMessage().deserialize(
          String.format("Couldn't find home named <red>%s</red>.", homeName)
      )));

      return false;
    }

    if (!userData.has(homeName)) {
      player.sendMessage(plugin.chatPrefix.append(MiniMessage.miniMessage().deserialize(
          String.format("Couldn't find home named <red>%s</red>.", homeName)
      )));

      return false;
    }

    userData.remove(homeName);

    player.sendMessage(plugin.chatPrefix.append(MiniMessage.miniMessage().deserialize(
        String.format("Successfully removed home <red>%s</red>.", homeName)
    )));

    return true;
  }
}
