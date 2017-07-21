package nl.stijn.SkyBlock.Island.Commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import nl.stijn.SkyBlock.PlayerData.PlayerManager;
import nl.stijn.SkyBlock.PlayerData.SkyPlayer;
import nl.stijn.SkyBlock.Utils.ScoreboardUtil;
import nl.stijn.SkyBlock.Utils.ScreenUtil;

public class Resets extends SubCommand {

	@Override
	public void onCommand(Player p, String[] args) {
		if(args.length == 0) {
			p.sendMessage(ScreenUtil.prefix + "Please specify set/add/remove/get");
			return;
		}
		if(args.length == 1) {
			p.sendMessage(ScreenUtil.prefix + "Please specify a player!");
			return;
		}
		Player of = Bukkit.getPlayer(args[1]);
		if(!of.isOnline() || of == null){
			p.sendMessage(ScreenUtil.prefix + "This player is not online!");
			return;
		}
		SkyPlayer sp = PlayerManager.getInstance().getSkyPlayer(of);
		if(args[0].equalsIgnoreCase("set")) {
			if(args.length == 2) {
				p.sendMessage(ScreenUtil.prefix + "Please specify a number!");
				return;
			}
			int number = 0;
			try {
				number = Integer.parseInt(args[2]);
			} catch(Exception e) {
				p.sendMessage(ScreenUtil.prefix + "Please specify a valid number!");
				return;
			}
			sp.setResets(number);
			p.sendMessage(ScreenUtil.prefix + "The player " + of.getName() + " now has " + sp.getResets() + " resets left!");
			if(of.isOnline()) {
				of.sendMessage(ScreenUtil.prefix + "You now have " + sp.getResets() + " resets left!");
				if(sp.getBoards()) {
					ScoreboardUtil.updateScorePlayer(of);
				} else {
					ScoreboardUtil.showByTime(of, 5);
				}

			}
			return;
		}
		if(args[0].equalsIgnoreCase("add")) {
			if(args.length == 2) {
				p.sendMessage(ScreenUtil.prefix + "Please specify a number!");
				return;
			}
			int number = 0;
			try {
				number = Integer.parseInt(args[2]);
			} catch(Exception e) {
				p.sendMessage(ScreenUtil.prefix + "Please specify a valid number!");
				return;
			}
			sp.addResets(number);
			p.sendMessage(ScreenUtil.prefix + "The player " + of.getName() + " now has " + sp.getResets() + " resets left!");
			if(of.isOnline()) {
				of.sendMessage(ScreenUtil.prefix + "You now have " + sp.getResets() + " resets left!");
				if(sp.getBoards()) {
					ScoreboardUtil.updateScorePlayer(of);
				} else {
					ScoreboardUtil.showByTime(of, 5);
				}
			}
			return;
		}
		if(args[0].equalsIgnoreCase("remove")) {
			if(args.length == 2) {
				p.sendMessage(ScreenUtil.prefix + "Please specify a number!");
				return;
			}
			int number = 0;
			try {
				number = Integer.parseInt(args[2]);
			} catch(Exception e) {
				p.sendMessage(ScreenUtil.prefix + "Please specify a valid number!");
				return;
			}
			sp.removeResets(number);
			p.sendMessage(ScreenUtil.prefix + "The player " + of.getName() + " now has " + sp.getResets() + " resets left!");
			if(of.isOnline()) {
				of.sendMessage(ScreenUtil.prefix + "You now have " + sp.getResets() + " resets left!");
				if(sp.getBoards()) {
					ScoreboardUtil.updateScorePlayer(of);
				} else {
					ScoreboardUtil.showByTime(of, 5);
				}
			}
			return;
		}
		if(args[0].equalsIgnoreCase("get")) {
			p.sendMessage(ScreenUtil.prefix + "The player " + of.getName() + " has " + sp.getResets() + " resets left!");
			if(sp.getBoards()) {
				ScoreboardUtil.updateScorePlayer(of);
			}
			return;
		}
	}

	@Override
	public String name() {
		return "resets";
	}

	@Override
	public String info() {
		return "Set the resets of a player!";
	}

	@Override
	public String[] aliases() {
		return new String[] {"re"};
	}

	@Override
	public boolean admin() {
		return true;
	}

}
