package me.reptiled.crackernetworkcoresurvival.Tpa;

import me.reptiled.crackernetworkcoresurvival.CrackerNetworkCore;
import me.reptiled.crackernetworkcoresurvival.SetHome.TpaTeleport;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.luckperms.api.cacheddata.CachedMetaData;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class Tpaccept implements CommandExecutor {

  CrackerNetworkCore plugin;
  public Tpaccept(CrackerNetworkCore plugin) {
    this.plugin = plugin;
  }

  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    if (!(sender instanceof Player player)) return false;

    Player targetPlayer = null;
    for (Map.Entry<Player, Player> entry : Tpa.teleportRequests.entrySet()) {
      if (player.equals(entry.getValue())) {
        targetPlayer = entry.getKey();
        break;
      }
    }
    
    if (targetPlayer == null) {
      player.sendMessage(MiniMessage.miniMessage().deserialize(
          "<gray>You don't have any teleport requests!</gray>")
      );
      return true;
    }

    final CachedMetaData targetPlayerData = plugin.LuckpermsApi.getPlayerAdapter(Player.class).getMetaData(targetPlayer);
    player.sendMessage(MiniMessage.miniMessage().deserialize(
        String.format(
            "<gray>Accepted <reset>%s %s<reset><gray>'s request.",
            targetPlayerData.getPrefix(),
            ((TextComponent) targetPlayer.displayName()).content()
        )
    ));

    Bukkit.getScheduler().cancelTask(Tpa.teleportThreads.get(targetPlayer));
    Tpa.teleportThreads.remove(targetPlayer);
    Tpa.teleportRequests.remove(targetPlayer);

    new TpaTeleport(plugin, targetPlayer, player, 5).runTaskTimer(plugin, 0L, 20L);

    return true;
  }
}
