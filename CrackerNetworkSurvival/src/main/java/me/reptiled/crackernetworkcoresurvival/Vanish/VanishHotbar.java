package me.reptiled.crackernetworkcoresurvival.Vanish;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;


public class VanishHotbar extends BukkitRunnable {
    public boolean executed = false;

    @Override
    public void run() {
        if (VanishCommand.invisiblePlayersUuid.isEmpty()) {
            cancel();
            return;
        }

        for (UUID playerUuid : VanishCommand.invisiblePlayersUuid) {
            Player player = Bukkit.getPlayer(playerUuid);
            if (player == null) {
                continue;
            }

            player.sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.RED + "" + ChatColor.BOLD + "VANISHED"));
        }
    }
}
