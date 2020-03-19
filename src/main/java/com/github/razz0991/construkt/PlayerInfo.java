package com.github.razz0991.construkt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;

import com.github.razz0991.construkt.CktConfigOptions.Limiter;
import com.github.razz0991.construkt.filters.BaseFilter;
import com.github.razz0991.construkt.filters.Filters;
import com.github.razz0991.construkt.parameters.AxisCktParameter;
import com.github.razz0991.construkt.parameters.BooleanCktParameter;
import com.github.razz0991.construkt.parameters.CktParameter;
import com.github.razz0991.construkt.parameters.IntegerCktParameter;
import com.github.razz0991.construkt.shapes.BaseShape;
import com.github.razz0991.construkt.shapes.Shapes;
import com.github.razz0991.construkt.utility.CktBlockContainer;
import com.github.razz0991.construkt.utility.CktMode;
import com.github.razz0991.construkt.utility.AreaInfo;

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
	private BaseShape shape;
	private Map<String, BaseFilter> filters = new HashMap<String, BaseFilter>();

	private Limiter limits = null;
	private List<CktBlockContainer> undoList = new ArrayList<CktBlockContainer>();
	private List<CktBlockContainer> redoList = new ArrayList<CktBlockContainer>();
	
	BooleanCktParameter exactCopy = new BooleanCktParameter(true);
	
	private boolean canBuild = true;
	
	public PlayerInfo(Player player) {
		plyId = player.getUniqueId();
		shape = Shapes.getShape(this, "cuboid");
		setLimits();
	}
	
	public boolean isConstruktEnabled() {
		return cktEnabled;
	}
	
	public void setCanBuild(boolean value) {
		canBuild = value;
	}
	
	public boolean getCanBuild() {
		return canBuild;
	}
	
	public void toggleConstruktEnabled() {
		Player ply = getPlayer();
		if (ply.getGameMode() != GameMode.CREATIVE) {
			CktUtil.messagePlayer(ply, "You must be in creative mode to use Construkt");
			return;
		}
		
		cktEnabled = !cktEnabled;
		
		if (cktEnabled) {
			int extraMessages = 2;
			if (filters.size() > 0)
				extraMessages = 3;
			
			String[] toMessage = new String[extraMessages + shape.getParameterNames().size()];
			toMessage[0] = ChatColor.AQUA + "Ready to build!";
			toMessage[1] = ChatColor.DARK_AQUA + "Shape: " + ChatColor.RESET + shape.getName();
			
			int inc = 2;
			for (String msg : getChatParameters()) {
				toMessage[inc] = msg;
				inc++;
			}

			if (filters.size() > 0) {
				toMessage[toMessage.length - 1] = ChatColor.DARK_AQUA + "Filters: " + ChatColor.RESET +
													String.join(", ", filters.keySet());
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
		if (!getPlayer().hasPermission("construkt.bypass_limits")) {
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
		
		AreaInfo vinfo = shape.getVolumeInformation(getFirstLocation(), secondPoint);
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
		return shape;
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
			this.shape = Shapes.getShape(this, shape);
//			clearShapeParameters();
			
			String[] toMessage = new String[1 + this.shape.getParameterNames().size()];
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
		String[] toMessage = new String[shape.getParameterNames().size()];
		int inc = 0;
		for (String par : shape.getParameterNames()) {
			CktParameter<?> parObj = shape.getParameter(par);
			if (parObj instanceof BooleanCktParameter) {
				toMessage[inc] = ChatColor.DARK_AQUA + par + ": " + ChatColor.RESET + 
						((BooleanCktParameter)parObj).getParameter();
			}
			else if (parObj instanceof IntegerCktParameter) {
				IntegerCktParameter intPar = (IntegerCktParameter)parObj;
				String out = ChatColor.DARK_AQUA + par + ": " + ChatColor.RESET + 
						intPar.getParameter();
				if (intPar.isLimited()) {
					out += ChatColor.GRAY + " (" + intPar.getMinValue() + " to " + intPar.getMaxValue() + ")";
				}
				toMessage[inc] = out;
			}
			else if (parObj instanceof AxisCktParameter) {
				AxisCktParameter axisPar = (AxisCktParameter)parObj;
				String out = ChatColor.DARK_AQUA + par + ": " + ChatColor.RESET + 
						axisPar.getParameter();
				toMessage[inc] = out;
			}
			inc++;
		}
		
		return toMessage;
	}
	
	// Parameter info methods for commands
	void getShapeParameterInfo(String name) {
		String lName = name.toLowerCase();
		if (!shape.hasParameter(name)) {
			CktUtil.messagePlayer(getPlayer(), "No parameter called \"" + name + "\" is in the " + shape + " shape.");
			return;
		}
		
		getParameterInfo(name, shape.getParameter(lName));
	}
	
	void getFilterParameterInfo(String name) {
		String lName = name.toLowerCase();
		if (!hasFilterParameter(lName)) {
			CktUtil.messagePlayer(getPlayer(), "No parameter called \"" + name + "\" is in the active filters.");
			return;
		}
		
		getParameterInfo(name, getFilterParameter(lName));
	}
	
	private void getParameterInfo(String name, CktParameter<?> parameter) {
		String[] toMessage = new String[2];
		
		if (parameter instanceof BooleanCktParameter) {
			toMessage[0] = ChatColor.AQUA + name + " accepts boolean values.";
			toMessage[1] = ChatColor.DARK_AQUA + "Current Value: " + ChatColor.RESET + 
					((BooleanCktParameter)parameter).getParameter();
		}
		else if (parameter instanceof IntegerCktParameter) {
			IntegerCktParameter intPar = (IntegerCktParameter)parameter;
			toMessage[0] = ChatColor.AQUA + name + " accepts full number values.";
			
			if (intPar.isLimited()) {
				toMessage[0] += ChatColor.GRAY + " (" + intPar.getMinValue() + " to " + intPar.getMaxValue() + ")";
			}
			
			toMessage[1] = ChatColor.DARK_AQUA + "Current Value: " + ChatColor.RESET + 
					intPar.getParameter();;
		}
		else if (parameter instanceof AxisCktParameter) {
			toMessage[0] = ChatColor.AQUA + name + " accepts axis values " + ChatColor.GRAY + "(x, y or z)";
			toMessage[1] = ChatColor.DARK_AQUA + "Current Value: " + ChatColor.RESET +
					parameter.getParameter();
		}
		
		CktUtil.messagePlayer(getPlayer(), toMessage);
	}
	
	/**
	 * Gets all set shape parameter names.
	 * @return A <code>List</code> of <code>Strings</code> of the parameters.
	 */
	public List<String> getAllShapeParameterKeys() {
		return shape.getParameterNames();
	}
	
	/**
	 * Gets all set filter parameter names.
	 * @return A <code>Set</code> of <code>Strings</code> of the parameters.
	 */
	public List<String> getAllFilterParameterKeys() {
		List<String> keys = new ArrayList<String>();
		for (BaseFilter filter : filters.values()) {
			keys.addAll(filter.getParameterNames());
		}
		return keys;
	}
	
	private boolean hasFilterParameter(String name) {
		if (!name.contains("."))
			return false;
		String filterName = name.split("\\.")[0];
		if (hasFilterActive(filterName)) {
			return filters.get(filterName).hasParameter(name);
		}
		return false;
	}
	
	private CktParameter<?> getFilterParameter(String name) {
		if (!name.contains("."))
			return null;
		String filterName = name.split("\\.")[0];
		if (hasFilterActive(filterName)) {
			return filters.get(filterName).getParameter(name);
		}
		return null;
	}
	
	// Set parameters via commands
	void setShapeParameter(String name, String value) {
		String lName = name.toLowerCase();
		if (!shape.hasParameter(lName)) {
			CktUtil.messagePlayer(getPlayer(), "No parameter called \"" + name + "\" is in the " + shape + " shape.");
			return;
		}
		
		setParameter(name, shape.getParameter(lName), value);
	}
	
	void setFilterParameter(String name, String value) {
		String lName = name.toLowerCase();
		if (!hasFilterParameter(lName)) {
			CktUtil.messagePlayer(getPlayer(), "No parameter called \"" + name + "\" is in any of your active filters");
			return;
		}
		
		setParameter(name, getFilterParameter(lName), value);
	}
	
	private void setParameter(String name, CktParameter<?> parameter, String value) {
		if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
			if (!(parameter instanceof BooleanCktParameter)) {
				CktUtil.messagePlayer(getPlayer(), "\"" + name + "\" does not take a boolean value.");
				return;
			}
			((BooleanCktParameter)parameter).setParameter(Boolean.parseBoolean(value));
			CktUtil.messagePlayer(getPlayer(), "Set \"" + name + "\" to " + value);
			return;
		}
		else if (CktUtil.isInteger(value)) {
			if (!(parameter instanceof IntegerCktParameter)) {
				CktUtil.messagePlayer(getPlayer(), "\"" + name + "\" does not take a number value.");
				return;
			}
			IntegerCktParameter intPar = (IntegerCktParameter)parameter;
			intPar.setParameter(Integer.parseInt(value));
			CktUtil.messagePlayer(getPlayer(), "Set \"" + name + "\" to " + intPar.getParameter());
			return;
		}
		else if (value.equalsIgnoreCase("x") || value.equalsIgnoreCase("y") || value.equalsIgnoreCase("z")) {
			if (!(parameter instanceof AxisCktParameter)) {
				CktUtil.messagePlayer(getPlayer(), "\"" + name + "\" does not take an axis value.");
				return;
			}
			((AxisCktParameter)parameter).setParameter(value.toLowerCase().charAt(0));
			CktUtil.messagePlayer(getPlayer(), "Set \"" + name + "\" axis to " + parameter.getParameter());
			return;
		}
		CktUtil.messagePlayer(getPlayer(), "\"" + value + "\" is not a valid value.");
	}
	
	public void resetMode() {
		setBlockData(null);
		setMode(CktMode.NONE);
		setFirstLocation(null);
	}
	
	// Checks if the player has an active filter
	private boolean hasFilterActive(String name) {
		return filters.containsKey(name);
	}
	
	/**
	 * Activates a filter on the player
	 * @param name The name of the {@link BaseFilter}
	 */
	public void addFilter(String name) {
		String lName = name.toLowerCase();
		if (Filters.hasFilter(name) && !hasFilterActive(lName)) {
			if (!getPlayer().hasPermission("construkt.filter." + lName)) {
				CktUtil.messagePlayer(getPlayer(), ChatColor.RED + 
						"You do not have permission to use the " + lName + " filter!");
				return;
			}
			BaseFilter toAdd = Filters.getFilter(lName);
			filters.put(toAdd.getName(), toAdd);
			
			CktUtil.messagePlayer(getPlayer(), ChatColor.AQUA + "Added the filter " + lName);
			return;
		}
		else if(hasFilterActive(lName)) {
			CktUtil.messagePlayer(getPlayer(), "You already have the " + lName + " filter active.");
		}
		CktUtil.messagePlayer(getPlayer(), "No filter found by the name \"" + lName + "\"");
	}
	
	/**
	 * Gets all active filters on the player
	 * @return An array containing all the {@link BaseFilter}s the player has active
	 */
	public BaseFilter[] getFilters() {
		BaseFilter[] filters = new BaseFilter[this.filters.size()];
		int inc = 0;
		for (BaseFilter filter : this.filters.values()) {
			filters[inc++] = filter;
		}
		return filters;
	}
	
	String[] getFilterNames() {
		String[] filters = new String[this.filters.size()];
		int inc = 0;
		for (String filter : this.filters.keySet()) filters[inc++] = filter;
		return filters;
	}
	
	/**
	 * Clears all active filters from the player
	 */
	public void clearFilters() {
		filters.clear();
		
		CktUtil.messagePlayer(getPlayer(), "Filters cleared");
	}
	
	/**
	 * Removes a filter from the player
	 * @param name The name of the {@link BaseFilter} to remove
	 */
	public void removeFilter(String name) {
		String lName = name.toLowerCase();
		if (hasFilterActive(lName)) {
			filters.remove(lName);
			
			CktUtil.messagePlayer(getPlayer(), "Removed the " + name + " filter");
			return;
		}
		CktUtil.messagePlayer(getPlayer(), "You don't have a filter active with the name " + name);
	}
	
	public void addUndo(CktBlockContainer container) {
		addUndoRedo(container, undoList);
	}
	
	public void addRedo(CktBlockContainer container) {
		addUndoRedo(container, redoList);
	}
	
	private void addUndoRedo(CktBlockContainer container, List<CktBlockContainer> list) {
		if (container == null)
			return;
		
		list.add(0, container);
		if (list.size() > CktConfigOptions.getUndoRedoLimit())
			list.remove(undoList.size() - 1);
	}
	
	public void undo() {
		if (undoRedo(undoList, redoList))
			CktUtil.messagePlayer(getPlayer(), "Undone changes.");
		else
			CktUtil.messagePlayer(getPlayer(), "Nothing to undo");
	}
	
	public void redo() {
		if (undoRedo(redoList, undoList))
			CktUtil.messagePlayer(getPlayer(), "Redone changes.");
		else
			CktUtil.messagePlayer(getPlayer(), "Nothing to redo");
	}
	
	private boolean undoRedo(List<CktBlockContainer> from, List<CktBlockContainer> to) {
		if (!from.isEmpty()) {
			CktBlockContainer result = null;
			int index = 0;
			for (CktBlockContainer cont : from) {
				if (cont.isFinalized()) {
					result = cont.replaceBlocks();
					break;
				}
				index++;
			}
			if (result != null) {
				addUndoRedo(result, to);
				from.remove(index);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Gets the value of the specified permission, if set.<br><br>
	 * If a permission override is not set on this object, the default value of the permission will be returned.
	 * @param permission Name of the permission
	 * @return Value of the permission
	 */
	public boolean hasPermission(String permission) {
		return getPlayer().hasPermission(permission);
	}
	
	/**
	 * Checks if the player has exact copy enabled for block placement.
	 * @return true if exact copy is enabled
	 */
	public boolean useExactCopy() {
		return exactCopy.getParameter();
	}
	
	void setUseExactCopy(String value) {
		setParameter("exact copy", exactCopy, value);
	}
	
	void getUseExactCopyInfo() {
		getParameterInfo("exact copy", exactCopy);
	}

}








