package com.github.soerxpso.xpspawners;

import java.util.logging.Level;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.github.soerxpso.xpspawners.listeners.BlockListener;
import com.github.soerxpso.xpspawners.listeners.ChunkListener;
import com.github.soerxpso.xpspawners.listeners.MobSpawnListener;
import com.github.soerxpso.xpspawners.manager.ConfigManager;
import com.github.soerxpso.xpspawners.manager.SpawnerManager;
import org.bukkit.scheduler.BukkitScheduler;

public class XPSpawners extends JavaPlugin {

	private static XPSpawners plugin;
	private SpawnerManager spawnerManager;
	
	public void onEnable() {
		plugin = this;
		spawnerManager = new SpawnerManager();
		ConfigManager.loadConfig();
		getServer().getPluginManager().registerEvents(new BlockListener(), this);
		getServer().getPluginManager().registerEvents(new MobSpawnListener(), this);
		getServer().getPluginManager().registerEvents(new ChunkListener(), this);

		BukkitScheduler scheduler = getServer().getScheduler();
		scheduler.scheduleSyncRepeatingTask(this, this::givePlayersXP, 0l, ConfigManager.getHarvestInterval());
	}
	
	public void givePlayersXP() {
		for(Player p : getServer().getOnlinePlayers()) {
			if (p.isDead()) {
				continue;
			}
			Spawner s = spawnerManager.nearestSpawner(p.getLocation());
			if(s == null) continue;
			getLogger().log(Level.FINE, "Nearest spawner to " + p + " is at " + s.getLocation());
			Player nearestToSpawner = s.findNearestPlayer();
			getLogger().log(Level.FINE, "Nearest player to the spawner at " + s.getLocation() + " is " + nearestToSpawner);
			if(nearestToSpawner == p) {
				if(p.getLocation().distance(s.getLocation()) < 16) {
					s.giveXP(p);
				}
			}
		}
	}
	
	public static XPSpawners getPlugin() {
		return plugin;
	}
	
	public SpawnerManager getSpawnerManager() {
		return spawnerManager;
	}
}
