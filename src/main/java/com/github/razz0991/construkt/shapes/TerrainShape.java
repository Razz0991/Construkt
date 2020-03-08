package com.github.razz0991.construkt.shapes;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.block.data.BlockData;
import org.bukkit.util.noise.SimplexOctaveGenerator;

import com.github.razz0991.construkt.shapes.filters.BaseFilter;
import com.github.razz0991.construkt.shapes.parameters.IntegerCktParameter;
import com.github.razz0991.construkt.shapes.parameters.CktParameter;

/*  Construkt Bukkit plugin for Minecraft.
 *  Copyright (C) 2020 _Razz_
 *
 *  Full disclaimer in Construkt.java
 */
public class TerrainShape extends BaseShape {
	
	private final String octaveName = "octaves";
	private final int octaveValue = 8;
	private final String scaleName = "scale";
	private final int scaleDefault = 5;

	@Override
	public Map<String, CktParameter<?>> getDefaultParameters() {
		Map<String, CktParameter<?>> pars = new HashMap<String, CktParameter<?>>();
		IntegerCktParameter octaves = new IntegerCktParameter(octaveValue, 1, 8);
		IntegerCktParameter scale = new IntegerCktParameter(scaleDefault, 1, 10);
		pars.put(octaveName, octaves);
		pars.put(scaleName, scale);
		return pars;
	}

	@Override
	public boolean generateShape(Location firstPoint, Location secondPoint, Map<String, CktParameter<?>> parameters,
			BlockData blockData, BaseFilter[] filters) {
		boolean reversed = blockData == null;
		final AreaData data = new AreaData(firstPoint, secondPoint, reversed);
		final SimplexOctaveGenerator gen = new SimplexOctaveGenerator(0L, parseIntegerShapeParameter(parameters.get(octaveName), 8));
		gen.setScale(parseIntegerShapeParameter(parameters.get(scaleName), scaleDefault) / 100.0d);
		
		data.createFillTask(new Runnable() {
			
			@Override
			public void run() {
				do {
					if (getNoise(gen, data) >= data.getCurrentRelativeY())
						if (canPlace(data, parameters, filters))
							setBlock(blockData, data.getCurrentLocation());
					
					boolean shouldWait = data.incrementLoop();
					if (shouldWait)
						return;
				} while (!data.isLoopFinished());
			}
		});
		
		return true;
	}
	
	// Gets the noise from the current location in the loop
	private double getNoise(SimplexOctaveGenerator generator, AreaData data) {
		double noise = generator.noise(data.getCurrentLocation().getX(), data.getCurrentLocation().getZ(), 1, 16, true);
		noise = (noise + 1) / 2;
		double height = noise * data.getYSize();
		
		return height;
	}

}
