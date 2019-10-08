package com.worldciv.events.player;

import org.bukkit.ChatColor;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import java.util.List;

import static com.worldciv.utility.utilityStrings.worldciv;

public class BoatFixEvent implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST) //OUR BOATS NOW !
    public void onBoatRightClick(PlayerInteractEntityEvent e) { //note: all in right click @ this event
        Entity entity = e.getRightClicked();
        Player p = e.getPlayer();

        if (entity instanceof Boat) {
            Boat boat = (Boat) entity;

            List<Entity> passengers = boat.getPassengers();

            if (passengers.size() == 0) {
                p.sendMessage(worldciv + ChatColor.GRAY + " You are the captain. Arrrgh!");
                boat.addPassenger(p);
                return;
            } else if (passengers.size() == 1) {
                p.sendMessage(worldciv + ChatColor.GRAY + " You are now a passenger of " + ChatColor.AQUA + passengers.get(0).getName() + ChatColor.GRAY + ".");
                boat.addPassenger(p);
                return;
            } else if(passengers.size() >= 2){
                p.sendMessage(worldciv + ChatColor.GRAY + " There is no more space on the pirate ship!");
                return;
            }
            e.setCancelled(true); //our event now!
        }
    }
}
