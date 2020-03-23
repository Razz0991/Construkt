package com.github.razz0991.construkt.shapes;

import org.bukkit.Location;
import org.bukkit.block.data.BlockData;

import com.github.razz0991.construkt.PlayerInfo;
import com.github.razz0991.construkt.filters.BaseFilter;
import com.github.razz0991.construkt.parameters.BooleanCktParameter;
import com.github.razz0991.construkt.utility.AreaData;
import com.github.razz0991.construkt.utility.CktBlockContainer;
import com.github.razz0991.construkt.utility.CopyData;

/*  Construkt Bukkit plugin for Minecraft.
 *  Copyright (C) 2020 _Razz_
 *
 *  Full disclaimer in Construkt.java
 */
public class CopyShape extends BaseShape {
	
	private static String pasteModeName = "paste_mode";
	private static boolean pasteModeDefault = false;
	private CopyData copy = null;
	
	public CopyShape() {
		super();
	}
	
	public CopyShape(PlayerInfo ply) {
		super(ply);
		parameters.put(pasteModeName, new BooleanCktParameter(pasteModeDefault));
	}

	@Override
	public CktBlockContainer generateShape(Location firstPoint, Location secondPoint, BlockData blockData,
			BaseFilter[] filters) {
		if (isUsingSingleLocation()) {
			final CktBlockContainer cont = copy != null ? new CktBlockContainer() : null;
			if (copy != null) {
				Location newSecond = firstPoint.clone();
				newSecond.setX(newSecond.getX() + copy.getXSize());
				newSecond.setY(newSecond.getY() + copy.getYSize());
				newSecond.setZ(newSecond.getZ() + copy.getZSize());
				final AreaData data = new AreaData(firstPoint, newSecond, false);
				data.createFillTask(new Runnable() {
					
					@Override
					public void run() {
						do {
							BlockData bdata = copy.getBlock(data.getCurrentRelativeX(), 
									data.getCurrentRelativeY(), 
									data.getCurrentRelativeZ());
							setBlock(bdata, data.getCurrentLocation(), cont, blockData);
							if (data.incrementLoop())
								return;
						} while (!data.isLoopFinished(cont, ply));
					}
				});
			}
			return cont;
		}
		else {
			AreaData data = new AreaData(firstPoint, secondPoint, false);
			copy = new CopyData(data);
			do {
				data.getCurrentLocation();
				copy.addBlock(data.getCurrentLocation().getBlock().getBlockData(),
						data.getCurrentRelativeX(), data.getCurrentRelativeY(), data.getCurrentRelativeZ());
				data.incrementLoop();
			} while (!data.isLoopFinished(ply));
		}
		return null;
	}

	@Override
	public String getName() {
		return "copy";
	}
	
	@Override
	public boolean isUsingSingleLocation() {
		return getBooleanParameter(pasteModeName, pasteModeDefault);
	}

}
