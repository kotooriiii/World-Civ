package com.worldciv.parties;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

import static com.worldciv.the60th.Main.plugin;
import static com.worldciv.utility.utilityMultimaps.*;
import static com.worldciv.utility.utilityStrings.worldciv;

public class Party {

    public void create(final Player p) {  // This function creates the party for the sender.

        UUID uuid = UUID.randomUUID(); //RANDOM UUID
        String stringuuid = String.valueOf(uuid); //CREATE IT TO STRING

        p.sendMessage(worldciv + ChatColor.GRAY + " You have successfully created a party! Be sure to invite other players with" + ChatColor.YELLOW + " /party invite <player>"); //MESSAGE SENDING
        partyid.put(p.getName(), stringuuid); //add the sender to the partyid group. playername and uuid
        partyleaderid.put(p.getName(), stringuuid); //add the sender to the partyidleader group. playename and uuid

    }

    public void leave(final Player sender) { //This function triggers when player decides to leave his party!

        Multimap<String, String> inversepartyid = Multimaps.invertFrom(partyid, ArrayListMultimap.<String, String>create()); //transforms partyid Key Value to Value Key. this way we can get values

        Collection collectionuuid = partyid.get(sender.getName()); //gets uuid in form of collection
        String listuuid = collectionuuid.toString(); //turns uuid to string i.e: "[uuid]"
        listuuid = listuuid.replace("[", ""); //remove brackets as the ruin data!
        listuuid = listuuid.replace("]", "");

        Collection<String> collectionplayers = inversepartyid.get(listuuid); //this returns all the players in the party in a collection

        //We don't need to remove brackets as this time we are iterating through each and we need the brackets to display it's in a collection!

        for (String collection : collectionplayers) {   //Iterate through all members

            Player members = Bukkit.getServer().getPlayer(collection); //create players
            if(members != sender) //not person leaving
            members.sendMessage(worldciv + " " + ChatColor.AQUA + sender.getName() + ChatColor.GRAY + " has left your party!");

        }


        sender.sendMessage(worldciv + ChatColor.GRAY + " You have bid farewell to your companions!"); //leaver gets this essage only
        partyid.removeAll(sender.getName()); //removes entire data of leaver in party
        return;
    }

    public void disband(final Player sender) { //This function disbands the party.

        Multimap<String, String> inversepartyid = Multimaps.invertFrom(partyid, ArrayListMultimap.<String, String>create()); //same stuff mentioned above

        Collection collectionuuid = partyid.get(sender.getName());
        String listuuid = collectionuuid.toString();
        listuuid = listuuid.replace("[", "");
        listuuid = listuuid.replace("]", "");

        Collection<String> collectionplayers = inversepartyid.get(listuuid);

        for (String collection : collectionplayers) {

            Player members = Bukkit.getServer().getPlayer(collection);

            if (members != sender) {
                members.sendMessage(worldciv + ChatColor.GRAY + " The party you were in was disbanded!");
            }
                partyid.removeAll(collection); //removes all players in party with no data!


        }

        partyleaderid.removeAll(sender.getName()); //removes leader data from party leader!
        sender.sendMessage(worldciv + ChatColor.GRAY + " You have disbanded the party!"); //sends this message to leader

        return;

    }


    public void add(final Player sender, final Player leader) {


        sender.sendMessage(worldciv + ChatColor.GRAY + " You have joined " + ChatColor.AQUA + leader.getName() + ChatColor.AQUA + "'s" + ChatColor.GRAY
                + " party!");


        Multimap<String, String> inversepartyid = Multimaps.invertFrom(partyid, ArrayListMultimap.<String, String>create());

        Collection collectionuuid = partyid.get(leader.getName()); //gets uuid
        String stringuuid = collectionuuid.toString();
        stringuuid = stringuuid.replace("[", "");
        stringuuid = stringuuid.replace("]", "");

        Collection<String> collectionplayers = inversepartyid.get(stringuuid); //this returns all the players in the party in a collection

        for (String collection : collectionplayers) {

            Player members = Bukkit.getServer().getPlayer(collection);
            members.sendMessage(worldciv + ChatColor.GRAY + " A new challenger, " + ChatColor.AQUA + sender.getName() + ChatColor.GRAY + ", has joined the party!");

            partyid.removeAll(sender.getName());

        }


        partyid.put(sender.getName(), stringuuid);

        System.out.print(partyid.entries().toString()); // for testing purposes
        System.out.print(inversepartyid.entries().toString());


    }

