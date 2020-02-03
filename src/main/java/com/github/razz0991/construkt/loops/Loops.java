package com.github.razz0991.construkt.loops;
/*  Construkt Bukkit plugin for Minecraft.
 *  Copyright (C) 2020 _Razz_
 *
 *  Full disclaimer in Construkt.java
 */
import java.util.HashMap;
import java.util.Map;

public class Loops {
	private static Map<String, BaseLoop> loops = new HashMap<String, BaseLoop>();
	
	static {
		loops.put("fill", new FillLoop());
	}
	
	public static BaseLoop getLoop(String name) {
		if (loops.containsKey(name.toLowerCase())) {
			return loops.get(name.toLowerCase());
		}
		return null;
	}
}
