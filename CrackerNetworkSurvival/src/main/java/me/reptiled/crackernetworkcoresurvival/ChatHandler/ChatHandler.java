package me.reptiled.crackernetworkcoresurvival.ChatHandler;

import io.papermc.paper.chat.ChatRenderer;
import io.papermc.paper.event.player.AsyncChatEvent;
import me.reptiled.crackernetworkcoresurvival.CrackerNetworkCore;
import me.reptiled.crackernetworkcoresurvival.Teams.CreateTeam;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.luckperms.api.cacheddata.CachedMetaData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONObject;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ChatHandler implements Listener {

  private final CrackerNetworkCore plugin;

  public ChatHandler(CrackerNetworkCore plugin) {
    this.plugin = plugin;
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onChat(AsyncChatEvent event) {
    Player source = event.getPlayer();
    Component sourceDisplayName = source.displayName();
    Component message = event.message();

    final CachedMetaData playerData = plugin.LuckpermsApi.getPlayerAdapter(Player.class).getMetaData(source);
    String channel = playerData.getMetaValue("chat.channel");
    JSONObject playerTeam = CreateTeam.getTeam(plugin, source);
    String channelPrefix = null;

    if (channel != null) {
      if (channel.equals("team") && playerTeam != null) {
        event.viewers().clear();
        event.viewers().addAll(CreateTeam.getTeamMembers(playerTeam));
        channelPrefix = "<gold>[T]</gold> ";

      } else if (channel.equals("staff") && source.hasPermission("chat.staff")) {
        event.viewers().clear();
        event.viewers().addAll(ChannelCommand.staffMembers);
        channelPrefix = "<light_purple>[S]</light_purple> ";

      } else if (channel.equals("local")) {
        event.viewers().clear();

        event.viewers().add(source);
        plugin.getServer().getScheduler().runTask(plugin, () -> {
          for (Entity entity : source.getNearbyEntities(50D, 50D, 50D)) {
            plugin.getLogger().info(entity.getName());
            if (entity instanceof Player p) {
              plugin.getLogger().info("okok " + p.getName());
              event.viewers().add(p);
            }
          }
        });

        channelPrefix = "<aqua>[L]</aqua> ";
      }
    }

    String playerPrefix = playerData.getMetaValue("chat.tag");
    String teamPrefix = playerTeam != null ? Objects.toString(playerTeam.get("prefix")) : null;

    final Component formattedMessage = MiniMessage.miniMessage().deserialize(
            String.format("%s%s %s <reset><b><dark_gray>>></dark_gray></b> %s",
                channelPrefix != null ? channelPrefix : "",
                playerPrefix != null && playerPrefix.equals("team") && teamPrefix != null
                    ? "<dark_aqua>[" + teamPrefix + "]</dark_aqua>"
                    : (playerData.getPrefix() != null ? playerData.getPrefix() : ""),
                ((TextComponent) sourceDisplayName).content(),
                playerData.getSuffix() != null ? playerData.getSuffix() : ""
            ))
        .append(
            source.hasPermission("chat.color")
                ? MiniMessage.miniMessage().deserialize(((TextComponent) message).content())
                : Component.text(((TextComponent) message).content())
        );

    event.renderer(ChatRenderer.viewerUnaware((@NotNull Player player, @NotNull Component displayName, @NotNull Component msg) -> formattedMessage));
  }
}