    public void block(Player sender, Player pblock) {

        blockedplayers.put(sender.getName(), pblock.getName());
    }

    public void unblock(Player sender, Player pblock){

        blockedplayers.remove(sender.getName(), pblock.getName());
    }

    public boolean isBlocked(Player sender, Player leader){
        if (blockedplayers.containsEntry(sender.getName(), leader.getName())) {//contains entry of blocked player


            return true;
        } else {

            return false;
        }
    }

    public void invite(Player sender, Player receiver) {

        sender.sendMessage(worldciv + ChatColor.GRAY + " You have sent an invitation to " + ChatColor.AQUA + receiver.getName() + ChatColor.GRAY
                + " to join your party! They have 15 seconds to respond to your message.");

        receiver.sendMessage(worldciv + ChatColor.GRAY + " You have received an invitation from " + ChatColor.AQUA + sender.getName()
                + ChatColor.GRAY + " to join their party! You have 15 seconds to confirm with " + ChatColor.YELLOW + "/party accept " + sender.getName() + ChatColor.GRAY + " or" + ChatColor.YELLOW + " /party deny " + sender.getName());


        partyrequest.put(sender.getName(), receiver.getName());

        new BukkitRunnable() {
            int x = 0;

            public void run() {

                if (!partyrequest.containsKey(sender.getName())) {
                    if (!partyrequest.containsValue(receiver.getName()))
                        cancel();
                    return;
                }

                if (x == 15) {
                    sender.sendMessage(worldciv + ChatColor.GRAY + " The invitation for " + ChatColor.AQUA + receiver.getName() + ChatColor.GRAY + " has expired.");
                    receiver.sendMessage(worldciv + ChatColor.GRAY + " The invitation from " + ChatColor.AQUA + sender.getName() + ChatColor.GRAY + " has expired.");
                    partyrequest.remove(sender.getName(), receiver.getName());
                    cancel();
                    return;
                }
                x++;
            }
        }.runTaskTimer(plugin, 0, 20);


    }


    public boolean hasParty(Player sender) {
        if (partyid.containsKey(sender.getName())) {

            return true;
        } else {

            return false;

        }
    }



    public String getPartyID(Player sender){

        Collection collectionuuid = partyid.get(sender.getName());
        String stringuuid = collectionuuid.toString();
        stringuuid = stringuuid.replace("[", "");
        stringuuid = stringuuid.replace("]", "");

        return stringuuid;
    }

    public boolean hasSameParty(Player sender, Player receiver) {

        Collection collectionuuid = partyid.get(sender.getName());
        String stringuuid = collectionuuid.toString();
        stringuuid = stringuuid.replace("[", "");
        stringuuid = stringuuid.replace("]", "");


        if (partyid.containsEntry(receiver.getName(), stringuuid)) {

            return true;
        } else {

            return false;

        }
    }

    public int size(Player sender) {

        Multimap<String, String> inversepartyid = Multimaps.invertFrom(partyid, ArrayListMultimap.<String, String>create());

        Collection collectionuuid = partyid.get(sender.getName());
        String stringuuid = collectionuuid.toString();
        stringuuid = stringuuid.replace("[", "");
        stringuuid = stringuuid.replace("]", "");

        return inversepartyid.get(stringuuid).size();
    }

