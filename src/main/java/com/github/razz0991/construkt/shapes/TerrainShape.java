package com.github.razz0991.construkt.shapes;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.block.data.BlockData;
import org.bukkit.util.noise.SimplexOctaveGenerator;

import com.github.razz0991.construkt.CktUtil;
import com.github.razz0991.construkt.PlayerInfo;
import com.github.razz0991.construkt.filters.BaseFilter;
import com.github.razz0991.construkt.parameters.BooleanCktParameter;
import com.github.razz0991.construkt.parameters.IntegerCktParameter;
import com.github.razz0991.construkt.parameters.LongCktParameter;
import com.github.razz0991.construkt.utility.AreaData;
import com.github.razz0991.construkt.utility.CktBlockContainer;

/*  Construkt Bukkit plugin for Minecraft.
 *  Copyright (C) 2020 _Razz_
 *
 *  Full disclaimer in Construkt.java
 */
public class TerrainShape extends BaseShape {
	
	private final String octaveName = "octaves";
	private final int octaveValue = 8;
	private final String scaleName = "scale";
	private final int scaleDefault = 5;
	private final String randomSeedName = "random_seed";
	private final boolean randomSeedDefault = false;
	private final String seedName = "seed";
	private final long seedDefault = 0L;
	private final String smoothEdgeName = "smooth_edge";
	private final boolean smoothEdgeDefault = false;
	private final String smoothDistanceName = "smooth_distance";
	private final int smoothDistanceDefault = 3;
	private final String smoothVerticalName = "smooth_vertical";
	private final int smoothVerticalDefault = 50;
	
	public TerrainShape() {
		super();
	}
	
	public TerrainShape(PlayerInfo plyInfo) {
		super(plyInfo);
		parameters.put(octaveName, new IntegerCktParameter(octaveValue, 1, 8));
		parameters.put(scaleName, new IntegerCktParameter(scaleDefault, 1, 10));
		parameters.put(randomSeedName, new BooleanCktParameter(randomSeedDefault));
		parameters.put(seedName, new LongCktParameter(seedDefault));
		parameters.put(smoothEdgeName, new BooleanCktParameter(smoothEdgeDefault));
		parameters.put(smoothDistanceName, new IntegerCktParameter(smoothDistanceDefault, 0, 10));
		parameters.put(smoothVerticalName, new IntegerCktParameter(smoothVerticalDefault, 0, 100));
	}

	@Override
	public String getName() {
		return "terrain";
	}

	@Override
	public CktBlockContainer generateShape(Location firstPoint, Location secondPoint,
			BlockData blockData, BaseFilter[] filters) {
		boolean reversed = blockData == null;
		final AreaData data = new AreaData(firstPoint, secondPoint, reversed);
		final BlockData strictBlock = secondPoint.getBlock().getBlockData().clone();
		final CktBlockContainer container = new CktBlockContainer();
		long seed = getLongParameter(seedName, seedDefault);
		if (getBooleanParameter(randomSeedName, randomSeedDefault)) {
			Random rand = new Random();
			seed = rand.nextLong();
		}
		final SimplexOctaveGenerator gen = new SimplexOctaveGenerator(seed, getIntegerParameter(octaveName, 8));
		gen.setScale(getIntegerParameter(scaleName, scaleDefault) / 100.0d);
		
		final boolean smoothEdge = getBooleanParameter(smoothEdgeName, smoothEdgeDefault);
		final int smoothDistance = getIntegerParameter(smoothDistanceName, smoothDistanceDefault);
		final int smoothVertical = getIntegerParameter(smoothVerticalName, smoothVerticalDefault);
		
		data.createFillTask(new Runnable() {
			
			@Override
			public void run() {
				do {
					double noise = getNoise(gen, data);
					if (smoothEdge) {
						double edgeDist = Math.min(data.currentDistanceToXEdge(), data.currentDistanceToZEdge());
						if (edgeDist <= smoothDistance) {
							double vertHeight = (smoothVertical / 100.0d) * data.getYSize();
							double edgeNorm = edgeDist / smoothDistance;
							noise = CktUtil.lerp(noise, vertHeight,  1 - edgeNorm);
						}
					}
					if (noise >= data.getCurrentRelativeY())
						if (canPlace(data, filters))
							setBlock(blockData, data.getCurrentLocation(), container, strictBlock);
					
					boolean shouldWait = data.incrementLoop();
					if (shouldWait)
						return;
				} while (!data.isLoopFinished(container, ply));
			}
		});
		
		return container;
	}
	
	// Gets the noise from the current location in the loop
	private double getNoise(SimplexOctaveGenerator generator, AreaData data) {
		double noise = generator.noise(data.getCurrentLocation().getX(), data.getCurrentLocation().getZ(), 1, 16, true);
		noise = (noise + 1) / 2;
		double height = noise * data.getYSize();
		
		return height;
	}

}
