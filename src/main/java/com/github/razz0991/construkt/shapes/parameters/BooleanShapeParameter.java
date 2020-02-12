package com.github.razz0991.construkt.shapes.parameters;

/*  Construkt Bukkit plugin for Minecraft.
 *  Copyright (C) 2020 _Razz_
 *
 *  Full disclaimer in Construkt.java
 */
public class BooleanShapeParameter implements ShapeParameter<Boolean>{
	
	private boolean value = false;
	
	public BooleanShapeParameter(boolean value) {
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

}
