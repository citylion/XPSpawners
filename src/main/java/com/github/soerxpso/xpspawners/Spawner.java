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

import com.github.soerxpso.xpspawners.manager.ConfigManager;

import vg.civcraft.mc.civmodcore.locations.QTBox;

public class Spawner implements QTBox, Comparable<Spawner> {
	private Location location;
	private int xpAmountPerHarvest;
	
	public Spawner(CreatureSpawner block) {
		this.location = block.getLocation().clone();
		this.xpAmountPerHarvest = (int) (ConfigManager.getBaseXpPerHour() / 60f / 60f / 20f 
				* ConfigManager.getHarvestInterval());
	}
	
	public Player findNearestPlayer() {
		Collection<Entity> nearbyEntities = 
				location.getWorld().getNearbyEntities(this.location, 33, 33, 33);
		Player nearest = null;
		double nearestDistance = Double.MAX_VALUE;
		for(Entity e : nearbyEntities) {
			if(e.getType() == EntityType.PLAYER) {
				double eDist = e.getLocation().distance(location);
				if(eDist < nearestDistance) {
					nearest = (Player)e;
					nearestDistance = eDist;
				}
			}
		}
		return nearest;
	}
	
	public void giveXP(Player p) {
		location.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, location, 1, 1, 1, 15);
		int oldLevel = p.getLevel();
		// Give between 1 and 2x Amount per harvest randomly, so over time it evens out perfectly but has variety.
		int xpToGive = 1 + (int) Math.floor(Math.random() * 2.0 * (double) xpAmountPerHarvest);
		p.giveExp(xpToGive);
		// gotta do this check so the sounds don't sync up awkwardly
		if(!(oldLevel < p.getLevel() && p.getLevel() % 5 == 0)) {
			p.playSound(location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.5f, 1);
		}
		XPSpawners.getPlugin().getLogger().log(Level.INFO, "Gave " + xpToGive + " XP to " + p);
	}
	
	public Location getLocation() {
		return location;
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
