package nl.stijn.SkyBlock.Challenges;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import nl.stijn.SkyBlock.SkyBlock;
import nl.stijn.SkyBlock.Utils.ScreenUtil;

public class ChallengeManager {

	private static ChallengeManager instance = new ChallengeManager();

	public static ChallengeManager getInstance() {
		return instance;
	}

	public ArrayList<Challenge> challenges;

	private ChallengeManager() {
		this.challenges = new ArrayList<Challenge>();
	}

	public void setup() {
		challenges.clear();

		File[] files = new File(SkyBlock.p.getDataFolder() + "/challenges").listFiles();
		if(files == null) {
			return;
		}

		for(File file : files) {
			try {
				FileConfiguration c = YamlConfiguration.loadConfiguration(file);

				int id = c.getInt("id");
				String name = c.getString("name");

				List<String> li = c.getStringList("lore");
				String[] lore = li.toArray(new String[li.size()]);

				ItemStack logo = c.getItemStack("logo");

				ArrayList<ItemStack> i = new ArrayList<ItemStack>();
				for(String s : c.getConfigurationSection("contents").getKeys(false)) {
					Integer r = Integer.parseInt(s);
					i.add(c.getItemStack("contents." + r));
				}

				ArrayList<ItemStack> i2 = new ArrayList<ItemStack>();
				for(String s : c.getConfigurationSection("win").getKeys(false)) {
					Integer r = Integer.parseInt(s);
					i2.add(c.getItemStack("win." + r));
				}

				ItemStack[] items = i.toArray(new ItemStack[i.size()]);

				ItemStack[] win = i2.toArray(new ItemStack[i2.size()]);

				Category cat = CategoryManager.getInstance().getCategory(c.getString("category"));

				String difficulty = c.getString("difficulty");

				challenges.add(new Challenge(id, name, lore, logo, items, win, file, c, cat, getDifficulty(difficulty)));

			} catch(Exception e) {
				System.out.println("An error occured!");
				e.printStackTrace();
			}
		}

	}

	public void updateChallenge(Challenge ch, ListIterator<Challenge> it) {
		if(challenges.contains(ch)) {
			it.remove();
			FileConfiguration c = YamlConfiguration.loadConfiguration(ch.getFile());

			int id = c.getInt("id");
			String name = c.getString("name");

			List<String> li = c.getStringList("lore");
			String[] lore = li.toArray(new String[li.size()]);

			ItemStack logo = c.getItemStack("logo");

			ArrayList<ItemStack> i = new ArrayList<ItemStack>();
			for(String s : c.getConfigurationSection("contents").getKeys(false)) {
				Integer r = Integer.parseInt(s);
				i.add(c.getItemStack("contents." + r));
			}

			ArrayList<ItemStack> i2 = new ArrayList<ItemStack>();
			for(String s : c.getConfigurationSection("win").getKeys(false)) {
				Integer r = Integer.parseInt(s);
				i2.add(c.getItemStack("win." + r));
			}

			ItemStack[] items = i.toArray(new ItemStack[i.size()]);

			ItemStack[] win = i2.toArray(new ItemStack[i2.size()]);

			Category cat = CategoryManager.getInstance().getCategory(c.getString("category"));

			String difficulty = c.getString("difficulty");

			it.add(new Challenge(id, name, lore, logo, items, win, ch.getFile(), c, cat, getDifficulty(difficulty)));

		}
	}

	public void setupNewChallenge(String name, String[] lore, ItemStack logo, ItemStack[] items, ItemStack[] win, Category cat, Difficulty difficulty) {
		int id = 0;
		try {
			id = getNewID();
		} catch(Exception e) {
			id = 0;
		}
		File file = new File(SkyBlock.p.getDataFolder() + "/challenges", name + ".yml");
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);

		config.set("id", id);
		config.set("name", name);
		config.set("category", cat.getName());
		config.set("difficulty", difficulty.getName());
		List<String> lor = config.getStringList("lore");
		for(String l : lore) {
			lor.add(l);
		}

		config.set("logo", logo);

		List<String> li = config.getStringList("lore");
		for(String s : lore) {
			li.add(s);
		}
		config.set("lore", li);

		int i = 0;
		for(ItemStack it : items) {
			config.set("contents." + i , it);
			i++;
		}

		int t = 0;
		for(ItemStack it : win) {
			config.set("win." + t , it);
			t++;
		}

		try {
			config.save(file);
		} catch(Exception e) {
			e.printStackTrace();
		}

		Challenge c = new Challenge(id, name, lore, logo, items, win, file, config, cat, difficulty);
		challenges.add(c);

		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch(IOException e) {
				System.out.println(ScreenUtil.prefix + "Error creating challenge!");
			}
		}

	}

	public Difficulty getDifficulty(String name) {
		Difficulty re = null;
		for(Difficulty d : Difficulty.values()) {
			if(d.getName().equals(name)) {
				re = d;
				return re;
			}
		}
		return re;
	}

	public Challenge getChallenge(String name) {
		Challenge re = null;
		for(Challenge c : this.challenges) {
			if(c.getName().equals(name)) {
				re = c;
				return re;
			}
		}
		return re;
	}

	public int getNewID() {
		ArrayList<Integer> ints = new ArrayList<Integer>();
		for(Challenge c : this.challenges) {
			ints.add(c.getID());
		}

		Collections.sort(ints);

		Integer[] arr = new Integer[ints.size()];
		arr = ints.toArray(arr);

		ArrayList<Integer> in = findMissingNumbers(arr, 0, ints.size());

		return in.get(0);

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

}