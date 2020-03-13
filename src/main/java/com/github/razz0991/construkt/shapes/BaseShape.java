package com.github.razz0991.construkt.shapes;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;

import com.github.razz0991.construkt.filters.BaseFilter;
import com.github.razz0991.construkt.parameters.ParameterObject;
import com.github.razz0991.construkt.utility.AreaData;
import com.github.razz0991.construkt.utility.CktBlockContainer;
import com.github.razz0991.construkt.utility.AreaInfo;

/*  Construkt Bukkit plugin for Minecraft.
 *  Copyright (C) 2020 _Razz_
 *
 *  Full disclaimer in Construkt.java
 */
/**
 * A shape to generate within a defined area
 */
public abstract class BaseShape extends ParameterObject {
	
	/**
	 * What mode blocks should be placed in
	 */
	protected PlaceMode placeMode = PlaceMode.AIR;
	
	/**
	 * Generates a shape and places blocks via the block data entered.
	 * @param firstPoint The first location of the area
	 * @param secondPoint The second location of the area
	 * @param parameters parameters parsed for the shape to use
	 * @param blockData The data of what block to place, null if the block will be air
	 * @param filter The filter that will be used
	 * @return true for success
	 */
	public abstract CktBlockContainer generateShape(Location firstPoint, Location secondPoint, BlockData blockData, BaseFilter[] filters);
	
	/**
	 * Gets the actual volume of the shape. Basic shapes only need the first and second point, however
	 * the sphere uses radial sizes so it will be different.
	 * @param firstPoint The first point for the area
	 * @param secondPoint The second point for the area
	 * @return {@link AreaInfo} with the relevant data
	 */
	public AreaInfo getVolumeInformation(Location firstPoint, Location secondPoint) {
		return new AreaInfo(firstPoint, secondPoint);
	}
	
	protected void setBlock(BlockData blockData, Location loc) {
		if (blockData != null)
			loc.getBlock().setBlockData(blockData.clone());
		else
			loc.getBlock().setType(Material.AIR);
	}
	
	protected void setBlock(BlockData blockData, Location loc, CktBlockContainer container) {
		container.addBlock(loc.getBlock().getBlockData(), loc);
		setBlock(blockData, loc);
	}
	
	protected boolean canPlace(AreaData data, BaseFilter[] filters) {
		return canPlace(null, filters, data);
	}
	
	protected boolean canPlace(Location current, BaseFilter[] filters, AreaData data) {
		Location cur = current;
		if (current == null)
			cur = data.getCurrentLocation();
		
		boolean placeInAir = true;
		if (placeMode == PlaceMode.SOLID)
			placeInAir = false;
		
		if ((placeInAir && cur.getBlock().getType() == Material.AIR) ||
				(!placeInAir && cur.getBlock().getType() != Material.AIR)) {
			if(filters.length == 0)
				return true;
			for (BaseFilter filter : filters) {
				if (!filter.checkCondition(data))
					return false;
			}
			return true;
		}
		return false;
	}
	
	/**
	 * Sets this shapes place mode
	 * @param mode The {@link PlaceMode} this shape should use
	 */
	public void setPlaceMode(PlaceMode mode) {
		placeMode = mode;
	}
	
	/**
	 * Place mode enum to set where blocks should be placed
	 */
	public enum PlaceMode {
		AIR,
		SOLID;
	}
}














