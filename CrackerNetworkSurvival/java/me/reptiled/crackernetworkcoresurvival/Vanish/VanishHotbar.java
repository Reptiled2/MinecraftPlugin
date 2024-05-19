package me.reptiled.crackernetworkcoresurvival.Vanish;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;


public class VanishHotbar extends BukkitRunnable {
    public boolean executed = false;

    @Override
    public void run() {
        if (VanishCommand.invisiblePlayers.isEmpty()) {
            cancel();
            return;
        }

        for (Player p : VanishCommand.invisiblePlayers) {
            p.sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.RED + "" + ChatColor.BOLD + "VANISHED"));
        }
    }

}
