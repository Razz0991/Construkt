package com.github.razz0991.construkt.shapes;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.block.data.BlockData;

import com.github.razz0991.construkt.shapes.parameters.BooleanShapeParameter;
import com.github.razz0991.construkt.shapes.parameters.ShapeParameter;

public class HollowCuboidShape extends BaseShape{
	
	private final boolean borderModeDefault = false;
	private final String borderModeName = "border_mode";

	@Override
	public Map<String, ShapeParameter<?>> getDefaultParameters() {
		Map<String, ShapeParameter<?>> parameters = new HashMap<String, ShapeParameter<?>>();
		parameters.put(borderModeName, new BooleanShapeParameter(borderModeDefault));
		return parameters;
	}

	@Override
	public boolean generateShape(Location firstPoint, Location secondPoint, Map<String, ShapeParameter<?>> parameters, BlockData blockData) {
		AreaData data = new AreaData(firstPoint, secondPoint);
		
		do {
			if (canPlace(data.getCurrentLocation(), parameters)) {
				if ((parseBooleanShapeParameter(parameters.get(borderModeName), true) &&
						isBorder(data)) || (!parseBooleanShapeParameter(parameters.get(borderModeName), borderModeDefault) &&
								isEdge(data)))
					setBlock(blockData, data.getCurrentLocation());
			}
			
			data.incrementLoop();
		} while (!data.isLoopFinished());
		
		if (canPlace(data.getCurrentLocation(), parameters))
			setBlock(blockData, data.getCurrentLocation());
		
		return true;
	}
	
	private boolean isEdge(AreaData data) {
		return (data.getCurrentLocation().getBlockX() == data.getFromLocation().getBlockX() ||
				data.getCurrentLocation().getBlockX() == data.getToLocation().getBlockX() ||
				data.getCurrentLocation().getBlockY() == data.getFromLocation().getBlockY() ||
				data.getCurrentLocation().getBlockY() == data.getToLocation().getBlockY() ||
				data.getCurrentLocation().getBlockZ() == data.getFromLocation().getBlockZ() ||
				data.getCurrentLocation().getBlockZ() == data.getToLocation().getBlockZ());
	}
	
	private boolean isBorder(AreaData data) {
		boolean isXEdge = (data.getCurrentLocation().getBlockX() == data.getFromLocation().getBlockX() ||
				data.getCurrentLocation().getBlockX() == data.getToLocation().getBlockX());
		boolean isYEdge = (data.getCurrentLocation().getBlockY() == data.getFromLocation().getBlockY() ||
				data.getCurrentLocation().getBlockY() == data.getToLocation().getBlockY());
		boolean isZEdge = (data.getCurrentLocation().getBlockZ() == data.getFromLocation().getBlockZ() ||
				data.getCurrentLocation().getBlockZ() == data.getToLocation().getBlockZ());
		
		return ((isXEdge && isZEdge && !isYEdge) ||
				(isXEdge && isYEdge && !isZEdge) || 
				(isZEdge && isYEdge && !isXEdge) ||
				(isXEdge && isYEdge && isZEdge));
	}

}
