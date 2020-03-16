package com.github.razz0991.construkt;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.razz0991.construkt.CktConfigOptions.Limiter;
import com.github.razz0991.construkt.filters.Filters;
import com.github.razz0991.construkt.protection.GriefPreventionProtection;
import com.github.razz0991.construkt.protection.PlotSquaredProtection;
import com.github.razz0991.construkt.protection.ProtectionPlugins;
import com.github.razz0991.construkt.protection.WorldGuardProtection;
import com.github.razz0991.construkt.shapes.Shapes;

/*  Construkt Bukkit plugin for Minecraft.
 *  Copyright (C) 2020 _Razz_
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
public class Construkt extends JavaPlugin {
	
	public static Construkt plugin;
	
	@Override
	public void onEnable() {
		plugin = this;
		getServer().getPluginManager().registerEvents(new CktEvents(), this);
		
		// Configuration stuff
		saveDefaultConfig();
		
		// Check for missing config keys and save them into the config file.
		boolean newKeys = false;
		for (String key : getConfig().getDefaults().getKeys(true)) {
			if (!getConfig().contains(key, true)) {
				getConfig().set(key, getConfig().getDefaults().get(key));
				getLogger().info("Added missing config option: " + key);
				newKeys = true;
			}
		}
		if (newKeys)
			saveConfig();
		
		// Apply config options to the plugin
		Set<String> limitNames = getConfig().getConfigurationSection("limits").getKeys(false);
		
		for (String limitName : limitNames) {
			int maxAxisLength = getConfig().getInt("limits." + limitName + ".maxAxisLength");
			int maxVolume = getConfig().getInt("limits." + limitName + ".maxVolume");;
			Limiter lmt = new Limiter(maxAxisLength, maxVolume);
			CktConfigOptions.addLimitation(limitName, lmt);
			getLogger().info("Added " + limitName + " limitation");
		}
		
		CktConfigOptions.setUndoRedoLimit(getConfig().getInt("undoRedoLimit"));
		
		for (String matName : getConfig().getStringList("blacklist")) {
			if (!CktConfigOptions.addBlacklistMaterial(matName)) {
				getLogger().info(matName + " is not a valid material name for blacklist."); 
			}
		}
		
		// Protection plugins
		PluginManager pManag = getServer().getPluginManager();
		
		if (pManag.getPlugin("WorldGuard") != null) {
			ProtectionPlugins.addProtectionPlugin(new WorldGuardProtection());
			getLogger().info("Enabled WorldGuard protection support");
		}
		
		if (pManag.getPlugin("PlotSquared") != null) {
			ProtectionPlugins.addProtectionPlugin(new PlotSquaredProtection());
			getLogger().info("Enabled PlotSquared protection support");
		}
		
		if (pManag.getPlugin("GriefPrevention") != null) {
			ProtectionPlugins.addProtectionPlugin(new GriefPreventionProtection());
			getLogger().info("Enabled GriefPrevention protection support");
		}
		
		// Final initialization
		
		for(Player ply : getServer().getOnlinePlayers()) {
			Players.addPlayer(ply);
		}
		
		getLogger().info("Construkt started.");
	}
	
	@Override
	public void onDisable() {
		for (UUID id : Players.getAllPlayers()) {
			Players.removePlayer(id);
		}
		
		getLogger().info("Construkt disabled.");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("construkt")) {
			if (sender instanceof Player) {
				Player player = (Player)sender;
				PlayerInfo plyInfo = Players.getPlayerInfo(player);
				
				// Stop here if player doesn't have permission
				if (!player.hasPermission("construkt.command")) {
					CktUtil.messagePlayer(player, ChatColor.RED + "You do not have permission to use Construkt!");
					return true;
				}
				
				if (args.length == 0) {
					plyInfo.toggleConstruktEnabled();
					return true;
				}
				else {
					// Set Parameter
					if (args[0].equalsIgnoreCase("parameter") &&
							args.length >= 2) {
						if (args.length == 3) {
							plyInfo.setParameter(args[1], args[2], false);
							return true;
						}
						else if (args.length == 2) {
							// Get a parameters details.
							plyInfo.getParameterInfo(args[1], false);
							return true;
						}
						CktUtil.messagePlayer(player, "Unknown parameter command!");
					}
					else if (args[0].equalsIgnoreCase("filter") && 
							args.length >= 2) {
						//Set Filter
						if (args[1].equalsIgnoreCase("add") && 
								args.length >= 3) {
							plyInfo.addFilter(args[2]);
							return true;
						}
						else if (args[1].equalsIgnoreCase("remove") &&
								args.length >= 3) {
							plyInfo.removeFilter(args[2]);
							return true;
						}
						else if (args[1].equalsIgnoreCase("clear")) {
							plyInfo.clearFilters();
							return true;
						}
						else if (args[1].equalsIgnoreCase("parameter") &&
								args.length >= 3) {
							if (args.length >= 4) {
								plyInfo.setParameter(args[2], args[3], true);
								return true;
							}
							else if (args.length == 3) {
								plyInfo.getParameterInfo(args[2], true);
								return true;
							}
						}
						CktUtil.messagePlayer(player, "Unknown filter command!");
						return true;
					}
					// Otherwise set shape
					String shape = args[0];
					plyInfo.setShape(shape);
					return true;
				}
			}
		}
		else if(cmd.getName().equalsIgnoreCase("construktundo") || cmd.getName().equalsIgnoreCase("construktredo")) {
			if (sender instanceof Player) {
				Player ply = (Player)sender;
				PlayerInfo plyInfo = Players.getPlayerInfo(ply);
				
				if (cmd.getName().equalsIgnoreCase("construktundo"))
					plyInfo.undo();
				else
					plyInfo.redo();
				return true;
			}
		}
		return false;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
		if (cmd.getName().equalsIgnoreCase("construkt")) {
			if (sender instanceof Player) {
				Player player = (Player)sender;
				PlayerInfo plyInfo = Players.getPlayerInfo(player);
				if (args.length == 1) {
					List<String> output = new ArrayList<String>();
					if ("parameter".startsWith(args[0]) && 
							player.hasPermission("construkt.command")) {
						output.add("parameter");
					}
					if ("filter".startsWith(args[0]) &&
							player.hasPermission("construkt.command")) {
						output.add("filter");
					}
					
					for (String shape : Shapes.getAllShapes()) {
						if (shape.startsWith(args[0]) && 
								player.hasPermission("construkt.shape." + shape))
							output.add(shape);
					}
					return output;
				}
				else if(args.length == 2 && args[0].equalsIgnoreCase("parameter")) {
					// List of parameters
					List<String> output = new ArrayList<String>();
					
					for (String par : plyInfo.getAllShapeParameterKeys()) {
						if (par.startsWith(args[1]))
							output.add(par);
					}
					
					return output;
				}
				else if (args.length >= 2 && args[0].equalsIgnoreCase("filter")) {
					List<String> output = new ArrayList<String>();
					
					if (args.length == 2) {
						if ("add".startsWith(args[1]))
							output.add("add");
						if ("remove".startsWith(args[1]))
							output.add("remove");
						if ("clear".startsWith(args[1]))
							output.add("clear");
						if ("parameter".startsWith(args[1]))
							output.add("parameter");
					}
					if (args.length == 3) {
						if (args[1].equalsIgnoreCase("add")) {
							for (String filter : Filters.getAllFilters()) {
								if (filter.startsWith(args[2]))
									output.add(filter);
							}
						}
						else if(args[1].equalsIgnoreCase("remove")) {
							for (String filter : plyInfo.getFilterNames()) {
								if (filter.startsWith(args[2]))
									output.add(filter);
							}
						}
						else if (args[1].equalsIgnoreCase("parameter")) {
							for (String parameter : plyInfo.getAllFilterParameterKeys()) {
								if (parameter.startsWith(args[2]))
									output.add(parameter);
							}
						}
					}
					
					return output;
				}
				else if(args.length >= 2) {
					return new ArrayList<String>();
				}
			}
		}
		return null;
	}
}










