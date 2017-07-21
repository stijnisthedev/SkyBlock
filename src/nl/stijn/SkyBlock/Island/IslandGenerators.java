package nl.stijn.SkyBlock.Island;

import java.util.Random;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;

public class IslandGenerators implements Listener{

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onFromTo(BlockFromToEvent event)
	{
		int id = event.getBlock().getTypeId();
		if(id >= 8 && id <= 11)
		{
			Block b = event.getToBlock();
			int toid = b.getTypeId();
			if(toid == 0)
			{
				if(generatesCobble(id, b))
				{
					
					int random = getRandom(1, 200);
					
					if(random == 1 || random == 2 || random == 3 || random == 4 || random == 5) {
						event.getToBlock().setType(Material.COAL_ORE);
						return;
					}
					
					if(random == 6 || random == 7 || random == 8 || random == 9) {
						event.getToBlock().setType(Material.IRON_ORE);
						event.getToBlock().getWorld().playEffect(event.getToBlock().getLocation().add(0.5,1,0.5), Effect.WITCH_MAGIC, 100);
						event.getToBlock().getWorld().playEffect(event.getToBlock().getLocation().add(0.6,1,0.6), Effect.WITCH_MAGIC, 100);
						event.getToBlock().getWorld().playEffect(event.getToBlock().getLocation().add(0.4,1,0.4), Effect.WITCH_MAGIC, 100);
						event.getToBlock().getWorld().playEffect(event.getToBlock().getLocation().add(0.3,1,0.3), Effect.WITCH_MAGIC, 100);
						event.getToBlock().getWorld().playEffect(event.getToBlock().getLocation().add(0.7,1,0.7), Effect.WITCH_MAGIC, 100);
						return;
					}
					
					if(random == 10 || random == 11 || random == 12) {
						event.getToBlock().setType(Material.GOLD_ORE);
						event.getToBlock().getWorld().playEffect(event.getToBlock().getLocation().add(0.5,1,0.5), Effect.WITCH_MAGIC, 100);
						event.getToBlock().getWorld().playEffect(event.getToBlock().getLocation().add(0.6,1,0.6), Effect.WITCH_MAGIC, 100);
						event.getToBlock().getWorld().playEffect(event.getToBlock().getLocation().add(0.4,1,0.4), Effect.WITCH_MAGIC, 100);
						event.getToBlock().getWorld().playEffect(event.getToBlock().getLocation().add(0.3,1,0.3), Effect.WITCH_MAGIC, 100);
						event.getToBlock().getWorld().playEffect(event.getToBlock().getLocation().add(0.7,1,0.7), Effect.WITCH_MAGIC, 100);
						return;
					}
					
					if(random == 13 || random == 14) {
						event.getToBlock().setType(Material.LAPIS_ORE);
						return;
					}
					
					if(random == 15) {
						event.getToBlock().setType(Material.DIAMOND_ORE);
						event.getToBlock().getWorld().playEffect(event.getToBlock().getLocation().add(0.5,1,0.5), Effect.WITCH_MAGIC, 100);
						event.getToBlock().getWorld().playEffect(event.getToBlock().getLocation().add(0.6,1,0.6), Effect.WITCH_MAGIC, 100);
						event.getToBlock().getWorld().playEffect(event.getToBlock().getLocation().add(0.4,1,0.4), Effect.WITCH_MAGIC, 100);
						event.getToBlock().getWorld().playEffect(event.getToBlock().getLocation().add(0.3,1,0.3), Effect.WITCH_MAGIC, 100);
						event.getToBlock().getWorld().playEffect(event.getToBlock().getLocation().add(0.7,1,0.7), Effect.WITCH_MAGIC, 100);
						return;
					}
					
					if(random == 16 || random == 17 || random == 21 || random == 22) {
						event.getToBlock().setType(Material.REDSTONE_ORE);
						return;
					}
					
					if(random == 18) {
						event.getToBlock().setType(Material.EMERALD_ORE);
						event.getToBlock().getWorld().playEffect(event.getToBlock().getLocation().add(0.5,1,0.5), Effect.WITCH_MAGIC, 100);
						event.getToBlock().getWorld().playEffect(event.getToBlock().getLocation().add(0.6,1,0.6), Effect.WITCH_MAGIC, 100);
						event.getToBlock().getWorld().playEffect(event.getToBlock().getLocation().add(0.4,1,0.4), Effect.WITCH_MAGIC, 100);
						event.getToBlock().getWorld().playEffect(event.getToBlock().getLocation().add(0.3,1,0.3), Effect.WITCH_MAGIC, 100);
						event.getToBlock().getWorld().playEffect(event.getToBlock().getLocation().add(0.7,1,0.7), Effect.WITCH_MAGIC, 100);
						
						return;
					}
					event.getToBlock().setType(Material.COBBLESTONE);
					return;
				}
			}
		}
	}

	private final BlockFace[] faces = new BlockFace[]
			{
					BlockFace.SELF,
					BlockFace.UP,
					BlockFace.DOWN,
					BlockFace.NORTH,
					BlockFace.EAST,
					BlockFace.SOUTH,
					BlockFace.WEST
			};

	@SuppressWarnings("deprecation")
	public boolean generatesCobble(int id, Block b)
	{
		int mirrorID1 = (id == 8 || id == 9 ? 10 : 8);
		int mirrorID2 = (id == 8 || id == 9 ? 11 : 9);
		for(BlockFace face : faces)
		{
			Block r = b.getRelative(face, 1);
			if(r.getTypeId() == mirrorID1 || r.getTypeId() == mirrorID2)
			{
				return true;
			}
		}
		return false;
	}
	
	public int getRandom(int lower, int upper) {
        Random random = new Random();
        return random.nextInt((upper - lower) + 1) + lower;
    }

}
