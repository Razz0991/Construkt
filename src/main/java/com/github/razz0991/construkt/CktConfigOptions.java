package com.github.razz0991.construkt;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/*  Construkt Bukkit plugin for Minecraft.
 *  Copyright (C) 2020 _Razz_
 *
 *  Full disclaimer in Construkt.java
 */
class CktConfigOptions {
	
	static private Map<String, Limiter> limitations = new HashMap<String, Limiter>();
	static private int undoRedoLimit = 10;
	
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
	
	public static int getUndoRedoLimit() {
		return undoRedoLimit;
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
