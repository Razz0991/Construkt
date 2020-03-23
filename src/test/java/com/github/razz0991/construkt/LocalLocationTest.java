package com.github.razz0991.construkt;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import com.github.razz0991.construkt.utility.LocalLocation;

/*  Construkt Bukkit plugin for Minecraft.
 *  Copyright (C) 2020 _Razz_
 *
 *  Full disclaimer in Construkt.java
 */
public class LocalLocationTest {
	private static Map<LocalLocation, Object> blocks;
	private static LocalLocation loc1;
	private static LocalLocation loc2;
	private static LocalLocation loc3;
	
	@BeforeClass
	public static void init() {
		 blocks = new HashMap<LocalLocation, Object>();
		 loc1 = new LocalLocation(1, 2, 3);
		 loc2 = new LocalLocation(1, 2, 3);
		 loc3 = new LocalLocation(3, 2, 1);
		 
		 blocks.put(loc1, null);
	}
	
	@Test
	public void localLocSame() {
		assertEquals(loc2, loc1);
	}
	
	@Test
	public void localLocDifferent() {
		assertNotSame(loc1, loc3);
	}
	
	@Test
	public void mapHasKey() {
		assertTrue(blocks.containsKey(loc2));
	}
	
	@Test
	public void mapNotHasKey() {
		assertFalse(blocks.containsKey(loc3));
	}
}
