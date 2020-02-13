package com.github.razz0991.construkt.shapes;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.block.data.BlockData;

import com.github.razz0991.construkt.shapes.parameters.BooleanShapeParameter;
import com.github.razz0991.construkt.shapes.parameters.ShapeParameter;

/*  Construkt Bukkit plugin for Minecraft.
 *  Copyright (C) 2020 _Razz_
 *
 *  Full disclaimer in Construkt.java
 */
public class SphereShape extends BaseShape{
	
	private final boolean hollowModeDefault = false;
	private final String hollowModeName = "hollow";

	@Override
	public Map<String, ShapeParameter<?>> getDefaultParameters() {
		Map<String, ShapeParameter<?>> parameters = new HashMap<String, ShapeParameter<?>>();
		parameters.put(hollowModeName, new BooleanShapeParameter(hollowModeDefault));
		return parameters;
	}
	
	public Location[] radiusToCube(Location firstLocation, Location secondLocation) {
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
		boolean reversed = blockData == null;
		Location[] sphereBoundry = radiusToCube(firstPoint, secondPoint);
		final AreaData data = new AreaData(sphereBoundry[0], sphereBoundry[1], reversed);
		final double dist = (data.getToLocation().getX() - data.getFromLocation().getX()) / 2;
		final Location center = firstPoint;
		
		data.createFillTask(new Runnable() {
			
			@Override
			public void run() {
				do {
					if (canPlace(data.getCurrentLocation(), parameters)) {
						if (parseBooleanShapeParameter(parameters.get(hollowModeName), hollowModeDefault)) {
							double curDist = center.distance(data.getCurrentLocation());
							if (curDist < dist + 0.5 && curDist > dist - 0.5)
								setBlock(blockData, data.getCurrentLocation());
						}
						else {
							if (center.distance(data.getCurrentLocation()) < dist + 0.5)
								setBlock(blockData, data.getCurrentLocation());
						}
					}
					
					boolean shouldWait = data.incrementLoop();
					if (shouldWait)
						return;
				} while (!data.isLoopFinished());
			}
		});
		
		return true;
	}

}
