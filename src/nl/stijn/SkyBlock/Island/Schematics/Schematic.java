package nl.stijn.SkyBlock.Island.Schematics;

import org.bukkit.inventory.ItemStack;

public class Schematic {
	
	private String name;
	private String file;
	private String description;
	private ItemStack logo;
	
	public Schematic(String name, String file, String description, ItemStack logo) {
		this.name = name;
		this.file = file;
		this.description = description;
		this.logo = logo;
	}
	
	public String getName() {
		return name;
	}
	
	public String getFile() {
		return file + ".schematic";
	}
	
	public String getDescription() {
		return description;
	}
	
	public ItemStack getLogo() {
		return logo;
	}

}
