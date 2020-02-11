package com.github.razz0991.construkt.shapes.parameters;

public class IntegerShapeParameter implements ShapeParameter<Integer>{
	
	private int value;
	private final boolean useLimit;
	private final int minValue;
	private final int maxValue;
	
	public IntegerShapeParameter(int value) {
		this.value = value;
		useLimit = false;
		minValue = 0;
		maxValue = 0;
	}
	
	public IntegerShapeParameter(int value, int min, int max) {
		this.value = value;
		useLimit = true;
		minValue = min;
		maxValue = max;
	}

	@Override
	public Integer getParameter() {
		return value;
	}

	@Override
	public void setParameter(final Integer parameter) {
		int toSet = parameter;
		if (useLimit) {
			if (toSet < minValue)
				toSet = minValue;
			else if (toSet > maxValue)
				toSet = maxValue;
		}
		value = toSet;
	}

}
