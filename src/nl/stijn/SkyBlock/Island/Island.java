package nl.stijn.SkyBlock.Island;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.sk89q.worldedit.bukkit.selections.CuboidSelection;

import nl.stijn.SkyBlock.SkyBlock;
import nl.stijn.SkyBlock.Challenges.Category;
import nl.stijn.SkyBlock.Challenges.CategoryManager;

public class Island {

	private UUID uuid;
	private int id;
	private String schematic;
	private File file;
	private FileConfiguration config;
	private Location loc;
	private ArrayList<UUID> owners;
	private CuboidSelection bounds;
	private UUID owner;
	private Location home;
	private int points;
	private boolean pvp;
	private ArrayList<Category> done;

	public Island(UUID uuid, UUID owner, int id, String schematic, File file, Location loc) {
		this.uuid = uuid;
		this.id = id;
		this.schematic = schematic;
		this.file = file;
		this.config = YamlConfiguration.loadConfiguration(file);
		this.loc = loc;
		this.owners = new ArrayList<UUID>();
		this.points = config.getInt("points");
		this.done = new ArrayList<Category>();

		Location cA = loc.clone().add(96,106,96);
		Location cB = loc.clone().add(-96, -150, -96);
		this.bounds = new CuboidSelection(Bukkit.getWorld(SkyBlock.p.getConfig().getString("world.W")), cA, cB);
		this.owner = owner;

		Location h = new Location(Bukkit.getWorld(SkyBlock.p.getConfig().getString("world.W")), config.getDouble("home.X"),config.getDouble("home.Y"),config.getDouble("home.Z"),(float) config.getInt("home.YA"), (float) config.getInt("home.P"));
		this.home = h;

		List<String> cowners = config.getStringList("owners");
		for(String i : cowners) {
			owners.add(UUID.fromString(i));
		}

		List<String> doned = config.getStringList("done");
		for(String i : doned) {
			if(CategoryManager.getInstance().getCategory(i) != null)
				done.add(CategoryManager.getInstance().getCategory(i));
		}

		this.pvp = config.getBoolean("pvp");

	}

	public UUID getUUID() {
		return uuid;
	}

	public ArrayList<UUID> getOwners() {
		return owners;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		getConfig().set("points", points);
		saveConfig();
		this.points = points;
	}

	public ArrayList<Category> getDone() {
		return done;
	}

	public Location getHome() {

		Location loc = home.clone();
		if(loc.getBlock().getType().isSolid()) {
			loc = loc.getWorld().getHighestBlockAt(loc).getLocation().add(0,1,0);
			return loc;
		}
		if(!loc.getBlock().getType().isSolid() && loc.clone().add(0,-1,0).getBlock().getType().isSolid()) {
			return loc;
		}
		if(loc.getWorld().getHighestBlockAt(loc) == null) {
			loc.clone().add(0,-1,0).getBlock().setType(Material.GRASS);
		}

		return loc;
	}

	public int getID() {
		return id;
	}

	public boolean getPvP() {
		return pvp;
	}

	public String getSchematic() {
		return schematic;
	}

	public FileConfiguration getConfig() {
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

	public File getFile() {
		return file;
	}

	public Location getLocation() {
		return loc;
	}

	public CuboidSelection getBounds() {
		return bounds;
	}

	public UUID getOwner() {
		return owner;
	}

	public void addOwner(UUID uuid) {
		List<String> uuids = this.config.getStringList("owners");
		uuids.add(uuid.toString());
		this.config.set("owners", uuids);
		owners.add(uuid);
		try {
			this.config.save(this.file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void removeOwner(UUID uuid) {
		List<String> uuids = this.config.getStringList("owners");
		uuids.remove(uuid.toString());
		this.config.set("owners", uuids);
		owners.remove(uuid);
		try {
			this.config.save(this.file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void addDone(Category cat) {
		List<String> uuids = this.config.getStringList("done");
		uuids.add(cat.getName());
		this.config.set("done", uuids);
		done.add(cat);
		saveConfig();

	}

	public void removeDone(Category cat) {
		List<String> uuids = this.config.getStringList("done");
		uuids.remove(cat.getName());
		this.config.set("done", uuids);
		done.remove(cat);
		saveConfig();

	}

	public void addLog(String log) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("YY/MM/DD HH:mm:ss");
		List<String> logs = this.config.getStringList("logs");
		logs.add(log + " Date: " + sdf.format(cal.getTime()));
		this.config.set("logs", logs);
		try {
			this.config.save(this.file);
		} catch (IOException e) {
			e.printStackTrace();
		}    
	}

	public void setPvP(boolean toggle) {
		this.config.set("pvp", toggle);
		saveConfig();
		IslandManager.getInstance().updateIsland(this);

	}

	public void setBiome(Biome biome) {
		ArrayList<Chunk> chunks = new ArrayList<Chunk>();
		for (int x = this.bounds.getMinimumPoint().getBlockX(); x <= this.bounds.getMaximumPoint().getBlockX(); x++) {
			for (int y = this.bounds.getMinimumPoint().getBlockY(); y <= this.bounds.getMaximumPoint().getBlockY(); y++) {
				for (int z = this.bounds.getMinimumPoint().getBlockZ(); z <= this.bounds.getMaximumPoint().getBlockZ(); z++) {
					Block blk = this.bounds.getMinimumPoint().getWorld().getBlockAt(new Location(this.bounds.getMinimumPoint().getWorld(), x, y, z));
					if(!chunks.contains(blk.getChunk())) {
						chunks.add(blk.getChunk());
					}
				}
			}
		}
		for(Chunk ch : chunks) {
			setChunkBiome(ch, biome);
		}
		return;
	}

	public void setChunkBiome(Chunk chunk, Biome biome) {
		for(int x = 0 ; x < 16; x++) {
			for(int z = 0 ; z < 16; z++) {
				final Block block = chunk.getBlock(x, 0, z);
				block.setBiome(biome);
			}
		}

	}
}
