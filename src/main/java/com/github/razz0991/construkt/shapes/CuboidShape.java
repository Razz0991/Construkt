package com.github.razz0991.construkt.shapes;
/*  Construkt Bukkit plugin for Minecraft.
 *  Copyright (C) 2020 _Razz_
 *
 *  Full disclaimer in Construkt.java
 */
import org.bukkit.Location;
import org.bukkit.block.data.BlockData;

import com.github.razz0991.construkt.CktUtil;

public class CuboidShape extends BaseShape{

	@Override
	public boolean generateShape(Location[] locations, boolean placeInAir, BlockData blockData) {
		if (locations.length != 2)
			return false;
		
		Location[] sortedLocations = CktUtil.areaRange(locations[0], locations[1]);
		Location first = sortedLocations[0];
		Location second = sortedLocations[1];
		Location current = first.clone();
		char[] order = {'y', 'x', 'z'};
		int[] from = CktUtil.locationToArray(first, order);
		int[] to = CktUtil.locationToArray(second, order);
		int[] increment = {1, 1, 1};
		int[] currentArr = CktUtil.locationToArray(current, order);

		do {
			if (canPlace(current, placeInAir))
				setBlock(blockData, current);
			
			incrementLoop(from, to, increment, currentArr);
			CktUtil.updateCoordinates(currentArr, order, null, current);
		} while (!current.equals(second));
		
		if (canPlace(current, placeInAir))
			setBlock(blockData, current);
		
		return true;
	}

}
