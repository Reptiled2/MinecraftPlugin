package me.reptiled.crackernetworkcoresurvival.JoinMessages;

import me.reptiled.crackernetworkcoresurvival.ChatHandler.ChannelCommand;
import me.reptiled.crackernetworkcoresurvival.CrackerNetworkCore;
import me.reptiled.crackernetworkcoresurvival.Teams.CreateTeam;
import me.reptiled.crackernetworkcoresurvival.Vanish.VanishCommand;
import me.reptiled.crackernetworkcoresurvival.Vanish.VanishHotbar;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.luckperms.api.cacheddata.CachedMetaData;
import org.bukkit.*;
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

            player.setMetadata("vanished", new FixedMetadataValue(plugin, true));
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.hasPermission("vanish")) {
                    continue;
                }

                p.hidePlayer(plugin, player);
            };

            plugin.TabApi.getTabListFormatManager().setName(plugin.TabApi.getPlayer(player.getUniqueId()),
                "<gray><italic>" + ((TextComponent) player.displayName()).content());

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

        final CachedMetaData playerData = plugin.LuckpermsApi.getPlayerAdapter(Player.class).getMetaData(player);
        String channel = playerData.getMetaValue("chat.channel");
        String teamId = CreateTeam.getUserTeamId(plugin, player);

        if (channel != null) {
            if (channel.equals("team") && !teamId.equals("-1")) {
                player.sendMessage(plugin.chatPrefix.append(MiniMessage.miniMessage().deserialize(
                    "Joined channel <gold>TEAM<gray>."
                )));

            } else if (channel.equals("staff") && player.hasPermission("chat.staff")) {
                player.sendMessage(plugin.chatPrefix.append(MiniMessage.miniMessage().deserialize(
                    "Joined channel <light_purple>STAFF<gray>."
                )));
            } else if (channel.equals("local")) {
                player.sendMessage(plugin.chatPrefix.append(MiniMessage.miniMessage().deserialize(
                    "Joined channel <aqua>LOCAL<gray>."
                )));
        }
        }

        if ((int) player.getLastLogin() == 0) {
            Location location;
            while (true) {
                Random random = new Random();
                int x = random.nextInt(1000);
                int z = random.nextInt(1000);

                location = new Location(player.getWorld(), x, 200, z);
                int y = location.getWorld().getHighestBlockYAt(location);
                location.setY(y + 1);

                Material blockType = location.getWorld().getType(x, y, z);
                if (blockType != Material.WATER && blockType != Material.LAVA && blockType.isSolid()) {
                    break;
                }
            }
            player.teleportAsync(location);
            player.setRespawnLocation(location, true);
        }

        if (player.hasPermission("chat.staff")) {
            ChannelCommand.staffMembers.add(player);
        }
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

        if (player.hasPermission("staff.chat")) {
            ChannelCommand.staffMembers.remove(player);
        }
    }

}
