package com.worldciv.events.inventory;

import com.worldciv.utility.Fanciful.mkremins.fanciful.FancyMessage;
import net.minecraft.server.v1_12_R1.IChatBaseComponent;
import net.minecraft.server.v1_12_R1.Packet;
import net.minecraft.server.v1_12_R1.PacketPlayOutTitle;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Random;

import static com.worldciv.the60th.Main.fileSystem;
import static com.worldciv.utility.utilityStrings.worldciv;

public class AnvilCreate implements Listener {

    public static boolean isAllFull(HumanEntity p, ItemStack item) {

        for (int i = 0; i < 35; i++) { //iterate through all inv slots

            if ((p.getInventory().getItem(i) == null)) { //empty slot counts as null
                return false;
            }

            if (p.getInventory().getItem(i).getAmount() + item.getAmount() <= item.getMaxStackSize()) {
                if (p.getInventory().getItem(i).getType().equals(item.getType())) {

                    //add item we found space !
                    return false;
                }
            }
            if (i == 34) {
                // no slots available
                return true;

            }
        }
        return true;

    }

    public void anvilBreakShiftLeft(Block anvil, HumanEntity p){

        for(int y = 0; y < 5; y++) {
            Random r = new Random();
            int x;
            if (fileSystem.getBlocksList("steel anvil").contains(anvil.getLocation())) {
                x = r.nextInt(150);

            } else {
                x = r.nextInt(100);
            }
            if (anvil.getData() < Byte.valueOf("6")) {
                if (x <= 5) {
                    anvil.setData(Byte.valueOf("6"));
                    anvil.getState().update();
                    p.sendMessage(worldciv + ChatColor.RED + " The anvil is starting to tear apart...");
                }
            } else if (anvil.getData() < Byte.valueOf("10")) {

                if (x <= 5) {
                    anvil.setData(Byte.valueOf("11"));
                    anvil.getState().update();
                    p.sendMessage(worldciv + ChatColor.RED + " The anvil is in critical health!");

                }
            } else if (anvil.getData() <= Byte.valueOf("11")) {

                if (x <= 10) {
                    anvil.setType(Material.AIR);
                    p.sendMessage(worldciv + ChatColor.RED + " The anvil broke from heavy forging!");
                    ((Player) p).playSound(anvil.getLocation(), Sound.BLOCK_ANVIL_DESTROY, 3L, 0L);
                }
            }
        }


    }

    public void anvilBreakSingleClick(Block anvil, HumanEntity p){
        Random r = new Random();
        int x;
        if (fileSystem.getBlocksList("steel anvil").contains(anvil.getLocation())) {
            x = r.nextInt(150);
        } else {
            x = r.nextInt(100);
        }

        if (anvil.getData() < Byte.valueOf("6")) {
            if (x <= 5) {
                anvil.setData(Byte.valueOf("6"));
                anvil.getState().update();
                p.sendMessage(worldciv + ChatColor.RED + " The anvil is starting to tear apart...");
            }
        } else if (anvil.getData() < Byte.valueOf("10")) {

            if (x <= 5) {
                anvil.setData(Byte.valueOf("11"));
                anvil.getState().update();
                p.sendMessage(worldciv + ChatColor.RED + " The anvil is in critical health!");
            }
        } else if (anvil.getData() <= Byte.valueOf("11")) {

            if (x <= 10) {
                anvil.setType(Material.AIR);
                ((Player) p).playSound(anvil.getLocation(), Sound.BLOCK_ANVIL_DESTROY, 3L, 0L);
                p.sendMessage(worldciv + ChatColor.RED + " The anvil broke from heavy forging!");
            }
        }

    }

