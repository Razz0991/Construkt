package com.github.razz0991.construkt;

import java.util.HashMap;
import java.util.Map;
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
		if (players.containsKey(uid))
			players.remove(uid);
	}
	
	public static PlayerInfo getPlayerInfo(Player player) {
		UUID uid = player.getUniqueId();
		if (players.containsKey(uid))
			return players.get(uid);
		return null;
	}
}
