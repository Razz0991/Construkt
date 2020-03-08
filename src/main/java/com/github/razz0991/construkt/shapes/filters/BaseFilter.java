package com.github.razz0991.construkt.shapes.filters;

import java.util.Map;

import com.github.razz0991.construkt.shapes.AreaData;
import com.github.razz0991.construkt.shapes.parameters.AxisCktParameter;
import com.github.razz0991.construkt.shapes.parameters.BooleanCktParameter;
import com.github.razz0991.construkt.shapes.parameters.IntegerCktParameter;
import com.github.razz0991.construkt.shapes.parameters.CktParameter;

/*  Construkt Bukkit plugin for Minecraft.
 *  Copyright (C) 2020 _Razz_
 *
 *  Full disclaimer in Construkt.java
 */
public abstract class BaseFilter {
	
	/**
	 * Gets the name of the filter
	 * @return The filters name, needs to be lowercase.
	 */
	public abstract String getFilterName();
	
	/**
	 * Gets this filters parameters.
	 * @return A map containing the name and value of 
	 * parameters for this filter.
	 */
	public abstract Map<String, CktParameter<?>> getParameters();
	/**
	 * A check to see if the current position should allow a 
	 * block placement based on this filter.
	 * @param data The area data
	 * @return true if a block should be placed
	 */
	public abstract boolean checkCondition(final AreaData data, final Map<String, CktParameter<?>> parameters);
	
	protected boolean parseBooleanParameter(Map<String, CktParameter<?>> parameters, String name, boolean defaultValue) {
		CktParameter<?> par = parameters.get(parseParameterName(name));
		if (par instanceof BooleanCktParameter)
			return ((BooleanCktParameter)par).getParameter();
		return defaultValue;
	}
	
	protected int parseIntegerParameter(Map<String, CktParameter<?>> parameters, String name, int defaultValue) {
		CktParameter<?> par = parameters.get(parseParameterName(name));
		if (par instanceof IntegerCktParameter)
			return ((IntegerCktParameter)par).getParameter();
		return defaultValue;
	}
	
	protected char parseAxisParameter(Map<String, CktParameter<?>> parameters, String name, char defaultValue) {
		CktParameter<?> par = parameters.get(parseParameterName(name));
		if (par instanceof AxisCktParameter)
			return ((AxisCktParameter)par).getParameter();
		return defaultValue;
	}
	
	protected String parseParameterName(String parameterName) {
		return getFilterName() + "_" + parameterName;
	}
}
