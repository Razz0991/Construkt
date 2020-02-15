package com.github.razz0991.construkt;

import java.util.Map;

import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.github.razz0991.construkt.shapes.parameters.BooleanShapeParameter;
import com.github.razz0991.construkt.shapes.parameters.ShapeParameter;

/*  Construkt Bukkit plugin for Minecraft.
 *  Copyright (C) 2020 _Razz_
 *
 *  Full disclaimer in Construkt.java
 */
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
			if(ply.getMode() == CktMode.NONE && 
					(ev.getPlayer().hasPermission("construkt.mode.place") || 
							ev.getPlayer().hasPermission("construkt.mode.replace"))) {
				// Begin place mode or replace mode.
				ply.setBlockData(ev.getBlock().getBlockData());
				ply.setMode(CktMode.PLACE);
				ply.setFirstLocation(ev.getBlock().getLocation());
				
				CktUtil.messagePlayer(ev.getPlayer(), "First block placed.");
			}
			else if (ply.getMode() == CktMode.PLACE && 
					ev.getPlayer().hasPermission("construkt.mode.place")) {
				// Check limits
				if (ply.exceedsLimit(ev.getBlock().getLocation())) {
					ev.setCancelled(true);
					return;
				}
				//Fill area.
				CktUtil.messagePlayer(ev.getPlayer(), "Second block placed, filling area.");
				ply.getShape().generateShape(ply.getFirstLocation(), ev.getBlock().getLocation(), 
						ply.getAllParameters(), ply.getBlockData(), ply.getFilters());
				
				ply.resetMode();
			}
			else if (ply.getMode() == CktMode.BREAK) {
				if (ply.getFirstLocation().equals(ev.getBlock().getLocation())) {
					// Undo first break
					ply.resetMode();
					CktUtil.messagePlayer(ev.getPlayer(), "Undone first block breakage.");
				}
			}
		}
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent ev) {
		PlayerInfo ply = Players.getPlayerInfo(ev.getPlayer());
		if (ply.isConstruktEnabled() && !ev.getPlayer().isSneaking()) {
			if(ply.getMode() == CktMode.NONE && 
					ev.getPlayer().hasPermission("construkt.mode.break")) {
				// Begin break mode.
				ply.setBlockData(ev.getBlock().getBlockData());
				ply.setMode(CktMode.BREAK);
				ply.setFirstLocation(ev.getBlock().getLocation());
				
				CktUtil.messagePlayer(ev.getPlayer(), "First block broken.");
			}
			else if (ply.getMode() == CktMode.BREAK) {
				// Check limits
				if (ply.exceedsLimit(ev.getBlock().getLocation())) {
					ev.setCancelled(true);
					return;
				}
				//Clear area.
				CktUtil.messagePlayer(ev.getPlayer(), "Second block broken, clearing area.");
				Map<String, ShapeParameter<?>> parameters = ply.getAllParameters();
				BooleanShapeParameter air = new BooleanShapeParameter(false);
				parameters.put("place_in_air", air);
				ply.getShape().generateShape(ply.getFirstLocation(), ev.getBlock().getLocation(), parameters, 
						null, ply.getFilters());
				
				ply.resetMode();
			}
			else if (ply.getMode() == CktMode.PLACE) {
				if (ply.getFirstLocation().equals(ev.getBlock().getLocation())) {
					// Undo first place
					ply.resetMode();
					CktUtil.messagePlayer(ev.getPlayer(), "Undone first block placement.");
				}
				else if(ev.getPlayer().hasPermission("construkt.mode.replace")){
					// Check limits
					if (ply.exceedsLimit(ev.getBlock().getLocation())) {
						ev.setCancelled(true);
						return;
					}
					// Replace mode
					CktUtil.messagePlayer(ev.getPlayer(), "Second block broken, replacing blocks.");
					Map<String, ShapeParameter<?>> parameters = ply.getAllParameters();
					BooleanShapeParameter air = new BooleanShapeParameter(false);
					parameters.put("place_in_air", air);
					ply.getShape().generateShape(ply.getFirstLocation(), ev.getBlock().getLocation(), parameters, 
							ply.getBlockData(), ply.getFilters());
					
					//Allow broken block to be replaced too
					ev.setCancelled(true);
					
					ply.resetMode();
				}
			}
		}
	}
	
	@EventHandler
	public void onGameModeChange(PlayerGameModeChangeEvent ev) {
		PlayerInfo ply = Players.getPlayerInfo(ev.getPlayer());
		if (ev.getPlayer().getGameMode() == GameMode.CREATIVE && ply.isConstruktEnabled()) {
			ply.toggleConstruktEnabled();
		}
	}
}
