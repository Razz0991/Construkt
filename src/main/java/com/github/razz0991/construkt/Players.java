package com.github.razz0991.construkt;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.entity.Player;

/*  Construkt Bukkit plugin for Minecraft.
 *  Copyright (C) 2020 _Razz_
 *
 *  Full disclaimer in Construkt.java
 */
public class Players {
	private static Map<UUID, PlayerInfo> players = new HashMap<UUID, PlayerInfo>();
	
	// Add player, automatically set via events
	static void addPlayer(Player player) {
		UUID uid = player.getUniqueId();
		if (!players.containsKey(uid))
			players.put(uid, new PlayerInfo(player));
	}
	
	// Remove player by Player object, automatically done via events
	static void removePlayer(Player player) {
		UUID uid = player.getUniqueId();
		removePlayer(uid);
	}
	
	// Remove player by UUID, automatically done on plugin disable
	static void removePlayer(UUID playerId) {
		if (players.containsKey(playerId))
			players.remove(playerId);
	}
	
	/**
	 * Gets the player info from Construkt.
	 * @param player The player to get the information for
	 * @return A <code>PlayerInfo</code> object or <code>null</code> if none
	 * are found.
	 */
	public static PlayerInfo getPlayerInfo(Player player) {
		UUID uid = player.getUniqueId();
		if (players.containsKey(uid))
			return players.get(uid);
		return null;
	}
	
	/**
	 * Gets a copy of all the players assigned in Construkt.
	 * @return A <code>Set</code> of all the UUIDs
	 */
	public static Set<UUID> getAllPlayers() {
		return new HashSet<UUID>(players.keySet());
	}
}
