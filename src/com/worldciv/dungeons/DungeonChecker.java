package com.worldciv.dungeons;

import com.worldciv.the60th.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import static com.worldciv.the60th.Main.getDungeonManager;

public class DungeonChecker implements Listener {

    @EventHandler
    public void mobSpawnChecker(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (getDungeonManager.getDungeon(player) != null) {
            Location to = event.getTo();
            Location from = event.getFrom();
            if (to.getBlockX() == from.getBlockX() && to.getBlockY() == from.getBlockY() && to.getBlockZ() == from.getBlockZ()) {
                return; // did not actually move to a new block. do nothing.
            }

            Location location = player.getLocation();
            Dungeon dungeon = getDungeonManager.getDungeon(player);
            for(int i = 0; i < dungeon.mobLocations.length; i++  ){
                long radius = Math.round(location.distance(dungeon.mobLocations[i].location));

                switch (dungeon.difficulty){
                    case "EASY":
                        if(radius <= dungeon.EASY_RANGE){
                            dungeon.mobLocations[i].spawnMob();
                        }
                        break;
                    case "MEDIUM":
                        if(radius <= dungeon.MED_RANGE){
                            dungeon.mobLocations[i].spawnMob();
                        }

                        break;
                    case "HARD":
                        if(radius <= dungeon.HARD_RANGE){
                            dungeon.mobLocations[i].spawnMob();
                        }
                        break;
                }
            }
            return;
        }
        return;
    }

    @EventHandler
    public void deathHandler(PlayerDeathEvent event){
        Player player = event.getEntity();
        if (getDungeonManager.getDungeon(player) != null) {
            event.setKeepInventory(true);
        }
    }
    @EventHandler
    public void respawnHandler(PlayerRespawnEvent event){
        Player player = event.getPlayer();
        if (getDungeonManager.getDungeon(player) != null) {
            player.setHealth(player.getMaxHealth());
            Location location = Main.fileSystem.getPlayerSpawn(getDungeonManager.getDungeon(player).getDungeonID()); //Location is dungeon respawn point.
            player.teleport(location);
                }

        }
    }

