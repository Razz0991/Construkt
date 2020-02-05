package com.github.razz0991.construkt.shapes;
/*  Construkt Bukkit plugin for Minecraft.
 *  Copyright (C) 2020 _Razz_
 *
 *  Full disclaimer in Construkt.java
 */
import org.bukkit.Location;
import org.bukkit.block.data.BlockData;

import com.github.razz0991.construkt.CktUtil;

public class SphereShape extends BaseShape{
	
	public Location[] shapeLocations(Location firstLocation, Location secondLocation) {
		// Sphere uses radius locations, so need different locations.
		double dist = firstLocation.distance(secondLocation);
		dist = Math.ceil(dist);
		Location newFirst = new Location(firstLocation.getWorld(), 
				firstLocation.getX() - dist, 
				firstLocation.getY() - dist, 
				firstLocation.getZ() - dist);
		Location newSecond = new Location(firstLocation.getWorld(), 
				firstLocation.getX() + dist, 
				firstLocation.getY() + dist, 
				firstLocation.getZ() + dist);
		Location[] output = {newFirst, newSecond};
		return output;
	}

	@Override
	public boolean generateShape(Location[] locations, boolean placeInAir, BlockData blockData) {
		if (locations.length != 2)
			return false;
		
		Location[] sphereBoundry = shapeLocations(locations[0], locations[1]);
		Location first = sphereBoundry[0];
		Location second = sphereBoundry[1];
		Location current = first.clone();
		double dist = (second.getX() - first.getX()) / 2;
		Location center = new Location(
				first.getWorld(), 
				first.getX() + dist, 
				first.getY() + dist, 
				first.getZ() + dist);
		char[] order = {'y', 'x', 'z'};
		int[] from = CktUtil.locationToArray(first, order);
		int[] to = CktUtil.locationToArray(second, order);
		int[] increment = {1, 1, 1};
		int[] currentArr = CktUtil.locationToArray(current, order);

		do {
			if (canPlace(current, placeInAir)) {
				if (center.distance(current) < dist + 0.5)
					setBlock(blockData, current);
			}
			
			incrementLoop(from, to, increment, currentArr);
			CktUtil.updateCoordinates(currentArr, order, null, current);
		} while (!current.equals(second));
		
		if (canPlace(current, placeInAir)) {
			if (center.distance(current) < dist + 0.5)
				setBlock(blockData, current);
		}
		
		return true;
	}

}
