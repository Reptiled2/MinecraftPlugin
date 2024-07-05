package me.reptiled.crackernetworkcoresurvival.SetHome;

import me.reptiled.crackernetworkcoresurvival.CrackerNetworkCore;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class HomeTeleport extends BukkitRunnable {

  CrackerNetworkCore plugin;
  Player player;
  int duration;
  Location location;
  String home;
  Location playerLocation;
  public HomeTeleport(CrackerNetworkCore plugin, Player player, int duration, Location location, String home) {
    this.plugin = plugin;
    this.player = player;
    this.duration = duration;
    this.location = location;
    this.home = home;

    this.playerLocation = player.getLocation().getBlock().getLocation();
  }

  @Override
  public void run() {
    if (duration < 1) {
      player.teleport(location);
      player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
      player.sendMessage(plugin.chatPrefix.append(Component.text(String.format("Successfully teleported to %s!", home))));

      cancel();
      return;
    }

    if (!playerLocation.equals(player.getLocation().getBlock().getLocation())) {
      player.sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
          ChatColor.RED + "Teleport Cancelled!"
      ));

      cancel();
      return;
    }

    player.sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
        "Teleporting in " + ChatColor.GREEN + "" + ChatColor.BOLD + duration
    ));
    duration--;

    player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
  }
}
