package com.github.soerxpso.xpspawners.manager;

import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import com.github.soerxpso.xpspawners.Spawner;

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
		spawners.add(spawner);
	}
}
