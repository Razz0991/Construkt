package com.github.razz0991.construkt.protection;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;

/*  Construkt Bukkit plugin for Minecraft.
 *  Copyright (C) 2020 _Razz_
 *
 *  Full disclaimer in Construkt.java
 */
public class ProtectionPlugins {
	
	private static List<ProtectionBase> protectionPlugins = new ArrayList<ProtectionBase>();
	
	/**
	 * Adds a protection plugin to check building against.
	 * @param plugin The {@link ProtectionBase} to add
	 */
	public static void addProtectionPlugin(ProtectionBase plugin) {
		protectionPlugins.add(plugin);
	}
	
	/**
	 * Checks if a player can build in a location. This checks all protection
	 * plugins that have been added.
	 * @param loc The {@link Location} to test
	 * @param ply The {@link Player} placing the block
	 * @return true if the player can place the block
	 */
	public static boolean canBuild(Location loc, Player ply) {
		for (ProtectionBase plugin : protectionPlugins) {
			if (!plugin.canBuild(loc, ply))
				return false;
		}
		return true;
	}
}
