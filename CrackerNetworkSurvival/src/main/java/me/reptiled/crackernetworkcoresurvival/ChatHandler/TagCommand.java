package me.reptiled.crackernetworkcoresurvival.ChatHandler;

import me.reptiled.crackernetworkcoresurvival.CrackerNetworkCore;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.NodeEqualityPredicate;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.MetaNode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public class TagCommand implements CommandExecutor {

  CrackerNetworkCore plugin;
  public TagCommand(CrackerNetworkCore plugin) {
    this.plugin = plugin;
  }

  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
    if (!(sender instanceof Player player)) {
      return false;
    }

    if (args.length == 0) {
      return true;
    }

    final User playerData = plugin.LuckpermsApi.getPlayerAdapter(Player.class).getUser(player);
    if (args[0].equalsIgnoreCase("team")) {
      final Node tagNode = MetaNode.builder("chat.tag", "team").build();

      playerData.data().clear(NodeType.META.predicate(mn -> mn.getMetaKey().equals("chat.tag")));
      playerData.data().add(tagNode);

    } else if (args[0].equalsIgnoreCase("role")) {
      final Node tagNode = MetaNode.builder("chat.tag", "role").build();

      playerData.data().clear(NodeType.META.predicate(mn -> mn.getMetaKey().equals("chat.tag")));
      playerData.data().add(tagNode);

    } else {
      player.sendMessage(plugin.chatPrefix.append(MiniMessage.miniMessage().deserialize(
          "<red>Wrong parameters!</red>"
      )));
      return true;
    }

    player.sendMessage(plugin.chatPrefix.append(MiniMessage.miniMessage().deserialize(
        "<gray>Sucessfully changed team tag!</gray>"
    )));
    return true;
  }
}
