package nl.stijn.SkyBlock.Island.Commands;

import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import nl.stijn.SkyBlock.Island.Island;
import nl.stijn.SkyBlock.Island.IslandManager;
import nl.stijn.SkyBlock.Utils.ScreenUtil;

public class Accept extends SubCommand {

	@Override
	public void onCommand(Player p, String[] args) {
		if(!Invite.invites.containsValue(p.getUniqueId())) {
			p.sendMessage(ScreenUtil.prefix + "You don't have an invite!");
			return;
		}
		Player t = null;
        for (Entry<UUID, UUID> entry : Invite.invites.entrySet()) {
            if (entry.getValue().equals(p.getUniqueId())) {
            	t = Bukkit.getPlayer(entry.getKey());
            }
        }
        if(t == null) {
        	return;
        }
        
        Island i = IslandManager.getInstance().getIsland(t);
        i.addOwner(p.getUniqueId());
        p.sendMessage(ScreenUtil.prefix + "You succesfully accepted the invite!");
        t.sendMessage(ScreenUtil.prefix + p.getName() + " accepted your invite!");
        Invite.invites.remove(t.getUniqueId());
		return;
		
	}

	@Override
	public String name() {
		return "accept";
	}

	@Override
	public String info() {
		return "Accept an invite!";
	}

	@Override
	public String[] aliases() {
		return new String [] {"ac"};
	}

	@Override
	public boolean admin() {
		return false;
	}
}
