package nl.stijn.SkyBlock.Island.Commands;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import nl.stijn.SkyBlock.Island.Island;
import nl.stijn.SkyBlock.Island.IslandManager;
import nl.stijn.SkyBlock.Utils.ScreenUtil;

public class Info extends SubCommand {

	@Override
	public void onCommand(Player p, String[] args) {
		if(args.length == 0) {
			p.sendMessage(ScreenUtil.prefix + "Please specify a players island!");
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
		
		p.sendMessage(ChatColor.GOLD + "-=-==-===-====-===-==-==-");
		p.sendMessage(ScreenUtil.prefix + "Island owner: ");
		p.sendMessage(ScreenUtil.prefix + Bukkit.getOfflinePlayer(is.getOwner()).getName());
		p.sendMessage(ChatColor.GOLD + "-=-==-===-====-===-==-==-");
		p.sendMessage(ScreenUtil.prefix + "Island added: ");
		int c = 1;
		for(UUID uuid : is.getOwners()) {
			OfflinePlayer ofp = Bukkit.getOfflinePlayer(uuid);
			if(ofp.equals(Bukkit.getOfflinePlayer(is.getOwner()))) {
				continue;
			}
			if(ofp.isOnline()) {
				p.sendMessage(ScreenUtil.prefix + c + ". " + ofp.getName() + " - " + ChatColor.GREEN + "Online");
			} else {
				p.sendMessage(ScreenUtil.prefix + c + ". " + ofp.getName() + " - " + ChatColor.RED + "Offline");
			}
			c++;

		}
		p.sendMessage(ChatColor.GOLD + "-=-==-===-====-===-==-==-");
		p.sendMessage(ScreenUtil.prefix + "Points: ");
		p.sendMessage(ScreenUtil.prefix + is.getPoints() + "");
		p.sendMessage(ChatColor.GOLD + "-=-==-===-====-===-==-==-");
		return;
		
	}

	@Override
	public String name() {
		return "info";
	}

	@Override
	public String info() {
		return "Get the information of an island";
	}

	@Override
	public String[] aliases() {
		return new String[] {"in"};
	}

	@Override
	public boolean admin() {
		return true;
	}

}
