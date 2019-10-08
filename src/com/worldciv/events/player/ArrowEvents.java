package com.worldciv.events.player;

import com.worldciv.filesystem.CustomItem;
import com.worldciv.the60th.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import java.awt.Event;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataStoreBase;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.metadata.Metadatable;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

public class ArrowEvents implements Listener {
    public static Plugin plugin = Main.plugin;
    public static final String ARROW_META_TAG = "arrowUUID";
    public static final String BOW_META_TAG = "bowUUID";
    public static final String FORCE_META_TAG = "eventForce";
    @EventHandler
    public void onEntityShootBowEvent(EntityShootBowEvent event){
        Entity entity =  event.getEntity();
        if(event.getProjectile() instanceof Arrow) {
            if(entity instanceof Player){
                Player p = (Player) entity;
                ItemStack arrowFired = getArrowStack(p);
                Arrow arrow = (Arrow)event.getProjectile();
                List<String> lore = event.getBow().getItemMeta().getLore();
                //Null check here.
                if(lore != null) {
                    if (CustomItem.unhideItemUUID(lore.get(lore.size() - 1)) != null && !CustomItem.unhideItemUUID(lore.get(lore.size() - 1)).isEmpty()) {
                        arrow.setMetadata(BOW_META_TAG, new FixedMetadataValue(plugin, CustomItem.unhideItemUUID(lore.get(lore.size() - 1))));
                    }
                }

                //Reuse the lore string so we don't have to create more data.
                lore = arrowFired.getItemMeta().getLore();

                if(lore != null) {
                    if (CustomItem.unhideItemUUID(lore.get(lore.size() - 1)) != null && !CustomItem.unhideItemUUID(lore.get(lore.size() - 1)).isEmpty()) {
                        arrow.setMetadata(ARROW_META_TAG, new FixedMetadataValue(plugin, CustomItem.unhideItemUUID(lore.get(lore.size() - 1))));
                    }
                }
                arrow.setMetadata(FORCE_META_TAG, new FixedMetadataValue(plugin,event.getForce())); // max = 1.0f

            }
        }
    }

    //Removed to use other type of event.
  /*  @EventHandler
    public void onProjectileHit(ProjectileHitEvent event){
        Projectile projectile = event.getEntity();
        if(projectile instanceof Arrow){
            Arrow arrow = (Arrow) projectile;
            if(projectile.hasMetadata(ARROW_META_TAG)){
                for(MetadataValue mdv : arrow.getMetadata(ARROW_META_TAG)){
                    System.out.println("Arrow lore: " + mdv.asString());

                }
            }
            if(projectile.hasMetadata(BOW_META_TAG)){
                for(MetadataValue mdv : arrow.getMetadata(BOW_META_TAG)){
                    System.out.println("Bow Lore: " + mdv.asString());
                }
            }
        }

    }*/
        //Moved to attackEvent
/*
    @EventHandler
    public void changeArrowDamage(EntityDamageByEntityEvent event){
        CustomItem bow;
        CustomItem arrow;
        System.out.println("ChangeArrowDamage");
        Entity entity = event.getEntity();
        if(entity instanceof Player){
            Player pHit = (Player) entity;
            if(event.getCause() == DamageCause.PROJECTILE){
                if(event.getDamager() instanceof Arrow){
                    Arrow eArrow = (Arrow) event.getDamager();
                    if(eArrow.hasMetadata("ArrowMeta")){
                        for(MetadataValue mdv : eArrow.getMetadata("ArrowMeta")){
                            System.out.println("Arrow lore: " + mdv.asString());
                            //Create a custom item from UUID.
                            arrow = CustomItem.getCustomItemFromUUID(mdv.asString());
                        }
                    }
                    if(eArrow.hasMetadata("BowMeta")){
                        for(MetadataValue mdv : eArrow.getMetadata("BowMeta")){
                            System.out.println("Bow Lore: " + mdv.asString());
                            bow = CustomItem.getCustomItemFromUUID(mdv.asString());
                        }
                    }
                    //Damage calculations here
                    //What?
                    Bukkit.broadcastMessage("Demo display:");
                }
            }
        }
    }
*/


    ItemStack getArrowStack(Player player){//If getEntity() in EntityShootBowEvent is a player, you can cast it and pass it to this method
        if(player.getInventory().getItemInOffHand().getType() == Material.ARROW){
            return player.getInventory().getItemInOffHand();
        }
        for(ItemStack stack : player.getInventory().getContents()){ //Now that you have a player object to work with, you can loop through their items
            if(stack != null && stack.getType()==Material.ARROW){ //Empty slots will be null, so we need to check that first. Then we can check if the item is an arrow
                return stack; //The arrow has been found, so the ItemStack can be returned
            }
        }
        return null; //If the player had no arrows, return null.
    }
}
