package com.github.razz0991.construkt;
/*  Construkt Bukkit plugin for Minecraft.
 *  Copyright (C) 2020 _Razz_
 *
 *  Full disclaimer in Construkt.java
 */
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.entity.Player;

public class Players {
	private static Map<UUID, PlayerInfo> players = new HashMap<UUID, PlayerInfo>();
	
	public static void addPlayer(Player player) {
		UUID uid = player.getUniqueId();
		if (!players.containsKey(uid))
			players.put(uid, new PlayerInfo(player));
	}
	
	public static void removePlayer(Player player) {
		UUID uid = player.getUniqueId();
		removePlayer(uid);
	}
	
	public static void removePlayer(UUID playerId) {
		if (players.containsKey(playerId))
			players.remove(playerId);
	}
	
	public static PlayerInfo getPlayerInfo(Player player) {
		UUID uid = player.getUniqueId();
		if (players.containsKey(uid))
			return players.get(uid);
		return null;
	}
	
	public static Set<UUID> getAllPlayers() {
		return new HashSet<UUID>(players.keySet());
	}
}
