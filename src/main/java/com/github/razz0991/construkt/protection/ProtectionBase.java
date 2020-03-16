package com.github.razz0991.construkt.protection;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface ProtectionBase {
	public abstract boolean canBuild(Location loc, Player ply);
}
