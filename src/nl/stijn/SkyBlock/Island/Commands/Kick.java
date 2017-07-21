package nl.stijn.SkyBlock.Island.Commands;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import nl.stijn.SkyBlock.SkyBlock;
import nl.stijn.SkyBlock.Island.Island;
import nl.stijn.SkyBlock.Island.IslandManager;
import nl.stijn.SkyBlock.Utils.JsonBuilder;
import nl.stijn.SkyBlock.Utils.JsonBuilder.ClickAction;
import nl.stijn.SkyBlock.Utils.JsonBuilder.HoverAction;
import nl.stijn.SkyBlock.Utils.ScreenUtil;

public class Kick extends SubCommand {

	public static HashMap<UUID, UUID> uuids = new HashMap<UUID, UUID>();

	@Override
	public void onCommand(Player p, String[] args) {
		Island is = IslandManager.getInstance().getIsland(p);
		if(is == null) {
			p.sendMessage(ScreenUtil.prefix + "Sorry, you don't have/own an island!");
			return;
		}
		if(args.length == 0) {
			if(!uuids.containsKey(p.getUniqueId())) {
				p.sendMessage(ScreenUtil.prefix + "Please specify a player!");
				return;
			} else {
				is.removeOwner(uuids.get(p.getUniqueId()));
				IslandManager.getInstance().updateIsland(is);
				p.sendMessage(ScreenUtil.prefix + "Removed player " + Bukkit.getOfflinePlayer(uuids.get(p.getUniqueId())).getName() + "!");
				OfflinePlayer t = Bukkit.getOfflinePlayer(uuids.get(p.getUniqueId()));
				if(t.isOnline()) {
					Player p1 = t.getPlayer();
					p1.sendMessage(ScreenUtil.prefix + "You've been kicked from "+Bukkit.getPlayer(is.getOwner()).getName()+" island!");
				}
				uuids.remove(p.getUniqueId());
				return;
			}
		}
		if(args[0].equals("no")) {
			if(uuids.containsKey(p.getUniqueId())) {
				uuids.remove(p.getUniqueId());
				p.sendMessage(ScreenUtil.prefix + "Cancelled!");
				return;
			} else {
				p.sendMessage(ScreenUtil.prefix + "This player is not online/valid!");
				return;
			}
		}
		Player target = Bukkit.getPlayer(args[0]);
		if(target == null || !target.isOnline()) {
			p.sendMessage(ScreenUtil.prefix + "This player is not online/valid!");
			return;
		}
		if(!uuids.containsKey(p.getUniqueId())) {
			JsonBuilder jb = new JsonBuilder("");
			p.sendMessage(ScreenUtil.prefix + "Are you sure?");
			jb.withText(ChatColor.GREEN + "" + ChatColor.BOLD + " Yes ");
			jb.withClickEvent(ClickAction.RUN_COMMAND, "/is kick");
			jb.withHoverEvent(HoverAction.SHOW_TEXT, ChatColor.GREEN + "Kick the player "+ target.getName() +"!");
			jb.withText(ChatColor.DARK_RED + "" + ChatColor.BOLD + "No");
			jb.withClickEvent(ClickAction.RUN_COMMAND, "/is kick no");
			jb.withHoverEvent(HoverAction.SHOW_TEXT, ChatColor.RED + "Don't kick the player "+ target.getName() +"!");
			jb.sendJson(p);
			uuids.put(p.getUniqueId(), target.getUniqueId());
			startTimer(p, target);
			return;
		}
		return;

	}

	public void startTimer(final Player p, final Player t) {
		Bukkit.getScheduler().scheduleSyncDelayedTask(SkyBlock.p, new Runnable() {
			@Override
			public void run() {
				if(uuids.containsKey(p.getUniqueId()) && uuids.containsValue(t.getUniqueId())) {
					if(p.isOnline() && t.isOnline())
					uuids.remove(p.getUniqueId(), t.getUniqueId());
				}
			}
		}, 400L);
	}

	@Override
	public String name() {
		return "kick";
	}

	@Override
	public String info() {
		return "Kick a player from your island!";
	}

	@Override
	public String[] aliases() {
		return new String[] {"k"};
	}

	@Override
	public boolean admin() {
		return false;
	}

}
