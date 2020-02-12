package com.github.razz0991.construkt.shapes;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/*  Construkt Bukkit plugin for Minecraft.
 *  Copyright (C) 2020 _Razz_
 *
 *  Full disclaimer in Construkt.java
 */
public class Shapes {
	private static Map<String, BaseShape> shapes = new HashMap<String, BaseShape>();
	
	static {
		shapes.put("cuboid", new CuboidShape());
		shapes.put("sphere", new SphereShape());
		shapes.put("hollow_cuboid", new HollowCuboidShape());
		shapes.put("terrain", new TerrainShape());
		shapes.put("overlay", new Overlay());
	}
	
	/**
	 * Gets a shape by its name.
	 * @param name The name of the shape
	 * @return The Shape or null if none is found.
	 */
	public static BaseShape getShape(String name) {
		if (shapes.containsKey(name.toLowerCase()))
			return shapes.get(name.toLowerCase());
		return null;
	}
	
	/**
	 * Gets the names of all the registered shapes.
	 * @return A <code>Set</code> containing the names of all the shapes
	 */
	public static Set<String> getAllShapes(){
		return new HashSet<String>(shapes.keySet());
	}
	
	/**
	 * Checks if a shape is currently registered.
	 * @param shapeName The name of the shape
	 * @return true if the shape is registered
	 */
	public static boolean hasShape(String shapeName) {
		return shapes.containsKey(shapeName.toLowerCase());
	}
}
