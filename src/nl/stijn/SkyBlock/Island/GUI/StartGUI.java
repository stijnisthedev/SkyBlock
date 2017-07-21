package nl.stijn.SkyBlock.Island.GUI;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import nl.stijn.SkyBlock.GUI.GUI;
import nl.stijn.SkyBlock.Island.Island;
import nl.stijn.SkyBlock.Island.IslandManager;
import nl.stijn.SkyBlock.PlayerData.SkyPlayer;
import nl.stijn.SkyBlock.Utils.ScreenUtil;

public class StartGUI implements Listener {
	
	public static void openMenuOwner(Island i, Player p) {
		Inventory menu = Bukkit.createInventory(null, 18, ChatColor.GREEN + "Your island");
		
		menu.setItem(0, ItemsFac.getHome());
		menu.setItem(1, ItemsFac.getPoints());
		menu.setItem(2, ItemsFac.getSethome());
		menu.setItem(3, ItemsFac.getShop());
		menu.setItem(4, ItemsFac.getInvite());
		menu.setItem(5, ItemsFac.getTrusted(i));
		menu.setItem(16, ItemsFac.getRemove());
		menu.setItem(17, ItemsFac.getSettings());
		
		p.openInventory(menu);
		return;
	}
	
	public static void openMenuTrusted(Island i, Player p) {
		Inventory menu = Bukkit.createInventory(null, 18, ChatColor.GREEN + "Your island");
		
		menu.setItem(0, ItemsFac.getHome());
		menu.setItem(1, ItemsFac.getShop());
		menu.setItem(2, ItemsFac.getTrusted(i));
		menu.setItem(16, ItemsFac.getRemove());
		menu.setItem(17, ItemsFac.getSettings());
		
		p.openInventory(menu);
		return;
	}
	
	public static void openSettings(Player p, Island is) {
		Inventory menu = Bukkit.createInventory(null, 18, ChatColor.GREEN + "Island settings");
		
		menu.setItem(0, ItemsFac.getSetting(is.getPvP(), "PvP", "Toggle the pvp on your island!"));
		
		p.openInventory(menu);
		return;
	}
	
	public static void openKick(Player p, Island is) {
		Inventory gui = GUI.getInstance().getPlayerHeadsList(p, ChatColor.RED + "Trusted players", 1, false, null, is.getOwners());
		p.openInventory(gui);
		return;
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		final Player p = (Player) e.getWhoClicked();
		if(e.getInventory().getTitle().equals(ChatColor.GREEN + "Your island")) {
			e.setCancelled(true);
			if(e.getCurrentItem() == null) {
				return;
			}
			if(e.getCurrentItem().equals(ItemsFac.getHome())) {
				p.performCommand("is home");
				p.closeInventory();
				return;
			}
			if(e.getCurrentItem().equals(ItemsFac.getInvite())) {
				p.closeInventory();
				p.performCommand("is invite");
				return;
			}
			if(e.getCurrentItem().equals(ItemsFac.getSethome())) {
				p.closeInventory();
				p.performCommand("is sethome");
				return;
			}
			if(e.getCurrentItem().equals(ItemsFac.getPoints())) {
				p.closeInventory();
				p.performCommand("is points");
				return;
			}
			if(e.getCurrentItem().equals(ItemsFac.getTop())) {
				p.closeInventory();
				p.performCommand("is top");
				return;
			}
			if(e.getCurrentItem().equals(ItemsFac.getShop())) {
				p.closeInventory();
				p.performCommand("is shop");
				return;
			}
			if(e.getCurrentItem().equals(ItemsFac.getTrusted(IslandManager.getInstance().getIsland(p)))) {
				openKick(p, IslandManager.getInstance().getIsland(p));
				return;
			}
			if(e.getCurrentItem().equals(ItemsFac.getRemove())) {
				p.closeInventory();
				p.performCommand("is remove");
				return;
			}
			if(e.getCurrentItem().equals(ItemsFac.getSettings())) {
				openSettings(p, IslandManager.getInstance().GetIsland(p));
				return;
			}
		}
		if(e.getInventory().getTitle().equals(ChatColor.GREEN + "Island settings")) {
			final Island is = IslandManager.getInstance().GetIsland(p);
			e.setCancelled(true);
			if(e.getCurrentItem() == null) {
				return;
			}
			if(e.getCurrentItem().equals(ItemsFac.getSetting(is.getPvP(), "PvP", "Toggle the pvp on your island!"))) {
				p.closeInventory();
				is.getConfig().set("pvp", !is.getPvP());
				is.saveConfig();
				IslandManager.getInstance().updateIsland(is);
				p.sendMessage(ScreenUtil.prefix + "PvP toggled!");
				for(Player p1 : Bukkit.getOnlinePlayers()) {
					if(is.getBounds().contains(p1.getLocation())) {
						SkyPlayer s = new SkyPlayer(p1.getUniqueId());
						if(!is.getPvP()) {
							s.displayTitle(10, 50, 10, ChatColor.DARK_AQUA + "PvP", ChatColor.AQUA + "PvP is now " + ChatColor.RED + "enabled" + ChatColor.AQUA + "!");
						} else {
							s.displayTitle(10, 50, 10, ChatColor.DARK_AQUA + "PvP", ChatColor.AQUA + "PvP is now " + ChatColor.GREEN + "disabled" + ChatColor.AQUA + "!");
						}

					}
				}
				return;
			}
		}

		
	}

}
