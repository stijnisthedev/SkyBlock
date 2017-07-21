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
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import nl.stijn.SkyBlock.SkyBlock;
import nl.stijn.SkyBlock.Utils.ScreenUtil;

public class CategoryBuilder implements Listener {

	public static HashMap<UUID, CategoryTuple> slot = new HashMap<UUID, CategoryTuple>();

	public static void openMenu(Player p, String name) {
		Inventory i = Bukkit.createInventory(null, 9, ChatColor.GREEN + "Category: " + name);

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
	
	public static void openItems(Player p) {
		Inventory i = Bukkit.createInventory(null, 9, ChatColor.GREEN + "Complete items");

		p.openInventory(i);
		return;
	}

	public static ItemStack is() {
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
		if(e.getInventory().getTitle().startsWith(ChatColor.GREEN + "Category: ")) {
			if(e.getCurrentItem() == null) {
				return;
			}
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
		if(e.getInventory().getTitle().startsWith(ChatColor.GREEN + "Category: ")) {
			if(e.getInventory().getItem(4) == null || e.getInventory().getItem(4).getType().equals(Material.AIR)) {
				p.sendMessage(ScreenUtil.prefix + "Please fill in an item!");
				return;
			}
			
			String name = slot.get(p.getUniqueId()).getName();
			ItemStack logo = e.getInventory().getItem(4);
			int slots = slot.get(p.getUniqueId()).getSlot();
			slot.remove(p.getUniqueId());
			slot.put(p.getUniqueId(), new CategoryTuple(name, logo, null, slots));
			Bukkit.getScheduler().scheduleSyncDelayedTask(SkyBlock.p, new Runnable() {
				@Override
				public void run() {
					if(p.isOnline()) {
						openItems(p);

					}
				}
			}, 1L);
			return;

		}
		if(e.getInventory().getTitle().startsWith(ChatColor.GREEN + "Complete items")) {
			String name = slot.get(p.getUniqueId()).getName();
			ItemStack logo = slot.get(p.getUniqueId()).getLogo();
			int slots = slot.get(p.getUniqueId()).getSlot();
			ArrayList<ItemStack> items = new ArrayList<ItemStack>();
			for(ItemStack i : e.getInventory().getContents()) {
				items.add(i);
			}

			ItemStack[] i = items.toArray(new ItemStack[items.size()]);
			slot.remove(p.getUniqueId());
			
			CategoryManager.getInstance().setupNewCategory(name, logo, i, slots);
			p.sendMessage(ScreenUtil.prefix + "You succesfully made a category with the name " + name + "!");
			return;

		}	
	}

}

