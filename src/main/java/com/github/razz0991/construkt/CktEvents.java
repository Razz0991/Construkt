package com.github.razz0991.construkt;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class CktEvents implements Listener {
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent ev) {
		ev.getPlayer().sendMessage("Hello " + ev.getPlayer().getDisplayName());
	}
}
