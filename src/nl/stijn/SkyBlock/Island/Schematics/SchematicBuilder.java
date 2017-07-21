package nl.stijn.SkyBlock.Island.Schematics;

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

import nl.stijn.SkyBlock.Island.IslandManager;
import nl.stijn.SkyBlock.Island.Commands.AddSchem;
import nl.stijn.SkyBlock.Utils.ScreenUtil;

public class SchematicBuilder implements Listener {

	public static HashMap<UUID, String> name = new HashMap<UUID, String>();
	public static HashMap<UUID, String> file = new HashMap<UUID, String>();

	public static HashMap<UUID, SchematicTuple> tuple = new HashMap<UUID, SchematicTuple>();

	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();
		if(AddSchem.chat.contains(p.getUniqueId())) {
			e.setCancelled(true);
			SchematicTuple t = new SchematicTuple(name.get(p.getUniqueId()), file.get(p.getUniqueId()), e.getMessage(), null);
			tuple.put(p.getUniqueId(), t);
			openMenu(p);
			return;
		}
	}

	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if(e.getCurrentItem() == null) {
			return;
		}
		if(e.getCurrentItem().equals(is())) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onClose(InventoryCloseEvent e) {
		Player p = (Player) e.getPlayer();
		if(e.getInventory().getTitle().equals(ChatColor.GREEN + "Put in your logo.")) {
			if(e.getInventory().getItem(4) == null) {
				tuple.remove(p.getUniqueId());
				name.remove(p.getUniqueId());
				file.remove(p.getUniqueId());
				AddSchem.chat.remove(p.getUniqueId());
				p.sendMessage(ScreenUtil.prefix + "Please put in a logo.");
				return;
			}
			String description = tuple.get(p.getUniqueId()).getDescription();
			SchematicTuple t = new SchematicTuple(name.get(p.getUniqueId()), file.get(p.getUniqueId()), description, e.getInventory().getItem(4));
			SchematicManager.getInstance().setupNewSchematic(t.getName(), t.getFile(), t.getDescription(), t.getLogo());
			p.sendMessage(ScreenUtil.prefix + "You succesfully created the schematic " + t.getName() + "!");
			tuple.remove(p.getUniqueId());
			name.remove(p.getUniqueId());
			file.remove(p.getUniqueId());
			AddSchem.chat.remove(p.getUniqueId());
			return;
		}

	}

	@EventHandler
	public void onClick2(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		if(e.getInventory().getTitle().equals(ChatColor.GREEN + "Create island...")) {
			if(e.getCurrentItem() == null) {
				return;
			}
			ItemStack item = SchematicManager.getInstance().getLogo(SchematicManager.getInstance().schematics.get(0)).clone();
			ItemMeta meta = item.getItemMeta();

			meta.setDisplayName(ChatColor.GOLD + "Start your adventure...");

			item.setItemMeta(meta);
			
			if(e.getCurrentItem().equals(item)) {
				p.closeInventory();
				try {
					p.sendMessage(ScreenUtil.prefix + "Creating island....");
					IslandManager.getInstance().setupIslandNew(p.getUniqueId(), SchematicManager.getInstance().schematics.get(0), p);
					return;
				} catch (Exception ee) {
					p.sendMessage(ScreenUtil.prefix + "An error occurred!");
					ee.printStackTrace();
					return;

				}
			}
		}
	}

	public static void openMenu(Player p) {
		Inventory menu = Bukkit.createInventory(null, 9, ChatColor.GREEN + "Put in your logo.");

		menu.setItem(0, is());
		menu.setItem(1, is());
		menu.setItem(2, is());
		menu.setItem(3, is());
		menu.setItem(5, is());
		menu.setItem(6, is());
		menu.setItem(7, is());
		menu.setItem(8, is());

		p.openInventory(menu);
		return;
	}

	public static ItemStack is() {
		ItemStack is = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 14);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(" ");
		is.setItemMeta(im);
		return is;
	}

	public static boolean isSchematic(ItemStack i) {
		for(Schematic sc : SchematicManager.getInstance().schematics) {
			if(SchematicManager.getInstance().getLogo(sc).equals(i)) {
				return true;
			}
		}
		return false;
	}

	public static Schematic getSchematic(ItemStack i) {
		for(Schematic sc : SchematicManager.getInstance().schematics) {
			if(SchematicManager.getInstance().getLogo(sc).equals(i)) {
				return sc;
			}
		}
		return null;
	}

}
