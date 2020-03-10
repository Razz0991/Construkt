package com.github.razz0991.construkt.filters;

import org.bukkit.util.noise.SimplexOctaveGenerator;

import com.github.razz0991.construkt.parameters.BooleanCktParameter;
import com.github.razz0991.construkt.parameters.IntegerCktParameter;
import com.github.razz0991.construkt.shapes.AreaData;

/*  Construkt Bukkit plugin for Minecraft.
 *  Copyright (C) 2020 _Razz_
 *
 *  Full disclaimer in Construkt.java
 */
public class NoiseFilter extends BaseFilter {

	private final String octaveName = "octaves";
	private final int octaveValue = 8;
	private final String scaleName = "scale";
	private final int scaleDefault = 5;
	private final String limitName = "limit";
	private final int limitDefault = 50;
	private final String invertName = "invert";
	private final boolean invertDefault = false;
	
	private SimplexOctaveGenerator gen;
	
	public NoiseFilter() {
		super();
		gen = new SimplexOctaveGenerator(0, 
				getIntegerParameter(octaveName, octaveValue));
		gen.setScale(getIntegerParameter(scaleName, scaleDefault) / 100d);
		
		IntegerCktParameter octaves = new IntegerCktParameter(octaveValue, 1, 8);
		IntegerCktParameter scale = new IntegerCktParameter(scaleDefault, 1, 10);
		IntegerCktParameter limit = new IntegerCktParameter(limitDefault, 1, 99);
		BooleanCktParameter invert = new BooleanCktParameter(invertDefault);
		parameters.put(octaveName, octaves);
		parameters.put(scaleName, scale);
		parameters.put(limitName, limit);
		parameters.put(invertName, invert);
	}

	@Override
	public String getName() {
		return "noise";
	}

	@Override
	public boolean checkCondition(AreaData data) {
		double limit = getIntegerParameter(limitName, limitDefault) / 100d;
		
		boolean output = false;
		if (!getBooleanParameter(invertName, invertDefault))
			output = getNoise(gen, data) <= limit;
		else
			output = getNoise(gen, data) >= limit;
		
		return output;
	}
	
	// Gets the noise from the current location in the loop
	private double getNoise(SimplexOctaveGenerator generator, AreaData data) {
		double noise = generator.noise(data.getCurrentLocation().getX(), 
				data.getCurrentLocation().getY(), data.getCurrentLocation().getZ(), 1, 16, true);
		noise = (noise + 1) / 2;
		return noise;
	}

}
