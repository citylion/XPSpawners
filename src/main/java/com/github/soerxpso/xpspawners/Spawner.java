package com.github.soerxpso.xpspawners;

import java.util.Collection;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import com.github.soerxpso.xpspawners.manager.ConfigManager;
import com.github.soerxpso.xpspawners.manager.SpawnerManager;

public class Spawner {
	private long lastTriggered = 0;
	private Block block;
	private SpawnerManager spawnerManager;
	
	public Spawner(Block block) {
		this.block = block;
		lastTriggered = System.currentTimeMillis();
		spawnerManager = XPSpawners.getPlugin().getSpawnerManager();
	}
	
	public boolean trigger() {
		long timeSinceLast = System.currentTimeMillis() - lastTriggered;
		if(timeSinceLast / 1000 < ConfigManager.getHarvestInterval()) {
			return false;
		}else {
			boolean isActivated = false;
			Collection<Entity> nearbyEntities = block.getWorld().getNearbyEntities(block.getLocation(), 33, 33, 33);
			for(Entity e : nearbyEntities) {
				if(e.getType() == EntityType.PLAYER) {
					//if nearest player is out of range, return false
					if(e.getLocation().distance(block.getLocation()) > 16) {
						return false;
					}
					//if nearest player has does not have another spawner closer to him, he activates this one
					if(spawnerManager.nearestSpawner(e.getLocation()) == this) {
						isActivated = true;
						break;
					}
				}
			}
			if(!isActivated) return false;
			
			lastTriggered = System.currentTimeMillis();
			createXP();
			return true;
		}
	}
	
	private void createXP() {
		int harvestsPerDay = (int) (86400000 / ConfigManager.getHarvestInterval());
		createXP(ConfigManager.getBaseXpPerDay() / harvestsPerDay);
	}
	
	private void createXP(int amount) {
		for(int i = 0; i < amount; i++) {
			block.getWorld()
				.spawnEntity(block.getLocation().add(0, 1, 0), EntityType.EXPERIENCE_ORB);
		}
	}
	
	public Location getLocation() {
		return block.getLocation();
	}
}
