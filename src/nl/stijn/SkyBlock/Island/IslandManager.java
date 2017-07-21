package nl.stijn.SkyBlock.Island;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import nl.stijn.SkyBlock.SkyBlock;
import nl.stijn.SkyBlock.Challenges.Challenge;
import nl.stijn.SkyBlock.Challenges.ChallengeManager;
import nl.stijn.SkyBlock.Island.Schematics.Schematic;
import nl.stijn.SkyBlock.Logic.IslandSchematic;
import nl.stijn.SkyBlock.Utils.ScreenUtil;

public class IslandManager {

	private static IslandManager instance = new IslandManager();

	public static IslandManager getInstance() {
		return instance;
	}

	public ArrayList<Island> islands;

	private IslandManager() {
		this.islands = new ArrayList<Island>();
	}

	public void setup() {
		islands.clear();

		File[] files = new File(SkyBlock.p.getDataFolder() + "/islands").listFiles();
		if(files == null) {
			return;
		}

		for(File file : files) {
			try {
				FileConfiguration c = YamlConfiguration.loadConfiguration(file);
				Location l = new Location(Bukkit.getWorld((String) SkyBlock.p.getConfig().get("world.W")), c.getDouble("X"), c.getDouble("Y"), c.getDouble("Z"));
				this.islands.add(new Island(UUID.fromString(c.getString("UUID")), UUID.fromString(c.getString("owner")), c.getInt("id"), c.getString("schematic"), file, l));
			} catch(Exception e) {
				System.out.println("An error occured!");
				e.printStackTrace();
			}
		}
	}

	public void setupIslandNew(UUID owner, Schematic schem, final Player p) {
		int id = 0;
		if(this.islands.isEmpty()) {

		} else {
			id = getLastIsland();
		}

		String num = String.valueOf(id);
		byte[] result = num.getBytes();
		UUID uuid = UUID.nameUUIDFromBytes(result);

		File file = new File(SkyBlock.p.getDataFolder() + "/islands", uuid + ".yml");
		FileConfiguration c = YamlConfiguration.loadConfiguration(file);

		pasteSchematic(schem.getFile(), id);

		int n1 = id/12;
		int n2 = id%12;
		n1 = n1 * 192;
		n2 = n2 * 192;

		final Location l = new Location(Bukkit.getWorld((String) SkyBlock.p.getConfig().get("world.W")), n1, 150, n2); 

		List<String> owners = SkyBlock.p.getConfig().getStringList("owners");

		owners.add(owner.toString());
		c.set("owner", owner.toString());
		c.set("id", id);
		c.set("UUID", uuid.toString());
		c.set("X", l.getX());
		c.set("Y", l.getY());
		c.set("Z", l.getZ());
		c.set("home.X", l.getX());
		c.set("home.Y", l.getY());
		c.set("home.Z", l.getZ());
		c.set("owners", owners);
		c.set("schematic", schem.getFile());
		c.set("points", 0);
		c.set("pvp", false);

		try {
			c.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		final Island i = new Island(uuid, owner, id, schem.getFile(), file, l);
		islands.add(i);
		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch(IOException e) {
				System.out.println(ScreenUtil.prefix + "Error creating island!");
			}
		}
		fillChests(i.getBounds().getMinimumPoint(), i.getBounds().getMaximumPoint());
		Bukkit.getScheduler().scheduleSyncDelayedTask(SkyBlock.p, new Runnable() {
			@Override
			public void run() {
				if(p.isOnline()) {
					p.setHealth(20);
					p.setFoodLevel(20);
					p.teleport(l.clone().add(0.5,0,0.5));
					p.sendMessage(ScreenUtil.prefix + "Island created. Teleporting....");
					Location loc = l.getWorld().getHighestBlockAt(l.clone()).getLocation();
					Cow koe = loc.clone().getWorld().spawn(loc.add(1,0,1), Cow.class);
					koe.setCustomName(ChatColor.RED + "" + ChatColor.BOLD + "Bertha the cow");
					koe.setCustomNameVisible(true);
					koe.setAdult();

				}
			}
		}, 30L);
	}

	public Island getIslandFromUUID(UUID uuid) {
		Island island = null;

		for(Island is : this.islands) {
			if(is.getUUID().equals(uuid)) {
				island = is;
				return island;
			}
		}

		return island;
	}

