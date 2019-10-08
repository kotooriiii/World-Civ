package com.worldciv.events.player;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class TorchBlockPlaceFixEvent implements Listener {

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e){
        Player p = e.getPlayer();
        Block blockplaced = e.getBlock();

        if (blockplaced.getType().equals(Material.TORCH)) { //The block being placed is a torch

            if (!p.getInventory().getItemInMainHand().getType().equals(Material.TORCH) && p.getInventory().getItemInMainHand().getType().isBlock()) { //Main hand not holding torch y AND if its a block

                if (p.getInventory().getItemInMainHand() == null || p.getInventory().getItemInMainHand().getType().equals(Material.AIR)) {

                } else {
                e.setCancelled(true);
                }
            }
        }
    }

}
