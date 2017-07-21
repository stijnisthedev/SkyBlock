package nl.stijn.SkyBlock.Island.Commands;

import org.bukkit.entity.Player;

import nl.stijn.SkyBlock.SkyBlock;
import nl.stijn.SkyBlock.Utils.ScreenUtil;

public class SetWorld extends SubCommand {

	@Override
	public void onCommand(Player p, String[] args) {
		if(args.length == 0) {
			p.sendMessage(ScreenUtil.prefix + "Please specify what type? Lobby/Skyworld");
			return;
		}
		if(args[0].equalsIgnoreCase("lobby")) {
			SkyBlock.p.getConfig().set("world.WL", p.getWorld().getName());
			SkyBlock.p.saveConfig();
			p.sendMessage(ScreenUtil.prefix + "Done!");
			return;
		}
		if(args[0].equalsIgnoreCase("skyworld")) {
			SkyBlock.p.getConfig().set("world.W", p.getWorld().getName());
			SkyBlock.p.saveConfig();
			p.sendMessage(ScreenUtil.prefix + "Done!");
			return;
		}
		
	}

	@Override
	public String name() {
		return "setworld";
	}

	@Override
	public String info() {
		return "Set the world!";
	}

	@Override
	public String[] aliases() {
		return new String[] {"sw"};
	}

	@Override
	public boolean admin() {
		return false;
	}

}
