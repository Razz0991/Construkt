package com.github.razz0991.construkt.shapes.filters;

import java.util.HashMap;
import java.util.Map;

import com.github.razz0991.construkt.shapes.AreaData;
import com.github.razz0991.construkt.shapes.parameters.BooleanShapeParameter;
import com.github.razz0991.construkt.shapes.parameters.ShapeParameter;

public class CheckeredFilter extends BaseFilter {
	
	private static String invertName = "invert";
	private static boolean invertDefault = false;
	
	@Override
	public String getFilterName() {
		return "checkered";
	}

	@Override
	public Map<String, ShapeParameter<?>> getParameters() {
		Map<String, ShapeParameter<?>> pars = new HashMap<String, ShapeParameter<?>>();
		pars.put(invertName, new BooleanShapeParameter(invertDefault));
		return pars;
	}

	@Override
	public boolean checkCondition(AreaData data, Map<String, ShapeParameter<?>> parameters) {
		boolean yEven = isEven(data.getCurrentRelativeY());
		if (parseBooleanParameter(parameters.get(parseParameterName(invertName)), invertDefault))
			yEven = !yEven;
		
		if (yEven) {
			if (!isEven(data.getCurrentRelativeX())) {
				if (!isEven(data.getCurrentRelativeZ()))
					return true;
			}
			else {
				if (isEven(data.getCurrentRelativeZ()))
					return true;
			}
		}
		else {
			if (!isEven(data.getCurrentRelativeX())) {
				if (isEven(data.getCurrentRelativeZ()))
					return true;
			}
			else {
				if (!isEven(data.getCurrentRelativeZ()))
					return true;
			}
		}
		return false;
	}
	
	private boolean isEven(int number) {
		return (number % 2 == 0);
	}
}
