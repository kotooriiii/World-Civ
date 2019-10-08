package com.worldciv.the60th;


import com.earth2me.essentials.Essentials;
import com.gmail.nossr50.mcMMO;
import com.palmergames.bukkit.towny.Towny;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.worldciv.commands.*;
import com.worldciv.dungeons.DungeonChecker;
import com.worldciv.dungeons.DungeonManager;
import com.worldciv.events.chat.ChatChannelEvent;
import com.worldciv.events.inventory.*;
import com.worldciv.events.mob.mobAttack;
import com.worldciv.events.mob.playerAttack;
import com.worldciv.events.player.*;
import com.worldciv.filesystem.FileSystem;
import com.worldciv.filesystem.Gear;
import com.worldciv.filesystem.audio.MusicManager;
import com.worldciv.scoreboard.scoreboardManager;
import com.worldciv.utility.FurnaceRecipes;
import com.xxmicloxx.NoteBlockAPI.NoteBlockPlayerMain;
import io.lumine.xikage.mythicmobs.MythicMobs;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import java.util.Random;
import java.util.logging.Logger;

import static com.worldciv.utility.utilityMultimaps.chatchannels;
import static com.worldciv.utility.utilityStrings.worldciv;

public class Main extends JavaPlugin {

    protected static scoreboardManager scoreboardManager;
    public static Plugin plugin;
    public static JavaPlugin javaPlugin;
    public static FileSystem fileSystem;
    public static DungeonManager getDungeonManager;
    public static Logger logger;



    public static final Flag vision_bypass = new StateFlag("vision-bypass", true);
    public static final Flag dungeon_region = new StateFlag("dungeon-region", true);

    public void onLoad(){

        FlagRegistry registry = getWorldGuard().getFlagRegistry();
        try {
            registry.register(vision_bypass);
            registry.register(dungeon_region);
        } catch (FlagConflictException e) {
            e.printStackTrace();
        }


    }

