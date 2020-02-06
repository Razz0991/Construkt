package com.github.razz0991.construkt.shapes;
import java.util.Map;

/*  Construkt Bukkit plugin for Minecraft.
 *  Copyright (C) 2020 _Razz_
 *
 *  Full disclaimer in Construkt.java
 */
import org.bukkit.Location;
import org.bukkit.block.data.BlockData;

import com.github.razz0991.construkt.shapes.parameters.ShapeParameter;

public class SphereShape extends BaseShape{

	@Override
	public Map<String, ShapeParameter<?>> getDefaultParameters() {
		return null;
	}
	
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
	public boolean generateShape(Location firstPoint, Location secondPoint, Map<String, ShapeParameter<?>> parameters, BlockData blockData) {
		Location[] sphereBoundry = shapeLocations(firstPoint, secondPoint);
		AreaData data = new AreaData(sphereBoundry[0], sphereBoundry[1]);
		double dist = (data.getToLocation().getX() - data.getFromLocation().getX()) / 2;
		Location center = firstPoint;

		do {
			if (canPlace(data.getCurrentLocation(), parameters)) {
				if (center.distance(data.getCurrentLocation()) < dist + 0.5)
					setBlock(blockData, data.getCurrentLocation());
			}
			
			data.incrementLoop();
		} while (!data.isLoopFinished());
		
		if (canPlace(data.getCurrentLocation(), parameters)) {
			if (center.distance(data.getCurrentLocation()) < dist + 0.5)
				setBlock(blockData, data.getCurrentLocation());
		}
		
		return true;
	}

}
