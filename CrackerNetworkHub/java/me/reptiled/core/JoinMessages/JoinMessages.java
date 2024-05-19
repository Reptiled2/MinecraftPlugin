package me.reptiled.core.JoinMessages;

import me.reptiled.core.CrackerNetworkCore;
import me.reptiled.core.Vanish.VanishHotbar;
import me.reptiled.core.Vanish.VanishCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinMessages implements Listener {

    CrackerNetworkCore plugin;

    public JoinMessages(CrackerNetworkCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
        player.sendMessage(plugin.chatPrefix.append(
            MiniMessage.miniMessage()
                .deserialize(
                    String.format("Welcome %s!", ((TextComponent) player.displayName()).content())
                )
        ));

        player.showTitle(Title.title(
            MiniMessage.miniMessage().deserialize(
                "<bold><gradient:#AA00AA:#FF55FF>ᴄʀᴀᴄᴋᴇʀ ɴᴇᴛᴡᴏʀᴋ</gradient><reset>"
            ),
            Component.text(String.format(
                    "Welcome %s!", ((TextComponent) player.displayName()).content()),
                NamedTextColor.WHITE
            )
        ));


        if (VanishCommand.invisiblePlayersUuid.contains(player.getUniqueId())) {
            event.setJoinMessage("");
            VanishCommand.invisiblePlayers.add(player);
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
