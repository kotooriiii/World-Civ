package com.worldciv.events.inventory;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

import static com.worldciv.utility.utilityArrays.lighttutorial;

public class DropItemEvent implements Listener {

    @EventHandler
    public void onDrop(PlayerDropItemEvent e){
        if(lighttutorial.contains(e.getPlayer())){
            e.setCancelled(true);
        }
    }
}
