package com.github.razz0991.construkt.shapes.parameters;

/*  Construkt Bukkit plugin for Minecraft.
 *  Copyright (C) 2020 _Razz_
 *
 *  Full disclaimer in Construkt.java
 */
public interface ShapeParameter<T> {
	
	/**
	 * Gets the parameters value.
	 * @return The value
	 */
	public T getParameter();
	/**
	 * Sets the parameters value.
	 * @param parameter The value for the parameter
	 */
	public void setParameter(T parameter);

}
