package com.github.soerxpso.xpspawners.listeners;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

import org.bukkit.Chunk;
import org.bukkit.block.BlockState;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;

import com.github.soerxpso.xpspawners.XPSpawners;
import com.github.soerxpso.xpspawners.manager.SpawnerManager;

/**
 * When a chunk loads, attempt to pre-load all spawners.
 *
 * @author ProgrammerDan
 */
public class ChunkListener implements Listener {
	HashMap<UUID, HashSet<Long>> loaded;

	SpawnerManager spawnerManager;
	
	public ChunkListener() {
		spawnerManager = XPSpawners.getPlugin().getSpawnerManager();
		loaded = new HashMap<UUID, HashSet<Long>>();
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void chunkLoad(ChunkLoadEvent event) {
		Chunk chunk = event.getChunk();
		if (chunk == null) return;

		UUID world = chunk.getWorld().getUID();
		long chunkId = ((long) chunk.getX() << 32L) + (long) chunk.getZ();

		HashSet<Long> load = loaded.get(world);
		if (load == null) {
			load = new HashSet<Long>();
			loaded.put(world, load);
		} else if (load.contains(chunkId)) {
			return;
		}
		load.add(chunkId);

		BlockState[] allTile = chunk.getTileEntities();
		if (allTile == null || allTile.length == 0) return;

		for (BlockState tile : allTile) {
			if (tile instanceof CreatureSpawner) {
				spawnerManager.safeAddSpawner((CreatureSpawner) tile);
			}
		}
	}
}

