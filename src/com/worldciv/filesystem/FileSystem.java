package com.worldciv.filesystem;

import com.worldciv.commands.PollCommand;
import com.worldciv.dungeons.DungeonMob;
import com.worldciv.the60th.Main;
import com.worldciv.utility.ItemType;
import com.worldciv.utility.Tier;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import static com.worldciv.dungeons.DungeonManager.activedungeons;
import static com.worldciv.the60th.Main.logger;
import static com.worldciv.the60th.Main.plugin;
import static com.worldciv.utility.utilityStrings.worldciv;

public class FileSystem {
    File dungeons_folder;
    File dungeons_file;

    File polls_folder;
    File pollsdata_folder;

    File polls_banlist;
    File polls_reportlist;

    File toggle_folder;
    File toggle_file;

    File channels_folder;
    File channels_file;

    File blocks_folder;
    File blocks_file;

    YamlConfiguration dungeons_yml;

    public FileSystem(){
        File custom_items_folder = new File(Bukkit.getPluginManager().getPlugin("WorldCivMaster").getDataFolder()+"/Custom_Items");

        dungeons_folder = new File(Bukkit.getPluginManager().getPlugin("WorldCivMaster").getDataFolder()+"/dungeons");
        dungeons_file = new File(dungeons_folder, "dungeons.yml");

        blocks_folder = new File(Main.plugin.getDataFolder() + "/blocks");
        blocks_file = new File(blocks_folder, "blocks.yml");

        polls_folder = new File(Bukkit.getPluginManager().getPlugin("WorldCivMaster").getDataFolder() + "/polls");
        pollsdata_folder = new File(Bukkit.getPluginManager().getPlugin("WorldCivMaster").getDataFolder() + "/polls/pollsdata");
        polls_banlist = new File(polls_folder, "banlist.yml");
        polls_reportlist = new File(polls_folder, "reportlist.yml");

        toggle_folder = new File(Main.plugin.getDataFolder() + "/toggle");
        toggle_file = new File(toggle_folder, "toggle.yml");

        channels_folder = new File(Main.plugin.getDataFolder() + "/channels");
        channels_file = new File(channels_folder, "channels.yml");

        if(!custom_items_folder.exists()) {
            custom_items_folder.mkdir();
        }

        if (!polls_folder.exists()) {
            polls_folder.mkdir();
        }

        if(!pollsdata_folder.exists()){
            pollsdata_folder.mkdir();
        }

        if(!dungeons_folder.exists()){
            dungeons_folder.mkdir();
        }

        if(!polls_banlist.exists()){
            try {
                polls_banlist.createNewFile();
                YamlConfiguration yaml = YamlConfiguration.loadConfiguration(polls_banlist);
                yaml.createSection("banlist");
                yaml.save(polls_banlist);
            } catch(Exception e){

            }
        }

        if(!polls_reportlist.exists()){
            try {
                polls_reportlist.createNewFile();
            } catch(Exception e){

            }
        }

        if (!toggle_folder.exists()) {
            toggle_folder.mkdir();
        }

        if (!toggle_file.exists()) {
            try {
                toggle_file.createNewFile();
                toggleSetup(toggle_file);
            } catch (Exception e) {
            }
        }

        if(!channels_folder.exists()){
            channels_folder.mkdir();
        }


        if (!channels_file.exists()) {
            try {
                channels_file.createNewFile();
                channelsSetup(channels_file);
            } catch (Exception e) {
            }
        }

        if(!dungeons_file.exists()){
            saveResource("dungeons.yml", dungeons_folder, false);
        }

        if (!blocks_folder.exists()) {
            blocks_folder.mkdir();
        }

        if (!blocks_file.exists()) {
            try {
                blocks_file.createNewFile();
                blocksSetup(blocks_file);
            } catch (Exception e) {

            }
        }

        dungeons_yml = YamlConfiguration.loadConfiguration(dungeons_file);
    }

