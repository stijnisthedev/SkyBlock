package nl.stijn.SkyBlock.Island.Commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;

import nl.stijn.SkyBlock.Island.Island;
import nl.stijn.SkyBlock.Island.IslandManager;
import nl.stijn.SkyBlock.Utils.ScreenUtil;

public class SetBiome extends SubCommand {

	@Override
	public void onCommand(Player p, String[] args) {
		if(args.length == 0) {
			p.sendMessage(ScreenUtil.prefix + "Please specify a players island!");
			return;
		}
		if(args.length == 1) {
			p.sendMessage(ScreenUtil.prefix + "Please specify a biome!");
			return;
		}
		@SuppressWarnings("deprecation")
		OfflinePlayer of = Bukkit.getOfflinePlayer(args[0]);
		if(!of.hasPlayedBefore()) {
			p.sendMessage(ScreenUtil.prefix + "Sorry, this player does not exist!");
			return;
		}
		Island is = IslandManager.getInstance().getIslandByOfflineUUID(of.getUniqueId());
		if(is == null) {
			p.sendMessage(ScreenUtil.prefix + "Sorry, this player doesn't have an island!");
			return;
		}
		Biome biome = null;
		
		try {
			biome = Biome.valueOf(args[1].toUpperCase());
			is.setBiome(biome);
			p.sendMessage(ScreenUtil.prefix + "Biome set to " + args[1] + "!");
			return;
		}
		catch(Exception e) {
			p.sendMessage(ScreenUtil.prefix + "Please specify a valid biome!");
		}

	}

	@Override
	public String name() {
		return "setbiome";
	}

	@Override
	public String info() {
		return "Set the biome of an island!";
	}

	@Override
	public String[] aliases() {
		return new String[] {"sb"};
	}

	@Override
	public boolean admin() {
		return true;
	}

}
