package com.worldciv.filesystem;

import com.worldciv.the60th.Main;
import com.worldciv.utility.*;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

//TODO Fix word list RNG?
public class ItemGenerator {
    //Should make different min-max for armor vs weapons.
    private static final int tierhalfMin = 4;
    private static final int tierhalfMax = 8;
    private static final int tierOneMin = 7;
    private static final int tierOneMax = 13;
    private static final int tieronehalfMin = 10;
    private static final int tieronehalfMax = 15;
    private static final int tierTwoMin = 14;
    private static final int tierTwoMax = 19;

    //Tiers below this not needed.
    private static final int tierThreeMin = 7;
    private static final int tierThreeMax = 11;

    private static final int tierFourMin = 9;
    private static final int tierFourMax = 13;

    private static final int tierFiveMin = 10;
    private static final int tierFiveMax = 16;

    private static final int commonMod = 0;
    private static final int uncommonMod = 3;
    private static final int rareMod = 5;
    private static final int epicMod = 7;
    private static final int legendaryMod = 10;


    public static CustomItem generateItem(ItemStack itemStack, Tier tier){

        return null;
    }

    //TODO Create methoods that you pass a custom name to,
    //For now just check names within the generator.
    public static CustomItem generateItem(ItemStack itemStack, Tier tier,ItemType itemType){
        int damage = 0;
        int armor = 0;
        Rarity rarity = calculateRarity(0);

        if(itemType == ItemType.ARROW){
            damage = calculateStat(rarity,tier);
        }
        else if(itemType == ItemType.AXE){
            damage = calculateStat(rarity,tier);
        }
        else if(itemType == ItemType.BOW){
            damage = calculateStat(rarity,tier);
        }
        else if(itemType == ItemType.SHIELD){
            damage = (calculateStat(rarity,tier)/3);
            if(damage < 1) damage = 1;
            armor = (calculateStat(rarity,tier));
            if(armor <1) armor = 1;
        }
        else if(itemType == ItemType.SWORD){
            damage = calculateStat(rarity,tier);
        }else if(itemType == ItemType.LANCE){
            damage = calculateStat(rarity,tier);
        }else if(itemType == ItemType.PIKE){
            damage = calculateStat(rarity,tier);
        }else if(itemType == ItemType.HELM){
            armor = (calculateStat(rarity,tier));
            if(armor <= 0){armor = 1;}
        }
        else if(itemType == ItemType.CHESTPLATE){
            armor = (calculateStat(rarity,tier));
            if(armor <= 0){armor = 1;}
        }
        else if(itemType == ItemType.LEGGINGS){
            armor = (calculateStat(rarity,tier));
            if(armor <= 0){armor = 1;}
        }
        else if(itemType == ItemType.BOOTS){
            armor = (calculateStat(rarity,tier));
            if(armor <= 0){armor = 1;}
        }
        String name = getItemName(itemType,tier);
        String id = CustomItem.unhideItemUUID(createUUID());
        id = convertToInvisibleString(id);
        return new CustomItem(itemStack,name,id,damage,armor,rarity,tier,itemType);
    }
    private static Rarity calculateRarity(double modifier){
        //Add checks for the modifier later on.
        Random random = new Random(System.currentTimeMillis());
        int x = random.nextInt(100)+1;
        if(isBetween(x,0,75)){ return Rarity.Common; }
        else if(isBetween(x,75,85)){return Rarity.Uncommon;}
        else if(isBetween(x,85,94)){return Rarity.Rare;}
        else if(isBetween(x,94,98)){return Rarity.Epic;}
        else if(isBetween(x,98,100)){return Rarity.Legendary;}
        else{
            Main.logger.info(("Rarity generation error has happened."));
            return Rarity.Common;
        }
    }
    private static int calculateStat(Rarity rarity,Tier tier){
        int calculatedValue;
        switch (tier){
            case tempHalfTier:
                calculatedValue = ThreadLocalRandom.current().nextInt(tierhalfMin, tierhalfMax + 1);
                break;
            case tempHalfTier2:
                calculatedValue = ThreadLocalRandom.current().nextInt(tieronehalfMin, tieronehalfMax + 1);
                break;
            case I:
                 calculatedValue = ThreadLocalRandom.current().nextInt(tierOneMin, tierOneMax + 1);
                break;
            case II:
                 calculatedValue = ThreadLocalRandom.current().nextInt(tierTwoMin, tierTwoMax + 1);
                break;
            case III:
                 calculatedValue = ThreadLocalRandom.current().nextInt(tierFourMin, tierThreeMax + 1);
                break;
            case IV:
                 calculatedValue = ThreadLocalRandom.current().nextInt(tierThreeMin, tierFourMax + 1);
                break;
            case V:
                calculatedValue = ThreadLocalRandom.current().nextInt(tierFiveMin, tierFiveMax + 1);
                break;
            default:
                calculatedValue = -1;
                break;
        }
        switch (rarity){
            case Common:
                calculatedValue = calculatedValue + commonMod;
                break;
            case Uncommon:
                calculatedValue = calculatedValue + uncommonMod;
                break;
            case Rare:
                calculatedValue = calculatedValue + rareMod;
                break;
            case Epic:
                calculatedValue = calculatedValue + epicMod;
                break;
            case Legendary:
                calculatedValue = calculatedValue + legendaryMod;
                break;
            default:
                calculatedValue = -1;
                break;
        }
        return calculatedValue;
    }
    public static ChatColor getColorFromRarity(Rarity rarity){
        switch (rarity){
            case Common:
                return ChatColor.WHITE;
            case Uncommon:
                return ChatColor.GREEN;
            case Rare:
                return ChatColor.BLUE;
            case Epic:
                return ChatColor.DARK_PURPLE;
            case Legendary:
                return ChatColor.GOLD;
            default:
                return ChatColor.RED;
        }
    }
    private  static String getItemName(ItemType itemType, Tier tier){
        String x =  checkItemType(itemType).toString().toLowerCase();
        String capitalletter = x.substring(0,1).toUpperCase();
        String rest_of_words = x.substring(1);
        String material_name = capitalletter + rest_of_words;

        return /*createRandomName() +*/ getMaterialByTier(tier) +" "+ x;
    }
    private static String createRandomName(){
        Random random = new Random();
        int x = random.nextInt(6)+1;
        switch (x){
            case 1:
                return WordLists.myWordListAppearance[random.nextInt(WordLists.myWordListAppearance.length)] + " ";
            case 2:
                return WordLists.myWordListColor[random.nextInt(WordLists.myWordListColor.length)] + " ";
            case 3:
                return WordLists.myWordListSize[random.nextInt(WordLists.myWordListSize.length)] + " ";
            case 4:
                return WordLists.myWordListTime[random.nextInt(WordLists.myWordListTime.length)] + " ";
            case 5:
                return WordLists.myWordListTouch[random.nextInt(WordLists.myWordListTouch.length)] + " ";
            case 6:
                break;
            default:
                x = random.nextInt(5)+1;
                String string = "";
                switch (x) {
                    case 1:
                        string = string + WordLists.myWordListAppearance[random.nextInt(WordLists.myWordListAppearance.length)] + " ";
                    case 2:
                        string = string + WordLists.myWordListColor[random.nextInt(WordLists.myWordListColor.length)] + " ";
                    case 3:
                        string = string + WordLists.myWordListSize[random.nextInt(WordLists.myWordListSize.length)] + " ";
                    case 4:
                        string = string + WordLists.myWordListTime[random.nextInt(WordLists.myWordListTime.length)] + " ";
                    case 5:
                        string = string + WordLists.myWordListTouch[random.nextInt(WordLists.myWordListTouch.length)] + " ";
                }
                x = random.nextInt(5)+1;
                switch (x) {
                    case 1:
                        string = string + WordLists.myWordListAppearance[random.nextInt(WordLists.myWordListAppearance.length)] + " ";
                    case 2:
                        string = string + WordLists.myWordListColor[random.nextInt(WordLists.myWordListColor.length)] + " ";
                    case 3:
                        string = string + WordLists.myWordListSize[random.nextInt(WordLists.myWordListSize.length)] + " ";
                    case 4:
                        string = string + WordLists.myWordListTime[random.nextInt(WordLists.myWordListTime.length)] + " ";
                    case 5:
                        string = string + WordLists.myWordListTouch[random.nextInt(WordLists.myWordListTouch.length)] + " ";
                }
                return string;
        }
        return "Bob's olde ";
    }



