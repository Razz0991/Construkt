package com.github.razz0991.construkt;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
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
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("construkt")) {
			if (sender instanceof Player) {
				Player ply = (Player)sender;
				Players.getPlayerInfo(ply).toggleConstruktEnabled();
				return true;
			}
		}
		return false;
	}
}
