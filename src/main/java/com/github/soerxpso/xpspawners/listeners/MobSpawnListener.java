package com.github.soerxpso.xpspawners.listeners;

import org.bukkit.block.CreatureSpawner;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.SpawnerSpawnEvent;

import com.github.soerxpso.xpspawners.Spawner;
import com.github.soerxpso.xpspawners.XPSpawners;
import com.github.soerxpso.xpspawners.manager.SpawnerManager;

public class MobSpawnListener implements Listener {

	private SpawnerManager spawnerManager;
	
	public MobSpawnListener() {
		this.spawnerManager = XPSpawners.getPlugin().getSpawnerManager();
	}
	
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onMobSpawn(SpawnerSpawnEvent e) {
		e.setCancelled(true);
		
		CreatureSpawner b = e.getSpawner();
		b.setDelay(Integer.MAX_VALUE);
		Spawner spawner = spawnerManager.getSpawner(b.getLocation());
		if(spawner == null) {
			spawnerManager.addSpawner(new Spawner(b));
		}
	}
}
