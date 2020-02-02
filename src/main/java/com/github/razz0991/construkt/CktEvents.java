package com.github.razz0991.construkt;

import org.bukkit.Location;
import org.bukkit.Material;
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
				if (ply.getBlockData() != null && ply.getFirstLocation() != null) {
					int[] x, y, z;
					Location first, second;
					first = ply.getFirstLocation();
					second = ev.getBlock().getLocation();
					x = CktUtil.smallestNumber(first.getBlockX(), second.getBlockX());
					y = CktUtil.smallestNumber(first.getBlockY(), second.getBlockY());
					z = CktUtil.smallestNumber(first.getBlockZ(), second.getBlockZ());
					
					// Fill area loops
					for (int lz = z[0]; lz <= z[1]; lz++) {
						for (int lx = x[0]; lx <= x[1]; lx++) {
							for (int ly = y[0]; ly <= y[1]; ly++) {
								Location position = new Location(ev.getBlock().getWorld(), lx, ly, lz);
								// Only place blocks in air blocks.
								if (position.getBlock().getType() == Material.AIR)
									position.getBlock().setBlockData(ply.getBlockData().clone());
							}
						}
					}
				}
				CktUtil.messagePlayer(ev.getPlayer(), "Area filled.");
				
				ply.setBlockData(null);
				ply.setMode(CktMode.NONE);
				ply.setFirstLocation(null);
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
				if (ply.getBlockData() != null && ply.getFirstLocation() != null) {
					int[] x, y, z;
					Location first, second;
					first = ply.getFirstLocation();
					second = ev.getBlock().getLocation();
					x = CktUtil.smallestNumber(first.getBlockX(), second.getBlockX());
					y = CktUtil.smallestNumber(first.getBlockY(), second.getBlockY());
					z = CktUtil.smallestNumber(first.getBlockZ(), second.getBlockZ());
					
					// Clear area loops
					for (int lz = z[0]; lz <= z[1]; lz++) {
						for (int lx = x[0]; lx <= x[1]; lx++) {
							for (int ly = y[0]; ly <= y[1]; ly++) {
								Location position = new Location(ev.getBlock().getWorld(), lx, ly, lz);
								position.getBlock().setType(Material.AIR);
							}
						}
					}
				}
				CktUtil.messagePlayer(ev.getPlayer(), "Area cleared.");
				
				ply.setBlockData(null);
				ply.setMode(CktMode.NONE);
				ply.setFirstLocation(null);
			}
		}
	}
}