    public static void AnvilID(Player p) {

        Block b = p.getTargetBlock(null, 20);

        if (b.getType().equals(Material.ANVIL)) {

            if (fileSystem.getBlocksList("steel anvil").contains(b.getLocation())) {
                String x = new FancyMessage("Steel Anvil").color(ChatColor.GRAY).toJSONString();

                // Title or subtitle, text, fade in (ticks), display time (ticks), fade out (ticks).
                PacketPlayOutTitle title = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, IChatBaseComponent.ChatSerializer.a(x), 1, 3, 1);
             //   PacketPlayOutTitle subtitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, IChatBaseComponent.ChatSerializer.a("{\"text\":\"to my server\"}"), 20, 40, 20);
                ((CraftPlayer) p.getPlayer()).getHandle().playerConnection.sendPacket(title);
                // ((CraftPlayer) p.getPlayer()).getHandle().playerConnection.sendPacket(subtitle);
            } else {
                String x = new FancyMessage("Standard Anvil").color(ChatColor.GRAY).toJSONString();

                PacketPlayOutTitle title = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, IChatBaseComponent.ChatSerializer.a(x), 1, 5, 1);
                ((CraftPlayer) p.getPlayer()).getHandle().playerConnection.sendPacket(title);
            }
        }
    }

    @EventHandler
    public void onAnvilInteraction(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        Block b = e.getClickedBlock();

        if(b == null){
            return;
        }

        if(b.getType() == null){
            return;
        }

        if (b.getType() == Material.ANVIL){
            if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
                if(p.isSneaking()){
                    AnvilID(p);
                }
            }
    }

}

    @EventHandler
    public void onAnvilPrepare(PrepareAnvilEvent e) {
        AnvilInventory anvilInv = e.getInventory();
        ItemStack[] itemsInAnvil = anvilInv.getContents();

        if ((itemsInAnvil[0] == null) || (itemsInAnvil[1] == null)) { //if null
            return;
        }

        //This is where all the functions for anvil recipes go. Not actual click event!
        RecipeRefinedIronIngot(itemsInAnvil, e);
        RecipeCokeFuel(itemsInAnvil, e);
        RecipeActivatedCarbon(itemsInAnvil, e);
        dummyLoreRecipeItem(itemsInAnvil, e);

    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        HumanEntity p = e.getWhoClicked();
        if (p instanceof Player) { //clicker is player

            if (e.getView().getType() == InventoryType.ANVIL) { // detects if its an anvil

                AnvilInventory anvilInv = (AnvilInventory) e.getInventory(); //get anviilinv
                int slot = e.getRawSlot(); //slots of anvil

                if (slot == 2) { //clicking on 2nd slot
                    ItemStack[] itemsInAnvil = anvilInv.getContents(); //get anvil slots

                    if ((itemsInAnvil[0] == null) || (itemsInAnvil[1] == null)) { //if null
                        return;
                    }

                    //This is where all the functions for item clicking goes. Not actual recipe display!
                    ClickRefinedIronIngot(p, itemsInAnvil, e); //Example Item To Click And Receive
                    ClickCokeFuel(p, itemsInAnvil, e);
                    ClickActivatedCarbon(p, itemsInAnvil, e);
                    dummyLoreClickItem(p, itemsInAnvil, e);

                }
            }
        }
    }

    private void ClickRefinedIronIngot(HumanEntity p, ItemStack[] itemsInAnvil, InventoryClickEvent e) {  // Dummy Item Click Event: Material + Material = ItemStack

        Material firstitem = Material.IRON_INGOT;
        Material seconditem = Material.IRON_INGOT;

        ItemStack resultitem = new ItemStack(Material.IRON_INGOT, 1); //dummy item
        ItemMeta resultitemmeta = resultitem.getItemMeta();
        resultitemmeta.setLore(Arrays.asList(ChatColor.GRAY + "The refined iron ingot is the", ChatColor.GRAY + "core ingredient to create equipment.", ChatColor.GRAY + "It is also the base material to create steel.", "", ChatColor.YELLOW + "Follow the guide on the /wc links"));
        resultitemmeta.setDisplayName(ChatColor.GRAY + "Refined Iron Ingot");
        resultitem.setItemMeta(resultitemmeta);

        ItemStack resultitem5 = new ItemStack(Material.IRON_INGOT, 5); //dummy item as 5, don't change 5, some stuff with 5 is hardcoded. i'd keep as 5 since multiples of 5 are really nice.
        ItemMeta resultitem5meta = resultitem5.getItemMeta();
        resultitem5meta.setLore(Arrays.asList(ChatColor.GRAY + "The refined iron ingot is the", ChatColor.GRAY + "core ingredient to create equipment.", ChatColor.GRAY + "It is also the base material to create steel.", "", ChatColor.YELLOW + "Follow the guide on the /wc links"));
        resultitem5meta.setDisplayName(ChatColor.GRAY + "Refined Iron Ingot");
        resultitem5.setItemMeta(resultitem5meta);

        String resultname = "refined iron ingot(s)!"; //add exclamation mark and plural cases, this is what it prints: You got "x" resultname


        if (itemsInAnvil[0].getType() == firstitem && itemsInAnvil[1].getType() == seconditem) //If first slot is that and if second slot is that
        {

            Block anvil = e.getInventory().getLocation().getBlock();


            if (e.isShiftClick() && ((p.getInventory().firstEmpty() != -1) || !isAllFull(p, resultitem5))) {  //checking if shift click and is not full for 5!

                if (itemsInAnvil[0].getAmount() < 5 || itemsInAnvil[1].getAmount() < 5) { //When you dont have 5 of the required items! in either slot!
                    p.sendMessage(worldciv + ChatColor.GRAY + " Not enough material to buy " + ChatColor.YELLOW + "5 " + resultname);
                    return;
                }

                p.sendMessage(worldciv + ChatColor.GRAY + " You have added " + ChatColor.YELLOW + "5 " + ChatColor.GRAY + resultname);

                if (itemsInAnvil[0].getAmount() == 5) { //check for first slot has 5 items
                    itemsInAnvil[0].setAmount(-1); //remove them to emptiness
                }

                if (itemsInAnvil[1].getAmount() == 5) { //same as above
                    itemsInAnvil[1].setAmount(-1);
                }

                anvilBreakShiftLeft(anvil, p);

                itemsInAnvil[0].setAmount(itemsInAnvil[0].getAmount() - 5); //Removes 5 every time! hype! Once you have emptiness these lines dont matter. the more negative the more emptiness!
                itemsInAnvil[1].setAmount(itemsInAnvil[1].getAmount() - 5);

                p.getInventory().addItem(resultitem5); //Add that item! ohayo~!

                ((Player) p).updateInventory(); //update inventory cos glitchy sometimes!

            } else if (!isAllFull(p, resultitem) || (p.getInventory().firstEmpty() != -1)) { //same as the check before this. just single clicks

                p.sendMessage(worldciv + ChatColor.GRAY + " You have added " + ChatColor.YELLOW + "1 " + ChatColor.GRAY + resultname);

                if (itemsInAnvil[0].getAmount() == 1) { //same as stuff above with 5, no need to spam here now..
                    itemsInAnvil[0].setAmount(-1);
                }

                if (itemsInAnvil[1].getAmount() == 1) {
                    itemsInAnvil[1].setAmount(-1);
                }


                anvilBreakSingleClick(anvil, p);
                itemsInAnvil[0].setAmount(itemsInAnvil[0].getAmount() - 1);
                itemsInAnvil[1].setAmount(itemsInAnvil[1].getAmount() - 1);


                p.getInventory().addItem(resultitem);

                ((Player) p).updateInventory();


            } else {

                p.sendMessage(worldciv + ChatColor.GRAY + " Inventory is full! You can't forge this!"); //no inv space :)

            }
        }

    }

    private void ClickCokeFuel(HumanEntity p, ItemStack[] itemsInAnvil, InventoryClickEvent e) {  // Dummy Item Click Event: Material + Material = ItemStack

        Material firstitem = Material.COAL;
        Material seconditem = Material.COAL;

        ItemStack resultitem = new ItemStack(Material.COAL, 1); //dummy item
        ItemMeta resultitemmeta = resultitem.getItemMeta();
        resultitemmeta.setLore(Arrays.asList(ChatColor.DARK_GRAY + "Coke is a fuel with no", ChatColor.DARK_GRAY + "impurities and high burn time.", ChatColor.DARK_GRAY + "It is also the best fuel to create steel.", "", ChatColor.YELLOW + "Follow the guide on the /wc links"));
        resultitemmeta.setDisplayName(ChatColor.DARK_GRAY + "Coke (fuel)");
        resultitem.setItemMeta(resultitemmeta);

        ItemStack resultitem5 = new ItemStack(Material.COAL, 5); //dummy item as 5, don't change 5, some stuff with 5 is hardcoded. i'd keep as 5 since multiples of 5 are really nice.
        ItemMeta resultitem5meta = resultitem5.getItemMeta();
        resultitem5meta.setLore(Arrays.asList(ChatColor.DARK_GRAY + "Coke is a fuel with no", ChatColor.DARK_GRAY + "impurities and high burn time.", ChatColor.DARK_GRAY + "It is also the best fuel to create steel.", "", ChatColor.YELLOW + "Follow the guide on the /wc links"));
        resultitem5meta.setDisplayName(ChatColor.DARK_GRAY + "Coke (fuel)");
        resultitem5.setItemMeta(resultitem5meta);

        String resultname = "coke(s)!"; //add exclamation mark and plural cases, this is what it prints: You got "x" resultname


        if (itemsInAnvil[0].getType() == firstitem && itemsInAnvil[1].getType() == seconditem) //If first slot is that and if second slot is that
        {
            if(itemsInAnvil[0].getDurability() != 1 && itemsInAnvil[1].getDurability() != 1){ //this is coal coal from underground  *NOT* charcoal

                Block anvil = e.getInventory().getLocation().getBlock();


                if (e.isShiftClick() && ((p.getInventory().firstEmpty() != -1) || !isAllFull(p, resultitem5))) {  //checking if shift click and is not full for 5!


                    if (itemsInAnvil[0].getAmount() < 5 || itemsInAnvil[1].getAmount() < 5) { //When you dont have 5 of the required items! in either slot!
                        p.sendMessage(worldciv + ChatColor.GRAY + " Not enough material to buy " + ChatColor.YELLOW + "5 " + resultname);
                        return;
                    }

                    p.sendMessage(worldciv + ChatColor.GRAY + " You have added " + ChatColor.YELLOW + "5 " + ChatColor.GRAY + resultname);

                    if (itemsInAnvil[0].getAmount() == 5) { //check for first slot has 5 items
                        itemsInAnvil[0].setAmount(-1); //remove them to emptiness
                    }

                    if (itemsInAnvil[1].getAmount() == 5) { //same as above
                        itemsInAnvil[1].setAmount(-1);
                    }

                    anvilBreakShiftLeft(anvil, p);

                    itemsInAnvil[0].setAmount(itemsInAnvil[0].getAmount() - 5); //Removes 5 every time! hype! Once you have emptiness these lines dont matter. the more negative the more emptiness!
                    itemsInAnvil[1].setAmount(itemsInAnvil[1].getAmount() - 5);

                    p.getInventory().addItem(resultitem5); //Add that item! ohayo~!

                    ((Player) p).updateInventory(); //update inventory cos glitchy sometimes!

                } else if (!isAllFull(p, resultitem) || (p.getInventory().firstEmpty() != -1)) { //same as the check before this. just single clicks

                    p.sendMessage(worldciv + ChatColor.GRAY + " You have added " + ChatColor.YELLOW + "1 " + ChatColor.GRAY + resultname);

                    if (itemsInAnvil[0].getAmount() == 1) { //same as stuff above with 5, no need to spam here now..
                        itemsInAnvil[0].setAmount(-1);
                    }

                    if (itemsInAnvil[1].getAmount() == 1) {
                        itemsInAnvil[1].setAmount(-1);
                    }


                    anvilBreakSingleClick(anvil, p);
                    itemsInAnvil[0].setAmount(itemsInAnvil[0].getAmount() - 1);
                    itemsInAnvil[1].setAmount(itemsInAnvil[1].getAmount() - 1);


                    p.getInventory().addItem(resultitem);

                    ((Player) p).updateInventory();


                } else {

                    p.sendMessage(worldciv + ChatColor.GRAY + " Inventory is full! You can't forge this!"); //no inv space :)

                }
            }
        }

    }

    private void ClickActivatedCarbon(HumanEntity p, ItemStack[] itemsInAnvil, InventoryClickEvent e) {  // Dummy Item Click Event: Material + Material = ItemStack

        Material firstitem = Material.COAL;
        Material seconditem = Material.COAL;

        ItemStack resultitem = new ItemStack(Material.COAL, 1); //dummy item
        resultitem.setDurability((short) 1);
        ItemMeta resultitemmeta = resultitem.getItemMeta();
        resultitemmeta.setLore(Arrays.asList(ChatColor.DARK_GRAY + "Activated Carbon is a fuel", ChatColor.DARK_GRAY + "with medium-high burn time.", ChatColor.DARK_GRAY + "It is cheap fuel to create steel.", "", ChatColor.YELLOW + "Follow the guide on the /wc links"));
        resultitemmeta.setDisplayName(ChatColor.DARK_GRAY + "Activated Carbon");
        resultitem.setItemMeta(resultitemmeta);

        ItemStack resultitem5 = new ItemStack(Material.COAL, 5); //dummy item as 5, don't change 5, some stuff with 5 is hardcoded. i'd keep as 5 since multiples of 5 are really nice.
        resultitem5.setDurability((short) 1);
        ItemMeta resultitem5meta = resultitem5.getItemMeta();
        resultitem5meta.setLore(Arrays.asList(ChatColor.DARK_GRAY + "Activated Carbon is a fuel", ChatColor.DARK_GRAY + "with medium-high burn time.", ChatColor.DARK_GRAY + "It is cheap fuel to create steel.", "", ChatColor.YELLOW + "Follow the guide on the /wc links"));
        resultitem5meta.setDisplayName(ChatColor.DARK_GRAY + "Activated Carbon");
        resultitem5.setItemMeta(resultitem5meta);

        String resultname = "Activated Carbon(s)!"; //add exclamation mark and plural cases, this is what it prints: You got "x" resultname


        if (itemsInAnvil[0].getType() == firstitem && itemsInAnvil[1].getType() == seconditem) //If first slot is that and if second slot is that
        {
            if(itemsInAnvil[0].getDurability() == 1 && itemsInAnvil[1].getDurability() == 1){ //this is coal coal from underground  *NOT* charcoal

                Block anvil = e.getInventory().getLocation().getBlock();


                if (e.isShiftClick() && ((p.getInventory().firstEmpty() != -1) || !isAllFull(p, resultitem5))) {  //checking if shift click and is not full for 5!


                    if (itemsInAnvil[0].getAmount() < 5 || itemsInAnvil[1].getAmount() < 5) { //When you dont have 5 of the required items! in either slot!
                        p.sendMessage(worldciv + ChatColor.GRAY + " Not enough material to buy " + ChatColor.YELLOW + "5 " + resultname);
                        return;
                    }

                    p.sendMessage(worldciv + ChatColor.GRAY + " You have added " + ChatColor.YELLOW + "5 " + ChatColor.GRAY + resultname);

                    if (itemsInAnvil[0].getAmount() == 5) { //check for first slot has 5 items
                        itemsInAnvil[0].setAmount(-1); //remove them to emptiness
                    }

                    if (itemsInAnvil[1].getAmount() == 5) { //same as above
                        itemsInAnvil[1].setAmount(-1);
                    }

                    anvilBreakShiftLeft(anvil, p);

                    itemsInAnvil[0].setAmount(itemsInAnvil[0].getAmount() - 5); //Removes 5 every time! hype! Once you have emptiness these lines dont matter. the more negative the more emptiness!
                    itemsInAnvil[1].setAmount(itemsInAnvil[1].getAmount() - 5);

                    p.getInventory().addItem(resultitem5); //Add that item! ohayo~!

                    ((Player) p).updateInventory(); //update inventory cos glitchy sometimes!

                } else if (!isAllFull(p, resultitem) || (p.getInventory().firstEmpty() != -1)) { //same as the check before this. just single clicks

                    p.sendMessage(worldciv + ChatColor.GRAY + " You have added " + ChatColor.YELLOW + "1 " + ChatColor.GRAY + resultname);

                    if (itemsInAnvil[0].getAmount() == 1) { //same as stuff above with 5, no need to spam here now..
                        itemsInAnvil[0].setAmount(-1);
                    }

                    if (itemsInAnvil[1].getAmount() == 1) {
                        itemsInAnvil[1].setAmount(-1);
                    }


                    anvilBreakSingleClick(anvil, p);
                    itemsInAnvil[0].setAmount(itemsInAnvil[0].getAmount() - 1);
                    itemsInAnvil[1].setAmount(itemsInAnvil[1].getAmount() - 1);


                    p.getInventory().addItem(resultitem);

                    ((Player) p).updateInventory();


                } else {

                    p.sendMessage(worldciv + ChatColor.GRAY + " Inventory is full! You can't forge this!"); //no inv space :)

                }
            }
        }

    }

    private void RecipeRefinedIronIngot(ItemStack[] itemsInAnvil, PrepareAnvilEvent e) { //Dummy Recipe: Material + Material = Item Stack (DISPLAYONLY)

        Material firstitem = Material.IRON_INGOT;
        Material seconditem = Material.IRON_INGOT;

        ItemStack resultitem = new ItemStack(Material.IRON_INGOT, 1); //dummy item
        ItemMeta resultitemmeta = resultitem.getItemMeta();
        resultitemmeta.setLore(Arrays.asList(ChatColor.GRAY + "The refined iron ingot is the", ChatColor.GRAY + "core ingredient to create equipment.", ChatColor.GRAY + "It is also the base material to create steel.", "", ChatColor.YELLOW + "Follow the guide on the /wc links"));
        resultitemmeta.setDisplayName(ChatColor.GRAY + "Refined Iron Ingot");
        resultitem.setItemMeta(resultitemmeta);


        if (itemsInAnvil[0].getType() == firstitem && itemsInAnvil[1].getType() == seconditem) {  //if only stuff was as this easy..

            e.setResult(resultitem); //display the item
        }

    }

    private void RecipeCokeFuel(ItemStack[] itemsInAnvil, PrepareAnvilEvent e) { //Dummy Recipe: Material + Material = Item Stack (DISPLAYONLY)

        Material firstitem = Material.COAL;
        Material seconditem = Material.COAL;

        ItemStack resultitem = new ItemStack(Material.COAL, 1); //dummy item
        ItemMeta resultitemmeta = resultitem.getItemMeta();
        resultitemmeta.setLore(Arrays.asList(ChatColor.DARK_GRAY + "Coke is a fuel with no", ChatColor.DARK_GRAY + "impurities and high burn time.", ChatColor.DARK_GRAY + "It is also the best fuel to create steel.", "", ChatColor.YELLOW + "Follow the guide on the /wc links"));
        resultitemmeta.setDisplayName(ChatColor.DARK_GRAY + "Coke (fuel)");
        resultitem.setItemMeta(resultitemmeta);


        if (itemsInAnvil[0].getType() == firstitem && itemsInAnvil[1].getType() == seconditem) {  //if only stuff was as this easy..

            if (itemsInAnvil[0].getDurability() != 1 && itemsInAnvil[1].getDurability() != 1) {
                //THIS IS COAL
                e.setResult(resultitem); //display the item
            }
        }

    }

    private void RecipeActivatedCarbon(ItemStack[] itemsInAnvil, PrepareAnvilEvent e) { //Dummy Recipe: Material + Material = Item Stack (DISPLAYONLY)

        Material firstitem = Material.COAL;
        Material seconditem = Material.COAL;

        ItemStack resultitem = new ItemStack(Material.COAL, 1); //dummy item
        resultitem.setDurability((short) 1);
        ItemMeta resultitemmeta = resultitem.getItemMeta();
        resultitemmeta.setLore(Arrays.asList(ChatColor.DARK_GRAY + "Activated Carbon is a fuel", ChatColor.DARK_GRAY + "with medium-high burn time.", ChatColor.DARK_GRAY + "It is cheap fuel to create steel.", "", ChatColor.YELLOW + "Follow the guide on the /wc links"));
        resultitemmeta.setDisplayName(ChatColor.DARK_GRAY + "Activated Carbon");
        resultitem.setItemMeta(resultitemmeta);


        if (itemsInAnvil[0].getType() == firstitem && itemsInAnvil[1].getType() == seconditem) {  //if only stuff was as this easy..

            if (itemsInAnvil[0].getDurability() == 1 && itemsInAnvil[1].getDurability() == 1) {
                //THIS IS CHARCOAL
                e.setResult(resultitem); //display the item
            }
        }

    }


    private void dummyLoreClickItem(HumanEntity p, ItemStack[] itemsInAnvil, InventoryClickEvent e) {  // Dummy Item Click Event: ItemStack + ItemStack = ItemStack

        Material firstitem = Material.EGG;
        Material seconditem = Material.EGG;

        ItemStack item = new ItemStack(Material.EGG, 1); //dummy item
        ItemStack item5 = new ItemStack(Material.EGG, 5); //dummy item as 5, don't change 5, some stuff with 5 is hardcoded. i'd keep as 5 since multiples of 5 are really nice.

        String resultname = "egg(s)!"; //add exclamation mark and plural cases, this is what it prints: You got "x" resultname


        if (itemsInAnvil[0].getType() == firstitem && itemsInAnvil[1].getType() == seconditem) //If first slot is that and if second slot is that
        {
            Block anvil = e.getInventory().getLocation().getBlock();

            if (itemsInAnvil[0].getItemMeta().getLore().get(0) == "imisspetra" && itemsInAnvil[0].getItemMeta().getLore().get(0) == "imisspetra") {

                if (e.isShiftClick() && ((p.getInventory().firstEmpty() != -1) || !isAllFull(p, item5))) {  //checking if shift click and is not full for 5!

                    if (itemsInAnvil[0].getAmount() < 5 || itemsInAnvil[1].getAmount() < 5) { //When you dont have 5 of the required items! in either slot!
                        p.sendMessage(worldciv + ChatColor.GRAY + " Not enough material to buy " + ChatColor.YELLOW + "5 " + resultname);
                        return;
                    }

                    p.sendMessage(worldciv + ChatColor.GRAY + " You have added " + ChatColor.YELLOW + "5 " + ChatColor.GRAY + resultname);

                    if (itemsInAnvil[0].getAmount() == 5) { //check for first slot has 5 items
                        itemsInAnvil[0].setAmount(-1); //remove them to emptiness
                    }

                    if (itemsInAnvil[1].getAmount() == 5) { //same as above
                        itemsInAnvil[1].setAmount(-1);
                    }

                    anvilBreakShiftLeft(anvil, p);

                    itemsInAnvil[0].setAmount(itemsInAnvil[0].getAmount() - 5); //Removes 5 every time! hype! Once you have emptiness these lines dont matter. the more negative the more emptiness!
                    itemsInAnvil[1].setAmount(itemsInAnvil[1].getAmount() - 5);


                    p.getInventory().addItem(item5); //Add that item! ohayo~!

                    ((Player) p).updateInventory(); //update inventory cos glitchy sometimes!

                } else if (!isAllFull(p, item) || (p.getInventory().firstEmpty() != -1)) { //same as the check before this. just single clicks

                    p.sendMessage(worldciv + ChatColor.GRAY + " You have added " + ChatColor.YELLOW + "1 " + ChatColor.GRAY + resultname);

                    if (itemsInAnvil[0].getAmount() == 1) { //same as stuff above with 5, no need to spam here now..
                        itemsInAnvil[0].setAmount(-1);
                    }

                    if (itemsInAnvil[1].getAmount() == 1) {
                        itemsInAnvil[1].setAmount(-1);
                    }

                    itemsInAnvil[0].setAmount(itemsInAnvil[0].getAmount() - 1);
                    itemsInAnvil[1].setAmount(itemsInAnvil[1].getAmount() - 1);


                    anvilBreakSingleClick(anvil, p);

                    p.getInventory().addItem(item);

                    ((Player) p).updateInventory();


                } else {

                    p.sendMessage(worldciv + ChatColor.GRAY + " Inventory is full! You can't forge this!"); //no inv space :)

                }
            }
        }

    }

    private void dummyLoreRecipeItem(ItemStack[] itemsInAnvil, PrepareAnvilEvent e) { //Dummy Recipe: ItemStack + ItemStack = Item Stack (DISPLAYONLY)

        Material firstitem = Material.EGG;
        Material seconditem = Material.EGG;

        ItemStack resultitem = new ItemStack(Material.EGG, 1); //dummy item

        if (itemsInAnvil[0].getType() == firstitem && itemsInAnvil[1].getType() == seconditem) {  //if only stuff was as this easy..
            if (itemsInAnvil[0].getItemMeta().getLore().get(0) == "imisspetra" && itemsInAnvil[0].getItemMeta().getLore().get(0) == "imisspetra") {

                e.setResult(resultitem); //display the item
            }
        }

    }
}





