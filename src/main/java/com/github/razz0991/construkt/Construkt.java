package com.github.razz0991.construkt;

import org.bukkit.plugin.java.JavaPlugin;

public class Construkt extends JavaPlugin {
	@Override
	public void onEnable() {
		getLogger().info("Construkt starting...");
		getServer().getPluginManager().registerEvents(new CktEvents(), this);
		getLogger().info("Construkt started.");
	}
	
	@Override
	public void onDisable() {
		getLogger().info("Construkt disabling...");
	}
}
