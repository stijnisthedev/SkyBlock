package nl.stijn.SkyBlock;

import java.util.HashMap;
import java.util.UUID;
import java.util.logging.LogRecord;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import nl.stijn.SkyBlock.Challenges.CategoryBuilder;
import nl.stijn.SkyBlock.Challenges.CategoryManager;
import nl.stijn.SkyBlock.Challenges.ChallengeBuilder;
import nl.stijn.SkyBlock.Challenges.ChallengeInventory;
import nl.stijn.SkyBlock.Challenges.ChallengeManager;
import nl.stijn.SkyBlock.Island.Island;
import nl.stijn.SkyBlock.Island.IslandGenerators;
import nl.stijn.SkyBlock.Island.IslandManager;
import nl.stijn.SkyBlock.Island.Commands.CommandManager;
import nl.stijn.SkyBlock.Island.GUI.Invite;
import nl.stijn.SkyBlock.Island.GUI.StartGUI;
import nl.stijn.SkyBlock.Island.Schematics.SchematicBuilder;
import nl.stijn.SkyBlock.Island.Schematics.SchematicManager;
import nl.stijn.SkyBlock.Island.Settings.PvpSetting;
import nl.stijn.SkyBlock.Listeners.BlockListener;
import nl.stijn.SkyBlock.Listeners.PlayerListener;
import nl.stijn.SkyBlock.PlayerData.PlayerManager;
import nl.stijn.SkyBlock.PlayerData.SkyPlayer;
import nl.stijn.SkyBlock.Settings.SettingsManager;
import nl.stijn.SkyBlock.Utils.ScoreboardUtil;

public class SkyBlock extends JavaPlugin {
	
	public static boolean works = true;
	public static String nmsver;
	
	public static SkyBlock p;
	
	public boolean worldEditSupport = false;
	public static WorldEditPlugin worldEdit;
	
    public static Chat chat = null;
    public static Economy econ = null;
	
	public static HashMap<String, Island> getIslands = new HashMap<String, Island>();
	public static HashMap<UUID, Integer> points = new HashMap<UUID, Integer>();
	
	public void onEnable() {
		p = this;
		nmsver = Bukkit.getServer().getClass().getPackage().getName();
		nmsver = nmsver.substring(nmsver.lastIndexOf(".") + 1);
		
        if (!setupEconomy() ) {
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
		setupChat();
		setupWorldEdit();
		SettingsManager.getInstance().setup(this);
		IslandManager.getInstance().setup();
		CategoryManager.getInstance().setup();
		ChallengeManager.getInstance().setup();
		SchematicManager.getInstance().setup();
		PlayerManager.getInstance().setup();
		
		Bukkit.getPluginManager().registerEvents(new CategoryBuilder(), this);
		Bukkit.getPluginManager().registerEvents(new IslandGenerators(), this);
		Bukkit.getPluginManager().registerEvents(new ChallengeBuilder(), this);
		Bukkit.getPluginManager().registerEvents(new BlockListener(), this);
		Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
		Bukkit.getPluginManager().registerEvents(new ChallengeInventory(), this);
		Bukkit.getPluginManager().registerEvents(new SchematicBuilder(), this);
		Bukkit.getPluginManager().registerEvents(new PvpSetting(), this);
		
		registerGUI();
		
		CommandManager cm = new CommandManager();
		cm.setup();
		getCommand("island").setExecutor(cm);
		
		nl.stijn.SkyBlock.Challenges.Commands.CommandManager c = new nl.stijn.SkyBlock.Challenges.Commands.CommandManager();
		c.setup();
		getCommand("challenge").setExecutor(c);
		
		registerPoints();
		
		new BukkitRunnable() {
			public void run() {
				for(Player p : Bukkit.getOnlinePlayers()) {
					SkyPlayer sp = PlayerManager.getInstance().getSkyPlayer(p);
					if(sp.getBoards()) {
						ScoreboardUtil.updateScorePlayer(p);
					}
				}
			}
		}.runTaskTimer(SkyBlock.p, 60, 60);
		
	}
	
    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
	
	public boolean isLoggable(LogRecord line) {
	    return !(line.getMessage().contains("/login") || line.getMessage().contains("/register") || line.getMessage().contains("pass"));
	}
	
	public void onDisable() {
		
	}
	
	public static void registerPoints() {
		for(Island is : IslandManager.getInstance().islands) {
			points.put(is.getUUID(), is.getPoints());
		}
	}
	
	private void setupWorldEdit() {
        Plugin plugin = getServer().getPluginManager().getPlugin("WorldEdit");
        if (plugin == null || !(plugin instanceof WorldEditPlugin))
            return;

        worldEditSupport = true;
        worldEdit = (WorldEditPlugin) plugin;
        
    }
	
    public boolean setupChat() {
        RegisteredServiceProvider<Chat> chatProvider = Bukkit.getServer().getServicesManager()
                .getRegistration(Chat.class);
        if (chatProvider != null) {
            chat = chatProvider.getProvider();
        }

        return (chat != null);
    }
	
	public static WorldEditPlugin getWorldEdit() {
		return worldEdit;
	}
	
	public static void registerGUI() {
		Bukkit.getPluginManager().registerEvents(new Invite(), p);
		Bukkit.getPluginManager().registerEvents(new StartGUI(), p);
	}

}
