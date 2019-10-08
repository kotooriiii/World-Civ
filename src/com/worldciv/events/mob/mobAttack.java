package com.worldciv.events.mob;

import com.worldciv.the60th.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.HashMap;

import static com.worldciv.events.player.PlayerAttackEvents.defenderArmorTracker;
import static com.worldciv.events.player.PlayerAttackEvents.getArmorFromArray;
import static com.worldciv.events.player.PlayerAttackEvents.getArmorItems;
import static com.worldciv.the60th.Main.getDungeonManager;

public class mobAttack  implements Listener {
    @EventHandler
    public void onEntityDamageEvent(EntityDamageByEntityEvent event) {
        //pDefender.isBlocking(); //Returns when blocking? 6 tick delay?? //Is blocking == 100 of SHIELD armor.
        //pDefender.isHandRaised(); //Returns when they are about to block. //IsRaised == 50% of SHIELD armor.

        Entity attacker = event.getDamager(); //Attacker
        Entity defender = event.getEntity(); //Defender

        if((defender instanceof Player) && (attacker instanceof Player)) return;
        if(!(defender instanceof Player)) return;
        Player pDefender = (Player) defender;
        double armor = getArmorFromArray(getArmorItems(pDefender));
        double damage = event.getDamage();
        double damagePostArmor = damage - armor;
        if (damagePostArmor <= 0) {
           // Bukkit.broadcastMessage("damage did not pass armor,");
            if (defenderArmorTracker.containsKey(pDefender)) {
                //Bukkit.broadcastMessage("contains If -> armor" + armor + " defenderStored damage " + defenderArmorTracker.get(pDefender));
                damagePostArmor =(damage + defenderArmorTracker.get(pDefender)) - armor ;
                if(damagePostArmor >= 6) damagePostArmor = 6;
                if (damagePostArmor < 0){
                    event.setDamage(0);
                    double storedDamage = defenderArmorTracker.get(pDefender);
                    defenderArmorTracker.put(pDefender,storedDamage+damage);
                    return;
                }
                if ((pDefender.getHealth() - damagePostArmor) < 0) {
                    if (getDungeonManager.getDungeon(pDefender) != null) {
                        pDefender.setHealth(pDefender.getMaxHealth());
                        Location location = Main.fileSystem.getPlayerSpawn(getDungeonManager.getDungeon(pDefender).getDungeonID()); //Location is dungeon respawn point.
                        pDefender.teleport(location);
                    }
                    //Lets the player die normally.
                    event.setDamage(999); //Make sure the player dies
                    defenderArmorTracker.remove(pDefender);
                    return;
                }else
                {
                    if (getDungeonManager.getDungeon(pDefender) != null) {
                        pDefender.setHealth(pDefender.getMaxHealth());
                        Location location = Main.fileSystem.getPlayerSpawn(getDungeonManager.getDungeon(pDefender).getDungeonID()); //Location is dungeon respawn point.
                        pDefender.teleport(location);
                    }
                    pDefender.setHealth(pDefender.getHealth() - (damagePostArmor));
                    event.setDamage(0);
                    defenderArmorTracker.remove(pDefender);

                }
            } else {
                defenderArmorTracker.put(pDefender, damage);
                //Damage did not overflow armor values.
                //Start tracking damage.
                event.setDamage(0);
            }


        } else {
            if(damagePostArmor >= 6) damagePostArmor = 6;
            if (pDefender.getHealth() - damagePostArmor < 0) {
                //Lets the player die normally.
                if (getDungeonManager.getDungeon(pDefender) != null) {
                    pDefender.setHealth(pDefender.getMaxHealth());
                    Location location = Main.fileSystem.getPlayerSpawn(getDungeonManager.getDungeon(pDefender).getDungeonID()); //Location is dungeon respawn point.
                    pDefender.teleport(location);
                }
                event.setDamage(999); //Make sure the player dies
            } else {
                try {
                    if (getDungeonManager.getDungeon(pDefender) != null) {
                        pDefender.setHealth(pDefender.getMaxHealth());
                        Location location = Main.fileSystem.getPlayerSpawn(getDungeonManager.getDungeon(pDefender).getDungeonID()); //Location is dungeon respawn point.
                        pDefender.teleport(location);
                    }
                    pDefender.setHealth(pDefender.getHealth() - (damagePostArmor)); //Cant set neg major bug
                    event.setDamage(0);
                } catch (IllegalArgumentException e) {
                    if (getDungeonManager.getDungeon(pDefender) != null) {
                        pDefender.setHealth(pDefender.getMaxHealth());
                        Location location = Main.fileSystem.getPlayerSpawn(getDungeonManager.getDungeon(pDefender).getDungeonID()); //Location is dungeon respawn point.
                        pDefender.teleport(location);
                    }
                    event.setDamage(999);
                    //Catch the negative bug we were running into and let the player die normally.
                }
            }
        }


    }}