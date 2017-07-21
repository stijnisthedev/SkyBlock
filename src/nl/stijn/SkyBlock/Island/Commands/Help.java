package nl.stijn.SkyBlock.Island.Commands;

import org.bukkit.entity.Player;

import nl.stijn.SkyBlock.Challenges.CategoryManager;
import nl.stijn.SkyBlock.Utils.ScreenUtil;

public class Help extends SubCommand {

	@Override
	public void onCommand(Player p, String[] args) {
		for (SubCommand c : CommandManager.commands) {
			if(c.name().equalsIgnoreCase(CategoryManager.getInstance().pass)) continue;
			if(c.admin()) {
				if(p.hasPermission("skyblock.admin.command")) {
					p.sendMessage(ScreenUtil.prefix + "/island " + c.name() + "(" + CommandManager.aliases(c) + ")" + " - " + c.info());
				} else {
					continue;
				}

			} else {
				p.sendMessage(ScreenUtil.prefix + "/island " + c.name() + "(" + CommandManager.aliases(c) + ")" + " - " + c.info());
			}
		}
	}

	@Override
	public String name() {
		return "help";
	}

	@Override
	public String info() {
		return "Display all commands!";
	}

	@Override
	public String[] aliases() {
		return new String[] {"he"};
	}

	@Override
	public boolean admin() {
		return false;
	}

}
