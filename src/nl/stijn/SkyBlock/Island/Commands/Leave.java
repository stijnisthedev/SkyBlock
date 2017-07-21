package nl.stijn.SkyBlock.Island.Commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import nl.stijn.SkyBlock.SkyBlock;
import nl.stijn.SkyBlock.Island.Island;
import nl.stijn.SkyBlock.Island.IslandManager;
import nl.stijn.SkyBlock.Utils.ScreenUtil;

public class Leave extends SubCommand {

	@Override
	public void onCommand(Player p, String[] args) {
		if(IslandManager.getInstance().GetIsland(p) == null) {
			p.sendMessage(ScreenUtil.prefix + "You don't have an island you can leave!");
			return;
		}
		Island i = IslandManager.getInstance().GetIsland(p);
		if(i.getOwner().equals(p.getUniqueId())) {
			p.sendMessage(ScreenUtil.prefix + "You cannot leave your own island please remove it by /island remove!");
			return;
		}
		i.removeOwner(p.getUniqueId());
		p.sendMessage(ScreenUtil.prefix + "You succesfully leaved the island!");

		if(SkyBlock.p.getConfig().getString("world.WL") == null) {
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "spawn " + p.getName());
		} else {
			World w = Bukkit.getWorld(SkyBlock.p.getConfig().getString("world.WL"));
			p.sendMessage(ScreenUtil.prefix + "Teleported to spawn!");
			p.teleport(new Location(w, w.getSpawnLocation().getX(), w.getSpawnLocation().getY(), w.getSpawnLocation().getZ(), w.getSpawnLocation().getYaw(), w.getSpawnLocation().getPitch()));
		}
		p.getInventory().clear();
		p.setHealth(20);
		p.setFoodLevel(20);
		p.getInventory().setArmorContents(null);
		return;
	}

	@Override
	public String name() {
		return "leave";
	}

	@Override
	public String info() {
		return "Leave an island!";
	}

	@Override
	public String[] aliases() {
		return new String [] {"l"};
	}

	@Override
	public boolean admin() {
		return false;
	}

}
