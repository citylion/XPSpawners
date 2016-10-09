package com.github.soerxpso.xpspawners.manager;

import org.bukkit.configuration.file.FileConfiguration;

import com.github.soerxpso.xpspawners.XPSpawners;

public class ConfigManager {

	private static FileConfiguration config;
	
	public static void loadConfig() {
		config = XPSpawners.getPlugin().getConfig();
	}
	
	public static int getHarvestInterval() {
		return config.getInt("harvest-interval", 200);
	}
	
	public static int getBaseXpPerDay() {
		return config.getInt("xp-per-day", 20000);
	}
}
