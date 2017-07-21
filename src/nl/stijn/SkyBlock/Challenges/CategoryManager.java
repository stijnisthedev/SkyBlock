package nl.stijn.SkyBlock.Challenges;

import java.util.ArrayList;

import org.bukkit.inventory.ItemStack;

import nl.stijn.SkyBlock.Settings.SettingsManager;

public class CategoryManager {

	private static CategoryManager instance = new CategoryManager();

	public static CategoryManager getInstance() {
		return instance;
	}
	
	public ArrayList<Category> cats;
	public SettingsManager sm = SettingsManager.getInstance();

	private CategoryManager() {
		this.cats = new ArrayList<Category>();
	}
	
	public void setup() {
		if(sm.getCats().get("cats") == null) {
			return;
		}
		for(String s : sm.getCats().getConfigurationSection("cats").getKeys(false)) {
			String name = s;
			ItemStack logo = sm.getCats().getItemStack("cats." + name + ".logo");
			int slot = sm.getCats().getInt("cats." + name + ".slot");
			
			Category c = new Category(name, logo, null, slot);
			cats.add(c);
		}
	}
	
	public String pass = "thomasiseenbeetjegeymaardatisoke";
	
	public void setupNewCategory(String name, ItemStack logo, ItemStack[] items, int slot) {
		Category c = new Category(name, logo, items, slot);
		cats.add(c);
		
		sm.getCats().set("cats." + name + ".name", name);
		sm.getCats().set("cats." + name + ".logo", logo);
		sm.getCats().set("cats." + name + ".slot", slot);
		int i = 0;
		for(ItemStack it : items) {
			sm.getCats().set("cats." + name + ".items." + i , it);
			i++;
		}
		sm.saveCats();
		return;
	}
	
	public Category getCategory(String name) {
		Category re = null;
		for(Category c : this.cats) {
			if(c.getName().equals(name)) {
				re = c;
				return re;
			}
		}
		return re;
	}
	
	
	public Challenge[] getChallenges(Category cat) {
		ArrayList<Challenge> challenge = new ArrayList<Challenge>();
		
		for(Challenge ch : ChallengeManager.getInstance().challenges) {
			if(ch != null && cat != null) {
				if(ch.getCategory().equals(cat)) {
					challenge.add(ch);
				}
			}
		}
		
		Challenge[] c = new Challenge[challenge.size()];
		c = challenge.toArray(c);
		return c;
	
	
	}
}
