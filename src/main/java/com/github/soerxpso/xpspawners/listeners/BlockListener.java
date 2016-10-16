package com.github.soerxpso.xpspawners.listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.github.soerxpso.xpspawners.Spawner;
import com.github.soerxpso.xpspawners.XPSpawners;
import com.github.soerxpso.xpspawners.manager.ConfigManager;
import com.github.soerxpso.xpspawners.manager.SpawnerManager;

public class BlockListener implements Listener {
	
	SpawnerManager spawnerManager;
	
	public BlockListener() {
		spawnerManager = XPSpawners.getPlugin().getSpawnerManager();
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onBlockBreak(BlockBreakEvent e) {
		Block b = e.getBlock();
		//the item the block was broken with
		ItemStack is = e.getPlayer().getInventory().getItemInMainHand();
		
		boolean brokenWithSilk = is.getEnchantments().containsKey(Enchantment.SILK_TOUCH);
		boolean isXpSpawner = b.getType().equals(Material.MOB_SPAWNER);
		
		if(isXpSpawner) {
			removeSpawner(b);
			e.setCancelled(true);
			e.getBlock().setType(Material.AIR);
			if(brokenWithSilk) {
				if(Math.random() < ConfigManager.getSilkDropChance()) {
					dropSpawnerLater(b);
				}
			}
		}
	}
	
	@EventHandler
	public void onBlockExplode(BlockExplodeEvent e) {
		
	}
	
	private void removeSpawner(Block b) {
		Spawner spawner = spawnerManager.getSpawner(b.getLocation());
		if(spawner == null) {
			spawnerManager.removeSpawner(spawner);
		}
	}
	
	private void dropSpawnerLater(Block b) {
		ItemStack itemToDrop = XPSpawners.getPlugin()
				.getSpawnerManager().convertSpawnerToItem(b);
		new BukkitRunnable() {
			@Override
			public void run() {
				b.getWorld().dropItem(b.getLocation().add(0.5, 0.5, 0.5), itemToDrop);
			}
		}.runTaskLater(XPSpawners.getPlugin(), 1);
	}
}
