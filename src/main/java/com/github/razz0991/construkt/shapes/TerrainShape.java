package com.github.razz0991.construkt.shapes;

import org.bukkit.Location;
import org.bukkit.block.data.BlockData;
import org.bukkit.util.noise.SimplexOctaveGenerator;

import com.github.razz0991.construkt.filters.BaseFilter;
import com.github.razz0991.construkt.parameters.IntegerCktParameter;

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
	
	public TerrainShape() {
		super();
		parameters.put(octaveName, new IntegerCktParameter(octaveValue, 1, 8));
		parameters.put(scaleName, new IntegerCktParameter(scaleDefault, 1, 10));
	}

	@Override
	public String getName() {
		return "terrain";
	}

	@Override
	public boolean generateShape(Location firstPoint, Location secondPoint,
			BlockData blockData, BaseFilter[] filters) {
		boolean reversed = blockData == null;
		final AreaData data = new AreaData(firstPoint, secondPoint, reversed);
		final SimplexOctaveGenerator gen = new SimplexOctaveGenerator(0L, getIntegerParameter(octaveName, 8));
		gen.setScale(getIntegerParameter(scaleName, scaleDefault) / 100.0d);
		
		data.createFillTask(new Runnable() {
			
			@Override
			public void run() {
				do {
					if (getNoise(gen, data) >= data.getCurrentRelativeY())
						if (canPlace(data, filters))
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
