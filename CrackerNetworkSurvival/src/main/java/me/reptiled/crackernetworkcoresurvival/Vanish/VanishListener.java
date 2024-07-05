package me.reptiled.crackernetworkcoresurvival.Vanish;

import me.reptiled.crackernetworkcoresurvival.CrackerNetworkCore;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

public class VanishListener implements Listener {

    CrackerNetworkCore plugin;
    public VanishListener(CrackerNetworkCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (player.hasPermission("vanish")) {
            return;
        }

        for (UUID p: VanishCommand.invisiblePlayersUuid) {
            Player targetPlayer = Bukkit.getPlayer(p);
            if (targetPlayer == null) {
                continue;
            }

            player.hidePlayer(plugin, targetPlayer);
        }
    }

    @EventHandler
    public void onAdvancement(PlayerAdvancementDoneEvent event) {
        Player player = event.getPlayer();
        if (VanishCommand.invisiblePlayersUuid.contains(player.getUniqueId())) {
            event.message(null);
        }
    }
}
