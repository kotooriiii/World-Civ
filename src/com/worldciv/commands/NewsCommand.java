package com.worldciv.commands;

import com.worldciv.the60th.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import static com.worldciv.the60th.Main.fileSystem;
import static com.worldciv.the60th.Main.plugin;
import static com.worldciv.utility.utilityArrays.setnewsmessage;
import static com.worldciv.utility.utilityStrings.*;

public class NewsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {
        if (cmd.getName().equalsIgnoreCase("news")) {
            if (!sender.hasPermission("worldciv.news") && sender instanceof Player) {

                if (args.length == 0) {

                    sender.sendMessage(maintop);
                    sender.sendMessage(ChatColor.GRAY + " The latest news has been delivered to you:");
                    sender.sendMessage(" ");
                    if (plugin.getConfig().getString("newsmessage").equals("          " + ChatColor.YELLOW + "empty")) {
                        sender.sendMessage(ChatColor.RESET + ChatColor.translateAlternateColorCodes('&', ChatColor.RED + "No news today!"));
                    } else {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("newsmessage")));
                    }
                    sender.sendMessage(" ");
                    if (fileSystem.getToggleList("sbanimation").contains(sender.getName())) sender.sendMessage(ChatColor.GRAY + " In order to be able to see the animation display use /toggle sb and /toggle anim");
                    sender.sendMessage(mainbot);
                    return true;
                } else {
                    sender.sendMessage(worldciv + ChatColor.GRAY +  " Did you mean to use" + ChatColor.YELLOW + " /news" + ChatColor.GRAY + "?");
                    return false;
                }
            } else if (args.length == 0) {
                sender.sendMessage(maintop);
                if (plugin.getConfig().getString("newsmessage") == null) {
                    sender.sendMessage(ChatColor.GRAY + "No current news message found for the server. To add one use:" + ChatColor.YELLOW + " /news set <message>" + ChatColor.GRAY + ".");
                    sender.sendMessage(mainbot);
                    return true;
                }
                sender.sendMessage(ChatColor.GRAY + " The current news message is set to:");
                sender.sendMessage(" ");
                if (plugin.getConfig().getString("newsmessage").equals("          " + ChatColor.YELLOW + "empty")) {
                    sender.sendMessage(ChatColor.RESET + ChatColor.translateAlternateColorCodes('&', ChatColor.RED + "No news today!"));
                } else {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("newsmessage")));
                }
                sender.sendMessage(" ");
                sender.sendMessage(ChatColor.GRAY + " To replace the current display message:" + ChatColor.YELLOW + " /news set <message>");
                sender.sendMessage(ChatColor.GRAY + " Got no news? A bit dry?:" + ChatColor.YELLOW + " /news set empty");
                sender.sendMessage(mainbot);
                return true;

            } else if (args[0].equalsIgnoreCase("set")) {

                if (args.length >= 2) {

                    if (sender instanceof ConsoleCommandSender) {
                        StringBuilder str = new StringBuilder();
                        for (int i = 1; i < args.length; i++) {
                            str.append(args[i] + " ");
                        }

                        newsstring = ChatColor.YELLOW + str.toString().substring(0, str.length() - 1); //removes space


                        plugin.getConfig().set("newsmessage", "          " + newsstring);
                        plugin.saveConfig();

                        sender.sendMessage(worldciv + ChatColor.GREEN + " The news message has been set to: ");

                        Bukkit.broadcastMessage(worldciv + ChatColor.GRAY + " The news has been updated! Check the scoreboard or /news");

                        return true;
                    }

                    Player p = (Player) sender;

                    if (setnewsmessage.contains(sender)) {
                        sender.sendMessage(worldciv + ChatColor.GRAY + " You can't use this again! Confirm with" + ChatColor.YELLOW + " /news y" + ChatColor.GRAY + " or" + ChatColor.YELLOW + " /news n");
                        return true;
                    }

                    setnewsmessage.add(p);

                    StringBuilder str = new StringBuilder();
                    for (int i = 1; i < args.length; i++) {
                        str.append(args[i] + " ");
                    }

                    newsstring = ChatColor.YELLOW + str.toString().substring(0, str.length() - 1); //removes space

                    sender.sendMessage(ChatColor.YELLOW + "This is what your news message looks like in text:");
                    sender.sendMessage("");
                    if (args[1].equalsIgnoreCase("empty")) {
                        sender.sendMessage(ChatColor.RESET + ChatColor.translateAlternateColorCodes('&', ChatColor.RED + "No news today!"));
                    } else {
                        sender.sendMessage(ChatColor.RESET + ChatColor.translateAlternateColorCodes('&', newsstring));
                    }
                    sender.sendMessage("");
                    sender.sendMessage(ChatColor.RED + "You have 30 seconds to confirm with:" + ChatColor.YELLOW + " /news y" + ChatColor.RED + " or" + ChatColor.YELLOW + " /news n");

                    new BukkitRunnable() {
                        int x = 0;

                        public void run() {

                            if (!setnewsmessage.contains(sender)) {
                                cancel();
                                return;
                            }


                            if (x == 30) {
                                sender.sendMessage(worldciv + ChatColor.RED + " The time expired!");
                                setnewsmessage.remove(sender);
                                cancel();
                                return;
                            }
                            x++;
                        }
                    }.runTaskTimer(plugin, 0, 20);


                    return true;
                }

                sender.sendMessage(worldciv + ChatColor.RED + " Specify a message! Example:" + ChatColor.YELLOW + " /news set <message>");
                return true;

            } else if (args[0].equalsIgnoreCase("y")) {

                if (!setnewsmessage.contains(sender)) {
                    sender.sendMessage(worldciv + ChatColor.RED + " You must set a message first!");
                    return true;
                }


                plugin.getConfig().set("newsmessage", "          " + newsstring);
                plugin.saveConfig();

                sender.sendMessage(worldciv + ChatColor.GREEN + " The news message has been set!");

                Bukkit.broadcastMessage(worldciv + ChatColor.GRAY + " The news has been updated! Check the scoreboard or /news");

                for (Player players : Bukkit.getOnlinePlayers()) {
                    // double pitch = .5 + (.033 * 2);                   //.5 (from place) + .033 (# note from beginning)
                    players.playSound(players.getLocation(), Sound.BLOCK_NOTE_SNARE, 3.0F, 6);
                    Main.getScoreboardManager().setScoreboard(players);
                }
                setnewsmessage.remove(sender);
                return true;

            } else if (args[0].equalsIgnoreCase("n")) {
                if (!setnewsmessage.contains(sender)) {
                    sender.sendMessage(worldciv + ChatColor.RED + " You must set a message first!");
                    return true;
                }
                sender.sendMessage(worldciv + ChatColor.RED + " You refused to use this news message!");
                setnewsmessage.remove(sender);
                return true;

            } else {
                sender.sendMessage(worldciv + ChatColor.RED + " Invalid arguments! Example:" + ChatColor.YELLOW + " /news set <message>");
                return true;

            }


        }

        return false;
    }
}
