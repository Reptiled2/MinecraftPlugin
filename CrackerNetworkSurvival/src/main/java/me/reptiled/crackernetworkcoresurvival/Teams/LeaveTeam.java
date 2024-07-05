package me.reptiled.crackernetworkcoresurvival.Teams;

import me.reptiled.crackernetworkcoresurvival.CrackerNetworkCore;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class LeaveTeam implements CommandExecutor {

  CrackerNetworkCore plugin;
  public LeaveTeam(CrackerNetworkCore plugin) {
    this.plugin = plugin;
  }


  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
    if (!(sender instanceof Player player)) {
      return false;
    }

    String teamId = CreateTeam.getUserTeamId(plugin, player);
    if (Objects.equals(teamId, "-1")) {
      player.sendMessage(plugin.chatPrefix.append(MiniMessage.miniMessage().deserialize(
          "<red>You are not in a team!"
      )));
      return true;
    }

    Audience audience = Audience.audience(CreateTeam.getTeamMembers(teamId));
    audience.sendMessage(MiniMessage.miniMessage().deserialize(
        String.format(
            "<gold>[T] <reset>%s <yellow>left the team.",
            ((TextComponent) player.displayName()).content()
        )
    ));

    CreateTeam.updateUserTeam(plugin, player, null);
    player.sendMessage(plugin.chatPrefix.append(MiniMessage.miniMessage().deserialize(
        "<gray>You left the team."
    )));

    return true;
  }
}
