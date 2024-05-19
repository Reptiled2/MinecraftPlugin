package me.reptiled.crackernetworkcoresurvival.JoinMessages;

import me.reptiled.crackernetworkcoresurvival.CrackerNetworkCore;
import me.reptiled.crackernetworkcoresurvival.Vanish.VanishCommand;
import me.reptiled.crackernetworkcoresurvival.Vanish.VanishHotbar;
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
