package nl.stijn.SkyBlock.Island.GUI;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import nl.stijn.SkyBlock.GUI.GUI;
import nl.stijn.SkyBlock.Island.Island;
import nl.stijn.SkyBlock.Island.IslandManager;

public class Invite implements Listener {
	
	@EventHandler
	public void InviteE(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		if(e.getInventory().getTitle().startsWith(ChatColor.RED + "Click player to invite")) {
			if(e.getCurrentItem() == null) {
				return;
			}
			e.setCancelled(true);
			if(e.getCurrentItem().equals(GUI.getInstance().next())) {
				p.openInventory(GUI.getInstance().getPlayerHeads(p, ChatColor.RED + "Click player to invite", (GUI.getInstance().getCurrentPage(p, ChatColor.RED + "Click player to invite") + 1), false, null));
				return;
			}
			if(e.getCurrentItem().equals(GUI.getInstance().prev())) {
				p.openInventory(GUI.getInstance().getPlayerHeads(p, ChatColor.RED + "Click player to invite", (GUI.getInstance().getCurrentPage(p, ChatColor.RED + "Click player to invite") - 1), false, null));
				return;
			}
			if(e.getCurrentItem().getType().equals(Material.SKULL_ITEM)) {
				if(e.getCurrentItem().getItemMeta().hasDisplayName()) {
					ItemStack i = e.getCurrentItem();
					String s = i.getItemMeta().getDisplayName();
					s = ChatColor.stripColor(s);
					
					Player t = Bukkit.getPlayer(s);
					if(t == null || !t.isOnline()) {
						p.closeInventory();
						return;
					}
					p.closeInventory();
					p.performCommand("is invite " + t.getName());
					return;
				}
			}
			
		}
	}
	
	@EventHandler
	public void InviteF(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		if(e.getInventory().getTitle().startsWith(ChatColor.RED + "Trusted players")) {
			if(e.getCurrentItem() == null) {
				return;
			}
			e.setCancelled(true);
			Island is = IslandManager.getInstance().getIsland(p);
			if(e.getCurrentItem().equals(GUI.getInstance().next())) {
				p.openInventory(GUI.getInstance().getPlayerHeadsList(p, ChatColor.RED + "Trusted players", (GUI.getInstance().getCurrentPage(p, ChatColor.RED + "Trusted players") + 1), false, null, is.getOwners()));
				return;
			}
			if(e.getCurrentItem().equals(GUI.getInstance().prev())) {
				p.openInventory(GUI.getInstance().getPlayerHeadsList(p, ChatColor.RED + "Trusted players", (GUI.getInstance().getCurrentPage(p, ChatColor.RED + "Trusted players") - 1), false, null, is.getOwners()));
				return;
			}
			if(e.getCurrentItem().getType().equals(Material.SKULL_ITEM)) {
				if(e.getCurrentItem().getItemMeta().hasDisplayName()) {
					ItemStack i = e.getCurrentItem();
					String s = i.getItemMeta().getDisplayName();
					s = ChatColor.stripColor(s);
					
					Player t = Bukkit.getPlayer(s);
					if(t == null || !t.isOnline()) {
						p.closeInventory();
						return;
					}
					p.closeInventory();
					p.performCommand("is kick " + t.getName());
					return;
				}
			}
			
		}
	}

}
