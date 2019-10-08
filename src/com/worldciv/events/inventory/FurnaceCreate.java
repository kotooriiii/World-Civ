package com.worldciv.events.inventory;


import net.minecraft.server.v1_12_R1.EnumParticle;
import net.minecraft.server.v1_12_R1.PacketPlayOutWorldParticles;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Furnace;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.FurnaceInventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

import static com.worldciv.utility.utilityStrings.worldciv;

public class FurnaceCreate implements Listener {

    @EventHandler
    public void onSmeltCompletion(FurnaceSmeltEvent e) {

        Furnace furnace = (Furnace) e.getBlock().getState();
        FurnaceInventory furnaceInv = (FurnaceInventory) furnace.getInventory();

        ItemStack[] itemsInFurnace = furnaceInv.getContents();

        Material source = Material.IRON_INGOT;
        Material fuel = Material.COAL;

        if (itemsInFurnace[0] == null) { //To prevent errors from occuring | This spot is material being smelted
            return;
        }

        if (itemsInFurnace[1] == null) {

            if(itemsInFurnace[0].getType() != source){
                return;
            }
            // No fuel
            List<Entity> near = furnace.getWorld().getEntities();
            for (Entity players : near) {
                if (players instanceof Player) {
                    if (players.getLocation().distance(furnace.getLocation()) <= 10) {
                        players.sendMessage(worldciv + ChatColor.GRAY + " There is no fuel on the furnace. Add more!");
                    }
                }
            }
            e.setCancelled(true);
            furnaceInv.getHolder().setBurnTime((short) 20);
            return;
        }

        if (itemsInFurnace[0].getType() == source && itemsInFurnace[1].getType() == fuel) { //Source and fuel detected in furnace


            List<Entity> AllEntities = furnace.getWorld().getEntities();

            for (Entity players : AllEntities) {
                if (players instanceof Player) {
                    if (players.getLocation().distance(furnace.getLocation()) <= 10) {

                        if (itemsInFurnace[0].getItemMeta().getLore() == null || itemsInFurnace[1].getItemMeta().getLore() == null) {

                            ((Player) players).updateInventory(); //it glitches with non-vanilla items, thts why we update
                            players.sendMessage(worldciv + ChatColor.GRAY + " The ingot(s) didn't smelt! Is this the correct recipe?");
                            PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.VILLAGER_ANGRY, true, furnace.getLocation().getBlockX(), furnace.getLocation().getBlockY(), furnace.getLocation().getBlockZ(), 1, 1, 1, 10, 50, null);
                            ((CraftPlayer) players).getHandle().playerConnection.sendPacket(packet);
                            ((Player) players).playSound(furnace.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 5L, 0);
                            e.setCancelled(true);

                        } else if (itemsInFurnace[0].getItemMeta().getLore().get(0).contains("refined iron ingot") && itemsInFurnace[1].getItemMeta().getLore().get(0).contains("Coke is a fuel")) {
                            if (itemsInFurnace[1].getDurability() != 1) {

                                ((Player) players).updateInventory(); //it glitches with non-vanilla items, thts why we update
                                PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.VILLAGER_HAPPY, true, furnace.getLocation().getBlockX(), furnace.getLocation().getBlockY(), furnace.getLocation().getBlockZ(), 1, 1, 1, 10, 50, null);
                                ((CraftPlayer) players).getHandle().playerConnection.sendPacket(packet);
                                ((Player) players).playSound(furnace.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 5L, 0);

                                if (itemsInFurnace[0].getAmount() > 1) {
                                    //was gunna add cooktime, leave this here
                                }

                            }

                        } else if (itemsInFurnace[0].getItemMeta().getLore().get(0).contains("refined iron ingot") && itemsInFurnace[1].getItemMeta().getLore().get(0).contains("Activated Carbon is a fuel")) {
                            if (itemsInFurnace[1].getDurability() == 1) { //charcoal

                                ((Player) players).updateInventory(); //it glitches with non-vanilla items, thts why we update
                                PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.VILLAGER_HAPPY, true, furnace.getLocation().getBlockX(), furnace.getLocation().getBlockY(), furnace.getLocation().getBlockZ(), 1, 1, 1, 10, 50, null);
                                ((CraftPlayer) players).getHandle().playerConnection.sendPacket(packet);
                                ((Player) players).playSound(furnace.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 5L, 0);

                                if (itemsInFurnace[0].getAmount() > 1) {
                                    //was gunna add cooktime, leave this here
                                }

                            }
                        }

                    }
                }
            }
        }
    }

    @EventHandler
    public void onBurnEvent(FurnaceBurnEvent e) {
        Furnace furnace = (Furnace) e.getBlock().getState();
        FurnaceInventory furnaceInv = (FurnaceInventory) furnace.getInventory();

        ItemStack[] itemsInFurnace = furnaceInv.getContents();

        Material source = Material.IRON_INGOT;
        Material fuel = Material.COAL;

        if (itemsInFurnace[0] == null || itemsInFurnace[1] == null) {
            return;
        }

        if (itemsInFurnace[0].getItemMeta().getLore() == null | itemsInFurnace[1].getItemMeta().getLore() == null) {
            return;
        }

        if (itemsInFurnace[1].getType() == Material.COAL && itemsInFurnace[1].getDurability() != 1 && itemsInFurnace[1].getItemMeta().getLore().get(0).contains("Coke is a fuel")) {
            for (Entity p : furnaceInv.getViewers()) {
               // p.sendMessage(worldciv + ChatColor.GRAY + " You added coke (fuel) to the furnace!");
            }
            e.setBurnTime(100); //5s of smelting time/burn time |||| This means you need two of these to smelt one steel
        } else if (itemsInFurnace[1].getType() == Material.COAL && itemsInFurnace[1].getDurability() == 1 && itemsInFurnace[1].getItemMeta().getLore().get(0).contains("Activated Carbon")) {
            for (Entity p : furnaceInv.getViewers()) {
               // p.sendMessage(worldciv + ChatColor.GRAY + " You added activated carbon to the furnace!");
            }
            e.setBurnTime(50); //2.5s || You need 4 of these to smelt one steel
        }

        if (itemsInFurnace[0].getType() == Material.IRON_INGOT && itemsInFurnace[0].getItemMeta().getLore().get(0).contains("refined iron ingot")) {

        }


    }

    @EventHandler
    public void onFurnaceClickEvent(InventoryClickEvent e) {
        HumanEntity p = e.getWhoClicked();
        ItemStack item = e.getCurrentItem();
        if (e.getView().getType() == InventoryType.FURNACE) { //We are looking at a furnace inventory.
            FurnaceInventory furnaceInv = (FurnaceInventory) e.getInventory();
            ItemStack[] itemsInFurnace = furnaceInv.getContents();
            int slot = e.getRawSlot();

            if (furnaceInv.getHolder().getBurnTime() > 0) { //It is currently smelting.


                if(item.getItemMeta() == null || item.getItemMeta().getLore() == null){
                    return;
                }

                if(e.isShiftClick()) {
                    Block furnaceblock = furnaceInv.getLocation().getBlock();
                    Block playerblock = p.getLocation().getBlock();
                    Location furnaceloc = furnaceblock.getLocation();
                    //Bukkit.broadcastMessage(furnaceblock.getFace(playerblock).toString());
                    //Bukkit.broadcastMessage(String.valueOf(furnaceblock.getFace(playerblock).getModX()));

                    p.sendMessage(worldciv + ChatColor.GRAY + " You have stopped the smelting process! Refunding resources!");

                    if(itemsInFurnace[0] != null && itemsInFurnace[0].getType() != Material.AIR) {
                        Bukkit.getWorld("world").dropItem(furnaceloc, itemsInFurnace[0]);
                        itemsInFurnace[0].setAmount(-1);
                    }
                    if(itemsInFurnace[1] != null && itemsInFurnace[1].getType() != Material.AIR) {
                        Bukkit.getWorld("world").dropItemNaturally(furnaceloc, itemsInFurnace[1]);
                        itemsInFurnace[1].setAmount(-1);
                    }

                    if(itemsInFurnace[2] != null && itemsInFurnace[1].getType() != Material.AIR) {
                        Bukkit.getWorld("world").dropItemNaturally(furnaceloc, itemsInFurnace[2]);
                        itemsInFurnace[2].setAmount(-1);
                    }

                    List<Entity> AllEntities = furnaceblock.getWorld().getEntities();

                    for (Entity players : AllEntities) {
                        if (players instanceof Player) {
                            if (players.getLocation().distance(furnaceblock.getLocation()) <= 10) {

                                ((Player) players).playSound(furnaceblock.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 5L, 0);
                            }
                        }
                    }

                    e.setCancelled(true);
                    e.getView().close();
                }


                if(item.getType() == Material.IRON_INGOT && item.getItemMeta().getLore().get(0).contains("refined iron ingot")){
                    p.sendMessage(worldciv + ChatColor.GRAY + " It is too dangerous to pick this item while smelting!");
                    p.sendMessage(ChatColor.YELLOW + "Shift click to stop the smelting process!"); //TIPS
                    e.setCancelled(true);
                    return;
                }

                if(item.getType() == Material.COAL && (item.getItemMeta().getLore().get(0).contains("Coke is a fuel") || item.getItemMeta().getLore().get(0).contains("Activated Carbon"))){
                    p.sendMessage(worldciv + ChatColor.GRAY + " It is too dangerous to pick this item while smelting!");
                    p.sendMessage(ChatColor.YELLOW + "Shift click to stop the smelting process!"); //TIPS
                    e.setCancelled(true);
                    return;
                }

                if (slot == 0 || slot == 1) { //If clicked on slot 0 or slot 1

                    if (itemsInFurnace[0] == null || itemsInFurnace[1] == null) { //prvent null errors
                        return;
                    }


                    if ((itemsInFurnace[0].getType() == Material.IRON_INGOT && itemsInFurnace[1].getType() == Material.COAL)) {

                       if (e.isLeftClick() || e.isRightClick()) {

                            p.sendMessage(worldciv + ChatColor.GRAY + " It is too dangerous to pick this item while smelting!");
                            p.sendMessage(ChatColor.YELLOW + "Shift click to stop the smelting process!"); //TIPS
                            e.setCancelled(true);

                        }
                    }

                }
            }
        }
    }
}

