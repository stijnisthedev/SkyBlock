package nl.stijn.SkyBlock.PlayerData;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import nl.stijn.SkyBlock.Settings.SettingsManager;

public class PlayerManager {

	private static PlayerManager instance = new PlayerManager();

	public static PlayerManager getInstance() {
		return instance;
	}
	
	public ArrayList<SkyPlayer> players;
	public SettingsManager sm = SettingsManager.getInstance();

	private PlayerManager() {
		this.players = new ArrayList<SkyPlayer>();
	}
	
	public void setup() {
		players.clear();
		for(Player p : Bukkit.getOnlinePlayers()) {
			players.add(new SkyPlayer(p.getUniqueId()));
		}
	}
	
	public SkyPlayer getSkyPlayer(Player of) {
		for(SkyPlayer sp : players) {
			if(sp.getPlayer().equals(of)) {
				return sp;
			}
		}
		return null;
	}
	
	public void updatePlayer(SkyPlayer player) {
		if(players.contains(player)) {
			players.remove(player);
			players.add(player);
		}
	}
	
}
