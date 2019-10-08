package com.worldciv.commands;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.worldciv.events.chat.ChatChannelEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

import static com.worldciv.events.chat.ChatChannelEvent.getRawMessage;
import static com.worldciv.utility.utilityMultimaps.chatchannels;
import static com.worldciv.utility.utilityStrings.*;

public class ChatCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {


        if (cmd.getName().equalsIgnoreCase("channels") || cmd.getName().equalsIgnoreCase("ch")) {

            if (args.length == 0) {
                sender.sendMessage(maintop);

                sender.sendMessage(ChatColor.DARK_PURPLE + "Global:");
                sender.sendMessage(ChatColor.YELLOW + "/g" + ChatColor.GRAY + ": [G] Join global chat.");
                sender.sendMessage(ChatColor.DARK_PURPLE + "Towny-Chat:");
                sender.sendMessage(ChatColor.YELLOW + "/tc" + ChatColor.GRAY + ": [TC] Join town chat.");
                sender.sendMessage(ChatColor.YELLOW + "/nc" + ChatColor.GRAY + ": [NC] Join nation chat.");
                sender.sendMessage(ChatColor.YELLOW + "/anc" + ChatColor.GRAY + ": [ANC] Join ally-nation chat.");
                sender.sendMessage(ChatColor.DARK_PURPLE + "Must be 50 blocks or closer:");
                sender.sendMessage(ChatColor.YELLOW + "/l" + ChatColor.GRAY + ": [RP] Join local chat.");
                sender.sendMessage(ChatColor.YELLOW + "/ooc" + ChatColor.GRAY + ": [OOC] Join OOC chat.");


                if (sender.hasPermission("worldciv.mod") || sender.hasPermission("worldciv.admin")) {
                    sender.sendMessage(ChatColor.RED + "Only moderators+ can see the following:");
                    sender.sendMessage(ChatColor.YELLOW + "/mod" + ChatColor.GRAY + ": [MOD] Join moderator chat.");
                }

                if (sender.hasPermission("worldciv.admin")) {
                    sender.sendMessage(ChatColor.RED + "Only admins can see the following:");
                    sender.sendMessage(ChatColor.YELLOW + "/admin" + ChatColor.GRAY + ": [ADMIN] Join admin chat.");

                }
                sender.sendMessage(mainbot);
                return true;
            }
        } else if (cmd.getName().equalsIgnoreCase("l") || cmd.getName().equalsIgnoreCase("local")) {

            if (args.length == 0) {
                executeChatTransfer(sender, "local");
                return true;
            }
            executeHiddenChatTransfer(sender, "local", args);
            return true;

        } else if (cmd.getName().equalsIgnoreCase("ooc")) {

            if (args.length == 0) {
                executeChatTransfer(sender, "ooc");
                return true;
            }
            executeHiddenChatTransfer(sender, "ooc", args);
            return true;

        } else if (cmd.getName().equalsIgnoreCase("g") || cmd.getName().equalsIgnoreCase("global")) {

            if (args.length == 0) {
                executeChatTransfer(sender, "global");
                return true;
            }
            executeHiddenChatTransfer(sender, "global", args);
            return true;

        } else if (cmd.getName().equalsIgnoreCase("tc")) {

            if (ChatChannelEvent.getTown((Player) sender) == null) {
                sender.sendMessage(worldciv + ChatColor.GRAY + " You must be in a town to use this chat!");
                return true;
            }

            if (args.length == 0) {
                executeChatTransfer(sender, "tc");
                return true;
            }

            executeHiddenChatTransfer(sender, "tc", args);
            return true;

        } else if (cmd.getName().equalsIgnoreCase("nc")) {

            if (ChatChannelEvent.getNation((Player) sender) == null) {
                sender.sendMessage(worldciv + ChatColor.GRAY + " You must be in a nation to use this chat!");
                return true;
            }

            if (args.length == 0) {
                executeChatTransfer(sender, "nc");
                return true;
            }

            executeHiddenChatTransfer(sender, "nc", args);
            return true;

        } else if (cmd.getName().equalsIgnoreCase("anc")) {

            if (ChatChannelEvent.getNation((Player) sender) == null) {
                sender.sendMessage(worldciv + ChatColor.GRAY + " You must be in a nation to use this chat!");
                return true;
            }

            if (args.length == 0) {
                executeChatTransfer(sender, "anc");
                return true;
            }
            executeHiddenChatTransfer(sender, "anc", args);
            return true;

        } else if (cmd.getName().equalsIgnoreCase("mod")) {

            if (!sender.hasPermission("worldciv.mod") && !sender.hasPermission("worldciv.admin")) { //not mod
                sender.sendMessage(worldciv + ChatColor.RED + " You have no permission to access this chat channel. Permission node: worldciv.mod OR worldciv.admin");
                return true;
            }

            if (args.length == 0) {
                executeChatTransfer(sender, "mod");
                return true;
            }
            executeHiddenChatTransfer(sender, "mod", args);
            return true;
        } else if (cmd.getName().equalsIgnoreCase("admin")) {
            if (!sender.hasPermission("worldciv.admin")) {
                sender.sendMessage(worldciv + ChatColor.RED + " You have no permission to access this chat channel. Permission node: worldciv.admin");
                return true;
            }

            if (args.length == 0) {
                executeChatTransfer(sender, "admin");
                return true;
            }

            executeHiddenChatTransfer(sender, "admin", args);
            return true;

        }

