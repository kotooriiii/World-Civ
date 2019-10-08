package com.worldciv.filesystem;

import com.sun.corba.se.spi.ior.IORTemplate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class Gear {
    //This should be done better? Feeder function to register recipes rather then by hand?
    //Swords need 3
    //Helms 2
    //Boots 2
    //Legs 1
    //Chests 1
    //Axes 2
    public static ShapedRecipe customTierOneSword;
    public static ShapedRecipe customTierOneSword2;
    public static ShapedRecipe customTierOneSword3;


    public static ShapedRecipe customTierOneHelm;
    public static ShapedRecipe customTierOneHelm2;

    public static ShapedRecipe customTierOneChest;

    public static ShapedRecipe customTierOneLeg;

    public static ShapedRecipe customTierOneBoots;
    public static ShapedRecipe customTierOneBoots2;

    public static ShapedRecipe customTierOneShield;

    public static ShapedRecipe customTierOneBow;

    public static ShapedRecipe customTierOneArrow;

    public static ShapedRecipe customTierOnePike;

    public  static ShapedRecipe customTierOneLance;

    public static ItemStack tierOneHelmIS;
    public static ItemStack tierOneChestIS;
    public static ItemStack tierOneLegsIS;
    public static ItemStack tierOneBootsIS;
    public static ItemStack tierOneSwordIS;
    public static ItemStack tierOneBowIS;
    public static ItemStack tierOneShieldIS;
    public static ItemStack tierOnePikeIS;
    public static ItemStack tierOneLanceIS;
    public static ItemStack tierOneArrowIS;

    public static ShapedRecipe lance;
    public static ShapedRecipe pike;
    public static ShapedRecipe arrow;
    public static ShapedRecipe shield;

    public static ItemStack tierTwoHelm;
    public static ItemStack tierTwoChest;
    public static ItemStack tierTwoLegs;
    public static ItemStack tierTwoBoots;
    public static ItemStack tierTwoSword;
    public static ItemStack tierTwoBow;
    public static ItemStack tierTwoShield;
    public static ItemStack tierTwoPike;
    public static ItemStack tierTwoLance;
    public static ItemStack tierTwoArrow;

    public static ItemStack tierOneHelm;
    public static ItemStack tierOneChest;
    public static ItemStack tierOneLegs;
    public static ItemStack tierOneBoots;
    public static ItemStack tierOneSword;
    public static ItemStack tierOneBow;
    public static ItemStack tierOneShield;
    public static ItemStack tierOnePike;
    public static ItemStack tierOneLance;
    public static ItemStack tierOneArrow;

    public static void registerRecipes(){
        customTierOneSword();
        customTierOneHelm();
        customTierOneChest();
        customTierOneLegs();
        customTierOneBoots();
        CustomTierOneShield();
        CustomTierOneBow();
        CustomTierOneArrow();
        CustomTierOnePike();
        CustomTierOneLance();
        registerLance();
        registerPike();
        registerArrow();
        registerTierTwo();
        registerTierOne();
    }
    private static void registerLance(){
        ItemStack item = new ItemStack(Material.IRON_SPADE, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GRAY + "Tier One Helm");
        meta.setLore(Arrays.asList(ChatColor.WHITE + "Tier one gear."));
        item.setItemMeta(meta);

        lance = new ShapedRecipe(item);
        lance.shape(
                "  @",
                " @ ",
                "#  "
        );
        lance.shape(
                "@  ",
                " @ ",
                "  #"
        );
        lance.setIngredient('@',Material.IRON_INGOT);
        lance.setIngredient('#',Material.STICK);
        Bukkit.getServer().addRecipe(lance);
    }
    private static void registerArrow(){
        ItemStack item = new ItemStack(Material.ARROW, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GRAY + "Tier One Arrow");
        meta.setLore(Arrays.asList(ChatColor.WHITE + "Tier one gear."));
        item.setItemMeta(meta);

        arrow = new ShapedRecipe(item);
        arrow.shape(
                " @ ",
                " # ",
                "   "
        );
        arrow.setIngredient('@',Material.IRON_INGOT);
        arrow.setIngredient('#',Material.STICK);
        Bukkit.getServer().addRecipe(arrow);
    }
    private static void registerPike(){
        ItemStack item = new ItemStack(Material.IRON_SPADE, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GRAY + "Tier One Helm");
        meta.setLore(Arrays.asList(ChatColor.WHITE + "Tier one gear."));
        item.setItemMeta(meta);
        pike = new ShapedRecipe(item);
        pike.shape(
                "  @",
                " # ",
                "#  "
        );
        pike.shape(
                "@  ",
                " # ",
                "  #"
        );
        pike.setIngredient('@',Material.IRON_INGOT);
        pike.setIngredient('#',Material.STICK);
        Bukkit.getServer().addRecipe(pike);
    }
    private static void registerShield(){

    }
    private static void customTierOneHelm(){
        //ItemStack item = CustomItem.getItemFromCustomItem(MainCombat.fileSystem.createItem((new ItemStack(Material.WOOD_SWORD,1)),Tier.five,ItemType.SWORD ));
        ItemStack item = new ItemStack(Material.IRON_HELMET, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GRAY + "Tier One Helm");
        meta.setLore(Arrays.asList(ChatColor.WHITE + "Tier one gear."));
        item.setItemMeta(meta);
        customTierOneHelm = new ShapedRecipe(item);
        customTierOneHelm.shape(
                "@@@",
                "@ @",
                "   ");
        customTierOneHelm.setIngredient('@',Material.IRON_BLOCK);
        Bukkit.getServer().addRecipe(customTierOneHelm);

        customTierOneHelm2 = new ShapedRecipe(item);
        customTierOneHelm2.shape(
                "   ",
                "@@@",
                "@ @");
        customTierOneHelm2.setIngredient('@',Material.IRON_BLOCK);
        Bukkit.getServer().addRecipe(customTierOneHelm2);
        tierOneHelmIS = item;
    }
    private static void customTierOneChest(){
        //ItemStack item = CustomItem.getItemFromCustomItem(MainCombat.fileSystem.createItem((new ItemStack(Material.WOOD_SWORD,1)),Tier.five,ItemType.SWORD ));
        ItemStack item = new ItemStack(Material.IRON_CHESTPLATE, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GRAY + "Tier One Chest plate");
        meta.setLore(Arrays.asList(ChatColor.WHITE + "Tier one gear."));
        item.setItemMeta(meta);
        customTierOneChest = new ShapedRecipe(item);
        customTierOneChest.shape(
                "@ @",
                "@@@",
                "@@@");
        customTierOneChest.setIngredient('@',Material.IRON_BLOCK);

        Bukkit.getServer().addRecipe(customTierOneChest);
        tierOneChestIS = item;
    }
    private static void customTierOneLegs(){
        //ItemStack item = CustomItem.getItemFromCustomItem(MainCombat.fileSystem.createItem((new ItemStack(Material.WOOD_SWORD,1)),Tier.five,ItemType.SWORD ));
        ItemStack item = new ItemStack(Material.IRON_LEGGINGS, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GRAY + "Tier One Leggings");
        meta.setLore(Arrays.asList(ChatColor.WHITE + "Tier one gear."));
        item.setItemMeta(meta);
        customTierOneLeg = new ShapedRecipe(item);
        customTierOneLeg.shape(
                "@@@",
                "@ @" ,
                "@ @");
        customTierOneLeg.setIngredient('@',Material.IRON_BLOCK);

        Bukkit.getServer().addRecipe(customTierOneLeg);
        tierOneLegsIS = item;
    }
    private static void customTierOneBoots(){
        //ItemStack item = CustomItem.getItemFromCustomItem(MainCombat.fileSystem.createItem((new ItemStack(Material.WOOD_SWORD,1)),Tier.five,ItemType.SWORD ));
        ItemStack item = new ItemStack(Material.IRON_BOOTS, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GRAY + "Tier One Boots");
        meta.setLore(Arrays.asList(ChatColor.WHITE + "Tier one gear."));
        item.setItemMeta(meta);

        customTierOneBoots = new ShapedRecipe(item);
        customTierOneBoots.shape(
                "   ",
                "@ @",
                "@ @");
        customTierOneBoots.setIngredient('@',Material.IRON_BLOCK);
        Bukkit.getServer().addRecipe(customTierOneBoots);

        customTierOneBoots2 = new ShapedRecipe(item);
        customTierOneBoots2.shape(
                "@ @",
                "@ @",
                "   "
        );
        customTierOneBoots2.setIngredient('@',Material.IRON_BLOCK);
        Bukkit.getServer().addRecipe(customTierOneBoots2);
        tierOneBootsIS = item;
    }
    private static void customTierOneSword(){
        //ItemStack item = CustomItem.getItemFromCustomItem(MainCombat.fileSystem.createItem((new ItemStack(Material.WOOD_SWORD,1)),Tier.five,ItemType.SWORD ));
        ItemStack item = new ItemStack(Material.IRON_SWORD, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GRAY + "Tier One Sword");
        meta.setLore(Arrays.asList(ChatColor.WHITE + "Tier one gear."));
        item.setItemMeta(meta);

        customTierOneSword = new ShapedRecipe(item);
        customTierOneSword.shape(
                "@  ",
                "@  ",
                "#  ");
        customTierOneSword.setIngredient('@',Material.IRON_BLOCK);
        customTierOneSword.setIngredient('#',Material.STICK);
        Bukkit.getServer().addRecipe(customTierOneSword);

        customTierOneSword2 = new ShapedRecipe(item);
        customTierOneSword2.shape(
                " @",
                " @",
                " #");
        customTierOneSword2.setIngredient('@',Material.IRON_BLOCK);
        customTierOneSword2.setIngredient('#',Material.STICK);
        Bukkit.getServer().addRecipe(customTierOneSword2);

        customTierOneSword3 = new ShapedRecipe(item);
        customTierOneSword3.shape(
                " @ ",
                " @ ",
                " # ");
        customTierOneSword3.setIngredient('@',Material.IRON_BLOCK);
        customTierOneSword3.setIngredient('#',Material.STICK);
        Bukkit.getServer().addRecipe(customTierOneSword3);
        tierOneSwordIS = item;
    }

    private static void CustomTierOneShield(){

        //ItemStack item = CustomItem.getItemFromCustomItem(MainCombat.fileSystem.createItem((new ItemStack(Material.WOOD_SWORD,1)),Tier.five,ItemType.SWORD ));
        ItemStack item = new ItemStack(Material.SHIELD, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GRAY + "Tier One Shield");
        meta.setLore(Arrays.asList(ChatColor.WHITE + "Tier one gear."));
        item.setItemMeta(meta);

        customTierOneShield = new ShapedRecipe(item);
        customTierOneShield.shape(
                "@ @",
                "@#@",
                " @ "
        );
        customTierOneShield.setIngredient('@',Material.IRON_BLOCK);
        customTierOneShield.setIngredient('#',Material.IRON_FENCE);
        customTierOneShield = new ShapedRecipe(item);
        customTierOneShield.shape(
                "@ @",
                "@#@",
                " @ "
        );
        customTierOneShield.setIngredient('@',Material.IRON_INGOT);
        customTierOneShield.setIngredient('#',Material.IRON_FENCE);

        Bukkit.getServer().addRecipe(customTierOneShield);
        tierOneShieldIS = item;
    }
    private static void CustomTierOneArrow(){

        //ItemStack item = CustomItem.getItemFromCustomItem(MainCombat.fileSystem.createItem((new ItemStack(Material.WOOD_SWORD,1)),Tier.five,ItemType.SWORD ));
        ItemStack item = new ItemStack(Material.ARROW, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GRAY + "Tier One Arrow");
        meta.setLore(Arrays.asList(ChatColor.WHITE + "Tier one gear."));
        item.setItemMeta(meta);

        customTierOneArrow = new ShapedRecipe(item);
        customTierOneArrow.shape(
                "  @",
                "  #",
                "   "
        );
        customTierOneArrow.setIngredient('@',Material.IRON_BLOCK);
        customTierOneArrow.setIngredient('#',Material.STICK);

        Bukkit.getServer().addRecipe(customTierOneArrow);
        tierOneArrowIS = item;
    }
    private static void CustomTierOneBow(){

        //ItemStack item = CustomItem.getItemFromCustomItem(MainCombat.fileSystem.createItem((new ItemStack(Material.WOOD_SWORD,1)),Tier.five,ItemType.SWORD ));
        ItemStack item = new ItemStack(Material.BOW, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GRAY + "Tier One Bow");
        meta.setLore(Arrays.asList(ChatColor.WHITE + "Tier one gear."));
        item.setItemMeta(meta);

        customTierOneBow = new ShapedRecipe(item);
        customTierOneBow.shape(
                " @#",
                "@ #",
                " @#"
        );
        customTierOneBow.setIngredient('@',Material.IRON_BLOCK);
        customTierOneBow.setIngredient('#',Material.STRING);
        customTierOneBow.shape(
                " @#",
                "@ #",
                " @#"
        );
        customTierOneBow.setIngredient('@',Material.IRON_INGOT);
        customTierOneBow.setIngredient('#',Material.STRING);


        Bukkit.getServer().addRecipe(customTierOneBow);
        tierOneBowIS = item;
    }
    private static void  CustomTierOnePike(){
        ItemStack item = new ItemStack(Material.IRON_SPADE,1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GRAY + "Tier One Pike");
        meta.setLore(Arrays.asList(ChatColor.WHITE + "Deals bonus damage to mounted foes."));
        item.setItemMeta(meta);

        customTierOnePike = new ShapedRecipe(item);
        customTierOnePike.shape(
                "  @",
                " @ ",
                "#  "
        );
        customTierOnePike.shape(
                "@  ",
                " @ ",
                "  #"
        );
        customTierOnePike.setIngredient('@',Material.IRON_BLOCK);
        customTierOnePike.setIngredient('#',Material.STICK);
        Bukkit.getServer().addRecipe(customTierOnePike);
        tierOnePikeIS = item;
    }
    private static void  CustomTierOneLance(){
        ItemStack item = new ItemStack(Material.IRON_SPADE,1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GRAY + "Tier One Lance");
        meta.setLore(Arrays.asList(ChatColor.WHITE + "Deals bonus damage when used from horseback."));
        item.setItemMeta(meta);

        customTierOneLance = new ShapedRecipe(item);
        customTierOneLance.shape(
                "  @",
                " # ",
                "#  "
        );
        customTierOneLance.shape(
                "@  ",
                " # ",
                "  #"
        );
        customTierOneLance.setIngredient('@',Material.IRON_BLOCK);
        customTierOneLance.setIngredient('#',Material.STICK);
        Bukkit.getServer().addRecipe(customTierOneLance);
        tierOneLanceIS = item;
    }


    public static void registerTierTwo(){
        ItemMeta meta;

        tierTwoHelm = new ItemStack(Material.IRON_HELMET, 1);
        meta = tierTwoHelm.getItemMeta();
        meta.setDisplayName(ChatColor.GRAY + "Tier Two Helm");
        meta.setLore(Arrays.asList(ChatColor.WHITE + "Tier Two gear."));
        tierTwoHelm.setItemMeta(meta);

        tierTwoChest = new ItemStack(Material.IRON_CHESTPLATE, 1);
        meta = tierTwoChest.getItemMeta();
        meta.setDisplayName(ChatColor.GRAY + "Tier Two Chestplate");
        meta.setLore(Arrays.asList(ChatColor.WHITE + "Tier Two gear."));
        tierTwoChest.setItemMeta(meta);

        tierTwoLegs = new ItemStack(Material.IRON_LEGGINGS, 1);
        meta = tierTwoLegs.getItemMeta();
        meta.setDisplayName(ChatColor.GRAY + "Tier Two Leggings");
        meta.setLore(Arrays.asList(ChatColor.WHITE + "Tier Two gear."));
        tierTwoLegs.setItemMeta(meta);

        tierTwoBoots = new ItemStack(Material.IRON_BOOTS, 1);
        meta = tierTwoBoots.getItemMeta();
        meta.setDisplayName(ChatColor.GRAY + "Tier Two Boots");
        meta.setLore(Arrays.asList(ChatColor.WHITE + "Tier Two gear."));
        tierTwoBoots.setItemMeta(meta);

        tierTwoSword = new ItemStack(Material.IRON_SWORD, 1);
        meta = tierTwoSword.getItemMeta();
        meta.setDisplayName(ChatColor.GRAY + "Tier Two Sword");
        meta.setLore(Arrays.asList(ChatColor.WHITE + "Tier Two gear."));
        tierTwoSword.setItemMeta(meta);

        //More custom items pikes, lances, bows arrows, shields.

        tierTwoPike = new ItemStack(Material.IRON_SPADE, 1);
        meta = tierTwoPike.getItemMeta();
        meta.setDisplayName(ChatColor.GRAY + "Tier Two Pike");
        meta.setLore(Arrays.asList(ChatColor.WHITE + "Tier Two gear."));
        tierTwoPike.setItemMeta(meta);

        tierTwoLance = new ItemStack(Material.IRON_SPADE, 1);
        meta = tierTwoLance.getItemMeta();
        meta.setDisplayName(ChatColor.GRAY + "Tier Two Lance");
        meta.setLore(Arrays.asList(ChatColor.WHITE + "Tier Two gear."));
        tierTwoLance.setItemMeta(meta);

        tierTwoBow = new ItemStack(Material.BOW, 1);
        meta = tierTwoBow.getItemMeta();
        meta.setDisplayName(ChatColor.GRAY + "Tier Two Bow");
        meta.setLore(Arrays.asList(ChatColor.WHITE + "Tier Two gear."));
        tierTwoBow.setItemMeta(meta);

        tierTwoArrow = new ItemStack(Material.ARROW, 1);
        meta = tierTwoArrow.getItemMeta();
        meta.setDisplayName(ChatColor.GRAY + "Tier Two Arrow");
        meta.setLore(Arrays.asList(ChatColor.WHITE + "Tier Two gear."));
        tierTwoArrow.setItemMeta(meta);

        tierTwoShield = new ItemStack(Material.SHIELD, 1);
        meta = tierTwoShield.getItemMeta();
        meta.setDisplayName(ChatColor.GRAY + "Tier Two Shield");
        meta.setLore(Arrays.asList(ChatColor.WHITE + "Tier Two gear."));
        tierTwoShield.setItemMeta(meta);
    }

    public static void registerTierOne(){
        ItemMeta meta;

        tierOneHelm = new ItemStack(Material.IRON_HELMET, 1);
        meta = tierOneHelm.getItemMeta();
        meta.setDisplayName(ChatColor.GRAY + "Tier One Helm");
        meta.setLore(Arrays.asList(ChatColor.WHITE + "Tier One gear."));
        tierOneHelm.setItemMeta(meta);

        tierOneChest = new ItemStack(Material.IRON_CHESTPLATE, 1);
        meta = tierOneChest.getItemMeta();
        meta.setDisplayName(ChatColor.GRAY + "Tier One Chestplate");
        meta.setLore(Arrays.asList(ChatColor.WHITE + "Tier One gear."));
        tierOneChest.setItemMeta(meta);

        tierOneLegs = new ItemStack(Material.IRON_LEGGINGS, 1);
        meta = tierOneLegs.getItemMeta();
        meta.setDisplayName(ChatColor.GRAY + "Tier One Leggings");
        meta.setLore(Arrays.asList(ChatColor.WHITE + "Tier One gear."));
        tierOneLegs.setItemMeta(meta);

        tierOneBoots = new ItemStack(Material.IRON_BOOTS, 1);
        meta = tierOneBoots.getItemMeta();
        meta.setDisplayName(ChatColor.GRAY + "Tier One Boots");
        meta.setLore(Arrays.asList(ChatColor.WHITE + "Tier One gear."));
        tierOneBoots.setItemMeta(meta);

        tierOneSword = new ItemStack(Material.IRON_SWORD, 1);
        meta = tierOneSword.getItemMeta();
        meta.setDisplayName(ChatColor.GRAY + "Tier One Sword");
        meta.setLore(Arrays.asList(ChatColor.WHITE + "Tier One gear."));
        tierOneSword.setItemMeta(meta);

        //More custom items pikes, lances, bows arrows, shields.

        tierOnePike = new ItemStack(Material.IRON_SPADE, 1);
        meta = tierOnePike.getItemMeta();
        meta.setDisplayName(ChatColor.GRAY + "Tier One Pike");
        meta.setLore(Arrays.asList(ChatColor.WHITE + "Tier One gear."));
        tierOnePike.setItemMeta(meta);

        tierOneLance = new ItemStack(Material.IRON_SPADE, 1);
        meta = tierOneLance.getItemMeta();
        meta.setDisplayName(ChatColor.GRAY + "Tier One Lance");
        meta.setLore(Arrays.asList(ChatColor.WHITE + "Tier One gear."));
        tierOneLance.setItemMeta(meta);

        tierOneBow = new ItemStack(Material.BOW, 1);
        meta = tierOneBow.getItemMeta();
        meta.setDisplayName(ChatColor.GRAY + "Tier One Bow");
        meta.setLore(Arrays.asList(ChatColor.WHITE + "Tier One gear."));
        tierOneBow.setItemMeta(meta);

        tierOneShield = new ItemStack(Material.SHIELD, 1);
        meta = tierOneShield.getItemMeta();
        meta.setDisplayName(ChatColor.GRAY + "Tier One Shield");
        meta.setLore(Arrays.asList(ChatColor.WHITE + "Tier One gear."));
        tierOneShield.setItemMeta(meta);

        tierOneArrow = new ItemStack(Material.ARROW, 1);
        meta = tierOneArrow.getItemMeta();
        meta.setDisplayName(ChatColor.GRAY + "Tier One Arrow");
        meta.setLore(Arrays.asList(ChatColor.WHITE + "Tier One gear."));
        tierOneArrow.setItemMeta(meta);
    }

}


