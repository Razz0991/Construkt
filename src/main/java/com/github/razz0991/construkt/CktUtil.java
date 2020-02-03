package com.github.razz0991.construkt;
/*  Construkt Bukkit plugin for Minecraft.
 *  Copyright (C) 2020 _Razz_
 *
 *  Full disclaimer in Construkt.java
 */
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class CktUtil {
	
	public static void messagePlayer(Player player, String message) {
		player.sendMessage(ChatColor.AQUA + "[Construkt] " + ChatColor.RESET + message);
	}
	
	public static int[] smallestNumber(int first, int second) {
		int[] output = new int[2];
		if (first > second) {
			output[0] = second;
			output[1] = first;
			return output;
		}
		else {
			output[0] = first;
			output[1] = second;
			return output;
		}
	}

}
