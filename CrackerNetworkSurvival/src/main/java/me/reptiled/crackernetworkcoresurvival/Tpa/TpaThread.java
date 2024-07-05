package me.reptiled.crackernetworkcoresurvival.Tpa;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class TpaThread extends BukkitRunnable {

  Player player;
  Player targetPlayer;
  public TpaThread(Player player, Player targetPlayer) {
    this.player = player;
    this.targetPlayer = targetPlayer;
  }

  @Override
  public void run() {
    targetPlayer.sendMessage(MiniMessage.miniMessage().deserialize(
        "<gray>Teleport request timed out.</gray>"
    ));
    Tpa.teleportRequests.remove(player, targetPlayer);
    Tpa.teleportThreads.remove(player);
  }
}
