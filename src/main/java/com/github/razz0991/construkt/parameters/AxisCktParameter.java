package com.github.razz0991.construkt.parameters;

import java.util.ArrayList;
import java.util.List;

public class AxisCktParameter implements CktParameter<Character>{
	
	private char axis = 'y';
	private List<String> possibleValues;
	
	public AxisCktParameter(Character defaultValue) {
		setParameter(defaultValue);
		possibleValues = new ArrayList<String>();
		possibleValues.add("x");
		possibleValues.add("y");
		possibleValues.add("z");
	}

	@Override
	public Character getParameter() {
		return axis;
	}

	@Override
	public void setParameter(Character parameter) {
		if (parameter == 'x' || parameter == 'y' || parameter == 'z')
			axis = parameter;
	}

	@Override
	public List<String> getAutoComplete(String comparitor) {
		List<String> out = new ArrayList<String>();
		for (String val : possibleValues) {
			if (val.startsWith(comparitor))
				out.add(val);
		}
		return out;
	}

}
