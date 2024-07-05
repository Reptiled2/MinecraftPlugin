package me.reptiled.core.ChatHandler;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class DiscordCommand implements CommandExecutor {
  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
    if (!(sender instanceof Player player)) {
      return false;
    }

    player.sendMessage(MiniMessage.miniMessage().deserialize(
        "<click:open_url:'https://discord.gg/YXksKKPGb2'><color:#5991ff>Discord: discord.gg/YXksKKPGb2</color></click>"
    ));

    return true;
  }
}
