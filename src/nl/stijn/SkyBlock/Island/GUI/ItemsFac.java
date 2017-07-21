package nl.stijn.SkyBlock.Island.GUI;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import nl.stijn.SkyBlock.Island.Island;

public class ItemsFac {

	public static ItemStack getHome() {
		ItemStack i = new ItemStack(Material.WOOD_DOOR);
		ItemMeta m = i.getItemMeta();
		
		m.setDisplayName(ChatColor.RED + "Go to your island!");
		
		i.setItemMeta(m);
		return i;
	}
	
	public static ItemStack getTrusted(Island is) {
		ItemStack i = new ItemStack(Material.SKULL_ITEM);
		ItemMeta m = i.getItemMeta();
		
		m.setDisplayName(ChatColor.RED + "List of trusted players!");
		
		ArrayList<String> lore = new ArrayList<String>();
		for(UUID uuids : is.getOwners()) {
			if(!uuids.equals(is.getOwner())) {
				OfflinePlayer pl = Bukkit.getOfflinePlayer(uuids);
				lore.add(ChatColor.GOLD + "- " + pl.getName());
			}
		}
		
		m.setLore(lore);
		i.setItemMeta(m);
		return i;
	}
	
	public static ItemStack getInvite() {
		ItemStack i = new ItemStack(Material.PAPER);
		ItemMeta m = i.getItemMeta();
		
		m.setDisplayName(ChatColor.RED + "Invite someone!");
		
		i.setItemMeta(m);
		return i;
	}
	
	public static ItemStack getSettings() {
		ItemStack i = new ItemStack(Material.ANVIL);
		ItemMeta m = i.getItemMeta();
		
		m.setDisplayName(ChatColor.RED + "Settings");
		
		i.setItemMeta(m);
		return i;
	}
	
	public static ItemStack getShop() {
		ItemStack i = new ItemStack(Material.DIAMOND);
		ItemMeta m = i.getItemMeta();
		
		m.setDisplayName(ChatColor.RED + "Shop");
		
		i.setItemMeta(m);
		return i;
	}
	
	public static ItemStack getRemove() {
		ItemStack i = new ItemStack(Material.BARRIER);
		ItemMeta m = i.getItemMeta();
		
		m.setDisplayName(ChatColor.RED + "Remove your island");
		
		i.setItemMeta(m);
		return i;
	}
	
	public static ItemStack getPoints() {
		ItemStack i = new ItemStack(Material.EXP_BOTTLE);
		ItemMeta m = i.getItemMeta();
		
		m.setDisplayName(ChatColor.RED + "Calculate your points");
		
		i.setItemMeta(m);
		return i;
	}
	
	public static ItemStack getTop() {
		ItemStack i = new ItemStack(Material.EMERALD);
		ItemMeta m = i.getItemMeta();
		
		m.setDisplayName(ChatColor.RED + "Top 10 islands");
		
		i.setItemMeta(m);
		return i;
	}
	
	public static ItemStack getSethome() {
		ItemStack i = new ItemStack(Material.ARMOR_STAND);
		ItemMeta m = i.getItemMeta();
		
		m.setDisplayName(ChatColor.RED + "Set your home");
		
		i.setItemMeta(m);
		return i;
	}
	
	public static ItemStack getSetting(boolean toggled, String name, String description) {
		ItemStack item = null;
		if(toggled) {
			item = new ItemStack(Material.EMERALD_BLOCK);
		} else {
		    item = new ItemStack(Material.REDSTONE_BLOCK);
		}
		ItemMeta meta = item.getItemMeta();
		
		meta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + name);
		
		List<String> lore = new ArrayList<String>();
		lore.add("");
		lore.add(ChatColor.RED + description);
		if(toggled) {
			lore.add(ChatColor.RED + "On");
		} else {
			lore.add(ChatColor.GREEN + "Off");
		}
		lore.add("");
		
		meta.setLore(lore);
		
		item.setItemMeta(meta);
		return item;
	}
	
}
