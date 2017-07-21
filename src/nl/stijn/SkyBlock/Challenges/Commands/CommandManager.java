package nl.stijn.SkyBlock.Challenges.Commands;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import nl.stijn.SkyBlock.Challenges.ChallengeBuilder;
import nl.stijn.SkyBlock.Island.IslandManager;
import nl.stijn.SkyBlock.Island.Commands.SubCommand;
import nl.stijn.SkyBlock.Utils.ScreenUtil;

public class CommandManager implements CommandExecutor {

	private ArrayList<SubCommand> commands = new ArrayList<SubCommand>();
	
	public void setup() {
		commands.add(new AddCat());
		commands.add(new AddCha());
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		
		if (!(sender instanceof Player)) {
			return true;
		}
		
		Player p = (Player) sender;
		
		if (cmd.getName().equalsIgnoreCase("challenge")) {
			if(IslandManager.getInstance().GetIsland(p) == null) {
				p.sendMessage(ScreenUtil.prefix + "Sorry, you don't have an island!");
				return true;
			}
			if (args.length == 0) {
				for (SubCommand c : commands) {
					if(c.admin()) {
						if(p.hasPermission("skyblock.admin.command")) {
							p.sendMessage(ScreenUtil.prefix + "/challenge " + c.name() + "(" + aliases(c) + ")" + " - " + c.info());
						} else {
							continue;
						}
					} else {
						p.sendMessage(ScreenUtil.prefix + "/challenge " + c.name() + "(" + aliases(c) + ")" + " - " + c.info());
					}

				}
			    ChallengeBuilder.openCats(p);
				return true;
			}
			
			SubCommand target = get(args[0]);
			
			if (target == null) {
				p.sendMessage(ScreenUtil.prefix + "/challenge " + args[0] + " is not a valid subcommand!");
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
	
	private String aliases(SubCommand cmd) {
		String fin = "";
		
		for (String a : cmd.aliases()) {
			fin += a + " | ";
		}
		
		return fin.substring(0, fin.lastIndexOf(" | "));
	}
	
	private SubCommand get(String name) {
		for (SubCommand cmd : commands) {
			if (cmd.name().equalsIgnoreCase(name)) return cmd;
			for (String alias : cmd.aliases()) if (name.equalsIgnoreCase(alias)) return cmd;
		}
		return null;
	}
}