package com.github.razz0991.construkt.shapes;

import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;

import com.github.razz0991.construkt.shapes.filters.BaseFilter;
import com.github.razz0991.construkt.shapes.parameters.BooleanShapeParameter;
import com.github.razz0991.construkt.shapes.parameters.IntegerShapeParameter;
import com.github.razz0991.construkt.shapes.parameters.ShapeParameter;

/*  Construkt Bukkit plugin for Minecraft.
 *  Copyright (C) 2020 _Razz_
 *
 *  Full disclaimer in Construkt.java
 */
public abstract class BaseShape {
	
	/**
	 * Gets the parameters that are available for a shape.
	 * @return A Map containing all valid parameters or null if none are set
	 */
	public abstract Map<String, ShapeParameter<?>> getDefaultParameters();
	/**
	 * Generates a shape and places blocks via the block data entered.
	 * @param firstPoint The first location of the area
	 * @param secondPoint The second location of the area
	 * @param parameters parameters parsed for the shape to use
	 * @param blockData The data of what block to place, null if the block will be air
	 * @param filter The filter that will be used
	 * @return true for success
	 */
	public abstract boolean generateShape(Location firstPoint, Location secondPoint, Map<String, ShapeParameter<?>> parameters, 
			BlockData blockData, BaseFilter[] filters);
	
	protected void setBlock(BlockData blockData, Location loc) {
		if (blockData != null)
			loc.getBlock().setBlockData(blockData.clone());
		else
			loc.getBlock().setType(Material.AIR);
	}
	
	protected boolean canPlace(AreaData data, Map<String, ShapeParameter<?>> parameters, BaseFilter[] filters) {
		return canPlace(null, parameters, filters, data);
	}
	
	protected boolean canPlace(Location current, Map<String, ShapeParameter<?>> parameters, BaseFilter[] filters, AreaData data) {
		Location cur = current;
		if (current == null)
			cur = data.getCurrentLocation();
		
		boolean placeInAir = true;
		if (parameters.containsKey("place_in_air")) {
			placeInAir = parseBooleanShapeParameter(parameters.get("place_in_air"), placeInAir);
		}
		
		if ((placeInAir && cur.getBlock().getType() == Material.AIR) ||
				(!placeInAir && cur.getBlock().getType() != Material.AIR)) {
			if(filters.length == 0)
				return true;
			for (BaseFilter filter : filters) {
				if (!filter.checkCondition(data, parameters))
					return false;
			}
			return true;
		}
		return false;
	}
	
	protected boolean parseBooleanShapeParameter(ShapeParameter<?> parameter, boolean defaultValue) {
		if (parameter instanceof BooleanShapeParameter) {
			return ((BooleanShapeParameter)parameter).getParameter();
		}
		return defaultValue;
	}
	
	protected int parseIntegerShapeParameter(ShapeParameter<?> parameter, int defaultValue) {
		if (parameter instanceof IntegerShapeParameter) {
			return ((IntegerShapeParameter)parameter).getParameter();
		}
		return defaultValue;
	}
}














