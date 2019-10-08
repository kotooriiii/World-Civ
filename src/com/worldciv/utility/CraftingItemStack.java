package com.worldciv.utility;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class CraftingItemStack {

    public static ItemStack getSteelAnvil(){
        ItemStack is = new ItemStack(Material.ANVIL, 1);
        ItemMeta im = is.getItemMeta();
        List<String> list = Arrays.asList(ChatColor.GRAY + "A steel anvil is more", ChatColor.GRAY + "resilient when custom crafting.", "", ChatColor.DARK_AQUA + "Damage Reduction:" + ChatColor.YELLOW + " 50%");
        im.setLore(list);
        im.setDisplayName(ChatColor.GRAY + "" + ChatColor.BOLD + "Steel Anvil");
        is.setItemMeta(im);

        return is;
    }

    public static ItemMeta getSteelAnvilItemMeta(){
        ItemStack is = new ItemStack(Material.ANVIL, 1);
        ItemMeta im = is.getItemMeta();
        List<String> list = Arrays.asList(ChatColor.GRAY + "A steel anvil is more", ChatColor.GRAY + "resilient when custom crafting.", "", ChatColor.DARK_AQUA + "Damage Reduction:" + ChatColor.YELLOW + " 50%");
        im.setLore(list);
        im.setDisplayName(ChatColor.GRAY + "" + ChatColor.BOLD + "Steel Anvil");
        return im;
    }
}
