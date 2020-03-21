package com.github.razz0991.construkt.filters;

import java.util.Random;

import org.bukkit.util.noise.SimplexOctaveGenerator;

import com.github.razz0991.construkt.CktUtil;
import com.github.razz0991.construkt.parameters.BooleanCktParameter;
import com.github.razz0991.construkt.parameters.IntegerCktParameter;
import com.github.razz0991.construkt.parameters.LongCktParameter;
import com.github.razz0991.construkt.utility.AreaData;

/*  Construkt Bukkit plugin for Minecraft.
 *  Copyright (C) 2020 _Razz_
 *
 *  Full disclaimer in Construkt.java
 */
public class NoiseFilter extends BaseFilter {

	private final String octaveName = "octaves";
	private final int octaveValue = 8;
	private final String scaleName = "scale";
	private final int scaleDefault = 5;
	private final String limitName = "limit";
	private final int limitDefault = 50;
	private final String invertName = "invert";
	private final boolean invertDefault = false;
	private final String randomSeedName = "random_seed";
	private final boolean randomSeedDefault = true;
	private final String seedName = "seed";
	private final long seedDefault = 0L;
	private final String smoothEdgeName = "smooth_edge";
	private final boolean smoothEdgeDefault = false;
	private final String smoothDistanceName = "smooth_distance";
	private final int smoothDistanceDefault = 3;
	
	private SimplexOctaveGenerator gen;
	
	public NoiseFilter() {
		super();
		IntegerCktParameter octaves = new IntegerCktParameter(octaveValue, 1, 8);
		IntegerCktParameter scale = new IntegerCktParameter(scaleDefault, 1, 10);
		IntegerCktParameter limit = new IntegerCktParameter(limitDefault, 1, 99);
		BooleanCktParameter invert = new BooleanCktParameter(invertDefault);
		BooleanCktParameter randomSeed = new BooleanCktParameter(randomSeedDefault);
		LongCktParameter seed = new LongCktParameter(seedDefault);
		parameters.put(octaveName, octaves);
		parameters.put(scaleName, scale);
		parameters.put(limitName, limit);
		parameters.put(invertName, invert);
		parameters.put(randomSeedName, randomSeed);
		parameters.put(seedName, seed);
		parameters.put(smoothEdgeName, new BooleanCktParameter(smoothEdgeDefault));
		parameters.put(smoothDistanceName, new IntegerCktParameter(smoothDistanceDefault, 0, 10));
	}

	@Override
	public String getName() {
		return "noise";
	}
	
	@Override
	public void runPreChecks() {
		long seed = getLongParameter(seedName, seedDefault);
		if (getBooleanParameter(randomSeedName, randomSeedDefault)) {
			Random rand = new Random();
			seed = rand.nextLong();
		}
		
		gen = new SimplexOctaveGenerator(seed, 
				getIntegerParameter(octaveName, octaveValue));
		gen.setScale(getIntegerParameter(scaleName, scaleDefault) / 100d);
	}

	@Override
	public boolean checkCondition(AreaData data) {
		double limit = getIntegerParameter(limitName, limitDefault) / 100d;
		
		final boolean smoothEdge = getBooleanParameter(smoothEdgeName, smoothEdgeDefault);
		final int smoothDistance = getIntegerParameter(smoothDistanceName, smoothDistanceDefault);
		
		double noise = getNoise(gen, data);
		if (smoothEdge) {
			double edgeDist = data.currentDistanceToAnyEdge();
			if (edgeDist <= smoothDistance) {
				double edgeNorm = edgeDist / smoothDistance;
				noise = CktUtil.lerp(noise, 1d,  1 - edgeNorm);
			}
		}
		
		boolean output = false;
		if (!getBooleanParameter(invertName, invertDefault))
			output = noise <= limit;
		else
			output = noise >= limit;
		
		return output;
	}
	
	// Gets the noise from the current location in the loop
	private double getNoise(SimplexOctaveGenerator generator, AreaData data) {
		double noise = generator.noise(data.getCurrentLocation().getX(), 
				data.getCurrentLocation().getY(), data.getCurrentLocation().getZ(), 1, 16, true);
		noise = (noise + 1) / 2;
		return noise;
	}

}
