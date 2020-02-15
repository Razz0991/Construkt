package com.github.razz0991.construkt.shapes.filters;

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
	private static Map<String, BaseFilter> filters = new HashMap<String, BaseFilter>();
	
	static {
		addFilter(new CheckeredFilter());
		addFilter(new SliceFilter());
		addFilter(new RandomFilter());
	}
	
	private static void addFilter(BaseFilter filter) {
		filters.put(filter.getFilterName(), filter);
	}
	
	/**
	 * Gets a filter by its name.
	 * @param name The name of the shape
	 * @return The Shape or null if none is found.
	 */
	public static BaseFilter getFilter(String name) {
		if (filters.containsKey(name.toLowerCase()))
			return filters.get(name.toLowerCase());
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
