package com.github.razz0991.construkt.shapes;
/*  Construkt Bukkit plugin for Minecraft.
 *  Copyright (C) 2020 _Razz_
 *
 *  Full disclaimer in Construkt.java
 */
import org.bukkit.Location;
import org.bukkit.block.data.BlockData;

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
		AreaData data = new AreaData(sphereBoundry[0], sphereBoundry[1]);
		double dist = (data.getToLocation().getX() - data.getFromLocation().getX()) / 2;
		Location center = locations[0];

		do {
			if (canPlace(data.getCurrentLocation(), placeInAir)) {
				if (center.distance(data.getCurrentLocation()) < dist + 0.5)
					setBlock(blockData, data.getCurrentLocation());
			}
			
			data.incrementLoop();
		} while (!data.isLoopFinished());
		
		if (canPlace(data.getCurrentLocation(), placeInAir)) {
			if (center.distance(data.getCurrentLocation()) < dist + 0.5)
				setBlock(blockData, data.getCurrentLocation());
		}
		
		return true;
	}

}
