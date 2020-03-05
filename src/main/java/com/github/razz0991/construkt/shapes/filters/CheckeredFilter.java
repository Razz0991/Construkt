package com.github.razz0991.construkt.shapes.filters;

import java.util.HashMap;
import java.util.Map;

import com.github.razz0991.construkt.shapes.AreaData;
import com.github.razz0991.construkt.shapes.parameters.BooleanShapeParameter;
import com.github.razz0991.construkt.shapes.parameters.IntegerShapeParameter;
import com.github.razz0991.construkt.shapes.parameters.ShapeParameter;

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
	public Map<String, ShapeParameter<?>> getParameters() {
		Map<String, ShapeParameter<?>> pars = new HashMap<String, ShapeParameter<?>>();
		pars.put(invertName, new BooleanShapeParameter(invertDefault));
		pars.put(sizeName, new IntegerShapeParameter(sizeDefault, 1, 20));
		return pars;
	}

	@Override
	public boolean checkCondition(AreaData data, Map<String, ShapeParameter<?>> parameters) {
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