    public void onTorchRainEffect() {
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            public void run() {

                Server server = getServer();
                long time = server.getWorld("world").getTime();

                if (time >= 13200 && time <= 13239) {
                    Bukkit.broadcastMessage(worldciv + ChatColor.GRAY + " The sun is setting.");
                } else if (time >= 22390 && time <= 22429) { //2399 is last tick or 2400? use 2399 for safety
                    Bukkit.broadcastMessage(worldciv + ChatColor.GRAY + " The sun is rising.");

                }

                for (Player players : Bukkit.getOnlinePlayers()) {

                    Location loc = players.getLocation();
                    World world = players.getWorld();
                    Biome biome = world.getBiome(loc.getBlockX(), loc.getBlockZ());

                    if (world.hasStorm()) {

                        if (!(biome == Biome.DESERT || biome == Biome.DESERT_HILLS | biome == Biome.MUTATED_DESERT || biome == Biome.MESA || biome == Biome.MESA_CLEAR_ROCK
                                || biome == Biome.MESA_ROCK || biome == Biome.MUTATED_MESA || biome == Biome.MUTATED_MESA_CLEAR_ROCK || biome == Biome.MUTATED_MESA_ROCK
                                || biome == Biome.SAVANNA || biome == Biome.SAVANNA_ROCK || biome == Biome.MUTATED_SAVANNA || biome == Biome.MUTATED_SAVANNA_ROCK)) {


                            if (world.getHighestBlockYAt(loc.getBlockX(), loc.getBlockZ()) < players.getLocation().getBlockY() + 1) {


                                ItemStack currentItem = players.getInventory().getItemInMainHand();
                                ItemStack offHandItem = players.getInventory().getItemInOffHand();

                                if (currentItem.getType() == Material.TORCH) {
                                    Random r = new Random();
                                    int chance = r.nextInt(1200000); //1,200,000

                                    if (chance < 109560) { //9.13%
                                        if (currentItem.getAmount() > 1) {
                                            currentItem.setAmount(currentItem.getAmount() - 1);

                                            if (chance < 27390) {  // 1/4th chance of drop
                                                players.getInventory().addItem(new ItemStack(Material.STICK, 1));
                                                players.sendMessage(worldciv + ChatColor.GRAY + " The storm extinguished your torch.");

                                            } else {
                                                players.sendMessage(worldciv + ChatColor.GRAY + " The storm made your torch useless!");
                                            }
                                        } else {
                                            currentItem.setAmount(-1);
                                            players.getInventory().addItem(new ItemStack(Material.STICK, 1));
                                            players.sendMessage(worldciv + ChatColor.GRAY + " Your last torch in your main hand was used!");
                                        }

                                    }
                                } else if (offHandItem.getType() == Material.TORCH) {
                                    Random r = new Random();
                                    int chance = r.nextInt(1200000);

                                    if (chance < 109560) {
                                        if (offHandItem.getAmount() > 1) {
                                            offHandItem.setAmount(offHandItem.getAmount() - 1);
                                            if (chance < 27390) {  // 1/4th chance of drop
                                                players.getInventory().addItem(new ItemStack(Material.STICK, 1));
                                                players.sendMessage(worldciv + ChatColor.GRAY + " The storm extinguished your torch.");

                                            } else {
                                                players.sendMessage(worldciv + ChatColor.GRAY + " The storm made your torch useless!");
                                            }
                                        } else {
                                            offHandItem.setAmount(-1);
                                            players.getInventory().addItem(new ItemStack(Material.STICK, 1));
                                            players.sendMessage(worldciv + ChatColor.GRAY + " Your last torch in your offhand was used!");
                                        }

                                    }
                                }
                            }
                        }
                    }
                }

            }
        }, 0, 40); //every 2s, try not to jam the server!
    }

    public void onEnable() {
        logger = Logger.getLogger("Minecraft");
        plugin = this;
        javaPlugin = this;

        scoreboardManager = new scoreboardManager();
        PluginDescriptionFile pdfFile = this.getDescription();

        getConfig().options().copyDefaults(true); //creates data folder for pl ----> move to filesystem some later time, not crucial, just looks messy
        fileSystem = new FileSystem();
        getDungeonManager = new DungeonManager();


        if (getConfig().getString("newsmessage") == null) {
            getConfig().set("newsmessage", "          " + ChatColor.GRAY + "This must be a new server. Set a news message with /news set <message>");
        }
        saveConfig();


        logger.info(pdfFile.getName()
                + " has successfully enabled. The current version is: "
                + pdfFile.getVersion());

        registerEvents();
        registerCommands();
        registerChatChannels();

        //Check time of day!
        onTorchRainEffect();

        for (Player p : Bukkit.getOnlinePlayers()){

            if(!chatchannels.containsValue(p.getName())){
                chatchannels.put("global", p.getName());
            }

            scoreboardManager.setScoreboard(p);
        }

        //CraftingRecipes.registerRecipes();
        FurnaceRecipes.registerFurnaceRecipes();
        Gear.registerRecipes();

/*
            If you really plan to add this feature. Which should not be important during a live server.....
            In /rl servers, this will give null error since WorldGuard has not been enabled yet. You will have to wait until WorldGuard as loaded.


        for (Player p : Bukkit.getOnlinePlayers()) {
            ApplicableRegionSet set = getWorldGuard().getRegionManager(p.getWorld()).getApplicableRegions(p.getLocation());

            if (!visionregion.contains(p)) {

                for (ProtectedRegion region : set) {
                    if (region.getFlag(vision_bypass) == StateFlag.State.ALLOW) {
                        visionregion.add(p);
                    }
                }

            }
        } */

        Bukkit.broadcastMessage(worldciv + ChatColor.GRAY + " Refreshing plugin data.");
    }

    public void onDisable() {
        plugin = null;
        PluginDescriptionFile pdfFile = getDescription();
        Logger logger = Logger.getLogger("Minecraft");
        logger.info(pdfFile.getName() + "has successfully disabled.");
    }

    public void registerCommands(){
        getCommand("toggle").setExecutor(new ToggleCommand());

        getCommand("dungeon").setExecutor(new DungeonCommand());
        getCommand("dg").setExecutor(new DungeonCommand());

        getCommand("news").setExecutor(new NewsCommand());

        getCommand("party").setExecutor(new PartyCommand());
        getCommand("p").setExecutor(new PartyCommand());

        getCommand("worldciv").setExecutor(new WorldCivCommand());
        getCommand("wc").setExecutor(new WorldCivCommand());

        getCommand("ch").setExecutor(new ChatCommand());
        getCommand("channels").setExecutor(new ChatCommand());

        getCommand("local").setExecutor(new ChatCommand());
        getCommand("l").setExecutor(new ChatCommand());

        getCommand("global").setExecutor(new ChatCommand());
        getCommand("g").setExecutor(new ChatCommand());

        getCommand("ooc").setExecutor(new ChatCommand());

        getCommand("nc").setExecutor(new ChatCommand());
        getCommand("tc").setExecutor(new ChatCommand());
        getCommand("anc").setExecutor(new ChatCommand());

        getCommand("admin").setExecutor(new ChatCommand());
        getCommand("mod").setExecutor(new ChatCommand());

        getCommand("polls").setExecutor(new PollCommand());
        getCommand("poll").setExecutor(new PollCommand());



    }

    public void registerEvents(){
        PluginManager pm = getServer().getPluginManager();

        pm.registerEvents(new QuitEvent(), this);
        pm.registerEvents(new CommandPreprocess(), this);
        pm.registerEvents(new WeatherChangingEvent(), this);
        pm.registerEvents(new AnvilCreate(), this);
        pm.registerEvents(new CraftCreate(), this);
        pm.registerEvents(new FurnaceCreate(), this);
        pm.registerEvents(new JoinEvent(), this);
        pm.registerEvents(new CraftEvent(), this);
        pm.registerEvents(new RegionEvent(), this);
        pm.registerEvents(new ArrowEvents(), this);
        pm.registerEvents(new BoatFixEvent(), this);
        pm.registerEvents(new PickUpItemEvent(), this);
        pm.registerEvents(new DropItemEvent(), this);
        pm.registerEvents(new ChatChannelEvent(), this);
        pm.registerEvents(new TorchBlockPlaceFixEvent(), this);
        pm.registerEvents(new TreeCutterEvent(), this);
        pm.registerEvents(new DungeonChecker(), this);
        pm.registerEvents(new playerAttack(), this);
        pm.registerEvents(new mobAttack(), this);
        pm.registerEvents(new PlayerAttackEvents(), this);
        pm.registerEvents(new CancelEnderpearlEvent(), this);
        pm.registerEvents(new SteelAnvilEvent(), this);
        pm.registerEvents(new MusicManager(), this);

    }

    public void registerChatChannels(){
        chatchannels.put("local", "dummy_string");
        chatchannels.put("global", "dummy_string");
        chatchannels.put("anc", "dummy_string");
        chatchannels.put("nc", "dummy_string");
        chatchannels.put("tc", "dummy_string");
        chatchannels.put("mod", "dummy_string");
        chatchannels.put("admin", "dummy_string");
        chatchannels.put("ooc", "dummy_string");

    }


    public static Plugin getPlugin() {
        return plugin;
    }

    public static scoreboardManager getScoreboardManager() {
        return scoreboardManager;
    }

    public static WorldGuardPlugin getWorldGuard() {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");

        // WorldGuard may not be loaded
        if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
            return null;
        }

        return (WorldGuardPlugin) plugin;
    }

    public static WorldEditPlugin getWorldEdit() {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");

        // WorldGuard may not be loaded
        if (plugin == null || !(plugin instanceof WorldEditPlugin)) {
            return null;
        }

        return (WorldEditPlugin) plugin;
    }

    public static MythicMobs getMythicMobs() {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("MythicMobs");

        // WorldGuard may not be loaded
        if (plugin == null || !(plugin instanceof MythicMobs)) {
            return null;
        }

        return (MythicMobs) plugin;
    }

    public static Towny getTowny(){
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("Towny");

        //Towny may not beloaded
        if(plugin == null || !(plugin instanceof Towny)){
            return null;
        }

        return (Towny) plugin;
    }

    public static Essentials getEssentials(){
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("Essentials");

        //essentials may not beloaded
        if(plugin == null || !(plugin instanceof Essentials)){
            return null;
        }

        return (Essentials) plugin;
    }

    public static PermissionsEx getPermissionsEx(){
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("PermissionsEx");

        //essentials may not beloaded
        if(plugin == null || !(plugin instanceof PermissionsEx)){
            return null;
        }

        return (PermissionsEx) plugin;
    }

    public static mcMMO getMCMMO(){
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("mcMMO");
        if(plugin == null || !(plugin instanceof mcMMO)){
            return null;
        }

        return (mcMMO) plugin;
    }


    public static NoteBlockPlayerMain getNoteBlockAPI(){
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("NoteBlockAPI");

        if(plugin == null || !(plugin instanceof mcMMO)){
            return null;
        }

        return (NoteBlockPlayerMain) plugin;
    }
}







