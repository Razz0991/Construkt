package com.github.razz0991.construkt.utility;

import org.bukkit.Location;

import com.github.razz0991.construkt.CktUtil;

public class AreaInfo{
	private final int xLength;
	private final int yLength;
	private final int zLength;
	private final int volume;
	private final Location first;
	private final Location second;

	/**
	 * Contains information about a volume.
	 * @param first the first location
	 * @param second the second location
	 * @return {@link AreaInfo} containing x, y, and z lengths plus volume
	 */
	public AreaInfo(final Location first, final Location second) {
		Location[] area = CktUtil.areaRange(first, second);
		xLength = area[1].getBlockX() - area[0].getBlockX() + 1;
		yLength = area[1].getBlockY() - area[0].getBlockY() + 1;
		zLength = area[1].getBlockZ() - area[0].getBlockZ() + 1;
		volume = xLength * yLength * zLength;
		
		this.first = first.clone();
		this.second = second.clone();
	}
	
	public int getXLength() {
		return xLength;
	}
	
	public int getYLength() {
		return yLength;
	}
	
	public int getZLength() {
		return zLength;
	}
	
	public int getVolume() {
		return volume;
	}
	
	public boolean lengthExceedsMax(int maxLength) {
		return (xLength > maxLength || yLength > maxLength || zLength > maxLength);
	}

	public Location getFirstLocation() {
		return first;
	}

	public Location getSecondLocation() {
		return second;
	}
}