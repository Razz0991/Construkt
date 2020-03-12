package com.github.razz0991.construkt.utility;

import org.bukkit.Location;

import com.github.razz0991.construkt.CktUtil;
import com.github.razz0991.construkt.Construkt;

/*  Construkt Bukkit plugin for Minecraft.
 *  Copyright (C) 2020 _Razz_
 *
 *  Full disclaimer in Construkt.java
 */
public class AreaData {
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
	 * @param reversed If the loop should be run in reverse
	 */
	public AreaData(Location firstPoint, Location secondPoint, char[] order, boolean reversed){
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
	 * @param reversed If the loop should be run in reverse
	 */
	public AreaData(Location firstPoint, Location secondPoint, boolean reversed){
		this(firstPoint, secondPoint, new char[] {'y', 'x', 'z'}, reversed);
	}
	
	/**
	 * Sets a new X coordinate for the current loop position.
	 * @param x
	 * @throws IndexOutOfBoundsException if x is not within the set area
	 */
	public void setCurrentX(int x) {
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
	public void setCurrentY(int y) {
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
	public void setCurrentZ(int z) {
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
	public Location getFirstPoint() {
		return firstPoint.clone();
	}
	
	/**
	 * Gets a copy of the second point.
	 * @return A cloned <code>Location</code>
	 */
	public Location getSecondPoint() {
		return secondPoint.clone();
	}
	
	/**
	 * Gets a copy of the areas smallest point.
	 * @return A cloned <code>Location</code>
	 */
	public Location getFromLocation() {
		return fromLocation.clone();
	}
	
	/**
	 * Gets a copy of the areas largest point.
	 * @return A cloned <code>Location</code>
	 */
	public Location getToLocation() {
		return toLocation.clone();
	}
	
	/**
	 * Gets a copy of the current location the loop is at.
	 * @return A clone of the <code>Location</code>
	 */
	public Location getCurrentLocation() {
		return currentLocation.clone();
	}
	
	/**
	 * Step to the next block in the set area.
	 * @return true if the loop has taken longer than a tick
	 */
	public boolean incrementLoop() {
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
	public boolean isLoopFinished() {
		if (loopFinished && taskId != -1) {
			cancelTask();
		}
		return loopFinished;
	}
	
	/**
	 * Gets the Y size of the set area.
	 * @return The Y size of the area
	 */
	public int getYSize() {
		return Math.abs(toLocation.getBlockY() - fromLocation.getBlockY());
	}
	
	/**
	 * Gets the X size of the set area.
	 * @return The X size of the area
	 */
	public int getXSize() {
		return Math.abs(toLocation.getBlockX() - fromLocation.getBlockX());
	}
	
	/**
	 * Gets the Z size of the set area.
	 * @return The Z size of the area
	 */
	public int getZSize() {
		return Math.abs(toLocation.getBlockZ() - fromLocation.getBlockZ());
	}
	
	/**
	 * Gets the X position of where the loop is currently at.
	 * @return The X value.
	 */
	public int getCurrentRelativeX() {
		return Math.abs(currentLocation.getBlockX() - fromLocation.getBlockX());
	}
	
	/**
	 * Gets the Y position of where the loop is currently at.
	 * @return The Y value.
	 */
	public int getCurrentRelativeY() {
		return Math.abs(currentLocation.getBlockY() - fromLocation.getBlockY());
	}
	
	/**
	 * Gets the Z position of where the loop is currently at.
	 * @return The Z value.
	 */
	public int getCurrentRelativeZ() {
		return Math.abs(currentLocation.getBlockZ() - fromLocation.getBlockZ());
	}
	
	public void createFillTask(Runnable task) {
		taskId = Construkt.plugin.getServer().getScheduler().scheduleSyncRepeatingTask(Construkt.plugin, task, 1, taskDelay);
	}
	
	public void cancelTask() {
		Construkt.plugin.getServer().getScheduler().cancelTask(taskId);
	}
}
