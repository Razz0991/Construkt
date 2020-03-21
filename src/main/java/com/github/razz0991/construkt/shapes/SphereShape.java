package com.github.razz0991.construkt.shapes;

import org.bukkit.Location;
import org.bukkit.block.data.BlockData;

import com.github.razz0991.construkt.PlayerInfo;
import com.github.razz0991.construkt.filters.BaseFilter;
import com.github.razz0991.construkt.parameters.BooleanCktParameter;
import com.github.razz0991.construkt.utility.AreaData;
import com.github.razz0991.construkt.utility.CktBlockContainer;
import com.github.razz0991.construkt.utility.AreaInfo;

/*  Construkt Bukkit plugin for Minecraft.
 *  Copyright (C) 2020 _Razz_
 *
 *  Full disclaimer in Construkt.java
 */
public class SphereShape extends BaseShape{
	
	private final boolean hollowModeDefault = false;
	private final String hollowModeName = "hollow";
	
	public SphereShape() {
		super();
	}
	
	public SphereShape(PlayerInfo plyInfo) {
		super(plyInfo);
		parameters.put(hollowModeName, new BooleanCktParameter(hollowModeDefault));
	}

	@Override
	public String getName() {
		return "sphere";
	}
	
	public Location[] radiusToCube(Location firstLocation, Location secondLocation) {
		// Sphere uses radius locations, so need different locations.
		AreaInfo info = getVolumeInformation(firstLocation, secondLocation);
		Location[] output = {info.getFirstLocation(), info.getSecondLocation()};
		return output;
	}

	@Override
	public CktBlockContainer generateShape(Location firstPoint, Location secondPoint, 
			BlockData blockData, BaseFilter[] filters) {
		boolean reversed = blockData == null;
		Location[] sphereBoundry = radiusToCube(firstPoint, secondPoint);
		final AreaData data = new AreaData(sphereBoundry[0], sphereBoundry[1], reversed);
		final BlockData strictBlock = secondPoint.getBlock().getBlockData().clone();
		final CktBlockContainer container = new CktBlockContainer();
		final double dist = data.getXSize() / 2;
		final Location center = firstPoint;
		
		data.createFillTask(new Runnable() {
			
			@Override
			public void run() {
				do {
					if (canPlace(data, filters)) {
						if (getBooleanParameter(hollowModeName, hollowModeDefault)) {
							double curDist = center.distance(data.getCurrentLocation());
							if (curDist < dist + 0.5 && curDist > dist - 0.5)
								setBlock(blockData, data.getCurrentLocation(), container, strictBlock);
						}
						else {
							if (center.distance(data.getCurrentLocation()) < dist + 0.5)
								setBlock(blockData, data.getCurrentLocation(), container, strictBlock);
						}
					}
					
					boolean shouldWait = data.incrementLoop();
					if (shouldWait)
						return;
				} while (!data.isLoopFinished(container, ply));
			}
		});
		
		return container;
	}

	@Override
	public AreaInfo getVolumeInformation(Location firstPoint, Location secondPoint) {

		double dist = firstPoint.distance(secondPoint);
		dist = Math.ceil(dist);
		Location newFirst = new Location(firstPoint.getWorld(), 
				firstPoint.getX() - dist, 
				firstPoint.getY() - dist, 
				firstPoint.getZ() - dist);
		Location newSecond = new Location(firstPoint.getWorld(), 
				firstPoint.getX() + dist, 
				firstPoint.getY() + dist, 
				firstPoint.getZ() + dist);
		return new AreaInfo(newFirst, newSecond);
	}

}
