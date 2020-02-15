package com.github.razz0991.construkt.shapes;

import java.util.Map;

import org.bukkit.Location;
import org.bukkit.block.data.BlockData;

import com.github.razz0991.construkt.shapes.filters.BaseFilter;
import com.github.razz0991.construkt.shapes.parameters.ShapeParameter;

/*  Construkt Bukkit plugin for Minecraft.
 *  Copyright (C) 2020 _Razz_
 *
 *  Full disclaimer in Construkt.java
 */
public class CuboidShape extends BaseShape{

	@Override
	public Map<String, ShapeParameter<?>> getDefaultParameters() {
		return null;
	}

	@Override
	public boolean generateShape(Location firstPoint, Location secondPoint, Map<String, ShapeParameter<?>> parameters, 
			BlockData blockData, BaseFilter[] filters) {
		boolean reversed = blockData == null;
		final AreaData data = new AreaData(firstPoint, secondPoint, reversed);
		
		Runnable runnable = new Runnable() {
			
			@Override
			public void run() {
				do {
					if (canPlace(data, parameters, filters))
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
