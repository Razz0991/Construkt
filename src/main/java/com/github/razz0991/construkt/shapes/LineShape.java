package com.github.razz0991.construkt.shapes;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.data.BlockData;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

import com.github.razz0991.construkt.PlayerInfo;
import com.github.razz0991.construkt.filters.BaseFilter;
import com.github.razz0991.construkt.utility.AreaData;
import com.github.razz0991.construkt.utility.CktBlockContainer;

/*  Construkt Bukkit plugin for Minecraft.
 *  Copyright (C) 2020 _Razz_
 *
 *  Full disclaimer in Construkt.java
 */
public class LineShape extends BaseShape {

	public LineShape() {
		super();
	}
	
	public LineShape(PlayerInfo plyInfo) {
		super(plyInfo);
	}

	@Override
	public CktBlockContainer generateShape(Location firstPoint, Location secondPoint, BlockData blockData,
			BaseFilter[] filters) {
		boolean reversed = blockData == null;
		final AreaData data = new AreaData(firstPoint, secondPoint, reversed);
		final BlockData strictBlock = secondPoint.getBlock().getBlockData().clone();
		final CktBlockContainer container = new CktBlockContainer();
		
		double distance = secondPoint.distance(firstPoint);
		
		Vector firstVec = firstPoint.toVector();
		Vector secondVec = secondPoint.toVector();
		firstVec.add(new Vector(0.5, 0.5, 0.5));
		secondVec.add(new Vector(0.5, 0.5, 0.5));
		BlockIterator iter = new BlockIterator(firstPoint.getWorld(), firstVec, 
				secondVec.subtract(firstVec), 0, (int)Math.ceil(distance));
		
		final List<Location> locations = new ArrayList<Location>();
		while (iter.hasNext()) {
			locations.add(iter.next().getLocation());
		}
		
		Runnable runnable = new Runnable() {
			
			@Override
			public void run() {
				do {
					if (canPlace(data, filters)) {
						if (locations.contains(data.getCurrentLocation()))
							setBlock(blockData, data.getCurrentLocation(), container, strictBlock);
					}
					
					boolean shouldWait = data.incrementLoop();
					if (shouldWait)
						return;
				} while (!data.isLoopFinished(container, ply));
			}
		};
		
		data.createFillTask(runnable);

		return container;
	}

	@Override
	public String getName() {
		return "line";
	}

}
