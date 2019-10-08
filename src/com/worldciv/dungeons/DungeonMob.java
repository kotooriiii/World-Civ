package com.worldciv.dungeons;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import static com.worldciv.the60th.Main.getMythicMobs;

public class DungeonMob {
    public Location location;
    private int amount =1;
    private String mobID;
    private boolean notSpawned = true;

    public DungeonMob(Location location, int amount, String mobID){
        this.location = location;
        this.amount = amount;
        this.mobID = mobID;
    }

    public void spawnMob(){
        if(this.notSpawned){
        for(int i =0; i <this.amount; i++){
            getMythicMobs().getMobManager().spawnMob(this.mobID, this.location);
            //Bukkit.broadcastMessage("Value of notSpawned  "+ i + " " +notSpawned );
        }
            this.notSpawned = false;
        }


    }
}

