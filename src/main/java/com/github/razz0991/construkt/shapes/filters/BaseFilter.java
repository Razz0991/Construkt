package com.github.razz0991.construkt.shapes.filters;

import java.util.Map;

import com.github.razz0991.construkt.shapes.AreaData;
import com.github.razz0991.construkt.shapes.parameters.AxisShapeParameter;
import com.github.razz0991.construkt.shapes.parameters.BooleanShapeParameter;
import com.github.razz0991.construkt.shapes.parameters.IntegerShapeParameter;
import com.github.razz0991.construkt.shapes.parameters.ShapeParameter;

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
	public abstract Map<String, ShapeParameter<?>> getParameters();
	/**
	 * A check to see if the current position should allow a 
	 * block placement based on this filter.
	 * @param data The area data
	 * @return true if a block should be placed
	 */
	public abstract boolean checkCondition(final AreaData data, final Map<String, ShapeParameter<?>> parameters);
	
	protected boolean parseBooleanParameter(Map<String, ShapeParameter<?>> parameters, String name, boolean defaultValue) {
		ShapeParameter<?> par = parameters.get(parseParameterName(name));
		if (par instanceof BooleanShapeParameter)
			return ((BooleanShapeParameter)par).getParameter();
		return defaultValue;
	}
	
	protected int parseIntegerParameter(Map<String, ShapeParameter<?>> parameters, String name, int defaultValue) {
		ShapeParameter<?> par = parameters.get(parseParameterName(name));
		if (par instanceof IntegerShapeParameter)
			return ((IntegerShapeParameter)par).getParameter();
		return defaultValue;
	}
	
	protected char parseAxisParameter(Map<String, ShapeParameter<?>> parameters, String name, char defaultValue) {
		ShapeParameter<?> par = parameters.get(parseParameterName(name));
		if (par instanceof AxisShapeParameter)
			return ((AxisShapeParameter)par).getParameter();
		return defaultValue;
	}
	
	protected String parseParameterName(String parameterName) {
		return getFilterName() + "_" + parameterName;
	}
}
