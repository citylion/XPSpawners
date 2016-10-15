package com.github.soerxpso.xpspawners.manager;

import org.bukkit.configuration.file.FileConfiguration;

import com.github.soerxpso.xpspawners.XPSpawners;

public class ConfigManager {

	private static FileConfiguration config;
	
	public static void loadConfig() {
		XPSpawners.getPlugin().saveDefaultConfig();
		XPSpawners.getPlugin().reloadConfig();
		config = XPSpawners.getPlugin().getConfig();
	}
	
	public static int getHarvestInterval() {
		return config.getInt("harvest-interval", 200);
	}
	
	public static int getBaseXpPerHour() {
		return config.getInt("xp-per-hour", 20000);
	}
}
