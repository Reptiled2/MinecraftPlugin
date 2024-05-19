package me.reptiled.crackernetworkcoresurvival.SetHome;

import com.google.gson.JsonObject;
import me.reptiled.crackernetworkcoresurvival.CrackerNetworkCore;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Homes implements CommandExecutor {

  CrackerNetworkCore plugin;
  public Homes(CrackerNetworkCore plugin) {
    this.plugin = plugin;
  }


  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
    if (!(sender instanceof Player player)) {
      return false;
    }


    final JsonObject userData = SetHome.homesJson.getAsJsonObject(player.getUniqueId().toString());
    if (userData == null || userData.isEmpty()) {
      player.sendMessage(plugin.chatPrefix.append(Component.text("You don't have any homes. Do /sethome.")));
      return false;
    }

    String stringData = String.join(", ", userData.keySet());

    player.sendMessage(MiniMessage.miniMessage().deserialize(
        "Homes: <red>" + stringData
    ));

    return true;
  }
}
