package com.github.razz0991.construkt.filters;

import com.github.razz0991.construkt.parameters.ParameterObject;
import com.github.razz0991.construkt.utility.AreaData;

/*  Construkt Bukkit plugin for Minecraft.
 *  Copyright (C) 2020 _Razz_
 *
 *  Full disclaimer in Construkt.java
 */
/**
 * A Filter object to modify the look of a {@link BaseShape}
 */
public abstract class BaseFilter extends ParameterObject{
	
	/**
	 * A check to see if the current position should allow a 
	 * block placement based on this filter.
	 * @param data The area data
	 * @return true if a block should be placed
	 */
	public abstract boolean checkCondition(final AreaData data);
	
	public void runPreChecks() {
		// Do nothing
	}
}