    public void saveResource(String resourcePath, File out_to_folder, boolean replace) {
        if (resourcePath != null && !resourcePath.equals("")) {
            resourcePath = resourcePath.replace('\\', '/');
            InputStream in = plugin.getResource(resourcePath);
            if (in == null) {
                throw new IllegalArgumentException("The embedded resource '" + resourcePath + "' cannot be found in " + plugin.getName());
            } else {
                File outFile = new File(out_to_folder, resourcePath);
                int lastIndex = resourcePath.lastIndexOf(47);
                File outDir = new File(out_to_folder, resourcePath.substring(0, lastIndex >= 0 ? lastIndex : 0));
                if (!outDir.exists()) {
                    outDir.mkdirs();
                }

                try {
                    if (outFile.exists() && !replace) {
                        plugin.getLogger().log(Level.WARNING, "Could not save " + outFile.getName() + " to " + outFile + " because " + outFile.getName() + " already exists.");
                    } else {
                        OutputStream out = new FileOutputStream(outFile);
                        byte[] buf = new byte[4096];

                        int len;
                        while((len = in.read(buf)) > 0) {
                            out.write(buf, 0, len);
                        }

                        out.close();
                        in.close();
                    }
                } catch (IOException var10) {
                    plugin.getLogger().log(Level.SEVERE, "Could not save " + outFile.getName() + " to " + outFile, var10);
                }

            }
        } else {
            throw new IllegalArgumentException("ResourcePath cannot be null or empty");
        }
    }

    public void createPoll(Player p, String msg) {
        if (!polls_folder.exists() && !pollsdata_folder.exists()) {
            return;
        }
        int id;

        for (id = 0; id < 200; id++) {

            File file = new File(pollsdata_folder, "ID" + String.valueOf(id) + "-" + p.getUniqueId() + "-" + p.getName() + ".yml");




            if (allFileNames(pollsdata_folder) == null || !file.exists()) { //no list

                YamlConfiguration file_yml = YamlConfiguration.loadConfiguration(file);
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = new Date();

                file_yml.createSection(String.valueOf(id));
                file_yml.createSection(String.valueOf(id) + ".Upload Date");
                file_yml.set(String.valueOf(id) + ".Upload Date", dateFormat.format(date));
                file_yml.createSection(String.valueOf(id) + ".Author");
                file_yml.set(String.valueOf(id) + ".Author", p.getName());
                file_yml.createSection(String.valueOf(id) + ".Subject");
                file_yml.set(String.valueOf(id) + ".Subject", msg);
                file_yml.createSection(String.valueOf(id) + ".VotesYes");
                file_yml.createSection(String.valueOf(id) + ".VotesNo");

                try{
                    file_yml.save(file);
                }catch(Exception e){

                }
                break;
            }
        }

    }

