package nl.stijn.SkyBlock.Island.Commands;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import nl.stijn.SkyBlock.SkyBlock;
import nl.stijn.SkyBlock.Island.Island;
import nl.stijn.SkyBlock.Island.IslandManager;
import nl.stijn.SkyBlock.Island.IslandPoints;
import nl.stijn.SkyBlock.Utils.ScoreboardUtil;
import nl.stijn.SkyBlock.Utils.ScreenUtil;

public class Points extends SubCommand {

	public HashMap<UUID, Integer> delay = new HashMap<UUID, Integer>();
	public HashMap<UUID, BukkitRunnable> cooldown = new HashMap<UUID, BukkitRunnable>();

	@Override
	public void onCommand(final Player p, String[] args) {
		Island is = IslandManager.getInstance().GetIsland(p);
		if(is == null) {
			p.sendMessage(ScreenUtil.prefix + "Sorry, you don't have an island!");
			return;
		}

		if(p.hasPermission("skyblock.nodelay")) {
			int points = IslandPoints.getInstance().calculatePoints(is);
			p.sendMessage(ScreenUtil.prefix + "Your points: " + points);
			is.setPoints(points);
			IslandManager.getInstance().updateIsland(is);
			SkyBlock.points.clear();
			SkyBlock.registerPoints();
			
			IslandManager.getInstance().updateIsland(is);
			
			ScoreboardUtil.updateScoreboard(is);
			return;
		}
		if(delay.containsKey(p.getUniqueId())) {
			p.sendMessage(ScreenUtil.prefix + "Sorry, please wait " + delay.get(p.getUniqueId()) + " seconds!");
			return;
		}

		int points = IslandPoints.getInstance().calculatePoints(is);
		p.sendMessage(ScreenUtil.prefix + "Your points: " + points);
		is.setPoints(points);
		IslandManager.getInstance().updateIsland(is);
		SkyBlock.points.clear();
		SkyBlock.registerPoints();
		
		IslandManager.getInstance().updateIsland(is);
		
		ScoreboardUtil.updateScoreboard(is);
		delay.put(p.getUniqueId(), 120);
		
		cooldown.put(p.getUniqueId(), new BukkitRunnable() {
			public void run() {
				delay.put(p.getUniqueId(), delay.get(p.getUniqueId()) - 1);
				if (delay.get(p.getUniqueId()) == 0) {
					delay.remove(p.getUniqueId());
					cooldown.remove(p.getUniqueId());
					cancel();
				}
			}
		});
		cooldown.get(p.getUniqueId()).runTaskTimer(SkyBlock.p, 20, 20);
		return;

	}

	@Override
	public String name() {
		return "points";
	}

	@Override
	public String info() {
		return "Calculate your points.";
	}

	@Override
	public String[] aliases() {
		return new String[] {"p"};
	}

	@Override
	public boolean admin() {
		return false;
	}

}
