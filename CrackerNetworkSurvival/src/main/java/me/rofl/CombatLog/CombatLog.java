package me.rofl.CombatLog;

import me.reptiled.crackernetworkcoresurvival.CrackerNetworkCore;
import me.reptiled.crackernetworkcoresurvival.Teams.CreateTeam;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class CombatLog implements Listener {
  private static final Map<UUID, UUID> recentAttackers = new HashMap<>();
  public static final Map<UUID, Long> logoutTimes = new HashMap<>();
  public final Map<UUID, BukkitRunnable> timerThreads = new HashMap<>();

  private final CrackerNetworkCore plugin;
  private static final long COMBAT_LOG_DURATION = 60000;

  public CombatLog(CrackerNetworkCore plugin) {
    this.plugin = plugin;
  }

  @EventHandler
  public void onPlayerLogOut(PlayerQuitEvent event) {
    Player player = event.getPlayer();
    if (player.getGameMode() != GameMode.SURVIVAL) return;

    UUID playerId = player.getUniqueId();
    UUID attackerId = recentAttackers.get(playerId);

    if (attackerId != null && System.currentTimeMillis() - logoutTimes.getOrDefault(playerId, 0L) <= COMBAT_LOG_DURATION) {
      for (ItemStack item : player.getInventory().getContents()) {
        if (item != null) {
          player.getWorld().dropItemNaturally(player.getLocation(), item);
        }
      }
      player.getInventory().clear();
      player.setHealth(0.0);

      // Clean up maps
      recentAttackers.remove(playerId);
      logoutTimes.remove(playerId);

      Player attacker = plugin.getServer().getPlayer(attackerId);
      if (attacker != null) {
        player.sendMessage("While you were offline, you were killed by " + attacker.getName());
      } else {
        player.sendMessage("While you were offline, you were killed.");
      }
    }
  }

  @EventHandler
  public void onPlayerLogIn(PlayerJoinEvent event) {
    Player player = event.getPlayer();
    UUID playerId = player.getUniqueId();

    if (recentAttackers.containsKey(playerId) && System.currentTimeMillis() - logoutTimes.getOrDefault(playerId, 0L) <= COMBAT_LOG_DURATION) {
      recentAttackers.remove(playerId);
      logoutTimes.remove(playerId);
      player.setHealth(0.0);
      player.sendMessage("You were killed while you were offline.");
    }
  }

  @EventHandler
  public void onEntityDamage(EntityDamageEvent event) {
    if (!(event.getEntity() instanceof Player damaged)) {
      return;
    }

    UUID damagerId = null;

    if (event instanceof EntityDamageByEntityEvent entityDamageByEntityEvent) {
      if (entityDamageByEntityEvent.getDamager() instanceof Player damagerPlayer) {
        String damagerTeam = CreateTeam.getUserTeamId(plugin, damagerPlayer);
        String damagedTeam = CreateTeam.getUserTeamId(plugin, damaged);

        if (!damagerTeam.equals("-1") && !damagedTeam.equals("-1") && damagerTeam.equals(damagedTeam)) {
          return;
        }

        if (damagerPlayer.getGameMode() != GameMode.CREATIVE) {
          damagerId = damagerPlayer.getUniqueId();
        }
      }
    }

    if (damagerId != null) {
      recentAttackers.put(damaged.getUniqueId(), damagerId);
      logoutTimes.put(damaged.getUniqueId(), System.currentTimeMillis());

      BukkitRunnable userThread = timerThreads.get(damagerId);

      if (userThread == null || userThread.isCancelled()) {
        BukkitRunnable newThread = new CombatThread(this, damaged);
        newThread.runTaskTimer(plugin, 0L, 20L);

        timerThreads.put(damagerId, newThread);
      }
    }
  }

  @EventHandler
  public void onPlayerDeath(PlayerDeathEvent event) {
    Player player = event.getEntity();

    logoutTimes.remove(player.getUniqueId());
    recentAttackers.remove(player.getUniqueId());
  }

  public static boolean isCombatLogged(Player player) {
    return recentAttackers.containsKey(player.getUniqueId()) &&
        System.currentTimeMillis() - logoutTimes.get(player.getUniqueId()) < COMBAT_LOG_DURATION;
  }
}