    public boolean votePoll(Player p, String id, String yes_no) {
        if (!polls_folder.exists() && !pollsdata_folder.exists()) {
            return false;
        }

        File file = PollCommand.getPollByID(pollsdata_folder, id);
        YamlConfiguration file_yml = YamlConfiguration.loadConfiguration(file);

        if (file_yml.get(String.valueOf(id) + ".VotesYes") == null) {

            Bukkit.broadcastMessage("y1");

            if (yes_no.equalsIgnoreCase("yes") || yes_no.equalsIgnoreCase("y")) {
                Bukkit.broadcastMessage("y2");
                List<String> list = new ArrayList<>();
                list.add(p.getName());
                file_yml.set(String.valueOf(id) + ".VotesYes", list);
                try {
                    file_yml.save(file);
                    return true;
                } catch (Exception e) {
                    return false;
                }
            }
        }
        if (file_yml.get(String.valueOf(id) + ".VotesNo") == null) {
            if (yes_no.equalsIgnoreCase("no") || yes_no.equalsIgnoreCase("n")) {
                List<String> list = new ArrayList<>();
                list.add(p.getName());
                file_yml.set(String.valueOf(id) + ".VotesNo", list);

                try {
                    file_yml.save(file);
                    return true;
                } catch (Exception e) {
                    return false;
                }
            }
        }


        List<String> voteyes = file_yml.getStringList(String.valueOf(id) + ".VotesYes");
        List<String> voteno = file_yml.getStringList(String.valueOf(id) + ".VotesNo");

        if (yes_no.equalsIgnoreCase("yes") || yes_no.equalsIgnoreCase("y")) {
            voteyes.add(p.getName());
            file_yml.set(String.valueOf(id) + ".VotesYes", voteyes);
        } else if (yes_no.equalsIgnoreCase("no") ||yes_no.equalsIgnoreCase("n")) {
            voteno.add(p.getName());
            file_yml.set(String.valueOf(id) + ".VotesNo", voteno);
        }

        try {
            file_yml.save(file);
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    public List<String> getPlayersInYes(String id) {
        File file = PollCommand.getPollByID(pollsdata_folder, id);
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
        return yaml.getStringList(String.valueOf(id) + ".VotesYes");
    }

    public List<String> getPlayersInNo(String id) {
        File file = PollCommand.getPollByID(pollsdata_folder, id);
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
        return yaml.getStringList(String.valueOf(id) + ".VotesNo");
    }

    public void transferVoteNoToYes(Player p, String id) {
        File file = PollCommand.getPollByID(pollsdata_folder, id);
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);

        List<String> list = getPlayersInNo(id); //get voting no and remove player and update
        list.remove(p.getName());
        yaml.set(String.valueOf(id) + ".VotesNo", list);

        List<String> list2 = getPlayersInYes(id); //get voting yes and add player and update
        list2.add(p.getName());
        yaml.set(String.valueOf(id) + ".VotesYes", list2);

        try {
            yaml.save(file);
        } catch (Exception e) {

        }

    }

    public void transferVoteYesToNo(Player p, String id) {
        File file = PollCommand.getPollByID(pollsdata_folder, id);
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);

        List<String> list = getPlayersInNo(id); //get voting no and add player and update
        list.add(p.getName());
        yaml.set(String.valueOf(id) + ".VotesNo", list);

        List<String> list2 = getPlayersInYes(id); //get voting yes and remove player and update
        list2.remove(p.getName());
        yaml.set(String.valueOf(id) + ".VotesYes", list2);

        try {
            yaml.save(file);
        } catch (Exception e) {

        }

    }

    public boolean hasVoted(Player p, String id) {
        if (getPlayersInYes(id).contains(p.getName())) {
            return true;
        }

        if (getPlayersInNo(id).contains(p.getName())) {
            return true;
        }

        return false;
    }

    public List<String> allFileNames(File folder) {
        File[] listOfFiles = folder.listFiles();
        List<String> list = new ArrayList<>();

        for (File file : listOfFiles) {
            String file_name = file.getName();
            list.add(file_name);
        }

        if (list.isEmpty() || list == null) {

            return null;
        }
        return list;
    }

    public File[] allFiles(File folder) {
        File[] listOfFiles = folder.listFiles();

        return listOfFiles;
    }

    public void createDungeon(String dungeon_id){
        if(!dungeons_folder.exists() || !dungeons_file.exists()){
            return;
        }
        List<String> easy = Arrays.asList("easy");
        List<String> medium = Arrays.asList("medium");
        List<String> hard = Arrays.asList("hard");

        dungeons_yml.createSection(dungeon_id);
        dungeons_yml.createSection(dungeon_id + ".Player-Spawn-Location");
        dungeons_yml.set(dungeon_id + ".Player-Spawn-Location", "null");
        dungeons_yml.createSection(dungeon_id + ".Player-End-Spawn-Location");
        dungeons_yml.set(dungeon_id + ".Player-End-Spawn-Location", "null");
        dungeons_yml.createSection(dungeon_id + ".Mob-Spawn-Locations");
        dungeons_yml.createSection(dungeon_id + ".Mob-Spawn-Locations.EASY");
        dungeons_yml.createSection(dungeon_id + ".Mob-Spawn-Locations.MEDIUM");
        dungeons_yml.createSection(dungeon_id + ".Mob-Spawn-Locations.HARD");

        dungeons_yml.createSection(dungeon_id + ".Mob-Spawn-Locations.EASY.ENCOUNTERS");
        dungeons_yml.set(dungeon_id + ".Mob-Spawn-Locations.EASY.ENCOUNTERS",easy);
        dungeons_yml.createSection(dungeon_id + ".Mob-Spawn-Locations.MEDIUM.ENCOUNTERS");
        dungeons_yml.set(dungeon_id + ".Mob-Spawn-Locations.MEDIUM.ENCOUNTERS",medium);
        dungeons_yml.createSection(dungeon_id + ".Mob-Spawn-Locations.HARD.ENCOUNTERS");
        dungeons_yml.set(dungeon_id + ".Mob-Spawn-Locations.HARD.ENCOUNTERS",hard);
        try {
            dungeons_yml.save(dungeons_file);
        } catch (IOException e){
            logger.info(worldciv + ChatColor.DARK_RED + " Failed to save dungeons file.");
            e.printStackTrace();
        }

    }

