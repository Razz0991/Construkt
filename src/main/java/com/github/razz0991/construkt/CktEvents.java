package com.github.razz0991.construkt;
/*  Construkt Bukkit plugin for Minecraft.
 *  Copyright (C) 2020 _Razz_
 *
 *  Full disclaimer in Construkt.java
 */
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class CktEvents implements Listener {
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent ev) {
		Players.addPlayer(ev.getPlayer());
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent ev) {
		Players.removePlayer(ev.getPlayer());
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent ev) {
		PlayerInfo ply = Players.getPlayerInfo(ev.getPlayer());
		if (ply.isConstruktEnabled() && !ev.getPlayer().isSneaking()) {
			if(ply.getMode() == CktMode.NONE) {
				// Begin place fill mode.
				ply.setBlockData(ev.getBlock().getBlockData());
				ply.setMode(CktMode.PLACE);
				ply.setFirstLocation(ev.getBlock().getLocation());
				
				CktUtil.messagePlayer(ev.getPlayer(), "First block placed.");
			}
			else if (ply.getMode() == CktMode.PLACE) {
				//Fill area.
				CktUtil.messagePlayer(ev.getPlayer(), "Second block placed, filling area.");
				Location[] locations = ply.getShape().shapeLocations(ply.getFirstLocation(), ev.getBlock().getLocation());
				ply.getShape().getLoop().runLoop(locations, true, ply.getBlockData(), ply.getShape());
				CktUtil.messagePlayer(ev.getPlayer(), "Area filled.");
				
				ply.setBlockData(null);
				ply.setMode(CktMode.NONE);
				ply.setFirstLocation(null);
			}
			else if (ply.getMode() == CktMode.BREAK) {
				if (ply.getFirstLocation().equals(ev.getBlock().getLocation())) {
					// Undo first break
					ply.setBlockData(null);
					ply.setMode(CktMode.NONE);
					ply.setFirstLocation(null);
					CktUtil.messagePlayer(ev.getPlayer(), "Undone first block breakage.");
				}
			}
		}
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent ev) {
		PlayerInfo ply = Players.getPlayerInfo(ev.getPlayer());
		if (ply.isConstruktEnabled() && !ev.getPlayer().isSneaking()) {
			if(ply.getMode() == CktMode.NONE) {
				// Begin break mode.
				ply.setBlockData(ev.getBlock().getBlockData());
				ply.setMode(CktMode.BREAK);
				ply.setFirstLocation(ev.getBlock().getLocation());
				
				CktUtil.messagePlayer(ev.getPlayer(), "First block broken.");
			}
			else if (ply.getMode() == CktMode.BREAK) {
				//Clear area.
				CktUtil.messagePlayer(ev.getPlayer(), "Second block broken, clearing area.");
				Location[] locations = ply.getShape().shapeLocations(ply.getFirstLocation(), ev.getBlock().getLocation());
				ply.getShape().getLoop().runLoop(locations, false, null, ply.getShape());
				CktUtil.messagePlayer(ev.getPlayer(), "Area cleared.");
				
				ply.setBlockData(null);
				ply.setMode(CktMode.NONE);
				ply.setFirstLocation(null);
			}
			else if (ply.getMode() == CktMode.PLACE) {
				if (ply.getFirstLocation().equals(ev.getBlock().getLocation())) {
					// Undo first place
					ply.setBlockData(null);
					ply.setMode(CktMode.NONE);
					ply.setFirstLocation(null);
					CktUtil.messagePlayer(ev.getPlayer(), "Undone first block placement.");
				}
				else {
					// Replace mode
					CktUtil.messagePlayer(ev.getPlayer(), "Second block broken, replacing blocks.");
					Location[] locations = ply.getShape().shapeLocations(ply.getFirstLocation(), ev.getBlock().getLocation());
					ply.getShape().getLoop().runLoop(locations, false, ply.getBlockData(), ply.getShape());
					CktUtil.messagePlayer(ev.getPlayer(), "Blocks replaced.");
					
					ply.setBlockData(null);
					ply.setMode(CktMode.NONE);
					ply.setFirstLocation(null);
				}
			}
		}
	}
}
