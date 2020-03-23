package com.github.razz0991.construkt.utility;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.block.data.BlockData;

/*  Construkt Bukkit plugin for Minecraft.
 *  Copyright (C) 2020 _Razz_
 *
 *  Full disclaimer in Construkt.java
 */
public class CopyData {
	
	private Map<LocalLocation, BlockData> blocks = new HashMap<LocalLocation, BlockData>();
//	private AreaData area;
	private int xSize;
	private int ySize;
	private int zSize;
	
	public CopyData(AreaData area) {
		xSize = area.getXSize();
		ySize = area.getYSize();
		zSize = area.getZSize();
	}
	
	public void addBlock(BlockData data, int localX, int localY, int localZ) {
		blocks.put(new LocalLocation(localX, localY, localZ), data);
	}
	
	public BlockData getBlock(int localX, int localY, int localZ) {
		LocalLocation loc = new LocalLocation(localX, localY, localZ);
		return blocks.containsKey(loc) ? blocks.get(loc) : null;
	}
	
	public int getXSize() {
		return xSize;
	}
	
	public int getYSize() {
		return ySize;
	}
	
	public int getZSize() {
		return zSize;
	}
}
