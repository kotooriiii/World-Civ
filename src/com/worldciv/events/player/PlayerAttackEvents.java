package com.worldciv.events.player;

import com.worldciv.filesystem.CustomItem;
import com.worldciv.the60th.Main;
import com.worldciv.utility.ExampleSelfCancelingTask;
import com.worldciv.utility.ItemType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.List;

import static com.worldciv.the60th.Main.getDungeonManager;

//Based off of old new combat stats plugin
public class PlayerAttackEvents implements Listener {
    private static final int PLAYER_DEFAULT_ARMOR = 1;
    public static HashMap<Player, Double> defenderArmorTracker = new HashMap<>();

    @EventHandler
    public void onEntityDamageEvent(EntityDamageByEntityEvent event) {
        //pDefender.isBlocking(); //Returns when blocking? 6 tick delay?? //Is blocking == 100 of SHIELD armor.
        //pDefender.isHandRaised(); //Returns when they are about to block. //IsRaised == 50% of SHIELD armor.

        Entity attacker = event.getDamager(); //Attacker
        Entity defender = event.getEntity(); //Defender
        // if (!(attacker instanceof Player) || !(defender instanceof Player) || !(event.getDamager() instanceof  Arrow) ) {
        //Not a PvP action, handle this elsewhere --> Write function to handle this with two Entity as params
        //  return;
        // }
        if (!(defender instanceof Player)) return;
        double customDamage;
        double damageScaler = 1;
        Player pAttacker = null;
        Player pDefender = (Player) defender;
        //Do what do melee calculations or ranged calculations.
        //Find out here.
        //TODO Create a damage scaler function for ranged damage.
        if (event.getCause() == EntityDamageEvent.DamageCause.PROJECTILE) {
            //Do arrow stuff here.
           // System.out.println("ChangeArrowDamage");
            CustomItem[] customItems = new CustomItem[2];
            double damage;
            Arrow eArrow = (Arrow) event.getDamager();
            if (eArrow.hasMetadata(ArrowEvents.ARROW_META_TAG)) {
                for (MetadataValue mdv : eArrow.getMetadata(ArrowEvents.ARROW_META_TAG)) {
                    //System.out.println("Arrow lore: " + mdv.asString());
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
            customDamage = customDamage * getDamageScalerBow(event.getDamage());
        } else {
            if(!(attacker instanceof  Player)) return;
            pAttacker = (Player) attacker;
            customDamage = getDamageFromArray(getDamageItems(pAttacker));
            customDamage = customDamage * getHorseAttackModifer(pAttacker, pDefender);
            damageScaler = getDamageScale(pAttacker, event.getDamage());
            customDamage = customDamage * damageScaler;
            if (customDamage == 0) {

            }
        }
        //TODO Refactor this shit
        //@Legendary look at this wonderful code.
        //All dem doubles. Hawt af
        double armor = getArmorFromArray(getArmorItems(pDefender));
        double rawdamage = event.getDamage();
        boolean isBlocking = pDefender.isBlocking(); //isBlocking() or isHandRaised
        boolean isHandRaised = pDefender.isHandRaised();
        double armorFromItems = getPlayerArmorFromHeldItems(pDefender);

        if (isBlocking) {
            //Player is blocking give full armor.
            armor = armor + armorFromItems;
        } else if (isHandRaised) {
            //Player is about to block give part armor.
            armor = armor + (int) (armorFromItems / 2);
        } else {
            //No block no armor.
            armor = armor;
        }
        double damagePostArmor = customDamage - armor;

        if (damagePostArmor <= 0) {
           // Bukkit.broadcastMessage("damage did not pass armor,");
            if (defenderArmorTracker.containsKey(pDefender)) {
               // Bukkit.broadcastMessage("contains If -> armor" + armor + " defenderStored damage " + defenderArmorTracker.get(pDefender));
                damagePostArmor =(customDamage + defenderArmorTracker.get(pDefender)) - armor ;
                if(damagePostArmor >= 6) damagePostArmor = 6;
                if (damagePostArmor < 0){
                    event.setDamage(0);
                    double storedDamage = defenderArmorTracker.get(pDefender);
                    defenderArmorTracker.put(pDefender,storedDamage+customDamage);
                    return;
                }
                else if (pDefender.getHealth() - damagePostArmor < 0) {
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
                defenderArmorTracker.put(pDefender, customDamage);
                //Damage did not overflow armor values.
                //Start tracking damage.
                event.setDamage(0);
            }


        } else {
            if(damagePostArmor >= 6) damagePostArmor = 6;
            if (pDefender.getHealth() - damagePostArmor < 0) {
                if (getDungeonManager.getDungeon(pDefender) != null) {
                    pDefender.setHealth(pDefender.getMaxHealth());
                    Location location = Main.fileSystem.getPlayerSpawn(getDungeonManager.getDungeon(pDefender).getDungeonID()); //Location is dungeon respawn point.
                    pDefender.teleport(location);
                }
                //Lets the player die normally.
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
        try {
            Main.logger.info(pAttacker.getDisplayName() + " just attacked " + pDefender.getDisplayName() + " dealing " + customDamage
                    + " pre armor and scale " + getDamageScale((Player) attacker, event.getDamage()) + " damage after scale " + damagePostArmor + " damage after armor. This leaves the defender at "
                    + (pDefender.getHealth()) + " health. The raw damage of the attack was " + rawdamage + " the scale was " + damageScaler +
                    "\n ****************** " + "\n");
        } catch (NullPointerException e) {

        }
    }


    public static CustomItem[] getArmorItems(Player player) {
        ItemStack helm;
        ItemStack Chestplate;
        ItemStack Legs;
        ItemStack boots;
        List<String> lore;
        CustomItem[] customItems = new CustomItem[4];
        try {
            if (player.getInventory().getHelmet() != null) {
                helm = player.getInventory().getHelmet();
                // Bukkit.broadcastMessage("Passed not null 1");
                if (helm.getItemMeta().getLore() != null) {
                    lore = helm.getItemMeta().getLore();
                    // Bukkit.broadcastMessage("Passed not null 2");
                    if (CustomItem.unhideItemUUID(lore.get(lore.size() - 1)).startsWith("7UUID: ")) {
                        //   Bukkit.broadcastMessage("Passed not null 3");
                        //   Bukkit.broadcastMessage(lore.get(lore.size()-1).substring(7));
                        customItems[0] = CustomItem.getCustomItemFromUUID(CustomItem.unhideItemUUID(lore.get(lore.size() - 1)).substring(7));
                        //Bukkit.broadcastMessage(customItems[0].getArmor() + "0");
                    }
                }
            }
        } catch (NullPointerException e) {
        }
        try {
            if (player.getInventory().getChestplate() != null) {
                // Bukkit.broadcastMessage("Passed not null 4");
                Chestplate = player.getInventory().getChestplate();
                if (Chestplate.getItemMeta().getLore() != null) {
                    //  Bukkit.broadcastMessage("Passed not null 5");
                    lore = Chestplate.getItemMeta().getLore();
                    if (CustomItem.unhideItemUUID(lore.get(lore.size() - 1)).startsWith("7UUID: ")) {
                        // Bukkit.broadcastMessage("Passed not null 6");
                        customItems[1] = CustomItem.getCustomItemFromUUID(CustomItem.unhideItemUUID(lore.get(lore.size() - 1)).substring(7));
                        // Bukkit.broadcastMessage(customItems[1].getArmor() + "1");
                    }
                }
            }
        } catch (NullPointerException e) {
        }
        try {
            if (player.getInventory().getLeggings() != null) {
                //  Bukkit.broadcastMessage("Passed not null 7");
                Legs = player.getInventory().getLeggings();
                if (Legs.getItemMeta().getLore() != null) {
                    //  Bukkit.broadcastMessage("Passed not null 8");
                    lore = Legs.getItemMeta().getLore();
                    if (CustomItem.unhideItemUUID(lore.get(lore.size() - 1)).startsWith("7UUID: ")) {
                        //   Bukkit.broadcastMessage("Passed not null 9");
                        customItems[2] = CustomItem.getCustomItemFromUUID(CustomItem.unhideItemUUID(lore.get(lore.size() - 1)).substring(7));
                        //Bukkit.broadcastMessage(customItems[2].getArmor() + "2");
                    }
                }
            }
        } catch (NullPointerException e) {
        }
        try {
            if (player.getInventory().getBoots() != null) {
                //  Bukkit.broadcastMessage("Passed not null 10");
                boots = player.getInventory().getBoots();
                if (boots.getItemMeta().getLore() != null) {
                    //   Bukkit.broadcastMessage("Passed not null 11");
                    lore = boots.getItemMeta().getLore();
                    if (CustomItem.unhideItemUUID(lore.get(lore.size() - 1)).startsWith("7UUID: ")) {
                        //    Bukkit.broadcastMessage("Passed not null 12");
                        customItems[3] = CustomItem.getCustomItemFromUUID(CustomItem.unhideItemUUID(lore.get(lore.size() - 1)).substring(7));
                        //Bukkit.broadcastMessage(customItems[3].getArmor() + "3");
                    }
                }
            }
        } catch (NullPointerException e) {
        }
        return customItems;
    }

    public static int getArmorFromArray(CustomItem[] customItems) {
        int armor = PLAYER_DEFAULT_ARMOR;
        for (int i = 0; i < customItems.length; i++) {
            // if(customItems[i] != null) {
            //Bukkit.broadcastMessage("getARFA: " +i +":" + customItems[i].getArmor());
            try {
                armor = armor + customItems[i].getArmor();
            } catch (NullPointerException e) {

            }
            //}
        }
        // Bukkit.broadcastMessage("armor" + armor);
        return armor;
    }

    //This method is only written to handle shields as of now.
    //This will be updated to getPlayerArmorFromOffHandSlot.
    //In the end this method will also be used for getPlayerArmorFromNonArmorSources
    public static int getPlayerArmorFromHeldItems(Player player) {
        ItemStack mainHand;
        ItemStack offHand;
        List<String> lore;
        int armor = 0;
        //Off hand SHIELD.
        try {
            if (player.getInventory().getItemInOffHand() != null) {
                offHand = player.getInventory().getLeggings();
                if (offHand.getItemMeta().getLore() != null) {
                    lore = offHand.getItemMeta().getLore();
                    if (CustomItem.unhideItemUUID(lore.get(lore.size() - 1)).startsWith("7UUID: ")) {
                        if (CustomItem.getCustomItemFromUUID(CustomItem.unhideItemUUID(lore.get(lore.size() - 1)).substring(7)).getArmor() > 0) {
                            armor = armor + CustomItem.getCustomItemFromUUID(CustomItem.unhideItemUUID(lore.get(lore.size() - 1)).substring(7)).getArmor();
                        } else {
                            armor = armor;
                        }
                    }
                }
            }
        } catch (NullPointerException e) {
        }

        //Main hand SHIELD
        try {
            if (player.getInventory().getItemInOffHand() != null) {
                mainHand = player.getInventory().getLeggings();
                if (mainHand.getItemMeta().getLore() != null) {
                    lore = mainHand.getItemMeta().getLore();
                    if (CustomItem.unhideItemUUID(lore.get(lore.size() - 1)).startsWith("7UUID: ")) {
                        if (CustomItem.getCustomItemFromUUID(CustomItem.unhideItemUUID(lore.get(lore.size() - 1)).substring(7)).getArmor() > 0) {
                            armor = armor + CustomItem.getCustomItemFromUUID(CustomItem.unhideItemUUID(lore.get(lore.size() - 1)).substring(7)).getArmor();
                        } else {
                            armor = armor;
                        }
                    }
                }
            }
        } catch (NullPointerException e) {
        }
        return armor;
    }

    public static CustomItem[] getDamageItems(Player player) {
        CustomItem[] customItems = new CustomItem[2];
        List<String> lore;
        ItemStack mainHand;
        ItemStack offHand;
        ItemStack other;
        try {
            if (player.getInventory().getItemInMainHand() != null) {
                mainHand = player.getInventory().getItemInMainHand();
                if (mainHand.getItemMeta().getLore() != null) {
                    lore = mainHand.getItemMeta().getLore();
                    if (CustomItem.unhideItemUUID(lore.get(lore.size() - 1)).startsWith("7UUID: ")) {
                        customItems[0] = CustomItem.getCustomItemFromUUID(CustomItem.unhideItemUUID(lore.get(lore.size() - 1)).substring(7));
                    }
                }
            }
        } catch (NullPointerException e) {
        }
        try {
            if (player.getInventory().getItemInOffHand() != null) {
                offHand = player.getInventory().getItemInOffHand();
                if (offHand.getItemMeta().getLore() != null) {
                    lore = offHand.getItemMeta().getLore();
                    if (CustomItem.unhideItemUUID(lore.get(lore.size() - 1)).startsWith("7UUID: ")) {
                        customItems[1] = CustomItem.getCustomItemFromUUID(CustomItem.unhideItemUUID(lore.get(lore.size() - 1)).substring(7));
                    }
                }
            }
        } catch (NullPointerException e) {
        }
        return customItems;
    }

    public static int getDamageFromArray(CustomItem[] customItems) {
        //TODO         //Need more duel wielding checks for pikes and lances.
        int damage = 0;
        //for (CustomItem customItem : customItems) {
        // damage = damage + customItem.getDamage();
        for (int i = 0; i < customItems.length; i++) {
            //  Bukkit.broadcastMessage("getDmgFA:2 " +i +":" + customItems[0].getDamage());
            try {
                //Do not give off hand damage to pikes or lances!
                if (i == 0) {
                    //Main hand
                    damage = damage + customItems[i].getDamage();
                } else if (i == 1) {
                    //Off hand
                    if (customItems[1].getItemType() == ItemType.PIKE || customItems[1].getItemType() == ItemType.LANCE) {
                        damage = damage;
                    } else {
                        damage = damage + customItems[i].getDamage();
                    }
                }
            } catch (NullPointerException e) {
                //    System.out.println(e);
            }
        }
        return damage;
    }

    public static double getDamageScale(Player player, Double eventDamage) {
        ItemStack item = player.getInventory().getItemInMainHand();
        Material itemM = item.getType();
        double damage;
        if (itemM == Material.WOOD_SWORD) {
            damage = 4;
        } else if (itemM == Material.STONE_SWORD) {
            damage = 5;
        } else if (itemM == Material.IRON_SWORD) {
            damage = 6;
        } else if (itemM == Material.DIAMOND_SWORD) {
            damage = 7;
        } else if (itemM == Material.GOLD_SWORD) {
            damage = 4;
        } else if (itemM == Material.WOOD_SPADE) {
            damage = 2;
        } else if (itemM == Material.STONE_SPADE) {
            damage = 3;
        } else if (itemM == Material.IRON_SPADE) {
            damage = 4;
        } else if (itemM == Material.DIAMOND_SPADE) {
            damage = 5;
        } else if (itemM == Material.GOLD_SPADE) {
            damage = 2;
        } else if (itemM == Material.WOOD_PICKAXE) {
            damage = 2;
        } else if (itemM == Material.STONE_PICKAXE) {
            damage = 3;
        } else if (itemM == Material.IRON_PICKAXE) {
            damage = 4;
        } else if (itemM == Material.DIAMOND_PICKAXE) {
            damage = 5;
        } else if (itemM == Material.GOLD_PICKAXE) {
            damage = 2;
        } else if (itemM == Material.WOOD_AXE) {
            damage = 7;
        } else if (itemM == Material.STONE_AXE) {
            damage = 9;
        } else if (itemM == Material.IRON_AXE) {
            damage = 9;
        } else if (itemM == Material.DIAMOND_AXE) {
            damage = 9;
        } else if (itemM == Material.GOLD_AXE) {
            damage = 7;
        } else {
            damage = 1;
        }
        // Bukkit.broadcastMessage("Should of dealt: " + damage + " Did deal: " + eventDamage);
        damage = eventDamage / damage;
        // Bukkit.broadcastMessage("Diff mod is " + damage);
        return damage;
    }

    public static double getDamageScalerBow(Double damage) {
        final double bowDamage = 9.5;
       // System.out.println("Bow scale factor: " + (damage / bowDamage));
        return damage / bowDamage;
    }

    public static double getHorseAttackModifer(Player attacker, Entity defender) {
        //Need more duel wielding checks for pikes and lances.
        CustomItem customItemAttacker[] = getDamageItems(attacker);
        double damageModifer = 1;
        if (attacker.isInsideVehicle()) {
            Entity entity = attacker.getVehicle();
            if (entity instanceof Horse) {
                if (customItemAttacker[0].getItemType() == ItemType.LANCE) {
                    damageModifer = 2.0d;
                    //Do lance calcs
                } //Get custom item from mainhand here.
                else {
                    //Not using a lance return raw horse value.
                    damageModifer = 1.75d;
                }
            }
            //Attacker lance damage.
        } else if (defender.isInsideVehicle() && !attacker.isInsideVehicle()) {
            //Attacker pike damage
            if (customItemAttacker[0].getItemType() == ItemType.PIKE) {
                damageModifer = 2.0;
            }
        }
        //Bukkit.broadcastMessage("Returning horse scale of: " + damageModifer);
        return damageModifer;
    }


    private void damageTracker() {


    }


}












       /* new BukkitRunnable(){public void run(){
            for(Player player : Bukkit.getOnlinePlayers()){
                EntityPlayer ep = ((CraftPlayer)player).getHandle();
               player.sendMessage("o(0) == "+ep.o(1));
            }
        }}.runTaskTimer(MainCombat.getPlugin(), 0, 1);*/


        /* OOO // Flow
        * On attack
        *  >Get attackers normal damage
        *  >Get attackers swing timer
        *  >Find the scale of the damage from the swing timer, to find what % of damage was dealt.
        *  >Keep track of this % scale.
        *  > <Break>
        *  >Get attackers weapon and its damage range. *
        *  >Get defenders armor and armor values. **
        *  >Scale attackers damage to a range based upon the swing timer value we stored before.
        *  >Check if scaled damage is greater then armor
        *  ?If so:
        *   > Take damage - armor
        *   > Deal that damage to defender
        *   > Finish event
        *  ?If not:
        *   > Track the damage dealt and store it and wait for next hit
        *   > On next hit add incoming damage to stored damage if its greater then armor deal damage
        *   > if not repeat above cycle till damage is meet
        *   > Clear stored damage after a amount of time to make new fights even.
        *
        *
        *  * Get attackers weapon and damage.
        *   ?How do this?
        *
        *  ** Get defenders armor and armor value
        *   ?How do this?
        *
        */
