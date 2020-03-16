package com.github.razz0991.construkt.protection;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import me.ryanhamshire.GriefPrevention.GriefPrevention;

/*  Construkt Bukkit plugin for Minecraft.
 *  Copyright (C) 2020 _Razz_
 *
 *  Full disclaimer in Construkt.java
 */
public class GriefPreventionProtection implements ProtectionBase {

	@Override
	public boolean canBuild(Location loc, Player ply) {
		return GriefPrevention.instance.allowBuild(ply, loc) == null 
				&& GriefPrevention.instance.allowBreak(ply, loc.getBlock(), loc) == null;
	}

}
