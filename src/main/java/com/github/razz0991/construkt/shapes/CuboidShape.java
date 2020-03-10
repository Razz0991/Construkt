package com.github.razz0991.construkt.shapes;

import org.bukkit.Location;
import org.bukkit.block.data.BlockData;

import com.github.razz0991.construkt.filters.BaseFilter;

/*  Construkt Bukkit plugin for Minecraft.
 *  Copyright (C) 2020 _Razz_
 *
 *  Full disclaimer in Construkt.java
 */
public class CuboidShape extends BaseShape{

	@Override
	public String getName() {
		return "cuboid";
	}

	@Override
	public boolean generateShape(Location firstPoint, Location secondPoint, 
			BlockData blockData, BaseFilter[] filters) {
		boolean reversed = blockData == null;
		final AreaData data = new AreaData(firstPoint, secondPoint, reversed);
		
		Runnable runnable = new Runnable() {
			
			@Override
			public void run() {
				do {
					if (canPlace(data, filters))
						setBlock(blockData, data.getCurrentLocation());
					
					boolean shouldWait = data.incrementLoop();
					if (shouldWait)
						return;
				} while (!data.isLoopFinished());
			}
		};
		
		data.createFillTask(runnable);

		return true;
	}

}
