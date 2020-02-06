package com.github.razz0991.construkt.shapes;
/*  Construkt Bukkit plugin for Minecraft.
 *  Copyright (C) 2020 _Razz_
 *
 *  Full disclaimer in Construkt.java
 */
import org.bukkit.Location;
import org.bukkit.block.data.BlockData;

public class CuboidShape extends BaseShape{

	@Override
	public boolean generateShape(Location firstPoint, Location secondPoint, boolean placeInAir, BlockData blockData) {
		AreaData data = new AreaData(firstPoint, secondPoint);

		do {
			if (canPlace(data.getCurrentLocation(), placeInAir))
				setBlock(blockData, data.getCurrentLocation());
			
			data.incrementLoop();
		} while (!data.isLoopFinished());
		
		if (canPlace(data.getCurrentLocation(), placeInAir))
			setBlock(blockData, data.getCurrentLocation());
		
		return true;
	}

}
