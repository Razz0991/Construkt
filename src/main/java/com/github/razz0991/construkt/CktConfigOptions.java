package com.github.razz0991.construkt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Material;

/*  Construkt Bukkit plugin for Minecraft.
 *  Copyright (C) 2020 _Razz_
 *
 *  Full disclaimer in Construkt.java
 */
class CktConfigOptions {
	
	private static Map<String, Limiter> limitations = new HashMap<String, Limiter>();
	private static int undoRedoLimit = 10;
	private static List<Material> blacklistMaterials = new ArrayList<Material>();
	
	static Limiter getLimitation(String name) {
		if (limitations.containsKey(name.toLowerCase()))
			return limitations.get(name);
		return null;
	}
	
	static void addLimitation(String name, Limiter limiter) {
		limitations.put(name.toLowerCase(), limiter);
	}
	
	static Set<String> getAllLimitationNames() {
		return new HashSet<String>(limitations.keySet());
	}
	
	static boolean hasLimitation(String name) { 
		return limitations.containsKey(name.toLowerCase());
	}
	
	static void setUndoRedoLimit(int limit) {
		undoRedoLimit = limit;
	}
	
	/**
	 * Gets the undo and redo limit for players
	 * @return The limit defined in the config
	 */
	public static int getUndoRedoLimit() {
		return undoRedoLimit;
	}
	
	static boolean addBlacklistMaterial(String name) {
		String upperName = name.toUpperCase();
		if (!name.contains("?")) {
			// Match exact name
			Material mat = Material.getMaterial(upperName);
			if (mat != null && !blacklistMaterials.contains(mat)) {
				blacklistMaterials.add(mat);
				return true;
			}
		}
		else {
			// Convert to regex
			String format = upperName;
			if (format.startsWith("?")) {
				format = format.replace("?", ".+(");
			}
			else {
				format = "(" + format;
			}
			format = format.replace("?", ").+(");
			if (format.endsWith("("))
				format = format.substring(0, format.length() - 1);
			else {
				format += ")";
			}
			
			// Match all materials with regex
			boolean itemAdded = false;
			for (Material mat : Material.values()) {
				if (mat.toString().matches(format)) {
					if (mat != null && !blacklistMaterials.contains(mat)) {
						blacklistMaterials.add(mat);
						itemAdded = true;
					}
				}
			}
			return itemAdded;
		}
		return false;
	}
	
	/**
	 * Checks to see if this material is blacklisted
	 * @param mat The material to check
	 * @return true if the material has been blacklisted
	 */
	public static boolean isMaterialBlacklisted(Material mat) {
		return blacklistMaterials.contains(mat);
	}
	
	static class Limiter {
		
		private int maxAxisLength;
		private int maxVolume;
		
		Limiter(int maxAxisLength, int maxVolume) {
			this.maxAxisLength = maxAxisLength;
			this.maxVolume = maxVolume;
		}
		
		int getMaxAxisLength() {
			return maxAxisLength;
		}
		
		int getMaxVolume() {
			return maxVolume;
		}
	}
}
