package me.reptiled.crackernetworkcoresurvival.SetHome;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.rofl.CombatLog.CombatLog;
import me.reptiled.crackernetworkcoresurvival.CrackerNetworkCore;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.*;


public class SetHome implements CommandExecutor {

  CrackerNetworkCore plugin;
  public static JsonObject homesJson;

  public SetHome(CrackerNetworkCore plugin) {
    this.plugin = plugin;

    try {
      homesJson =
          (JsonObject) new JsonParser().parse(
              new FileReader(new File(plugin.getDataFolder(), "homes.json"))
          );

    } catch (Exception e) {
      homesJson = new JsonObject();
    }
  }


  public void updateJson() {
    try {
      File path = new File(plugin.getDataFolder(), "homes.json");
      FileWriter fileWrite = new FileWriter(path);
      fileWrite.write(homesJson.toString());
      fileWrite.close();

    } catch (Exception err) {
      return;
    }
  }

  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
    if (!(sender instanceof Player player)) {
      return false;
    }

    if (!player.getWorld().getName().equals("world")) {
      player.sendMessage(plugin.chatPrefix.append(MiniMessage.miniMessage().deserialize(
              "<red>You can't use that here!</red>"
          )
      ));
      return true;
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

    final JsonObject userData = homesJson.getAsJsonObject(player.getUniqueId().toString());
    String homeName = args.length > 0 ? args[0].toLowerCase() : "home";
    String location = player.getLocation().getX() + " " + player.getLocation().getY() + " " + player.getLocation().getZ();

    if (userData == null) {
      JsonObject template = new JsonObject();
      template.addProperty(homeName, location);

      homesJson.add(player.getUniqueId().toString(), template);
      player.sendMessage(plugin.chatPrefix.append(MiniMessage.miniMessage().deserialize(
          String.format("Home <red>%s</red> successfully set.", homeName)
      )));

      return true;
    }

    if (userData.size() >= 2) {
      if (userData.has(homeName)) {
        userData.addProperty(homeName, location);

        player.sendMessage(plugin.chatPrefix.append(MiniMessage.miniMessage().deserialize(
            String.format("Home <red>%s</red> successfully set.", homeName)
        )));

        return true;
      }

      player.sendMessage(plugin.chatPrefix.append(Component.text("You can't have more than 2 homes!")));
      return false;
    }

    userData.addProperty(homeName, location);
    player.sendMessage(plugin.chatPrefix.append(MiniMessage.miniMessage().deserialize(
        String.format("Home <red>%s</red> successfully set.", homeName)
    )));

    return true;
  }
}
