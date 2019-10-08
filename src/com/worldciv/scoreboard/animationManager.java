package com.worldciv.scoreboard;

import com.worldciv.the60th.Main;
import com.worldciv.utility.Scroller;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import static com.worldciv.the60th.Main.fileSystem;

public final class animationManager {
    private final ChatColor defaultcolor = ChatColor.GOLD;   //worldciv default coloring
    private final ChatColor defaultcolorrcom = ChatColor.LIGHT_PURPLE; //worldciv default coloring
    private final ChatColor highlightrcom = ChatColor.DARK_PURPLE; //highlight per word rcom
    private final ChatColor highlight = ChatColor.YELLOW; //highlight default worldciv
    private final ChatColor rcombar = ChatColor.LIGHT_PURPLE; //default coloring bar for rcom
    private final String sin = ChatColor.DARK_RED + ">";
    private final String sout = ChatColor.DARK_RED + "<";
    private final ChatColor rcomhighlight = ChatColor.DARK_PURPLE;


    public animationManager(){}

    public void serverNameAnimation(Objective objective, Player player, Team team,Team newsTeam){
        new BukkitRunnable(){
            int rotation = 1000;
            Scroller scroller = new Scroller(ChatColor.translateAlternateColorCodes('&', Main.plugin.getConfig().getString("newsmessage")), 16, 5, '&');
        @Override
        public void run() {
            if(!player.isOnline()){
                cancel();
            }

            if(fileSystem.getToggleList("scoreboard").contains(player.getName()) || fileSystem.getToggleList("sbanimation").contains(player.getName())){
                newsTeam.setPrefix(ChatColor.RED + "/news");
                objective.setDisplayName(defaultcolor + "World-Civ");
                team.setPrefix(ChatColor.GRAY + "          ");
                team.setSuffix(ChatColor.GRAY + "           "); //DO NOT DELET!
                cancel();
                return;
            }



            if (Main.plugin.getConfig().getString("newsmessage") == null || Main.plugin.getConfig().getString("newsmessage").equals("          " + ChatColor.YELLOW + "empty") || Main.plugin.getConfig().getString("newsmessage").isEmpty()) {
                newsTeam.setPrefix(ChatColor.RED + "No news today!");
            } else {
                newsTeam.setPrefix(ChatColor.translateAlternateColorCodes('&', scroller.next()));  //to change go to top in static line numbers. | line 5.
            }
        if (!fileSystem.getToggleList("sbanimation").contains(player.getName())) {
            if (rotation == 1000) {
                objective.setDisplayName(defaultcolorrcom + "RCommunityMC");
                team.setPrefix(ChatColor.GRAY + "          "); //use this format or setDisplay for future animations with title
                team.setSuffix(ChatColor.GRAY + "           ");
                rotation--;
            } else if (rotation == 999) {
                objective.setDisplayName(defaultcolorrcom + "RCommunityMC");

                rotation--;
            } else if (rotation == 998) {
                objective.setDisplayName(defaultcolorrcom + "RCommunityMC");
                rotation--;
            } else if (rotation == 997) {
                objective.setDisplayName(defaultcolorrcom + "RCommunityMC");
                rotation--;
            } else if (rotation == 996) {
                objective.setDisplayName(defaultcolorrcom + "RCommunityMC");
                rotation--;
            } else if (rotation == 995) {
                objective.setDisplayName(defaultcolorrcom + "RCommunityMC");
                rotation--;
            } else if (rotation == 994) {
                objective.setDisplayName(defaultcolorrcom + "RCommunityMC");
                rotation--;
            } else if (rotation == 993) {
                objective.setDisplayName(defaultcolorrcom + "RCommunityMC"); // this is the eighth slide.
                rotation--;
            } else if (rotation == 992) {
                objective.setDisplayName(highlightrcom + "R" + defaultcolorrcom + "CommunityMC"); //R (beginning of highlight)
                rotation--;
            } else if (rotation == 991) {
                objective.setDisplayName(defaultcolorrcom + "R" + highlightrcom + "C" + defaultcolorrcom + "ommunityMC"); //C
                rotation--;
            } else if (rotation == 990) {
                objective.setDisplayName(defaultcolorrcom + "RC" + highlightrcom + "o" + defaultcolorrcom + "mmunityMC");  // o

                rotation--;
            } else if (rotation == 989) {
                objective.setDisplayName(defaultcolorrcom + "RCo" + highlightrcom + "m" + defaultcolorrcom + "munityMC"); //m
                rotation--;
            } else if (rotation == 988) {
                objective.setDisplayName(defaultcolorrcom + "RCom" + highlightrcom + "m" + defaultcolorrcom + "unityMC"); //m

                rotation--;
            } else if (rotation == 987) {
                objective.setDisplayName(defaultcolorrcom + "RComm" + highlightrcom + "u" + defaultcolorrcom + "nityMC"); //u
                rotation--;
            } else if (rotation == 986) {
                objective.setDisplayName(defaultcolorrcom + "RCommu" + highlightrcom + "n" + defaultcolorrcom + "ityMC"); //n

                rotation--;
            } else if (rotation == 985) {
                objective.setDisplayName(defaultcolorrcom + "RCommun" + highlightrcom + "i" + defaultcolorrcom + "tyMC"); //i

                rotation--;
            } else if (rotation == 984) {
                objective.setDisplayName(defaultcolorrcom + "RCommuni" + highlightrcom + "t" + defaultcolorrcom + "yMC"); //t

                rotation--;
            } else if (rotation == 983) {
                objective.setDisplayName(defaultcolorrcom + "RCommunit" + highlightrcom + "y" + defaultcolorrcom + "MC"); //y

                rotation--;
            } else if (rotation == 982) {
                objective.setDisplayName(defaultcolorrcom + "RCommunity" + highlightrcom + "M" + defaultcolorrcom + "C"); //M

                rotation--;
            } else if (rotation == 981) {
                objective.setDisplayName(defaultcolorrcom + "RCommunityM" + highlightrcom + "C"); //C

                rotation--;
            } else if (rotation == 980) {
                objective.setDisplayName(defaultcolorrcom + "RCommunityMC");
                rotation--;
            } else if (rotation == 979) {
                objective.setDisplayName(defaultcolorrcom + "RCommunityMC");
                rotation--;
            } else if (rotation == 978) {
                objective.setDisplayName(defaultcolorrcom + "RCommunityMC");
                rotation--;
            } else if (rotation == 977) {
                objective.setDisplayName(defaultcolorrcom + "RCommunityMC");
                rotation--;
            } else if (rotation == 976) {
                objective.setDisplayName(defaultcolorrcom + "RCommunityMC");
                rotation--;
            } else if (rotation == 975) {
                objective.setDisplayName(defaultcolorrcom + "RCommunityMC");
                rotation--;
            } else if (rotation == 974) {
                objective.setDisplayName(defaultcolorrcom + "RCommunityMC");
                rotation--;
            } else if (rotation == 973) {
                objective.setDisplayName(defaultcolorrcom + "RCommunityMC"); //eight slide
                rotation--;
            } else if (rotation == 972) {
                objective.setDisplayName(sin + defaultcolorrcom + "  RCommunityMC  " + sout); // double eat rcom
                rotation--;
            } else if (rotation == 971) {
                objective.setDisplayName(sin + defaultcolorrcom + "  RCommunityMC  " + sout);
                rotation--;
            } else if (rotation == 970) {
                objective.setDisplayName(sin + defaultcolorrcom + " RCommunityMC " + sout);
                rotation--;
            } else if (rotation == 969) {
                objective.setDisplayName(sin + defaultcolorrcom + " CommunityM " + sout);
                rotation--;
            } else if (rotation == 968) {
                objective.setDisplayName(sin + defaultcolorrcom + " ommunity " + sout);
                rotation--;
            } else if (rotation == 967) {
                objective.setDisplayName(sin + defaultcolorrcom + " mmunit " + sout);
                rotation--;
            } else if (rotation == 966) {
                objective.setDisplayName(sin + defaultcolorrcom + " muni " + sout);
                rotation--;
            } else if (rotation == 965) {
                objective.setDisplayName(sin + defaultcolorrcom + " un " + sout);
                rotation--;
            } else if (rotation == 964) {
                objective.setDisplayName(sin + defaultcolorrcom + "" + sout);
                rotation--;
            } else if (rotation == 963) {
                objective.setDisplayName(sout + defaultcolor + "" + sin);
                rotation = 961;                                                 //SKIP 962, mistakes were made
            } else if (rotation == 961) {
                objective.setDisplayName(sout + defaultcolor + " d " + sin);
                rotation--;
            } else if (rotation == 960) {
                objective.setDisplayName(sout + defaultcolor + " ld- " + sin);
                rotation--;
            } else if (rotation == 959) {
                objective.setDisplayName(sout + defaultcolor + " rld-C " + sin);
                rotation--;
            } else if (rotation == 958) {
                objective.setDisplayName(sout + defaultcolor + " orld-Ci " + sin);
                rotation--;
            } else if (rotation == 957) {
                objective.setDisplayName(sout + defaultcolor + " World-Civ " + sin);
                rotation--;
            } else if (rotation == 956) {
                objective.setDisplayName(sout + defaultcolor + "  World-Civ  " + sin);
                rotation--;
            } else if (rotation == 955) {
                objective.setDisplayName(sout + defaultcolor + "  World-Civ  " + sin);
                rotation--;
            } else if (rotation == 954) {
                objective.setDisplayName(defaultcolor + "World-Civ"); //static
                rotation--;
            } else if (rotation == 953) {
                objective.setDisplayName(defaultcolor + "World-Civ");
                rotation--;
            } else if (rotation == 952) {
                objective.setDisplayName(defaultcolor + "World-Civ");
                rotation--;
            } else if (rotation == 951) {
                objective.setDisplayName(defaultcolor + "World-Civ");
                rotation = 0;
            } else if (rotation == 0) {
                objective.setDisplayName(defaultcolor + "World-Civ");
                rotation = 1;
            } else if (rotation == 1) {
                objective.setDisplayName(defaultcolor + "World-Civ");
                rotation = 2;
            } else if (rotation == 2) {
                objective.setDisplayName(defaultcolor + "World-Civ");
                rotation = 3;
            } else if (rotation == 3) {
                objective.setDisplayName(defaultcolor + "World-Civ"); //TO BE EIGHTH SLIDE
                rotation = 4;
            } else if (rotation == 4) {
                objective.setDisplayName(highlight + "W" + defaultcolor + "orld-Civ"); //W //begin highlight animation
                rotation = 5;
            } else if (rotation == 5) {
                objective.setDisplayName(defaultcolor + "W" + highlight + "o" + defaultcolor + "rld-Civ"); //o
                rotation = 6;
            } else if (rotation == 6) {
                objective.setDisplayName(defaultcolor + "Wo" + highlight + "r" + defaultcolor + "ld-Civ"); //r
                rotation = 7;
            } else if (rotation == 7) {
                objective.setDisplayName(defaultcolor + "Wor" + highlight + "l" + defaultcolor + "d-Civ"); //l
                rotation = 8;
            } else if (rotation == 8) {
                objective.setDisplayName(defaultcolor + "Worl" + highlight + "d" + defaultcolor + "-Civ"); //d
                rotation = 9;
            } else if (rotation == 9) {
                objective.setDisplayName(defaultcolor + "World" + highlight + "-" + defaultcolor + "Civ"); //-
                rotation = 10;
            } else if (rotation == 10) {
                objective.setDisplayName(defaultcolor + "World-" + highlight + "C" + defaultcolor + "iv"); //C
                rotation = 11;
            } else if (rotation == 11) {
                objective.setDisplayName(defaultcolor + "World-C" + highlight + "i" + defaultcolor + "v"); //i
                rotation = 12;
            } else if (rotation == 12) {
                objective.setDisplayName(defaultcolor + "World-Ci" + highlight + "v"); //v
                rotation = 13;
            } else if (rotation == 13) {
                objective.setDisplayName(defaultcolor + "World-Civ");
                rotation++;
            } else if (rotation == 14) {
                objective.setDisplayName(defaultcolor + "World-Civ");
                rotation++;
            } else if (rotation == 15) {
                objective.setDisplayName(defaultcolor + "World-Civ");
                rotation++;
            } else if (rotation == 16) {
                objective.setDisplayName(defaultcolor + "World-Civ");
                rotation++;
            } else if (rotation == 17) {
                objective.setDisplayName(defaultcolor + "World-Civ");
                rotation++;
            } else if (rotation == 18) {
                objective.setDisplayName(defaultcolor + "World-Civ");
                rotation++;
            } else if (rotation == 19) {
                objective.setDisplayName(defaultcolor + "World-Civ");
                rotation++;
            } else if (rotation == 20) {
                objective.setDisplayName(defaultcolor + "World-Civ"); //TO BE EIGHTH SLIDE
                rotation++;
            } else if (rotation == 21) {
                objective.setDisplayName(sin + defaultcolor + "  World-Civ  " + sout);  //EATING WORLDCIV
                rotation++;
            } else if (rotation == 22) {
                objective.setDisplayName(sin + defaultcolor + "  World-Civ  " + sout);
                rotation++;
            } else if (rotation == 23) {
                objective.setDisplayName(sin + defaultcolor + " World-Civ " + sout);
                rotation++;
            } else if (rotation == 24) {
                objective.setDisplayName(sin + defaultcolor + " orld-Ci " + sout);
                rotation++;
            } else if (rotation == 25) {
                objective.setDisplayName(sin + defaultcolor + " rld-C " + sout);
                rotation++;
            } else if (rotation == 26) {
                objective.setDisplayName(sin + defaultcolor + " ld- " + sout);
                rotation++;
            } else if (rotation == 27) {
                objective.setDisplayName(sin + defaultcolor + " d- " + sout);
                rotation++;
            } else if (rotation == 28) {
                objective.setDisplayName(sin + defaultcolor + "" + sout);
                rotation++;
            } else if (rotation == 29) {
                objective.setDisplayName(sout + defaultcolorrcom + "" + sin);
                rotation++;
            } else if (rotation == 30) {
                objective.setDisplayName(sout + defaultcolorrcom + " un " + sin); //THROWING UP RCOM
                rotation++;
            } else if (rotation == 31) {
                objective.setDisplayName(sout + defaultcolorrcom + " muni " + sin);
                rotation++;
            } else if (rotation == 32) {
                objective.setDisplayName(sout + defaultcolorrcom + " mmunit " + sin);
                rotation++;
            } else if (rotation == 33) {
                objective.setDisplayName(sout + defaultcolorrcom + " ommunity " + sin);
                rotation++;
            } else if (rotation == 34) {
                objective.setDisplayName(sout + defaultcolorrcom + " CommunityM " + sin);
                rotation++;
            } else if (rotation == 35) {
                objective.setDisplayName(sout + defaultcolorrcom + " RCommunityMC " + sin);
                rotation++;
            } else if (rotation == 36) {
                objective.setDisplayName(sout + defaultcolorrcom + "  RCommunityMC  " + sin);
                rotation++;
            } else if (rotation == 37) {
                objective.setDisplayName(sout + defaultcolorrcom + "  RCommunityMC  " + sin);
                rotation = 1000;
            }
        }


    }

}.runTaskTimer(Main.plugin, 0, 5);
    }


    public void otherAnim(){}
    public void placeHolderAnimOne(){}
}
