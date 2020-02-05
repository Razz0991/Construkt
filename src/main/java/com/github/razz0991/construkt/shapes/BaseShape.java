package com.github.razz0991.construkt.shapes;
/*  Construkt Bukkit plugin for Minecraft.
 *  Copyright (C) 2020 _Razz_
 *
 *  Full disclaimer in Construkt.java
 */
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;

public abstract class BaseShape {
	public abstract boolean generateShape(Location[] locations, boolean placeInAir, BlockData blockData);
	
	public void incrementLoop(int[] from, int[] to, int[] increment, int[] current) {
		if (from.length != to.length && 
				from.length != increment.length && 
				from.length != current.length)
			return;
		
		current[current.length - 1] += increment[current.length - 1];
		
		for (int i = current.length -1; i >= 0; i--) {
			if (current[i] == to[i] + increment[i]) {
				current[i-1] += increment[i-1];
				current[i] = from[i];
			}
		}
	}
	
	public void setBlock(BlockData blockData, Location loc) {
		if (blockData != null)
			loc.getBlock().setBlockData(blockData.clone());
		else
			loc.getBlock().setType(Material.AIR);
	}
	
	public boolean canPlace(Location current, boolean placeInAir) {
		if ((placeInAir && current.getBlock().getType() == Material.AIR) ||
				(!placeInAir && current.getBlock().getType() != Material.AIR)) {
			return true;
		}
		return false;
	}
}
