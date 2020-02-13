package com.github.razz0991.construkt.shapes;

import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;

import com.github.razz0991.construkt.CktUtil;
import com.github.razz0991.construkt.Construkt;
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
	 * @return true for success
	 */
	public abstract boolean generateShape(Location firstPoint, Location secondPoint, Map<String, ShapeParameter<?>> parameters, BlockData blockData);
	
	protected void setBlock(BlockData blockData, Location loc) {
		if (blockData != null)
			loc.getBlock().setBlockData(blockData.clone());
		else
			loc.getBlock().setType(Material.AIR);
	}
	
	protected boolean canPlace(Location current, Map<String, ShapeParameter<?>> parameters) {
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
	
	protected class AreaData {
		private final Location firstPoint;
		private final Location secondPoint;
		private final Location fromLocation;
		private final Location toLocation;
		private Location currentLocation;
		private char[] order;
		private int[] from;
		private int[] to;
		private final int increment;
		private int[] current;
		private boolean loopFinished = false;
		private int taskId = -1;
		private long lastTime = -1;
		private final int tickLength = 50;
		private final int taskDelay = 2;
		
		/**
		 * Creates an area from two locations to cycle through block by block.
		 * @param firstPoint The first corner of the area
		 * @param secondPoint The second corner of the area
		 * @param order What order the loop should run through
		 * @param increment How to increment the loop
		 */
		AreaData(Location firstPoint, Location secondPoint, char[] order, boolean reversed){
			this.firstPoint = firstPoint;
			this.secondPoint = secondPoint;
			
			Location[] definedArea = CktUtil.areaRange(firstPoint, secondPoint);
			
			fromLocation = !reversed ? definedArea[0] : definedArea[1];
			toLocation = !reversed ? definedArea[1] : definedArea[0];
			currentLocation = fromLocation.clone();
			
			this.order = order;
			increment = !reversed ? 1 : -1;
			
			from = CktUtil.locationToArray(fromLocation, order);
			to = CktUtil.locationToArray(toLocation, order);
			
			current = from.clone();
			
			lastTime = System.currentTimeMillis();
		}
		
		/**
		 * Creates an area from two locations to cycle through block by block.
		 * The order in which it will run will be z, then x, then y, from smallest
		 * to largest.
		 * @param firstPoint The first corner of the area
		 * @param secondPoint The second corner of the area
		 */
		AreaData(Location firstPoint, Location secondPoint, boolean reversed){
			this(firstPoint, secondPoint, new char[] {'y', 'x', 'z'}, reversed);
		}
		
		/**
		 * Sets a new X coordinate for the current loop position.
		 * @param x
		 * @throws IndexOutOfBoundsException if x is not within the set area
		 */
		void setCurrentX(int x) {
			if (x > toLocation.getBlockX())
				throw new IndexOutOfBoundsException("X position is larger than the area");
			else if (x < fromLocation.getBlockX())
				throw new IndexOutOfBoundsException("X position is smaller than the set area");
			
			currentLocation.setX(x);
			for (int i = 0; i < order.length; i++) {
				if (order[i] == 'x')
					current[i] = x;
			}
		}
		
		/**
		 * Sets a new Y coordinate for the current loop position.
		 * @param y
		 * @throws IndexOutOfBoundsException if y is not within the set area
		 */
		void setCurrentY(int y) {
			if (y > toLocation.getBlockY())
				throw new IndexOutOfBoundsException("Y position is larger than the area");
			else if (y < fromLocation.getBlockY())
				throw new IndexOutOfBoundsException("Y position is smaller than the set area");
			
			currentLocation.setY(y);
			for (int i = 0; i < order.length; i++) {
				if (order[i] == 'y')
					current[i] = y;
			}
		}
		
		/**
		 * Sets a new Z coordinate for the current loop position.
		 * @param z
		 * @throws IndexOutOfBoundsException if z is not within the set area.
		 */
		void setCurrentZ(int z) {
			if (z > toLocation.getBlockZ())
				throw new IndexOutOfBoundsException("Z position is larger than the area");
			else if (z < fromLocation.getBlockZ())
				throw new IndexOutOfBoundsException("Z position is smaller than the set area");
			
			currentLocation.setZ(z);
			for (int i = 0; i < order.length; i++) {
				if (order[i] == 'z')
					current[i] = z;
			}
		}
		
		/**
		 * Gets a copy of the first point.
		 * @return A cloned <code>Location</code>
		 */
		Location getFirstPoint() {
			return firstPoint.clone();
		}
		
		/**
		 * Gets a copy of the second point.
		 * @return A cloned <code>Location</code>
		 */
		Location getSecondPoint() {
			return secondPoint.clone();
		}
		
		/**
		 * Gets a copy of the areas smallest point.
		 * @return A cloned <code>Location</code>
		 */
		Location getFromLocation() {
			return fromLocation.clone();
		}
		
		/**
		 * Gets a copy of the areas largest point.
		 * @return A cloned <code>Location</code>
		 */
		Location getToLocation() {
			return toLocation.clone();
		}
		
		/**
		 * Gets a copy of the current location the loop is at.
		 * @return A clone of the <code>Location</code>
		 */
		Location getCurrentLocation() {
			return currentLocation.clone();
		}
		
		/**
		 * Step to the next block in the set area.
		 * @return true if the loop has taken longer than a tick
		 */
		boolean incrementLoop() {
			if (currentLocation.equals(toLocation)) {
				loopFinished = true;
				return false;
			}
			
			current[current.length - 1] += increment;
			
			for (int i = current.length -1; i >= 0; i--) {
				if (current[i] == to[i] + increment) {
					current[i-1] += increment;
					current[i] = from[i];
				}
			}
			
			CktUtil.updateCoordinates(current, order, currentLocation);
			
			if (System.currentTimeMillis() >= lastTime + tickLength) {
				lastTime = System.currentTimeMillis() + taskDelay * tickLength;
				return true;
			}
			return false;
		}
		
		/**
		 * Checks if the loop is finished.
		 * @return true if the loop is finished
		 */
		boolean isLoopFinished() {
			if (loopFinished && taskId != -1) {
				cancelTask();
			}
			return loopFinished;
		}
		
		/**
		 * Gets the Y size of the set area.
		 * @return The Y size of the area
		 */
		int getYSize() {
			return toLocation.getBlockY() - fromLocation.getBlockY();
		}
		
		/**
		 * Gets the X size of the set area.
		 * @return The X size of the area
		 */
		int getXSize() {
			return toLocation.getBlockX() - fromLocation.getBlockX();
		}
		
		/**
		 * Gets the Z size of the set area.
		 * @return The Z size of the area
		 */
		int getZSize() {
			return toLocation.getBlockZ() - fromLocation.getBlockZ();
		}
		
		/**
		 * Gets the X position of where the loop is currently at.
		 * @return The X value.
		 */
		int getCurrentRelativeX() {
			return currentLocation.getBlockX() - fromLocation.getBlockX();
		}
		
		/**
		 * Gets the Y position of where the loop is currently at.
		 * @return The Y value.
		 */
		int getCurrentRelativeY() {
			return currentLocation.getBlockY() - fromLocation.getBlockY();
		}
		
		/**
		 * Gets the Z position of where the loop is currently at.
		 * @return The Z value.
		 */
		int getCurrentRelativeZ() {
			return currentLocation.getBlockZ() - fromLocation.getBlockZ();
		}
		
		void createFillTask(Runnable task) {
			taskId = Construkt.plugin.getServer().getScheduler().scheduleSyncRepeatingTask(Construkt.plugin, task, 1, taskDelay);
		}
		
		void cancelTask() {
			Construkt.plugin.getServer().getScheduler().cancelTask(taskId);
		}
	}
}














