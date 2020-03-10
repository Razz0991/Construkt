package com.github.razz0991.construkt.filters;

import com.github.razz0991.construkt.parameters.BooleanCktParameter;
import com.github.razz0991.construkt.parameters.IntegerCktParameter;
import com.github.razz0991.construkt.shapes.AreaData;

/*  Construkt Bukkit plugin for Minecraft.
 *  Copyright (C) 2020 _Razz_
 *
 *  Full disclaimer in Construkt.java
 */
public class CheckeredFilter extends BaseFilter {

	private static String invertName = "invert";
	private static boolean invertDefault = false;
	private static String sizeName = "size";
	private static int sizeDefault = 1;
	
	public CheckeredFilter() {
		super();
		parameters.put(invertName, new BooleanCktParameter(invertDefault));
		parameters.put(sizeName, new IntegerCktParameter(sizeDefault, 1, 20));
	}

	@Override
	public String getName() {
		return "checkered";
	}
	
	@Override
	public boolean checkCondition(AreaData data) {
		int checkerSize = getIntegerParameter(sizeName, sizeDefault);
		boolean yEven = isChecker(data.getCurrentRelativeY(), checkerSize);
		if (getBooleanParameter(invertName, invertDefault))
			yEven = !yEven;
		
		if (yEven) {
			if (!isChecker(data.getCurrentRelativeX(), checkerSize)) {
				if (!isChecker(data.getCurrentRelativeZ(), checkerSize))
					return true;
			}
			else {
				if (isChecker(data.getCurrentRelativeZ(), checkerSize))
					return true;
			}
		}
		else {
			if (!isChecker(data.getCurrentRelativeX(), checkerSize)) {
				if (isChecker(data.getCurrentRelativeZ(), checkerSize))
					return true;
			}
			else {
				if (!isChecker(data.getCurrentRelativeZ(), checkerSize))
					return true;
			}
		}
		return false;
	}
	
	private boolean isChecker(int number, int checkerSize) {
		int value = number / checkerSize;
		return (value % 2 == 0);
	}
}
