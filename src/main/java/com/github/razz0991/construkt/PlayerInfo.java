package com.github.razz0991.construkt;
import java.util.HashMap;
import java.util.HashSet;
/*  Construkt Bukkit plugin for Minecraft.
 *  Copyright (C) 2020 _Razz_
 *
 *  Full disclaimer in Construkt.java
 */
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;

import com.github.razz0991.construkt.shapes.BaseShape;
import com.github.razz0991.construkt.shapes.Shapes;
import com.github.razz0991.construkt.shapes.parameters.BooleanShapeParameter;
import com.github.razz0991.construkt.shapes.parameters.IntegerShapeParameter;
import com.github.razz0991.construkt.shapes.parameters.ShapeParameter;

public class PlayerInfo {
	private UUID plyId;
	private boolean cktEnabled = false;
	
	private BlockData blkData = null;
	private Location firstLocation = null;
	
	private CktMode mode = CktMode.NONE;
	private String shape = "cuboid";
	
	private Map<String, ShapeParameter<?>> parameters = new HashMap<String, ShapeParameter<?>>();
	
	public PlayerInfo(Player player) {
		plyId = player.getUniqueId();
	}
	
	public boolean isConstruktEnabled() {
		return cktEnabled;
	}
	
	public void toggleConstruktEnabled() {
		Player ply = getPlayer();
		if (ply.getGameMode() != GameMode.CREATIVE) {
			CktUtil.messagePlayer(ply, "You must be in creative mode to use Construkt");
			return;
		}
		
		cktEnabled = !cktEnabled;
		
		if (cktEnabled) {
			String[] toMessage = new String[2 + parameters.size()];
			toMessage[0] = ChatColor.AQUA + "Ready to build!";
			toMessage[1] = ChatColor.DARK_AQUA + "Shape: " + ChatColor.RESET + shape;
			
			int inc = 2;
			for (String msg : getChatParameters()) {
				toMessage[inc] = msg;
				inc++;
			}
			
			CktUtil.messagePlayer(ply, toMessage);
		}
		else {
			CktUtil.messagePlayer(ply, ChatColor.RED + "Building disabled");
			resetMode();
		}
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
	
	public BaseShape getShape() {
		return Shapes.getShape(shape);
	}
	
	public void setShape(String shape) {
		if (Shapes.hasShape(shape)) {
			this.shape = shape;
			clearParameters();
			
			if (getShape().getDefaultParameters() != null)
				parameters = getShape().getDefaultParameters();
			
			String[] toMessage = new String[1 + parameters.size()];
			toMessage[0] = ChatColor.AQUA + "Changed shape to " + shape;
			int inc = 1;
			for (String msg : getChatParameters()) {
				toMessage[inc] = msg;
				inc++;
			}
			CktUtil.messagePlayer(getPlayer(), toMessage);
			return;
		}
		CktUtil.messagePlayer(getPlayer(), "No shape found by the name \"" + shape + "\"");
	}
	
	private String[] getChatParameters() {
		String[] toMessage = new String[parameters.size()];
		int inc = 0;
		for (String par : parameters.keySet()) {
			ShapeParameter<?> parObj = parameters.get(par);
			if (parObj instanceof BooleanShapeParameter) {
				toMessage[inc] = ChatColor.DARK_AQUA + par + ": " + ChatColor.RESET + 
						((BooleanShapeParameter)parObj).getParameter();
			}
			else if (parObj instanceof IntegerShapeParameter) {
				IntegerShapeParameter intPar = (IntegerShapeParameter)parObj;
				String out = ChatColor.DARK_AQUA + par + ": " + ChatColor.RESET + 
						intPar.getParameter();
				if (intPar.isLimited()) {
					out += ChatColor.GRAY + " (" + intPar.getMinValue() + " to " + intPar.getMaxValue() + ")";
				}
				toMessage[inc] = out;
			}
			inc++;
		}
		
		return toMessage;
	}
	
	public void getParameterInfo(String name) {
		if (!parameters.containsKey(name)) {
			CktUtil.messagePlayer(getPlayer(), "No parameter called \"" + name + "\" is in the " + shape + " shape.");
			return;
		}
		String[] toMessage = new String[2];
		ShapeParameter<?> parObj = parameters.get(name);
		if (parObj instanceof BooleanShapeParameter) {
			toMessage[0] = ChatColor.AQUA + name + " accepts boolean values.";
			toMessage[1] = ChatColor.DARK_AQUA + "Current Value: " + ChatColor.RESET + 
					((BooleanShapeParameter)parObj).getParameter();
		}
		else if (parObj instanceof IntegerShapeParameter) {
			IntegerShapeParameter intPar = (IntegerShapeParameter)parObj;
			toMessage[0] = ChatColor.AQUA + name + " accepts full number values.";
			
			if (intPar.isLimited()) {
				toMessage[0] += ChatColor.GRAY + " (" + intPar.getMinValue() + " to " + intPar.getMaxValue() + ")";
			}
			
			toMessage[1] = ChatColor.DARK_AQUA + "Current Value: " + ChatColor.RESET + 
					intPar.getParameter();;
		}
		
		CktUtil.messagePlayer(getPlayer(), toMessage);
	}
	
	public ShapeParameter<?> getParameter(String name) {
		if (parameters.containsKey(name))
			return parameters.get(name);
		return null;
	}
	
	public Set<String> getAllParameterKeys() {
		return new HashSet<String>(parameters.keySet());
	}
	
	public Map<String, ShapeParameter<?>> getAllParameters(){
		return new HashMap<String, ShapeParameter<?>>(parameters);
	}
	
	public void setParameter(String name, String value) {
		if (!parameters.containsKey(name)) {
			CktUtil.messagePlayer(getPlayer(), "No parameter called \"" + name + "\" is in the " + shape + " shape.");
			return;
		}

		if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
			if (!(parameters.get(name) instanceof BooleanShapeParameter)) {
				CktUtil.messagePlayer(getPlayer(), "\"" + name + "\" does not take a boolean value.");
				return;
			}
			((BooleanShapeParameter)parameters.get(name)).setParameter(Boolean.parseBoolean(value));
			CktUtil.messagePlayer(getPlayer(), "Set \"" + name + "\" to " + value);
			return;
		}
		else if (CktUtil.isInteger(value)) {
			if (!(parameters.get(name) instanceof IntegerShapeParameter)) {
				CktUtil.messagePlayer(getPlayer(), "\"" + name + "\" does not take a number value.");
				return;
			}
			IntegerShapeParameter par = (IntegerShapeParameter)parameters.get(name);
			par.setParameter(Integer.parseInt(value));
			CktUtil.messagePlayer(getPlayer(), "Set \"" + name + "\" to " + par.getParameter());
			return;
		}
		CktUtil.messagePlayer(getPlayer(), "\"" + value + "\" is not a valid value.");
	}
	
	public void clearParameters() {
		parameters.clear();
	}
	
	public void resetMode() {
		setBlockData(null);
		setMode(CktMode.NONE);
		setFirstLocation(null);
	}

}
