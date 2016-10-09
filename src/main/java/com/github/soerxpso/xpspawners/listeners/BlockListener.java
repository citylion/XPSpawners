package com.github.soerxpso.xpspawners.listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import com.github.soerxpso.xpspawners.XPSpawners;

public class BlockListener implements Listener {

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onBlockBreak(BlockBreakEvent e) {
		Block b = e.getBlock();
		//the item the block was broken with
		ItemStack is = e.getPlayer().getInventory().getItemInMainHand();
		
		boolean brokenWithSilk = is.getEnchantments().containsKey(Enchantment.SILK_TOUCH);
		boolean isXpSpawner = b.getType().equals(Material.MOB_SPAWNER);
		if(brokenWithSilk && isXpSpawner) {
			e.setCancelled(true);
			e.getBlock().setType(Material.AIR);
			ItemStack itemToDrop = XPSpawners.getPlugin()
					.getSpawnerManager().convertSpawnerToItem(b);
			b.getWorld().dropItem(b.getLocation().add(0.5, 0.5, 0.5), itemToDrop);
		}
	}
}