        return true;
    }


    public Set<Player> getRecipients() {

        Collection<? extends Player> online_players = Bukkit.getOnlinePlayers();
        Set<Player> recipients = new HashSet<Player>();
        recipients.addAll(online_players);

        return recipients;
    }

    public void executeChatTransfer(CommandSender sender, String channelname) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(worldciv + ChatColor.RED + " You can't join chat channels as console. It is HIGHLY planned to use logs to record messages in the future.");
            return;
        }


        boolean found_player = false; //Found the player in the same channel!

        for (String all_players_in_chat : chatchannels.get(channelname)) { //For all players in the parameter's channel

            if (all_players_in_chat.equalsIgnoreCase(sender.getName())) { //If the player matches the sender.
                found_player = true; //We found the player.
            }

        }

        if (found_player) { //If player is found!
            sender.sendMessage(worldciv + ChatColor.GRAY + " You are already in " + ChatColor.YELLOW + channelname + ChatColor.GRAY + " chat.");
            return; //Don't do anything! He's already in this parameter's chat.
        }

        //Condition already met: Not already in the chat mode.

        if (chatchannels.containsValue(sender.getName())) { //If chat channel finds a player in a chat.
            Multimap<String, String> inversecc = Multimaps.invertFrom(chatchannels, ArrayListMultimap.<String, String>create()); //Inverse to find the channelname
            Collection<String> chat_channel = inversecc.get(sender.getName()); //Find the channelname

            for (String channel : chat_channel) { //Filtering! :)
                chatchannels.remove(channel, sender.getName()); //Remove player from previous channel name

            }
        }

        //Conditions already met: Not already in chat mode && removed from (if any) chat channel (which should be removed.. always going to be in one.)

        chatchannels.put(channelname, sender.getName());
        sender.sendMessage(worldciv + ChatColor.GRAY + " You have joined " + ChatColor.YELLOW + channelname + ChatColor.GRAY + " chat.");
        return;
    }

    public void executeHiddenChatTransfer(CommandSender sender, String channelname, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(worldciv + ChatColor.RED + " You can't join chat channels as console. It is HIGHLY planned to use logs to record messages in the future.");
            return;
        }


        boolean found_player = false; //Found the player in the same channel!

        for (String all_players_in_chat : chatchannels.get(channelname)) { //For all players in the parameter's channel

            if (all_players_in_chat.equalsIgnoreCase(sender.getName())) { //If the player matches the sender.
                found_player = true; //We found the player.
            }

        }

        if (found_player) { //If player is found!
            ((Player) sender).chat(getRawMessage(args));
            return; //Don't do anything! He's already in this parameter's chat.
        }

        //Condition already met: Not already in the chat mode.

        String old_channel = "";

        if (chatchannels.containsValue(sender.getName())) { //If chat channel finds a player in a chat.
            Multimap<String, String> inversecc = Multimaps.invertFrom(chatchannels, ArrayListMultimap.<String, String>create()); //Inverse to find the channelname
            Collection<String> chat_channel = inversecc.get(sender.getName()); //Find the channelname

            for (String channel : chat_channel) { //Filtering! :)
                old_channel = channel;
                chatchannels.remove(channel, sender.getName()); //Remove player from previous channel name
            }
        }
        //Conditions already met: Not already in chat mode && removed from (if any) chat channel (which should be removed.. always going to be in one.)

        chatchannels.put(channelname, sender.getName());
        ((Player) sender).chat(getRawMessage(args));
        chatchannels.remove(channelname, sender.getName());
        chatchannels.put(old_channel, sender.getName());
        return;
    }


}