package nl.stijn.SkyBlock.Settings;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;

public class SettingsManager {

	private SettingsManager() { }

	static SettingsManager instance = new SettingsManager();

	public static SettingsManager getInstance() {
		return instance;
	}

	Plugin p;

	FileConfiguration config;
	File cfile;

	FileConfiguration cats;
	File cafile;

	FileConfiguration pl;
	File plfile;

	FileConfiguration re;
	File refile;

	public void setup(Plugin p) {
		cfile = new File(p.getDataFolder(), "config.yml");
		config = p.getConfig();

		if (!p.getDataFolder().exists()) {
			p.getDataFolder().mkdir();
		}

		cafile = new File(p.getDataFolder(), "categories.yml");

		if (!cafile.exists()) {
			try {
				cafile.createNewFile();
			}
			catch (IOException e) {
				Bukkit.getServer().getLogger().severe(ChatColor.RED + "Could not create categories.yml!");
			}
		}

		cats = YamlConfiguration.loadConfiguration(cafile);

		plfile = new File(p.getDataFolder(), "schematics.yml");

		if (!plfile.exists()) {
			try {
				plfile.createNewFile();
			}
			catch (IOException e) {
				Bukkit.getServer().getLogger().severe(ChatColor.RED + "Could not create schematics.yml!");
			}
		}

		pl = YamlConfiguration.loadConfiguration(plfile);

		refile = new File(p.getDataFolder(), "resets.yml");

		if (!refile.exists()) {
			try {
				refile.createNewFile();
			}
			catch (IOException e) {
				Bukkit.getServer().getLogger().severe(ChatColor.RED + "Could not create resets.yml!");
			}
		}

		re = YamlConfiguration.loadConfiguration(refile);
	}

	public FileConfiguration getCats() {
		return cats;
	}

	public FileConfiguration getSchematics() {
		return pl;
	}

	public FileConfiguration getResets() {
		return re;
	}

	public void saveCats() {
		try {
			cats.save(cafile);
		}
		catch (IOException e) {
			Bukkit.getServer().getLogger().severe(ChatColor.RED + "Could not save categories.yml!");
		}
	}
	
	public void saveResets() {
		try {
			re.save(refile);
		}
		catch (IOException e) {
			Bukkit.getServer().getLogger().severe(ChatColor.RED + "Could not save resets.yml!");
		}
	}


	public void reloadCats() {
		cats = YamlConfiguration.loadConfiguration(cafile);
	}

	public void reloadResets() {
		re = YamlConfiguration.loadConfiguration(refile);
	}

	public void saveSchematics() {
		try {
			pl.save(plfile);
		}
		catch (IOException e) {
			Bukkit.getServer().getLogger().severe(ChatColor.RED + "Could not save schematics.yml!");
		}
	}

	public void reloadSchematics() {
		cats = YamlConfiguration.loadConfiguration(cafile);
	}

	public FileConfiguration getConfig() {
		return config;
	}

	public void saveConfig() {
		try {
			config.save(cfile);
		}
		catch (IOException e) {
			Bukkit.getServer().getLogger().severe(ChatColor.RED + "Could not save config.yml!");
		}
	}

	public void reloadConfig() {
		config = YamlConfiguration.loadConfiguration(cfile);
	}

	public PluginDescriptionFile getDesc() {
		return p.getDescription();
	}
}