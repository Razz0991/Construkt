package com.github.razz0991.construkt.utility;

/*  Construkt Bukkit plugin for Minecraft.
 *  Copyright (C) 2020 _Razz_
 *
 *  Full disclaimer in Construkt.java
 */
public class LocalLocation {
	private int x;
	private int y; 
	private int z;
	
	public LocalLocation(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public int getX() {
		return x;
	}

	
	public int getY() {
		return y;
	}

	
	public int getZ() {
		return z;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		else if (obj instanceof LocalLocation) {
			LocalLocation loc = (LocalLocation)obj;
			if (loc.getX() == x && loc.getY() == y && loc.getZ() == z)
				return true;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		String code = "" + x + "" + y + "" + z;
		return code.hashCode();
	}
}