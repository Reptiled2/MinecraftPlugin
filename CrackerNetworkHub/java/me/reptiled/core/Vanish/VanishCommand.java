package me.reptiled.core.Vanish;

import me.reptiled.core.CrackerNetworkCore;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


public class VanishCommand implements CommandExecutor {

    public static final ArrayList<Player> invisiblePlayers = new ArrayList<Player>();
    public static final ArrayList<java.util.UUID> invisiblePlayersUuid = new ArrayList<java.util.UUID>();
    CrackerNetworkCore plugin;
    public static VanishHotbar hotbarTask;


    public VanishCommand(CrackerNetworkCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(sender instanceof Player player)) {
            return false;
        }

        if (invisiblePlayers.contains(player)) {
            invisiblePlayers.remove(player);
            invisiblePlayersUuid.remove(player.getUniqueId());
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.showPlayer(plugin, player);
            };

            player.sendMessage(plugin.chatPrefix.append(MiniMessage.miniMessage()
                    .deserialize("You are now visible!"))
            );

            plugin.getServer().broadcast(MiniMessage.miniMessage()
                    .deserialize(
                            String.format("<gray>[</gray><green>+</green><gray>] </gray><gray>%s</gray>", ((TextComponent) player.displayName()).content())
                    )
            );

        } else {
            invisiblePlayers.add(player);
            invisiblePlayersUuid.add(player.getUniqueId());

            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.hasPermission("vanish")) {
                    continue;
                }

              p.hidePlayer(plugin, player);
            };


            if (hotbarTask == null || hotbarTask.executed || hotbarTask.isCancelled()) {
                hotbarTask = new VanishHotbar();
                hotbarTask.runTaskTimer(this.plugin, 0L, 40L);
                hotbarTask.executed = true;
            }

            player.sendMessage(plugin.chatPrefix.append(MiniMessage.miniMessage()
                    .deserialize("You are now invisible!"))
            );

            plugin.getServer().broadcast(MiniMessage.miniMessage()
                    .deserialize(
                            String.format("<gray>[</gray><red>-</red><gray>] </gray><gray>%s</gray>", ((TextComponent) player.displayName()).content())
                    )
            );


        }

        return true;
    }
}
