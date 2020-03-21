package com.github.razz0991.construkt;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.github.razz0991.construkt.protection.ProtectionPlugins;
import com.github.razz0991.construkt.shapes.BaseShape.PlaceMode;
import com.github.razz0991.construkt.utility.CktBlockContainer;
import com.github.razz0991.construkt.utility.CktMode;

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
	
	private boolean hasPermissions(PlayerInfo ply, Block block) {
		if (!ply.hasPermission("construkt.bypass_blacklist") && 
				CktConfigOptions.isMaterialBlacklisted(block.getType())) {
			CktUtil.messagePlayer(ply.getPlayer().getPlayer(), 
					ChatColor.RED + "This block is blacklisted from usage!");
			return false;
		}
		return true;
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onBlockPlace(BlockPlaceEvent ev) {
		if (ev.isCancelled() || 
				!ProtectionPlugins.canBuild(ev.getBlock().getLocation(), ev.getPlayer()))
			return;
		
		PlayerInfo ply = Players.getPlayerInfo(ev.getPlayer());
		if (ply.isConstruktEnabled() && !ev.getPlayer().isSneaking()) {
			if(ply.getMode() == CktMode.NONE && 
					(ev.getPlayer().hasPermission("construkt.mode.place") || 
							ev.getPlayer().hasPermission("construkt.mode.replace"))) {
				if (!hasPermissions(ply, ev.getBlock())) {
					ev.setCancelled(true);
					return;
				}
				if (!ply.getShape().isUsingSingleLocation()) {
					// Begin place mode or replace mode.
					ply.setBlockData(ev.getBlock().getBlockData());
					ply.setMode(CktMode.PLACE);
					ply.setFirstLocation(ev.getBlock().getLocation());
					
					CktUtil.messagePlayer(ev.getPlayer(), "First block placed.");
				}
				else {
					ply.getShape().setPlaceMode(PlaceMode.AIR);
					CktBlockContainer undo = ply.getShape().generateShape(ev.getBlock().getLocation(), null, 
							ev.getBlock().getBlockData(), ply.getFilters());
					
					if (ply.hasPermission("construkt.undo"))
						ply.addUndo(undo);
					
					ply.setCanBuild(false);
					ply.resetMode();
					
					CktUtil.messagePlayer(ev.getPlayer(), "Filling Shape.");
					
					if (ply.getShape().isCancelingPointPlacement())
						ev.setCancelled(true);
				}
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
				ply.getShape().setPlaceMode(PlaceMode.AIR);
				CktBlockContainer undo = ply.getShape().generateShape(ply.getFirstLocation(), ev.getBlock().getLocation(), 
						ply.getBlockData(), ply.getFilters());
				
				if (ply.hasPermission("construkt.undo"))
					ply.addUndo(undo);
				
				ply.setCanBuild(false);
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
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onBlockBreak(BlockBreakEvent ev) {
		if (ev.isCancelled() || 
				!ProtectionPlugins.canBuild(ev.getBlock().getLocation(), ev.getPlayer()))
			return;
		
		PlayerInfo ply = Players.getPlayerInfo(ev.getPlayer());
		if (ply.isConstruktEnabled() && !ev.getPlayer().isSneaking()) {
			if(ply.getMode() == CktMode.NONE && 
					ev.getPlayer().hasPermission("construkt.mode.break")) {
				if (!hasPermissions(ply, ev.getBlock())) {
					ev.setCancelled(true);
					return;
				}
				if (!ply.getShape().isUsingSingleLocation()) {
					// Begin break mode.
					ply.setBlockData(ev.getBlock().getBlockData());
					ply.setMode(CktMode.BREAK);
					ply.setFirstLocation(ev.getBlock().getLocation());
					
					CktUtil.messagePlayer(ev.getPlayer(), "First block broken.");
				}
				else {
					ply.getShape().setPlaceMode(PlaceMode.SOLID);
					CktBlockContainer undo = ply.getShape().generateShape(ev.getBlock().getLocation(), null, 
							null, ply.getFilters());
					
					if (ply.hasPermission("construkt.undo"))
						ply.addUndo(undo);
					
					ply.setCanBuild(false);
					ply.resetMode();
					
					CktUtil.messagePlayer(ev.getPlayer(), "Clearing Shape.");

					if (ply.getShape().isCancelingPointPlacement())
						ev.setCancelled(true);
				}
			}
			else if (ply.getMode() == CktMode.BREAK) {
				// Check limits
				if (ply.exceedsLimit(ev.getBlock().getLocation())) {
					ev.setCancelled(true);
					return;
				}
				//Clear area.
				CktUtil.messagePlayer(ev.getPlayer(), "Second block broken, clearing area.");
				ply.getShape().setPlaceMode(PlaceMode.SOLID);
				CktBlockContainer undo = ply.getShape().generateShape(ply.getFirstLocation(), ev.getBlock().getLocation(), 
						null, ply.getFilters());
				
				if (ply.hasPermission("construkt.undo"))
					ply.addUndo(undo);
				
				ply.setCanBuild(false);
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
					ply.getShape().setPlaceMode(PlaceMode.SOLID);
					CktBlockContainer undo = ply.getShape().generateShape(ply.getFirstLocation(), ev.getBlock().getLocation(), 
							ply.getBlockData(), ply.getFilters());
					
					if (ply.hasPermission("construkt.undo"))
						ply.addUndo(undo);
					
					//Allow broken block to be replaced too
					ev.setCancelled(true);
					
					ply.setCanBuild(false);
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
	
	@EventHandler
	public void onWorldChange(PlayerChangedWorldEvent ev) {
		PlayerInfo ply = Players.getPlayerInfo(ev.getPlayer());
		if(ply.isConstruktEnabled()) {
			ply.toggleConstruktEnabled();
			ply.resetMode();
		}
	}
}