    public void removeDungeon(String dungeon_id){
        if(!dungeons_folder.exists() || !dungeons_file.exists()){
            return;
        }

        dungeons_yml.set(dungeon_id, null);
        activedungeons.remove(dungeon_id);

        try {
            dungeons_yml.save(dungeons_file);
        } catch (IOException e){
            logger.info(worldciv + ChatColor.DARK_RED + " Failed to save dungeons file.");
            e.printStackTrace();
        }
    }

    public void channelsSetup(File file) {
        YamlConfiguration yml = YamlConfiguration.loadConfiguration(file);
        yml.createSection("channels");
        yml.createSection("channels.Server");
        yml.createSection("channels.Towny");

        try {
            yml.save(file);
        } catch (Exception e) {
        }

    }

    public void blocksSetup(File file) {
        YamlConfiguration yml = YamlConfiguration.loadConfiguration(file);
        yml.createSection("blocks");
        yml.createSection("blocks.SteelAnvil");
        try {
            yml.save(file);
        } catch (Exception e) {
        }

    }

    public void addBlocks(Block b, String blocks) {
        YamlConfiguration yml = YamlConfiguration.loadConfiguration(blocks_file);
        switch (blocks.toLowerCase()) {
            case "steelanvil":
            case "steel anvil":
                List<Location> list = getBlocksList("steel anvil");
                list.add(b.getLocation());
                yml.set("blocks.SteelAnvil", list);
                break;
        }

        try {
            yml.save(blocks_file);
        } catch (Exception e) {

        }
    }

    public void removeBlocks(Block b, String blocks) {
        YamlConfiguration yml = YamlConfiguration.loadConfiguration(blocks_file);
        switch (blocks.toLowerCase()) {
            case "steelanvil":
            case "steel anvil":
                List<Location> list = getBlocksList("steel anvil");
                list.remove(b.getLocation());
                yml.set("blocks.SteelAnvil", list);
                break;
        }

        try {
            yml.save(blocks_file);
        } catch (Exception e) {

        }
    }

    public List<Location> getBlocksList(String customBlockName) {
        YamlConfiguration yml = YamlConfiguration.loadConfiguration(blocks_file);


        switch (customBlockName.toLowerCase()) {
            case "steel anvil":
            case "steelanvil":
                if (yml.getList("blocks.SteelAnvil") == null
                        || yml.get("blocks.SteelAnvil") == null
                        || yml == null) {
                    List<Location> list = new ArrayList<>();
                    return list;
                } else {
                    List<Location> list = (List<Location>) yml.getList("blocks.SteelAnvil");;
                    return list;
                }
            default:
                return null;
        }

    }

    public void addChannels(Player p, String channel) {
        YamlConfiguration yml = YamlConfiguration.loadConfiguration(channels_file);
        switch(channel.toLowerCase()){
            case "server":
                List<String> list = getChannelList("server");
                list.add(p.getName());
                yml.set("channels.Server", list);
                break;
            case "towny":
                List<String> list2 = getChannelList("towny");
                list2.add(p.getName());
                yml.set("channels.Towny", list2);
                break;

        }

        try{
            yml.save(channels_file);
        }catch (Exception e){

        }
    }
    public void removeChannels(Player p, String channel) {
        YamlConfiguration yml = YamlConfiguration.loadConfiguration(channels_file);
        switch(channel.toLowerCase()){
            case "server":
                List<String> list = getChannelList("server");
                list.remove(p.getName());
                yml.set("channels.Server", list);
                break;
            case "towny":
                List<String> list2 = getChannelList("towny");
                list2.remove(p.getName());
                yml.set("channels.Towny", list2);
                break;

        }

        try{
            yml.save(channels_file);
        }catch (Exception e){

        }
    }

