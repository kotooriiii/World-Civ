package com.worldciv.scoreboard;

import com.worldciv.the60th.Main;
import com.worldciv.utility.LightLevelEvent;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

import java.util.List;

import static com.worldciv.the60th.Main.fileSystem;
import static com.worldciv.utility.utilityArrays.*;
import static com.worldciv.utility.utilityStrings.worldciv;


public class scoreboardManager {

    private animationManager animationManager = new animationManager();

    /**
     *
     * @param player
     */
    public void setScoreboard(Player player) {

        if (player == null || !player.isOnline()){
            return;
        }

        Scoreboard oboard = Bukkit.getScoreboardManager().getNewScoreboard(); //Creates a new scoreboardManager for every player.

        final Objective obj = oboard.registerNewObjective("WorldCiv", "dummy"); //Uses FINAL for same objective. Different objective every time will cause flickering
        obj.setDisplaySlot(DisplaySlot.SIDEBAR); //self explanatory, this objective is present in the sidebar

        //Complex team variables. These will be only used for CHANGING VALUES. I.E: Health, Torch. DO NOT USE FOR STATIC SCORES LIKE BLANK SCORES, IGN, "HEALTH: "
        final Team newsteam = oboard.registerNewTeam("News"); //valid team: news changes for all players
        final Team torchteam = oboard.registerNewTeam("Torch"); //valid team: blind or not
        final Team blankscoreofficial = oboard.registerNewTeam("blankscore"); //used to increase size

        //Create dummyplayers. These are returned by Bukkit. Not real players, creates object player.
        newsteam.addPlayer(Bukkit.getOfflinePlayer(ChatColor.RED.toString()));
        torchteam.addPlayer(Bukkit.getOfflinePlayer(ChatColor.BLUE.toString()));
        blankscoreofficial.addPlayer(Bukkit.getOfflinePlayer(ChatColor.YELLOW.toString()));

        //STATIC BLANKSCORES. DO NOT CHANGE!!! // also creates static placement of scores.
       // Score blankscore = obj.getScore("                                ");   //EMPTY LINES OF SCORE
        Score blankscore2 = obj.getScore("                            "); //static 19
        blankscore2.setScore(7);
        Score blankscore3 = obj.getScore("  ");
        blankscore3.setScore(4);

        //Create static placement of scores. (what line on sidebar)
        obj.getScore(ChatColor.RED.toString()).setScore(5); //changing value of  news.
        obj.getScore(ChatColor.BLUE.toString()).setScore(3); //changing value of whether blind or not. torch check or x.
        obj.getScore(ChatColor.YELLOW.toString()).setScore(10); //static we need prefix/suffix abuse

        //The loop under me plays the animated display title. try not to create too many loops yo.

        animationManager.serverNameAnimation(obj,player,blankscoreofficial,newsteam);

        //The loop under me updated the scoreboardManager every tick.
       new BukkitRunnable() {
            @Override
            public void run() {
                if(!player.isOnline() || player == null){
                    cancel();
                }
                updateScoreboard(player, obj, newsteam, torchteam , blankscoreofficial); //update  half a second
            }

        }.runTaskTimer(Main.plugin, 40, 10);

        player.setScoreboard(oboard);

    }


