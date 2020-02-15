package com.github.razz0991.construkt.shapes.parameters;

public class AxisShapeParameter implements ShapeParameter<Character>{
	
	private char axis = 'y';
	
	public AxisShapeParameter(Character defaultValue) {
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
