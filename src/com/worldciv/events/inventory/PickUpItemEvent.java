package com.worldciv.events.inventory;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class PickUpItemEvent implements Listener {

    @EventHandler
    public void  onPickUp(PlayerPickupItemEvent e) {

        Player p = e.getPlayer();

        Item item = e.getItem();
        ItemStack is = item.getItemStack();
        ItemMeta im = is.getItemMeta();

        List<String> lore = im.getLore();

        if (is.getType() == Material.TORCH) {

            if(lore == null){
                return;
            }

            if (!lore.get(1).equalsIgnoreCase(ChatColor.AQUA + p.getName())) {
                e.setCancelled(true);
            }
        }
    }
}
