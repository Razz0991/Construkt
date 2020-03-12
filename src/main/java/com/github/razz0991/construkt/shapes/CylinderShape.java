package com.github.razz0991.construkt.shapes;

import org.bukkit.Location;
import org.bukkit.block.data.BlockData;

import com.github.razz0991.construkt.CktBlockContainer;
import com.github.razz0991.construkt.filters.BaseFilter;
import com.github.razz0991.construkt.parameters.AxisCktParameter;
import com.github.razz0991.construkt.parameters.BooleanCktParameter;

/*  Construkt Bukkit plugin for Minecraft.
 *  Copyright (C) 2020 _Razz_
 *
 *  Full disclaimer in Construkt.java
 */
public class CylinderShape extends BaseShape {
	
	private final boolean hollowModeDefault = false;
	private final String hollowModeName = "hollow";
	private final char axisDefault = 'y';
	private final String axisName = "axis";
	
	public CylinderShape() {
		super();
		parameters.put(hollowModeName, new BooleanCktParameter(hollowModeDefault));
		parameters.put(axisName, new AxisCktParameter(axisDefault));
	}
	
	public Location[] radiusToCube(Location firstLocation, Location secondLocation) {
		// Cylinder uses a radial axis, so need different locations.
		Location temp = secondLocation.clone();
		char axis = getAxisParameter(axisName, axisDefault);
		if (axis == 'y')
			temp.setY(firstLocation.getY());
		else if (axis == 'x')
			temp.setX(firstLocation.getX());
		else if (axis == 'z')
			temp.setZ(firstLocation.getZ());
		
		double dist = firstLocation.distance(temp);
		dist = Math.ceil(dist);
		Location newFirst = new Location(firstLocation.getWorld(), 
				axis != 'x' ? firstLocation.getX() - dist : firstLocation.getX(), 
				axis != 'y' ? firstLocation.getY() - dist : firstLocation.getY(), 
				axis != 'z' ? firstLocation.getZ() - dist : firstLocation.getZ());
		Location newSecond = new Location(firstLocation.getWorld(), 
				axis != 'x' ? firstLocation.getX() + dist : secondLocation.getX(), 
				axis != 'y' ? firstLocation.getY() + dist : secondLocation.getY(), 
				axis != 'z' ? firstLocation.getZ() + dist : secondLocation.getZ());
		Location[] output = {newFirst, newSecond};
		return output;
	}

	@Override
	public CktBlockContainer generateShape(Location firstPoint, Location secondPoint, BlockData blockData, BaseFilter[] filters) {
		boolean reversed = blockData == null;
		Location[] cylinderBoundry = radiusToCube(firstPoint, secondPoint);
		final AreaData data = new AreaData(cylinderBoundry[0], cylinderBoundry[1], reversed);
		final CktBlockContainer container = new CktBlockContainer();
		final char axis = getAxisParameter(axisName, axisDefault);
		double dist;
		
		switch (axis) {
		case 'y':
			dist = data.getXSize() / 2;
			break;
		case 'x':
			dist = data.getYSize() / 2;
			break;
		case 'z':
			dist = data.getYSize() / 2;
			break;
		default:
			dist = data.getXSize() / 2;
			break;
		}
		final double finalDist = dist;
		final Location center = firstPoint.clone();
		
		data.createFillTask(new Runnable() {
			
			@Override
			public void run() {
				do {
					if (canPlace(data, filters)) {
						if (axis == 'y')
							center.setY(data.getCurrentLocation().getY());
						else if (axis == 'x')
							center.setX(data.getCurrentLocation().getX());
						else if (axis == 'z')
							center.setZ(data.getCurrentLocation().getZ());
						
						if (getBooleanParameter(hollowModeName, hollowModeDefault)) {
							double curDist = center.distance(data.getCurrentLocation());
							if (curDist < finalDist + 0.5 && curDist > finalDist - 0.5)
								setBlock(blockData, data.getCurrentLocation(), container);
						}
						else {
							if (center.distance(data.getCurrentLocation()) < finalDist + 0.5)
								setBlock(blockData, data.getCurrentLocation(), container);
						}
					}
					
					boolean shouldWait = data.incrementLoop();
					if (shouldWait)
						return;
				} while (!data.isLoopFinished());
			}
		});
		
		return container;
	}

	@Override
	public String getName() {
		return "cylinder";
	}

}
