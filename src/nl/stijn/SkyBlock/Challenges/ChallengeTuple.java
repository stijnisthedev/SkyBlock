package nl.stijn.SkyBlock.Challenges;

import org.bukkit.inventory.ItemStack;

public class ChallengeTuple {
	
	private String name;
	private String lore;
	private ItemStack logo;
	private ItemStack[] items;
	private ItemStack[] win;
	private Category cat;
	private Difficulty dif;
	
	public ChallengeTuple(String name, String lore, ItemStack logo, ItemStack[] items, ItemStack[] win, Difficulty dif, Category cat) {
		this.name = name;
		this.lore = lore;
		this.logo = logo;
		this.items = items;
		this.win = win;
		this.cat = cat;
		this.dif = dif;
		
	}
	
	public String getName() {
		return name;
	}
	
	public String getLore() {
		return lore;
	}
	
	public ItemStack getLogo() {
		return logo;
	}
	
	public ItemStack[] getItems() {
		return items;
	}
	
	public ItemStack[] getWin() {
		return win;
	}
	
	public Category getCategory() {
		return cat;
	}
	
	public Difficulty getDifficulty() {
		return dif;
	}

}
