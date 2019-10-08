package com.worldciv.mythicmobs;


import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.mobs.MythicMob;
import org.bukkit.Location;

import static com.worldciv.the60th.Main.getMythicMobs;

public class CustomMobs {

    public static void spawn(Location loc) {
        //MythicMob CustomPolarBear = getMythicMobs().getMobManager().getMythicMob("PolarBear");
        getMythicMobs().getMobManager().spawnMob("PolarBear", loc);

    }


}
