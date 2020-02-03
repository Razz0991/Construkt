package com.github.razz0991.construkt.shapes;
/*  Construkt Bukkit plugin for Minecraft.
 *  Copyright (C) 2020 _Razz_
 *
 *  Full disclaimer in Construkt.java
 */
import org.bukkit.Location;

import com.github.razz0991.construkt.loops.BaseLoop;
import com.github.razz0991.construkt.loops.Loops;

public abstract class BaseShape {
	public abstract String loopType();
	public abstract boolean loopCondition(Location firstLocation, Location secondLocation, Location loopPosition);
	public abstract Location[] shapeLocations(Location firstLocation, Location secondLocation);
	
	public BaseLoop getLoop() {
		return Loops.getLoop(loopType());
	}
}
