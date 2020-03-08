package com.github.razz0991.construkt.shapes;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;

import com.github.razz0991.construkt.shapes.filters.BaseFilter;
import com.github.razz0991.construkt.shapes.parameters.IntegerCktParameter;
import com.github.razz0991.construkt.shapes.parameters.CktParameter;

/*  Construkt Bukkit plugin for Minecraft.
 *  Copyright (C) 2020 _Razz_
 *
 *  Full disclaimer in Construkt.java
 */
public class Overlay extends BaseShape {
	
	private final String depthName = "depth";
	private final int depthDefault = 1;

	@Override
	public Map<String, CktParameter<?>> getDefaultParameters() {
		Map<String, CktParameter<?>> params = new HashMap<String, CktParameter<?>>();
		IntegerCktParameter depth = new IntegerCktParameter(depthDefault, 1, 10);
		params.put(depthName, depth);
		return params;
	}

	@Override
	public boolean generateShape(Location firstPoint, Location secondPoint, Map<String, CktParameter<?>> parameters,
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
						if (parseBooleanShapeParameter(parameters.get("place_in_air"), true)) {
							int startY = cur.getBlockY() + 1;
							int endY = startY + (parseIntegerShapeParameter(parameters.get(depthName), depthDefault) - 1);
							if (endY > data.getToLocation().getBlockY())
								endY = data.getToLocation().getBlockY();
							for (int y = startY; y <= endY; y++) {
								cur.setY(y);
								if (canPlace(cur, parameters, filters, data))
									setBlock(blockData, cur);
							}
						}
						else {
							int startY = cur.getBlockY();
							int endY = startY - (parseIntegerShapeParameter(parameters.get(depthName), depthDefault) - 1);
							if (endY < data.getFromLocation().getBlockY())
								endY = data.getFromLocation().getBlockY();
							
							for (int y = startY; y >= endY; y--) {
								cur.setY(y);
								if (canPlace(cur, parameters, filters, data)) {
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