	public void fillChests(Location min, Location max) {
		ArrayList<Chest> c = new ArrayList<Chest>();
		ArrayList<ItemStack> items = new ArrayList<ItemStack>();
		for (int x = min.getBlockX(); x <= max.getBlockX(); x++) {
			for (int y = min.getBlockY(); y <= max.getBlockY(); y++) {
				for (int z = min.getBlockZ(); z <= max.getBlockZ(); z++) {
					Block blk = min.getWorld().getBlockAt(new Location(min.getWorld(), x, y, z));
					if(blk.getType().equals(Material.CHEST)) {
						Chest ch = (Chest) blk.getState();
						c.add(ch);
					}
				}
			}
		}
		items.add(new ItemStack(Material.LAVA_BUCKET, 1));
		items.add(new ItemStack(Material.ICE, 2));
		items.add(new ItemStack(Material.APPLE, 5));
		items.add(new ItemStack(Material.COOKED_BEEF, 5));
		items.add(new ItemStack(Material.SUGAR_CANE, 1));
		items.add(new ItemStack(Material.CACTUS, 1));
		items.add(new ItemStack(Material.MELON_SEEDS, 1));
		items.add(new ItemStack(Material.PUMPKIN_SEEDS, 1));
		items.add(new ItemStack(Material.TORCH, 5));
		items.add(new ItemStack(Material.SAPLING, 1));
		items.add(new ItemStack(Material.BONE, 8));
		items.add(new ItemStack(Material.LEATHER_BOOTS, 1));
		items.add(new ItemStack(Material.LEATHER_LEGGINGS, 1));
		items.add(new ItemStack(Material.LEATHER_CHESTPLATE, 1));
		items.add(new ItemStack(Material.LEATHER_HELMET, 1));
		items.add(new ItemStack(Material.WOOD_SWORD));

		if(c.size() == 0) {
			return;
		}
		
		for(Chest chh : c) {
			chh.getInventory().clear();
		}

		List<List<ItemStack>> parts = splitAndReturn(items, items.size()/c.size());
		Integer ch = 0;
		HashMap<Integer, List<ItemStack>> it = new HashMap<Integer, List<ItemStack>>();
		for(List<ItemStack> item : parts) {
			it.put(ch, item);
			ch++;
		}
		for(Integer i : it.keySet()) {
			List<ItemStack> ite = it.get(i);
			Chest chh = c.get(i);
			Inventory inv = chh.getInventory();
			for(ItemStack ittt : ite) {
				inv.addItem(ittt);
			}
		}

	}


	public void updateIsland(Island is) {
		if(islands.contains(is)) {
			islands.remove(is);
			FileConfiguration c = YamlConfiguration.loadConfiguration(is.getFile());
			Location l = new Location(Bukkit.getWorld((String) SkyBlock.p.getConfig().get("world.W")), c.getDouble("X"), c.getDouble("Y"), c.getDouble("Z"));
			this.islands.add(new Island(UUID.fromString(c.getString("UUID")), UUID.fromString(c.getString("owner")), c.getInt("id"), c.getString("schematic"), is.getFile(), l));
		}
	}

	public void removeIsland(Island is) {
		
		ListIterator<Challenge> it = ChallengeManager.getInstance().challenges.listIterator();
		while(it.hasNext()) {
			Challenge ch = it.next();
			if(ch.getOwned().contains(is.getUUID())) {
				List<String> owned = ch.getFileConfiguration().getStringList("owned");
				owned.remove(is.getUUID().toString());
				ch.getFileConfiguration().set("owned", owned);
				ch.saveConfig();
				ChallengeManager.getInstance().updateChallenge(ch, it);
			}

		}
		
		File[] files = new File(SkyBlock.p.getDataFolder() + "/islands").listFiles();
		if(files == null) {
			return;
		}
		removeBlocks(is.getBounds().getMinimumPoint(), is.getBounds().getMaximumPoint());
		islands.remove(is);
		if(SkyBlock.points.containsKey(is.getUUID())) {
			SkyBlock.points.remove(is.getUUID());
		}

		for(Entity en : is.getBounds().getWorld().getEntities()) {
			if(is.getBounds().contains(en.getLocation())) {
				en.remove();
			}
		}
		File filetoremove = null;
		for(File file : files) {
			if(file.getName().replace(".yml", "").equals(is.getUUID().toString())) {
				filetoremove = file;
			}
		}
		
		filetoremove.delete();
		return;
	}

	public List<List<ItemStack>> splitAndReturn(List<ItemStack> numbers, int size) {
		List<List<ItemStack>> smallList = new ArrayList<List<ItemStack>>();
		int i = 0;
		while (i + size < numbers.size()) {
			smallList.add(numbers.subList(i, i + size));
			i = i + size;
		}
		smallList.add(numbers.subList(i, numbers.size()));
		return smallList;
	}

	public void setHome(Location loc, Island i) {
		i.getConfig().set("home.X", loc.getX());
		i.getConfig().set("home.Y", loc.getY());
		i.getConfig().set("home.Z", loc.getZ());
		i.getConfig().set("home.YA", loc.getYaw());
		i.getConfig().set("home.P", loc.getPitch());

		try {
			i.getConfig().save(i.getFile());
		} catch (IOException e) {
			e.printStackTrace();
		}

		updateIsland(i);
		return;

	}

	public void removeBlocks(Location min, Location max) {
		for (int x = min.getBlockX(); x <= max.getBlockX(); x++) {
			for (int y = min.getBlockY(); y <= max.getBlockY(); y++) {
				for (int z = min.getBlockZ(); z <= max.getBlockZ(); z++) {
					Block blk = min.getWorld().getBlockAt(new Location(min.getWorld(), x, y, z));

					blk.setType(Material.AIR);
				}
			}
		}
	}

