package nl.stijn.SkyBlock.Challenges;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import nl.stijn.SkyBlock.Island.Island;

public class Challenge {
	
	private int ID;
	private String name;
	private String[] lore;
	private ItemStack logo;
	private ItemStack[] items;
	private ItemStack[] win;
	private File file;
	private FileConfiguration config;
	private ArrayList<UUID> owned;
	private Category cat;
	private Difficulty difficulty;
	
	public Challenge(int ID, String name, String lore[], ItemStack logo, ItemStack[] items, ItemStack[] win, File file, FileConfiguration config, Category cat, Difficulty difficulty) {
		this.ID = ID;
		this.name = name;
		this.lore = lore;
		this.logo = logo;
		this.items = items;
		this.file = file;
		this.config = config;
		this.win = win;
		this.cat = cat;
		this.owned = new ArrayList<UUID>();
		this.difficulty = difficulty;
		
		for(String s : config.getStringList("owned")) {
			this.owned.add(UUID.fromString(s));
		}
				
	}
	
	public String getName() {
		return name;
	}
	
	public ArrayList<UUID> getOwned() {
		return owned;
	}
	
	public String[] getLore() {
		return lore;
	}
	
	public ItemStack getLogo() {
		return logo;
	}
	
	public Category getCategory() {
		return cat;
	}
	
	public ItemStack[] getWin() {
		return win;
	}
	
	public ItemStack[] getItems() {
		return items;
	}
	
	public File getFile() {
		return file;
	}
	
	public FileConfiguration getFileConfiguration() {
		return config;
	}
	
	public void saveConfig() {
		try {
			config.save(file);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	public void reloadConfig() {
		config = YamlConfiguration.loadConfiguration(file);
	}
	
	public int getID() {
		return ID;
	}
	
	public Difficulty getDifficulty() {
		return difficulty;
	}
	
	public void addOwned(Island is) {
		owned.add(is.getUUID());
		List<String> uuids = new ArrayList<String>();
		for(UUID o : owned) {
			uuids.add(o.toString());
		}
		config.set("owned", uuids);
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
