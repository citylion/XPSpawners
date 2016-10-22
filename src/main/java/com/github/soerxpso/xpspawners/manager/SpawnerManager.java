package com.github.soerxpso.xpspawners.manager;

import java.util.Set;
import java.util.logging.Level;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.block.CreatureSpawner;

import com.github.soerxpso.xpspawners.Spawner;
import com.github.soerxpso.xpspawners.XPSpawners;

import vg.civcraft.mc.civmodcore.locations.QTBox;
import vg.civcraft.mc.civmodcore.locations.SparseQuadTree;

public class SpawnerManager {
	
	private SparseQuadTree spawners = new SparseQuadTree();
	
	public ItemStack convertSpawnerToItem(Block b) {
		return new ItemStack(Material.MOB_SPAWNER);
	}
	
	public Spawner nearestSpawner(Location loc) {
		Spawner closest = null;
		Set<QTBox> nearby = spawners.find(loc.getBlockX(), loc.getBlockZ());
		for(QTBox box : nearby) {
			Spawner s = (Spawner)box;
			if(closest == null) {
				closest = s;
				continue;
			}
			if(loc.distance(s.getLocation()) < loc.distance(closest.getLocation())) {
				closest = s;
			}
		}
		return closest;
	}
	
	public Spawner getSpawner(Location loc) {
		Spawner s = nearestSpawner(loc);
		if(s == null) return null;
		if(s.getLocation().equals(loc)) return s;
		return null;
	}
	
	public void addSpawner(Spawner spawner) {
		XPSpawners.getPlugin().getLogger().log(Level.INFO, "Loaded spawner at " + spawner.getLocation());
		spawners.add(spawner);
	}
	
	public void safeAddSpawner(CreatureSpawner spawner) {
		if (getSpawner(spawner.getLocation()) == null) {
			addSpawner(new Spawner(spawner));
		}
	}

	public void removeSpawner(Spawner spawner) {
		spawners.remove(spawner);
	}
}
