package com.github.razz0991.construkt.parameters;

/*  Construkt Bukkit plugin for Minecraft.
 *  Copyright (C) 2020 _Razz_
 *
 *  Full disclaimer in Construkt.java
 */
public class LongCktParameter implements CktParameter<Long>{
	
	long value = 0L;
	
	public LongCktParameter(long value) {
		this.value = value;
	}

	@Override
	public Long getParameter() {
		return value;
	}

	@Override
	public void setParameter(Long parameter) {
		value = parameter;
	}

}
