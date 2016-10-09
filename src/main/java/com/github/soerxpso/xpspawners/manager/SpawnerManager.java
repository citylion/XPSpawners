package com.github.soerxpso.xpspawners.manager;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import com.github.soerxpso.xpspawners.Spawner;

public class SpawnerManager {
	private HashMap<Long, ArrayList<Spawner>> spawners = 
			new HashMap<Long, ArrayList<Spawner>>();
	
	public ItemStack convertSpawnerToItem(Block b) {
		return new ItemStack(Material.MOB_SPAWNER);
	}
	
	public Spawner nearestSpawner(Location loc) {
		Spawner closest = null;
		for(int xShift = -1; xShift <= 1; xShift++) {
			for(int zShift = -1; zShift <= 1; zShift++) {
				long chunkID = chunkID(loc.add(xShift, 0, zShift));
				ArrayList<Spawner> chunk = spawners.get(chunkID);
				if(chunk == null) continue;
				for(Spawner s : chunk) {
					if(closest == null) {
						closest = s;
						continue;
					}
					if(loc.distance(s.getLocation()) < loc.distance(closest.getLocation())) {
						closest = s;
					}
				}
			}
		}
		return closest;
	}
	
	public Spawner getSpawner(Location loc) {
		ArrayList<Spawner> spawners = this.spawners.get(chunkID(loc));
		if(spawners == null) return null;
		for(Spawner s : spawners) {
			if(s.getLocation().equals(loc)) {
				return s;
			}
		}
		return null;
	}
	
	public static long chunkID(Location loc) {
		int chunkX = loc.getChunk().getX();
		int chunkZ = loc.getChunk().getZ();
		return ((long)chunkX << 32) | (chunkZ & 0xffffffffL);
	}
	
	public void addSpawner(Spawner spawner) {
		long chunkID = chunkID(spawner.getLocation());
		if(spawners.get(chunkID) == null) {
			spawners.put(chunkID, new ArrayList<Spawner>());
		}
		spawners.get(chunkID).add(spawner);
	}
}
