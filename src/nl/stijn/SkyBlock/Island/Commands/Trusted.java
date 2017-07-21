package nl.stijn.SkyBlock.Island.Commands;

import org.bukkit.entity.Player;

import nl.stijn.SkyBlock.Island.Island;
import nl.stijn.SkyBlock.Island.IslandManager;
import nl.stijn.SkyBlock.Utils.ScreenUtil;

public class Trusted extends SubCommand {

	@Override
	public void onCommand(Player p, String[] args) {
		Island i = IslandManager.getInstance().GetIsland(p);
		if(i == null) {
			p.sendMessage(ScreenUtil.prefix + "You don't have an island!");
			return;
		}
	}

	@Override
	public String name() {
		return "trusted";
	}

	@Override
	public String info() {
		return "Display trusted players!";
	}

	@Override
	public String[] aliases() {
		return new String[] {"tr"};
	}

	@Override
	public boolean admin() {
		return false;
	}

}
