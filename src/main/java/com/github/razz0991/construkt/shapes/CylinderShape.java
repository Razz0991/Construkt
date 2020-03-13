package com.github.razz0991.construkt.shapes;

import org.bukkit.Location;
import org.bukkit.block.data.BlockData;

import com.github.razz0991.construkt.filters.BaseFilter;
import com.github.razz0991.construkt.parameters.AxisCktParameter;
import com.github.razz0991.construkt.parameters.BooleanCktParameter;
import com.github.razz0991.construkt.utility.AreaData;
import com.github.razz0991.construkt.utility.CktBlockContainer;
import com.github.razz0991.construkt.utility.AreaInfo;

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
		AreaInfo info = getVolumeInformation(firstLocation, secondLocation);
		Location[] output = {info.getFirstLocation(), info.getSecondLocation()};
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
				} while (!data.isLoopFinished(container));
			}
		});
		
		return container;
	}

	@Override
	public String getName() {
		return "cylinder";
	}

	@Override
	public AreaInfo getVolumeInformation(Location firstPoint, Location secondPoint) {
		Location temp = secondPoint.clone();
		char axis = getAxisParameter(axisName, axisDefault);
		if (axis == 'y')
			temp.setY(firstPoint.getY());
		else if (axis == 'x')
			temp.setX(firstPoint.getX());
		else if (axis == 'z')
			temp.setZ(firstPoint.getZ());
		
		double dist = firstPoint.distance(temp);
		dist = Math.ceil(dist);
		Location newFirst = new Location(firstPoint.getWorld(), 
				axis != 'x' ? firstPoint.getX() - dist : firstPoint.getX(), 
				axis != 'y' ? firstPoint.getY() - dist : firstPoint.getY(), 
				axis != 'z' ? firstPoint.getZ() - dist : firstPoint.getZ());
		Location newSecond = new Location(firstPoint.getWorld(), 
				axis != 'x' ? firstPoint.getX() + dist : secondPoint.getX(), 
				axis != 'y' ? firstPoint.getY() + dist : secondPoint.getY(), 
				axis != 'z' ? firstPoint.getZ() + dist : secondPoint.getZ());
		return new AreaInfo(newFirst, newSecond);
	}

}
