package com.worldciv.events.player;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import static com.worldciv.commands.DungeonCommand.isDungeonAdmin;
import static com.worldciv.the60th.Main.getDungeonManager;
import static com.worldciv.utility.utilityStrings.worldciv;

public class CommandPreprocess implements Listener {
    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        Player p = event.getPlayer();
        String[] args= event.getMessage().split(" ");


        if (event.getMessage().toLowerCase().startsWith("/tab") && !event.getPlayer().isOp() && !(event.getPlayer() instanceof ConsoleCommandSender)) {
            event.getPlayer().sendMessage("Unknown command. Type \"/help\" for help."); //diguise
            event.setCancelled(true);
        }
        if ((event.getMessage().equalsIgnoreCase("/pl") ||event.getMessage().equalsIgnoreCase("/plugins") || event.getMessage().toLowerCase().startsWith("/?")) && !event.getPlayer().hasPermission("worlciv.cmds")) {
            event.getPlayer().sendMessage(worldciv + ChatColor.GRAY + " Not allowed to use this special command.");
            event.setCancelled(true);
        }

        if (event.getMessage().toLowerCase().startsWith("/rg")) {

            if (!isDungeonAdmin(p)) {
                return;
            }


            switch (args[1].toLowerCase()) {
                case "remove":
                    for (String dungeon_id : getDungeonManager.getAllDungeons()) {
                        if (dungeon_id.toString().equalsIgnoreCase(args[2])) {

                            p.sendMessage(worldciv + ChatColor.RED + " The dungeon region " + ChatColor.YELLOW + "'" + args[2] + "'"
                                    + ChatColor.RED + " CANNOT be removed! Use" + ChatColor.YELLOW + " /dg remove <dungeon-id> " + ChatColor.RED + "instead!");

                            event.setCancelled(true);
                        }
                    }
                    return;

            }

            if (event.getMessage().contains("dungeon-region")) {
                p.sendMessage(worldciv + ChatColor.RED + " The dungeon region flag CANNOT be added/removed! Use" + ChatColor.YELLOW + " /dg admin" + ChatColor.RED + " for more help.");

                event.setCancelled(true);
            }
        }



        }

}
