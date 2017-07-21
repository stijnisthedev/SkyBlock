package nl.stijn.SkyBlock.Listeners;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.painting.PaintingBreakByEntityEvent;
import org.bukkit.event.painting.PaintingPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import nl.stijn.SkyBlock.SkyBlock;
import nl.stijn.SkyBlock.Island.Island;
import nl.stijn.SkyBlock.Island.IslandManager;
import nl.stijn.SkyBlock.Utils.ScreenUtil;

@SuppressWarnings("deprecation")
public class BlockListener implements Listener {

	@EventHandler
	public void BlockPlace(BlockPlaceEvent e) {
		Player p = e.getPlayer();

		if(IslandManager.getInstance().getIslandOfLocation(e.getBlock().getLocation()) == null && e.getBlock().getWorld().getName().equals((String) SkyBlock.p.getConfig().get("world.W"))) {
			e.setCancelled(true);
			return;
		}
		if(IslandManager.getInstance().getIslandOfLocation(e.getBlock().getLocation()) != null) {
			if(IslandManager.getInstance().getIslandOfLocation(e.getBlock().getLocation()).getOwners().contains(p.getUniqueId())) {
				e.setCancelled(false);
			} else {
				p.sendMessage(ScreenUtil.prefix + "Sorry, you can't build on the island of " + Bukkit.getOfflinePlayer(IslandManager.getInstance().getIslandOfLocation(e.getBlock().getLocation()).getOwner()).getName() + "!");
				e.setCancelled(true);
			}
		}

	}
	
	@EventHandler
	public void onPainting(PaintingBreakByEntityEvent e) {
		if(e.getRemover() instanceof Player) {
			Player p = (Player) e.getRemover();
			if(IslandManager.getInstance().getIslandOfLocation(e.getPainting().getLocation()) != null) {
				if(IslandManager.getInstance().getIslandOfLocation(e.getPainting().getLocation()).getOwners().contains(p.getUniqueId())) {
					e.setCancelled(false);
				} else {
					p.sendMessage(ScreenUtil.prefix + "Sorry, you can't build on the island of " + Bukkit.getOfflinePlayer(IslandManager.getInstance().getIslandOfLocation(e.getPainting().getLocation()).getOwner()).getName() + "!");
					e.setCancelled(true);
				}
			}
			if(IslandManager.getInstance().getIslandOfLocation(e.getPainting().getLocation()) == null && e.getPainting().getWorld().getName().equals((String) SkyBlock.p.getConfig().get("world.W"))) {
				e.setCancelled(true);
				return;
			}
		}
	}
	
	@EventHandler
	public void onPaintingPlace(PaintingPlaceEvent e) {
		if(e.getPlayer() instanceof Player) {
			Player p = (Player) e.getPlayer();
			if(IslandManager.getInstance().getIslandOfLocation(e.getPainting().getLocation()) != null) {
				if(IslandManager.getInstance().getIslandOfLocation(e.getPainting().getLocation()).getOwners().contains(p.getUniqueId())) {
					e.setCancelled(false);
				} else {
					p.sendMessage(ScreenUtil.prefix + "Sorry, you can't build on the island of " + Bukkit.getOfflinePlayer(IslandManager.getInstance().getIslandOfLocation(e.getPainting().getLocation()).getOwner()).getName() + "!");
					e.setCancelled(true);
				}
			}
			if(IslandManager.getInstance().getIslandOfLocation(e.getPainting().getLocation()) == null && e.getPainting().getWorld().getName().equals((String) SkyBlock.p.getConfig().get("world.W"))) {
				e.setCancelled(true);
				return;
			}
		}
	}

