package nl.stijn.SkyBlock.Island.Commands;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import nl.stijn.SkyBlock.Island.Island;
import nl.stijn.SkyBlock.Island.IslandManager;
import nl.stijn.SkyBlock.Island.GUI.StartGUI;
import nl.stijn.SkyBlock.Utils.ScreenUtil;

public class CommandManager implements CommandExecutor {

	public static ArrayList<SubCommand> commands = new ArrayList<SubCommand>();
	
	public void setup() {
		commands.add(new SetWorld());
		commands.add(new Create());
		commands.add(new Remove());
		commands.add(new Home());
		commands.add(new Invite());
		commands.add(new Accept());
		commands.add(new Leave());
		commands.add(new SetHome());
		commands.add(new Deny());
		commands.add(new Points());
		commands.add(new TopIslands());
		commands.add(new AddSchem());
		commands.add(new SetBiome());
		commands.add(new Info());
		commands.add(new Shop());
		commands.add(new SetShop());
		commands.add(new Trusted());
		commands.add(new Kick());
		commands.add(new Resets());
		commands.add(new Help());
		commands.add(new Scoreboard());
		commands.add(new CommandTuple());
		
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (!(sender instanceof Player)) {
			return true;
		}
		
		Player p = (Player) sender;
		
		if (cmd.getName().equalsIgnoreCase("island")) {
			if (args.length == 0) {
				
				Island is = IslandManager.getInstance().GetIsland(p);
				if(is == null) {
					p.performCommand("is c");
					return true;
				}
				if(is.getOwner().equals(p.getUniqueId())) {
					StartGUI.openMenuOwner(is, p);
					p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, 1, 1);
				} else {
					StartGUI.openMenuTrusted(is, p);
					p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, 1, 1);
				}

				return true;
			}
			
			SubCommand target = get(args[0]);
			
			if (target == null) {
				p.sendMessage(ScreenUtil.prefix + "/island " + args[0] + " is not a valid subcommand!");
				return true;
			}
			
			ArrayList<String> a = new ArrayList<String>();
			a.addAll(Arrays.asList(args));
			a.remove(0);
			args = a.toArray(new String[a.size()]);
			
			try {
				target.onCommand(p, args);
			}
			
			catch (Exception e) {
				p.sendMessage(ScreenUtil.prefix + "An error has occured: " + e.getCause());
				e.printStackTrace();
			}
		}
		
		return true;
	}
	
	public static String aliases(SubCommand cmd) {
		String fin = "";
		
		for (String a : cmd.aliases()) {
			fin += a + " | ";
		}
		
		return fin.substring(0, fin.lastIndexOf(" | "));
	}
	
	public static SubCommand get(String name) {
		for (SubCommand cmd : commands) {
			if (cmd.name().equalsIgnoreCase(name)) return cmd;
			for (String alias : cmd.aliases()) if (name.equalsIgnoreCase(alias)) return cmd;
		}
		return null;
	}
}