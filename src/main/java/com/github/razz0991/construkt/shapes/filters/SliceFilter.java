package com.github.razz0991.construkt.shapes.filters;

import java.util.HashMap;
import java.util.Map;

import com.github.razz0991.construkt.shapes.AreaData;
import com.github.razz0991.construkt.shapes.parameters.AxisShapeParameter;
import com.github.razz0991.construkt.shapes.parameters.BooleanShapeParameter;
import com.github.razz0991.construkt.shapes.parameters.IntegerShapeParameter;
import com.github.razz0991.construkt.shapes.parameters.ShapeParameter;

/*  Construkt Bukkit plugin for Minecraft.
 *  Copyright (C) 2020 _Razz_
 *
 *  Full disclaimer in Construkt.java
 */
public class SliceFilter extends BaseFilter {
	
	private final String axisName = "axis";
	private final char axisDefault = 'y';
	private final String slicePercentName = "percent";
	private final int slicePercentDefault = 50;
	private final String invertName = "invert";
	private final boolean invertDefault = false;

	@Override
	public String getFilterName() {
		return "slice";
	}

	@Override
	public Map<String, ShapeParameter<?>> getParameters() {
		Map<String, ShapeParameter<?>> pars = new HashMap<String, ShapeParameter<?>>();
		pars.put(axisName, new AxisShapeParameter(axisDefault));
		pars.put(slicePercentName, new IntegerShapeParameter(slicePercentDefault, 1, 99));
		pars.put(invertName, new BooleanShapeParameter(invertDefault));
		return pars;
	}

	@Override
	public boolean checkCondition(AreaData data, Map<String, ShapeParameter<?>> parameters) {
		char axis = parseAxisParameter(parameters, axisName, axisDefault);
		double perc = parseIntegerParameter(parameters, slicePercentName, slicePercentDefault) / 100d;
		boolean invert = parseBooleanParameter(parameters, invertName, invertDefault);
		double length = 0;
		int axisDist = 0;
		
		if (axis == 'y') {
			length = data.getYSize() * perc;
			axisDist = data.getCurrentRelativeY();
		}
		else if (axis == 'x') {
			length = data.getXSize() * perc;
			axisDist = data.getCurrentRelativeX();
		}
		else if (axis == 'z') {
			length = data.getZSize() * perc;
			axisDist = data.getCurrentRelativeZ();
		}
		
		if (!invert) {
			return axisDist <= length;
		}
		else {
			return axisDist >= length;
		}
	}

}
