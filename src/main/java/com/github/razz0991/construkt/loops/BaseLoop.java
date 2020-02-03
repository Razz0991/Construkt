package com.github.razz0991.construkt.loops;
/*  Construkt Bukkit plugin for Minecraft.
 *  Copyright (C) 2020 _Razz_
 *
 *  Full disclaimer in Construkt.java
 */
import org.bukkit.Location;
import org.bukkit.block.data.BlockData;

import com.github.razz0991.construkt.shapes.BaseShape;

public interface BaseLoop {
	public void runLoop(Location[] locations, boolean placeInAir, BlockData blockData, BaseShape shape);
}