	@EventHandler
	public void BlockBreak(BlockBreakEvent e) {
		Player p = e.getPlayer();

		if(IslandManager.getInstance().getIslandOfLocation(e.getBlock().getLocation()) != null) {
			if(IslandManager.getInstance().getIslandOfLocation(e.getBlock().getLocation()).getOwners().contains(p.getUniqueId())) {
				e.setCancelled(false);
			} else {
				p.sendMessage(ScreenUtil.prefix + "Sorry, you can't build on the island of " + Bukkit.getOfflinePlayer(IslandManager.getInstance().getIslandOfLocation(e.getBlock().getLocation()).getOwner()).getName() + "!");
				e.setCancelled(true);
			}
		}
		if(IslandManager.getInstance().getIslandOfLocation(e.getBlock().getLocation()) == null && e.getBlock().getWorld().getName().equals((String) SkyBlock.p.getConfig().get("world.W"))) {
			e.setCancelled(true);
			return;
		}
	}

	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e) {
		EntityType[] blocked = new EntityType[] {EntityType.IRON_GOLEM,EntityType.RABBIT,EntityType.CHICKEN,EntityType.COW,EntityType.PIG,EntityType.VILLAGER,EntityType.ARMOR_STAND,EntityType.BOAT,EntityType.ENDERMITE,EntityType.MINECART,EntityType.MINECART_CHEST,EntityType.MINECART_COMMAND,EntityType.MINECART_FURNACE,EntityType.MINECART_HOPPER,EntityType.MINECART_MOB_SPAWNER,EntityType.MINECART_TNT,EntityType.HORSE,EntityType.WOLF,EntityType.MUSHROOM_COW,EntityType.OCELOT,EntityType.SQUID,EntityType.SNOWMAN,EntityType.SLIME,EntityType.PAINTING};
		if(e.getDamager() instanceof Player) {
			Player damager = (Player) e.getDamager();
			ArrayList<EntityType> types = new ArrayList<EntityType>(Arrays.asList(blocked));
			Island is = IslandManager.getInstance().getIslandOfPlayer(damager);
			if(!is.getOwners().contains(damager.getUniqueId())) {
				if(types.contains(e.getEntityType())) {
					e.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		
		if(e.getAction() == Action.PHYSICAL && e.getClickedBlock().getType() == Material.SOIL) {
			if(IslandManager.getInstance().getIslandOfLocation(p.getLocation()) != null) {
				if(IslandManager.getInstance().getIslandOfLocation(p.getLocation()).getOwners().contains(p.getUniqueId())) {
					e.setCancelled(false);
				} else {
					e.setCancelled(true);
					e.setUseInteractedBlock(Result.DENY);
					e.getClickedBlock().setTypeIdAndData(e.getClickedBlock().getType().getId(), e.getClickedBlock().getData(), true);
				}
			}
			if(IslandManager.getInstance().getIslandOfLocation(p.getLocation()) == null && e.getClickedBlock().getWorld().getName().equals((String) SkyBlock.p.getConfig().get("world.W"))) {
				e.setCancelled(true);
				return;
			}
		}
		if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			if(e.getClickedBlock() == null) {
				return;
			}
			if(e.getClickedBlock().getType().equals(Material.CHEST)) {
				if(IslandManager.getInstance().getIslandOfLocation(e.getClickedBlock().getLocation()) != null) {
					if(IslandManager.getInstance().getIslandOfLocation(e.getClickedBlock().getLocation()).getOwners().contains(p.getUniqueId())) {
						e.setCancelled(false);
					} else {
						p.sendMessage(ScreenUtil.prefix + "Sorry, you can't open a chest on the island of " + Bukkit.getOfflinePlayer(IslandManager.getInstance().getIslandOfLocation(e.getClickedBlock().getLocation()).getOwner()).getName() + "!");
						e.setCancelled(true);
					}
				}
				if(IslandManager.getInstance().getIslandOfLocation(e.getClickedBlock().getLocation()) == null && e.getClickedBlock().getWorld().getName().equals((String) SkyBlock.p.getConfig().get("world.W"))) {
					e.setCancelled(true);
					return;
				}
			}
			if(e.getClickedBlock().getType().equals(Material.FURNACE)) {
				if(IslandManager.getInstance().getIslandOfLocation(e.getClickedBlock().getLocation()) != null) {
					if(IslandManager.getInstance().getIslandOfLocation(e.getClickedBlock().getLocation()).getOwners().contains(p.getUniqueId())) {
						e.setCancelled(false);
					} else {
						p.sendMessage(ScreenUtil.prefix + "Sorry, you can't open a furnace on the island of " + Bukkit.getOfflinePlayer(IslandManager.getInstance().getIslandOfLocation(e.getClickedBlock().getLocation()).getOwner()).getName() + "!");
						e.setCancelled(true);
					}
				}
				if(IslandManager.getInstance().getIslandOfLocation(e.getClickedBlock().getLocation()) == null && e.getClickedBlock().getWorld().getName().equals((String) SkyBlock.p.getConfig().get("world.W"))) {
					e.setCancelled(true);
					return;
				}
			}
			if(e.getClickedBlock().getType().equals(Material.DROPPER)) {
				if(IslandManager.getInstance().getIslandOfLocation(e.getClickedBlock().getLocation()) != null) {
					if(IslandManager.getInstance().getIslandOfLocation(e.getClickedBlock().getLocation()).getOwners().contains(p.getUniqueId())) {
						e.setCancelled(false);
					} else {
						p.sendMessage(ScreenUtil.prefix + "Sorry, you can't open a dropper on the island of " + Bukkit.getOfflinePlayer(IslandManager.getInstance().getIslandOfLocation(e.getClickedBlock().getLocation()).getOwner()).getName() + "!");
						e.setCancelled(true);
					}
				}
				if(IslandManager.getInstance().getIslandOfLocation(e.getClickedBlock().getLocation()) == null && e.getClickedBlock().getWorld().getName().equals((String) SkyBlock.p.getConfig().get("world.W"))) {
					e.setCancelled(true);
					return;
				}
			}
			if(e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
				if(p.getTargetBlock((Set<Material>) null, 5).getType().equals(Material.FIRE)) {
					if(IslandManager.getInstance().getIslandOfLocation(e.getClickedBlock().getLocation()) != null) {
						if(IslandManager.getInstance().getIslandOfLocation(e.getClickedBlock().getLocation()).getOwners().contains(p.getUniqueId())) {
							e.setCancelled(false);
						} else {
							p.sendMessage(ScreenUtil.prefix + "Sorry, you can't open a dropper on the island of " + Bukkit.getOfflinePlayer(IslandManager.getInstance().getIslandOfLocation(e.getClickedBlock().getLocation()).getOwner()).getName() + "!");
							e.setCancelled(true);
						}
					}
					if(IslandManager.getInstance().getIslandOfLocation(e.getClickedBlock().getLocation()) == null && e.getClickedBlock().getWorld().getName().equals((String) SkyBlock.p.getConfig().get("world.W"))) {
						e.setCancelled(true);
						return;
					}
				}
			}
			if(e.getClickedBlock().getType().equals(Material.DISPENSER)) {
				if(IslandManager.getInstance().getIslandOfLocation(e.getClickedBlock().getLocation()) != null) {
					if(IslandManager.getInstance().getIslandOfLocation(e.getClickedBlock().getLocation()).getOwners().contains(p.getUniqueId())) {
						e.setCancelled(false);
					} else {
						p.sendMessage(ScreenUtil.prefix + "Sorry, you can't open a dispenser on the island of " + Bukkit.getOfflinePlayer(IslandManager.getInstance().getIslandOfLocation(e.getClickedBlock().getLocation()).getOwner()).getName() + "!");
						e.setCancelled(true);
					}
				}
				if(IslandManager.getInstance().getIslandOfLocation(e.getClickedBlock().getLocation()) == null && e.getClickedBlock().getWorld().getName().equals((String) SkyBlock.p.getConfig().get("world.W"))) {
					e.setCancelled(true);
					return;
				}
			}
			if(e.getClickedBlock().getType().equals(Material.BREWING_STAND)) {
				if(IslandManager.getInstance().getIslandOfLocation(e.getClickedBlock().getLocation()) != null) {
					if(IslandManager.getInstance().getIslandOfLocation(e.getClickedBlock().getLocation()).getOwners().contains(p.getUniqueId())) {
						e.setCancelled(false);
					} else {
						p.sendMessage(ScreenUtil.prefix + "Sorry, you can't open a brewing stand on the island of " + Bukkit.getOfflinePlayer(IslandManager.getInstance().getIslandOfLocation(e.getClickedBlock().getLocation()).getOwner()).getName() + "!");
						e.setCancelled(true);
					}
				}
				if(IslandManager.getInstance().getIslandOfLocation(e.getClickedBlock().getLocation()) == null && e.getClickedBlock().getWorld().getName().equals((String) SkyBlock.p.getConfig().get("world.W"))) {
					e.setCancelled(true);
					return;
				}
			}
			if(e.getClickedBlock().getType().equals(Material.TRAP_DOOR)) {
				if(IslandManager.getInstance().getIslandOfLocation(e.getClickedBlock().getLocation()) != null) {
					if(IslandManager.getInstance().getIslandOfLocation(e.getClickedBlock().getLocation()).getOwners().contains(p.getUniqueId())) {
						e.setCancelled(false);
					} else {
						p.sendMessage(ScreenUtil.prefix + "Sorry, you can't do this on the island of " + Bukkit.getOfflinePlayer(IslandManager.getInstance().getIslandOfLocation(e.getClickedBlock().getLocation()).getOwner()).getName() + "!");
						e.setCancelled(true);
					}
				}
				if(IslandManager.getInstance().getIslandOfLocation(e.getClickedBlock().getLocation()) == null && e.getClickedBlock().getWorld().getName().equals((String) SkyBlock.p.getConfig().get("world.W"))) {
					e.setCancelled(true);
					return;
				}
			}
			if(e.getClickedBlock().getType().equals(Material.STONE_BUTTON)) {
				if(IslandManager.getInstance().getIslandOfLocation(e.getClickedBlock().getLocation()) != null) {
					if(IslandManager.getInstance().getIslandOfLocation(e.getClickedBlock().getLocation()).getOwners().contains(p.getUniqueId())) {
						e.setCancelled(false);
					} else {
						p.sendMessage(ScreenUtil.prefix + "Sorry, you can't do this on the island of " + Bukkit.getOfflinePlayer(IslandManager.getInstance().getIslandOfLocation(e.getClickedBlock().getLocation()).getOwner()).getName() + "!");
						e.setCancelled(true);
					}
				}
				if(IslandManager.getInstance().getIslandOfLocation(e.getClickedBlock().getLocation()) == null && e.getClickedBlock().getWorld().getName().equals((String) SkyBlock.p.getConfig().get("world.W"))) {
					e.setCancelled(true);
					return;
				}
			}
			if(e.getClickedBlock().getType().equals(Material.WOOD_BUTTON)) {
				if(IslandManager.getInstance().getIslandOfLocation(e.getClickedBlock().getLocation()) != null) {
					if(IslandManager.getInstance().getIslandOfLocation(e.getClickedBlock().getLocation()).getOwners().contains(p.getUniqueId())) {
						e.setCancelled(false);
					} else {
						p.sendMessage(ScreenUtil.prefix + "Sorry, you can't do this on the island of " + Bukkit.getOfflinePlayer(IslandManager.getInstance().getIslandOfLocation(e.getClickedBlock().getLocation()).getOwner()).getName() + "!");
						e.setCancelled(true);
					}
				}
				if(IslandManager.getInstance().getIslandOfLocation(e.getClickedBlock().getLocation()) == null && e.getClickedBlock().getWorld().getName().equals((String) SkyBlock.p.getConfig().get("world.W"))) {
					e.setCancelled(true);
					return;
				}

			}
			if(e.getClickedBlock().getType().equals(Material.LEVER)) {
				if(IslandManager.getInstance().getIslandOfLocation(e.getClickedBlock().getLocation()) != null) {
					if(IslandManager.getInstance().getIslandOfLocation(e.getClickedBlock().getLocation()).getOwners().contains(p.getUniqueId())) {
						e.setCancelled(false);
					} else {
						p.sendMessage(ScreenUtil.prefix + "Sorry, you can't do this on the island of " + Bukkit.getOfflinePlayer(IslandManager.getInstance().getIslandOfLocation(e.getClickedBlock().getLocation()).getOwner()).getName() + "!");
						e.setCancelled(true);
					}
				}
				if(IslandManager.getInstance().getIslandOfLocation(e.getClickedBlock().getLocation()) == null && e.getClickedBlock().getWorld().getName().equals((String) SkyBlock.p.getConfig().get("world.W"))) {
					e.setCancelled(true);
					return;
				}
			}
			if(e.getClickedBlock().getType().equals(Material.DIODE) || e.getClickedBlock().getType().equals(Material.DIODE_BLOCK_OFF) || e.getClickedBlock().getType().equals(Material.DIODE_BLOCK_ON)) {
				if(IslandManager.getInstance().getIslandOfLocation(e.getClickedBlock().getLocation()) != null) {
					if(IslandManager.getInstance().getIslandOfLocation(e.getClickedBlock().getLocation()).getOwners().contains(p.getUniqueId())) {
						e.setCancelled(false);
					} else {
						p.sendMessage(ScreenUtil.prefix + "Sorry, you can't do this on the island of " + Bukkit.getOfflinePlayer(IslandManager.getInstance().getIslandOfLocation(e.getClickedBlock().getLocation()).getOwner()).getName() + "!");
						e.setCancelled(true);
					}
				}
				if(IslandManager.getInstance().getIslandOfLocation(e.getClickedBlock().getLocation()) == null && e.getClickedBlock().getWorld().getName().equals((String) SkyBlock.p.getConfig().get("world.W"))) {
					e.setCancelled(true);
					return;
				}
			}
			if(e.getClickedBlock().getType().equals(Material.DIODE) || e.getClickedBlock().getType().equals(Material.DIODE_BLOCK_OFF) || e.getClickedBlock().getType().equals(Material.DIODE_BLOCK_ON)) {
				if(IslandManager.getInstance().getIslandOfLocation(e.getClickedBlock().getLocation()) != null) {
					if(IslandManager.getInstance().getIslandOfLocation(e.getClickedBlock().getLocation()).getOwners().contains(p.getUniqueId())) {
						e.setCancelled(false);
					} else {
						p.sendMessage(ScreenUtil.prefix + "Sorry, you can't do this on the island of " + Bukkit.getOfflinePlayer(IslandManager.getInstance().getIslandOfLocation(e.getClickedBlock().getLocation()).getOwner()).getName() + "!");
						e.setCancelled(true);
					}
				}
				if(IslandManager.getInstance().getIslandOfLocation(e.getClickedBlock().getLocation()) == null && e.getClickedBlock().getWorld().getName().equals((String) SkyBlock.p.getConfig().get("world.W"))) {
					e.setCancelled(true);
					return;
				}
			}
			if(e.getClickedBlock().getType().equals(Material.FENCE_GATE)) {
				if(IslandManager.getInstance().getIslandOfLocation(e.getClickedBlock().getLocation()) != null) {
					if(IslandManager.getInstance().getIslandOfLocation(e.getClickedBlock().getLocation()).getOwners().contains(p.getUniqueId())) {
						e.setCancelled(false);
					} else {
						p.sendMessage(ScreenUtil.prefix + "Sorry, you can't do this on the island of " + Bukkit.getOfflinePlayer(IslandManager.getInstance().getIslandOfLocation(e.getClickedBlock().getLocation()).getOwner()).getName() + "!");
						e.setCancelled(true);
					}
				}
				if(IslandManager.getInstance().getIslandOfLocation(e.getClickedBlock().getLocation()) == null && e.getClickedBlock().getWorld().getName().equals((String) SkyBlock.p.getConfig().get("world.W"))) {
					e.setCancelled(true);
					return;
				}
			}
			if(e.getClickedBlock().getType().equals(Material.ACACIA_FENCE)) {
				if(IslandManager.getInstance().getIslandOfLocation(e.getClickedBlock().getLocation()) != null) {
					if(IslandManager.getInstance().getIslandOfLocation(e.getClickedBlock().getLocation()).getOwners().contains(p.getUniqueId())) {
						e.setCancelled(false);
					} else {
						p.sendMessage(ScreenUtil.prefix + "Sorry, you can't do this on the island of " + Bukkit.getOfflinePlayer(IslandManager.getInstance().getIslandOfLocation(e.getClickedBlock().getLocation()).getOwner()).getName() + "!");
						e.setCancelled(true);
					}
				}
				if(IslandManager.getInstance().getIslandOfLocation(e.getClickedBlock().getLocation()) == null && e.getClickedBlock().getWorld().getName().equals((String) SkyBlock.p.getConfig().get("world.W"))) {
					e.setCancelled(true);
					return;
				}
			}
			if(e.getClickedBlock().getType().equals(Material.BIRCH_FENCE_GATE)) {
				if(IslandManager.getInstance().getIslandOfLocation(e.getClickedBlock().getLocation()) != null) {
					if(IslandManager.getInstance().getIslandOfLocation(e.getClickedBlock().getLocation()).getOwners().contains(p.getUniqueId())) {
						e.setCancelled(false);
					} else {
						p.sendMessage(ScreenUtil.prefix + "Sorry, you can't do this on the island of " + Bukkit.getOfflinePlayer(IslandManager.getInstance().getIslandOfLocation(e.getClickedBlock().getLocation()).getOwner()).getName() + "!");
						e.setCancelled(true);
					}
				}
				if(IslandManager.getInstance().getIslandOfLocation(e.getClickedBlock().getLocation()) == null && e.getClickedBlock().getWorld().getName().equals((String) SkyBlock.p.getConfig().get("world.W"))) {
					e.setCancelled(true);
					return;
				}
			}
			if(e.getClickedBlock().getType().equals(Material.DARK_OAK_FENCE_GATE)) {
				if(IslandManager.getInstance().getIslandOfLocation(e.getClickedBlock().getLocation()) != null) {
					if(IslandManager.getInstance().getIslandOfLocation(e.getClickedBlock().getLocation()).getOwners().contains(p.getUniqueId())) {
						e.setCancelled(false);
					} else {
						p.sendMessage(ScreenUtil.prefix + "Sorry, you can't do this on the island of " + Bukkit.getOfflinePlayer(IslandManager.getInstance().getIslandOfLocation(e.getClickedBlock().getLocation()).getOwner()).getName() + "!");
						e.setCancelled(true);
					}
				}
				if(IslandManager.getInstance().getIslandOfLocation(e.getClickedBlock().getLocation()) == null && e.getClickedBlock().getWorld().getName().equals((String) SkyBlock.p.getConfig().get("world.W"))) {
					e.setCancelled(true);
					return;
				}
			}
			if(e.getClickedBlock().getType().equals(Material.JUNGLE_FENCE_GATE)) {
				if(IslandManager.getInstance().getIslandOfLocation(e.getClickedBlock().getLocation()) != null) {
					if(IslandManager.getInstance().getIslandOfLocation(e.getClickedBlock().getLocation()).getOwners().contains(p.getUniqueId())) {
						e.setCancelled(false);
					} else {
						p.sendMessage(ScreenUtil.prefix + "Sorry, you can't do this on the island of " + Bukkit.getOfflinePlayer(IslandManager.getInstance().getIslandOfLocation(e.getClickedBlock().getLocation()).getOwner()).getName() + "!");
						e.setCancelled(true);
					}
				}
				if(IslandManager.getInstance().getIslandOfLocation(e.getClickedBlock().getLocation()) == null && e.getClickedBlock().getWorld().getName().equals((String) SkyBlock.p.getConfig().get("world.W"))) {
					e.setCancelled(true);
					return;
				}
			}
			if(e.getClickedBlock().getType().equals(Material.SPRUCE_FENCE_GATE)) {
				if(IslandManager.getInstance().getIslandOfLocation(e.getClickedBlock().getLocation()) != null) {
					if(IslandManager.getInstance().getIslandOfLocation(e.getClickedBlock().getLocation()).getOwners().contains(p.getUniqueId())) {
						e.setCancelled(false);
					} else {
						p.sendMessage(ScreenUtil.prefix + "Sorry, you can't do this on the island of " + Bukkit.getOfflinePlayer(IslandManager.getInstance().getIslandOfLocation(e.getClickedBlock().getLocation()).getOwner()).getName() + "!");
						e.setCancelled(true);
					}
				}
				if(IslandManager.getInstance().getIslandOfLocation(e.getClickedBlock().getLocation()) == null && e.getClickedBlock().getWorld().getName().equals((String) SkyBlock.p.getConfig().get("world.W"))) {
					e.setCancelled(true);
					return;
				}
			}
			if(e.getClickedBlock().getType().equals(Material.ACACIA_DOOR)) {
				if(IslandManager.getInstance().getIslandOfLocation(e.getClickedBlock().getLocation()) != null) {
					if(IslandManager.getInstance().getIslandOfLocation(e.getClickedBlock().getLocation()).getOwners().contains(p.getUniqueId())) {
						e.setCancelled(false);
					} else {
						p.sendMessage(ScreenUtil.prefix + "Sorry, you can't do this on the island of " + Bukkit.getOfflinePlayer(IslandManager.getInstance().getIslandOfLocation(e.getClickedBlock().getLocation()).getOwner()).getName() + "!");
						e.setCancelled(true);
					}
				}
				if(IslandManager.getInstance().getIslandOfLocation(e.getClickedBlock().getLocation()) == null && e.getClickedBlock().getWorld().getName().equals((String) SkyBlock.p.getConfig().get("world.W"))) {
					e.setCancelled(true);
					return;
				}
			}
			if(e.getClickedBlock().getType().equals(Material.BIRCH_DOOR)) {
				if(IslandManager.getInstance().getIslandOfLocation(e.getClickedBlock().getLocation()) != null) {
					if(IslandManager.getInstance().getIslandOfLocation(e.getClickedBlock().getLocation()).getOwners().contains(p.getUniqueId())) {
						e.setCancelled(false);
					} else {
						p.sendMessage(ScreenUtil.prefix + "Sorry, you can't do this on the island of " + Bukkit.getOfflinePlayer(IslandManager.getInstance().getIslandOfLocation(e.getClickedBlock().getLocation()).getOwner()).getName() + "!");
						e.setCancelled(true);
					}
				}
				if(IslandManager.getInstance().getIslandOfLocation(e.getClickedBlock().getLocation()) == null && e.getClickedBlock().getWorld().getName().equals((String) SkyBlock.p.getConfig().get("world.W"))) {
					e.setCancelled(true);
					return;
				}
			}
			if(e.getClickedBlock().getType().equals(Material.DARK_OAK_DOOR)) {
				if(IslandManager.getInstance().getIslandOfLocation(e.getClickedBlock().getLocation()) != null) {
					if(IslandManager.getInstance().getIslandOfLocation(e.getClickedBlock().getLocation()).getOwners().contains(p.getUniqueId())) {
						e.setCancelled(false);
					} else {
						p.sendMessage(ScreenUtil.prefix + "Sorry, you can't do this on the island of " + Bukkit.getOfflinePlayer(IslandManager.getInstance().getIslandOfLocation(e.getClickedBlock().getLocation()).getOwner()).getName() + "!");
						e.setCancelled(true);
					}
				}
				if(IslandManager.getInstance().getIslandOfLocation(e.getClickedBlock().getLocation()) == null && e.getClickedBlock().getWorld().getName().equals((String) SkyBlock.p.getConfig().get("world.W"))) {
					e.setCancelled(true);
					return;
				}
			}
			if(e.getClickedBlock().getType().equals(Material.JUNGLE_DOOR)) {
				if(IslandManager.getInstance().getIslandOfLocation(e.getClickedBlock().getLocation()) != null) {
					if(IslandManager.getInstance().getIslandOfLocation(e.getClickedBlock().getLocation()).getOwners().contains(p.getUniqueId())) {
						e.setCancelled(false);
					} else {
						p.sendMessage(ScreenUtil.prefix + "Sorry, you can't do this on the island of " + Bukkit.getOfflinePlayer(IslandManager.getInstance().getIslandOfLocation(e.getClickedBlock().getLocation()).getOwner()).getName() + "!");
						e.setCancelled(true);
					}
				}
				if(IslandManager.getInstance().getIslandOfLocation(e.getClickedBlock().getLocation()) == null && e.getClickedBlock().getWorld().getName().equals((String) SkyBlock.p.getConfig().get("world.W"))) {
					e.setCancelled(true);
					return;
				}
			}
			if(e.getClickedBlock().getType().equals(Material.SPRUCE_DOOR)) {
				if(IslandManager.getInstance().getIslandOfLocation(e.getClickedBlock().getLocation()) != null) {
					if(IslandManager.getInstance().getIslandOfLocation(e.getClickedBlock().getLocation()).getOwners().contains(p.getUniqueId())) {
						e.setCancelled(false);
					} else {
						p.sendMessage(ScreenUtil.prefix + "Sorry, you can't do this on the island of " + Bukkit.getOfflinePlayer(IslandManager.getInstance().getIslandOfLocation(e.getClickedBlock().getLocation()).getOwner()).getName() + "!");
						e.setCancelled(true);
					}
				}
				if(IslandManager.getInstance().getIslandOfLocation(e.getClickedBlock().getLocation()) == null && e.getClickedBlock().getWorld().getName().equals((String) SkyBlock.p.getConfig().get("world.W"))) {
					e.setCancelled(true);
					return;
				}
			}
			if(e.getClickedBlock().getType().equals(Material.WOOD_DOOR)) {
				if(IslandManager.getInstance().getIslandOfLocation(e.getClickedBlock().getLocation()) != null) {
					if(IslandManager.getInstance().getIslandOfLocation(e.getClickedBlock().getLocation()).getOwners().contains(p.getUniqueId())) {
						e.setCancelled(false);
					} else {
						p.sendMessage(ScreenUtil.prefix + "Sorry, you can't do this on the island of " + Bukkit.getOfflinePlayer(IslandManager.getInstance().getIslandOfLocation(e.getClickedBlock().getLocation()).getOwner()).getName() + "!");
						e.setCancelled(true);
					}
				}
				if(IslandManager.getInstance().getIslandOfLocation(e.getClickedBlock().getLocation()) == null && e.getClickedBlock().getWorld().getName().equals((String) SkyBlock.p.getConfig().get("world.W"))) {
					e.setCancelled(true);
					return;
				}
			}
			if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
				if(IslandManager.getInstance().getIslandOfLocation(e.getClickedBlock().getLocation()) != null) {
					if(IslandManager.getInstance().getIslandOfLocation(e.getClickedBlock().getLocation()).getOwners().contains(p.getUniqueId())) {
						e.setCancelled(false);
					} else {
						p.sendMessage(ScreenUtil.prefix + "Sorry, you can't do this on the island of " + Bukkit.getOfflinePlayer(IslandManager.getInstance().getIslandOfLocation(e.getClickedBlock().getLocation()).getOwner()).getName() + "!");
						e.setCancelled(true);
					}
				}
				if(IslandManager.getInstance().getIslandOfLocation(e.getClickedBlock().getLocation()) == null && e.getClickedBlock().getWorld().getName().equals((String) SkyBlock.p.getConfig().get("world.W"))) {
					e.setCancelled(true);
					return;
				}
			}
		}
	}
}
