package com.worldciv.events.player;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class CancelEnderpearlEvent implements Listener {
    @EventHandler
    public void onEnderpearlToss(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        ItemStack is = e.getItem();
if (is == null){
    return;
}

if (is.getType() == null){
    return;
}


        if (is.getType().equals(Material.ENDER_PEARL)) {
            if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                e.setCancelled(true);
            }
        }
    }
}
