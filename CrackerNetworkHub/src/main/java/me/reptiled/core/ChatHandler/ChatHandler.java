package me.reptiled.crackernetworkcoresurvival.ChatHandler;

import io.papermc.paper.chat.ChatRenderer;
import io.papermc.paper.event.player.AsyncChatEvent;
import me.reptiled.core.CrackerNetworkCore;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.luckperms.api.cacheddata.CachedMetaData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

public class ChatHandler implements Listener {

  CrackerNetworkCore plugin;
  public ChatHandler(CrackerNetworkCore plugin) {
    this.plugin = plugin;
  }

  @EventHandler(priority = EventPriority.NORMAL)
  public void onChat(AsyncChatEvent event) {
    event.renderer(ChatRenderer.viewerUnaware(new ChatRenderer.ViewerUnaware() {
      @Override
      public @NotNull Component render(@NotNull Player source, @NotNull Component sourceDisplayName, @NotNull Component message) {
        final CachedMetaData playerData = plugin.LuckpermsApi.getPlayerAdapter(Player.class).getMetaData(source);
        return MiniMessage.miniMessage()
            .deserialize(
                String.format("%s %s <reset><b><dark_gray>>></dark_gray></b> %s",
                    playerData.getPrefix() != null ? playerData.getPrefix() : "",
                    ((TextComponent) sourceDisplayName).content(),
                    playerData.getSuffix() != null ? playerData.getSuffix() : ""
                )
            )
            .append(
                source.hasPermission("chat.color") ? MiniMessage.miniMessage().deserialize(
                    ((TextComponent) message).content()
                ) : Component.text(((TextComponent) message).content())
            )
            ;
      }
    }));
  }
}
