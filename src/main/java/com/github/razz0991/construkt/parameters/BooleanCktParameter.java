package com.github.razz0991.construkt.parameters;

import java.util.ArrayList;
import java.util.List;

/*  Construkt Bukkit plugin for Minecraft.
 *  Copyright (C) 2020 _Razz_
 *
 *  Full disclaimer in Construkt.java
 */
public class BooleanCktParameter implements CktParameter<Boolean>{
	
	private boolean value = false;
	
	public BooleanCktParameter(boolean value) {
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

	@Override
	public List<String> getAutoComplete(String comparitor) {
		List<String> out = new ArrayList<String>();
		if ("true".startsWith(comparitor))
			out.add("true");
		if ("false".startsWith(comparitor))
			out.add("false");
		return out;
	}

}
