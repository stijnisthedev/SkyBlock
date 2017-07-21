package nl.stijn.SkyBlock.Utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import nl.stijn.SkyBlock.SkyBlock;
import nl.stijn.SkyBlock.Island.Island;
import nl.stijn.SkyBlock.Island.IslandManager;
import nl.stijn.SkyBlock.PlayerData.PlayerManager;
import nl.stijn.SkyBlock.PlayerData.SkyPlayer;
import nl.stijn.SkyBlock.PlayerData.SkyPlayer.Scoreboards;

public class ScoreboardUtil {

	public static void setScoreboardIsland(Player p) {
		Island is = IslandManager.getInstance().getIslandOfPlayer(p);
		if(is == null) {
			return;
		}
		ScoreboardManager manager = Bukkit.getScoreboardManager();
		Scoreboard sb = manager.getNewScoreboard();
		Objective obj = sb.registerNewObjective("sb", "dummy");
		obj.setDisplaySlot(DisplaySlot.SIDEBAR); 
		obj.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + " DroxSkyBlock   ");
		obj.getScore(ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + "---------------").setScore(6);
		obj.getScore(ChatColor.YELLOW + "" + ChatColor.BOLD + "Money:").setScore(5);
		obj.getScore(ChatColor.GOLD + "" + ChatColor.BOLD + SkyBlock.econ.getBalance(p)).setScore(4);
		obj.getScore("   ").setScore(3);
		obj.getScore(ChatColor.YELLOW + "" + ChatColor.BOLD + "Points:").setScore(2);
		obj.getScore(ChatColor.GOLD + "" + ChatColor.BOLD + is.getPoints()).setScore(1);
		obj.getScore(ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + "--------------- ").setScore(0);
		p.setScoreboard(sb);
	}

	public static void setScoreboard(Player p) {
		ScoreboardManager manager = Bukkit.getScoreboardManager();
		Scoreboard sb = manager.getNewScoreboard();
		Objective obj = sb.registerNewObjective("sb", "dummy");
		obj.setDisplaySlot(DisplaySlot.SIDEBAR); 
		obj.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + " DroxSkyBlock   ");
		obj.getScore(ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + "---------------").setScore(4);
		obj.getScore(ChatColor.YELLOW + "" + ChatColor.BOLD + "Money:").setScore(3);
		obj.getScore(ChatColor.GOLD + "" + ChatColor.BOLD + SkyBlock.econ.getBalance(p)).setScore(2);
		obj.getScore(ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + "--------------- ").setScore(1);
		p.setScoreboard(sb);
	}

	public static void showByTime(final Player p, int seconds) {
		SkyPlayer sp = PlayerManager.getInstance().getSkyPlayer(p);
		if(!sp.getBoards()) {
			Scoreboards current = sp.getCurrentBoard();
			if(current.equals(Scoreboards.PLAYER)) {
				setScoreboard(p);
			} else {
				setScoreboardIsland(p);
			}
			Bukkit.getScheduler().scheduleSyncDelayedTask(SkyBlock.p, new Runnable() {
				public void run() {
					p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
				}
			}, 20 * seconds);
		}
	}

	public static void updateScoreboard(Island is) {
		for(Player p : Bukkit.getOnlinePlayers()) {
			if(is.getBounds().contains(p.getLocation())) {
				setScoreboardIsland(p);
			}
		}
	}

	public static void updateScorePlayer(Player p) {
		SkyPlayer sp = PlayerManager.getInstance().getSkyPlayer(p);
		Scoreboards current = sp.getCurrentBoard();
		if(current.equals(Scoreboards.PLAYER)) {
			setScoreboard(p);
		} else {
			setScoreboardIsland(p);
		}
	}
}