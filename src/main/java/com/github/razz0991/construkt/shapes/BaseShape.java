package com.github.razz0991.construkt.shapes;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;

import com.github.razz0991.construkt.PlayerInfo;
import com.github.razz0991.construkt.filters.BaseFilter;
import com.github.razz0991.construkt.parameters.ParameterObject;
import com.github.razz0991.construkt.protection.ProtectionPlugins;
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
	protected PlayerInfo ply;
	/**
	 * Whether this shape uses a single location
	 */
	protected boolean usingSingleLocation = false;
	protected boolean cancelPointPlacement = false;
	
	public BaseShape() {
		this(null);
	}
	
	public BaseShape(PlayerInfo plyInfo) {
		ply = plyInfo;
	}
	
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
		if (!isUsingSingleLocation())
			return new AreaInfo(firstPoint, secondPoint);
		else
			return new AreaInfo(firstPoint, firstPoint);
	}
	
	protected BlockData setBlock(BlockData blockData, Location loc, BlockData strictBlock) {
		if (!ProtectionPlugins.canBuild(loc, ply.getPlayer()))
			return null;
		
		BlockData before = loc.getBlock().getBlockData();
		if (strictBlock == null)
			strictBlock = Material.AIR.createBlockData();
		
		if (blockData != null) {
			if (ply.useExactCopy()) {
				if (!ply.isStrictBreak() || placeMode == PlaceMode.AIR ||
						loc.getBlock().getType() == strictBlock.getMaterial())
					loc.getBlock().setBlockData(blockData.clone());
				else
					return null;
			}
			else {
				if (!ply.isStrictBreak() || placeMode == PlaceMode.AIR ||
						loc.getBlock().getType() == strictBlock.getMaterial())
					loc.getBlock().setType(blockData.getMaterial());
				else
					return null;
			}
		}
		else {
			if (!ply.isStrictBreak() ||
					loc.getBlock().getType() == strictBlock.getMaterial())
				loc.getBlock().setType(Material.AIR);
			else
				return null;
		}
		
		return before;
	}
	
	protected void setBlock(BlockData blockData, Location loc, CktBlockContainer container, BlockData strictBlock) {
		BlockData data = setBlock(blockData, loc, strictBlock);
		if (data != null)
			container.addBlock(data, loc);
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
	
	public boolean isUsingSingleLocation() {
		return usingSingleLocation;
	}
	
	public boolean isCancelingPointPlacement() {
		return cancelPointPlacement;
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














