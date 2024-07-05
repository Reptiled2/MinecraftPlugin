package me.rofl.CombatLog;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;
import java.util.UUID;

public class CombatThread extends BukkitRunnable {

  CombatLog thread;
  Player player;
  Map<UUID, Long> durationMap;
  public CombatThread(CombatLog thread, Player player) {
    this.thread = thread;
    this.player = player;
    this.durationMap = CombatLog.logoutTimes;
  }

  @Override
  public void run() {
    Long duration = durationMap.get(player.getUniqueId());
    if (duration == null ||  System.currentTimeMillis() - duration >= 60000) {
      cancel();
      return;
    }

    player.sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
        ChatColor.GRAY + "Combat log expires in " + ChatColor.RED + ((duration + 60000 - System.currentTimeMillis())/1000) + ChatColor.GRAY
            + " seconds!"
    ));
  }
}