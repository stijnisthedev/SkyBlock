package nl.stijn.SkyBlock.Challenges.Commands;

import org.bukkit.entity.Player;

import nl.stijn.SkyBlock.Challenges.Category;
import nl.stijn.SkyBlock.Challenges.CategoryBuilder;
import nl.stijn.SkyBlock.Challenges.CategoryManager;
import nl.stijn.SkyBlock.Challenges.CategoryTuple;
import nl.stijn.SkyBlock.Island.Commands.SubCommand;
import nl.stijn.SkyBlock.Utils.ScreenUtil;

public class AddCat extends SubCommand {

	@Override
	public void onCommand(Player p, String[] args) {
		if(args.length == 0) {
			p.sendMessage(ScreenUtil.prefix + "Please specify a name!");
			return;
		}
		if(args.length == 1) {
			p.sendMessage(ScreenUtil.prefix + "Please specify a slot!");
			return;
		}
		if(!isInt(args[1])) {
			p.sendMessage(ScreenUtil.prefix + args[1] + " is not a valid number!");
			return;
		}
		int i = Integer.parseInt(args[1]);
		if(i > 25) {
			p.sendMessage(ScreenUtil.prefix + "Please specify a number under 25!");
			return;
		}
		
		Category c = CategoryManager.getInstance().getCategory(args[0]);
		if(c != null || CategoryBuilder.slot.containsKey(args[0])) {
			p.sendMessage(ScreenUtil.prefix + "Sorry, this category already exists!");
			return;
		}
		
		CategoryBuilder.openMenu(p, args[0]);
		CategoryBuilder.slot.put(p.getUniqueId(), new CategoryTuple(args[0], null, null, i));
		return;
	}
	
	public static boolean isInt(String i) {
		try {
			Integer.parseInt(i);
			return true;
		} catch(Exception e) {
			return false;
		}
	}

	@Override
	public String name() {
		return "addcat";
	}

	@Override
	public String info() {
		return  "Add a catagory!";
	}

	@Override
	public String[] aliases() {
		return new String[] {"ac"};
	}

	@Override
	public boolean admin() {
		return true;
	}

}
