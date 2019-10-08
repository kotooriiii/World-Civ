package com.worldciv.commands;

import com.worldciv.utility.Fanciful.mkremins.fanciful.FancyMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.worldciv.the60th.Main.fileSystem;
import static com.worldciv.utility.utilityMultimaps.playerandsubject;
import static com.worldciv.utility.utilityStrings.*;

@SuppressWarnings("all")
public class PollCommand implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender commandSender, Command cmd, String s, String[] args) {

        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(ChatColor.RED + " You can't vote as console.");
            return true;
        }

        Player sender = (Player) commandSender;

        if (cmd.getName().equalsIgnoreCase("polls") || cmd.getName().equalsIgnoreCase("poll")) {

            File pollsdata_folder = new File(Bukkit.getPluginManager().getPlugin("WorldCivMaster").getDataFolder() + "/polls/pollsdata"); //folder
            File polls_folder = new File(Bukkit.getPluginManager().getPlugin("WorldCivMaster").getDataFolder() + "/polls"); //folder
            File polls_banlist = new File(polls_folder, "banlist.yml");
            File polls_reportlist = new File(polls_folder, "reportlist.yml");

            YamlConfiguration polls_banlist_yml = YamlConfiguration.loadConfiguration(polls_banlist);
            YamlConfiguration polls_reportlist_yml = YamlConfiguration.loadConfiguration(polls_reportlist);

            if (args.length == 0) {

                sender.sendMessage(maintop);

                sender.sendMessage(ChatColor.YELLOW + "/polls create <subject>" + ChatColor.GRAY + ": Create a poll.");
                sender.sendMessage(ChatColor.YELLOW + "/polls list" + ChatColor.GRAY + ": Show polls by subject and id.");
                sender.sendMessage(ChatColor.YELLOW + "/polls accept" + ChatColor.GRAY + ": Accept terms and conditions");
                sender.sendMessage(ChatColor.YELLOW + "/polls report <id>" + ChatColor.GRAY + ": Report a poll.");
                sender.sendMessage(ChatColor.YELLOW + "/polls refresh" + ChatColor.GRAY + ": Refresh poll data.");
                sender.sendMessage(ChatColor.YELLOW + "/polls vote <id> <Y/N>" + ChatColor.GRAY + ": Vote a subject by their ID.");
                sender.sendMessage(ChatColor.YELLOW + "/polls show <id> " + ChatColor.GRAY + ": Show more information about a poll.");

                if (sender.hasPermission("worldciv.mod") || sender.hasPermission("worldciv.admin")) {
                    sender.sendMessage(ChatColor.RED + " Only moderators+ can see below: ");
                    sender.sendMessage(ChatColor.RED + "/polls remove <id>" + ChatColor.RED + ": Remove a poll.");
                    sender.sendMessage(ChatColor.RED + "/polls resolve <id>" + ChatColor.RED + ": Resolve a reported poll.");
                    sender.sendMessage(ChatColor.RED + "/polls reportlist" + ChatColor.RED + ": View reported polls.");
                    sender.sendMessage(ChatColor.RED + "/polls ban <player>" + ChatColor.RED + ": Bans by UUID. Removes player data from polls.");
                }

                sender.sendMessage(mainbot);

                return true;
            }


            switch (args[0].toLowerCase()) {

                case "ban":
                    if (!sender.hasPermission("worldciv.mod") && !sender.hasPermission("worldciv.admin")) {
                        sender.sendMessage(worldciv + ChatColor.GRAY + " No permission to use this command. Permission node: " + ChatColor.YELLOW + "worldciv.mod" + ChatColor.GRAY + " or " + ChatColor.YELLOW + "worldciv.admin" + ChatColor.GRAY + ".");
                        return true;
                    }

                    if (args.length == 2) {

                        if (Bukkit.getPlayer(args[1]) == null) {
                            sender.sendMessage(worldciv + ChatColor.GRAY + " No player was found with that name.");
                            return true;
                        }

                        Player bannedp = Bukkit.getPlayer(args[1]);

                        if (polls_banlist_yml.get("banlist") == null) {
                            List<String> list = new ArrayList<>();
                            list.add(bannedp.getUniqueId().toString());
                            polls_banlist_yml.set("banlist", list);

                        } else {
                            List<String> list = polls_banlist_yml.getStringList("banlist");

                            if(list.contains(bannedp.getUniqueId().toString())){
                                sender.sendMessage(worldciv + ChatColor.GREEN + " You unbanned a user from the polls banlist.");
                                list.remove(bannedp.getUniqueId().toString());
                                polls_banlist_yml.set("banlist", list);
                                try {
                                    polls_banlist_yml.save(polls_banlist);
                                } catch (Exception e) {
                                }

                                return true;
                            }

                            list.add(bannedp.getUniqueId().toString());
                            polls_banlist_yml.set("banlist", list);
                        }

                        try {
                            polls_banlist_yml.save(polls_banlist);
                        } catch (Exception e) {
                        }

                        if (polls_reportlist_yml.getKeys(false) != null) { //check report list keys, if player is in it. remove. if last one, remove it.
                            for (String id : polls_reportlist_yml.getKeys(false)) {
                                List<String> listx = polls_reportlist_yml.getStringList(id);
                                if (listx.size() == 1) {
                                    polls_reportlist_yml.set(id, null);
                                } else {
                                    listx.remove(bannedp);
                                }
                                try {
                                    polls_reportlist_yml.save(polls_reportlist);
                                } catch (Exception e) {

                                }

                            }
                        }
                        for (File file : fileSystem.allFiles(pollsdata_folder)) {
                            if (file.getName().contains(bannedp.getName())) {
                                file.delete();
                            }
                        }


                        sender.sendMessage(worldciv + ChatColor.GREEN + " Successfully banned a player from using polls. Deleted all reported and created polls by user.");
                        return true;


                    }
                    sender.sendMessage(worldciv + ChatColor.GRAY + " Ban a player from accessing polls with: " + ChatColor.YELLOW + " /polls ban <player> " + ChatColor.GRAY + ".");
                    return true;

                case "remove":
                    if (!sender.hasPermission("worldciv.mod") && !sender.hasPermission("worldciv.admin")) {
                        sender.sendMessage(worldciv + ChatColor.GRAY + " No permission to use this command. Permission node: " + ChatColor.YELLOW + "worldciv.mod" + ChatColor.GRAY + " or " + ChatColor.YELLOW + "worldciv.admin" + ChatColor.GRAY + ".");
                        return true;
                    }

                    if (args.length == 2) {

                        if (!getPollIds(pollsdata_folder).contains(args[1])) {
                            sender.sendMessage(worldciv + ChatColor.GRAY + " The ID you provided was not valid.");
                            return true;
                        }

                        File file = getPollByID(pollsdata_folder, args[1]);
                        file.delete();

                        if (polls_reportlist_yml.getKeys(false).contains(args[1])) {
                            try {
                                polls_reportlist_yml.set(args[1], null);
                                polls_reportlist_yml.save(polls_reportlist);
                            } catch (Exception e) {

                            }

                        }
                        sender.sendMessage(worldciv + ChatColor.GREEN + " Successfully removed the poll from the server.");
                        return true;
                    }
                    sender.sendMessage(worldciv + ChatColor.GRAY + " Remove a poll with: " + ChatColor.YELLOW + " /polls remove <id> " + ChatColor.GRAY + ".");

                    return true;

                case "resolve":
                    if (!sender.hasPermission("worldciv.mod") && !sender.hasPermission("worldciv.admin")) {
                        sender.sendMessage(worldciv + ChatColor.GRAY + " No permission to use this command. Permission node: " + ChatColor.YELLOW + "worldciv.mod" + ChatColor.GRAY + " or " + ChatColor.YELLOW + "worldciv.admin" + ChatColor.GRAY + ".");
                        return true;
                    }

                    if (args.length == 2) {

                        if (!polls_reportlist_yml.getKeys(false).contains(args[1])) {
                            sender.sendMessage(worldciv + ChatColor.GRAY + " The ID you provided was not valid.");
                            return true;
                        }
                        polls_reportlist_yml.set(args[1], null);
                        sender.sendMessage(worldciv + ChatColor.GREEN + " Successfully resolved the report issue.");
                        try {
                            polls_reportlist_yml.save(polls_reportlist);
                        } catch (Exception e) {

                        }
                        return true;
                    }
                    sender.sendMessage(worldciv + ChatColor.GRAY + " Resolve a reported poll with: " + ChatColor.YELLOW + " /polls resolve <id> " + ChatColor.GRAY + ".");

                    return true;
                case "reportlist":

                    if (!sender.hasPermission("worldciv.mod") && !sender.hasPermission("worldciv.admin")) {
                        sender.sendMessage(worldciv + ChatColor.GRAY + " No permission to use this command. Permission node: " + ChatColor.YELLOW + "worldciv.mod" + ChatColor.GRAY + " or " + ChatColor.YELLOW + "worldciv.admin" + ChatColor.GRAY + ".");
                        return true;
                    }

                    if (args.length == 1) {

                        if (polls_reportlist_yml.getKeys(true).size() == 0) {
                            sender.sendMessage(worldciv + ChatColor.GRAY + " No polls have been reported.");
                            return true;
                        }


                        sender.sendMessage(worldciv + ChatColor.GRAY + " Returning report list:");
                        for (String key : polls_reportlist_yml.getKeys(false)) {
                            List<String> list = polls_reportlist_yml.getStringList(key);
                            String playerswhoreported = list.toString().replace("[", "").replace("]", "");
                            sender.sendMessage(ChatColor.GRAY + "ID: " + ChatColor.YELLOW + key + ChatColor.GRAY + " was reported by " + playerswhoreported);
                        }
                        return true;
                    }

                    sender.sendMessage(worldciv + ChatColor.GRAY + " Display the list of reported poll ids with" + ChatColor.YELLOW + " /polls reportlist " + ChatColor.GRAY + ".");
                    return true;
                case "accept":
                    if (args.length == 1) {

                        if (polls_banlist_yml.getStringList("banlist").contains(sender.getUniqueId().toString())) {
                            sender.sendMessage(worldciv + ChatColor.GRAY + " You are banned from using polls.");
                            return true;
                        }


                        if (!playerandsubject.containsKey(sender)) { //not in array
                            sender.sendMessage(worldciv + ChatColor.GRAY + " You must create a poll subject before you can accept the terms and conditions.");
                            return true;
                        }

                        String subject = playerandsubject.get(sender); //subject
                        int iteration = 0; //self-explanatory


                        if (fileSystem.allFiles(pollsdata_folder) != null) {
                            for (File file : fileSystem.allFiles(pollsdata_folder)) { //all files in folder

                                String file_id = getPollId(file);
                                YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file); //load file

                                String file_subject = yaml.get(file_id + ".Subject").toString();

                                if (file_subject.equalsIgnoreCase(subject)) {
                                    sender.sendMessage(worldciv + ChatColor.GRAY + " A player has already published this subject.");
                                    return true;
                                }
                            }
                        }

                      /*  if (banned) {
                            sender.sendMessage(worldciv + ChatColor.GRAY + " You were banned before you could accept.");

                        } */

                        playerandsubject.remove(sender);
                        fileSystem.createPoll(sender, subject);

                        sender.sendMessage(worldciv + ChatColor.GREEN + " You accepted the terms and conditions. Use " + ChatColor.YELLOW + "/polls list " + ChatColor.GREEN + "to view your poll.");

                    } else {
                        sender.sendMessage(worldciv + ChatColor.GRAY + " Invalid arguments. Use " + ChatColor.YELLOW + "/polls accept" + ChatColor.GREEN + "to accept the terms and conditions.");
                    }
                    return true;
                case "create":

                    if (args.length == 1) {
                        sender.sendMessage(worldciv + ChatColor.GRAY + " To create a subject to vote in: " + ChatColor.YELLOW + "/polls create <subject>" + ChatColor.GRAY + ".");
                        return true;
                    }

                    if (polls_banlist_yml.getStringList("banlist").contains(sender.getUniqueId().toString())) {
                        sender.sendMessage(worldciv + ChatColor.GRAY + " You are banned from using polls.");
                        return true;
                    }

                    String subject = getMessage(args);

                    if (playerandsubject.containsKey(sender)) {
                        sender.sendMessage(worldciv + ChatColor.RED + " Your previous poll subject was abandoned.");
                        playerandsubject.remove(sender);
                    }

                    if (true) {
                        //check if this is already a name
                    }

                    if (true) {
                        //check if youre banned
                    }

                    playerandsubject.put(sender, subject);

                    new FancyMessage(worldciv + ChatColor.GRAY + " Before your poll subject, " + ChatColor.BLUE + subject + ChatColor.GRAY + ", is published, please read the ").then(ChatColor.YELLOW + "terms and conditions (hover me).").
                            tooltip(ChatColor.YELLOW + " Click me to redirect to terms and conditions.").link("https://discord.gg/r2NqfJ6").send(sender);
                    new FancyMessage(" ").send(sender);
                    new FancyMessage(ChatColor.GREEN + " Click me to accept the terms and conditions or use " + ChatColor.YELLOW + "/polls accept" + ChatColor.GRAY + ".").tooltip(ChatColor.GREEN + "Click me to accept the terms and conditions.").command("/polls accept").send(sender);
                    new FancyMessage(" ").send(sender);
                    return true;
                case "report":

                    if (args.length == 2) {
                        String possible_id = args[1];
                        if (polls_banlist_yml.getStringList("banlist").contains(sender.getUniqueId().toString())) {
                            sender.sendMessage(worldciv + ChatColor.GRAY + " You are banned from using polls.");
                            return true;
                        }
                        if (!getPollIds(pollsdata_folder).contains(possible_id)) {
                            sender.sendMessage(worldciv + ChatColor.GRAY + " The ID you provided was not valid.");
                            return true;
                        }

                        if (polls_banlist_yml.contains(sender.getName())) {
                            sender.sendMessage(worldciv + ChatColor.GRAY + " You are banned from using this poll feature.");
                            return true;
                        }

                        if (polls_reportlist_yml.get(possible_id) == null) { //it hasnt been added to a reported list, make player first one
                            List<String> list = new ArrayList<>();
                            list.add(sender.getName());
                            polls_reportlist_yml.createSection(possible_id);
                            polls_reportlist_yml.set(possible_id, list);

                        } else { //its already a list and need to check
                            List<String> list = polls_reportlist_yml.getStringList(possible_id);
                            if (list.contains(sender.getName())) {
                                sender.sendMessage(worldciv + ChatColor.GRAY + " You have already reported this ID.");
                                //todo remove this from teh commadn remove
                                return true;
                            }
                            list.add(sender.getName());
                            polls_reportlist_yml.set(possible_id, list);
                        }

                        try {
                            polls_reportlist_yml.save(polls_reportlist);
                        } catch (Exception e) {

                        }

                        sender.sendMessage(worldciv + ChatColor.GRAY + " Your report was successfully sent.");
                        return true;
                    }

                    sender.sendMessage(worldciv + ChatColor.GRAY + " To report a subject: " + ChatColor.YELLOW + "/polls report <subject>" + ChatColor.GRAY + ".");
                    return true;
                case "list":

                    File[] files = fileSystem.allFiles(pollsdata_folder);

                    if (files.length == 0 || files == null) {
                        sender.sendMessage(worldciv + ChatColor.GRAY + " There are no open polls.");
                        return true;
                    }
                    sender.sendMessage(worldciv + ChatColor.GRAY + " Polls (Hovering subject reveals ID): ");
                    for (File file : files) {
                        String id = getPollId(file);
                        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
                        String subj = yaml.get(id + ".Subject").toString();

                        new FancyMessage(ChatColor.GRAY + "Subject: " + ChatColor.BLUE + subj).tooltip(ChatColor.GRAY + "ID: " + ChatColor.YELLOW + id + ChatColor.GRAY + ". Click for more information.").command("/polls show " + id).send(sender);
                    }
                    return true;
                case "vote":

                    if (args.length == 3) {
                        if (polls_banlist_yml.getStringList("banlist").contains(sender.getUniqueId().toString())) {
                            sender.sendMessage(worldciv + ChatColor.GRAY + " You are banned from using polls.");
                            return true;
                        }
                        String possible_id = args[1];

                        if (!getPollIds(pollsdata_folder).contains(possible_id)) {
                            sender.sendMessage(worldciv + ChatColor.GRAY + " The ID you provided was not valid.");
                            return true;
                        }

                        if (!args[2].equalsIgnoreCase("yes") && !args[2].equalsIgnoreCase("n") && !args[2].equalsIgnoreCase("y") && !args[2].equalsIgnoreCase("no")) {
                            sender.sendMessage(worldciv + ChatColor.GRAY + " You must provide a binary answer:" + ChatColor.YELLOW + " YES" +
                                    ChatColor.GRAY + " or " + ChatColor.YELLOW + "NO" + ChatColor.GRAY + ".");
                            return true;
                        }

                        if (fileSystem.getPlayersInYes(possible_id).contains(sender.getName())) {
                            if (args[2].equalsIgnoreCase("yes") || args[2].equalsIgnoreCase("y")) {
                                sender.sendMessage(worldciv + ChatColor.GRAY + " You already voted" + ChatColor.YELLOW + " YES " + ChatColor.GRAY + "for this poll.");
                                return true;
                            } else if (args[2].equalsIgnoreCase("no") || args[2].equalsIgnoreCase("n")) {
                                sender.sendMessage(worldciv + ChatColor.GRAY + " Your vote was changed to" + ChatColor.YELLOW + " NO " + ChatColor.GRAY + "for this poll.");
                                fileSystem.transferVoteYesToNo(sender, possible_id);
                                return true;
                            }
                        } else if (fileSystem.getPlayersInNo(possible_id).contains(sender.getName())) {
                            if (args[2].equalsIgnoreCase("yes") || args[2].equalsIgnoreCase("y")) {
                                sender.sendMessage(worldciv + ChatColor.GRAY + " Your vote was changed to" + ChatColor.YELLOW + " YES " + ChatColor.GRAY + "for this poll.");
                                fileSystem.transferVoteNoToYes(sender, possible_id);
                                return true;
                            } else if (args[2].equalsIgnoreCase("no") || args[2].equalsIgnoreCase("n")) {
                                sender.sendMessage(worldciv + ChatColor.GRAY + " You already voted" + ChatColor.YELLOW + " NO " + ChatColor.GRAY + "for this poll.");
                                return true;
                            }
                        }


                        fileSystem.votePoll(sender, possible_id, args[2]);
                        sender.sendMessage(worldciv + ChatColor.GRAY + " You have voted " + ChatColor.YELLOW + args[2].toUpperCase() + ChatColor.GRAY + " for this poll.");

                        return true;
                    }

                    sender.sendMessage(worldciv + ChatColor.GRAY + " To vote a subject: " + ChatColor.YELLOW + "/polls vote <subject> <yes/no>" + ChatColor.GRAY + ".");
                    //todo
                    return true;
                case "refresh":
                case "reload":

                    try {
                        polls_banlist_yml.save(polls_banlist);
                        polls_reportlist_yml.save(polls_reportlist);

                        for (File file : fileSystem.allFiles(pollsdata_folder)) {
                            YamlConfiguration x = YamlConfiguration.loadConfiguration(file);
                            x.save(file);
                        }
                        sender.sendMessage(worldciv + ChatColor.GREEN + " The polls were refreshed.");
                    } catch (Exception e) {
                        sender.sendMessage(worldciv + ChatColor.RED + " The polls failed to refresh.");
                    }
                    //todo refresh config
                    return true;
                case "show":

                    if (args.length == 2) {
                        if (!getPollIds(pollsdata_folder).contains(args[1])) {
                            sender.sendMessage(worldciv + ChatColor.GRAY + " The ID you provided was not valid.");
                            return true;
                        }

                        File file = getPollByID(pollsdata_folder, args[1]);
                        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);

                        sender.sendMessage(maintop);
                        sender.sendMessage(ChatColor.BLUE + "Poll ID: " + ChatColor.YELLOW + args[1]);
                        sender.sendMessage(ChatColor.BLUE + "Subject: " + ChatColor.YELLOW + yaml.get(args[1] + ".Subject"));
                        sender.sendMessage(ChatColor.BLUE + "Publisher: " + ChatColor.YELLOW + yaml.get(args[1] + ".Author"));
                        sender.sendMessage(ChatColor.BLUE + "Upload Date: " + ChatColor.YELLOW + yaml.get(args[1] + ".Upload Date"));

                        int voteno = fileSystem.getPlayersInNo(args[1]).size();
                        int voteyes = fileSystem.getPlayersInYes(args[1]).size();
                        sender.sendMessage(" ");
                        sender.sendMessage(ChatColor.BLUE + "[Votes] " + ChatColor.GREEN + "Yes: " + ChatColor.YELLOW + voteyes + " "
                                + ChatColor.RED + "No: " + ChatColor.YELLOW + voteno);
                        sender.sendMessage(" ");

                        //7 spaces
                        new FancyMessage("[Vote-Yes]").color(ChatColor.GREEN).tooltip(ChatColor.GREEN + "Click me to vote \"yes!\"").command("/polls vote " + args[1] + " yes").then("     ").then("[Report]")
                                .color(ChatColor.BLUE).tooltip(ChatColor.GRAY + "Click me to report this poll.").command("/polls report " + args[1]).then("     ").then("[Refresh]").color(ChatColor.BLUE).tooltip(ChatColor.GRAY + " Click me to refresh!").command("/polls show " + args[1]).then("     ").then("[Vote-No]")
                                .color(ChatColor.RED).tooltip(ChatColor.RED + "Click me to vote \"no!\"").command("/polls vote " + args[1] + " no").send(sender);
                        if (sender.hasPermission("worldciv.mod") || sender.hasPermission("worldciv.admin")) {
                            sender.sendMessage(" ");
                            sender.sendMessage(ChatColor.RED + "Moderators+ can see below:");
                            sender.sendMessage(" ");

                            new FancyMessage("  [Remove]").color(ChatColor.DARK_RED).tooltip(ChatColor.GRAY + "Click me to remove this poll.").command("/polls remove " + args[1]).
                                    then("  ").then("[Resolve]").color(ChatColor.GREEN).tooltip(ChatColor.GRAY + "Click me to resolve this poll").command("/polls resolve " + args[1]).then("  ").
                                    then("[Ban-User]").color(ChatColor.DARK_RED).tooltip(ChatColor.GRAY + "Click me to ban this player").command("/polls ban " + yaml.get(args[1] + ".Author")).send(sender);
                        }
return true;
                    }

                    sender.sendMessage(worldciv + ChatColor.GRAY + " Display more about a poll: " + ChatColor.YELLOW + "/polls show <subject>" + ChatColor.GRAY + ".");
                    return true;
                default:
                    sender.sendMessage(worldciv + ChatColor.GRAY + " Invalid parameter. Use " + ChatColor.YELLOW + "/polls" + ChatColor.GRAY + " for more help");
                    return true;
            }
        }
        return true;
    }

    public String getMessage(String[] args) {
        List<String> oldlist = Arrays.asList(args); //make to list
        List<String> list = new ArrayList<>();
        list.addAll(oldlist);

        list.remove(0); //remove first index which should be [create [returns-all-these-args]
        int size = list.size(); //size
        int index = 0; //index
        String finalmessage = "";

        for (String arg : list) {

            if (index == 0) {
                finalmessage += arg;
            } else {
                finalmessage += " " + arg;
            }
            index++;

        }
        return finalmessage;

    }

    public static String getPollId(File file) {
        String file_name = file.getName(); //returns file name
        String[] broken_file_name = file_name.split("-"); //split it into the first category
        String file_id = broken_file_name[0].substring(2); //returns id.
        return file_id;
    }

    public static File getPollByID(File folder, String id) {
        for (File file : fileSystem.allFiles(folder)) {
            String file_name = file.getName();
            String[] broken_file_name = file_name.split("-"); //split it into the first category
            String file_id = broken_file_name[0].substring(2); //returns id.

            if (id.equalsIgnoreCase(file_id)) {
                return file;
            }
        }
        return null;
    }

    public static List<String> getPollIds(File folder) {

        List<String> list = new ArrayList<>();

        for (File file : fileSystem.allFiles(folder)) {
            String id = getPollId(file);
            list.add(id);
        }
        return list;

    }


}
