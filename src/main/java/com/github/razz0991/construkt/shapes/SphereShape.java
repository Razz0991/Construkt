package com.github.razz0991.construkt.shapes;
/*  Construkt Bukkit plugin for Minecraft.
 *  Copyright (C) 2020 _Razz_
 *
 *  Full disclaimer in Construkt.java
 */
import org.bukkit.Location;

public class SphereShape extends BaseShape{

	@Override
	public String loopType() {
		return "fill";
	}

	@Override
	public boolean loopCondition(Location firstLocation, Location secondLocation, Location loopPosition) {
		double dist = (secondLocation.getX() - firstLocation.getX()) / 2;
		Location center = new Location(
				firstLocation.getWorld(), 
				firstLocation.getX() + dist, 
				firstLocation.getY() + dist, 
				firstLocation.getZ() + dist);
		
		if (center.distance(loopPosition) < dist + 0.5)
			return true;
		return false;
	}

	@Override
	public Location[] shapeLocations(Location firstLocation, Location secondLocation) {
		// Sphere uses radius locations, so need different locations.
		double dist = firstLocation.distance(secondLocation);
		dist = Math.ceil(dist);
		Location newFirst = new Location(firstLocation.getWorld(), 
				firstLocation.getX() - dist, 
				firstLocation.getY() - dist, 
				firstLocation.getZ() - dist);
		Location newSecond = new Location(firstLocation.getWorld(), 
				firstLocation.getX() + dist, 
				firstLocation.getY() + dist, 
				firstLocation.getZ() + dist);
		Location[] output = {newFirst, newSecond};
		return output;
	}

}