    private static boolean isBetween(int x, int lower, int upper) {
        return lower <= x && x <= upper;
    }
    private static String createUUID(){
        return UUID.randomUUID().toString();
    }
    private static String convertToInvisibleString(String s) {
        String hidden = "";
        for (char c : s.toCharArray()) hidden += ChatColor.COLOR_CHAR+""+c;
        return hidden;
    }

    private static ItemType checkItemType(ItemType itemType){
        switch (itemType){
            case SHIELD:
                return ItemType.SHIELD;
            case AXE:
                return ItemType.AXE;
            case SWORD:
                return ItemType.SWORD;
            case BOW:
                return ItemType.BOW;
            case ARROW:
                return ItemType.ARROW;
            case PIKE:
                return ItemType.PIKE;
            case LANCE:
                return ItemType.LANCE;
            case HELM:
                return ItemType.HELM;
            case CHESTPLATE:
                return ItemType.CHESTPLATE;
            case LEGGINGS:
                return  ItemType.LEGGINGS;
            case BOOTS:
                return ItemType.BOOTS;
        }
        return ItemType.DEFAULT;
    }

    private static String getMaterialByTier(Tier tier){
        switch (tier){
            case tempHalfTier:
                return "Iron";
            case I:
                return "Refined Iron";
            case tempHalfTier2:
                return "Refined Iron";
            case II:
                return "Steel";
            case III:
                return "Hard Steel";
            case IV:
                return "Meteorite";
            case V:
                return "Dragon Steel";
        }
        return "Basic";
    }
}
