package com.github.razz0991.construkt;
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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.razz0991.construkt.shapes.Shapes;

public class Construkt extends JavaPlugin {
	
	@Override
	public void onEnable() {
		getLogger().info("Construkt starting...");
		getServer().getPluginManager().registerEvents(new CktEvents(), this);
		getLogger().info("Construkt started.");
		
		for(Player ply : getServer().getOnlinePlayers()) {
			Players.addPlayer(ply);
		}
		
	}
	
	@Override
	public void onDisable() {
		getLogger().info("Construkt disabling...");
		
		for (UUID id : Players.getAllPlayers()) {
			Players.removePlayer(id);
		}
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("construkt")) {
			if (sender instanceof Player) {
				Player player = (Player)sender;
				PlayerInfo plyInfo = Players.getPlayerInfo(player);
				if (args.length == 0) {
					plyInfo.toggleConstruktEnabled();
					return true;
				}
				else {
					// Set Parameter
					if (args[0].equalsIgnoreCase("parameter") &&
							args.length >= 2) {
						if (args.length == 3) {
							plyInfo.setParameter(args[1], args[2]);
							return true;
						}
						else if (args.length == 2) {
							plyInfo.getParameterInfo(args[1]);
							return true;
						}
					}
					// Otherwise set shape
					String shape = args[0];
					plyInfo.setShape(shape);
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
		if (cmd.getName().equalsIgnoreCase("construkt")) {
			if (sender instanceof Player) {
				PlayerInfo plyInfo = Players.getPlayerInfo((Player)sender);
				if (args.length == 1) {
					List<String> output = new ArrayList<String>();
					if ("parameter".startsWith(args[0]))
						output.add("parameter");
					
					for (String shape : Shapes.getAllShapes()) {
						if (shape.startsWith(args[0]))
							output.add(shape);
					}
					return output;
				}
				else if(args.length == 2 && args[0].equalsIgnoreCase("parameter")) {
					List<String> output = new ArrayList<String>();
					
					for (String par : plyInfo.getAllParameterKeys()) {
						if (par.startsWith(args[1]))
							output.add(par);
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










