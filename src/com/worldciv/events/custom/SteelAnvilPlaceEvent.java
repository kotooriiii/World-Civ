package com.worldciv.events.custom;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class SteelAnvilPlaceEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    Player p;
    Block b;
    boolean cancelled;

    public SteelAnvilPlaceEvent(Player p, Block b){
        this.b = b;
        this.p = p;
    }

    public Player getPlayer() {
        return p;
    }

    public Block getBlock(){
        return b;
    }

    public boolean isCancelled(){
        return cancelled;
    }

    public void setCancelled(boolean cancel){
        cancelled = cancel;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList(){
        return handlers;
    }

}
