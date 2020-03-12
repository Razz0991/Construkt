package com.github.razz0991.construkt.utility;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.data.BlockData;

import com.github.razz0991.construkt.Construkt;

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
	private boolean isFinalized = false;
	
	/**
	 * A {@link BlockData} history container to store undos and redos
	 */
	public CktBlockContainer() {
		blocks = new ArrayList<BlockInfo>();
	}
	
	/**
	 * Add {@link BlockData} to the history
	 * @param data The {@link BlockData} to store
	 * @param location The {@link Location} to where the block was
	 */
	public void addBlock(BlockData data, Location location) {
		blocks.add(0, new BlockInfo(data, location));
	}
	
	/**
	 * Finalize this container so it can be run.
	 * If this container is not finalized, the replaceBlocks() function will
	 * not run.
	 */
	public void finalize() {
		isFinalized = true;
	}
	
	/**
	 * Check if this container has been finalized.
	 * @return true if the container has been finalized.
	 */
	public boolean isFinalized() {
		return isFinalized;
	}
	
	// Cycles over the blocks, for the replaceBlocks() function to use
	private BlockInfo nextBlock() {
		if (isFinished()) {
			return null;
		}
		return blocks.get(loopPos++);
	}
	
	/**
	 * Checks if the {@code replaceBlocks()} loop is finished
	 * @return true if the loop is complete
	 */
	public boolean isFinished() {
		return loopPos == blocks.size();
	}
	
	// The fill task for replaceBlocks()
	private void createFillTask(Runnable task) {
		taskId = Construkt.plugin.getServer().getScheduler().scheduleSyncRepeatingTask(Construkt.plugin, task, 1, taskDelay);
	}
	
	private void cancelTask() {
		Construkt.plugin.getServer().getScheduler().cancelTask(taskId);
	}
	
	/**
	 * Replaces the blocks that are stored in the history
	 * @return A {@link CktBlockContainer} blocks that were there before the change<br>
	 * null if this container hasn't been finalized
	 */
	public CktBlockContainer replaceBlocks() {
		if (!isFinalized)
			return null;
		
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
				
				container.finalize();
				cancelTask();
			}
		});
		return container;
	}
	
	// Helper class to store both BlockData and Location
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

