	public Biome getBiome(String name) {
		Biome biome = null;
		switch (name) {
		case "plains":
			biome = Biome.PLAINS;
			break;
		case "birchforest":
			biome = Biome.BIRCH_FOREST;
			break;
		case "taiga":
			biome = Biome.TAIGA;
			break;
		case "desert":
			biome = Biome.DESERT;
			break;
		case "ocean":
			biome = Biome.OCEAN;
			break;
		case "swamp":
			biome = Biome.SWAMPLAND;
			break;
		case "extremehills":
			biome = Biome.EXTREME_HILLS;
			break;
		case "mesa":
			biome = Biome.MESA;
			break;
		case "forest":
			biome = Biome.FOREST;
			break;
		case "beach":
			biome = Biome.BEACH;
			break;
		case "sunflowerplains":
			biome = Biome.SUNFLOWER_PLAINS;
			break;
		case "nether":
			biome = Biome.HELL;
			break;
		case "end":
			biome = Biome.SKY;
			break;
		case "savanna":
			biome = Biome.SAVANNA;
			break;
		case "jungle":
			biome = Biome.JUNGLE;
			break;
		case "deepocean":
			biome = Biome.DEEP_OCEAN;
			break;
		case "megataiga":
			biome = Biome.MEGA_TAIGA;
			break;
		case "icespikes":
			biome = Biome.ICE_PLAINS_SPIKES;
			break;
		default:
			biome = null;
			break;
		}
		return biome;

	}

	public static ArrayList<Integer> findMissingNumbers(Integer[] a, int first, int last) {
		ArrayList<Integer> r = new ArrayList<Integer>();
		for (int i = first; i < a[0]; i++) {
			r.add(i);
		}
		for (int i = 1; i < a.length; i++) {
			for (int j = 1+a[i-1]; j < a[i]; j++) {
				r.add(j);
			}
		}
		for (int i = 1+a[a.length-1]; i <= last; i++) {
			r.add(i);
		}
		return r;
	}

	public int getLastIsland() {
		ArrayList<Integer> isl = new ArrayList<Integer>();
		for(Island is : this.islands) {
			isl.add(is.getID());
		}
		Collections.sort(isl);

		Integer[] arr = new Integer[isl.size()];
		arr = isl.toArray(arr);

		ArrayList<Integer> ints = findMissingNumbers(arr, 0, isl.size());

		return ints.get(0);
	}

	public void pasteSchematic(String schematic, int ID) {
		int n1 = ID/9;
		int n2 = ID%9;
		n1 = n1 * 192;
		n2 = n2 * 192;

		Location l = new Location(Bukkit.getWorld((String) SkyBlock.p.getConfig().get("world.W")), n1, 150, n2);

		IslandSchematic.paste(schematic, l);
	}

	public boolean isHisIsland(Player p, Island i) {
		if(i.getBounds().contains(p.getLocation())) {
			if(i.getOwners().contains(p.getUniqueId())) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public Island getIsland(Player p) {
		Island isl = null;
		for(Island is : this.islands) {
			if(is.getOwner().equals(p.getUniqueId())) {
				isl = is;
			}
		}
		return isl;
	}

	public Island GetIsland(Player p) {
		Island isl = null;
		for(Island is : this.islands) {
			if(is.getOwners().contains(p.getUniqueId())) {
				isl = is;
				return isl;
			}
		}
		return isl;
	}

	public boolean hasIsland(Player p) {
		for(Island i : this.islands) {
			if(i.getOwners().contains(p.getUniqueId())) {
				return true;
			}
		}
		return false;
	}

	public Island getIslandOfPlayer(Player p) {
		Island isl = null;
		for(Island is : this.islands) {
			if(is == null) {
				return null;
			}
			if(is.getBounds() == null) {
				return null;
			}
			if(is.getBounds().contains(p.getLocation())) {
				isl = is;
				return isl;
			}
		}
		return isl;
	}
	
	public Island getIslandByOfflineUUID(UUID uuid) {
		Island is = null;
		
		for(Island isl : islands) {
			if(isl.getOwners().contains(uuid)) {
				is = isl;
				return is;
						
			}
		}
		
		return is;
	}

	public Island getIslandOfLocation(Location l) {
		Island isl = null;
		for(Island is : this.islands) {
			if(is == null) {
				return null;
			}
			if(is.getBounds() == null) {
				return null;
			}
			if(is.getBounds().contains(l)) {
				isl = is;
				return isl;
			}
		}
		return null;

	}

	public boolean HasIsland(Player p) {
		boolean succes = false;
		for(Island i : this.islands) {
			if(i.getOwner().equals(p.getUniqueId())) {
				succes = true;
			}
		}
		return succes;
	}

	public Island getIslandByName(String name) {
		Island is = null;
		if(islands.contains(name)) {
			for (int i = 0; i < islands.size(); i++) {
				if(islands.get(i).equals(name)) {
					is = islands.get(i);
				}
			}
		}
		return is;
	}


}
