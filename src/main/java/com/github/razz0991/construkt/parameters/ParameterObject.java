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
	
	public abstract String getName();
	
	public boolean hasParameter(String parameterName) {
		return parameters.containsKey(repairParameterName(parameterName));
	}

	public List<String> getParameterNames(){
		List<String> output = new ArrayList<String>();
		for (String name : parameters.keySet()) {
			output.add(parseParameterName(name));
		}
		return output;
	}
	
	public CktParameter<?> getParameter(String parameterName) {
		String actualName = repairParameterName(parameterName);
		
		if (parameters.containsKey(actualName)) {
			return parameters.get(actualName);
		}
		return null;
	}
	
	protected boolean parseBooleanParameter(String name, boolean defaultValue) {
		CktParameter<?> par = parameters.get(repairParameterName(name));
		if (par instanceof BooleanCktParameter)
			return ((BooleanCktParameter)par).getParameter();
		return defaultValue;
	}
	
	protected int parseIntegerParameter(String name, int defaultValue) {
		CktParameter<?> par = parameters.get(repairParameterName(name));
		if (par instanceof IntegerCktParameter)
			return ((IntegerCktParameter)par).getParameter();
		return defaultValue;
	}
	
	protected char parseAxisParameter(String name, char defaultValue) {
		CktParameter<?> par = parameters.get(repairParameterName(name));
		if (par instanceof AxisCktParameter)
			return ((AxisCktParameter)par).getParameter();
		return defaultValue;
	}
	
	protected String parseParameterName(String parameterName) {
		return getName() + "." + parameterName;
	}
	
	protected String repairParameterName(String parameterName) {
		return parameterName.replace(getName() + ".", "");
	}

}
