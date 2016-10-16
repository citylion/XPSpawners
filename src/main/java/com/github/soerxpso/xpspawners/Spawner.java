package com.github.soerxpso.xpspawners;

import java.util.Collection;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.github.soerxpso.xpspawners.manager.ConfigManager;
import com.github.soerxpso.xpspawners.manager.SpawnerManager;

import vg.civcraft.mc.civmodcore.locations.QTBox;

public class Spawner implements QTBox, Comparable<Spawner> {
	private CreatureSpawner block;
	private SpawnerManager spawnerManager;
	
	public Spawner(CreatureSpawner block) {
		this.block = block;
		spawnerManager = XPSpawners.getPlugin().getSpawnerManager();
		
		new BukkitRunnable() {
			public void run() {
				trigger();
			}
		}.runTaskTimer(XPSpawners.getPlugin(), 0, ConfigManager.getHarvestInterval());
	}
	
	public boolean trigger() {
		Player recipient = null;
		double recipientDist = Double.MAX_VALUE;
		Collection<Entity> nearbyEntities = block.getWorld().getNearbyEntities(block.getLocation(), 33, 33, 33);
		if(nearbyEntities == null) return false;
		for(Entity e : nearbyEntities) {
			if(e.getType() == EntityType.PLAYER) {
				//if player has closer spawner, he activates that one instead
				if(spawnerManager.nearestSpawner(e.getLocation()) != this) {
					continue;
				}
				double eDist = e.getLocation().distance(block.getLocation());
				if(eDist < recipientDist) {
					recipient = (Player)e;
					recipientDist = eDist;
				}
			}
		}
		//if nearest player is out of range, return false
		if(recipientDist > 16) {
			return false;
		}
		
		giveXP(recipient);
		return true;
	}
	
	private void giveXP(Player p) {
		int amount = (int) (ConfigManager.getBaseXpPerHour() / 60f / 60f / 20f 
						* ConfigManager.getHarvestInterval());
		// pretty
		block.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, block.getLocation(), 1, 1, 1, 5);
		p.playSound(block.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
		
		p.giveExp(amount);
		XPSpawners.getPlugin().getLogger().log(Level.INFO, "Gave " + amount + " XP to " + p);
	}
	
	public Location getLocation() {
		return block.getLocation();
	}
	
	@Override
	public int qtXMax() {
		return getLocation().getBlockX() + 16;
	}

	@Override
	public int qtXMid() {
		return getLocation().getBlockX();
	}

	@Override
	public int qtXMin() {
		return getLocation().getBlockX() - 16;
	}

	@Override
	public int qtZMax() {
		return getLocation().getBlockZ() + 16;
	}

	@Override
	public int qtZMid() {
		return getLocation().getBlockZ();
	}

	@Override
	public int qtZMin() {
		return getLocation().getBlockZ() - 16;
	}

	@Override
	public int compareTo(Spawner o) {
		UUID thisWorld = getLocation().getWorld().getUID();
		UUID otherWorld = o.getLocation().getWorld().getUID();
		int worldCompare = thisWorld.compareTo(otherWorld);
		if (worldCompare != 0) {
			return worldCompare;
		}

		int thisX = getLocation().getBlockX();
		int thisY = getLocation().getBlockY();
		int thisZ = getLocation().getBlockZ();
		
		int otherX = o.getLocation().getBlockX();
		int otherY = o.getLocation().getBlockY();
		int otherZ = o.getLocation().getBlockZ();
		
		if (thisX < otherX) {
			return -1;
		}
		if (thisX > otherX) {
			return 1;
		}

		if (thisY < otherY) {
			return -1;
		}
		if (thisY > otherY) {
			return 1;
		}
		
		if (thisZ < otherZ) {
			return -1;
		}
		if (thisZ > otherZ) {
			return 1;
		}

		return 0;
	}
}