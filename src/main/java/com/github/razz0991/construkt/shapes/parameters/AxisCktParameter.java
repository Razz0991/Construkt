package com.github.razz0991.construkt.shapes.parameters;

public class AxisCktParameter implements CktParameter<Character>{
	
	private char axis = 'y';
	
	public AxisCktParameter(Character defaultValue) {
		setParameter(defaultValue);
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

}
