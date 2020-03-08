package com.github.razz0991.construkt.shapes.filters;

import java.util.HashMap;
import java.util.Map;

import com.github.razz0991.construkt.shapes.AreaData;
import com.github.razz0991.construkt.shapes.parameters.IntegerCktParameter;
import com.github.razz0991.construkt.shapes.parameters.CktParameter;

/*  Construkt Bukkit plugin for Minecraft.
 *  Copyright (C) 2020 _Razz_
 *
 *  Full disclaimer in Construkt.java
 */
public class RandomFilter extends BaseFilter {
	
	private final String chanceName = "chance";
	private final int chanceDefault = 50;

	@Override
	public String getFilterName() {
		return "random";
	}

	@Override
	public Map<String, CktParameter<?>> getParameters() {
		Map<String, CktParameter<?>> pars = new HashMap<String, CktParameter<?>>();
		pars.put(chanceName, new IntegerCktParameter(chanceDefault, 1, 99));
		return pars;
	}

	@Override
	public boolean checkCondition(AreaData data, Map<String, CktParameter<?>> parameters) {
		double chance = parseIntegerParameter(parameters, chanceName, chanceDefault) / 100d;
		
		return Math.random() <= chance;
	}

}
