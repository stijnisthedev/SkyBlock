package nl.stijn.SkyBlock.Challenges;

import org.bukkit.inventory.ItemStack;

public class Category {
	
	private String name;
	private ItemStack logo;
	private ItemStack[] items;
	private int slot;
	
	public Category(String name, ItemStack logo, ItemStack[] items, int slot) {
		this.name = name;
		this.logo = logo;
		this.slot = slot;
		this.items = items;
	}
	
	public String getName() {
		return name;
	}
	
	public ItemStack[] getItems() {
		return items;
	}
	
	public ItemStack getLogo() {
		return logo;
	}
	
	public int getSlot() {
		return slot;
	}

}
