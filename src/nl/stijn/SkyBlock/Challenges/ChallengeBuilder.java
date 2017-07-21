package nl.stijn.SkyBlock.Challenges;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import nl.stijn.SkyBlock.Island.Island;
import nl.stijn.SkyBlock.Island.IslandManager;
import nl.stijn.SkyBlock.MySQL.MySQL;
import nl.stijn.SkyBlock.Settings.SettingsManager;
import nl.stijn.SkyBlock.Utils.FireworkUtil;
import nl.stijn.SkyBlock.Utils.ItemUtil;
import nl.stijn.SkyBlock.Utils.ScreenUtil;

public class ChallengeBuilder implements Listener {

	public static SettingsManager sm = SettingsManager.getInstance();

	public static void openCats(Player p) {
		int size = 27;
		if(IslandManager.getInstance().GetIsland(p) == null) {
			size = 27;
		} else {
			size = 36;
		}
		Inventory i = Bukkit.createInventory(null, size, ChatColor.GREEN + "Categories");

		for(Category c : CategoryManager.getInstance().cats) {
			if(c.getLogo() == null) {
				continue;
			}
			ItemStack is = c.getLogo().clone();
			ItemMeta im = is.getItemMeta();
			im.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + c.getName());

			ArrayList<String> lore = new ArrayList<String>();

			lore.add(getProcent(c, IslandManager.getInstance().GetIsland(p)) + "% complete");

			Island iss = IslandManager.getInstance().GetIsland(p);
			if(getProcent(c, iss) >= 100) {
				if(!iss.getDone().contains(c)) {
					lore.add("Right click to get reward!");
				}
			}

			im.setLore(lore);
			is.setItemMeta(im);

			i.setItem(c.getSlot(), is);
		}
		if(IslandManager.getInstance().GetIsland(p) != null) {
			i.setItem(31, yourisland(p));
			i.setItem(27, glass());
			i.setItem(28, glass());
			i.setItem(29, glass());
			i.setItem(30, glass());
			i.setItem(32, glass());
			i.setItem(33, glass());
			i.setItem(34, glass());
			i.setItem(35, glass());
		}

