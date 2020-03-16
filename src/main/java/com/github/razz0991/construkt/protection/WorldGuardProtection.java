package com.github.razz0991.construkt.protection;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.regions.RegionQuery;

/*  Construkt Bukkit plugin for Minecraft.
 *  Copyright (C) 2020 _Razz_
 *
 *  Full disclaimer in Construkt.java
 */
public class WorldGuardProtection implements ProtectionBase{

	@Override
	public boolean canBuild(Location loc, Player ply) {
		LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(ply);
		RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
		if (!WorldGuard.getInstance().getPlatform().getSessionManager().hasBypass(localPlayer, BukkitAdapter.adapt(ply.getWorld()))) {
			return query.testState(BukkitAdapter.adapt(loc), localPlayer, Flags.BUILD);
		}
		return true;
	}

}
