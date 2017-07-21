package nl.stijn.SkyBlock.Island.Schematics;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import nl.stijn.SkyBlock.SkyBlock;
import nl.stijn.SkyBlock.Settings.SettingsManager;

public class SchematicManager {

	private static SchematicManager instance = new SchematicManager();
	private SettingsManager sm = SettingsManager.getInstance();

	public static SchematicManager getInstance() {
		return instance;
	}

	public ArrayList<Schematic> schematics;

	private SchematicManager() {
		this.schematics = new ArrayList<Schematic>();
	}

	public void setup() {
		for(String s : SettingsManager.getInstance().getSchematics().getConfigurationSection("schematics").getKeys(false)) {
			String name = sm.getSchematics().getString("schematics." + s + ".name");
			String file = sm.getSchematics().getString("schematics." + s + ".file");
			String description = sm.getSchematics().getString("schematics." + s + ".description");
			ItemStack logo = sm.getSchematics().getItemStack("schematics." + s + ".logo");
			Schematic schem = new Schematic(name, file, description, logo);
			schematics.add(schem);
		}
	}
	
	public void setupNewSchematic(String name, String file, String description, ItemStack logo) {
		Schematic schem = new Schematic(name, file, description, logo);
		schematics.add(schem);
		
		sm.getSchematics().set("schematics." + name + ".name", name);
		sm.getSchematics().set("schematics." + name + ".file", file);
		sm.getSchematics().set("schematics." + name + ".description", description);
		sm.getSchematics().set("schematics." + name + ".logo", logo);
		
		sm.saveSchematics();
		return;
	}
	
	public Schematic getSchematic(String name) {
		Schematic schem = null;
		for(Schematic sc : this.schematics) {
			if(sc.getName().equals(name)) {
				schem = sc;
				return schem;
			}
		}
		return schem;
	}
	
	public boolean exist(String f) {
		boolean exist = false;
		File[] files = new File(SkyBlock.p.getDataFolder() + "/schematics").listFiles();
		if(files == null) {
			return false;
		}

		for(File file : files) {
			if(file.getName().equals(f + ".schematic")) {
				exist = true;
				return exist;
			}
		}
		return exist;
	}
	
	public ItemStack getLogo(Schematic schem) {
		ItemStack item = schem.getLogo().clone();
		ItemMeta meta = item.getItemMeta();
		
		meta.setDisplayName(ChatColor.YELLOW + "" + ChatColor.BOLD + schem.getName());
		
		meta.setLore(Arrays.asList("", ChatColor.RED + schem.getDescription() ,""));
		
		item.setItemMeta(meta);
		return item;
	}

	public void openMenu(Player p) {
		Inventory menu = Bukkit.createInventory(null, 9, ChatColor.GREEN + "Create island...");
		
		ItemStack item = getLogo(schematics.get(0)).clone();
		ItemMeta meta = item.getItemMeta();
		
		meta.setDisplayName(ChatColor.GOLD + "Start your adventure...");
		
		item.setItemMeta(meta);
		menu.setItem(4, item);
		
		p.openInventory(menu);
		return;
	}

}
