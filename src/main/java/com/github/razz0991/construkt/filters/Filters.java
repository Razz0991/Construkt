package com.github.razz0991.construkt.filters;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/*  Construkt Bukkit plugin for Minecraft.
 *  Copyright (C) 2020 _Razz_
 *
 *  Full disclaimer in Construkt.java
 */
public class Filters {
	private static Map<String, Class<? extends BaseFilter>> filters = new HashMap<String, Class<? extends BaseFilter>>();
	
	static {
		addFilter(CheckeredFilter.class);
		addFilter(SliceFilter.class);
		addFilter(RandomFilter.class);
		addFilter(NoiseFilter.class);
	}
	
	private static void addFilter(Class<? extends BaseFilter> filter) {
		try {
			filters.put(filter.newInstance().getName(), filter);
		} catch (InstantiationException | IllegalAccessException | 
				IllegalArgumentException | SecurityException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets a filter by its name.
	 * @param name The name of the shape
	 * @return The Shape or null if none is found.
	 */
	public static BaseFilter getFilter(String name) {
		if (filters.containsKey(name.toLowerCase()))
			try {
				return filters.get(name.toLowerCase()).newInstance();
			} catch (InstantiationException | IllegalAccessException | 
					IllegalArgumentException | SecurityException e) {
				e.printStackTrace();
			}
		return null;
	}
	
	/**
	 * Gets the names of all the registered filters.
	 * @return A <code>Set</code> containing the names of all the filters
	 */
	public static Set<String> getAllFilters(){
		return new HashSet<String>(filters.keySet());
	}
	
	/**
	 * Checks if a filter is currently registered.
	 * @param filterName The name of the shape
	 * @return true if the shape is registered
	 */
	public static boolean hasFilter(String filterName) {
		return filters.containsKey(filterName.toLowerCase());
	}
}
