package com.github.razz0991.construkt.shapes;
/*  Construkt Bukkit plugin for Minecraft.
 *  Copyright (C) 2020 _Razz_
 *
 *  Full disclaimer in Construkt.java
 */
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Shapes {
	private static Map<String, BaseShape> shapes = new HashMap<String, BaseShape>();
	
	static {
		shapes.put("cuboid", new CuboidShape());
		shapes.put("sphere", new SphereShape());
		shapes.put("hollow_cuboid", new HollowCuboidShape());
		shapes.put("terrain", new TerrainShape());
		shapes.put("overlay", new Overlay());
	}
	
	public static BaseShape getShape(String name) {
		if (shapes.containsKey(name.toLowerCase()))
			return shapes.get(name.toLowerCase());
		return null;
	}
	
	public static Set<String> getAllShapes(){
		return new HashSet<String>(shapes.keySet());
	}
	
	public static boolean hasShape(String shapeName) {
		return shapes.containsKey(shapeName.toLowerCase());
	}
}
