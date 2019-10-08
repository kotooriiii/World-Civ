package com.worldciv.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static com.worldciv.utility.utilityMultimaps.partyrequest;
import static com.worldciv.utility.utilityStrings.*;


public class PartyCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender robotsender, Command cmd, String alias, String[] args) {

        if (!(robotsender instanceof Player)) {
            robotsender.sendMessage(ChatColor.RED + "Silly console! You can't join parties, that's unfair!");
            return true;
        }

        Player sender = (Player) robotsender;
        com.worldciv.parties.Party party = new com.worldciv.parties.Party();


        if (cmd.getName().equalsIgnoreCase("party") || cmd.getName().equalsIgnoreCase("p")) {
            if (args.length == 0 ) {

                if (!party.hasParty(sender)) { //this if statement checks if youre in a party /// !p.hasParty()
                    sender.sendMessage(maintop);
                    sender.sendMessage(ChatColor.YELLOW + "/party create" + ChatColor.GRAY + ": Create a party to challenge a dungeon.");
                    sender.sendMessage(ChatColor.YELLOW + "/party show" + ChatColor.GRAY + ": Displays your party members.");
                    sender.sendMessage(ChatColor.YELLOW + "/party leave" + ChatColor.GRAY + ": Abandon your current party.");
                    sender.sendMessage(ChatColor.YELLOW + "/party disband" + ChatColor.GRAY + ": Disbands the entire party.");
                    sender.sendMessage(ChatColor.YELLOW + "/party invite <player>" + ChatColor.GRAY + ": Invite a challenger to your party.");
                    sender.sendMessage(ChatColor.YELLOW + "/party kick <player>" + ChatColor.GRAY + ": Kick the coward from your party.");
                    sender.sendMessage(ChatColor.YELLOW + "/party accept <player>" + ChatColor.GRAY + ": Join a dungeon-raiding party.");
                    sender.sendMessage(ChatColor.YELLOW + "/party deny <player>" + ChatColor.GRAY + ": Refuse to join a feeble party.");
                    sender.sendMessage(ChatColor.YELLOW + "/party block <player>" + ChatColor.GRAY + ": Block/Unblock invitations from players.");
                    sender.sendMessage(ChatColor.YELLOW + "/party help" + ChatColor.GRAY + ": Displays this help page.");
                    sender.sendMessage(mainbot);
                    return true;
                } else {

                 //GUI
                    sender.sendMessage("in dev for GUI");

                    return true;
                }

            }

            switch (args[0]) {
                case "i":
                case "inv":
                case "invite":

                    if (args.length >= 2) { //If /party invite <this argument or more>

                        if (!party.hasParty(sender)) {
                            sender.sendMessage(worldciv + ChatColor.GRAY + " To invite players you must create your own party!");
                            return true;
                        }

                        Player receiver = Bukkit.getServer().getPlayer(args[1]); //DECLARE receiver
                        long radius = Math.round(sender.getLocation().distance(receiver.getLocation()));

                        if(!party.isLeader(sender)) {
                            sender.sendMessage(worldciv + ChatColor.GRAY + " You are not the leader of this party.");
                            return true;
                        }

                        if (party.size(sender) >= 4) {
                            sender.sendMessage(worldciv + ChatColor.GRAY + " You have reached max party size! You can't invite more challengers!");
                            return true;
                        }

                        if (receiver == null || !receiver.isOnline()) { //PLAYER DOES NOT EXIST
                            sender.sendMessage(worldciv + ChatColor.GRAY + " We tried our hardest... but we couldn't find this player.");
                            return true;
                        }

                        if (party.hasSameParty(sender, receiver)) {
                            sender.sendMessage(worldciv + ChatColor.GRAY + " This player is already in your party!");
                            return true;
                        }

                        if (party.hasParty(receiver)) {
                            sender.sendMessage(worldciv + ChatColor.GRAY + " Unfortunately, " + ChatColor.AQUA + receiver.getName() + ChatColor.GRAY + ", is already in another party.");
                            return true;
                        }

                        if(party.isBlocked(receiver, sender)){
                            sender.sendMessage(worldciv + ChatColor.AQUA + " " + receiver.getName() + ChatColor.GRAY + " has blocked you from inviting him.");
                            return true;
                        }

                        if (receiver == sender) { //same player
                            sender.sendMessage(worldciv + ChatColor.GRAY + " There has to be someone else you can invite. You can't invite yourself...");
                            return true;
                        }


                        if (party.isInvited(sender, receiver)) { //if the player you are inviting is the same player
                            sender.sendMessage(worldciv + " " + ChatColor.YELLOW + receiver.getName() + ChatColor.GRAY + " still has an open invitation from you.");
                            return true;
                        }


                        if (!(radius <= 6)){
                            sender.sendMessage(worldciv + ChatColor.GRAY + " You must be nearby " +ChatColor.AQUA + receiver.getName() + ChatColor.GRAY +" to invite them.");
                            return true;
                        }

                        party.invite(sender, receiver); //invite from to receiver

                        return true;

                    }
                    sender.sendMessage(worldciv + ChatColor.GRAY + " We need more arguments! Use" + ChatColor.YELLOW + " /party invite <player>");
                    return true;

                case "a":
                case "accept":

                    if (args.length >= 2) { //If /party accept <this argument or more>

                        Player leader = Bukkit.getServer().getPlayer(args[1]);

                        if (leader == null || !leader.isOnline()) {
                            sender.sendMessage(worldciv + ChatColor.GRAY + " We tried our hardest... but we couldn't find this player.");
                            return true;
                        }

                        if (party.hasParty(sender)) {
                            sender.sendMessage(worldciv + ChatColor.GRAY + " You're already in a party!");
                            return true;
                        }

                        if (sender == leader) {
                            sender.sendMessage(worldciv + ChatColor.GRAY + " You can't join your own party... Join another!");
                            return true;
                        }

                        if (!partyrequest.containsValue(sender.getName()) || partyrequest.containsValue(sender.getName())) { //if person who is doing /party accept is or isnt even have any open invs
                            if (!partyrequest.containsKey(leader.getName())) {
                                sender.sendMessage(worldciv + ChatColor.GRAY + " I can't find any open invitations from " + ChatColor.AQUA + leader.getName() + ChatColor.GRAY + ".");
                                return true;
                            }
                        }


                        if (partyrequest.containsEntry(leader.getName(), sender.getName())) {

                            if (party.hasParty(leader)) {

                                if (party.size(leader) >= 4) {

                                    sender.sendMessage(worldciv + ChatColor.GRAY + " The party is full!");
                                    return true;
                                } else {
                                    partyrequest.remove(leader.getName(), sender.getName());

                                    party.add(sender, leader); //some party sht
                                    return true;
                                }
                            } else {
                                sender.sendMessage(worldciv + ChatColor.GRAY + " The party was disbanded before you joined.");
                            }


                            return true;
                        }


                        return true;

                    }
                    sender.sendMessage(worldciv + ChatColor.GRAY + " We need more arguments! Use" + ChatColor.YELLOW + " /party accept <player>");
                    return true;

                case "d":
                case "deny":

                    if (args.length >= 2) { //If /party deny <this argument or more>

                        Player leader = Bukkit.getServer().getPlayer(args[1]);

                        if (leader == null || !leader.isOnline()) {
                            sender.sendMessage(worldciv + ChatColor.GRAY + " We tried our hardest... but we couldn't find this player.");
                            return true;
                        }

                        if (party.hasParty(sender)) {
                            sender.sendMessage(worldciv + ChatColor.GRAY + " You're already in a party!");
                            return true;
                        }

                        if (sender == leader) {
                            sender.sendMessage(worldciv + ChatColor.GRAY + " You can't deny your own party... Deny another!");
                            return true;
                        }

                        if (!partyrequest.containsValue(sender.getName()) || partyrequest.containsValue(sender.getName())) { //if person who is doing /party accept is or isnt even have any open invs
                            if (!partyrequest.containsKey(leader.getName())) {
                                sender.sendMessage(worldciv + ChatColor.GRAY + " I can't find any open invitations from " + ChatColor.AQUA + leader.getName() + ChatColor.GRAY + ".");
                                return true;
                            }
                        }


                        if (partyrequest.containsEntry(leader.getName(), sender.getName())) {

                            sender.sendMessage(worldciv + ChatColor.GRAY + " You have refused to join " + ChatColor.AQUA + leader.getName() + "'s" + ChatColor.GRAY
                                    + " party!");

                            leader.sendMessage(worldciv + " " + ChatColor.AQUA + sender.getName() + ChatColor.GRAY + " has refused to join the party!");

                            partyrequest.remove(leader.getName(), sender.getName());

                            return true;
                        }


                        return true;

                    }
                    sender.sendMessage(worldciv + ChatColor.GRAY + " We need more arguments! Use" + ChatColor.YELLOW + " /party deny <player>");
                    return true;

                case "c":
                case "create":

                    if (args.length >= 2) { //If /party create <this argument or more>

                        sender.sendMessage(worldciv + ChatColor.GRAY + " There's too many arguments! Use" + ChatColor.YELLOW + " /party create");

                        return true;

                    }

                    if (party.hasParty(sender)) {
                        sender.sendMessage(worldciv + ChatColor.GRAY + " You're already in a party!");
                        return true;
                    }

                    party.create(sender);
                    return true;

                case "l":
                case "leave":


                    if (args.length >= 2) { //If /party create <this argument or more>

                        sender.sendMessage(worldciv + ChatColor.GRAY + " There's too many arguments! Use" + ChatColor.YELLOW + " /party leave");

                        return true;
                    }

                    if(!party.hasParty(sender)){
                        sender.sendMessage(worldciv + ChatColor.GRAY + " You're not in a party!");
                        return true;
                    }

                    if (party.isLeader(sender)) {
                        sender.sendMessage(worldciv + ChatColor.GRAY + " You are the leader of this party! You must assign leadership to someone else with "
                                + ChatColor.YELLOW + " /party leader <player>" + ChatColor.GRAY + " or disband the party with " + ChatColor.YELLOW + " /party disband" +ChatColor.GRAY+".");
                        return true;
                    }

                    party.leave(sender);
                    return true;

                case "b":
                case "block":

                    if (args.length == 2) { //If /party block <thisisargslength2>


                        Player pblock = Bukkit.getServer().getPlayer(args[1]); //DECLARE receiver

                        if (pblock == null || !pblock.isOnline()) { //PLAYER DOES NOT EXIST
                            sender.sendMessage(worldciv + ChatColor.GRAY + " We tried our hardest... but we couldn't find this player.");
                            return true;
                        }

                        if (pblock == sender) { //same player
                            sender.sendMessage(worldciv + ChatColor.GRAY + " You can't block yourself!");
                            return true;
                        }

                        if(party.isBlocked(sender, pblock)){
                            sender.sendMessage(worldciv + ChatColor.GRAY + " You have unblocked " + ChatColor.AQUA + pblock.getName() + ChatColor.GRAY + " from sending you party invites.");
                            party.unblock(sender, pblock);
                        } else {
                            sender.sendMessage(worldciv + ChatColor.GRAY + " You have blocked " + ChatColor.AQUA + pblock.getName() + ChatColor.GRAY + " from sending you party invites.");
                            party.block(sender, pblock);
                        }


                        return true;


                    } else if (args.length > 2) {   //< or more>
                        sender.sendMessage(worldciv + ChatColor.GRAY + " There's too many arguments! Use" + ChatColor.YELLOW + " /party block <player>");
                        return true;
                    }

                    sender.sendMessage(worldciv + ChatColor.GRAY + " We need more arguments! Use" + ChatColor.YELLOW + " /party block <player>");
                    return true;

                case "db":
                case "disband":

                    if (args.length >= 2) { //If /party create <this argument or more>

                        sender.sendMessage(worldciv + ChatColor.GRAY + " There's too many arguments! Use" + ChatColor.YELLOW + " /party disband");

                        return true;

                    }

                    if(!party.hasParty(sender)) {
                        sender.sendMessage(worldciv + ChatColor.GRAY + " You're not in a party!");
                        return true;
                    }

                    if(!party.isLeader(sender)){
                        sender.sendMessage(worldciv + ChatColor.GRAY + " You are not the leader of this party.");
                        return true;
                    }


                    party.disband(sender);
                    return true;

                case "k":
                case "kick":
                    if (args.length == 2) { //If /party block <thisisargslength2>


                        Player kicked = Bukkit.getServer().getPlayer(args[1]); //DECLARE receiver



                        if (kicked == null || !kicked.isOnline()) { //PLAYER DOES NOT EXIST
                            sender.sendMessage(worldciv + ChatColor.GRAY + " We tried our hardest... but we couldn't find this player.");
                            return true;
                        }

                        if(!party.isLeader(sender)){
                            sender.sendMessage(worldciv + ChatColor.GRAY + " You are not the leader of this party.");
                            return true;
                        }

                        if(!party.hasParty(sender)){
                            sender.sendMessage(worldciv + ChatColor.GRAY + " You're not in a party!");
                            return true;
                        }

                        if (kicked == sender) { //same player
                            sender.sendMessage(worldciv + ChatColor.GRAY + " You can't kick yourself! You can disband with" + ChatColor.YELLOW + "/party disband");
                            return true;
                        }

                        if (party.hasSameParty(sender, kicked)) {
                            party.kick(sender, kicked);
                            return true;

                        }


                    } else if (args.length > 2) {   //< or more>
                        sender.sendMessage(worldciv + ChatColor.GRAY + " There's too many arguments! Use" + ChatColor.YELLOW + " /party kick <player>");
                        return true;
                    }

                    sender.sendMessage(worldciv + ChatColor.GRAY + " We need more arguments! Use" + ChatColor.YELLOW + " /party kick <player>");
                    return true;

                case "s":
                case "show":

                    if (party.hasParty(sender)) {
                        party.show(sender);

                    } else {
                        sender.sendMessage(worldciv + ChatColor.GRAY + " You're not in a party!");
                    }
                    return true;

                case "leader":
                    if (args.length >= 2) { //If /party accept <this argument or more>

                        Player newleader = Bukkit.getServer().getPlayer(args[1]);

                        if(!party.hasParty(sender)){
                            sender.sendMessage(worldciv + ChatColor.GRAY + " You're not in a party!");
                            return true;
                        }

                        if(!party.isLeader(sender)){
                            sender.sendMessage(worldciv + ChatColor.GRAY + " You are not the leader of this party.");
                            return true;
                        }
                        if (newleader == null || !newleader.isOnline()) {
                            sender.sendMessage(worldciv + ChatColor.GRAY + " We tried our hardest... but we couldn't find this player.");
                            return true;
                        }

                        if(!party.hasSameParty(sender, newleader)){
                            sender.sendMessage(worldciv + ChatColor.GRAY + " You're not in the same party!");
                            return  true;
                        }


                        if (sender == newleader) {
                            sender.sendMessage(worldciv + ChatColor.GRAY + " You're already leader!");
                            return true;
                        }

                        party.setLeader(sender, newleader);


                        return true;

                    }
                    sender.sendMessage(worldciv + ChatColor.GRAY + " We need more arguments! Use" + ChatColor.YELLOW + " /party leader <player>");
                    return true;

                case "h":
                case "help":
                    sender.sendMessage(maintop);
                    sender.sendMessage(ChatColor.YELLOW + "/party create" + ChatColor.GRAY + ": Create a party to challenge a dungeon.");
                    sender.sendMessage(ChatColor.YELLOW + "/party show" + ChatColor.GRAY + ": Displays your party members.");
                    sender.sendMessage(ChatColor.YELLOW + "/party leave" + ChatColor.GRAY + ": Abandon your current party.");
                    sender.sendMessage(ChatColor.YELLOW + "/party disband" + ChatColor.GRAY + ": Disbands the entire party.");
                    sender.sendMessage(ChatColor.YELLOW + "/party invite <player>" + ChatColor.GRAY + ": Invite a challenger to your party.");
                    sender.sendMessage(ChatColor.YELLOW + "/party kick <player>" + ChatColor.GRAY + ": Kick the coward from your party.");
                    sender.sendMessage(ChatColor.YELLOW + "/party accept <player>" + ChatColor.GRAY + ": Join a dungeon-raiding party.");
                    sender.sendMessage(ChatColor.YELLOW + "/party deny <player>" + ChatColor.GRAY + ": Refuse to join a feeble party.");
                    sender.sendMessage(ChatColor.YELLOW + "/party block <player>" + ChatColor.GRAY + ": Block/Unblock invitations from players.");
                    sender.sendMessage(ChatColor.YELLOW + "/party help" + ChatColor.GRAY + ": Displays this help page.");
                    sender.sendMessage(mainbot);
                    return true;


            }

            sender.sendMessage(worldciv + ChatColor.GRAY + " Invalid arguments. Use" + ChatColor.YELLOW + " /party" + ChatColor.GRAY + " or " +
                    ChatColor.YELLOW + "/p help" + " for more help");
            return true;

        }

     return false; //end of command boolean
    }
}
