package com.github.razz0991.construkt.shapes;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;

import com.github.razz0991.construkt.shapes.parameters.IntegerShapeParameter;
import com.github.razz0991.construkt.shapes.parameters.ShapeParameter;

public class Overlay extends BaseShape {
	
	private String depthName = "depth";
	private int depthDefault = 1;

	@Override
	public Map<String, ShapeParameter<?>> getDefaultParameters() {
		Map<String, ShapeParameter<?>> params = new HashMap<String, ShapeParameter<?>>();
		IntegerShapeParameter depth = new IntegerShapeParameter(depthDefault, 1, 10);
		params.put(depthName, depth);
		return params;
	}

	@Override
	public boolean generateShape(Location firstPoint, Location secondPoint, Map<String, ShapeParameter<?>> parameters,
			BlockData blockData) {
		
		AreaData data = new AreaData(firstPoint, secondPoint, new char[] {'x', 'z'}, new int[] {1, 1});
		data.setCurrentY(data.getSecondPoint().getBlockY());
		
		do {
			Location cur = data.getCurrentLocation();
			int min = data.getFromLocation().getBlockY();
			
			while (cur.getBlock().getType() == Material.AIR && cur.getBlockY() >= min) {
				cur.setY(cur.getY() - 1);
			}
			
			if (cur.getBlock().getType() != Material.AIR) {
				if (parseBooleanShapeParameter(parameters.get("place_in_air"), true)) {
					int startY = cur.getBlockY() + 1;
					int endY = startY + parseIntegerShapeParameter(parameters.get(depthName), depthDefault);
					if (endY > data.getToLocation().getBlockY())
						endY = data.getToLocation().getBlockY();
					for (int y = startY; y < endY; y++) {
						cur.setY(y);
						setBlock(blockData, cur);
					}
				}
				else {
					int startY = cur.getBlockY();
					int endY = startY - parseIntegerShapeParameter(parameters.get(depthName), depthDefault);
					if (endY < data.getFromLocation().getBlockY())
						endY = data.getFromLocation().getBlockY();
					
					for (int y = startY; y > endY; y--) {
						cur.setY(y);
						if (canPlace(cur, parameters)) {
							setBlock(blockData, cur);
						}
					}
				}
			}
			
			data.incrementLoop();
		} while(!data.isLoopFinished());
		
		return true;
	}

}