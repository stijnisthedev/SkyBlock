package nl.stijn.SkyBlock.Island.Commands;

import org.bukkit.entity.Player;

import nl.stijn.SkyBlock.PlayerData.PlayerManager;
import nl.stijn.SkyBlock.PlayerData.SkyPlayer;
import nl.stijn.SkyBlock.Utils.ScreenUtil;

public class Scoreboard extends SubCommand {

	@Override
	public void onCommand(Player p, String[] args) {
		if(args.length == 0) {
			p.sendMessage(ScreenUtil.prefix + "Usage: /island scoreboard (ON/OFF/TOGGLE)");
			return;
		}
		SkyPlayer sp = PlayerManager.getInstance().getSkyPlayer(p);
		if(args[0].equalsIgnoreCase("on")) {
			sp.setWants(true);
			
			return;
		}
		if(args[0].equalsIgnoreCase("off")) {
			sp.setWants(false);
			
			return;
		}
		if(args[0].equalsIgnoreCase("toggle")) {
			sp.setWants(!sp.getBoards());
			
			return;
		}
		
	}

	@Override
	public String name() {
		return "scoreboard";
	}

	@Override
	public String info() {
		return "Toggle your scoreboard!";
	}

	@Override
	public String[] aliases() {
		return new String[] {"sb"};
	}

	@Override
	public boolean admin() {
		return false;
	}

}
