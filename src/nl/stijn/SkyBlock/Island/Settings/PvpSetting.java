package nl.stijn.SkyBlock.Island.Settings;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import nl.stijn.SkyBlock.Island.Island;
import nl.stijn.SkyBlock.Island.IslandManager;

public class PvpSetting implements Listener {
	
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e) {
		if(e.getDamager() == null) {
			return;
		}
		if(e.getEntity() == null) {
			return;
		}
		if(e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			Island is = IslandManager.getInstance().getIslandOfPlayer(p);
			if(is == null) {
				return;
			}
			
			if(e.getDamager() instanceof Player) {
				boolean can = is.getPvP();
				can = !can;
					e.setCancelled(can);
			}
			if(e.getDamager() instanceof Snowball) {
				Snowball sn = (Snowball) e.getDamager();
				if(!(sn.getShooter() instanceof Player)) {
					return;
				}
				Island i = IslandManager.getInstance().getIslandOfLocation(sn.getLocation());
				boolean can = i.getPvP();
				can = !can;
				e.setCancelled(can);
				return;
			}
			if(e.getDamager() instanceof Egg) {
				Egg sn = (Egg) e.getDamager();
				if(!(sn.getShooter() instanceof Player)) {
					return;
				}
				Island i = IslandManager.getInstance().getIslandOfLocation(sn.getLocation());
				boolean can = i.getPvP();
				can = !can;
				e.setCancelled(can);
				return;
			}
			if(e.getDamager() instanceof Arrow) {
				Arrow sn = (Arrow) e.getDamager();
				if(!(sn.getShooter() instanceof Player)) {
					return;
				}
				Island i = IslandManager.getInstance().getIslandOfLocation(sn.getLocation());
				boolean can = i.getPvP();
				can = !can;
				e.setCancelled(can);
				return;
			}
			
		}

	}

}
