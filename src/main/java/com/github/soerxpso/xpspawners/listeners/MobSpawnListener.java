package com.github.soerxpso.xpspawners.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.SpawnerSpawnEvent;

import com.github.soerxpso.xpspawners.Spawner;
import com.github.soerxpso.xpspawners.XPSpawners;
import com.github.soerxpso.xpspawners.manager.ConfigManager;
import com.github.soerxpso.xpspawners.manager.SpawnerManager;

public class MobSpawnListener implements Listener {
	
	private SpawnerManager spawnerManager;
	
	public MobSpawnListener() {
		spawnerManager = XPSpawners.getPlugin().getSpawnerManager();
	}

	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = false)
	public void onMobSpawn(SpawnerSpawnEvent e) {
		e.setCancelled(true);
		e.getSpawner().setDelay(ConfigManager.getHarvestInterval());
		
		Spawner spawner = spawnerManager.getSpawner(e.getSpawner().getLocation());
		if(spawner == null) {
			spawner = new Spawner(e.getSpawner());
			spawnerManager.addSpawner(spawner);
		}
		spawner.trigger();
	}
}
