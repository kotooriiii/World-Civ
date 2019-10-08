package com.worldciv.events.mob;

import com.worldciv.events.chat.ChatChannelEvent;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

public class DenySpawnEvent implements Listener {


    @EventHandler
    public void onSpawn(EntitySpawnEvent e) {
        Entity entity = e.getEntity();
        Location loc = e.getLocation();
        EntityType type = e.getEntityType();

        if (type.equals(EntityType.HORSE)) {
            if (entity.getName().contains("Wild Horse")) {


                if (ChatChannelEvent.isTownLand(entity)){
                    e.setCancelled(true);
                }
            }
        }
    }
}
