package me.reptiled.crackernetworkcoresurvival.ChatHandler;

import me.reptiled.crackernetworkcoresurvival.CrackerNetworkCore;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.MetaNode;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;


public class ChannelCommand implements CommandExecutor {
  public static HashSet<Player> staffMembers = new HashSet<>();

  CrackerNetworkCore plugin;
  public ChannelCommand(CrackerNetworkCore plugin) {
    this.plugin = plugin;
  }

  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
    if (!(sender instanceof Player player)) {
      return false;
    }

    String channel = args.length > 0 ? args[0] : "all";
    final User playerData = plugin.LuckpermsApi.getPlayerAdapter(Player.class).getUser(player);

    if (channel.equals("all")) {
      final Node tagNode = MetaNode.builder("chat.channel", "all").build();

      playerData.data().clear(NodeType.META.predicate(mn -> mn.getMetaKey().equals("chat.channel")));
      playerData.data().add(tagNode);

      player.sendMessage(plugin.chatPrefix.append(MiniMessage.miniMessage().deserialize(
          "<gray>Sucessfully changed channel to <green>ALL<gray>."
      )));
      return true;

    }

    if (channel.equals("team")) {
      final Node tagNode = MetaNode.builder("chat.channel", "team").build();

      playerData.data().clear(NodeType.META.predicate(mn -> mn.getMetaKey().equals("chat.channel")));
      playerData.data().add(tagNode);

      player.sendMessage(plugin.chatPrefix.append(MiniMessage.miniMessage().deserialize(
          "<gray>Sucessfully changed channel to <gold>TEAM<gray>."
      )));
      return true;
    }

    if (channel.equals("local")) {
      final Node tagNode = MetaNode.builder("chat.channel", "local").build();

      playerData.data().clear(NodeType.META.predicate(mn -> mn.getMetaKey().equals("chat.channel")));
      playerData.data().add(tagNode);

      player.sendMessage(plugin.chatPrefix.append(MiniMessage.miniMessage().deserialize(
          "<gray>Sucessfully changed channel to <aqua>LOCAL<aqua>."
      )));
      return true;
    }

    if (channel.equals("staff") && player.hasPermission("chat.staff")) {
      final Node tagNode = MetaNode.builder("chat.channel", "staff").build();

      playerData.data().clear(NodeType.META.predicate(mn -> mn.getMetaKey().equals("chat.channel")));
      playerData.data().add(tagNode);

      player.sendMessage(plugin.chatPrefix.append(MiniMessage.miniMessage().deserialize(
          "<gray>Sucessfully changed channel to <light_purple>STAFF<gray>."
      )));
      return true;
    }

    return false;
  }
}

