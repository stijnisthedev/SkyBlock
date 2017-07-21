package nl.stijn.SkyBlock.PlayerData;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import nl.stijn.SkyBlock.SkyBlock;
import nl.stijn.SkyBlock.Settings.SettingsManager;
import nl.stijn.SkyBlock.Utils.ScoreboardUtil;

public class SkyPlayer {
	
	public enum Scoreboards {
		ISLAND, PLAYER;
	}
	
	private UUID uuid;
	private Player player;
	private Scoreboards current;
	private boolean wants;
	
	public SkyPlayer(UUID uuid) {
		this.uuid = uuid;
		if(Bukkit.getPlayer(uuid) != null) {
			this.player = Bukkit.getPlayer(uuid);
		}
		if(SettingsManager.getInstance().getConfig().get("scoreboards." + player.getName()) == null) {
			SettingsManager.getInstance().getConfig().set("scoreboards." + player.getName(), true);
		}
		this.wants = SettingsManager.getInstance().getConfig().getBoolean("scoreboards." + player.getName());
		this.current = Scoreboards.PLAYER;
		
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public boolean getBoards() {
		return wants;
	}
	
	public void setWants(boolean wants) {
		SettingsManager.getInstance().getConfig().set("scoreboards." + player.getName(), wants);
		SettingsManager.getInstance().saveConfig();
		this.wants = wants;
		
		if(wants == true) {
			ScoreboardUtil.updateScorePlayer(getPlayer());
			return;
		}
		if(wants == false) {
			getPlayer().setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
			return;
		}
	}
	
	public UUID getUUID() {
		return uuid;
	}
	
	public Scoreboards getCurrentBoard() {
		return current;
	}
	
	public int getResets() {
		return SettingsManager.getInstance().getResets().getInt("resets." + getPlayer().getName());
	}
	
	public void setCurrent(Scoreboards current) {
		this.current = current;
	}

	public void addResets(int resets) {
		SettingsManager.getInstance().getResets().set("resets." + getPlayer().getName(), getResets() + resets);
		SettingsManager.getInstance().saveResets();
		PlayerManager.getInstance().updatePlayer(this);
	}
	
	public void setResets(int resets) {
		SettingsManager.getInstance().getResets().set("resets." + getPlayer().getName(), resets);
		SettingsManager.getInstance().saveResets();
		PlayerManager.getInstance().updatePlayer(this);
	}
	
	public void removeResets(int resets) {
		SettingsManager.getInstance().getResets().set("resets." + getPlayer().getName(), getResets() - resets);
		SettingsManager.getInstance().saveResets();
		PlayerManager.getInstance().updatePlayer(this);
	}
	
	public void displayTitle(Integer fadeIn, Integer stay, Integer fadeOut, String title, String subtitle)
	{
		PlayerConnection connection = ((CraftPlayer)player).getHandle().playerConnection;

		PacketPlayOutTitle packetPlayOutTimes = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TIMES, null, fadeIn.intValue(), stay.intValue(), fadeOut.intValue());
		connection.sendPacket(packetPlayOutTimes);
		if (subtitle != null)
		{
			IChatBaseComponent titleSub = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + subtitle + "\"}");
			PacketPlayOutTitle packetPlayOutSubTitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, titleSub);
			connection.sendPacket(packetPlayOutSubTitle);
		}
		if (title != null){
			IChatBaseComponent titleSkyBlock = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + title + "\"}");
			PacketPlayOutTitle packetPlayOutTitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, titleSkyBlock);
			connection.sendPacket(packetPlayOutTitle);
		}
	}

	public void displayBar(String message)
	{
		try
		{
			Class<?> c1 = Class.forName("org.bukkit.craftbukkit." + SkyBlock.nmsver + ".entity.CraftPlayer");
			Object p = c1.cast(player);
			Object ppoc = null;
			Class<?> c4 = Class.forName("net.minecraft.server." + SkyBlock.nmsver + ".PacketPlayOutChat");
			Class<?> c5 = Class.forName("net.minecraft.server." + SkyBlock.nmsver + ".Packet");
			if ((SkyBlock.nmsver.equalsIgnoreCase("v1_8_R1")) || (!SkyBlock.nmsver.startsWith("v1_8_")))
			{
				Class<?> c2 = Class.forName("net.minecraft.server." + SkyBlock.nmsver + ".ChatSerializer");
				Class<?> c3 = Class.forName("net.minecraft.server." + SkyBlock.nmsver + ".IChatBaseComponent");
				Method m3 = c2.getDeclaredMethod("a", new Class[] { String.class });
				Object cbc = c3.cast(m3.invoke(c2, new Object[] { "{\"text\": \"" + message + "\"}" }));
				ppoc = c4.getConstructor(new Class[] { c3, Byte.TYPE }).newInstance(new Object[] { cbc, Byte.valueOf((byte) 2) });
			}
			else
			{
				Class<?> c2 = Class.forName("net.minecraft.server." + SkyBlock.nmsver + ".ChatComponentText");
				Class<?> c3 = Class.forName("net.minecraft.server." + SkyBlock.nmsver + ".IChatBaseComponent");
				Object o = c2.getConstructor(new Class[] { String.class }).newInstance(new Object[] { message });
				ppoc = c4.getConstructor(new Class[] { c3, Byte.TYPE }).newInstance(new Object[] { o, Byte.valueOf((byte) 2) });
			}
			Method m1 = c1.getDeclaredMethod("getHandle", new Class[0]);
			Object h = m1.invoke(p, new Object[0]);
			Field f1 = h.getClass().getDeclaredField("playerConnection");
			Object pc = f1.get(h);
			Method m5 = pc.getClass().getDeclaredMethod("sendPacket", new Class[] { c5 });
			m5.invoke(pc, new Object[] { ppoc });
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			SkyBlock.works = false;
		}
	}
}
