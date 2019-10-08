package com.worldciv.events.mob;

import com.worldciv.events.player.ArrowEvents;
import com.worldciv.filesystem.CustomItem;
import org.bukkit.Bukkit;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.metadata.MetadataValue;

import static com.worldciv.events.player.PlayerAttackEvents.*;

public class playerAttack implements Listener {

    @EventHandler
    public void onEntityDamageEvent(EntityDamageByEntityEvent event) {
        float customDamage;
        Entity eDefender = event.getEntity();
        Entity attacker = event.getDamager(); //Attacker
        Entity defender = event.getEntity(); //Defender
        //if(!(attacker instanceof Player) || defender instanceof Player) return;
        if(defender instanceof Player) return;
        if(attacker instanceof Monster) return;
        if (event.getCause() == EntityDamageEvent.DamageCause.PROJECTILE) {
            //Do arrow stuff here.
            //System.out.println("ChangeArrowDamageMob");
            CustomItem[] customItems = new CustomItem[2];
            double damage;
            Arrow eArrow = (Arrow) event.getDamager();
            if (eArrow.hasMetadata(ArrowEvents.ARROW_META_TAG)) {
                for (MetadataValue mdv : eArrow.getMetadata(ArrowEvents.ARROW_META_TAG)) {
                   // System.out.println("Arrow lore: " + mdv.asString());
                    //Create a custom item from UUID.
                    customItems[0] = CustomItem.getCustomItemFromUUID(mdv.asString().substring(7));
                }
            }
            if (eArrow.hasMetadata(ArrowEvents.BOW_META_TAG)) {
                for (MetadataValue mdv : eArrow.getMetadata(ArrowEvents.BOW_META_TAG)) {
                  //  System.out.println("Bow Lore: " + mdv.asString());
                    customItems[1] = CustomItem.getCustomItemFromUUID(mdv.asString().substring(7));
                }
            }

            customDamage = getDamageFromArray(customItems);
            customDamage = customDamage *(float) getDamageScalerBow(event.getDamage());
        }
        else{
            Player pAttacker = (Player) attacker;
            eDefender = defender;
            pAttacker = (Player) attacker;

            customDamage = (float)event.getDamage();
            float damageScaler = 1;
            customDamage = getDamageFromArray(getDamageItems(pAttacker));
            customDamage = (float) (customDamage * getHorseAttackModifer(pAttacker, eDefender));
            damageScaler = (float) getDamageScale(pAttacker, event.getDamage());
            customDamage = customDamage * damageScaler;
        }
        //Bukkit.broadcastMessage("Damage: " + customDamage);
        //The attacker is now a player
        //and the defender is not a player.

        if(customDamage <= 1) customDamage = (float) event.getDamage();
        
        if(eDefender instanceof LivingEntity ){
            LivingEntity mob = (LivingEntity) eDefender;
            //Bukkit.broadcastMessage("Old health: " + mob.getHealth() + " new health " + (mob.getHealth()-customDamage));
            event.setDamage(customDamage);
            //Bukkit.broadcastMessage("did damage to entity");
        }else{
          // Bukkit.broadcastMessage("Do something else.");
        }

    }

}