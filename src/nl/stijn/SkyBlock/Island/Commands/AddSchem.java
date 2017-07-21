package nl.stijn.SkyBlock.Island.Commands;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.entity.Player;

import nl.stijn.SkyBlock.Island.Schematics.SchematicBuilder;
import nl.stijn.SkyBlock.Island.Schematics.SchematicManager;
import nl.stijn.SkyBlock.Utils.ScreenUtil;

public class AddSchem extends SubCommand {

	public static ArrayList<UUID> chat = new ArrayList<UUID>();
	
	@Override
	public void onCommand(Player p, String[] args) {
		if(args.length == 0) {
			p.sendMessage(ScreenUtil.prefix + "Sorry, please specify a name!");
			return;
		}
		if(args.length == 1) {
			p.sendMessage(ScreenUtil.prefix + "Sorry, please specify a schematic!");
			return;
		}
		if(SchematicManager.getInstance().getSchematic(args[0]) != null) {
			p.sendMessage(ScreenUtil.prefix + "Sorry, this schematic does already exist!");
			return;
		}
		if(SchematicManager.getInstance().exist(args[1]) == false) {
			p.sendMessage(ScreenUtil.prefix + "Sorry, this schematic is not in your folder!");
			return;
		}
		chat.add(p.getUniqueId());
		SchematicBuilder.name.put(p.getUniqueId(), args[0]);
		SchematicBuilder.file.put(p.getUniqueId(), args[1]);
		p.sendMessage(ScreenUtil.prefix + "Now type the description!");
		return;
		
	}

	@Override
	public String name() {
		return "addschematic";
	}

	@Override
	public String info() {
		return "Add a schematic.";
	}

	@Override
	public String[] aliases() {
		return new String[] {"as"};
	}

	@Override
	public boolean admin() {
		return true;
	}

}
