package nl.stijn.SkyBlock.Island.Commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import nl.stijn.SkyBlock.SkyBlock;
import nl.stijn.SkyBlock.Island.IslandManager;
import nl.stijn.SkyBlock.Utils.ScreenUtil;

public class TopIslands extends SubCommand {

	@Override
	public void onCommand(Player p, String[] args) {
		Set<Entry<UUID, Integer>> set = SkyBlock.points.entrySet();
		List<Entry<UUID, Integer>> list = new ArrayList<Entry<UUID, Integer>>(
				set);
		Collections.sort(list, new Comparator<Map.Entry<UUID, Integer>>() {

			@Override
			public int compare(Entry<UUID, Integer> o1,
					Entry<UUID, Integer> o2) {

				return o2.getValue().compareTo(o1.getValue());
			}

		});

		int max = SkyBlock.points.size();
		int number = 1;
		if(max < 10) {
			for(Entry<UUID, Integer> uuid : list.subList(0, max)) {
				p.sendMessage(ScreenUtil.prefix + number + ". " + Bukkit.getOfflinePlayer(IslandManager.getInstance().getIslandFromUUID(uuid.getKey()).getOwner()).getName() + " " + SkyBlock.points.get(uuid.getKey()) + " points.");
				number++;
			}
		} else {
			for(Entry<UUID, Integer> uuid : list.subList(0, 10)) {
				p.sendMessage(ScreenUtil.prefix + number + ". " + Bukkit.getOfflinePlayer(IslandManager.getInstance().getIslandFromUUID(uuid.getKey()).getOwner()).getName() + " " + SkyBlock.points.get(uuid.getKey()) + " points.");
				number++;
			}
		}
	}

	@Override
	public String name() {
		return "top";
	}

	@Override
	public String info() {
		return "The top 10 islands!";
	}

	@Override
	public String[] aliases() {
		return new String[] {"ti"};
	}

	@Override
	public boolean admin() {
		return false;
	}

}
