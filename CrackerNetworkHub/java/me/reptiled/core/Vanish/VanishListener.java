package me.reptiled.core.Vanish;

import me.reptiled.core.CrackerNetworkCore;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

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

        for (Player p: VanishCommand.invisiblePlayers) {
            if (!VanishCommand.invisiblePlayersUuid.contains(p.getUniqueId())) {
                VanishCommand.invisiblePlayers.remove(p);
                continue;
            }
            player.hidePlayer(plugin, p);
        }
    }
}
