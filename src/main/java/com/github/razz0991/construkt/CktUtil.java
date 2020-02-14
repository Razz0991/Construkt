package com.github.razz0991.construkt;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/*  Construkt Bukkit plugin for Minecraft.
 *  Copyright (C) 2020 _Razz_
 *
 *  Full disclaimer in Construkt.java
 */
public class CktUtil {
	
	// Single line Message
	static void messagePlayer(Player player, String message) {
		player.sendMessage(ChatColor.BLUE + "[Construkt] " + ChatColor.RESET + message);
	}
	
	// Multiline message
	static void messagePlayer(Player player, String[] messages) {
		String[] outMessages = new String[2 + messages.length];
		
		outMessages[0] = ChatColor.BLUE + "---------- [Construkt] ----------";
		int inc = 1;
		for (String msg : messages) {
			outMessages[inc] = msg;
			inc++;
		}
		outMessages[outMessages.length - 1] = ChatColor.BLUE + "-------------------------------";
		
		player.sendMessage(outMessages);
	}
	
	/**
	 * Helper method to sort two numbers from smallest to largest.
	 * @param first
	 * @param second
	 * @return An <code>int[]</code> containing the smallest, then largest, number.
	 */
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
	
	/**
	 * Helper method to get a smallest to largest location 
	 * area from a set of two locations.
	 * @param location1
	 * @param location2
	 * @return A <code>Location[]</code> where the first 
	 * location will be smaller than the second.
	 */
	public static Location[] areaRange(Location location1, Location location2){

		int[] x, y, z;
		x = smallestNumber(location1.getBlockX(), location2.getBlockX());
		y = smallestNumber(location1.getBlockY(), location2.getBlockY());
		z = smallestNumber(location1.getBlockZ(), location2.getBlockZ());
		
		return new Location[] {new Location(location1.getWorld(), x[0], y[0], z[0]),
				new Location(location1.getWorld(), x[1], y[1], z[1])};
	}
	
	/**
	 * Turns a location into an array.
	 * @param loc The <code>Location</code>
	 * @param order The order in which you want the locations values<br>
	 * eg:<br><code>{'x', 'y', 'z'}</code> will give all values in that order<br>
	 * <code>{'x', 'z'}</code> will ignore <code>'y'</code>
	 * @return An <code>int[]</code> of sorted values.
	 * @throws IllegalArgumentException if there are more than 3 axes or an 
	 * unexpected character is entered
	 */
	public static int[] locationToArray(Location loc, char[] order) {
		if (order.length > 3)
			throw new IllegalArgumentException("Only 3 axes can be assigned in order");
		
		int[] output = new int[order.length];
		for (int i = 0; i < order.length; i++) {
			if (order[i] == 'x')
				output[i] = loc.getBlockX();
			else if (order[i] == 'y')
				output[i] = loc.getBlockY();
			else if (order[i] == 'z')
				output[i] = loc.getBlockZ();
			else
				throw new IllegalArgumentException("Invalid axis assigned in order array! Expected x, y or z, got " + order[i]);
		}
		return output;
	}
	
	/**
	 * Updates an existing location using an array of coordinates and an order.<br>
	 * If not all axes are in the order variable, the Location will keep whatever axis
	 * that is missing.
	 * @param coords The coordinates
	 * @param order The order of the coordinates
	 * @param toUpdate The <code>Location</code> to update
	 * @throws IllegalArgumentException if coords and order don't have the same length
	 */
	public static void updateCoordinates(int[] coords, char[] order, Location toUpdate) {
		if (coords.length != order.length)
			throw new IllegalArgumentException("The length of coords and order must be the same.");
		
		for (int i = 0; i < order.length; i++) {
			if (order[i] == 'x')
				toUpdate.setX(coords[i]);
			else if (order[i] == 'y')
				toUpdate.setY(coords[i]);
			else if (order[i] == 'z')
				toUpdate.setZ(coords[i]);
		}
	}
	
	/**
	 * A boolean check to see if a string is an Integer
	 * @param value The String of a possible integer
	 * @return <code>true</code> if the value is an integer
	 */
	public static boolean isInteger(String value) {
		try {
			Integer.parseInt(value);
			return true;
		}
		catch (NumberFormatException e) {
			return false;
		}
	}
	
	/**
	 * Gets information about a volume.
	 * @param first the first location
	 * @param second the second location
	 * @return <code>VolumeInformation</code> containing x, y, and z lengths plus volume
	 */
	public static VolumeInformation getVolumeInformation(Location first, Location second) {
		return new VolumeInformation(first, second);
	}
	
	protected static class VolumeInformation{
		private final int xLength;
		private final int yLength;
		private final int zLength;
		private final int volume;
		
		protected VolumeInformation(final Location first, final Location second) {
			Location[] area = areaRange(first, second);
			xLength = area[1].getBlockX() - area[0].getBlockX() + 1;
			yLength = area[1].getBlockY() - area[0].getBlockY() + 1;
			zLength = area[1].getBlockZ() - area[0].getBlockZ() + 1;
			volume = xLength * yLength * zLength;
		}
		
		public int getXLength() {
			return xLength;
		}
		
		public int getYLength() {
			return yLength;
		}
		
		public int getZLength() {
			return zLength;
		}
		
		public int getVolume() {
			return volume;
		}
		
		public boolean lengthExceedsMax(int maxLength) {
			return (xLength > maxLength || yLength > maxLength || zLength > maxLength);
		}
	}

}









