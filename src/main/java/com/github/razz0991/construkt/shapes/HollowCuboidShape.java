package com.github.razz0991.construkt.shapes;

import org.bukkit.Location;
import org.bukkit.block.data.BlockData;

import com.github.razz0991.construkt.filters.BaseFilter;
import com.github.razz0991.construkt.parameters.BooleanCktParameter;
import com.github.razz0991.construkt.utility.AreaData;
import com.github.razz0991.construkt.utility.CktBlockContainer;

/*  Construkt Bukkit plugin for Minecraft.
 *  Copyright (C) 2020 _Razz_
 *
 *  Full disclaimer in Construkt.java
 */
public class HollowCuboidShape extends BaseShape{
	
	private final boolean borderModeDefault = false;
	private final String borderModeName = "border_mode";
	
	public HollowCuboidShape() {
		super();
		parameters.put(borderModeName, new BooleanCktParameter(borderModeDefault));
	}

	@Override
	public String getName() {
		return "hollow_cuboid";
	}

	@Override
	public CktBlockContainer generateShape(Location firstPoint, Location secondPoint, 
			BlockData blockData, BaseFilter[] filters) {
		boolean reversed = blockData == null;
		final AreaData data = new AreaData(firstPoint, secondPoint, reversed);
		final CktBlockContainer container = new CktBlockContainer();
		
		data.createFillTask(new Runnable() {
			
			@Override
			public void run() {
				do {
					if (canPlace(data, filters)) {
						if ((getBooleanParameter(borderModeName, true) &&
								isBorder(data)) || (!getBooleanParameter(borderModeName, borderModeDefault) &&
										isEdge(data)))
							setBlock(blockData, data.getCurrentLocation(), container);
					}
					
					boolean shouldWait = data.incrementLoop();
					if (shouldWait)
						return;
				} while (!data.isLoopFinished());
			}
		});
		
		return container;
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
