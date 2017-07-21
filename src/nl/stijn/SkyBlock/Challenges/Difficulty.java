package nl.stijn.SkyBlock.Challenges;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum Difficulty {

	EASY("Easy", "Simple challenges.", new ItemStack(Material.DETECTOR_RAIL), ChatColor.GREEN),
	MEDIUM("Medium", "Not really hard challenges", new ItemStack(Material.WORKBENCH), ChatColor.AQUA),
	HARD("Hard", "Hard challenges", new ItemStack(Material.WORKBENCH), ChatColor.RED);
	
	private String name;
	private String description;
	private ItemStack item;
	private ChatColor color;
	
	private Difficulty(String name, String description, ItemStack item, ChatColor color) {
		this.name = name;
		this.description = description;
		this.item = item;
		this.color = color;
	}
	
	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public ItemStack getItem() {
		return item;
	}
	
	public ChatColor getColor() {
		return color;
	}
	
}
