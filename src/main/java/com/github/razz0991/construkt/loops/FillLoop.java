package com.github.razz0991.construkt.loops;
/*  Construkt Bukkit plugin for Minecraft.
 *  Copyright (C) 2020 _Razz_
 *
 *  Full disclaimer in Construkt.java
 */
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;

import com.github.razz0991.construkt.CktUtil;
import com.github.razz0991.construkt.shapes.BaseShape;

public class FillLoop implements BaseLoop{

	@Override
	public void runLoop(Location[] locations, boolean placeInAir, BlockData blockData, BaseShape shape) {
		if (locations.length != 2)
			return;
		
		Location first = locations[0];
		Location second = locations[1];
		
		int[] x, y, z;
		x = CktUtil.smallestNumber(first.getBlockX(), second.getBlockX());
		y = CktUtil.smallestNumber(first.getBlockY(), second.getBlockY());
		z = CktUtil.smallestNumber(first.getBlockZ(), second.getBlockZ());
		
		// Fill area loops
		for (int lz = z[0]; lz <= z[1]; lz++) {
			for (int lx = x[0]; lx <= x[1]; lx++) {
				for (int ly = y[0]; ly <= y[1]; ly++) {
					Location position = new Location(first.getWorld(), lx, ly, lz);
					// Only place blocks in air blocks.
					if ((placeInAir && position.getBlock().getType() == Material.AIR) ||
							(!placeInAir && position.getBlock().getType() != Material.AIR)) {
						// Check if allowed to place using the shapes conditions
						if (shape.loopCondition(first, second, position)) {
							if (blockData != null)
								position.getBlock().setBlockData(blockData.clone());
							else
								position.getBlock().setType(Material.AIR);
						}
					}
				}
			}
		}
	}

}
