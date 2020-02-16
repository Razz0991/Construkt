package com.github.razz0991.construkt.shapes.filters;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.util.noise.SimplexOctaveGenerator;

import com.github.razz0991.construkt.shapes.AreaData;
import com.github.razz0991.construkt.shapes.parameters.BooleanShapeParameter;
import com.github.razz0991.construkt.shapes.parameters.IntegerShapeParameter;
import com.github.razz0991.construkt.shapes.parameters.ShapeParameter;

public class NoiseFilter extends BaseFilter {
	
	private final String octaveName = "octaves";
	private final int octaveValue = 8;
	private final String scaleName = "scale";
	private final int scaleDefault = 5;
	private final String limitName = "limit";
	private final int limitDefault = 50;
	private final String invertName = "invert";
	private final boolean invertDefault = false;

	@Override
	public String getFilterName() {
		return "noise";
	}

	@Override
	public Map<String, ShapeParameter<?>> getParameters() {
		Map<String, ShapeParameter<?>> pars = new HashMap<String, ShapeParameter<?>>();
		IntegerShapeParameter octaves = new IntegerShapeParameter(octaveValue, 1, 8);
		IntegerShapeParameter scale = new IntegerShapeParameter(scaleDefault, 1, 10);
		IntegerShapeParameter limit = new IntegerShapeParameter(limitDefault, 1, 99);
		BooleanShapeParameter invert = new BooleanShapeParameter(invertDefault);
		pars.put(octaveName, octaves);
		pars.put(scaleName, scale);
		pars.put(limitName, limit);
		pars.put(invertName, invert);
		return pars;
	}

	@Override
	public boolean checkCondition(AreaData data, Map<String, ShapeParameter<?>> parameters) {
		SimplexOctaveGenerator gen = new SimplexOctaveGenerator(0, 
				parseIntegerParameter(parameters, octaveName, octaveValue));
		gen.setScale(parseIntegerParameter(parameters, scaleName, scaleDefault) / 100d);
		
		double limit = parseIntegerParameter(parameters, limitName, limitDefault) / 100d;
		
		boolean output = false;
		if (!parseBooleanParameter(parameters, invertName, invertDefault))
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
