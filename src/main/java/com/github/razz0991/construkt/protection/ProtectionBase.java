package com.github.razz0991.construkt.protection;

import org.bukkit.Location;
import org.bukkit.entity.Player;

/*  Construkt Bukkit plugin for Minecraft.
 *  Copyright (C) 2020 _Razz_
 *
 *  Full disclaimer in Construkt.java
 */
public interface ProtectionBase {
	/**
	 * Checks if a player can build at a set location.
	 * @param loc The {@link Location} of the block
	 * @param ply The {@link Player} placing the block
	 * @return true if the player can place a block
	 */
	public abstract boolean canBuild(Location loc, Player ply);
}
