package com.github.razz0991.construkt.filters;

import com.github.razz0991.construkt.parameters.AxisCktParameter;
import com.github.razz0991.construkt.parameters.BooleanCktParameter;
import com.github.razz0991.construkt.parameters.IntegerCktParameter;
import com.github.razz0991.construkt.shapes.AreaData;

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
	
	public SliceFilter() {
		super();
		parameters.put(axisName, new AxisCktParameter(axisDefault));
		parameters.put(slicePercentName, new IntegerCktParameter(slicePercentDefault, 1, 99));
		parameters.put(invertName, new BooleanCktParameter(invertDefault));
	}

	@Override
	public String getName() {
		return "slice";
	}

	@Override
	public boolean checkCondition(AreaData data) {
		char axis = getAxisParameter(axisName, axisDefault);
		double perc = getIntegerParameter(slicePercentName, slicePercentDefault) / 100d;
		boolean invert = getBooleanParameter(invertName, invertDefault);
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
