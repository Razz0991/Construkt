package com.github.razz0991.construkt.shapes.parameters;

public class IntegerShapeParameter implements ShapeParameter<Integer>{
	
	private int value;
	
	public IntegerShapeParameter(int value) {
		this.value = value;
	}

	@Override
	public Integer getParameter() {
		return value;
	}

	@Override
	public void setParameter(Integer parameter) {
		value = parameter;
	}

}
