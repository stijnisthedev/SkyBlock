package nl.stijn.SkyBlock.Island.Commands;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import nl.stijn.SkyBlock.SkyBlock;
import nl.stijn.SkyBlock.Island.Island;
import nl.stijn.SkyBlock.Island.IslandManager;
import nl.stijn.SkyBlock.Utils.ScreenUtil;

public class SetHome extends SubCommand {

	public HashMap<UUID, Integer> delay = new HashMap<UUID, Integer>();
	public HashMap<UUID, BukkitRunnable> cooldown = new HashMap<UUID, BukkitRunnable>();

	@Override
	public void onCommand(final Player p, String[] args) {
		if(IslandManager.getInstance().getIsland(p) == null) {
			p.sendMessage(ScreenUtil.prefix + "Sorry, you don't own an island!");
			return;
		}
		Island i = IslandManager.getInstance().getIsland(p);
		if(!i.getBounds().contains(p.getLocation())) {
			p.sendMessage(ScreenUtil.prefix + "Please set a spawn on your island!");
			return;
		}
		if(p.hasPermission("skyblock.nodelay")) {
			IslandManager.getInstance().setHome(p.getLocation(), i);
			p.sendMessage(ScreenUtil.prefix + "Succesfully set home!");
			return;
		}
		if(delay.containsKey(p.getUniqueId())) {
			p.sendMessage(ScreenUtil.prefix + "Sorry, please wait " + delay.get(p.getUniqueId()) + " seconds!");
			return;
		}
		IslandManager.getInstance().setHome(p.getLocation(), i);
		p.sendMessage(ScreenUtil.prefix + "Succesfully set home!");
		delay.put(p.getUniqueId(), 120);
		cooldown.put(p.getUniqueId(), 		new BukkitRunnable() {
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
		return "sethome";
	}

	@Override
	public String info() {
		return "Set home of island!";
	}

	@Override
	public String[] aliases() {
		return new String [] {"sh"};
	}

	@Override
	public boolean admin() {
		return false;
	}

}
