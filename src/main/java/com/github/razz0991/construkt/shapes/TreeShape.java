package com.github.razz0991.construkt.shapes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.BlockChangeDelegate;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Orientable;

import com.github.razz0991.construkt.CktUtil;
import com.github.razz0991.construkt.Construkt;
import com.github.razz0991.construkt.PlayerInfo;
import com.github.razz0991.construkt.filters.BaseFilter;
import com.github.razz0991.construkt.parameters.ListCktParameter;
import com.github.razz0991.construkt.utility.AreaInfo;
import com.github.razz0991.construkt.utility.CktBlockContainer;

/*  Construkt Bukkit plugin for Minecraft.
 *  Copyright (C) 2020 _Razz_
 *
 *  Full disclaimer in Construkt.java
 */
public class TreeShape extends BaseShape {
	
	private final String treeTypeName = "tree_type";
	private final TreeType treeTypeDefault = TreeType.TREE;
	private Map<Material, SaplingType> saplingTypes;
	private List<Material> allWoods;
	private List<Material> allLeaves;
	
	public TreeShape() {
		super();
	}
	
	public TreeShape(PlayerInfo ply) {
		super(ply);
		usingSingleLocation = true;
		cancelPointPlacement = true;
		
		List<String> treeTypes = new ArrayList<String>();
		for (TreeType type : TreeType.values())
			treeTypes.add(type.toString().toLowerCase());
		treeTypes.remove(TreeType.CHORUS_PLANT.toString().toLowerCase());
		
		parameters.put(treeTypeName, new ListCktParameter(
				treeTypeDefault.toString().toLowerCase(), treeTypes));
		
		saplingTypes = new HashMap<Material, TreeShape.SaplingType>();
		saplingTypes.put(Material.ACACIA_SAPLING, 
				new SaplingType(Material.ACACIA_LOG, Material.ACACIA_LEAVES));
		saplingTypes.put(Material.BIRCH_SAPLING, 
				new SaplingType(Material.BIRCH_LOG, Material.BIRCH_LEAVES));
		saplingTypes.put(Material.DARK_OAK_SAPLING, 
				new SaplingType(Material.DARK_OAK_LOG, Material.DARK_OAK_LEAVES));
		saplingTypes.put(Material.JUNGLE_SAPLING, 
				new SaplingType(Material.JUNGLE_LOG, Material.JUNGLE_LEAVES));
		saplingTypes.put(Material.OAK_SAPLING, 
				new SaplingType(Material.OAK_LOG, Material.OAK_LEAVES));
		saplingTypes.put(Material.SPRUCE_SAPLING, 
				new SaplingType(Material.SPRUCE_LOG, Material.SPRUCE_LEAVES));
		saplingTypes.put(Material.BROWN_MUSHROOM, 
				new SaplingType(Material.MUSHROOM_STEM, Material.BROWN_MUSHROOM_BLOCK));
		saplingTypes.put(Material.RED_MUSHROOM, 
				new SaplingType(Material.MUSHROOM_STEM, Material.RED_MUSHROOM_BLOCK));
		
		allWoods = CktUtil.matchMaterial("?_LOG");
		allWoods.add(Material.MUSHROOM_STEM);
		
		allLeaves = CktUtil.matchMaterial("?_LEAVES");
		allLeaves.addAll(CktUtil.matchMaterial("?_MUSHROOM_BLOCK"));
	}

	@Override
	public CktBlockContainer generateShape(Location firstPoint, Location secondPoint, BlockData blockData,
			BaseFilter[] filters) {
		if (blockData == null)
			return null;
		
		final CktBlockContainer cont = new CktBlockContainer();
		final TreeType treeType = TreeType.valueOf(getListParameter(treeTypeName, 
				treeTypeDefault.toString().toLowerCase()).toUpperCase());
		final Location treeLoc = firstPoint.clone();
		final SaplingType type = saplingTypes.containsKey(blockData.getMaterial()) ? 
				saplingTypes.get(blockData.getMaterial()) : null;
		
		Construkt.plugin.getServer().getScheduler().scheduleSyncDelayedTask(Construkt.plugin, new Runnable() {
			
			@Override
			public void run() {
				treeLoc.getWorld().generateTree(treeLoc, treeType, new BlockChangeDelegate() {
					
					@Override
					public boolean setBlockData(int x, int y, int z, BlockData bdata) {
						Location loc = new Location(treeLoc.getWorld(), x, y, z);
						cont.addBlock(loc.getBlock().getBlockData(), loc);
						if (type == null) {
							if (allWoods.contains(bdata.getMaterial()) || allLeaves.contains(bdata.getMaterial()))
								loc.getBlock().setBlockData(blockData);
							else
								loc.getBlock().setBlockData(bdata);
						}
						else {
							if (allWoods.contains(bdata.getMaterial())) {
								BlockData wood = type.getWoodMaterial().createBlockData();
								if (wood instanceof Orientable && bdata instanceof Orientable) {
									((Orientable)wood).setAxis(((Orientable)bdata).getAxis());
								}
								loc.getBlock().setBlockData(wood);
							}
							else if (allLeaves.contains(bdata.getMaterial())) {
								loc.getBlock().setType(type.getLeavesMaterial());
							}
							else {
								loc.getBlock().setBlockData(bdata);
							}
						}
						return true;
					}
					
					@Override
					public boolean isEmpty(int x, int y, int z) {
						return new Location(treeLoc.getWorld(), x, y, z).getBlock().getType() == Material.AIR;
					}
					
					@Override
					public int getHeight() {
						return treeLoc.getWorld().getMaxHeight();
					}
					
					@Override
					public BlockData getBlockData(int x, int y, int z) {
						return treeLoc.getBlock().getBlockData();
					}
				});
				
				cont.finalize();
			}
		}, 1);
		
		return cont;
	}

	@Override
	public String getName() {
		return "tree";
	}
	
	// Class to hold the sapling wood and leaf types
	private class SaplingType {
		private Material wood;
		private Material leaves;
		
		public SaplingType(Material wood, Material leaves) {
			this.wood = wood;
			this.leaves = leaves;
		}
		
		public Material getWoodMaterial() {
			return wood;
		}
		
		public Material getLeavesMaterial() {
			return leaves;
		}
	}
	
	@Override
	public AreaInfo getVolumeInformation(Location firstPoint, Location secondPoint) {
		return super.getVolumeInformation(firstPoint, firstPoint);
	}

}
