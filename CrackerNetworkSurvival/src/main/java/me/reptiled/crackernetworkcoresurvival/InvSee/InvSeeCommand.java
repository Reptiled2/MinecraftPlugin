package me.reptiled.crackernetworkcoresurvival.InvSee;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class InvSeeCommand implements CommandExecutor {
  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
    if (!(sender instanceof Player player)) {
      return false;
    }

    if (args.length == 0) {
      player.sendMessage(Component.text("Please fill all the required parameters."));
      return true;
    }

    Player targetPlayer = Bukkit.getPlayerExact(args[0]);
    if (targetPlayer == null) {
      player.sendMessage(Component.text("Player not found."));
      return true;
    }

    player.openInventory(targetPlayer.getInventory());

    return true;
  }
}