    public List<String> getChannelList(String channel){
        YamlConfiguration yml = YamlConfiguration.loadConfiguration(channels_file);

        switch(channel.toLowerCase()){
            case "server":
                if(yml.getStringList("channels.Server").isEmpty() || yml.getStringList("channels.Server") == null){
                    List<String> list = new ArrayList<>();
                    return list;
                } else {
                    return yml.getStringList("channels.Server");
                }
            case "towny":
                if(yml.getStringList("channels.Towny").isEmpty() || yml.getStringList("channels.Towny") == null){
                    List<String> list = new ArrayList<>();
                    return list;
                } else {
                    return yml.getStringList("channels.Towny");
                }
            default:
                return null;
        }
    }

    public void toggleSetup(File file) {
        YamlConfiguration yml = YamlConfiguration.loadConfiguration(file);
        yml.createSection("toggle");
        yml.createSection("toggle.Scoreboard");
        yml.createSection("toggle.ScoreboardAnimation");
        yml.createSection("toggle.VisionMessages");
        yml.createSection("toggle.Censorship");
        yml.createSection("toggle.Timber");
        yml.createSection("toggle.TimberMessages");
        yml.createSection("toggle.VisionBypass");
        yml.createSection("toggle.SocialSpy");
        yml.createSection("toggle.Colorblind");

        try {
            yml.save(file);
        } catch (Exception e) {
        }

    }

    public void addToggle(Player p, String toggle_name){
        YamlConfiguration yml = YamlConfiguration.loadConfiguration(toggle_file);

        switch(toggle_name.toLowerCase()){
            case "scoreboard":
            case "sb":
                List<String> list = getToggleList("sb");
                list.add(p.getName());
                yml.set("toggle.Scoreboard", list);
                break;
            case "sbanimation":
            case "anim":
                List<String> list2 = getToggleList("anim");
                list2.add(p.getName());
                yml.set("toggle.ScoreboardAnimation", list2);
                break;
            case "visionmessages":
            case "vms":
            case "vm":
                List<String> list3 = getToggleList("vms");
                list3.add(p.getName());
                yml.set("toggle.VisionMessages", list3);
                break;
            case "censorship":
            case "c":
                List<String> list4 = getToggleList("c");
                list4.add(p.getName());
                yml.set("toggle.Censorship",list4);
                break;
            case "timber":
            case "t":
                List<String> list5 = getToggleList("timber");
                list5.add(p.getName());
                yml.set("toggle.Timber", list5);
                break;
            case "timbermessages":
            case "tms":
            case "tm":
                List<String> list6 = getToggleList("tms");
                list6.add(p.getName());
                yml.set("toggle.TimberMessages", list6);
                break;
            case "vision":
            case "v":
                List<String> list7 = getToggleList("v");
                list7.add(p.getName());
                yml.set("toggle.VisionBypass", list7);
                break;
            case "socialspy":
            case "ss":
                List<String> list8 = getToggleList("ss");
                list8.add(p.getName());
                yml.set("toggle.SocialSpy", list8);
                break;
            case "colorblind":
            case "cb":
                List<String> list9 = getToggleList("cb");
                list9.add(p.getName());
                yml.set("toggle.Colorblind", list9);
                break;
        }

        try{
            yml.save(toggle_file);
        }catch (Exception e){

        }


    }

    public void removeToggle(Player p, String toggle_name){
        YamlConfiguration yml = YamlConfiguration.loadConfiguration(toggle_file);

        switch(toggle_name.toLowerCase()){
            case "scoreboard":
            case "sb":
                List<String> list = getToggleList("sb");
                list.remove(p.getName());
                yml.set("toggle.Scoreboard", list);
                break;
            case "sbanimation":
            case "anim":
                List<String> list2 = getToggleList("anim");
                list2.remove(p.getName());
                yml.set("toggle.ScoreboardAnimation", list2);
                break;
            case "visionmessages":
            case "vms":
            case "vm":
                List<String> list3 = getToggleList("vms");
                list3.remove(p.getName());
                yml.set("toggle.VisionMessages", list3);
                break;
            case "censorship":
            case "c":
                List<String> list4 = getToggleList("c");
                list4.remove(p.getName());
                yml.set("toggle.Censorship",list4);
                break;
            case "timber":
            case "t":
                List<String> list5 = getToggleList("timber");
                list5.remove(p.getName());
                yml.set("toggle.Timber", list5);
                break;
            case "timbermessages":
            case "tms":
            case "tm":
                List<String> list6 = getToggleList("tms");
                list6.remove(p.getName());
                yml.set("toggle.TimberMessages", list6);
                break;
            case "vision":
            case "v":
                List<String> list7 = getToggleList("v");
                list7.remove(p.getName());
                yml.set("toggle.VisionBypass", list7);
                break;
            case "socialspy":
            case "ss":
                List<String> list8 = getToggleList("ss");
                list8.remove(p.getName());
                yml.set("toggle.SocialSpy", list8);
                break;
            case "colorblind":
            case "cb":
                List<String> list9 = getToggleList("cb");
                list9.remove(p.getName());
                yml.set("toggle.Colorblind", list9);
                break;
        }
        try{
            yml.save(toggle_file);
        }catch (Exception e){

        }
    }