    public boolean isInvited(Player sender, Player receiver) {

        if (partyrequest.containsEntry(sender.getName(), receiver.getName())) {
            return true;

        }
        return false;

    }

    public boolean isLeader(Player leader){

        if (partyleaderid.containsKey(leader.getName())) {


            return true;
        }
        return  false;
    }

    public void kick(Player sender, Player kicked){

        Multimap<String, String> inversepartyid = Multimaps.invertFrom(partyid, ArrayListMultimap.<String, String>create());

        Collection collectionuuid = partyid.get(sender.getName());
        String listuuid = collectionuuid.toString();
        listuuid = listuuid.replace("[", "");
        listuuid = listuuid.replace("]", "");

        Collection<String> collectionplayers = inversepartyid.get(listuuid); //this returns all the players in the party in a collection

        for (String collection : collectionplayers) {

            Player members = Bukkit.getServer().getPlayer(collection);

            if(members != kicked) {
                members.sendMessage(worldciv + ChatColor.AQUA + " " + kicked.getName() + ChatColor.GRAY + " was kicked from the party!");

            }

        }

        kicked.sendMessage(worldciv + ChatColor.GRAY + " You were kicked from the party!");

        partyid.removeAll(kicked.getName());

        System.out.print(partyid.entries());

    }

    public void show(Player sender){

        Multimap<String, String> inversepartyid = Multimaps.invertFrom(partyid, ArrayListMultimap.<String, String>create());

        Collection collectionuuid = partyid.get(sender.getName());
        String listuuid = collectionuuid.toString();
        listuuid = listuuid.replace("[", "");
        listuuid = listuuid.replace("]", "");

        Collection<String> collectionplayers = inversepartyid.get(listuuid); //this returns all the players in the party in a collection
        String stringplayers = collectionplayers.toString();
        stringplayers = stringplayers.replace("[", "");
        stringplayers = stringplayers.replace("]", "");

        sender.sendMessage(worldciv + ChatColor.GRAY + " These are your teammates: " + ChatColor.AQUA + stringplayers + ".");
    }

    public void setLeader(Player oldleader, Player newleader){

        Multimap<String, String> inversepartyid = Multimaps.invertFrom(partyid, ArrayListMultimap.<String, String>create());

        Collection collectionuuid = partyid.get(oldleader.getName()); //gets uuid
        String listuuid = collectionuuid.toString();
        listuuid = listuuid.replace("[", "");
        listuuid = listuuid.replace("]", ""); //uuid w/o brackies

        Collection<String> collectionplayers = inversepartyid.get(listuuid); //this returns all the players in the party in a collection

        for (String collection : collectionplayers) {

            Player members = Bukkit.getServer().getPlayer(collection);

            if((members != newleader) && (members != oldleader)) {
                members.sendMessage(worldciv + ChatColor.AQUA + " " + newleader.getName() + ChatColor.GRAY + " has become the leader of the party!");

            }

        }

        partyleaderid.put(newleader.getName(), listuuid);
        newleader.sendMessage(worldciv + ChatColor.GRAY + " You have become the new leader of the party!");
        partyleaderid.remove(oldleader.getName(), listuuid);
        oldleader.sendMessage(worldciv + ChatColor.GRAY + " You have transferred leadership of the party to " + ChatColor.AQUA + newleader.getName() + ChatColor.GRAY + ".");

    }

    public void remove(Player p){
        Multimap<String, String> inversepartyid = Multimaps.invertFrom(partyid, ArrayListMultimap.<String, String>create());

        Collection collectionuuid = partyid.get(p.getName());
        String listuuid = collectionuuid.toString();
        listuuid = listuuid.replace("[", "");
        listuuid = listuuid.replace("]", "");

        Collection<String> collectionplayers = inversepartyid.get(listuuid); //this returns all the players in the party in a collection

        for (String collection : collectionplayers) {

            Player members = Bukkit.getServer().getPlayer(collection);

            if(members != p) {
                members.sendMessage(worldciv + ChatColor.AQUA + " " + p.getName() + ChatColor.GRAY + " has been removed from the party as a result of leaving the server!");

            }

        }

        partyid.removeAll(p.getName());
        System.out.print(partyid.entries().toString());
    }

