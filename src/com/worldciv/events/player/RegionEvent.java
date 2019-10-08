package com.worldciv.events.player;

import com.mewin.WGRegionEvents.events.RegionEnterEvent;
import com.mewin.WGRegionEvents.events.RegionLeaveEvent;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.worldciv.dungeons.Dungeon;
import com.worldciv.parties.Party;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Collection;

import static com.worldciv.dungeons.DungeonManager.activedungeons;
import static com.worldciv.the60th.Main.*;
import static com.worldciv.utility.utilityArrays.visionregion;
import static com.worldciv.utility.utilityMultimaps.partyid;
import static com.worldciv.utility.utilityStrings.worldciv;

public class RegionEvent implements Listener {



    @EventHandler
    public void PlayerEnterRegion(RegionEnterEvent e) {

        Player player = e.getPlayer();
        ApplicableRegionSet set = getWorldGuard().getRegionManager(player.getWorld()).getApplicableRegions(player.getLocation());

        if (e.getRegion().getFlag(vision_bypass) == StateFlag.State.ALLOW && e.isCancellable()) {

            visionregion.add(player);
            player.sendMessage(worldciv + ChatColor.GRAY + " You are protected in this region.");
        }

        if (e.getRegion().getFlag(vision_bypass) == StateFlag.State.DENY){
            visionregion.remove(player);
        }

        if(e.getRegion().getFlag(vision_bypass) == null){
            visionregion.remove(player);
        }

        if(e.getRegion().getFlag(dungeon_region) == StateFlag.State.ALLOW && e.isCancellable()){

            Party party = new Party();

            if(!party.hasParty(player) && !player.hasPermission("worldciv.dungeonadmin")){
                player.sendMessage(worldciv + ChatColor.RED + " You can't enter a dungeon without being in a party.");
                e.setCancelled(true);
                return;
            }

            Dungeon dungeon = getDungeonManager.getDungeon(player);

            if(dungeon == null) {
            return;
            }

            player.sendMessage(worldciv + ChatColor.GOLD + " You have entered dungeon " + ChatColor.YELLOW + "'" + e.getRegion().getId() + "'" + ChatColor.GOLD
                    + ", difficulty " + ChatColor.YELLOW + "'" + dungeon.getDifficulty() + "'" +ChatColor.GOLD + ", and party total size " + ChatColor.YELLOW + "'" + party.size(player) + "'" +ChatColor.GOLD + ".");
        }



     /*   if (e.getRegion().getFlag(vision_bypass) == StateFlag.State.DENY && e.getRegion().getFlag(vision_bypass) == null && e.isCancellable()) {

            visionregion.remove(player);

        } */

    }
        @EventHandler
        public void PlayerLeaveRegion (RegionLeaveEvent e){

        Player player = e.getPlayer();

            if (e.getRegion().getFlag(vision_bypass) == StateFlag.State.ALLOW && e.isCancellable()) {

                visionregion.remove(player);

                player.sendMessage(worldciv + ChatColor.GRAY + " You have abandoned the protected region.");


            }

            if(e.getRegion().getFlag(dungeon_region) == StateFlag.State.ALLOW && e.isCancellable()) {

                Collection<String> party_id = partyid.get(player.getName());
                String party_uuid = party_id.toString();
                party_uuid = party_uuid.replace("[", "");
                party_uuid = party_uuid.replace("]", "");

                Dungeon dungeon = getDungeonManager.getDungeon(party_uuid);

                activedungeons.remove(dungeon);

                player.sendMessage(worldciv + ChatColor.GOLD + " You have exited the dungeon.");

            }


        }


    }