    public List<String> getToggleList(String toggle_name){
        YamlConfiguration yml = YamlConfiguration.loadConfiguration(toggle_file);

        switch(toggle_name.toLowerCase()){
            case "scoreboard":
            case "sb":
                if(yml.getStringList("toggle.Scoreboard").isEmpty() || yml.getStringList("toggle.Scoreboard") == null){
                    List<String> list = new ArrayList<>();
                    return list;
                } else {
                    return yml.getStringList("toggle.Scoreboard");
                }
            case "sbanimation":
            case "anim":
                if(yml.getStringList("toggle.ScoreboardAnimation").isEmpty()|| yml.getStringList("toggle.ScoreboardAnimation") == null){
                    List<String> list = new ArrayList<>();
                    return list;
                } else {
                    return yml.getStringList("toggle.ScoreboardAnimation");
                }
            case "visionmessages":
            case "vms":
            case "vm":
                if(yml.getStringList("toggle.VisionMessages").isEmpty()|| yml.getStringList("toggle.VisionMessages") == null){
                    List<String> list = new ArrayList<>();
                    return list;
                } else {
                    return yml.getStringList("toggle.VisionMessages");
                }
            case "censorship":
            case "c":
                if(yml.getStringList("toggle.Censorship").isEmpty()|| yml.getStringList("toggle.Censorship") == null){
                    List<String> list = new ArrayList<>();
                    return list;
                } else {
                    return yml.getStringList("toggle.Censorship");
                }
            case "timber":
            case "t":
                if(yml.getStringList("toggle.Timber").isEmpty()|| yml.getStringList("toggle.Timber") == null){
                    List<String> list = new ArrayList<>();
                    return list;
                } else {
                    return yml.getStringList("toggle.Timber");
                }
            case "timbermessages":
            case "tms":
            case "tm":
                if(yml.getStringList("toggle.TimberMessages").isEmpty()|| yml.getStringList("toggle.TimberMessages") == null){
                    List<String> list = new ArrayList<>();
                    return list;
                } else {
                    return yml.getStringList("toggle.TimberMessages");
                }
            case "vision":
            case "v":
                if(yml.getStringList("toggle.VisionBypass").isEmpty()|| yml.getStringList("toggle.VisionBypass") == null){
                    List<String> list = new ArrayList<>();
                    return list;
                } else {
                    return yml.getStringList("toggle.VisionBypass");
                }
            case "socialspy":
            case "ss":
                if(yml.getStringList("toggle.SocialSpy").isEmpty()|| yml.getStringList("toggle.SocialSpy") == null){
                    List<String> list = new ArrayList<>();
                    return list;
                } else {
                    return yml.getStringList("toggle.SocialSpy");
                }
            case "colorblind":
            case "cb":
                if(yml.getStringList("toggle.Colorblind").isEmpty()|| yml.getStringList("toggle.Colorblind") == null){
                    List<String> list = new ArrayList<>();
                    return list;
                } else {
                    return yml.getStringList("toggle.Colorblind");
                }
            default:
                return null;
        }
    }

