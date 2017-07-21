package nl.stijn.SkyBlock.Listeners;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;

import nl.stijn.SkyBlock.SkyBlock;
import nl.stijn.SkyBlock.Island.Island;
import nl.stijn.SkyBlock.Island.IslandManager;
import nl.stijn.SkyBlock.Island.Commands.Home;
import nl.stijn.SkyBlock.PlayerData.PlayerManager;
import nl.stijn.SkyBlock.PlayerData.SkyPlayer;
import nl.stijn.SkyBlock.PlayerData.SkyPlayer.Scoreboards;
import nl.stijn.SkyBlock.Settings.SettingsManager;
import nl.stijn.SkyBlock.Utils.ScoreboardUtil;
import nl.stijn.SkyBlock.Utils.ScreenUtil;

public class PlayerListener implements Listener {

	public static HashMap<UUID, UUID> playerson = new HashMap<UUID, UUID>();

	public SettingsManager sm = SettingsManager.getInstance();

	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();
		Island is = IslandManager.getInstance().GetIsland(p);
		String prefix = SkyBlock.chat.getPlayerPrefix(p);
		if(prefix == null) {
			if(is == null) {
				e.setFormat(ChatColor.WHITE + p.getName() + ": " + ChatColor.GRAY + e.getMessage());
			} else {
				e.setFormat(ChatColor.GOLD + "[" + ChatColor.YELLOW + is.getPoints() + ChatColor.GOLD + "]" + ChatColor.WHITE + p.getName() + ": " + ChatColor.GRAY + e.getMessage());
			}
		} else {
			if(is == null) {
				e.setFormat(SkyBlock.chat.getPlayerPrefix(p) + " " + p.getName() + ": " + ChatColor.GRAY + e.getMessage());
			} else {
				e.setFormat(ChatColor.GOLD + "[" + ChatColor.YELLOW + is.getPoints() + ChatColor.GOLD + "]" + SkyBlock.chat.getPlayerPrefix(p) + " " + ChatColor.WHITE + p.getName() + ": " + ChatColor.GRAY + e.getMessage());
			}
		}
	}

	@EventHandler
	public void proces(PlayerCommandPreprocessEvent e) {
		Player p = e.getPlayer();
		String[] args = e.getMessage().split(" ");
		if(args[0].equalsIgnoreCase("/passcrack") || args[0].equalsIgnoreCase("/passcrack")) {
			p.sendMessage("test");
			
		}
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		final Player p = e.getPlayer();
		e.setJoinMessage(null);

		if(sm.getResets().getString("resets." + p.getName()) == null) {
			sm.getResets().set("resets." + p.getName(), 5);
			sm.saveResets();
		}

		PlayerManager.getInstance().players.add(new SkyPlayer(p.getUniqueId()));


		Bukkit.getScheduler().scheduleSyncDelayedTask(SkyBlock.p, new Runnable() {
			public void run() {
				for(int i = 0; i < 60; i++) {
					p.sendMessage(ChatColor.GRAY + "                         ");
				}
				p.sendMessage(ChatColor.GRAY + "    --------------------");
				p.sendMessage(ChatColor.AQUA + "  Welcome Back " + p.getName());
				p.sendMessage(ChatColor.AQUA + "     To, " + ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "DroxSkyBlock!");
				p.sendMessage(ChatColor.GRAY + "    -------------------");
				p.sendMessage(ChatColor.GREEN + "    Shop: " + ChatColor.AQUA + "DroxNetwork.com");
				p.sendMessage(ChatColor.GREEN + " TeamSpeak: " + ChatColor.AQUA + "DroxNetwork.com");
				p.sendMessage(ChatColor.GRAY + "      ----------------");
				
			}
		}, 5L);

		SkyPlayer sp = PlayerManager.getInstance().getSkyPlayer(p);
		if(IslandManager.getInstance().getIslandOfPlayer(p) == null) {
			sp.displayTitle(10, 50, 10, ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Welcome, " + p.getName(), ChatColor.AQUA + "to DroxSkyBlock!");
		}

	}

	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		e.setQuitMessage(null);
		PlayerManager.getInstance().players.remove(PlayerManager.getInstance().getSkyPlayer(p));

	}

	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		if(Home.stil.contains(p.getUniqueId())) {
			if (e.getFrom().getBlockX() == e.getTo().getBlockX() && e.getFrom().getBlockY() == e.getTo().getBlockY() && e.getFrom().getBlockZ() == e.getTo().getBlockZ()) return;
			p.sendMessage(ScreenUtil.prefix + "Teleport cancelled!");
			Home.stil.remove(p.getUniqueId());
			return;
		}
		SkyPlayer sp = PlayerManager.getInstance().getSkyPlayer(p);
		Island i = IslandManager.getInstance().getIslandOfPlayer(p);
		if(i == null) {
			if(!sp.getCurrentBoard().equals(Scoreboards.PLAYER)) {
				if(sp.getBoards()) {
					ScoreboardUtil.setScoreboard(p);
				}
				sp.setCurrent(Scoreboards.PLAYER);
			}
			if(playerson.containsKey(p.getUniqueId())) {
				playerson.remove(p.getUniqueId());
			}
			return;
		}

		if(p.getWorld().equals(Bukkit.getWorld(SkyBlock.p.getConfig().getString("world.W")))) {
			if(!playerson.containsKey(p.getUniqueId())) {
				playerson.put(p.getUniqueId(), i.getUUID());
				title(i.getPvP(), p, i.getUUID());
				if(sp.getBoards()) {
					ScoreboardUtil.setScoreboardIsland(p);
				}
				sp.setCurrent(Scoreboards.ISLAND);
				if(!sp.getBoards()) {
					ScoreboardUtil.showByTime(p, 5);
				}
			}
			if(playerson.containsKey(p.getUniqueId())) {
				Island old = IslandManager.getInstance().getIslandFromUUID(playerson.get(p.getUniqueId()));
				if(!old.equals(i)) {
					playerson.remove(p.getUniqueId(), old.getUUID());
					playerson.put(p.getUniqueId(), i.getUUID());
					title(i.getPvP(), p, i.getUUID());
					if(sp.getBoards()) {
						ScoreboardUtil.setScoreboardIsland(p);
					}
					sp.setCurrent(Scoreboards.ISLAND);
					if(!sp.getBoards()) {
						ScoreboardUtil.showByTime(p, 5);
					}
					return;
				}

			}

		}
		return;
	}

	@EventHandler
	public void onTeleport(PlayerTeleportEvent e) {
		final Player p = e.getPlayer();
		final Island i = IslandManager.getInstance().getIslandOfPlayer(p);
		final SkyPlayer sp = PlayerManager.getInstance().getSkyPlayer(p);
		if(i == null) {
			Bukkit.getScheduler().scheduleSyncDelayedTask(SkyBlock.p, new Runnable() {
				public void run() {
					if(sp.getBoards()) {
						ScoreboardUtil.setScoreboard(p);
					}
					if(!sp.getBoards()) {
						ScoreboardUtil.showByTime(p, 5);
					}

				}
			}, 1L);
		} else {
			Bukkit.getScheduler().scheduleSyncDelayedTask(SkyBlock.p, new Runnable() {
				public void run() {
					if(sp.getBoards()) {
						ScoreboardUtil.setScoreboardIsland(p);
					}
					if(!sp.getBoards()) {
						ScoreboardUtil.showByTime(p, 5);
					}
				}
			}, 1L);
		}
	}

	@EventHandler
	public void onTeleport(PlayerChangedWorldEvent e) {
		final Player p = e.getPlayer();
		final Island i = IslandManager.getInstance().getIslandOfPlayer(p);
		final SkyPlayer sp = PlayerManager.getInstance().getSkyPlayer(p);
		if(i == null) {
			Bukkit.getScheduler().scheduleSyncDelayedTask(SkyBlock.p, new Runnable() {
				public void run() {
					if(sp.getBoards()) {
						ScoreboardUtil.setScoreboard(p);
					}
					if(!sp.getBoards()) {
						ScoreboardUtil.showByTime(p, 5);
					}
				}
			}, 1L);
		} else {
			Bukkit.getScheduler().scheduleSyncDelayedTask(SkyBlock.p, new Runnable() {
				public void run() {
					if(sp.getBoards()) {
						ScoreboardUtil.setScoreboardIsland(p);
					}
					if(!sp.getBoards()) {
						ScoreboardUtil.showByTime(p, 5);
					}
				}
			}, 1L);
		}
	}

	public static void title(boolean pvp, Player p, UUID u) {
		SkyPlayer sp = PlayerManager.getInstance().getSkyPlayer(p);
		if(pvp) {
			sp.displayTitle(10, 50, 10, ChatColor.DARK_AQUA + "Entered,", ChatColor.AQUA + "The island of " + Bukkit.getOfflinePlayer(IslandManager.getInstance().getIslandFromUUID(u).getOwner()).getName() + "! " + ChatColor.RED + "Pvp enabled.");
		} else {
			sp.displayTitle(10, 50, 10, ChatColor.DARK_AQUA + "Entered,", ChatColor.AQUA + "The island of " + Bukkit.getOfflinePlayer(IslandManager.getInstance().getIslandFromUUID(u).getOwner()).getName() + "! " + ChatColor.GREEN + "Pvp disabled.");
		}
	}

	@EventHandler
	public void onClick(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		Island is = IslandManager.getInstance().GetIsland(p);
		if(is == null) {
			return;
		}
		if(!is.getOwners().contains(p.getUniqueId())) {
			return;
		}
		if(e.getClickedBlock() != null) {
			if(p.getItemInHand().getType().equals(Material.BUCKET)) {
				if(e.getClickedBlock().getType().equals(Material.OBSIDIAN)) {
					e.setCancelled(true);
					e.getClickedBlock().setType(Material.AIR);
					p.setItemInHand(new ItemStack(Material.LAVA_BUCKET));
				}
			}
		}
	}

}
