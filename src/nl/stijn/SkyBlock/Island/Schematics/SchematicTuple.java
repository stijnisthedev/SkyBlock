package nl.stijn.SkyBlock.Island.Schematics;

import org.bukkit.inventory.ItemStack;

public class SchematicTuple {
	
	private String name;
	private String file;
	private String description;
	private ItemStack logo;
	
	public SchematicTuple(String name, String file, String description, ItemStack logo) {
		this.name = name;
		this.file = file;
		this.description = description;
		this.logo = logo;
		
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setFile(String file) {
		this.file = file;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public void setLogo(ItemStack logo) {
		this.logo = logo;
	}
	
	public String getName() {
		return name;
	}
	
	public String getFile() {
		return file;
	}
	
	public String getDescription() {
		return description;
	}
	
	public ItemStack getLogo() {
		return logo;
	}

}
