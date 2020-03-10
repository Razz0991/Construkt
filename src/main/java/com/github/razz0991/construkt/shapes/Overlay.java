package com.github.razz0991.construkt.shapes;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;

import com.github.razz0991.construkt.filters.BaseFilter;
import com.github.razz0991.construkt.parameters.IntegerCktParameter;

/*  Construkt Bukkit plugin for Minecraft.
 *  Copyright (C) 2020 _Razz_
 *
 *  Full disclaimer in Construkt.java
 */
public class Overlay extends BaseShape {
	
	private final String depthName = "depth";
	private final int depthDefault = 1;
	
	public Overlay() {
		super();
		parameters.put(depthName, new IntegerCktParameter(depthDefault, 1, 10));
	}

	@Override
	public String getName() {
		return "overlay";
	}

	@Override
	public boolean generateShape(Location firstPoint, Location secondPoint,
			BlockData blockData, BaseFilter[] filters) {
		
		final AreaData data = new AreaData(firstPoint, secondPoint, new char[] {'x', 'z'}, false);
		data.setCurrentY(data.getToLocation().getBlockY());
		
		data.createFillTask(new Runnable() {
			
			@Override
			public void run() {
				do {
					Location cur = data.getCurrentLocation();
					int min = data.getFromLocation().getBlockY();
					
					while (cur.getBlock().getType() == Material.AIR && cur.getBlockY() >= min) {
						cur.setY(cur.getY() - 1);
					}
					
					if (cur.getBlock().getType() != Material.AIR) {
						if (placeMode == PlaceMode.AIR) {
							int startY = cur.getBlockY() + 1;
							int endY = startY + (parseIntegerParameter(depthName, depthDefault) - 1);
							if (endY > data.getToLocation().getBlockY())
								endY = data.getToLocation().getBlockY();
							for (int y = startY; y <= endY; y++) {
								cur.setY(y);
								if (canPlace(cur, filters, data))
									setBlock(blockData, cur);
							}
						}
						else {
							int startY = cur.getBlockY();
							int endY = startY - (parseIntegerParameter(depthName, depthDefault) - 1);
							if (endY < data.getFromLocation().getBlockY())
								endY = data.getFromLocation().getBlockY();
							
							for (int y = startY; y >= endY; y--) {
								cur.setY(y);
								if (canPlace(cur, filters, data)) {
									setBlock(blockData, cur);
								}
							}
						}
					}
					
					boolean shouldWait = data.incrementLoop();
					if (shouldWait)
						return;
				} while(!data.isLoopFinished());
			}
		});
		
		return true;
	}

}
