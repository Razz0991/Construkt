package com.github.razz0991.construkt.parameters;

/*  Construkt Bukkit plugin for Minecraft.
 *  Copyright (C) 2020 _Razz_
 *
 *  Full disclaimer in Construkt.java
 */
public class IntegerCktParameter implements CktParameter<Integer>{
	
	private int value;
	private final boolean useLimit;
	private final int minValue;
	private final int maxValue;
	
	public IntegerCktParameter(int value) {
		this.value = value;
		useLimit = false;
		minValue = 0;
		maxValue = 0;
	}
	
	public IntegerCktParameter(int value, int min, int max) {
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
	
	/**
	 * Checks if this parameter is limited.
	 * @return true if the parameter has been limited
	 */
	public boolean isLimited() {
		return useLimit;
	}
	
	/**
	 * Gets the minimum value of this parameter.
	 * @return The minimum value
	 */
	public int getMinValue() {
		return minValue;
	}

	/**
	 * Gets the maximum value of this parameter.
	 * @return The maximum value
	 */
	public int getMaxValue() {
		return maxValue;
	}

}
