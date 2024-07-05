package me.reptiled.core.JoinMessages;

import me.reptiled.core.CrackerNetworkCore;
import me.reptiled.core.Vanish.VanishHotbar;
import me.reptiled.core.Vanish.VanishCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.Title;
import net.luckperms.api.cacheddata.CachedMetaData;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.Random;

public class JoinMessages implements Listener {

    CrackerNetworkCore plugin;

    public JoinMessages(CrackerNetworkCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);

        if (VanishCommand.invisiblePlayersUuid.contains(player.getUniqueId())) {
            event.setJoinMessage("");

            plugin.TabApi.getTabListFormatManager().setName(plugin.TabApi.getPlayer(player.getUniqueId()),
                "<gray><italic>" + ((TextComponent) player.displayName()).content());

            player.setMetadata("vanished", new FixedMetadataValue(plugin, true));
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.hasPermission("vanish")) {
                    continue;
                }

                p.hidePlayer(plugin, player);
            };

            if (VanishCommand.hotbarTask == null || VanishCommand.hotbarTask.executed || VanishCommand.hotbarTask.isCancelled()) {
                VanishCommand.hotbarTask = new VanishHotbar();
                VanishCommand.hotbarTask.runTaskTimer(this.plugin, 0L, 40L);
                VanishCommand.hotbarTask.executed = true;
            }
            return;
        }

        event.joinMessage(MiniMessage.miniMessage()
            .deserialize(
                String.format("<gray>[</gray><green>+</green><gray>] </gray><gray>%s</gray>", ((TextComponent) player.displayName()).content())
            )
        );
    }


    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        if (VanishCommand.invisiblePlayersUuid.contains(player.getUniqueId())) {
            event.setQuitMessage("");
            return;
        }

        event.quitMessage(MiniMessage.miniMessage()
            .deserialize(
                String.format("<gray>[</gray><red>-</red><gray>] </gray><gray>%s</gray>", ((TextComponent) player.displayName()).content())
            )
        );
    }

}
