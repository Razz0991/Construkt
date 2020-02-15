package com.github.razz0991.construkt.shapes.filters;

import java.util.HashMap;
import java.util.Map;

import com.github.razz0991.construkt.shapes.AreaData;
import com.github.razz0991.construkt.shapes.parameters.IntegerShapeParameter;
import com.github.razz0991.construkt.shapes.parameters.ShapeParameter;

public class RandomFilter extends BaseFilter {
	
	private final String chanceName = "chance";
	private final int chanceDefault = 50;

	@Override
	public String getFilterName() {
		return "random";
	}

	@Override
	public Map<String, ShapeParameter<?>> getParameters() {
		Map<String, ShapeParameter<?>> pars = new HashMap<String, ShapeParameter<?>>();
		pars.put(chanceName, new IntegerShapeParameter(chanceDefault, 1, 99));
		return pars;
	}

	@Override
	public boolean checkCondition(AreaData data, Map<String, ShapeParameter<?>> parameters) {
		double chance = parseIntegerParameter(parameters.get(parseParameterName(chanceName)), chanceDefault) / 100d;
		
		return Math.random() <= chance;
	}

}