    public void saveMob(String dungeon_id, String mythicMobID,String difficulty, int amount, Location location,String encounterName){
        dungeons_yml.createSection(dungeon_id + ".Mob-Spawn-Locations."+difficulty+"."+encounterName);
        dungeons_yml.createSection(dungeon_id + ".Mob-Spawn-Locations."+difficulty+"."+encounterName+".Mobs");
        dungeons_yml.createSection(dungeon_id + ".Mob-Spawn-Locations."+difficulty+"."+encounterName+".Mobs.Name");
        dungeons_yml.createSection(dungeon_id + ".Mob-Spawn-Locations."+difficulty+"."+encounterName+".Mobs.amount");
        dungeons_yml.set(dungeon_id + ".Mob-Spawn-Locations."+difficulty+"."+encounterName+".Mobs.amount",amount);
        dungeons_yml.set(dungeon_id + ".Mob-Spawn-Locations."+difficulty+"."+encounterName+".Mobs.Name",mythicMobID);
        dungeons_yml.set(dungeon_id + ".Mob-Spawn-Locations."+difficulty+"."+encounterName+".Location", location);

        List<String> encounters = dungeons_yml.getStringList(dungeon_id+".Mob-Spawn-Locations."+difficulty+".ENCOUNTERS");

        if (encounters.get(0).equals("easy") ||encounters.get(0).equals("medium") ||encounters.get(0).equals("hard")) {
            encounters.remove(0);
            encounters.add(encounterName);
        }else{
            encounters.add(encounterName);
        }
        dungeons_yml.set(dungeon_id+".Mob-Spawn-Locations."+difficulty+".ENCOUNTERS",encounters);

        try {
            dungeons_yml.save(dungeons_file);
        } catch (IOException e){
            logger.info(worldciv + ChatColor.DARK_RED + " Failed to save dungeons file.");
            e.printStackTrace();
        }
    }
    public DungeonMob[] loadMobs(String dungeon_id, String difficulty){
        List<String> encounters = dungeons_yml.getStringList(dungeon_id+".Mob-Spawn-Locations."+difficulty+".ENCOUNTERS");
        //Bukkit.broadcastMessage(encounters.toString());
        DungeonMob[] mobs = new DungeonMob[encounters.size()];
        //YamlConfiguration yaml = dungeons_yml.get(dungeon_id + ".Mob-Spawn-Locations."+difficulty);
        for(int i = 0; i <encounters.size();i++){
            Location location = (Location) dungeons_yml.get(dungeon_id + ".Mob-Spawn-Locations." +difficulty+"."+ encounters.get(i)+".Location");
            //Bukkit.broadcastMessage(location.toString());
            mobs[i] = new DungeonMob(location,
                    dungeons_yml.getInt(dungeon_id + ".Mob-Spawn-Locations."+difficulty+"."+encounters.get(i)+".Mobs.amount"),
                    dungeons_yml.getString(dungeon_id + ".Mob-Spawn-Locations."+difficulty+"."+encounters.get(i)+".Mobs.Name"));
        }
        return mobs;
    }




    public void setPlayerSpawn(String dungeon_id, Location location ){
        if(!dungeons_folder.exists() || !dungeons_file.exists()){
            return;
        }

        //String concat_coords = String.valueOf(x) + ", " + String.valueOf(y) + ", " + String.valueOf(z);

        dungeons_yml.set(dungeon_id + ".Player-Spawn-Location", location);

        try {
            dungeons_yml.save(dungeons_file);
        } catch (IOException e){
            logger.info(worldciv + ChatColor.DARK_RED + " Failed to save dungeons file.");
            e.printStackTrace();
        }
    }
    public Location getPlayerSpawn(String dungeon_id){
        if(!dungeons_folder.exists() || !dungeons_file.exists()){
            return null;
        }

        Object value = dungeons_yml.get(dungeon_id + ".Player-Spawn-Location");

        if(value.toString() == "null"){
            logger.info(worldciv + ChatColor.DARK_RED + " No intro spawn location found for " + ChatColor.YELLOW + dungeon_id);
            return null;
        }

        Location location = (Location) dungeons_yml.get(dungeon_id + ".Player-Spawn-Location");
        return location;
    }

