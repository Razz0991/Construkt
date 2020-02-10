package com.github.razz0991.construkt.shapes;
import java.util.Map;

/*  Construkt Bukkit plugin for Minecraft.
 *  Copyright (C) 2020 _Razz_
 *
 *  Full disclaimer in Construkt.java
 */
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;

import com.github.razz0991.construkt.CktUtil;
import com.github.razz0991.construkt.shapes.parameters.BooleanShapeParameter;
import com.github.razz0991.construkt.shapes.parameters.IntegerShapeParameter;
import com.github.razz0991.construkt.shapes.parameters.ShapeParameter;

public abstract class BaseShape {
	
	public abstract Map<String, ShapeParameter<?>> getDefaultParameters();
	public abstract boolean generateShape(Location firstPoint, Location secondPoint, Map<String, ShapeParameter<?>> parameters, BlockData blockData);
	
	public void setBlock(BlockData blockData, Location loc) {
		if (blockData != null)
			loc.getBlock().setBlockData(blockData.clone());
		else
			loc.getBlock().setType(Material.AIR);
	}
	
	public boolean canPlace(Location current, Map<String, ShapeParameter<?>> parameters) {
		boolean placeInAir = true;
		if (parameters.containsKey("place_in_air")) {
			placeInAir = parseBooleanShapeParameter(parameters.get("place_in_air"), placeInAir);
		}
		if ((placeInAir && current.getBlock().getType() == Material.AIR) ||
				(!placeInAir && current.getBlock().getType() != Material.AIR)) {
			return true;
		}
		return false;
	}
	
	public boolean parseBooleanShapeParameter(ShapeParameter<?> parameter, boolean defaultValue) {
		if (parameter instanceof BooleanShapeParameter) {
			return ((BooleanShapeParameter)parameter).getParameter();
		}
		return defaultValue;
	}
	
	public int parseIntegerShapeParameter(ShapeParameter<?> parameter, int defaultValue) {
		if (parameter instanceof IntegerShapeParameter) {
			return ((IntegerShapeParameter)parameter).getParameter();
		}
		return defaultValue;
	}
	
	class AreaData {
		private final Location firstPoint;
		private final Location secondPoint;
		private final Location fromLocation;
		private final Location toLocation;
		private Location currentLocation;
		private char[] order;
		private int[] from;
		private int[] to;
		private int[] increment;
		private int[] current;
		
		AreaData(Location firstPoint, Location secondPoint, char[] order, int[] increment){
			this.firstPoint = firstPoint;
			this.secondPoint = secondPoint;
			
			Location[] definedArea = CktUtil.areaRange(firstPoint, secondPoint);
			fromLocation = definedArea[0];
			toLocation = definedArea[1];
			currentLocation = fromLocation.clone();
			
			this.order = order;
			this.increment = increment;
			
			from = CktUtil.locationToArray(fromLocation, order);
			to = CktUtil.locationToArray(toLocation, order);
			current = from.clone();
		}
		
		AreaData(Location firstPoint, Location secondPoint){
			this(firstPoint, secondPoint, new char[] {'y', 'x', 'z'}, new int[] {1, 1, 1});
		}
		
		Location getFirstPoint() {
			return firstPoint;
		}
		
		Location getSecondPoint() {
			return secondPoint;
		}
		
		Location getFromLocation() {
			return fromLocation;
		}
		
		Location getToLocation() {
			return toLocation;
		}
		
		Location getCurrentLocation() {
			return currentLocation;
		}
		
		void incrementLoop() {
			if (from.length != to.length && 
					from.length != increment.length && 
					from.length != current.length)
				return;
			
			current[current.length - 1] += increment[current.length - 1];
			
			for (int i = current.length -1; i >= 0; i--) {
				if (current[i] == to[i] + increment[i]) {
					current[i-1] += increment[i-1];
					current[i] = from[i];
				}
			}
			
			CktUtil.updateCoordinates(current, order, currentLocation);
		}
		
		boolean isLoopFinished() {
			return currentLocation.equals(toLocation);
		}
		
		int getYSize() {
			return toLocation.getBlockY() - fromLocation.getBlockY();
		}
		
		int getXSize() {
			return toLocation.getBlockX() - fromLocation.getBlockX();
		}
		
		int getZSize() {
			return toLocation.getBlockZ() - fromLocation.getBlockZ();
		}
		
		int getCurrentNormalizedX() {
			return currentLocation.getBlockX() - fromLocation.getBlockX();
		}
		
		int getCurrentNormalizedY() {
			return currentLocation.getBlockY() - fromLocation.getBlockY();
		}
		
		int getCurrentNormalizedZ() {
			return currentLocation.getBlockZ() - fromLocation.getBlockZ();
		}
	}
}














