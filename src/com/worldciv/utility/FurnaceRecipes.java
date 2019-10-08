package com.worldciv.utility;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class FurnaceRecipes {

    public static void registerFurnaceRecipes(){
        SmeltToSteel();
    }

    private static void SmeltToSteel(){

        ItemStack resultitem = new ItemStack(Material.IRON_INGOT, 1);
        ItemMeta resultitemmeta = resultitem.getItemMeta();
        resultitemmeta.setLore(Arrays.asList(ChatColor.GRAY +
                "Steel is a core ingredient",ChatColor.GRAY +"to create tier 2 equipment.",
                ChatColor.GRAY + "It is also the base material to create higher ", ChatColor.GRAY+"tier crafting materials.", "",
                ChatColor.YELLOW + "Follow the guide on the /wc links"));

        resultitemmeta.setDisplayName(ChatColor.GRAY + "" +  ChatColor.BOLD + "Steel");
        resultitem.setItemMeta(resultitemmeta);

        Material sourceitem =  Material.IRON_INGOT;

        FurnaceRecipe recipe = new FurnaceRecipe(resultitem, sourceitem);

        Bukkit.getServer().addRecipe(recipe);

    }
}
