package com.worldciv.events.custom;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class SteelAnvilBreakEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
  Block block;
    Player p;
 boolean cancelled;

    public SteelAnvilBreakEvent(Player p, Block b){
        this.p = p;
        block = b;
    }

    public SteelAnvilBreakEvent(Block b){
        block = b;
    }

    public Player getPlayer(){
        return this.p;
    }

    public Block getBlock(){
        return block;
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
