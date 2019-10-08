package com.worldciv.commands;

import com.palmergames.bukkit.towny.object.Resident;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.worldciv.events.chat.ChatChannelEvent;
import com.worldciv.utility.Fanciful.mkremins.fanciful.FancyMessage;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.List;

import static com.worldciv.the60th.Main.fileSystem;
import static com.worldciv.the60th.Main.getWorldGuard;
import static com.worldciv.the60th.Main.plugin;
import static com.worldciv.utility.utilityArrays.*;
import static com.worldciv.utility.utilityStrings.*;

public class WorldCivCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {

        if (cmd.getName().equalsIgnoreCase("wc") || cmd.getName().equalsIgnoreCase("worldciv")) {


            if (args.length == 0) {
                helppage1((Player) sender);
                return true;
            }


            switch (args[0].toLowerCase()) {

                case "links":
                    if(args.length == 1){
                        sender.sendMessage(maintop);
                        new FancyMessage(ChatColor.BLUE+ "Discord:").tooltip(ChatColor.BLUE + "Voice/Text Communication").then(" ").then(ChatColor.BOLD+ "" + ChatColor.BLUE +"Link").tooltip(ChatColor.BLUE + "Click me to be redirected!").link("https://discord.gg/2WXyCNc").send(sender);
                        new FancyMessage(ChatColor.GOLD+ "Website:").tooltip(ChatColor.GOLD + "Forums/Applications/Shop").then(" ").then(ChatColor.BOLD+ "" + ChatColor.GOLD +"Link").tooltip(ChatColor.BLUE + "Click me to be redirected!").link("http://www.rcommunitymc.com").send(sender);
                        new FancyMessage(ChatColor.GOLD+ "Dynmap:").tooltip(ChatColor.GOLD + "Live Map").then(" ").then(ChatColor.BOLD+ "" + ChatColor.GOLD +"Link").tooltip(ChatColor.BLUE + "Click me to be redirected!").link(
                                "http://www.rcommunitymc.com/dynmap").send(sender);
                        new FancyMessage(ChatColor.GOLD+ "Server IP:").tooltip(ChatColor.GOLD + "The server IP address").then(" ").then(ChatColor.BOLD+ "" + ChatColor.GOLD +"Hover me").tooltip(ChatColor.BLUE + "IP: world-civ.com").send(sender);

                        sender.sendMessage(mainbot);
                    }
                    sender.sendMessage(worldciv + ChatColor.GRAY + " Did you mean to use " + ChatColor.YELLOW + "/wc links" + ChatColor.GRAY + ".");
                    return true;

                case "towny":
                    if (args.length == 1) {
                        wctownyhelppage1((Player) sender);
                        return true;
                    }

                    if (args.length == 3) {

                        if (!args[1].equalsIgnoreCase("mute")) {
                            sender.sendMessage(worldciv + ChatColor.GRAY + " I don't understand " + args[1] + ". Use: " + ChatColor.YELLOW + "/wc towny " + ChatColor.GRAY + "for more assistance.");
                            return true;
                        }

                        if (ChatChannelEvent.getNation((Player) sender) == null) {
                            sender.sendMessage(worldciv + ChatColor.GRAY + " You must be in a nation to mute a player.");
                            return true;
                        }

                        if (ChatChannelEvent.getTown((Player) sender) == null) {
                            sender.sendMessage(worldciv + ChatColor.GRAY + " You must be in a town to mute a player.");
                            return true;
                        }
                        /**
                         * The sender is in a town and nation
                         */

                        if (!ChatChannelEvent.isTownStaff((Player) sender)) {
                            sender.sendMessage(worldciv + ChatColor.GRAY + " You must be the mayor or an assistant to mute residents.");
                            return true;
                        }
                        /**
                         * The sender is assistant or mayor
                         */

                        Player player_muted = Bukkit.getPlayer(args[2]);

                        if (player_muted == null) {
                            sender.sendMessage(worldciv + ChatColor.GRAY + " The player you requested for was not found in the server.");
                            return true;
                        }
                        /**
                         * Player is not null
                         */

                        if (sender == player_muted) {
                            sender.sendMessage(worldciv + ChatColor.GRAY + " You can't mute yourself.");
                            return true;
                        }

                        /**
                         * There's always that one player who tries to mute themselves.. why..
                         */

                        if (!ChatChannelEvent.getNationResidents((Player) sender).contains(ChatChannelEvent.getResident((Player) player_muted))) {
                            sender.sendMessage(worldciv + ChatColor.GRAY + " You can't mute players from other nations.");
                            return true;
                        }
                        /**
                         * If the nation residents is not the residen tyoure trying to mute. cross-nation muting
                         */

                        if (!ChatChannelEvent.getNation((Player) sender).isKing(ChatChannelEvent.getResident((Player) sender))) {
                            if (!ChatChannelEvent.getTownResidents((Player) sender).contains(ChatChannelEvent.getResident((Player) player_muted))) {
                                sender.sendMessage(worldciv + ChatColor.GRAY + " Only the king can mute players from other towns");
                                return true;
                            }
                        }
                        /**
                         * Only kings can mute players from other towns
                         */

                        if (!ChatChannelEvent.getNation((Player) sender).isKing(ChatChannelEvent.getResident((Player) sender))) { //if hes not king
                            if (!ChatChannelEvent.getResident((Player) sender).isMayor()) { //If he's not mayor
                                if (ChatChannelEvent.isTownStaff(player_muted)) { //player ur muting is of town rank staff
                                    sender.sendMessage(worldciv + ChatColor.GRAY + " You can't mute the mayor or an assistant.");
                                    return true;
                                }
                            }
                        }
                        /**
                         * If the player is not a mayor, and he's trying to mute the mayor or the assistant
                         */

                        if(fileSystem.getChannelList("towny").contains(player_muted.getName())){
                            //If the player is already muted, unmute him
                            for (Resident resident : ChatChannelEvent.getNationResidents((Player) sender)){
                                Player player = Bukkit.getPlayer(resident.getName());
                                player.sendMessage(worldciv + " " + ChatColor.AQUA + player_muted.getName() + ChatColor.RED + " is now able to participate in towny chat.");

                            }
                            fileSystem.removeChannels(player_muted, "towny");
                            return true;
                        } else { //if teh player is not muted
                            for (Resident resident : ChatChannelEvent.getNationResidents((Player) sender)){
                                Player player = Bukkit.getPlayer(resident.getName());
                                player.sendMessage(worldciv + " " + ChatColor.AQUA + player_muted.getName() + ChatColor.RED + " is muted from using towny chat.");
                            }
                            fileSystem.addChannels(player_muted, "towny");
                            return true;
                        }
                    }
                    sender.sendMessage(worldciv + ChatColor.GRAY + " You are missing arguments. Use: " + ChatColor.YELLOW + "/wc towny mute <player>" + ChatColor.GRAY + ".");
                    return true;

                case "mute":

                    if(!sender.hasPermission("worldciv.mod") && !sender.hasPermission("worldciv.admin")){
                        sender.sendMessage(worldciv + ChatColor.GRAY + " You aren't allowed to server mute players. Permission node: " +ChatColor.YELLOW + "worldciv.mod" +ChatColor.GRAY
                        + " OR " +ChatColor.YELLOW + "worldciv.admin" + ChatColor.GRAY + ".");
                        return true;
                    }

                    if (args.length == 2) {

                        Player player_muted = Bukkit.getPlayer(args[1]);

                        if (player_muted == null) {
                            sender.sendMessage(worldciv + ChatColor.GRAY + " The player you requested for was not found in the server");
                            return true;
                        }
                        /**
                         * Player is not null
                         */

                        if (sender == player_muted) {
                            sender.sendMessage(worldciv + ChatColor.GRAY + " You can't mute yourself.");
                            return true;
                        }

                        /**
                         * There's always that one player who tries to mute themselves.. why..
                         */

                        if (ChatChannelEvent.getPriorityGroup((Player) sender).getWeight() == ChatChannelEvent.getPriorityGroup(player_muted).getWeight()) {
                            sender.sendMessage(worldciv + ChatColor.GRAY + " You can't mute other players of the same rank.");
                            return true;
                        }
                        /**
                         * Can't mute people of same rank of PEX
                         */
                        if (ChatChannelEvent.getPriorityGroup((Player) sender).getWeight() > ChatChannelEvent.getPriorityGroup(player_muted).getWeight()) {
                            sender.sendMessage(worldciv + ChatColor.GRAY + " You can't mute a player of higher ranking.");
                            return true;
                        }

                        /**
                         * cant mute player of higher ranking
                         */

                        if(fileSystem.getChannelList("server").contains(player_muted.getName())){
                            for (Player onlineplayer : Bukkit.getOnlinePlayers()) {
                                onlineplayer.sendMessage(worldciv + " " + ChatColor.YELLOW + player_muted.getName() + ChatColor.RED + " is now able to participate in chat.");
                            }
                            fileSystem.removeChannels(player_muted, "server");
                            return true;
                        } else {
                            for (Player onlineplayer : Bukkit.getOnlinePlayers()) {
                                onlineplayer.sendMessage(worldciv + " " + ChatColor.YELLOW + player_muted.getName() + ChatColor.RED + " is now muted from all chat.");
                            }
                            fileSystem.addChannels(player_muted, "server");
                            return true;
                        }
                    }


                        sender.sendMessage(worldciv + ChatColor.GRAY + " You are missing arguments. Use: " + ChatColor.YELLOW + "/wc mute <player>" + ChatColor.GRAY + ".");
                    return true;

                case "help":
                case "h":

                    if (args.length == 1) {
                        helppage1((Player) sender);
                        return true;
                    }

                    if (args.length == 2) {

                        if (!args[1].matches("-?\\d+(\\.\\d+)?")) {
                            sender.sendMessage(worldciv + ChatColor.GRAY + " You must provide a numerical value.");
                            return true;
                        }

                        switch (args[1].toLowerCase()) {

                            case "1":
                                helppage1((Player) sender);
                                return true;
                            case "2":
                                helppage2((Player) sender);
                                return true;
                            case "3":
                                helppage3((Player) sender);
                            default:
                                sender.sendMessage(worldciv + ChatColor.GRAY + " The help page you requested for was not found.");
                                return true;

                        }

                    }

                    sender.sendMessage(worldciv + ChatColor.GRAY + " Invalid arguments. Use" + ChatColor.YELLOW + " /wc help [page]" + ChatColor.GRAY + ".");
                    return true;
                case "t":
                case "tutorial":

                    if (!sender.hasPermission("worldciv.admin")) {
                        sender.sendMessage(worldciv + ChatColor.GRAY + "No permission for this command.");
                        return true;
                    }

                    if (args.length != 3) {
                        sender.sendMessage(worldciv + ChatColor.GRAY + " Invalid arguments. Use " + ChatColor.YELLOW + "/wc tutorial <tutorial> <player>");
                        return true;
                    }


                    switch (args[1].toLowerCase()) {
                        case "light":
                        case "l":

                            if (Bukkit.getPlayer(args[2]) == null) {
                                sender.sendMessage(worldciv + ChatColor.GRAY + " The player you requested is not a player in this server.");
                                return true;
                            }

                            Player tutorialplayer = Bukkit.getPlayer(args[2]);

                            if (tutorialplayer.getInventory().getContents().length == 36) { //needs to b checked
                                sender.sendMessage(worldciv + ChatColor.GRAY + " There is no inventory space for you to enter the light tutorial.");
                                return true;
                            }
                            addLightTutorial(tutorialplayer);

                            return true;
                        default:
                            sender.sendMessage(worldciv + ChatColor.GRAY + " There is no tutorial with that name.");
                            return true;
                    }

                case "quit":
                case "q":

                    removeLightTutorial((Player) sender);
                    return true;


                default:
                    sender.sendMessage(worldciv + ChatColor.GRAY + " Did you mean to use" + ChatColor.YELLOW + " /wc" + ChatColor.GRAY + "?");
                    return true;
            }
        }
        return true;
    }


    public static void addLightTutorial(Player player) {

        ItemStack is = new ItemStack(Material.TORCH, 1);
        ItemMeta im = is.getItemMeta();
        List<String> templore = Arrays.asList(ChatColor.GRAY + "This torch is designed for:", ChatColor.AQUA + player.getName());
        im.setLore(templore);

        is.setItemMeta(im);

        Location location = new Location(Bukkit.getWorld("world"), 8125, 81, 6370, 180, 0);
        Location torchlocation = new Location(Bukkit.getWorld("world"), 8125, 90, 6370, 180, 0);


        player.teleport(location);

        visionregion.remove(player);

        lighttutorial.add(player); //add to an array of  ppl inside lighttutorial.

        String introduction = worldciv + ChatColor.GRAY + " Welcome! You have entered the light tutorial. [Duration: 53 seconds]";
        String optout = ChatColor.YELLOW + " If you ever want to leave the tutorial: " + ChatColor.YELLOW + "/wc quit" + ChatColor.GRAY + ".";
        String droptorch = ChatColor.GRAY + " We aren't sure if you have a torch with you, so for now, we will drop one in the center.";
        String cantfindit = ChatColor.YELLOW + " If you moved out of the center, good luck finding it!";
        String nostealing = ChatColor.GRAY + " Now don't take more than one. If there are other players taking the tutorial, it wouldn't be nice.";
        String justincasestealing = ChatColor.YELLOW + " Just in case, we won't let you steal. We made it steal-proof.";
        String holdinglight = ChatColor.GRAY + " Can you feel the energy of the light run through you? This is the power light has. Without it, you're vulnerable.";
        String playersholdinglight = ChatColor.YELLOW + " If there are other players nearby holding a torch, you are also illuminated. This will be crucial for dungeons!";
        String placinglight = ChatColor.GRAY + " Any sort of lighting on the block you're standing on will provide you vision.";
        String lightingen = ChatColor.YELLOW + " With that said: the sun, lava, glowstone, and anything that provides light will provide you with vision.";
        String weather = ChatColor.GRAY + " In conclusion, be careful of the weather, if you're caught outside in a storm, your torches will cease to provide light.";
        String thx = ChatColor.YELLOW + " If you have any other questions or comments, feel free to ask staff!";
        String torch = ChatColor.GRAY + "Oh, that's right! We're taking back our torch!";

        List<String> messages = Arrays.asList(introduction, optout, droptorch, cantfindit, nostealing, justincasestealing, holdinglight, playersholdinglight, placinglight
                , lightingen, weather, thx, torch);


        new BukkitRunnable() {
            int x = 0;


            public void run() {

                if (!lighttutorial.contains(player)) {

                    removeLightTutorial(player);
                    cancel();
                    return;
                }

                if (messages.size() == x) {

                    player.getInventory().remove(is);
                    removeLightTutorial(player);
                    cancel();
                    return;
                }


                ApplicableRegionSet set = getWorldGuard().getRegionManager(player.getWorld()).getApplicableRegions(player.getLocation());

                boolean tutorialcheck = false;

                for (ProtectedRegion region : set) {

                    if ("tutorial".equalsIgnoreCase(region.getId())) {

                        tutorialcheck = true;
                    }
                }

                if (!tutorialcheck) {

                    removeLightTutorial(player);
                    cancel();
                    return;
                }

                if (x == 2) {

                    Bukkit.getWorld("world").dropItemNaturally(torchlocation, is);
                    player.playSound(location, Sound.ENTITY_SNOWBALL_THROW, 5, 1);
                }

                player.sendMessage(messages.get(x));

                x++;
            }
        }.runTaskTimer(plugin, 20, 80);


    }

    public static void removeLightTutorial(Player player) {

        ItemStack is = new ItemStack(Material.TORCH, 1);
        ItemMeta im = is.getItemMeta();
        List<String> templore = Arrays.asList(ChatColor.GRAY + "This torch is designed for:", ChatColor.AQUA + player.getName());
        im.setLore(templore);
        is.setItemMeta(im);

        if (!lighttutorial.contains(player)) {
            player.sendMessage(worldciv + ChatColor.GRAY + " You are not in a tutorial.");
            return;
        }


        player.getInventory().remove(is);

        Location location = new Location(Bukkit.getWorld("world"), 8125, 153, 6374, (-90), 0);
        player.teleport(location);
        lighttutorial.remove(player);
        visionregion.add(player);
        player.sendMessage(worldciv + ChatColor.GRAY + " You have exited the tutorial.");
        return;

    }

    public void helppage1(Player sender) {
        sender.sendMessage(maintop);
        sender.sendMessage(ChatColor.GRAY + " These are some of the commands available for you: ");
        sender.sendMessage(ChatColor.YELLOW + "/party" + ChatColor.GRAY + ": Displays the party commands.");
        sender.sendMessage(ChatColor.YELLOW + "/dungeon" + ChatColor.GRAY + ": Displays the dungeon commands.");
        sender.sendMessage(ChatColor.YELLOW + "/news" + ChatColor.GRAY + ": Displays the current news.");
        sender.sendMessage(ChatColor.YELLOW + "/wc links" + ChatColor.GRAY + ": Displays media (discord, website, etc).");
        sender.sendMessage(ChatColor.YELLOW + "/polls" + ChatColor.GRAY + ": Displays the polls.");


        if (sender.hasPermission("worldciv.admin")) {
            sender.sendMessage(ChatColor.RED + "Only admins can see the following:");
            sender.sendMessage(ChatColor.YELLOW + "/wc tutorial light <player>" + ChatColor.GRAY + ": Send a player to the light level tutorial.");
        }

        sender.sendMessage(" ");
        sender.sendMessage(ChatColor.YELLOW + "/wc help [page]" + ChatColor.GRAY + ": Select your help page. " + ChatColor.ITALIC + "[Currently on page:1/3]");
        sender.sendMessage(mainbot);
        return;
    }

    public void wctownyhelppage1(Player sender) {
        sender.sendMessage(maintop);
        sender.sendMessage(ChatColor.GRAY + " These are some of the wc-towny commands available for you: ");
        sender.sendMessage(ChatColor.YELLOW + "/wc towny mute <player>" + ChatColor.GRAY + ": Mutes a resident from participating in town, nation, and ally-nation chat. You must be an assistant or mayor to use this command.");
        sender.sendMessage(mainbot);
    }


    public void helppage2(Player sender) {
        sender.sendMessage(maintop);

        sender.sendMessage(ChatColor.GRAY + " These are some of the wc-commands available for you: ");
        sender.sendMessage(ChatColor.YELLOW + "/rules" + ChatColor.GRAY + ": Displays the rules.");
        sender.sendMessage(ChatColor.YELLOW + "/toggle help" + ChatColor.GRAY + ": Displays the toggle commands.");
        sender.sendMessage(ChatColor.YELLOW + "/towny" + ChatColor.GRAY + ": Displays the towny commands.");
        sender.sendMessage(ChatColor.YELLOW + "/wc towny" + ChatColor.GRAY + ": Displays the wc-towny commands.");
        sender.sendMessage(ChatColor.YELLOW + "/channels" + ChatColor.GRAY + ": Displays chat channel commands.");


        if (sender.hasPermission("worldciv.admin")) {
            sender.sendMessage(ChatColor.RED + "Only admins can see the following:");
            sender.sendMessage(ChatColor.YELLOW + "/wc mute <player>" + ChatColor.GRAY + ": Mute a player from ALL chat channels.");
        }

        sender.sendMessage(" ");
        sender.sendMessage(ChatColor.YELLOW + "/wc help [page]" + ChatColor.GRAY + ": Select your help page. " + ChatColor.ITALIC + "[Currently on page:2/3]");
        sender.sendMessage(mainbot);
        return;
    }

    public void helppage3(Player sender) {
        sender.sendMessage(maintop);

        sender.sendMessage(ChatColor.GRAY + " These are some of the commands available for you: ");
        sender.sendMessage(ChatColor.YELLOW + "/brewery" + ChatColor.GRAY + ": Displays the brewery commands.");


        if (sender.hasPermission("worldciv.admin")) {
            sender.sendMessage(ChatColor.RED + "Only admins can see the following:");
            // sender.sendMessage(ChatColor.YELLOW + "/wc mute <player>" + ChatColor.GRAY + ": Mute a player from ALL chat channels.");
        }

        sender.sendMessage(" ");
        sender.sendMessage(ChatColor.YELLOW + "/wc help [page]" + ChatColor.GRAY + ": Select your help page. " + ChatColor.ITALIC + "[Currently on page:3/3]");
        sender.sendMessage(mainbot);
        return;
    }


}
