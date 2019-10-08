package com.worldciv.utility;

import com.worldciv.events.player.PlayerAttackEvents;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class ExampleSelfCancelingTask extends BukkitRunnable {

    private final JavaPlugin plugin;

    private int counter;

    private Player player;

    public ExampleSelfCancelingTask(JavaPlugin plugin, int counter, Player player) {
        this.player = player;
        this.plugin = plugin;
        if (counter < 1) {
            throw new IllegalArgumentException("counter must be greater than 1");
        } else {
            this.counter = counter;
        }
    }

    @Override
    public void run() {
        if(PlayerAttackEvents.defenderArmorTracker.containsKey(player)){
            if(counter >= 30){
                PlayerAttackEvents.defenderArmorTracker.remove(player);
            }else{

            }
        }
        else if(!(PlayerAttackEvents.defenderArmorTracker.containsKey(player))){
            this.cancel();
        }
    }

}
