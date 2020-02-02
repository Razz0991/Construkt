package com.github.razz0991.construkt;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;

public class PlayerInfo {
	private UUID plyId;
	private boolean cktEnabled = false;
	
	private BlockData blkData = null;
	private Location firstLocation = null;
	
	private CktMode mode = CktMode.NONE;
	
	public PlayerInfo(Player player) {
		plyId = player.getUniqueId();
	}
	
	public boolean isConstruktEnabled() {
		return cktEnabled;
	}
	
	public void toggleConstruktEnabled() {
		cktEnabled = !cktEnabled;
		Player ply = getPlayer();
		
		if (cktEnabled)
			CktUtil.messagePlayer(ply, "Ready to build!");
		else
			CktUtil.messagePlayer(ply, "Building disabled");
	}
	
	public Player getPlayer() {
		return Bukkit.getServer().getPlayer(plyId);
	}
	
	public BlockData getBlockData() {
		return blkData;
	}
	
	public void setBlockData(BlockData data) {
		if (data != null)
			blkData = data.clone();
		else
			blkData = null;
	}
	
	public CktMode getMode() {
		return mode;
	}
	
	public void setMode(CktMode mode) {
		this.mode = mode;
	}

	public Location getFirstLocation() {
		return firstLocation;
	}

	public void setFirstLocation(Location firstLocation) {
		this.firstLocation = firstLocation;
	}

}
