package com.github.soerxpso.xpspawners.listeners;

import java.util.logging.Level;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.inventory.ItemStack;

import com.github.soerxpso.xpspawners.Spawner;
import com.github.soerxpso.xpspawners.XPSpawners;
import com.github.soerxpso.xpspawners.manager.ConfigManager;
import com.github.soerxpso.xpspawners.manager.SpawnerManager;

/**
 * When a chunk loads, attempt to pre-load all spawners.
 *
 * @author ProgrammerDan
 */
public class ChunkListener implements Listener {

	SpawnerManager spawnerManager;
	
	public ChunkListener() {
		spawnerManager = XPSpawners.getPlugin().getSpawnerManager();
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void chunkLoad(ChunkLoadEvent event) {
		Chunk chunk = event.getChunk();
		if (chunk == null) return;

		BlockState[] allTile = chunk.getTileEntities();
		if (allTile == null || allTile.length == 0) return;

		for (BlockState tile : allTile) {
			if (tile instanceof CreatureSpawner) {
				spawnerManager.safeAddSpawner((CreatureSpawner) tile);
			}
		}
	}

}

