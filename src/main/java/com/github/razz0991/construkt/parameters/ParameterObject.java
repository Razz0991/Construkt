package com.github.razz0991.construkt.parameters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ParameterObject {
	
	protected Map<String, CktParameter<?>> parameters;
	
	public ParameterObject() {
		parameters = new HashMap<String, CktParameter<?>>();
	}
	
	/**
	 * The name of the object, should be in lower case
	 * @return The name
	 */
	public abstract String getName();
	
	/**
	 * Checks if this object has a parameter
	 * @param parameterName The name of the parameter
	 * @return true if the parameter exists
	 */
	public boolean hasParameter(String parameterName) {
		return parameters.containsKey(repairParameterName(parameterName));
	}
	
	/**
	 * Gets all parameters in this object
	 * @return A {@link List} containing all parameters in this object
	 */
	public List<String> getParameterNames(){
		List<String> output = new ArrayList<String>();
		for (String name : parameters.keySet()) {
			output.add(parseParameterName(name));
		}
		return output;
	}
	
	/**
	 * Gets a parameter from this object
	 * @param parameterName The name of the parameter
	 * @return A {@link CktParameter} that can be modified
	 */
	public CktParameter<?> getParameter(String parameterName) {
		String actualName = repairParameterName(parameterName);
		
		if (parameters.containsKey(actualName)) {
			return parameters.get(actualName);
		}
		return null;
	}
	
	/**
	 * Gets a boolean parameter from this objects parameters
	 * @param name The name of the parameter
	 * @param defaultValue The default value
	 * @return The boolean value of this parameter or the defaultValue
	 * if not set
	 */
	protected boolean getBooleanParameter(String name, boolean defaultValue) {
		CktParameter<?> par = parameters.get(repairParameterName(name));
		if (par instanceof BooleanCktParameter)
			return ((BooleanCktParameter)par).getParameter();
		return defaultValue;
	}
	
	/**
	 * Gets a integer parameter from this objects parameters
	 * @param name The name of the parameter
	 * @param defaultValue The default value
	 * @return The integer value of this parameter or the defaultValue
	 * if not set
	 */
	protected int getIntegerParameter(String name, int defaultValue) {
		CktParameter<?> par = parameters.get(repairParameterName(name));
		if (par instanceof IntegerCktParameter)
			return ((IntegerCktParameter)par).getParameter();
		return defaultValue;
	}
	
	/**
	 * Gets an axis parameter from this objects parameters
	 * @param name The name of the parameter
	 * @param defaultValue The default value
	 * @return The axis value of this parameter or the defaultValue
	 * if not set
	 */
	protected char getAxisParameter(String name, char defaultValue) {
		CktParameter<?> par = parameters.get(repairParameterName(name));
		if (par instanceof AxisCktParameter)
			return ((AxisCktParameter)par).getParameter();
		return defaultValue;
	}
	
	/**
	 * Gets a long parameter from this objects parameters
	 * @param name The name of the parameter
	 * @param defaultValue The default value
	 * @return The long value of this parameter or the defaultValue
	 * if not set
	 */
	protected long getLongParameter(String name, long defaultValue) {
		CktParameter<?> par = parameters.get(repairParameterName(name));
		if (par instanceof LongCktParameter) {
			return ((LongCktParameter)par).getParameter();
		}
		return defaultValue;
	}
	
	/**
	 * Gets a list parameter from this objects parameters
	 * @param name The name of the parameter
	 * @param defaultValue The default value
	 * @return The selected item from the list or the defaultValue
	 * if not set
	 */
	protected String getListParameter(String name, String defaultValue) {
		CktParameter<?> par = parameters.get(repairParameterName(name));
		if (par instanceof ListCktParameter) {
			return ((ListCktParameter)par).getParameter();
		}
		return defaultValue;
	}
	
	// Gets the name of the parameter as seen by the player
	protected String parseParameterName(String parameterName) {
		return getName() + "." + parameterName;
	}
	
	// Gets the actual name of the parameter without the extra stuff
	protected String repairParameterName(String parameterName) {
		return parameterName.replace(getName() + ".", "");
	}

}
