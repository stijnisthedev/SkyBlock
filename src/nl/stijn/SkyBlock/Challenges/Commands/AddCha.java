package nl.stijn.SkyBlock.Challenges.Commands;

import org.bukkit.entity.Player;

import nl.stijn.SkyBlock.Challenges.ChallengeInventory;
import nl.stijn.SkyBlock.Challenges.ChallengeTuple;
import nl.stijn.SkyBlock.Island.Commands.SubCommand;
import nl.stijn.SkyBlock.Utils.ScreenUtil;

public class AddCha extends SubCommand {
	
	@Override
	public void onCommand(Player p, String[] args) {
		if(args.length == 0) {
			p.sendMessage(ScreenUtil.prefix + "Please specify a name!");
			return;
		}
		p.sendMessage(ScreenUtil.prefix + "Now type the lore!");
		ChallengeInventory.tuple.put(p.getUniqueId(), new ChallengeTuple(args[0], null, null, null, null, null, null));
		return;
	}

	@Override
	public String name() {
		return "addcha";
	}

	@Override
	public String info() {
		return  "Add a challenge!";
	}

	@Override
	public String[] aliases() {
		return new String[] {"ach"};
	}

	@Override
	public boolean admin() {
		return true;
	}

}
