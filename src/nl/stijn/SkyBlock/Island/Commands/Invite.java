package nl.stijn.SkyBlock.Island.Commands;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import nl.stijn.SkyBlock.SkyBlock;
import nl.stijn.SkyBlock.GUI.GUI;
import nl.stijn.SkyBlock.Island.IslandManager;
import nl.stijn.SkyBlock.Utils.JsonBuilder;
import nl.stijn.SkyBlock.Utils.ScreenUtil;
import nl.stijn.SkyBlock.Utils.JsonBuilder.ClickAction;
import nl.stijn.SkyBlock.Utils.JsonBuilder.HoverAction;

public class Invite extends SubCommand {

	public static HashMap<UUID, UUID> invites = new HashMap<UUID, UUID>();
	
	@Override
	public void onCommand(Player p, String[] args) {
		if(args.length == 0) {
			p.openInventory(GUI.getInstance().getPlayerHeads(p, ChatColor.RED + "Click player to invite", 1, false, null));
			return;
		}
		Player t = Bukkit.getPlayer(args[0]);
		if(t == null || !t.isOnline()) {
			p.sendMessage(ScreenUtil.prefix + "Sorry, this player is not online/doesn't exist!");
			return;
		}
		if(t.equals(p)) {
			p.sendMessage(ScreenUtil.prefix + "Sorry, you can not invite yourself!");
			return;
		}
		if(invites.containsValue(t.getUniqueId()) ) {
			p.sendMessage(ScreenUtil.prefix + "Sorry, this player has already been invited!");
			return;
		}
		if(invites.containsKey(t.getUniqueId()) ) {
			p.sendMessage(ScreenUtil.prefix + "Sorry, this player has already been invited!");
			return;
		}
		if(invites.containsValue(p.getUniqueId()) ) {
			p.sendMessage(ScreenUtil.prefix + "Sorry, you are already invited!");
			return;
		}
		if(invites.containsKey(p.getUniqueId()) ) {
			p.sendMessage(ScreenUtil.prefix + "Sorry, you already invited someone!");
			return;
		}
		if(IslandManager.getInstance().GetIsland(t) != null) {
			p.sendMessage(ScreenUtil.prefix + "Sorry, this player already has an island!");
			return;
		}
		invites.put(p.getUniqueId(), t.getUniqueId());
		JsonBuilder jb = new JsonBuilder("");
		t.sendMessage(ScreenUtil.prefix + p.getName() + " invited you to his island! You have 20 seconds to accept it!");
		jb.withText(ChatColor.GREEN + "" + ChatColor.BOLD + " Accept ");
		jb.withClickEvent(ClickAction.RUN_COMMAND, "/is accept");
		jb.withHoverEvent(HoverAction.SHOW_TEXT, ChatColor.GREEN + "Accept the invite!");
		jb.withText(ChatColor.DARK_RED + "" + ChatColor.BOLD + "Deny");
		jb.withClickEvent(ClickAction.RUN_COMMAND, "/is deny");
		jb.withHoverEvent(HoverAction.SHOW_TEXT, ChatColor.RED + "Deny the invite!");
		jb.sendJson(t);
		
		p.sendMessage(ScreenUtil.prefix + "You succesfully invited " + t.getName() + "! They have 20 seconds to accept it!");
		
		startTimer(p, t);
		return;
	}
	
	public void startTimer(final Player p, final Player t) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(SkyBlock.p, new Runnable() {
            @Override
            public void run() {
            	if(invites.containsKey(p.getUniqueId()) && invites.containsValue(t.getUniqueId())) {
            		if(p.isOnline() && t.isOnline())
            		invites.remove(p.getUniqueId(), t.getUniqueId());
            		p.sendMessage(ScreenUtil.prefix + "Your invite has been expired!");
            		t.sendMessage(ScreenUtil.prefix + "The invite of the player " + p.getName() + " has been expired!");
            	}
            }
        }, 400L);
	}
	
	@Override
	public String name() {
		return "invite";
	}

	@Override
	public String info() {
		return "Invite someone to your island!";
	}

	@Override
	public String[] aliases() {
		return new String [] {"i"};
	}

	@Override
	public boolean admin() {
		return false;
	}

}
