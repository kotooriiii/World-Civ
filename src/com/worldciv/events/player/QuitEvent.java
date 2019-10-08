package com.worldciv.events.player;

import com.sk89q.worldguard.bukkit.event.entity.SpawnEntityEvent;
import com.worldciv.dungeons.Dungeon;
import com.worldciv.events.chat.ChatChannelEvent;
import com.worldciv.parties.Party;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import static com.worldciv.commands.WorldCivCommand.removeLightTutorial;
import static com.worldciv.the60th.Main.getDungeonManager;
import static com.worldciv.utility.utilityArrays.lighttutorial;

public class QuitEvent implements Listener {


    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {

        Player p = e.getPlayer();
        Party party = new Party();

        if(getDungeonManager.getDungeon(p) != null){ //player is in dungeon
            if(party.size(p) == 1){ //cant be zero hes still in. must be 1 to be last player
               Dungeon dungeon = getDungeonManager.getDungeon(p);
               getDungeonManager.removeDungeon(dungeon);
            }
        }
        if(party.isLeader(p)){
            party.removeLeader(p); //TIHS ALSO REMOVES PARTY DW!
        }

        if(party.hasParty(p)){
            party.remove(p);
        }

        if(lighttutorial.contains(p)){
removeLightTutorial(p);
        }



    }
}
