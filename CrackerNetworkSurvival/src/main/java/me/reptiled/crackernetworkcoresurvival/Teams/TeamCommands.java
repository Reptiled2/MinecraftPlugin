package me.reptiled.crackernetworkcoresurvival.Teams;

import me.reptiled.crackernetworkcoresurvival.CrackerNetworkCore;
import me.reptiled.crackernetworkcoresurvival.Vanish.VanishCommand;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.ForwardingAudience;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.luckperms.api.cacheddata.CachedMetaData;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.*;

public class TeamCommands implements CommandExecutor {

  public static HashMap<UUID, InvitePair> teamInvites = new HashMap<>();

  CrackerNetworkCore plugin;
  public TeamCommands(CrackerNetworkCore plugin) {
    this.plugin = plugin;
  }

  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
    if (!(sender instanceof Player player)) {
      return false;
    }

    if (args.length == 0) {
      String teamId = CreateTeam.getUserTeamId(plugin, player);
      JSONObject team = CreateTeam.getTeam(teamId);
      if (team == null) {
        player.sendMessage(plugin.chatPrefix.append(MiniMessage.miniMessage().deserialize(
            "<red>You are not in a team!"
        )));
        return true;
      }

      String teamOwner = (String) team.get("owner");
      StringJoiner teamMembers = new StringJoiner(", ");
      for (Object member: (JSONArray) team.get("members")) {
        teamMembers.add(((TextComponent) Bukkit.getPlayer(UUID.fromString(member.toString())).displayName()).content());
      }

      Object teamPrefix = team.get("prefix");
      player.sendMessage(plugin.chatPrefix.append(MiniMessage.miniMessage().deserialize(
          String.format(
              "\n<gold> -------- <b><dark_aqua>%s</b><gold> --------<reset>\n" +
                  "<gold>Team Owner: <aqua>%s<reset>\n" +
                  "<gold>Team Members: <aqua>%s\n" +
                  "<gold>Tag: <aqua>%s<reset>\n",
              teamId,
              ((TextComponent) Bukkit.getPlayer(UUID.fromString(teamOwner)).displayName()).content(),
              teamMembers,
              teamPrefix != null ? teamPrefix.toString() : "None"
          )
      )));
      return true;
    }

    if (args[0].equalsIgnoreCase("accept")) {
      InvitePair invite = teamInvites.get(player.getUniqueId());
      if (invite == null) {
        player.sendMessage(plugin.chatPrefix.append(MiniMessage.miniMessage().deserialize(
            "<red>You don't have any invites!"
        )));
        return true;
      }

      invite.getTask().cancel();
      String teamName = invite.getTeamName();
      JSONObject team = (JSONObject) CreateTeam.teams.get(teamName);

      ForwardingAudience audience = Audience.audience(CreateTeam.getTeamMembers(teamName));

      final CachedMetaData playerData = plugin.LuckpermsApi.getPlayerAdapter(Player.class).getMetaData(player);
      audience.sendMessage(MiniMessage.miniMessage().deserialize(
          String.format(
              "<gold>[T] <green>%s %s <yellow>joined the team.",
              playerData.getPrefix() != null ? playerData.getPrefix() : "",
              ((TextComponent) player.displayName()).content()
          )
      ));

      CreateTeam.updateUserTeam(plugin, player, teamName);
      player.sendMessage(plugin.chatPrefix.append(MiniMessage.miniMessage().deserialize(
          String.format("<gold>[T] <yellow>You joined <dark_green>%s<green>!",
              invite.getTeamName()
          )
      )));

      teamInvites.remove(player.getUniqueId());
      return true;
    }

    if (args[0].equalsIgnoreCase("deny")) {
      InvitePair invite = teamInvites.get(player.getUniqueId());
      if (invite == null) {
        player.sendMessage(plugin.chatPrefix.append(MiniMessage.miniMessage().deserialize(
            "<red>You don't have any invites!"
        )));
        return true;
      }

      invite.getTask().cancel();
      player.sendMessage(plugin.chatPrefix.append(MiniMessage.miniMessage().deserialize(
          String.format("<gray>Invite removed.",
              invite.getTeamName()
          )
      )));

      teamInvites.remove(player.getUniqueId());
      return true;
    }

    String teamId = CreateTeam.getUserTeamId(plugin, player);
    JSONObject team = CreateTeam.getTeam(teamId);
    if (team == null) {
      player.sendMessage(plugin.chatPrefix.append(MiniMessage.miniMessage().deserialize(
          "<red>You are not in a team!"
      )));
      return true;
    }

    String teamOwner = (String) team.get("owner");

