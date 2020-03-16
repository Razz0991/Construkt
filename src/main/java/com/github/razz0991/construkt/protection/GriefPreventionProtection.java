package com.github.razz0991.construkt.protection;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import me.ryanhamshire.GriefPrevention.GriefPrevention;

public class GriefPreventionProtection implements ProtectionBase {

	@Override
	public boolean canBuild(Location loc, Player ply) {
		return GriefPrevention.instance.allowBuild(ply, loc) == null 
				&& GriefPrevention.instance.allowBreak(ply, loc.getBlock(), loc) == null;
	}

}
