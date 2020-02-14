package com.github.razz0991.construkt;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;

import com.github.razz0991.construkt.CktConfigOptions.Limiter;
import com.github.razz0991.construkt.CktUtil.VolumeInformation;
import com.github.razz0991.construkt.shapes.BaseShape;
import com.github.razz0991.construkt.shapes.Shapes;
import com.github.razz0991.construkt.shapes.parameters.BooleanShapeParameter;
import com.github.razz0991.construkt.shapes.parameters.IntegerShapeParameter;
import com.github.razz0991.construkt.shapes.parameters.ShapeParameter;

/*  Construkt Bukkit plugin for Minecraft.
 *  Copyright (C) 2020 _Razz_
 *
 *  Full disclaimer in Construkt.java
 */
public class PlayerInfo {
	private UUID plyId;
	private boolean cktEnabled = false;
	
	private BlockData blkData = null;
	private Location firstLocation = null;
	
	private CktMode mode = CktMode.NONE;
	private String shape = "cuboid";
	
	private Map<String, ShapeParameter<?>> parameters = new HashMap<String, ShapeParameter<?>>();
	private Limiter limits = null;
	
	public PlayerInfo(Player player) {
		plyId = player.getUniqueId();
		setLimits();
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
			
			setLimits();
			
			CktUtil.messagePlayer(ply, toMessage);
		}
		else {
			CktUtil.messagePlayer(ply, ChatColor.RED + "Building disabled");
			resetMode();
		}
	}
	
	/**
	 * Automatically sets the players limits based on their permissions
	 */
	public void setLimits() {
		if (!getPlayer().hasPermission("constukt.bypass_limits")) {
			for (String limiter : CktConfigOptions.getAllLimitationNames()) {
				if (getPlayer().hasPermission("construkt.limits." + limiter)) {
					limits = CktConfigOptions.getLimitation(limiter);
					break;
				}
			}
			if (limits == null)
				limits = CktConfigOptions.getLimitation("default");
		}
		else if (limits != null)
			limits = null;
	}
	
	/**
	 * Checks if the volume of an area or one of the lengths of its
	 * axes is larger than the players limit.
	 * @param secondPoint The second point of the selected area
	 * @return true if one of the limits are reached
	 */
	boolean exceedsLimit(Location secondPoint) {
		if (limits == null)
			return false;
		
		VolumeInformation vinfo = new VolumeInformation(getFirstLocation(), secondPoint);
		if (vinfo.lengthExceedsMax(limits.getMaxAxisLength())) {
			CktUtil.messagePlayer(getPlayer(), new String[] {
					ChatColor.RED + "Distance from first block exceeds your limit!",
					ChatColor.GRAY + "Your distances:",
					ChatColor.GRAY + "X: " + vinfo.getXLength() + ", Y: " + 
					vinfo.getYLength() + ", Z: " + vinfo.getZLength(),
					ChatColor.GRAY + "Max Length: " + limits.getMaxAxisLength()});
			return true;
		}
		else if(vinfo.getVolume() > limits.getMaxVolume()) {
			CktUtil.messagePlayer(getPlayer(), new String[] {
					ChatColor.RED + "Volume of this area exceeds your limit!",
					ChatColor.GRAY + "Your Volume: " + vinfo.getVolume(),
					ChatColor.GRAY + "Max Volume: " + limits.getMaxVolume()});
			return true;
		}
		return false;
	}
	
	/**
	 * Gets the players limitations
	 * @return A <code>Limiter</code> object with the max volume and max axis length or
	 * null if the player has no limits.
	 */
	public Limiter getLimitations() {
		return limits;
	}
	
	/**
	 * Gets the Bukkit Player object
	 * @return <code>Player</code>
	 */
	public Player getPlayer() {
		return Bukkit.getServer().getPlayer(plyId);
	}
	
	/**
	 * Gets the data from the first block that was placed.<br>
	 * If the block has changed from when it was first placed, the data will still be
	 * that of the original block.
	 * @return <code>BlockData</code> or <code>null</code> if the first block hasn't been assigned.
	 */
	public BlockData getBlockData() {
		return blkData;
	}
	
	/**
	 * Sets the data for a shape to use in placement.<br>
	 * This is done automatically via events.
	 * @param data The <code>BlockData</code> to be saved
	 */
	public void setBlockData(BlockData data) {
		if (data != null)
			blkData = data.clone();
		else
			blkData = null;
	}
	
	/**
	 * Gets what the player did in the first action. Used to determine what placement
	 * mode the player is in.
	 * @return <code>CktMode</code> -
	 * <code>NONE, PLACE, BREAK</code>
	 */
	public CktMode getMode() {
		return mode;
	}
	
	// Sets the action the player first took.
	void setMode(CktMode mode) {
		this.mode = mode;
	}
	
	/**
	 * Gets the <code>Location</code> of the first block placement.
	 * @return <code>Location</code>
	 */
	public Location getFirstLocation() {
		return firstLocation.clone();
	}

	// Sets the Location of the first block placement.
	void setFirstLocation(final Location firstLocation) {
		this.firstLocation = firstLocation;
	}
	
	/**
	 * Gets the shape thats currently active on the player.
	 * @return <code>BaseShape</code> - The primitive version of the shape.
	 */
	public BaseShape getShape() {
		return Shapes.getShape(shape);
	}
	
	/**
	 * Sets the players active shape. Function was made to be called via command.
	 * @param shape - The name of the shape
	 */
	public void setShape(String shape) {
		if (Shapes.hasShape(shape)) {
			if (!getPlayer().hasPermission("construkt.shape." + shape.toLowerCase())) {
				CktUtil.messagePlayer(getPlayer(), ChatColor.RED + 
						"You do not have permission to use the " + shape + " shape!");
				return;
			}
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
	
	// Method for player to get information on a parameter via command.
	void getParameterInfo(String name) {
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
	
	/**
	 * Gets a set parameter via its name.
	 * @param name The name of the parameter
	 * @return <code>ShapeParameter</code> in its primitive form or 
	 * <code>null</code> if one isn't found by the name entered.
	 */
	public ShapeParameter<?> getParameter(String name) {
		if (parameters.containsKey(name))
			return parameters.get(name);
		return null;
	}
	
	/**
	 * Gets all set parameter names.
	 * @return A <code>Set</code> of <code>Strings</code> of the parameters.
	 */
	public Set<String> getAllParameterKeys() {
		return new HashSet<String>(parameters.keySet());
	}
	
	/**
	 * Gets a copy of the <code>Map</code> containing all the set parameters.
	 * @return A copy of the parameters.
	 */
	public Map<String, ShapeParameter<?>> getAllParameters(){
		return new HashMap<String, ShapeParameter<?>>(parameters);
	}
	
	// The method run via the parameter command.
	void setParameter(String name, String value) {
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
	
	private void clearParameters() {
		parameters.clear();
	}
	
	public void resetMode() {
		setBlockData(null);
		setMode(CktMode.NONE);
		setFirstLocation(null);
	}

}
