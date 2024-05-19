package me.reptiled.crackernetworkcoresurvival.TabFixer;

import me.reptiled.crackernetworkcoresurvival.CrackerNetworkCore;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;


public class TabFixer implements Listener {

  private class task extends BukkitRunnable {
    @Override
    public void run() {
      plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), "tab reload");
    }
  }

  CrackerNetworkCore plugin;
  public TabFixer(CrackerNetworkCore plugin) {
    this.plugin = plugin;
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onJoin(PlayerJoinEvent event) {
    new task().runTaskLater(plugin, 20L);
  }
}
