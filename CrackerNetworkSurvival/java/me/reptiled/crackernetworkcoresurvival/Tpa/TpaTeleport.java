package me.reptiled.crackernetworkcoresurvival.SetHome;

import me.reptiled.crackernetworkcoresurvival.CrackerNetworkCore;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class TpaTeleport extends BukkitRunnable {

  CrackerNetworkCore plugin;
  Player player;
  Player targetPlayer;
  int duration;
  Location playerLocation;
  public TpaTeleport(CrackerNetworkCore plugin, Player player, Player targetPlayer, int duration) {
    this.plugin = plugin;
    this.player = player;
    this.targetPlayer = targetPlayer;
    this.duration = duration;

    this.playerLocation = player.getLocation().getBlock().getLocation();
  }

  @Override
  public void run() {
    if (duration < 1) {
      player.teleport(targetPlayer.getLocation());
      player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);

      final CachedMetaData playerData = plugin.LuckpermsApi.getPlayerAdapter(Player.class).getMetaData(targetPlayer);
      player.sendMessage(plugin.chatPrefix.append(MiniMessage.miniMessage().deserialize(
          String.format(
              "<gray>Teleported to </gray>%s %s<reset>!",
              playerData.getPrefix() != null ? playerData.getPrefix() : "",
              ((net.kyori.adventure.text.TextComponent) targetPlayer.displayName()).content()
          )
      )));

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
