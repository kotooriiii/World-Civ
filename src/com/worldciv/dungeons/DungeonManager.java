package com.worldciv.dungeons;

import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.worldciv.parties.Party;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

import static com.worldciv.the60th.Main.*;
import static com.worldciv.utility.utilityArrays.dungeonregionlist;
import static com.worldciv.utility.utilityArrays.notreadylist;
import static com.worldciv.utility.utilityMultimaps.partyid;

public class DungeonManager {

    public static ArrayList<Dungeon> activedungeons = new ArrayList<Dungeon>();

    public void addDungeon(Dungeon dungeon) {

        if(!isDungeon(dungeon.getDungeonID())){

            activedungeons.add(dungeon);
            dungeon.teleportToDungeon();
        }
    }

    public void removeDungeon(Dungeon dungeon) {

        if(isDungeon(dungeon.getDungeonID())){

            activedungeons.remove(dungeon);
            dungeon.teleportOutOfDungeon();
        }
    }

    public boolean isDungeon(String dungeonid){

        for(Dungeon dungeon : activedungeons){
            if(dungeon.getDungeonID().equalsIgnoreCase(dungeonid)){
                return true;
            }
        }

        return false;
    }

    public Dungeon getDungeon(String partyid) {

        for (Dungeon dungeon : activedungeons) {
            if (dungeon.getPartyID().equalsIgnoreCase(partyid)) {
                //dungeon list does have a dungeon with that party id.
                return dungeon;
            } else {
                //dungeon list does not have a dungeon with that party id.
            }
        }

        return null;
    }

    public Dungeon getDungeon(Player player){

        for (Dungeon dungeon : activedungeons) {

            String party_id = partyid.get(player.getName()).toString();
            party_id =party_id.replace("[", "");
            party_id =party_id.replace("]", "");

            if (dungeon.getPartyID().equalsIgnoreCase(party_id)) {
                //dungeon list does have a dungeon with that party id.
                return dungeon;
            } else {
                //dungeon list does not have a dungeon with that party id.
            }
        }

        return null;
    }



    public ArrayList<String> getAllDungeons() {

        dungeonregionlist.removeAll(dungeonregionlist);

        for (Object regionname : getWorldGuard().getRegionManager(Bukkit.getWorld("world")).getRegions().keySet().toArray()) { //CHANGE REGION WORLDS //TODO change this world to dungeonworld

            if (getWorldGuard().getRegionManager(Bukkit.getWorld("world")).getRegion(regionname.toString()).getFlag(dungeon_region) == StateFlag.State.ALLOW) {
                if (!dungeonregionlist.contains(regionname.toString())) {
                    dungeonregionlist.add(regionname.toString());
                }
            }
        }

        Collections.sort(dungeonregionlist);

        return dungeonregionlist;

    }

    public Set<ProtectedRegion> getCurrentRegion(Player player) {

        return getWorldGuard().getRegionManager(player.getWorld()).getApplicableRegions(player.getLocation()).getRegions();
    }


    public HashMap<String, String> getAllActiveDungeons() {

        HashMap<String, String> hashactives = new HashMap<String, String>();

        for (Dungeon dungeon : activedungeons) { //iterate through all ids in the map

                hashactives.put(dungeon.getDungeonID(), dungeon.getPlayers(dungeon.getPartyID()).toString());

            }

        return  hashactives; //returns nameofdungeon and playersinside
    }

    public boolean allReady(Player player){
        Party party = new Party();
       List<String> player_list=  party.getPlayers(player);
       int players_ready = 0;
       for(String player_string : player_list) {
           Player p = Bukkit.getPlayer(player_string);

           if (!notreadylist.contains(p)) {
               players_ready++;
           }
       }
       if(party.size(player) == players_ready){
           return  true;
       } else {
           return false;
       }

    }

    public List<String> getPlayersNotReady(Player player){
        if(allReady(player)){
            return null;
        }
        Party party = new Party();
        List<String> player_list=  party.getPlayers(player);
        List<String> players_not_ready = new ArrayList<String>();
        for(String player_string : player_list) {
            Player p = Bukkit.getPlayer(player_string);

            if (notreadylist.contains(p)) {
                players_not_ready.add(p.getName());
            }
        }

      //  String complete = players_not_ready.toString().replace("]", "").replace("[", "");

return players_not_ready;

    }

    public DungeonManager(){

    }
}
