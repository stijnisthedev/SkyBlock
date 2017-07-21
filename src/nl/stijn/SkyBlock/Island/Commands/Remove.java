package nl.stijn.SkyBlock.Island.Commands;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import nl.stijn.SkyBlock.SkyBlock;
import nl.stijn.SkyBlock.Island.Island;
import nl.stijn.SkyBlock.Island.IslandManager;
import nl.stijn.SkyBlock.PlayerData.PlayerManager;
import nl.stijn.SkyBlock.PlayerData.SkyPlayer;
import nl.stijn.SkyBlock.Utils.JsonBuilder;
import nl.stijn.SkyBlock.Utils.ScoreboardUtil;
import nl.stijn.SkyBlock.Utils.ScreenUtil;
import nl.stijn.SkyBlock.Utils.JsonBuilder.ClickAction;
import nl.stijn.SkyBlock.Utils.JsonBuilder.HoverAction;

public class Remove extends SubCommand {

	public static ArrayList<UUID> uuids = new ArrayList<UUID>();

	@Override
	public void onCommand(Player p, String[] args) {
		if(IslandManager.getInstance().getIsland(p) == null) {
			p.sendMessage(ScreenUtil.prefix + "You don't have an island!");
			return;
		}
		Island is = IslandManager.getInstance().getIsland(p);
		if(args.length != 0) {
			if(args[0].equals(p.getUniqueId())) {
				if(uuids.contains(p.getUniqueId())) {
					uuids.remove(p.getUniqueId());
					p.sendMessage(ScreenUtil.prefix + "Cancelled!");
					return;
				}
			}
		}
		if(args.length == 0) {
			if(uuids.contains(p.getUniqueId())) {
				for(Player pl : Bukkit.getOnlinePlayers()) {
					if(is.getBounds().contains(pl.getLocation())) {
						if(!is.getOwners().contains(pl.getUniqueId())) {
							if(SkyBlock.p.getConfig().getString("world.WL") == null) {
								Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "spawn " + p.getName());
							} else {
								World w = Bukkit.getWorld(SkyBlock.p.getConfig().getString("world.WL"));
								p.sendMessage(ScreenUtil.prefix + "Teleported to spawn!");
								p.teleport(new Location(w, w.getSpawnLocation().getX(), w.getSpawnLocation().getY(), w.getSpawnLocation().getZ(), w.getSpawnLocation().getYaw(), w.getSpawnLocation().getPitch()));
							}
						} else {
							if(SkyBlock.p.getConfig().getString("world.WL") == null) {
								Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "spawn " + p.getName());
							} else {
								World w = Bukkit.getWorld(SkyBlock.p.getConfig().getString("world.WL"));
								p.teleport(new Location(w, w.getSpawnLocation().getX(), w.getSpawnLocation().getY(), w.getSpawnLocation().getZ(), w.getSpawnLocation().getYaw(), w.getSpawnLocation().getPitch()));
								p.sendMessage(ScreenUtil.prefix + "Teleported to spawn!");
							}
							pl.getInventory().clear();
							pl.setHealth(20);
							pl.setFoodLevel(20);
							pl.getInventory().setArmorContents(null);
						}
					}
				}

				SkyPlayer sp = PlayerManager.getInstance().getSkyPlayer(p);
				p.sendMessage(ScreenUtil.prefix + "You succesfully removed your island!");
				sp.removeResets(1);
				p.sendMessage(ScreenUtil.prefix + "You now have " + sp.getResets() + " resets left!");
				if(sp.getBoards()) {
					ScoreboardUtil.updateScorePlayer(p);
				} else {
					ScoreboardUtil.showByTime(p, 5);
				}
				IslandManager.getInstance().removeIsland(is);
				uuids.remove(p.getUniqueId());
				return;
			}
		}
		if(!uuids.contains(p.getUniqueId())) {
			JsonBuilder jb = new JsonBuilder("");
			p.sendMessage(ScreenUtil.prefix + "Are you sure?");
			jb.withText(ChatColor.GREEN + "" + ChatColor.BOLD + " Yes ");
			jb.withClickEvent(ClickAction.RUN_COMMAND, "/is remove");
			jb.withHoverEvent(HoverAction.SHOW_TEXT, ChatColor.GREEN + "Remove your island!");
			jb.withText(ChatColor.DARK_RED + "" + ChatColor.BOLD + "No");
			jb.withClickEvent(ClickAction.RUN_COMMAND, "/is remove " + p.getUniqueId());
			jb.withHoverEvent(HoverAction.SHOW_TEXT, ChatColor.RED + "Cancel this!");
			jb.sendJson(p);
			uuids.add(p.getUniqueId());
			startTimer(p);
			return;
		}

		return;
	}

	public void startTimer(final Player p) {
		Bukkit.getScheduler().scheduleSyncDelayedTask(SkyBlock.p, new Runnable() {
			@Override
			public void run() {
				if(uuids.contains(p.getUniqueId())) {
					if(p.isOnline())
						uuids.remove(p.getUniqueId());
				}
			}
		}, 400L);
	}
	@Override
	public String name() {
		return "remove";
	}

	@Override
	public String info() {
		return "Remove an island!";
	}

	@Override
	public String[] aliases() {
		return new String [] {"r"};
	}

	@Override
	public boolean admin() {
		return false;
	}

}
