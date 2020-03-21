package com.github.razz0991.construkt.parameters;

import java.util.ArrayList;
import java.util.List;

/*  Construkt Bukkit plugin for Minecraft.
 *  Copyright (C) 2020 _Razz_
 *
 *  Full disclaimer in Construkt.java
 */
public class ListCktParameter implements CktParameter<String>{
	
	private String value;
	private List<String> possibleValues;
	
	public ListCktParameter(String defaultValue, List<String> possibleValues) {
		if (!possibleValues.contains(defaultValue))
			throw new IllegalArgumentException("possibleValues does not contain the defaultValue!");
		value = defaultValue;
		this.possibleValues = new ArrayList<String>(possibleValues);
	}

	@Override
	public String getParameter() {
		return value;
	}

	@Override
	public void setParameter(String parameter) {
		if (possibleValues.contains(parameter))
			value = parameter;
	}
	
	public boolean hasValue(String parameter) {
		return possibleValues.contains(parameter);
	}
	
	public List<String> getPossibleValues() {
		return new ArrayList<String>(possibleValues);
	}

	@Override
	public List<String> getAutoComplete(String comparitor) {
		List<String> out = new ArrayList<String>();
		for (String val : possibleValues) {
			if (val.startsWith(comparitor))
				out.add(val);
		}
		return out;
	}

}
