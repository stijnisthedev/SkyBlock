package nl.stijn.SkyBlock.Island;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.block.Block;

public class IslandPoints {
	
	private static IslandPoints instance = new IslandPoints();
	private HashMap<Material, Integer> points = new HashMap<Material, Integer>();

	public static IslandPoints getInstance() {
		return instance;
	}
	
	private IslandPoints() {
		points.put(Material.COBBLESTONE, 1);
		points.put(Material.WOOD, 2);
		points.put(Material.LOG, 2);
		points.put(Material.GRASS, 2);
		points.put(Material.OBSIDIAN, 10);
		points.put(Material.ANVIL, 5);
		points.put(Material.BEACON, 100);
		points.put(Material.IRON_ORE, 3);
		points.put(Material.COAL_ORE, 1);
		points.put(Material.DIAMOND_ORE, 7);
		points.put(Material.BANNER, 2);
		points.put(Material.COBBLESTONE_STAIRS, 1);
		points.put(Material.TRAP_DOOR, 2);
		points.put(Material.TORCH, 1);
		points.put(Material.FURNACE, 2);
		points.put(Material.WOOD_BUTTON, 1);
		points.put(Material.STONE_BUTTON, 1);
		points.put(Material.SAND, 1);
		points.put(Material.WORKBENCH, 2);
		points.put(Material.BREWING_STAND, 10);
		
	}
	
	public int calculatePoints(Island is) {
		int point = 0;
		for (int x = is.getBounds().getMinimumPoint().getBlockX(); x <= is.getBounds().getMaximumPoint().getBlockX(); x++) {
			for (int y = is.getBounds().getMinimumPoint().getBlockY(); y <= is.getBounds().getMaximumPoint().getBlockY(); y++) {
				for (int z = is.getBounds().getMinimumPoint().getBlockZ(); z <= is.getBounds().getMaximumPoint().getBlockZ(); z++) {
					Block b = is.getBounds().getWorld().getBlockAt(x, y, z);
					if(points.containsKey(b.getType())) {
						point = point+points.get(b.getType());
					}
				}
			}
		}
		return point;
		
	}

}