    public void setPlayerEndSpawn(String dungeon_id, Location location ){
        if(!dungeons_folder.exists() || !dungeons_file.exists()){
            return;
        }

        //String concat_coords = String.valueOf(x) + ", " + String.valueOf(y) + ", " + String.valueOf(z);

        dungeons_yml.set(dungeon_id + ".Player-End-Spawn-Location", location);

        try {
            dungeons_yml.save(dungeons_file);
        } catch (IOException e){
            logger.info(worldciv + ChatColor.DARK_RED + " Failed to save dungeons file.");
            e.printStackTrace();
        }
    }
    public Location getPlayerEndSpawn(String dungeon_id){
        if(!dungeons_folder.exists() || !dungeons_file.exists()){
            return null;
        }

        Object value = dungeons_yml.get(dungeon_id + ".Player-End-Spawn-Location");


        if(value.toString() == "null"){
            logger.info(worldciv + ChatColor.DARK_RED + " No outro spawn location found for " + ChatColor.YELLOW + dungeon_id);

            return null;
        }

        Location location = (Location) dungeons_yml.get(dungeon_id + ".Player-End-Spawn-Location");
        return location;

    }






    public boolean saveItem(CustomItem item){
        File dir = new File(Bukkit.getPluginManager().getPlugin("WorldCivMaster").getDataFolder()+"/Custom_Items");
        if(dir.exists()) {
            File file = new File(dir,CustomItem.unhideItemUUID(item.getId())+".yml");
            if(file.exists()){
                Main.logger.info("Failed error has occurred when saving an item. Item UUID: [" + item.getId() + "}");
                return false;
            }else{
                YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
                yaml = createFileSectionsFromFile(yaml);
                yaml = writeDataToFile(yaml,item);

                try {
                    yaml.save(file);
                    return true;
                } catch (IOException e) {
                    Main.logger.info("Failed error has occurred when saving an item. Item UUID: [" + item.getId() + "}");
                    e.printStackTrace();
                    return false;
                }

            }
        }else{
            Main.logger.info("Failed error has occurred when saving an item. Item UUID: [" + item.getId() + "}");
            return false;
        }

    }
    public CustomItem createItem(ItemStack itemStack, Tier tier, ItemType itemType){
        CustomItem customItem = ItemGenerator.generateItem(itemStack,tier,itemType);
        saveItem(customItem);
        return customItem;
    }

    private YamlConfiguration createFileSectionsFromFile(YamlConfiguration yamlConfiguration){
        yamlConfiguration.createSection("Item-Data");
        yamlConfiguration.createSection("Item-Data.UUID");
        yamlConfiguration.createSection("Item-Data.Name");
        yamlConfiguration.createSection("Item-Data.Damage");
        yamlConfiguration.createSection("Item-Data.Armor");
        yamlConfiguration.createSection("Item-Data.Rarity");
        yamlConfiguration.createSection("Item-Data.Tier");
        yamlConfiguration.createSection("Item-Data.ItemType");
        yamlConfiguration.createSection("Item-Data.Lore");
        yamlConfiguration.createSection("Item-Data.Other");
        yamlConfiguration.createSection("Item-Data.ItemStack");
        return yamlConfiguration;
    }
    private YamlConfiguration writeDataToFile(YamlConfiguration yamlConfiguration, CustomItem item){
        if(item.getId()!=null)yamlConfiguration.set("Item-Data.UUID",CustomItem.unhideItemUUID(item.getId()));
        if(item.getName()!=null)yamlConfiguration.set("Item-Data.Name",item.getName());
        if(item.getDamage()!=-1)yamlConfiguration.set("Item-Data.Damage",item.getDamage());
        if(item.getArmor()!=-1)yamlConfiguration.set("Item-Data.Armor",item.getArmor());
        if(item.getRarity()!=null)yamlConfiguration.set("Item-Data.Rarity",item.getRarity().toString());
        if(item.getTier()!=null)yamlConfiguration.set("Item-Data.Tier",item.getTier().toString());
        if(item.getId()!=null)yamlConfiguration.set("Item-Data.ItemType",item.getItemType().toString());
        if(item.getOther()!=-1)yamlConfiguration.set("Item-Data.Lore",item.getOther());
        if(item.getOther()!=-1)yamlConfiguration.set("Item-Data.Other",item.getOther());
        if(item.getItemStack()!=null)yamlConfiguration.set("Item-Data.ItemStack",item.getItemStack());
        return yamlConfiguration;
    }
}
