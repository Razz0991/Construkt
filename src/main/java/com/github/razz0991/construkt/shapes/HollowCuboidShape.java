package com.github.razz0991.construkt.shapes;

import org.bukkit.Location;
import org.bukkit.block.data.BlockData;

public class HollowCuboidShape extends BaseShape{

	@Override
	public boolean generateShape(Location[] locations, boolean placeInAir, BlockData blockData) {
		if (locations.length != 2)
			return false;
		
		AreaData data = new AreaData(locations[0], locations[1]);

		do {
			if (canPlace(data.getCurrentLocation(), placeInAir) && isEdge(data))
				setBlock(blockData, data.getCurrentLocation());
			
			data.incrementLoop();
		} while (!data.isLoopFinished());
		
		if (canPlace(data.getCurrentLocation(), placeInAir) && isEdge(data))
			setBlock(blockData, data.getCurrentLocation());
		
		return true;
	}
	
	private boolean isEdge(AreaData data) {
		return (data.getCurrentLocation().getBlockX() == data.getFromLocation().getBlockX() ||
				data.getCurrentLocation().getBlockX() == data.getToLocation().getBlockX() ||
				data.getCurrentLocation().getBlockY() == data.getFromLocation().getBlockY() ||
				data.getCurrentLocation().getBlockY() == data.getToLocation().getBlockY()||
				data.getCurrentLocation().getBlockZ() == data.getFromLocation().getBlockZ() ||
				data.getCurrentLocation().getBlockZ() == data.getToLocation().getBlockZ());
	}

}
