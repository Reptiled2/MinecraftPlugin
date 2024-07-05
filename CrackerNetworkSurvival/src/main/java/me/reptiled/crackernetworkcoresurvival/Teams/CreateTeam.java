package me.reptiled.crackernetworkcoresurvival.Teams;

import me.reptiled.crackernetworkcoresurvival.CrackerNetworkCore;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.ForwardingAudience;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.MetaNode;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.*;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class CreateTeam implements CommandExecutor {

  public static JSONObject teams;

  CrackerNetworkCore plugin;
  public CreateTeam(CrackerNetworkCore plugin) {
    this.plugin = plugin;

    try {
      teams =
          (JSONObject) new JSONParser().parse(
              new FileReader(new File(plugin.getDataFolder(), "teams.json"))
          );

    } catch (Exception e) {
      teams = new JSONObject();
    }
  }

  private String createTeam(Player player, String teamName) {
    JSONObject team = new JSONObject();
    team.put("name", teamName);
    team.put("owner", player.getUniqueId().toString());
    team.put("admins", new JSONArray());
    team.put("level", 0);
    team.put("prefix", null);
    team.put("members", new JSONArray());

    teams.put(teamName, team);
    return teamName;
  }

  public void saveData() {
    try {
      File path = new File(plugin.getDataFolder(), "teams.json");
      FileWriter fileWrite = new FileWriter(path);
      fileWrite.write(teams.toString());

      fileWrite.close();
    } catch (IOException err) {
      return;
    }
  }

  public static JSONObject getTeam(String teamId) {
    return (JSONObject) teams.get(teamId);
  }

  public static JSONObject getTeam(CrackerNetworkCore plugin, Player player) {
    return (JSONObject) teams.get(getUserTeamId(plugin, player));
  }

  public static Set<Player> getTeamMembers(String teamID) {
    JSONObject team = (JSONObject) teams.get(teamID);

    Set<Player> members = new HashSet<>();
    for (Object member: (JSONArray) team.get("members")) {
      Player iMember = Bukkit.getPlayer(UUID.fromString((String) member));
      if (iMember != null) {
        members.add(iMember);
      }
    }


    return members;
  }

  public static Set<Player> getTeamMembers(JSONObject team) {
    Set<Player> members = new HashSet<>();
    for (Object member: (JSONArray) team.get("members")) {
      Player iMember = Bukkit.getPlayer(UUID.fromString((String) member));
      if (iMember != null) {
        members.add(iMember);
      }
    }

    return members;
  }

  public static void updateUserTeam(CrackerNetworkCore plugin, Player player, String teamId) {
    User playerData = plugin.LuckpermsApi.getPlayerAdapter(Player.class).getUser(player);
    String currentTeamId = getUserTeamId(plugin, player);

    playerData.data().clear(NodeType.META.predicate(mn -> mn.getMetaKey().equals("team")));
    if (teamId == null) {
      if (!Objects.equals(currentTeamId, "-1")) {
        JSONObject currentTeamObject = getTeam(currentTeamId);
        if (currentTeamObject == null) {
          return;
        }

        JSONArray teamMembers = (JSONArray) currentTeamObject.get("members");
        if (teamMembers == null) {
          return;
        }

        if (currentTeamObject.get("owner").toString().equals(player.getUniqueId().toString())) {
          for (Object member: teamMembers) {
            plugin.LuckpermsApi.getUserManager().modifyUser(UUID.fromString(member.toString()), (User user) -> {
              user.data().clear(NodeType.META.predicate(mn -> mn.getMetaKey().equals("team")));
            });
          }

          teams.remove(currentTeamId);
          return;
        }


        teamMembers.remove(player.getUniqueId().toString());
        currentTeamObject.put("members", teamMembers);
        teams.put(currentTeamId, currentTeamObject);
      }

      return;
    }

    MetaNode node = MetaNode.builder("team", teamId).build();
    playerData.data().add(node);
    plugin.LuckpermsApi.getUserManager().saveUser(playerData);

    JSONObject teamObject = getTeam(teamId);
    if (teamObject == null) {
      return;
    }

    JSONArray teamMembers = (JSONArray) teamObject.get("members");
    if (teamMembers == null) {
      teamMembers = new JSONArray();
    }

    teamMembers.add(player.getUniqueId().toString());
    teamObject.put("members", teamMembers);
    teams.put(teamId, teamObject);

  }

  public static void updateUserTeam(CrackerNetworkCore plugin, OfflinePlayer player, String teamId) {
    User playerData = plugin.LuckpermsApi.getUserManager().loadUser(player.getUniqueId()).join();
    String currentTeamId = getUserTeamId(plugin, player);

    playerData.data().clear(NodeType.META.predicate(mn -> mn.getMetaKey().equals("team")));
    if (teamId == null) {
      if (!Objects.equals(currentTeamId, "-1")) {
        JSONObject currentTeamObject = getTeam(currentTeamId);
        if (currentTeamObject == null) {
          return;
        }

        JSONArray teamMembers = (JSONArray) currentTeamObject.get("members");
        if (teamMembers == null) {
          return;
        }

        if (currentTeamObject.get("owner").toString().equals(player.getUniqueId().toString())) {
          for (Object member : teamMembers) {
            plugin.LuckpermsApi.getUserManager().modifyUser(UUID.fromString(member.toString()), (User user) -> {
              user.data().clear(NodeType.META.predicate(mn -> mn.getMetaKey().equals("team")));
            });
          }

          teams.remove(currentTeamId);
          return;
        }

        teamMembers.remove(player.getUniqueId().toString());
        currentTeamObject.put("members", teamMembers);
        teams.put(currentTeamId, currentTeamObject);
      }
    }
  }

  public static String getUserTeamId(CrackerNetworkCore plugin, Player player) {
    CachedMetaData metaData = plugin.LuckpermsApi.getPlayerAdapter(Player.class).getMetaData(player);

    return metaData.getMetaValue("team", String::toString).orElse("-1");
  }

  public static String getUserTeamId(CrackerNetworkCore plugin, OfflinePlayer player) {
    CompletableFuture<User> user = plugin.LuckpermsApi.getUserManager().loadUser(player.getUniqueId());
    CachedMetaData metaData = user.join().getCachedData().getMetaData();

    return metaData.getMetaValue("team", String::toString).orElse("-1");
  }

  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
    if (!(sender instanceof Player player)) {
      return false;
    }

    if (args.length == 0 || args[0].length() < 3) {
      player.sendMessage(plugin.chatPrefix.append(MiniMessage.miniMessage().deserialize(
          "<red>Team name must be longer than 3 words."
      )));
      return true;
    }

    if (teams.containsKey(args[0])) {
      player.sendMessage(plugin.chatPrefix.append(MiniMessage.miniMessage().deserialize(
          "<red>This team already exists!"
      )));
      return true;
    }

    String userTeam = getUserTeamId(plugin, player);
    if (!Objects.equals(userTeam, "-1")) {
      player.sendMessage(plugin.chatPrefix.append(MiniMessage.miniMessage().deserialize(
          "<red>You are already in a team! Leave the team with <gray>/leaveteam</gray><red>."
      )));
      return true;
    }

    String teamId = createTeam(player, args[0]);
    updateUserTeam(plugin, player, teamId);

    player.sendMessage(plugin.chatPrefix.append(MiniMessage.miniMessage().deserialize(
        String.format("<dark_green>Successfully created team</dark_green> <green>%s</green><dark_green>.",
            args[0]
        )
    )));
    return true;
  }
}
