package nl.stijn.SkyBlock.Island.Commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import nl.stijn.SkyBlock.SkyBlock;
import nl.stijn.SkyBlock.Utils.ScreenUtil;

public class Shop extends SubCommand {

	@Override
	public void onCommand(final Player p, String[] args) {
		if(SkyBlock.p.getConfig().get("shop.W") == null) {
			p.sendMessage(ScreenUtil.prefix + "Shop not set!");
			return;
		}
		if(Home.stil.contains(p.getUniqueId())) {
			p.sendMessage(ScreenUtil.prefix + "Please wait...");
			return;
		}
		if(p.hasPermission("skyblock.timer")) {
			World w = Bukkit.getWorld(SkyBlock.p.getConfig().getString("shop.W"));
			double X = Double.parseDouble(SkyBlock.p.getConfig().getString("shop.X"));
			double Y = Double.parseDouble(SkyBlock.p.getConfig().getString("shop.Y"));
			double Z = Double.parseDouble(SkyBlock.p.getConfig().getString("shop.Z"));
			float YA = Float.parseFloat(SkyBlock.p.getConfig().getString("shop.YA"));
			float P = Float.parseFloat(SkyBlock.p.getConfig().getString("shop.P"));
			Location shop = new Location(w,X,Y,Z,YA,P);
			p.teleport(shop);
			p.sendMessage(ScreenUtil.prefix + "You teleported to the shop!");
			return;
		}
		p.sendMessage(ScreenUtil.prefix + "Teleporting in 3 seconds!");
		Home.stil.add(p.getUniqueId());
		Bukkit.getScheduler().scheduleSyncDelayedTask(SkyBlock.p, new Runnable() {
			@Override
			public void run() {
				if(p.isOnline()) {
					if(Home.stil.contains(p.getUniqueId())) {
						World w = Bukkit.getWorld(SkyBlock.p.getConfig().getString("shop.W"));
						double X = Double.parseDouble(SkyBlock.p.getConfig().getString("shop.X"));
						double Y = Double.parseDouble(SkyBlock.p.getConfig().getString("shop.Y"));
						double Z = Double.parseDouble(SkyBlock.p.getConfig().getString("shop.Z"));
						float YA = Float.parseFloat(SkyBlock.p.getConfig().getString("shop.YA"));
						float P = Float.parseFloat(SkyBlock.p.getConfig().getString("shop.P"));
						Location shop = new Location(w,X,Y,Z,YA,P);
						p.teleport(shop);
						p.sendMessage(ScreenUtil.prefix + "You teleported to the shop!");
						Home.stil.remove(p.getUniqueId());
					}
				}
			}
		}, 60L);
		return;
	}

	@Override
	public String name() {
		return "shop";
	}

	@Override
	public String info() {
		return "Go to the shop!";
	}

	@Override
	public String[] aliases() {
		return new String [] {"sp"};
	}

	@Override
	public boolean admin() {
		return false;
	}

}