		p.openInventory(i);
		return;
	}

	@EventHandler
	public void onClick2(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		if(e.getInventory().getTitle().equals(ChatColor.GREEN + "Categories")) {
			if(e.getCurrentItem() == null) {
				return;
			}
			if(e.getClick().equals(ClickType.RIGHT)) {
				if(e.getCurrentItem().equals(yourisland(p))) {
					return;
				}
				Category ca = null;
				for(Category c : CategoryManager.getInstance().cats) {
					ItemStack is = c.getLogo().clone();
					ItemMeta im = is.getItemMeta();
					im.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + c.getName());
					ArrayList<String> lore = new ArrayList<String>();

					lore.add(getProcent(c, IslandManager.getInstance().GetIsland(p)) + "% complete");

					Island iss = IslandManager.getInstance().GetIsland(p);
					if(getProcent(c, iss) >= 100) {
						if(!iss.getDone().contains(c)) {
							lore.add("Right click to get reward!");
						}
					}

					im.setLore(lore);
					is.setItemMeta(im);

					if(e.getCurrentItem().equals(is)) {
						ca = c;
					}
				}
				if(ca == null) {
					return;
				}

				Island iss = IslandManager.getInstance().GetIsland(p);
				if(getProcent(ca, iss) < 100) {
					if(iss.getDone().contains(ca)) {
						p.closeInventory();
						p.sendMessage(ScreenUtil.prefix + "You already got a reward from this category!");
						return;
					} else {
						p.closeInventory();
						p.sendMessage(ScreenUtil.prefix + "Please get 100% before you can get a reward!");
						return;
					}
				}

				if(ca.getItems() == null) {
					return;
				}
				for(ItemStack i : ca.getItems()) {
					p.getInventory().addItem(i);
				}
				p.closeInventory();
				iss.addDone(ca);
				IslandManager.getInstance().updateIsland(iss);
				p.sendMessage(ScreenUtil.prefix + "Received reward of the category " + ca.getName() + "!");
				return;
			}
		}

	}

	public static int getProcent(Category ca, Island is) {
		double procent = 0;
		double items = 0;
		double max = 0;
		for(Challenge ch : ChallengeManager.getInstance().challenges) {
			if(ch == null) {
				continue;
			}
			if(ch.getCategory() == null) {
				continue;
			}
			if(ca == null) {
				continue;
			}
			if(ch.getCategory().getName().equals(ca.getName())) {
				max++;
				if(ch.getOwned().contains(is.getUUID())) {
					items++;
				}
			}
		}
		if(max == 0 || items == 0) {
			return 0;
		}
		procent = items/max;
		procent = procent * 100;
		return (int) Math.round(procent);

	}

	public static void openCat(Player p, Category cat, int page) {
		page = Math.max(1, Math.min(page, getMaxPagesAmount(cat)));
		final int finalPage = page;

		ArrayList<Challenge> ch = new ArrayList<Challenge>();

		for(Challenge c : ChallengeManager.getInstance().challenges) {
			if(c.getCategory().equals(cat)) {
				ch.add(c);
			}
		}

		int listSize = ch.size();

		Inventory inv = Bukkit.createInventory(null, 36, ChatColor.RED + "" + ChatColor.BOLD + cat.getName() + " " + page);

		int from = 1;
		if(finalPage > 1) {
			from = 27 * (finalPage - 1) + 1;
		}
		int to = 27 * finalPage;

		for (int h = from; h <= to; h++) {
			if(h > listSize) {
				break;
			}
			Challenge c = ch.get(h - 1);
			if(!c.getOwned().contains(IslandManager.getInstance().GetIsland(p).getUUID())) {
				inv.setItem(getNextSlot(inv), getItem(c, p));
			}

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

		inv.setItem(31, back());
		p.openInventory(inv);
		return;
	}

	public static ItemStack next() {
		ItemStack item = new ItemStack(Material.ARROW);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "Next page");
		item.setItemMeta(meta);
		return item;
	}

	public static ItemStack prev() {
		ItemStack item = new ItemStack(Material.ARROW);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "Previous page");
		item.setItemMeta(meta);
		return item;
	}

	public static ItemStack back() {
		ItemStack item = new ItemStack(Material.BARRIER);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "Back");
		item.setItemMeta(meta);
		return item;
	}

	public static ItemStack yourisland(Player p) {
		ItemStack item = new ItemStack(Material.CHEST);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "Your island challenges");
		meta.setLore(Arrays.asList(ChatColor.GREEN + "" + getChallenges(IslandManager.getInstance().GetIsland(p)).size() + " challenges!"));
		item.setItemMeta(meta);
		return item;
	}

	public static ItemStack glass() {
		ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 14);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(" ");
		item.setItemMeta(meta);
		return item;
	}

	public static int getNextSlot(Inventory inv) {
		for (int i = 0; i < inv.getSize() - 9; i++) {
			if(inv.getItem(i) == null) {
				return i;
			}

		}
		return 0;
	}

	private static int getMaxPagesAmount(Category c) {
		ArrayList<Challenge> ch = new ArrayList<Challenge>();

		for(Challenge c1 : ChallengeManager.getInstance().challenges) {
			if(c1.getCategory().equals(c)) {
				ch.add(c1);
			}
		}

		int max = 27;
		int i = ch.size();
		if (i % max == 0) return i / max;
		double j = i / 27;
		int h = (int) Math.floor(j * 100) / 100;
		return h + 1;
	}

	public static ArrayList<Challenge> getChallenges(Island i) {
		ArrayList<Challenge> ch = new ArrayList<Challenge>();
		for(Challenge cha : ChallengeManager.getInstance().challenges) {
			if(cha.getOwned().contains(i.getUUID())) {
				ch.add(cha);
			}
		}
		return ch;
	}

	private static int getMaxPagesAmount2(Island is) {
		int max = 27;
		int i = getChallenges(is).size();
		if (i % max == 0) return i / max;
		double j = i / 27;
		int h = (int) Math.floor(j * 100) / 100;
		return h + 1;
	}
	
	public static MySQL MySQL = new MySQL("217.123.153.88", "3306", "drox", "root", "chocolade123");
	public static Connection co = null;

	public static int getCurrentPage(Player p, Category cat) {
		if(p.getOpenInventory() != null && p.getOpenInventory().getTopInventory().getTitle().startsWith(ChatColor.RED + "" + ChatColor.BOLD + cat.getName())) {
			String s = p.getOpenInventory().getTopInventory().getTitle().split(" ")[1];
			s = ChatColor.stripColor(s);
			return Integer.parseInt(s);
		}
		return 0;
	}

	public static int getCurrentPage2(Player p) {
		if(p.getOpenInventory() != null && p.getOpenInventory().getTopInventory().getTitle().startsWith(ChatColor.RED + "" + ChatColor.BOLD + "Your challenges | ")) {
			String s = p.getOpenInventory().getTopInventory().getTitle().split(" ")[1];
			s = ChatColor.stripColor(s);
			return Integer.parseInt(s);
		}
		return 0;
	}

	public static boolean isChallenge(ItemStack item, Player p) {
		for(Challenge c : ChallengeManager.getInstance().challenges) {
			if(getItem(c, p).equals(item)) {
				return true;
			}
		}
		return false;
	}

	public static Challenge getChallenge(ItemStack item, Player p) {
		for(Challenge c : ChallengeManager.getInstance().challenges) {
			if(getItem(c, p).equals(item)) {
				return c;
			}
		}
		return null;
	}

	public static ItemStack getItem(Challenge c, Player p) {
		ItemStack is = c.getLogo().clone();
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + c.getName());

		if(is.getAmount() > 64) {
			is.setAmount(64);
		} else {
			is.setAmount(is.getAmount());
		}

		ArrayList<String> lore = new ArrayList<String>();

		for(String s : c.getLore()) {
			lore.add(ChatColor.AQUA + s);
		}

		lore.add(ChatColor.GOLD + "Difficulty: " + c.getDifficulty().getColor() + c.getDifficulty().getName());

		im.setLore(lore);
		is.setItemMeta(im);
		return is;
	}

	public static int getProcent(Challenge ch, Player p) {
		int g = 0;

		HashMap<Integer, ItemStack> slots = new HashMap<Integer, ItemStack>();

		for(ItemStack im : ch.getItems()) {
			Material m = im.getType();
			for(int i = 0; i < p.getInventory().getSize(); i++) {
				ItemStack imt = p.getInventory().getItem(i);
				if(imt == null) {
					continue;
				}
				if(imt.getType().equals(m) && imt.getData().equals(im.getData())) {
					g = g + imt.getAmount();
					slots.put(i, imt);
					p.getInventory().setItem(i, null);
				}
			}

		}

		Iterator<Integer> it = slots.keySet().iterator();
		while(it.hasNext()) {
			int i = it.next();
			p.getInventory().setItem(i, slots.get(i));
		}
		return g;
	}
	
	public static void setup() {
		try {
			co = MySQL.openConnection();
		} catch(Exception e) {
			
		}
	}

	public static int getMaxProcent(Challenge ch) {
		int g = 0;
		for(ItemStack is : ch.getItems()) {
			g = g + is.getAmount();
		}
		return g;
	}

	public void openChallenges(Player p, Island is, int page) {
		page = Math.max(1, Math.min(page, getMaxPagesAmount2(is)));
		final int finalPage = page;

		int listSize = getChallenges(is).size();

		Inventory inv = Bukkit.createInventory(null, 36, ChatColor.RED + "" + ChatColor.BOLD + "Your challenges | " + page);

		int from = 1;
		if(finalPage > 1) {
			from = 27 * (finalPage - 1) + 1;
		}
		int to = 27 * finalPage;

		for (int h = from; h <= to; h++) {
			if(h > listSize) {
				break;
			}
			Challenge c = getChallenges(is).get(h - 1);
			ItemStack iss = c.getLogo().clone();
			ItemMeta im = iss.getItemMeta();
			im.setDisplayName(ChatColor.GREEN + "" + ChatColor.RED + c.getName());
			im.setLore(getItem(c, p).getItemMeta().getLore());
			iss.setItemMeta(im);
			inv.setItem(getNextSlot(inv), ItemUtil.addGlow(iss));

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

		inv.setItem(31, back());


		p.openInventory(inv);
		return;

	}

	@EventHandler
	public void onClick(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		if(e.getClickedInventory() == null || e.getInventory() == null) {
			return;
		}
		if(e.getInventory().getTitle().equals(ChatColor.GREEN + "Categories")) {
			e.setCancelled(true);
			if(e.getCurrentItem() == null) {
				return;
			}
			if(e.getClick().equals(ClickType.LEFT)) {
				if(e.getCurrentItem().equals(yourisland(p))) {
					openChallenges(p, IslandManager.getInstance().GetIsland(p), 1);
					return;
				}
			}
			if(e.getClick().equals(ClickType.LEFT)) {
				Category ca = null;
				for(Category c : CategoryManager.getInstance().cats) {
					if(c.getLogo() == null) {
						return;
					}
					ItemStack is = c.getLogo().clone();
					ItemMeta im = is.getItemMeta();
					im.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + c.getName());
					ArrayList<String> lore = new ArrayList<String>();

					lore.add(getProcent(c, IslandManager.getInstance().GetIsland(p)) + "% complete");

					Island iss = IslandManager.getInstance().GetIsland(p);
					if(getProcent(c, iss) >= 100) {
						if(!iss.getDone().contains(c)) {
							lore.add("Right click to get reward!");
						}
					}

					im.setLore(lore);
					is.setItemMeta(im);

					if(e.getCurrentItem().equals(is)) {
						ca = c;
					}
				}
				if(ca == null) {
					return;
				}
				openCat(p, ca, getCurrentPage(p, ca));
				return;
			}
		}

		String ssssss = null;
		boolean d = false;
		String s = e.getInventory().getTitle().split(" ")[0];
		s = ChatColor.stripColor(s);
		for(Category c : CategoryManager.getInstance().cats) {
			if(c.getName().equals(s)) {
				d = true;
				ssssss = s;
			}
		}
		if(e.getInventory().getTitle().startsWith(ChatColor.RED + "" + ChatColor.BOLD + "Your challenges | ")) {
			e.setCancelled(true);
			if(e.getCurrentItem().equals(next())) {
				openChallenges(p, IslandManager.getInstance().GetIsland(p), getCurrentPage2(p) + 1);
				return;
			}
			if(e.getCurrentItem().equals(prev())) {
				openChallenges(p, IslandManager.getInstance().GetIsland(p), getCurrentPage2(p) - 1);
				return;
			}
			if(e.getCurrentItem().equals(back())) {
				openCats(p);
				return;
			}

		}
		if(e.getInventory().getTitle().startsWith(ChatColor.RED + "" + ChatColor.BOLD + ssssss)) {
			e.setCancelled(true);
			if(d == true) {
				if(e.getCurrentItem().equals(next())) {
					Category cg = CategoryManager.getInstance().getCategory(s); 
					openCat(p, cg, getCurrentPage(p, cg) + 1);
					return;
				}
				if(e.getCurrentItem().equals(prev())) {
					Category cg = CategoryManager.getInstance().getCategory(s);
					openCat(p, cg, getCurrentPage(p, cg) - 1);
					return;
				}
				if(e.getCurrentItem().equals(back())) {
					openCats(p);
					return;
				} else {
					if(e.getInventory().equals(p.getOpenInventory().getTopInventory())) {
						if(isChallenge(e.getCurrentItem(), p)) {
							Challenge c = getChallenge(e.getCurrentItem(), p);
							boolean noitems = false;

							HashMap<Integer, ItemStack > slots = new HashMap<Integer, ItemStack>();

							for(ItemStack im : c.getItems()) {
								boolean has = false;
								Inventory in = p.getInventory();
								if(im != null || in != null) {
									if(hasAmount(im.getType(), im.getData(), in, im.getAmount())) {
										has = true;
										slots.put(getSlot(im, p.getInventory()), im);
										p.getInventory().setItem(getSlot(im, p.getInventory()), null);
									}
								}
								if(has == false) {
									noitems = true;
									break;
								}
							}
							Iterator<Integer> it = slots.keySet().iterator();
							while(it.hasNext()) {
								int i = it.next();
								p.getInventory().setItem(i, slots.get(i));

							}

							if(noitems == true) {
								p.closeInventory();
								p.sendMessage(ScreenUtil.prefix + "You don't have enough items!");
								return;
							}
							if(IslandManager.getInstance().GetIsland(p) == null) {
								p.sendMessage(ScreenUtil.prefix + "You don't have an island!");
								return;
							}
							p.closeInventory();

							ArrayList<ItemStack> remaining = new ArrayList<ItemStack>(Arrays.asList(c.getItems().clone()));
							for(ItemStack item : c.getItems()) {
								if(remaining.size() == 0) {
									break;
								}
								removeItems(p.getInventory(), item.getType(), item.getAmount());
								p.updateInventory();
								remaining.remove(item);
							}

							for(ItemStack im : c.getWin()) {	
								if(!check(p)) {
									p.getWorld().dropItem(p.getLocation(), im);
									continue;
								}
								p.getInventory().addItem(im);
								continue;
							}
							p.sendMessage(ScreenUtil.prefix + "You succesfully completed the challenge " + c.getName() + "!");
							FireworkUtil.instantFirework(p.getLocation(), FireworkEffect.builder().withColor(Color.AQUA).withColor(Color.RED).withColor(Color.ORANGE).flicker(true).trail(true).with(Type.BALL_LARGE).build());
							c.addOwned(IslandManager.getInstance().GetIsland(p));
							return;
						}
					}

				}
			}
		}

	}

	public static boolean hasAmount(Material mat, MaterialData date, Inventory inv, int amt){

		int invamt = 0;

		for (ItemStack i : inv) {
			if(i != null)
				if(i.getType().equals(mat) && i.getData().equals(date)){
					invamt = invamt + i.getAmount();
				}
		}

		if(invamt >= amt){
			return true;
		}else{
			return false;
		}
	}

	public static void removeItems(Inventory inventory, Material type, int amount) {
		if (amount <= 0) return;
		int size = inventory.getSize();
		for (int slot = 0; slot < size; slot++) {
			ItemStack is = inventory.getItem(slot);
			if (is == null) continue;
			if (type == is.getType()) {
				int newAmount = is.getAmount() - amount;
				if (newAmount > 0) {
					is.setAmount(newAmount);
					break;
				} else {
					inventory.clear(slot);
					amount = -newAmount;
					if (amount == 0) break;
				}
			}
		}
	}

	public static boolean hasMaterial(Material mat, short data, Inventory inv){

		for (ItemStack i : inv) {
			if(i != null)
				if(i.getType().equals(mat) && i.getData().equals(data)){
					return true;
				}
		}
		return false;
	}

	public int getAmount(Material mat, Inventory inv) {
		int invamt = 0;

		for (ItemStack i : inv) {
			if(i != null)
				if(i.getType().equals(mat)){
					invamt = invamt + i.getAmount();
				}
		}

		return invamt;

	}

	public static int getSlot(ItemStack item, Inventory inv) {
		if(item == null || inv == null) {
			return 0;
		}
		int s = 0;
		for(int i = 0; i < inv.getSize(); i++){
			if(inv.getItem(i) != null)
				if(inv.getItem(i).equals(item)) {
					s = i;
					return s;
				}
		}
		return s;
	}

	public static int getSlot(Material item, Inventory inv) {
		if(item == null || inv == null) {
			return 0;
		}
		int s = 0;
		for(int i = 0; i < inv.getSize(); i++){
			if(inv.getItem(i) != null)
				if(inv.getItem(i).getType().equals(item)) {
					s = i;
					return s;
				}
		}
		return s;
	}

	public static boolean check(Player p) { 
		boolean isEmpty = true;
		for (ItemStack item : p.getInventory().getContents()) {
			if(item != null) {
				isEmpty = false;
				break;
			}
		}
		return isEmpty;

	}

}


