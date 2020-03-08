package com.github.razz0991.construkt.shapes.filters;

import java.util.HashMap;
import java.util.Map;

import com.github.razz0991.construkt.shapes.AreaData;
import com.github.razz0991.construkt.shapes.parameters.BooleanCktParameter;
import com.github.razz0991.construkt.shapes.parameters.IntegerCktParameter;
import com.github.razz0991.construkt.shapes.parameters.CktParameter;

/*  Construkt Bukkit plugin for Minecraft.
 *  Copyright (C) 2020 _Razz_
 *
 *  Full disclaimer in Construkt.java
 */
public class CheckeredFilter extends BaseFilter {
	
	private static String invertName = "invert";
	private static boolean invertDefault = false;
	private static String sizeName = "size";
	private static int sizeDefault = 1;
	
	@Override
	public String getFilterName() {
		return "checkered";
	}

	@Override
	public Map<String, CktParameter<?>> getParameters() {
		Map<String, CktParameter<?>> pars = new HashMap<String, CktParameter<?>>();
		pars.put(invertName, new BooleanCktParameter(invertDefault));
		pars.put(sizeName, new IntegerCktParameter(sizeDefault, 1, 20));
		return pars;
	}

	@Override
	public boolean checkCondition(AreaData data, Map<String, CktParameter<?>> parameters) {
		int checkerSize = parseIntegerParameter(parameters, sizeName, sizeDefault);
		boolean yEven = isChecker(data.getCurrentRelativeY(), checkerSize);
		if (parseBooleanParameter(parameters, invertName, invertDefault))
			yEven = !yEven;
		
		if (yEven) {
			if (!isChecker(data.getCurrentRelativeX(), checkerSize)) {
				if (!isChecker(data.getCurrentRelativeZ(), checkerSize))
					return true;
			}
			else {
				if (isChecker(data.getCurrentRelativeZ(), checkerSize))
					return true;
			}
		}
		else {
			if (!isChecker(data.getCurrentRelativeX(), checkerSize)) {
				if (isChecker(data.getCurrentRelativeZ(), checkerSize))
					return true;
			}
			else {
				if (!isChecker(data.getCurrentRelativeZ(), checkerSize))
					return true;
			}
		}
		return false;
	}
	
	private boolean isChecker(int number, int checkerSize) {
		int value = number / checkerSize;
		return (value % 2 == 0);
	}
}
