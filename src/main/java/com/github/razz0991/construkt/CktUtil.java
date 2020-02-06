package com.github.razz0991.construkt;

/*  Construkt Bukkit plugin for Minecraft.
 *  Copyright (C) 2020 _Razz_
 *
 *  Full disclaimer in Construkt.java
 */
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class CktUtil {
	
	public static void messagePlayer(Player player, String message) {
		player.sendMessage(ChatColor.AQUA + "[Construkt] " + ChatColor.RESET + message);
	}
	
	public static int[] smallestNumber(int first, int second) {
		int[] output = new int[2];
		if (first > second) {
			output[0] = second;
			output[1] = first;
			return output;
		}
		else {
			output[0] = first;
			output[1] = second;
			return output;
		}
	}
	
	public static Location[] areaRange(Location location1, Location location2){

		int[] x, y, z;
		x = smallestNumber(location1.getBlockX(), location2.getBlockX());
		y = smallestNumber(location1.getBlockY(), location2.getBlockY());
		z = smallestNumber(location1.getBlockZ(), location2.getBlockZ());
		
		return new Location[] {new Location(location1.getWorld(), x[0], y[0], z[0]),
				new Location(location1.getWorld(), x[1], y[1], z[1])};
	}
	
	public static int[] locationToArray(Location loc, char[] order) {
		int[] output = new int[order.length];
		for (int i = 0; i < order.length; i++) {
			if (order[i] == 'x')
				output[i] = loc.getBlockX();
			else if (order[i] == 'y')
				output[i] = loc.getBlockY();
			else if (order[i] == 'z')
				output[i] = loc.getBlockZ();
		}
		return output;
	}
	
	public static void updateCoordinates(int[] coords, char[] order, Location toUpdate) {
		for (int i = 0; i < order.length; i++) {
			if (order[i] == 'x')
				toUpdate.setX(coords[i]);
			else if (order[i] == 'y')
				toUpdate.setY(coords[i]);
			else if (order[i] == 'z')
				toUpdate.setZ(coords[i]);
		}
	}

}
