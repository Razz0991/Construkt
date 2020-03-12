package com.github.razz0991.construkt;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.data.BlockData;

/*  Construkt Bukkit plugin for Minecraft.
 *  Copyright (C) 2020 _Razz_
 *
 *  Full disclaimer in Construkt.java
 */
public class CktBlockContainer {
	
	private List<BlockInfo> blocks;
	private int loopPos = 0;
	private int taskId = -1;
	private long lastTime = -1;
	private final int tickLength = 50;
	private final int taskDelay = 2;
	
	public CktBlockContainer() {
		blocks = new ArrayList<BlockInfo>();
	}
	
	public void addBlock(BlockData data, Location location) {
		blocks.add(new BlockInfo(data, location));
	}
	
	public BlockInfo nextBlock() {
		if (isFinished()) {
			return null;
		}
		return blocks.get(loopPos++);
	}
	
	public boolean isFinished() {
		return loopPos == blocks.size();
	}
	
	private void createFillTask(Runnable task) {
		taskId = Construkt.plugin.getServer().getScheduler().scheduleSyncRepeatingTask(Construkt.plugin, task, 1, taskDelay);
	}
	
	private void cancelTask() {
		Construkt.plugin.getServer().getScheduler().cancelTask(taskId);
	}
	
	public CktBlockContainer replaceBlocks() {
		final CktBlockContainer container = new CktBlockContainer();
		createFillTask(new Runnable() {
			
			@Override
			public void run() {
				do {
					BlockInfo data = nextBlock();
					if (data == null)
						break;
					
					container.addBlock(data.getLocation().getBlock().getBlockData(), data.getLocation());
					data.getLocation().getBlock().setBlockData(data.getData());
					
					if (System.currentTimeMillis() >= lastTime + tickLength) {
						lastTime = System.currentTimeMillis() + taskDelay * tickLength;
						return;
					}
				} while (!isFinished());
				
				cancelTask();
			}
		});
		return container;
	}
	
	private class BlockInfo {
		private BlockData data;
		private Location location;
		
		BlockInfo(BlockData data, Location location) {
			this.data = data;
			this.location = location;
		}
		
		Location getLocation() {
			return location.clone();
		}
		
		BlockData getData() {
			return data.clone();
		}
	}
}

























