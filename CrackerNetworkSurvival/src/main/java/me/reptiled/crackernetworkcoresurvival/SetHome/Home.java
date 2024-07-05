package me.reptiled.crackernetworkcoresurvival.SetHome;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.rofl.CombatLog.CombatLog;
import me.reptiled.crackernetworkcoresurvival.CrackerNetworkCore;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Home implements CommandExecutor {

  CrackerNetworkCore plugin;
  public Home(CrackerNetworkCore plugin) {
    this.plugin = plugin;
  }


  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
    if (!(sender instanceof Player player)) {
      return false;
    }

    for (Entity entity: player.getNearbyEntities(10D, 10D, 10D)) {
      if (entity instanceof Monster) {
        player.sendMessage(plugin.chatPrefix.append(MiniMessage.miniMessage().deserialize(
            "<red>There are monsters nearby!</red>"
            )
        ));
        return true;
      }
    }

    if (CombatLog.isCombatLogged(player)) {
      player.sendMessage(plugin.chatPrefix.append(MiniMessage.miniMessage().deserialize(
              "<red>You are combat logged!</red>"
          )
      ));
      return true;
    }

    String selectedHome = args.length > 0 ? args[0].toLowerCase() : "home";

    final JsonObject userData = SetHome.homesJson.getAsJsonObject(player.getUniqueId().toString());
    if (userData == null) {
      player.sendMessage(plugin.chatPrefix.append(Component.text("This home doesnt exist. Do /sethome")));
      return false;
    }

    final JsonElement home = userData.get(selectedHome);
    if (home == null) {
      player.sendMessage(plugin.chatPrefix.append(Component.text("This home doesnt exist. Do /sethome")));
      return false;
    }

    String[] coords = home.getAsString().split(" ");
    Location homeLocation = new Location(Bukkit.getWorld("world"),
        Double.parseDouble(coords[0]),
        Double.parseDouble(coords[1]),
        Double.parseDouble(coords[2]));


    player.sendMessage(plugin.chatPrefix.append(MiniMessage.miniMessage().deserialize(
        "<gray>Teleporting in</gray> <green>5</green> <gray>seconds. Don't move!</gray>"
    )));
    new HomeTeleport(plugin, player, 5, homeLocation, selectedHome).runTaskTimer(plugin, 0L, 20L);

    return true;
  }
}
