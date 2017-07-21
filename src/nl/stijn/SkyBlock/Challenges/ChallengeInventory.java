package nl.stijn.SkyBlock.Challenges;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import nl.stijn.SkyBlock.SkyBlock;
import nl.stijn.SkyBlock.Utils.ScreenUtil;

public class ChallengeInventory implements Listener {
	
	public static HashMap<UUID, ChallengeTuple> tuple = new HashMap<UUID, ChallengeTuple>();

	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();
		if(!tuple.containsKey(p.getUniqueId())) {
			return;
		}
		String name = tuple.get(p.getUniqueId()).getName();
		tuple.remove(p.getUniqueId());
		tuple.put(p.getUniqueId(), new ChallengeTuple(name, e.getMessage(), null, null, null, null, null));
		e.setCancelled(true);
		openMenu(p);
		return;

	}

	public void openMenu(Player p) {
		Inventory i = Bukkit.createInventory(null, 9, ChatColor.GREEN + "Create challenge.");

		i.setItem(0, is());
		i.setItem(1, is());
		i.setItem(2, is());
		i.setItem(3, is());
		i.setItem(5, is());
		i.setItem(6, is());
		i.setItem(7, is());
		i.setItem(8, is());

		p.openInventory(i);
		return;

	}

	public ItemStack is() {
		ItemStack is = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 14);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(" ");
		is.setItemMeta(im);
		return is;
	}

	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if(e.getCurrentItem() == null) {
			return;
		}
		if(e.getInventory() == null) {
			return;
		}
		if(e.getInventory().getTitle().startsWith(ChatColor.GREEN + "Challenge: ")) {
			if(e.getCurrentItem().equals(is())) {
				e.setCancelled(true);
			}
		}
		return;

	}

	@EventHandler
	public void onClose(InventoryCloseEvent e) {
		final Player p = (Player) e.getPlayer();
		if(e.getInventory().getTitle() == null) {
			return;
		}

		if(!(e.getPlayer() instanceof Player)) {
			return;
		}
		if(e.getInventory().getTitle().startsWith(ChatColor.GREEN + "Create challenge.")) {
			if(e.getInventory().getItem(4) == null || e.getInventory().getItem(4).getType().equals(Material.AIR)) {
				p.sendMessage(ScreenUtil.prefix + "Please fill in an item!");
				if(tuple.containsKey(p.getUniqueId())) {
					tuple.remove(p.getUniqueId());
				}
				return;
			}
			final String name = tuple.get(p.getUniqueId()).getName();
			String lore = tuple.get(p.getUniqueId()).getLore();
			tuple.remove(p.getUniqueId());
			ItemStack logo = e.getInventory().getItem(4);
			tuple.put(p.getUniqueId(), new ChallengeTuple(name, lore, logo, null, null, null, null));
			Bukkit.getScheduler().scheduleSyncDelayedTask(SkyBlock.p, new Runnable() {
				@Override
				public void run() {
					if(p.isOnline()) {
						p.openInventory(Bukkit.createInventory(null, 36, ChatColor.GREEN + "Add items."));

					}
				}
			}, 1L);
			return;

		}	
		if(e.getInventory().getTitle().startsWith(ChatColor.GREEN + "Add items.")) {
			final String name = tuple.get(p.getUniqueId()).getName();
			String lore = tuple.get(p.getUniqueId()).getLore();
			ItemStack logo = tuple.get(p.getUniqueId()).getLogo();

			ArrayList<ItemStack> items = new ArrayList<ItemStack>();
			for(ItemStack i : e.getInventory().getContents()) {
				items.add(i);
			}

			ItemStack[] i = items.toArray(new ItemStack[items.size()]);
			tuple.remove(p.getUniqueId());
			tuple.put(p.getUniqueId(), new ChallengeTuple(name, lore, logo, i, null, null, null));
			
			Bukkit.getScheduler().scheduleSyncDelayedTask(SkyBlock.p, new Runnable() {
				@Override
				public void run() {
					if(p.isOnline()) {
						openWin(p);

					}
				}
			}, 1L);
			return;

		}
		if(e.getInventory().getTitle().startsWith(ChatColor.GREEN + "Win items.")) {
			final String name = tuple.get(p.getUniqueId()).getName();
			String lore = tuple.get(p.getUniqueId()).getLore();
			ItemStack logo = tuple.get(p.getUniqueId()).getLogo();
			ItemStack item[] = tuple.get(p.getUniqueId()).getItems();

			ArrayList<ItemStack> items = new ArrayList<ItemStack>();
			for(ItemStack i : e.getInventory().getContents()) {
				items.add(i);
			}

			ItemStack[] i = items.toArray(new ItemStack[items.size()]);
			tuple.remove(p.getUniqueId());
			tuple.put(p.getUniqueId(), new ChallengeTuple(name, lore, logo, item, i, null, null));
			
			Bukkit.getScheduler().scheduleSyncDelayedTask(SkyBlock.p, new Runnable() {
				@Override
				public void run() {
					if(p.isOnline()) {
						openDifficulties(p);

					}
				}
			}, 1L);
			return;

		}

		if(e.getInventory().getTitle().startsWith(ChatColor.GREEN + "Categories: ")) {
			return;

		}
	}

	@EventHandler
	public void onClick2(InventoryClickEvent e) {
		final Player p = (Player) e.getWhoClicked();
		
		if(e.getInventory().getTitle().startsWith(ChatColor.GREEN + "Difficulties")) {
			if(e.getCurrentItem() == null) {
				return;
			}
			e.setCancelled(true);
			Difficulty dif = null;
			final String name = tuple.get(p.getUniqueId()).getName();
			String lore = tuple.get(p.getUniqueId()).getLore();
			ItemStack logo = tuple.get(p.getUniqueId()).getLogo();
			ItemStack item[] = tuple.get(p.getUniqueId()).getItems();
			ItemStack win[] = tuple.get(p.getUniqueId()).getWin();
			for(Difficulty d : Difficulty.values()) {
				ItemStack is = d.getItem().clone();
				ItemMeta im = is.getItemMeta();
				
				im.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + d.getName());
				
				is.setItemMeta(im);
				if(e.getCurrentItem().equals(is)) {
					dif = d;
				}
			}
			
			if(dif == null) {
				return;
			}
			tuple.remove(p.getUniqueId());
			tuple.put(p.getUniqueId(), new ChallengeTuple(name, lore, logo, item, win, dif, null));
			
			Bukkit.getScheduler().scheduleSyncDelayedTask(SkyBlock.p, new Runnable() {
				@Override
				public void run() {
					if(p.isOnline()) {
						openCats(p);
					}
				}
			}, 1L);
			return;
		}
		
		if(e.getInventory().getTitle().startsWith(ChatColor.GREEN + "Pick categorie")) {
			if(e.getCurrentItem() == null) {
				return;
			}
			e.setCancelled(true);
			Category ca = null;
			final String name = tuple.get(p.getUniqueId()).getName();
			String lore = tuple.get(p.getUniqueId()).getLore();
			ItemStack logo = tuple.get(p.getUniqueId()).getLogo();
			ItemStack item[] = tuple.get(p.getUniqueId()).getItems();
			ItemStack win[] = tuple.get(p.getUniqueId()).getWin();
			Difficulty dif = tuple.get(p.getUniqueId()).getDifficulty();
			for(Category c : CategoryManager.getInstance().cats) {
				ItemStack is = c.getLogo().clone();
				ItemMeta im = is.getItemMeta();
				im.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + c.getName());
				is.setItemMeta(im);

				if(e.getCurrentItem().equals(is)) {
					ca = c;
				}
			}
			if(ca == null) {
				return;
			}
			tuple.remove(p.getUniqueId());
			String[] loree = new String[] {lore};
			ChallengeManager.getInstance().setupNewChallenge(name, loree, logo, item, win, ca, dif);
			p.sendMessage(ScreenUtil.prefix + "You succesfully made a challenge with the name " + name + "!");
			
			p.closeInventory();
			return;

		}
	}

	public void openWin(Player p) {
		Inventory i = Bukkit.createInventory(null, 36, ChatColor.GREEN + "Win items.");

		p.openInventory(i);
		return;
	}
	
	public void openDifficulties(Player p) {
		Inventory i = Bukkit.createInventory(null, 27, ChatColor.GREEN + "Difficulties");
		
		for(Difficulty d : Difficulty.values()) {
			ItemStack is = d.getItem().clone();
			ItemMeta im = is.getItemMeta();
			
			im.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + d.getName());
			
			is.setItemMeta(im);
			
			i.addItem(is);
		}
		
		p.openInventory(i);
		return;
	}

	public void openCats(Player p) {
		Inventory i = Bukkit.createInventory(null, 27, ChatColor.GREEN + "Pick categorie");

		for(Category c : CategoryManager.getInstance().cats) {
			ItemStack is = c.getLogo().clone();
			ItemMeta im = is.getItemMeta();
			im.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + c.getName());
			is.setItemMeta(im);

			i.setItem(c.getSlot(), is);
		}

		p.openInventory(i);
		return;
	}

}
