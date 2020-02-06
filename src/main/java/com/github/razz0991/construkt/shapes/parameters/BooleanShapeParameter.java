package com.github.razz0991.construkt.shapes.parameters;

public class BooleanShapeParameter implements ShapeParameter<Boolean>{
	
	private boolean value = false;
	
	public BooleanShapeParameter(boolean value) {
		this.value = value;
	}
	
	@Override
	public Boolean getParameter() {
		return value;
	}

	@Override
	public void setParameter(Boolean parameter) {
		value = parameter;
	}

}
