package com.worldciv.events.player;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

import static com.worldciv.utility.utilityStrings.worldciv;

public class WeatherChangingEvent implements Listener {
    /*
            This class has only used to detect storms, with the given information we can detect if a player is in a biome where snow/rain exist

               todo  :  enable custom biomes, click on edit and then click on "custom biomes".
               todo : worldpainter can create biomes in any shape you want. This is the template for later use,  if decide to use this!
      */

    @EventHandler
    public void onWeatherChangeEvent(org.bukkit.event.weather.WeatherChangeEvent e){

        for (Player players : Bukkit.getOnlinePlayers()) {

            Location loc = players.getLocation();
            World world = players.getWorld();
            Biome biome = world.getBiome(loc.getBlockX(), loc.getBlockZ());


            if (!world.hasStorm()) {
                if (biome == Biome.DESERT || biome == Biome.DESERT_HILLS | biome == Biome.MUTATED_DESERT || biome == Biome.MESA || biome == Biome.MESA_CLEAR_ROCK
                        || biome == Biome.MESA_ROCK || biome == Biome.MUTATED_MESA || biome == Biome.MUTATED_MESA_CLEAR_ROCK || biome == Biome.MUTATED_MESA_ROCK
                        || biome == Biome.SAVANNA || biome == Biome.SAVANNA_ROCK || biome == Biome.MUTATED_SAVANNA || biome == Biome.MUTATED_SAVANNA_ROCK) {
                    players.sendMessage(worldciv + ChatColor.GRAY + " It is storming! You are safe in the " + biome.name());
                } else {
                    players.sendMessage(worldciv + ChatColor.GRAY + " It is storming! I don't think torches work during this time.");
                }

            } else {
                players.sendMessage(worldciv + ChatColor.GRAY + " It stopped storming.");

            }
        }

    }
}
