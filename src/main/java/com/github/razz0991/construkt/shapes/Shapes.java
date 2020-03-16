package com.github.razz0991.construkt.shapes;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.github.razz0991.construkt.PlayerInfo;

/*  Construkt Bukkit plugin for Minecraft.
 *  Copyright (C) 2020 _Razz_
 *
 *  Full disclaimer in Construkt.java
 */
public class Shapes {
	private static Map<String, Class<? extends BaseShape>> shapes = new HashMap<String, Class<? extends BaseShape>>();
	
	static {
		addShape(CuboidShape.class);
		addShape(SphereShape.class);
		addShape(HollowCuboidShape.class);
		addShape(TerrainShape.class);
		addShape(OverlayShape.class);
		addShape(CylinderShape.class);
		addShape(LineShape.class);
	}
	
	private static void addShape(Class<? extends BaseShape> shape) {
		try {
			shapes.put(shape.newInstance().getName(), shape);
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets a shape by its name.
	 * @param name The name of the shape
	 * @return The Shape or null if none is found.
	 */
	public static BaseShape getShape(PlayerInfo ply, String name) {
		if (shapes.containsKey(name.toLowerCase()))
			try {
				return shapes.get(name.toLowerCase()).getDeclaredConstructor(PlayerInfo.class).newInstance(ply);
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | 
					InvocationTargetException | NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}
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
