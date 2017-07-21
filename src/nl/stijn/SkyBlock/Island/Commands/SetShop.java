package nl.stijn.SkyBlock.Island.Commands;

import org.bukkit.entity.Player;

import nl.stijn.SkyBlock.SkyBlock;
import nl.stijn.SkyBlock.Utils.ScreenUtil;

public class SetShop extends SubCommand {

	@Override
	public void onCommand(Player p, String[] args) {
		p.sendMessage(ScreenUtil.prefix + "Shop set!");
		SkyBlock.p.getConfig().set("shop.W", p.getWorld().getName());
		SkyBlock.p.getConfig().set("shop.X", p.getLocation().getX());
		SkyBlock.p.getConfig().set("shop.Y", p.getLocation().getY());
		SkyBlock.p.getConfig().set("shop.Z", p.getLocation().getZ());
		SkyBlock.p.getConfig().set("shop.YA", p.getLocation().getYaw());
		SkyBlock.p.getConfig().set("shop.P", p.getLocation().getPitch());
		SkyBlock.p.saveConfig();
		return;
	}

	@Override
	public String name() {
		return "setshop";
	}

	@Override
	public String info() {
		return "Set the shop location!";
	}

	@Override
	public String[] aliases() {
		return new String [] {"ssp"};
	}

	@Override
	public boolean admin() {
		return true;
	}

}
