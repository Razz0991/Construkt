package com.github.razz0991.construkt.shapes;

import org.bukkit.Location;
import org.bukkit.block.data.BlockData;

import com.github.razz0991.construkt.PlayerInfo;
import com.github.razz0991.construkt.filters.BaseFilter;
import com.github.razz0991.construkt.utility.AreaData;
import com.github.razz0991.construkt.utility.CktBlockContainer;

/*  Construkt Bukkit plugin for Minecraft.
 *  Copyright (C) 2020 _Razz_
 *
 *  Full disclaimer in Construkt.java
 */
public class CuboidShape extends BaseShape{

	public CuboidShape() {
		super();
	}
	
	public CuboidShape(PlayerInfo plyInfo) {
		super(plyInfo);
	}

	@Override
	public String getName() {
		return "cuboid";
	}

	@Override
	public CktBlockContainer generateShape(Location firstPoint, Location secondPoint, 
			BlockData blockData, BaseFilter[] filters) {
		boolean reversed = blockData == null;
		final AreaData data = new AreaData(firstPoint, secondPoint, reversed);
		final BlockData strictBlock = secondPoint.getBlock().getBlockData().clone();
		final CktBlockContainer container = new CktBlockContainer();
		
		Runnable runnable = new Runnable() {
			
			@Override
			public void run() {
				do {
					if (canPlace(data, filters))
						setBlock(blockData, data.getCurrentLocation(), container, strictBlock);
					
					boolean shouldWait = data.incrementLoop();
					if (shouldWait)
						return;
				} while (!data.isLoopFinished(container, ply));
			}
		};
		
		data.createFillTask(runnable);

		return container;
	}

}
