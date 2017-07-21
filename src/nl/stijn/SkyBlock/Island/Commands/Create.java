package nl.stijn.SkyBlock.Island.Commands;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import nl.stijn.SkyBlock.SkyBlock;
import nl.stijn.SkyBlock.Island.IslandManager;
import nl.stijn.SkyBlock.Island.Schematics.SchematicManager;
import nl.stijn.SkyBlock.PlayerData.PlayerManager;
import nl.stijn.SkyBlock.PlayerData.SkyPlayer;
import nl.stijn.SkyBlock.Utils.ScreenUtil;

public class Create extends SubCommand {
	
	public HashMap<UUID, Integer> delay = new HashMap<UUID, Integer>();
	public HashMap<UUID, BukkitRunnable> cooldown = new HashMap<UUID, BukkitRunnable>();
	
	@Override
	public void onCommand(final Player p, String[] args) {
		if(IslandManager.getInstance().GetIsland(p) != null) {
			p.sendMessage(ScreenUtil.prefix + "Sorry, you already have an island. You can remove it by /island remove");
			return;
		}
		
		SkyPlayer sp = PlayerManager.getInstance().getSkyPlayer(p);
		if(sp.getResets() == 0) {
			p.sendMessage(ScreenUtil.prefix + "Sorry, you don't have any resets left!");
			return;
		}
		
		if(p.hasPermission("skyblock.nodelay")) {
			SchematicManager.getInstance().openMenu(p);
			p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, 1, 1);
			return;
		}
		if(delay.containsKey(p.getUniqueId())) {
			p.sendMessage(ScreenUtil.prefix + "Sorry, please wait " + delay.get(p.getUniqueId()) + " seconds!");
			return;
		}
		
		SchematicManager.getInstance().openMenu(p);
		p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, 1, 1);
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
		return "create";
	}

	@Override
	public String info() {
		return "Create an island!";
	}

	@Override
	public String[] aliases() {
		return new String[] {"c"};
	}

	@Override
	public boolean admin() {
		return false;
	}

}
