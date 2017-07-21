package nl.stijn.SkyBlock.Island.Commands;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Instrument;
import org.bukkit.Location;
import org.bukkit.Note;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import nl.stijn.SkyBlock.SkyBlock;
import nl.stijn.SkyBlock.Island.Island;
import nl.stijn.SkyBlock.Island.IslandManager;
import nl.stijn.SkyBlock.Utils.ScreenUtil;

public class Home extends SubCommand {

	public static ArrayList<UUID> stil = new ArrayList<UUID>();

	@Override
	public void onCommand(final Player p, String[] args) {
		if(args.length == 0) {
		
		final Island i = IslandManager.getInstance().GetIsland(p);
		if(i == null) {
			p.sendMessage(ScreenUtil.prefix + "You don't have an island!");
			return;
		}
		if(stil.contains(p.getUniqueId())) {
			p.sendMessage(ScreenUtil.prefix + "Please wait...");
			return;
		}
		if(!i.getOwners().contains(p.getUniqueId())) {
			if(!i.getBounds().contains(p.getLocation())) {
				p.sendMessage(ScreenUtil.prefix + "You can only set a home in your island!");
				return;
			}
		}
		if(p.hasPermission("skyblock.timer")) {
			p.teleport(i.getHome().clone().add(0.5,0,0.5));
			p.playNote(p.getLocation(), Instrument.PIANO, new Note(3));
			p.sendMessage(ScreenUtil.prefix + "You teleported to your home island!");
			return;
		}
		p.sendMessage(ScreenUtil.prefix + "Teleporting in 3 seconds!");
		stil.add(p.getUniqueId());
		Bukkit.getScheduler().scheduleSyncDelayedTask(SkyBlock.p, new Runnable() {
			@Override
			public void run() {
				if(p.isOnline()) {
					if(stil.contains(p.getUniqueId())) {
						Location l = i.getHome();
						p.teleport(l.clone().add(0.5,0,0.5));
						p.playNote(p.getLocation(), Instrument.PIANO, new Note(3));
						p.sendMessage(ScreenUtil.prefix + "You teleported to your home island!");
						stil.remove(p.getUniqueId());
					}
				}
			}
		}, 60L);
		return;
		}
		if(args.length >= 1) {
			if(!p.hasPermission("skyblock.home.other")) {
				p.performCommand("is home");
				return;
			}
			@SuppressWarnings("deprecation")
			OfflinePlayer of = Bukkit.getOfflinePlayer(args[0]);
			if(!of.hasPlayedBefore()) {
				p.sendMessage(ScreenUtil.prefix + "Sorry, this player does not exist!");
				return;
			}
			Island is = IslandManager.getInstance().getIslandByOfflineUUID(of.getUniqueId());
			if(is == null) {
				p.sendMessage(ScreenUtil.prefix + "Sorry, this player doesn't have an island!");
				return;
			}
			p.teleport(is.getHome().clone().add(0.5,0,0.5));
			p.sendMessage(ScreenUtil.prefix + "You teleported to the island of " + of.getName() + "!");
			return;
			
		}

	}

	@Override
	public String name() {
		return "home";
	}

	@Override
	public String info() {
		return "Get to your home island!";
	}

	@Override
	public String[] aliases() {
		return new String [] {"h"};
	}

	@Override
	public boolean admin() {
		return false;
	}

}
