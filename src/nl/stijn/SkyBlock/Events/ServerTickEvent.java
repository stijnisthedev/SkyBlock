package nl.stijn.SkyBlock.Events;

import org.bukkit.event.HandlerList;

public class ServerTickEvent {

	private static final HandlerList handlers = new HandlerList();

	public ServerTickEvent() {
		
	}
	
	public HandlerList getHandlers() {
	    return handlers;
	}

	public static HandlerList getHandlerList() {
	    return handlers;
	}
	
}
