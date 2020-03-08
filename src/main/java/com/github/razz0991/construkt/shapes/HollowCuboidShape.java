package com.github.razz0991.construkt.shapes;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.block.data.BlockData;

import com.github.razz0991.construkt.shapes.filters.BaseFilter;
import com.github.razz0991.construkt.shapes.parameters.BooleanCktParameter;
import com.github.razz0991.construkt.shapes.parameters.CktParameter;

/*  Construkt Bukkit plugin for Minecraft.
 *  Copyright (C) 2020 _Razz_
 *
 *  Full disclaimer in Construkt.java
 */
public class HollowCuboidShape extends BaseShape{
	
	private final boolean borderModeDefault = false;
	private final String borderModeName = "border_mode";

	@Override
	public Map<String, CktParameter<?>> getDefaultParameters() {
		Map<String, CktParameter<?>> parameters = new HashMap<String, CktParameter<?>>();
		parameters.put(borderModeName, new BooleanCktParameter(borderModeDefault));
		return parameters;
	}

	@Override
	public boolean generateShape(Location firstPoint, Location secondPoint, Map<String, CktParameter<?>> parameters, 
			BlockData blockData, BaseFilter[] filters) {
		boolean reversed = blockData == null;
		final AreaData data = new AreaData(firstPoint, secondPoint, reversed);
		
		data.createFillTask(new Runnable() {
			
			@Override
			public void run() {
				do {
					if (canPlace(data, parameters, filters)) {
						if ((parseBooleanShapeParameter(parameters.get(borderModeName), true) &&
								isBorder(data)) || (!parseBooleanShapeParameter(parameters.get(borderModeName), borderModeDefault) &&
										isEdge(data)))
							setBlock(blockData, data.getCurrentLocation());
					}
					
					boolean shouldWait = data.incrementLoop();
					if (shouldWait)
						return;
				} while (!data.isLoopFinished());
			}
		});
		
		return true;
	}
	
	// Checks if the loop is currently on the edge of the area
	private boolean isEdge(AreaData data) {
		Location cur = data.getCurrentLocation();
		Location from = data.getFromLocation();
		Location to = data.getToLocation();
		
		return (cur.getBlockX() == from.getBlockX() ||
				cur.getBlockX() == to.getBlockX() ||
				cur.getBlockY() == from.getBlockY() ||
				cur.getBlockY() == to.getBlockY() ||
				cur.getBlockZ() == from.getBlockZ() ||
				cur.getBlockZ() == to.getBlockZ());
	}
	
	// Checks if the loop is currently on the border of the area
	private boolean isBorder(AreaData data) {
		Location cur = data.getCurrentLocation();
		Location from = data.getFromLocation();
		Location to = data.getToLocation();
		
		boolean isXEdge = (cur.getBlockX() == from.getBlockX() ||
				cur.getBlockX() == to.getBlockX());
		boolean isYEdge = (cur.getBlockY() == from.getBlockY() ||
				cur.getBlockY() == to.getBlockY());
		boolean isZEdge = (cur.getBlockZ() == from.getBlockZ() ||
				cur.getBlockZ() == to.getBlockZ());
		
		return ((isXEdge && isZEdge && !isYEdge) ||
				(isXEdge && isYEdge && !isZEdge) || 
				(isZEdge && isYEdge && !isXEdge) ||
				(isXEdge && isYEdge && isZEdge));
	}

}
