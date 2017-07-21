package nl.stijn.SkyBlock.Challenges;

import org.bukkit.inventory.ItemStack;

public class CategoryTuple {
	
	private String name;
	private ItemStack logo;
	private ItemStack[] items;
	private int slot;
	
	public CategoryTuple(String name, ItemStack logo, ItemStack[] items, int slot) {
		this.name = name;
		this.logo = logo;
		this.items = items;
		
	}
	
	public String getName() {
		return name;
	}
	
	public ItemStack getLogo() {
		return logo;
	}
	
	public ItemStack[] getItems() {
		return items;
	}
	
	public int getSlot() {
		return slot;
	}

}
