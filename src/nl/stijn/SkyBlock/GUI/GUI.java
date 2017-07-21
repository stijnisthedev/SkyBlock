package nl.stijn.SkyBlock.GUI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class GUI {

	private static GUI instance = new GUI();

	public static GUI getInstance() {
		return instance;
	}

	public Inventory getPlayerHeads(Player p, String title, int page, boolean self, List<String> lore) {
		page = Math.max(1, Math.min(page, getMaxPagesAmount()));
		final int finalPage = page;

		int listSize = Bukkit.getOnlinePlayers().size();

		Inventory inv = Bukkit.createInventory(null, 36, title + " " + page);

		int from = 1;
		if(finalPage > 1) {
			from = 27 * (finalPage - 1) + 1;
		}
		int to = 27 * finalPage;

		for (int h = from; h <= to; h++) {
			if(h > listSize) {
				break;
			}

			List<UUID> players = new ArrayList<UUID>();

			for(Player pp : Bukkit.getOnlinePlayers()) {
				players.add(pp.getUniqueId());
			}
			Player p1 = Bukkit.getPlayer(players.get(h - 1));
			if(!p1.isOnline() || p1 == null) {
				continue;
			}
			if(self != true) {
				if(p1.equals(p)) {
					continue;
				}
			}
			ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
			SkullMeta meta = (SkullMeta) skull.getItemMeta();
			meta.setOwner(p1.getName());
			meta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + p1.getName());
			meta.setLore(lore);
			skull.setItemMeta(meta);
			inv.setItem(getNextSlot(inv), skull);

		}

		int ssss = (listSize/27) + 1;
		if(page == 1) {
			inv.setItem(35, next());
		}else if(page == ssss) {
			inv.setItem(27, prev());
		}else if(page != ssss && page != 1) {
			inv.setItem(27, prev());
			inv.setItem(35, next());
		}

		return inv;
	}
	
	public Inventory getPlayerHeadsList(Player p, String title, int page, boolean self, List<String> lore, List<UUID> players) {
		page = Math.max(1, Math.min(page, getMaxPagesAmount()));
		final int finalPage = page;

		int listSize = players.size();

		Inventory inv = Bukkit.createInventory(null, 36, title + " " + page);

		int from = 1;
		if(finalPage > 1) {
			from = 27 * (finalPage - 1) + 1;
		}
		int to = 27 * finalPage;

		for (int h = from; h <= to; h++) {
			if(h > listSize) {
				break;
			}

			Player p1 = Bukkit.getPlayer(players.get(h - 1));
			if(self != true) {
				if(p1.equals(p)) {
					continue;
				}
			}
			ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
			SkullMeta meta = (SkullMeta) skull.getItemMeta();
			meta.setOwner(p1.getName());
			meta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + p1.getName());
			List<String> lore2 = new ArrayList<String>();
			lore2.add(ChatColor.RED + "Click to remove");
			meta.setLore(lore2);
			skull.setItemMeta(meta);
			inv.setItem(getNextSlot(inv), skull);

		}

		int ssss = (listSize/27) + 1;
		if(page == 1) {
			inv.setItem(35, next());
		}else if(page == ssss) {
			inv.setItem(27, prev());
		}else if(page != ssss && page != 1) {
			inv.setItem(27, prev());
			inv.setItem(35, next());
		}

		return inv;
	}

	public int getNextSlot(Inventory inv) {
		for (int i = 0; i < inv.getSize() - 9; i++) {
			if(inv.getItem(i) == null) {
				return i;
			}

		}
		return 0;
	}

	public ItemStack next() {
		ItemStack item = new ItemStack(Material.ARROW);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "Next page");
		item.setItemMeta(meta);
		return item;
	}

	public ItemStack prev() {
		ItemStack item = new ItemStack(Material.ARROW);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "Previous page");
		item.setItemMeta(meta);
		return item;
	}

	public int getMaxPagesAmount() {
		int max = 27;
		int i = Bukkit.getOnlinePlayers().size();
		if (i % max == 0) return i / max;
		double j = i / 27;
		int h = (int) Math.floor(j * 100) / 100;
		return h + 1;
	}

	public int getCurrentPage(Player p, String title) {
		if(p.getOpenInventory() != null && p.getOpenInventory().getTopInventory().getTitle().startsWith(title)) {
			String s = p.getOpenInventory().getTopInventory().getTitle();
			s = ChatColor.stripColor(s);
			String[] sf = s.split(" ");
			List<String> list = new ArrayList<String>(Arrays.asList(sf));
			return Integer.parseInt(list.get(list.size() - 1));
		}
		return 0;
	}

}