    /**
     * @param player
     * @param objective
     * @param newsTeam
     * @param torchTeam
     * @param blankScoreOfficial
     */
    private static void updateScoreboard(Player player, Objective objective, Team newsTeam, Team torchTeam, Team blankScoreOfficial) {


        if(player == null || !player.isOnline()){
            return;
        }

        //Variables that need to init every time to update.
        long health = Math.round(player.getHealth());

        // obj.setDisplayName("World Civilization"); //idea for future: create animated title (look down for updateSidebarTitle). create a function to make things more neat.

        updateVisionTeam(player); //has to be called before checking if you are blind or not. checkmark or x.
        LightLevelEvent.updateLightLevelEvent(player);

        //blankscore


        if(fileSystem.getToggleList("scoreboard").contains(player.getName())){
            return;
        }


        Score score = objective.getScore(ChatColor.AQUA + "IGN:");
        score.setScore(9);

        Score score1 = objective.getScore(ChatColor.GRAY + player.getName());
        score1.setScore(8);

        //blankscore2 | line 7

        Score score2 = objective.getScore(ChatColor.AQUA + "News:");
        score2.setScore(6);

        //newsteam.setPrefix();  //to change go to top in static line numbers. | line 5.

        //blankscore3 | line 4

        // SCORE TO HAVE ✓ OR ✗ MARK

        if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR || fileSystem.getToggleList("vision").contains(player.getName()) || visionregion.contains(player)) {
            torchTeam.setPrefix(ChatColor.YELLOW + "VISION BYPASS");
            torchTeam.setSuffix(ChatColor.RESET + "");
        } else {

            if (visionteam.contains(player)) {
                torchTeam.setPrefix(ChatColor.YELLOW + "Vision [V]:");
                torchTeam.setSuffix(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + " ✓");

            } else if (!visionteam.contains(player)) {
                torchTeam.setPrefix(ChatColor.YELLOW + "Vision [V]:");
                torchTeam.setSuffix(ChatColor.RED + " ✗");
            }
        }
    }

    /**
     *
     * @param player
     */

    public static void updateVisionTeam(Player player) {

        if (fileSystem.getToggleList("vision").contains(player.getName()) || player.getGameMode() == GameMode.CREATIVE){
            if (currentlyBlinded.contains(player)) {

                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "tab player " + player.getName() + " tagprefix &e[V] &f"); //<3 the tab
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "tab reload");

                visionteam.add(player); //ONLY STRING FOR ACTUAL REMOVING BLINDNESS GO TO LIGHTLEVEL EVENT

            }
            return;
        }




        Location location = player.getLocation(); //player loc variables
        Location vision = new Location(location.getWorld(), location.getX(), location.getY() + 1, location.getZ());
        int LightLevel = vision.getBlock().getLightLevel();



        List<Entity> entitylist = player.getNearbyEntities(5, 5, 5); //getting radius 5
        for (int i = 0; i < entitylist.size(); i++) { // for all that are in vision effect

            if (entitylist.get(i).getType() == EntityType.PLAYER) { //for those that are players

                if (holdingLight.contains(player) && !visionteam.contains(player)) { //if you are being lit and you are already not in vision.

                    if(!fileSystem.getToggleList("vms").contains(entitylist.get(i).getName()))
                        entitylist.get(i).sendMessage(worldciv + ChatColor.GRAY + " You have been provided vision by " + ChatColor.AQUA + player.getDisplayName());

                }

                if (holdingLight.contains((Player) entitylist.get(i))) { //BELOW THIS (PLAYER) X BECOMES PERSON BEING LIT

                    if (vision.getBlock().getType() != Material.WATER || vision.getBlock().getType() != Material.STATIONARY_WATER) {
                        //    entitylist.get(i).sendMessage(x.getDisplayName()); WILL TELL YOU (ALL) WHO YOU (HOLDER OF TORCH) ARE LIGHTING HYPE example: KotoriXIII (me): You are lighting (all players)

                        if (!visionteam.contains(player)) {

                            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "tab player " + player.getName() + " tagprefix &e[V] &f"); //<3 the tab
                            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "tab reload");

                            visionteam.add(player); //ADDS TO TEAM AND VISION FROM AOE
                        }
                        return; //CANCELS UNNECESSARY SPAM FROM BELOW
                    }

                }
            }
        }


        if (currentlyBlinded.contains(player)) { //if ur light level is low with no light

            if (visionteam.contains(player)) { //if u r in torch visionteam

                visionteam.remove(player); // remove from visionteam
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "tab player " + player.getName() + " tagprefix &f"); //<3 the tab
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "tab reload");

                //YOU HAVE BEEN BLINDED

            }

        }


        if (!currentlyBlinded.contains(player)) { //if ur light level is high, u can see
            if (!visionteam.contains(player)) { //if ur not on torch team now u r

                if (LightLevel > 5 || holdingLight.contains(player)) {

                    if(player != null) {

                        visionteam.add(player);
                        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "tab player " + player.getName() + " tagprefix &e[V] &f"); //<3 the tab
                        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "tab reload");

                        //YOU HAVE EYES NOW, YAY YOU CAN SEE
                    }
                }
            }
        }


    }
}
//d




























