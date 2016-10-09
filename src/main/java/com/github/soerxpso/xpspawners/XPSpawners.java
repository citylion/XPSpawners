package com.github.soerxpso.xpspawners;

import java.util.logging.Level;

import org.bukkit.plugin.java.JavaPlugin;

import com.github.soerxpso.xpspawners.listeners.BlockListener;
import com.github.soerxpso.xpspawners.listeners.MobSpawnListener;
import com.github.soerxpso.xpspawners.manager.ConfigManager;
import com.github.soerxpso.xpspawners.manager.SpawnerManager;

public class XPSpawners extends JavaPlugin {

	private static XPSpawners plugin;
	private SpawnerManager spawnerManager;
	
	public void onEnable() {
		plugin = this;
		spawnerManager = new SpawnerManager();
		ConfigManager.loadConfig();
		getServer().getPluginManager().registerEvents(new BlockListener(), this);
		getServer().getPluginManager().registerEvents(new MobSpawnListener(), this);
	}
	
	public static XPSpawners getPlugin() {
		return plugin;
	}
	
	public SpawnerManager getSpawnerManager() {
		return spawnerManager;
	}
	
	public static void log(String message) {
		plugin.getServer().getLogger().log(Level.INFO, "[XPSpawners] " + message);
	}
}
