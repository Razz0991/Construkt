package com.github.razz0991.construkt.shapes.filters;

import java.util.HashMap;
import java.util.Map;

import com.github.razz0991.construkt.shapes.AreaData;
import com.github.razz0991.construkt.shapes.parameters.AxisCktParameter;
import com.github.razz0991.construkt.shapes.parameters.BooleanCktParameter;
import com.github.razz0991.construkt.shapes.parameters.IntegerCktParameter;
import com.github.razz0991.construkt.shapes.parameters.CktParameter;

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
	public Map<String, CktParameter<?>> getParameters() {
		Map<String, CktParameter<?>> pars = new HashMap<String, CktParameter<?>>();
		pars.put(axisName, new AxisCktParameter(axisDefault));
		pars.put(slicePercentName, new IntegerCktParameter(slicePercentDefault, 1, 99));
		pars.put(invertName, new BooleanCktParameter(invertDefault));
		return pars;
	}

	@Override
	public boolean checkCondition(AreaData data, Map<String, CktParameter<?>> parameters) {
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
