package com.github.razz0991.construkt.shapes;

import org.bukkit.Location;
import org.bukkit.block.data.BlockData;

import com.github.razz0991.construkt.filters.BaseFilter;
import com.github.razz0991.construkt.utility.AreaData;
import com.github.razz0991.construkt.utility.CktBlockContainer;

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
	public CktBlockContainer generateShape(Location firstPoint, Location secondPoint, 
			BlockData blockData, BaseFilter[] filters) {
		boolean reversed = blockData == null;
		final AreaData data = new AreaData(firstPoint, secondPoint, reversed);
		final CktBlockContainer container = new CktBlockContainer();
		
		Runnable runnable = new Runnable() {
			
			@Override
			public void run() {
				do {
					if (canPlace(data, filters))
						setBlock(blockData, data.getCurrentLocation(), container);
					
					boolean shouldWait = data.incrementLoop();
					if (shouldWait)
						return;
				} while (!data.isLoopFinished(container));
			}
		};
		
		data.createFillTask(runnable);

		return container;
	}

}
