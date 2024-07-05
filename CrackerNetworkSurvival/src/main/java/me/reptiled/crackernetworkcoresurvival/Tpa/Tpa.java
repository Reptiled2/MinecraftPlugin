package me.reptiled.crackernetworkcoresurvival.Tpa;

import me.rofl.CombatLog.CombatLog;
import me.reptiled.crackernetworkcoresurvival.CrackerNetworkCore;
import me.reptiled.crackernetworkcoresurvival.Vanish.VanishCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.luckperms.api.cacheddata.CachedMetaData;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class Tpa implements CommandExecutor {

  public static Map<Player, Player> teleportRequests = new HashMap<Player, Player>();
  public static Map<Player, Integer> teleportThreads = new HashMap<Player, Integer>();
  CrackerNetworkCore plugin;
  public Tpa(CrackerNetworkCore plugin) {
    this.plugin = plugin;
  }

  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    if (!(sender instanceof Player player)) return false;

    if (args.length == 0) {
      player.sendMessage(Component.text("Please fill all the required parameters."));
      return true;
    }

    if (teleportRequests.containsKey(player)) {
      player.sendMessage(plugin.chatPrefix.append(MiniMessage.miniMessage().deserialize(
          "<gray>You already have an ongoing teleport request. You can do </gray><red><click:run_command:'/tpacancel'>/tpacancel</click></red><gray>.</gray>"
      )));
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

    Player targetPlayer = Bukkit.getPlayerExact(args[0]);
    if (targetPlayer == null || targetPlayer.equals(player) ||
        (VanishCommand.invisiblePlayersUuid.contains(targetPlayer.getUniqueId()) && !player.hasPermission("vanish"))) {
      player.sendMessage(plugin.chatPrefix.append(MiniMessage.miniMessage().deserialize(
          "<gray>Couldn't find player with that name.</gray>"
      )));
      return true;
    }

    final CachedMetaData playerData = plugin.LuckpermsApi.getPlayerAdapter(Player.class).getMetaData(player);
    final CachedMetaData targetPlayerData = plugin.LuckpermsApi.getPlayerAdapter(Player.class).getMetaData(targetPlayer);
    targetPlayer.sendMessage(MiniMessage.miniMessage().deserialize(
        String.format(
            "<gold> -------- Teleport Request -------- </gold>\n" +
            "<gold>|</gold> %s %s <reset><gray>wants to teleport to you.</gray>\n" +
            "<gold>|</gold> <green><click:run_command:'/tpaccept'>/tpaccept</click></green> <gray>to accept the request.</gray>\n" +
            "<gold>|</gold> <red><click:run_command:'/tpadeny'>/tpadeny</click></red> <gray>to deny the request</gray>\n" +
            "<gold>|</gold>\n<gold>|</gold> <gray>You have 60 seconds to accept the request</gray>",
            playerData.getPrefix() != null ? playerData.getPrefix() : "",
            ((TextComponent) player.displayName()).content())
    ));
    player.sendMessage(plugin.chatPrefix.append(MiniMessage.miniMessage().deserialize(
        String.format(
            "<gray>Sent teleport request to</gray> %s %s",
            targetPlayerData.getPrefix() != null ? targetPlayerData.getPrefix() : "",
            ((TextComponent) targetPlayer.displayName()).content()
        )
    )));

    teleportRequests.put(player, targetPlayer);

    BukkitRunnable thread = new TpaThread(player, targetPlayer);
    thread.runTaskLater(plugin, 1200L);
    teleportThreads.put(player, thread.getTaskId());

    return true;
  }
}
