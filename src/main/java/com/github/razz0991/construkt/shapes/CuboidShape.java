package com.github.razz0991.construkt.shapes;
/*  Construkt Bukkit plugin for Minecraft.
 *  Copyright (C) 2020 _Razz_
 *
 *  Full disclaimer in Construkt.java
 */
import org.bukkit.Location;

public class CuboidShape extends BaseShape{

	@Override
	public String loopType() {
		return "fill";
	}

	@Override
	public boolean loopCondition(Location firstLocation, Location secondLocation, Location loopPosition) {
		// Everything should be filled
		return true;
	}

	@Override
	public Location[] shapeLocations(Location firstLocation, Location secondLocation) {
		// No difference in locations for cuboid
		Location[] output = {firstLocation, secondLocation};
		return output;
	}

}
