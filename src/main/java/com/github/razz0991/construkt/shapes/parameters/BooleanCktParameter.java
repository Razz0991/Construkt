package com.github.razz0991.construkt.shapes.parameters;

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

}
