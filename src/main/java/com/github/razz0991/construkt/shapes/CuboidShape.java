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
	public boolean generateShape(Location[] locations, boolean placeInAir, BlockData blockData) {
		if (locations.length != 2)
			return false;
		
		AreaData data = new AreaData(locations[0], locations[1]);

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
