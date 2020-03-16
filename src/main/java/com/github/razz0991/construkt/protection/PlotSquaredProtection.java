package com.github.razz0991.construkt.protection;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.github.intellectualsites.plotsquared.plot.PlotSquared;

public class PlotSquaredProtection implements ProtectionBase {

	@Override
	public boolean canBuild(Location loc, Player ply) {
		com.github.intellectualsites.plotsquared.plot.object.Location ploc = 
				new com.github.intellectualsites.plotsquared.plot.object.Location(
						loc.getWorld().getName(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
		// If not plot world, ignore
		if (PlotSquared.get().getPlotManager(ploc) == null)
			return true;
		
		if (ploc.isPlotArea()) {
			if (ploc.getPlotAbs() != null) {
				return ploc.getPlotAbs().isAdded(ply.getUniqueId()) ||
						ply.hasPermission("plots.admin.build.unowned") ||
						ply.hasPermission("plots.admin.build.other");
			}
		}
		return false;
	}

}