    if (args[0].equalsIgnoreCase("invite")) {
      if (!teamOwner.equals(player.getUniqueId().toString())) {
        player.sendMessage(plugin.chatPrefix.append(MiniMessage.miniMessage().deserialize(
            "<red>You are not permitted to do that!"
        )));

        return true;
      }

      if (args.length < 2) {
        player.sendMessage(plugin.chatPrefix.append(MiniMessage.miniMessage().deserialize(
            "<red>Couldn't find player!"
        )));
        return true;
      }

      Player targetPlayer = Bukkit.getPlayer(args[1]);
      if (targetPlayer == null ||
          (VanishCommand.invisiblePlayersUuid.contains(targetPlayer.getUniqueId()) && !player.hasPermission("vanish"))) {
        player.sendMessage(plugin.chatPrefix.append(MiniMessage.miniMessage().deserialize(
            "<red>Couldn't find player!"
        )));
        return true;
      }

      if (!CreateTeam.getUserTeamId(plugin, targetPlayer).equals("-1")) {
        player.sendMessage(plugin.chatPrefix.append(MiniMessage.miniMessage().deserialize(
            "<red>This player already has a team!"
        )));

        return true;
      }

      if (teamInvites.containsKey(targetPlayer.getUniqueId())) {
        player.sendMessage(plugin.chatPrefix.append(MiniMessage.miniMessage().deserialize(
            "<red>This player already has an invite!"
        )));

        return true;
      }

      final CachedMetaData targetPlayerData = plugin.LuckpermsApi.getPlayerAdapter(Player.class).getMetaData(targetPlayer);

      String teamName = (String) team.get("name");
      targetPlayer.sendMessage(plugin.chatPrefix.append(MiniMessage.miniMessage().deserialize(
          String.format(
              "\n<gold> -------- Team Invitation -------- </gold>\n" +
              "<gold>|</gold> You have received an invitation from team <dark_aqua>%s</dark_aqua><gray>.</gray>\n" +
              "<gold>|</gold> <dark_green><click:run_command:'/team accept %s'>/team accept %s</click></dark_green> <gray>to accept the invitation.</gray>\n" +
              "<gold>|</gold> <red><click:run_command:'/team deny %s'>/team deny %s</click></red> <gray>to deny the invitation</gray>\n" +
              "<gold>|</gold>\n<gold>|</gold> <gray>You have 120 seconds to accept the invitation.</gray>",
              teamName,
              teamName, teamName,
              teamName, teamName
      ))
      ));

      player.sendMessage(plugin.chatPrefix.append(MiniMessage.miniMessage().deserialize(
          String.format(
              "<gray>Sent invitation to %s %s<reset><gray>.",
              targetPlayerData.getPrefix() != null ? targetPlayerData.getPrefix() : "",
              ((TextComponent) targetPlayer.displayName()).content()
          )
      )));

      teamInvites.put(targetPlayer.getUniqueId(), new InvitePair(plugin, targetPlayer.getUniqueId(), teamName));

      ForwardingAudience audience = Audience.audience(CreateTeam.getTeamMembers(teamName));

      audience.sendMessage(MiniMessage.miniMessage().deserialize(
          String.format(
              "<gold>[T] <green>%s %s <yellow>has been invited to the team.",
              targetPlayerData.getPrefix() != null ? targetPlayerData.getPrefix() : "",
              ((TextComponent) targetPlayer.displayName()).content()
          )
      ));

      return true;
    }

    if (args[0].equalsIgnoreCase("kick")) {
      if (!teamOwner.equals(player.getUniqueId().toString())) {
        player.sendMessage(plugin.chatPrefix.append(MiniMessage.miniMessage().deserialize(
            "<red>You are not permitted to do that!"
        )));

        return true;
      }

      if (args.length < 2) {
        player.sendMessage(plugin.chatPrefix.append(MiniMessage.miniMessage().deserialize(
            "<red>Couldn't find player!"
        )));
        return true;
      }

      JSONArray teamMembers = (JSONArray) team.get("members");
      Player targetPlayer = Bukkit.getPlayer(args[1]);
      if (targetPlayer == null) {

      }

      if (!teamMembers.contains(targetPlayer.getUniqueId().toString())) {
        player.sendMessage(plugin.chatPrefix.append(MiniMessage.miniMessage().deserialize(
            "<red>This player is not in your team!"
        )));
        return true;
      }

      CreateTeam.updateUserTeam(plugin, targetPlayer, null);

      Audience.audience(CreateTeam.getTeamMembers(teamId)).sendMessage(MiniMessage.miniMessage().deserialize(
          String.format(
              "<gold>[T] <dark_green>%s <yellow>has been <dark_red>kicked <yellow>from <dark_aqua>%s",
              targetPlayer.getName(), teamId
          )
      ));


    }

    if (args[0].equalsIgnoreCase("prefix") || args[0].equalsIgnoreCase("tag")) {
      if (!teamOwner.equals(player.getUniqueId().toString())) {
        player.sendMessage(plugin.chatPrefix.append(MiniMessage.miniMessage().deserialize(
            "<red>You are not permitted to do that!"
        )));
      }

      if (args.length >= 2) {
        TextComponent tag = (TextComponent) MiniMessage.miniMessage().deserialize(args[1]);

        if (tag.content().length() > 6 || tag.content().length() < 3) {
          player.sendMessage(plugin.chatPrefix.append(MiniMessage.miniMessage().deserialize(
              "<red>Tag must be between 3-6 words!"
          )));
          return true;
        }

        for (Object iTeam: CreateTeam.teams.values()) {
          Object teamPrefix = ((JSONObject) iTeam).get("prefix");
          if (Objects.equals((teamPrefix != null ? teamPrefix.toString() : ""), tag.content())) {
            player.sendMessage(plugin.chatPrefix.append(MiniMessage.miniMessage().deserialize(
                "<red>Another team already has this tag!"
            )));
            return true;
          }
        }

        team.put("prefix", tag.content());

        player.sendMessage(plugin.chatPrefix.append(MiniMessage.miniMessage().deserialize(
            "<green>Successfully changed team's tag!"
        )));

        return true;
      }
    }

    return true;
  }
}

class InvitePair {
  private final BukkitRunnable task;
  private final String teamName;

  public InvitePair(CrackerNetworkCore plugin, UUID playerUUID, String teamName) {
    this.teamName = teamName;

    this.task = new BukkitRunnable() {
      @Override
      public void run() {
        TeamCommands.teamInvites.remove(playerUUID);
      }
    };

    task.runTaskLaterAsynchronously(plugin,  2400L);
  }

  public BukkitRunnable getTask() {
    return task;
  }

  public String getTeamName() {
    return teamName;
  }
}