    public List<String> getPlayers(Player player){

        Multimap<String, String> inversepartyid = Multimaps.invertFrom(partyid, ArrayListMultimap.<String, String>create());

        Collection collectionuuid = partyid.get(player.getName());
        String listuuid = collectionuuid.toString();
        listuuid = listuuid.replace("[", "");
        listuuid = listuuid.replace("]", "");

        Collection<String> collectionplayers = inversepartyid.get(listuuid); //this returns all the players in the party in a collection
        String stringplayers = collectionplayers.toString();

        stringplayers = stringplayers.replace("[", "");
        stringplayers = stringplayers.replace("]", "");

        List<String> arrayplayers = Arrays.asList(stringplayers.split(", "));

        return arrayplayers;

    }

    public void sendToParty(Player memberofparty, String msg){

        for (String player_string : getPlayers(memberofparty)){
            Player player = Bukkit.getPlayer(player_string);
            player.sendMessage(msg);
        }

    }

    public boolean isAllNear(Player source_player, double distance) {

        for (String teammate_string : getPlayers(source_player)) {

            Player teammate_player = Bukkit.getServer().getPlayer(teammate_string);

            if (teammate_player != source_player) {

                long radius = Math.round(source_player.getLocation().distance(teammate_player.getLocation()));

                if (radius <= distance) {
                    //Teammate is within the distance parameter.
                } else {
                    return false;
                }
            }

        }
        return true;

    }


    public Multimap<String, String> getWhoNotNear(Player source_player, double distance) {

        Multimap<String, String> playersnotnear = HashMultimap.create();

        for (String teammate_string : getPlayers(source_player)) {

            Player teammate_player = Bukkit.getServer().getPlayer(teammate_string);

            if (teammate_player != source_player) {

                double radius = Math.round(source_player.getLocation().distance(teammate_player.getLocation()));

                if (radius <= distance) {
                    playersnotnear.put(teammate_string, "Acceptable Range");
                } else {
                    double quickmaths = radius-distance;
                    playersnotnear.put(teammate_string, String.valueOf(quickmaths));
                }
            }

        }


        return playersnotnear;


    }

    public void removeLeader(Player p){

        Collection collectionuuid = partyid.get(p.getName()); //gets uuid
        String listuuid = collectionuuid.toString();
        listuuid = listuuid.replace("[", "");
        listuuid = listuuid.replace("]", ""); //uuid w/o brackies

        partyid.removeAll(p.getName());
        partyleaderid.removeAll(p.getName());

        Multimap<String, String> inversepartyid = Multimaps.invertFrom(partyid, ArrayListMultimap.<String, String>create());

        Collection<String> collectionplayers = inversepartyid.get(listuuid); //this returns all the players in the party in a collection

        if(collectionplayers.size() == 0 || collectionplayers.isEmpty()){


            return;
        }

        int rnd = new Random().nextInt(collectionplayers.size());

        Object randomobject = collectionplayers.toArray()[rnd];
        Player randomplayer = Bukkit.getServer().getPlayer(randomobject.toString());

        System.out.print(partyid.entries().toString());

        for (String collection : collectionplayers) {

            Player members = Bukkit.getServer().getPlayer(collection);

            if(members != randomplayer) {
                members.sendMessage(worldciv + ChatColor.AQUA + " " + randomplayer.getName() + ChatColor.GRAY + " has become the leader of the party as a result of the old setLeader leaving the server!");

            }

        }

        partyleaderid.put(randomplayer.getName(), listuuid);
        randomplayer.sendMessage(worldciv + ChatColor.GRAY + " You have become the new leader of the party as a result of the old leader leaving the server!");

    }


}






