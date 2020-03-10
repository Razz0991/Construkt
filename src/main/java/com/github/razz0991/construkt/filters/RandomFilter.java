package com.github.razz0991.construkt.filters;

import com.github.razz0991.construkt.parameters.IntegerCktParameter;
import com.github.razz0991.construkt.shapes.AreaData;

/*  Construkt Bukkit plugin for Minecraft.
 *  Copyright (C) 2020 _Razz_
 *
 *  Full disclaimer in Construkt.java
 */
public class RandomFilter extends BaseFilter {

	private final String chanceName = "chance";
	private final int chanceDefault = 50;
	
	public RandomFilter() {
		super();
		parameters.put(chanceName, new IntegerCktParameter(chanceDefault, 1, 99));
	}

	@Override
	public String getName() {
		return "random";
	}

	@Override
	public boolean checkCondition(AreaData data) {
		double chance = getIntegerParameter(chanceName, chanceDefault) / 100d;
		
		return Math.random() <= chance;
	}

}
