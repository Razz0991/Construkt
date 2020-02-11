package com.github.razz0991.construkt.shapes;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.block.data.BlockData;
import org.bukkit.util.noise.PerlinOctaveGenerator;

import com.github.razz0991.construkt.shapes.parameters.IntegerShapeParameter;
import com.github.razz0991.construkt.shapes.parameters.ShapeParameter;
/*  Construkt Bukkit plugin for Minecraft.
 *  Copyright (C) 2020 _Razz_
 *
 *  Full disclaimer in Construkt.java
 */
public class TerrainShape extends BaseShape {
	
	private String octaveName = "octaves";
	private int octaveValue = 8;
	private String scaleName = "scale";
	private int scaleDefault = 5;

	@Override
	public Map<String, ShapeParameter<?>> getDefaultParameters() {
		Map<String, ShapeParameter<?>> pars = new HashMap<String, ShapeParameter<?>>();
		IntegerShapeParameter octaves = new IntegerShapeParameter(octaveValue, 1, 8);
		IntegerShapeParameter scale = new IntegerShapeParameter(scaleDefault, 1, 10);
		pars.put(octaveName, octaves);
		pars.put(scaleName, scale);
		return pars;
	}

	@Override
	public boolean generateShape(Location firstPoint, Location secondPoint, Map<String, ShapeParameter<?>> parameters,
			BlockData blockData) {
		AreaData data = new AreaData(firstPoint, secondPoint);
		PerlinOctaveGenerator gen = new PerlinOctaveGenerator(0L, parseIntegerShapeParameter(parameters.get(octaveName), 8));
		gen.setScale(parseIntegerShapeParameter(parameters.get(scaleName), scaleDefault) / 100.0d);
		
		do {
			if (getNoise(gen, data) >= data.getCurrentNormalizedY())
				if (canPlace(data.getCurrentLocation(), parameters))
					setBlock(blockData, data.getCurrentLocation());
			
			data.incrementLoop();
		} while (!data.isLoopFinished());
		
		if (getNoise(gen, data) >= data.getCurrentNormalizedY())
			if (canPlace(data.getCurrentLocation(), parameters))
				setBlock(blockData, data.getCurrentLocation());
		
		return true;
	}
	
	private double getNoise(PerlinOctaveGenerator generator, AreaData data) {
		double noise = generator.noise(data.getCurrentLocation().getX(), data.getCurrentLocation().getZ(), 1, 16, true);
		noise = (noise + 1) / 2;
		double height = noise * data.getYSize();
		
		return height;
	}

}
