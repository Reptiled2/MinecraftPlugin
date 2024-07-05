package me.reptiled.core.Spawn;

import me.reptiled.core.CrackerNetworkCore;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;


public class Spawn implements Listener {
  CrackerNetworkCore plugin;
  public Spawn(CrackerNetworkCore plugin) {
    this.plugin = plugin;
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onPlayerJoin(PlayerJoinEvent e) {
    Player player = e.getPlayer();
    Location spawn = new Location(player.getWorld(), 8.5, -30.0, 5.5);

    new BukkitRunnable() {
      public void run() {
        player.teleport(spawn);
      }
    }.runTaskLater(plugin, 1L);
  }

  @EventHandler
  public void onFoodLevelChange(FoodLevelChangeEvent e) {
    e.setFoodLevel(20);
  }

  @EventHandler
  public void onDamage(EntityDamageEvent e) {
    if (!(e.getEntity() instanceof Player player)) {
      return;
    }

    if (e.getCause() == EntityDamageEvent.DamageCause.VOID) {
      player.teleportAsync(new Location(player.getWorld(), 8.5, -30.0, 5.5));
      e.setCancelled(true);
    }
  }
}
