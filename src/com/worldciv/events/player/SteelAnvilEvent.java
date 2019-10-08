package com.worldciv.events.player;

import com.worldciv.events.custom.SteelAnvilBreakEvent;
import com.worldciv.events.custom.SteelAnvilPlaceEvent;
import com.worldciv.utility.CraftingItemStack;
import net.minecraft.server.v1_12_R1.EnumParticle;
import net.minecraft.server.v1_12_R1.PacketPlayOutWorldParticles;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

import static com.worldciv.the60th.Main.fileSystem;

public class SteelAnvilEvent implements Listener {

    @EventHandler
    public void onSteelAnvilPlace(SteelAnvilPlaceEvent e) {
        Player p = e.getPlayer();
        Block b = e.getBlock();

        for (Player nearbyplayers : Bukkit.getOnlinePlayers()) {
            if (nearbyplayers.getLocation().distance(b.getLocation()) <= 8) {
                PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.TOTEM, true, b.getLocation().getBlockX(), b.getLocation().getBlockY(), b.getLocation().getBlockZ(), 0, 0, 0, 1, 15, null);
                ((CraftPlayer) nearbyplayers).getHandle().playerConnection.sendPacket(packet);
            }
        }

        fileSystem.addBlocks(b, "steel anvil");

    }

    @EventHandler
    public void onSteelAnvilRemoval(SteelAnvilBreakEvent e) {
        Player p = e.getPlayer(); //this can return null, care.
        Block b = e.getBlock();
        Location loc = b.getLocation();

        if (p == null) {
            //it could return null
        }

        b.setType(Material.AIR);
        Bukkit.getWorld("world").dropItem(loc, CraftingItemStack.getSteelAnvil());

        fileSystem.removeBlocks(b, "steel anvil");

        Bukkit.getWorld("world").playEffect(loc, Effect.EXTINGUISH, 3);

        for (Player nearbyplayers : Bukkit.getOnlinePlayers()) {
            if (nearbyplayers.getLocation().distance(b.getLocation()) <= 8) {
                nearbyplayers.playSound(b.getLocation(), Sound.BLOCK_NOTE_SNARE, 4, 5);
                PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.CLOUD, true, b.getLocation().getBlockX(), b.getLocation().getBlockY(), b.getLocation().getBlockZ(), 0, 0, 0, 1, 15, null);
                ((CraftPlayer) nearbyplayers).getHandle().playerConnection.sendPacket(packet);
            }
        }


    }


    /**
     * On place of steel anvil , calls event
     */

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {

        Block b = e.getBlockPlaced();

        if (b.getType().equals(Material.ANVIL)) {
            Player p = e.getPlayer();
            ItemStack is = e.getItemInHand();
            ItemMeta im = is.getItemMeta();
            if (im == null || im.getLore() == null || im.getLore().get(0).isEmpty()) {
                if (fileSystem.getBlocksList("steel anvil").contains(b.getLocation())) {
                    fileSystem.removeBlocks(b, "steel anvil");
                }
                return;
            }

            if (im.getLore().get(0).equalsIgnoreCase(CraftingItemStack.getSteelAnvil().getItemMeta().getLore().get(0))) {
                SteelAnvilPlaceEvent event = new SteelAnvilPlaceEvent(p, b);
                Bukkit.getServer().getPluginManager().callEvent(event);
            }

        }
    }

    /**
     * On removal of block  , calls the event
     */

    @EventHandler
    public void onPhysics(EntityChangeBlockEvent e) {
        Block b = e.getBlock();
        if(b.getType().equals(Material.ANVIL)){
            if (e.getEntity() instanceof FallingBlock) {
                if (fileSystem.getBlocksList("steel anvil").contains(b.getLocation())) {
                    e.setCancelled(true);
                    SteelAnvilBreakEvent event = new SteelAnvilBreakEvent(b);
                    Bukkit.getServer().getPluginManager().callEvent(event);
                }
            }
        }

    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Block b = e.getBlock();
        Player p = e.getPlayer();

        if (b.getType().equals(Material.ANVIL)) {
            if (fileSystem.getBlocksList("steel anvil").contains(b.getLocation())) {
                e.setCancelled(true);
                SteelAnvilBreakEvent event = new SteelAnvilBreakEvent(p, b);
                Bukkit.getServer().getPluginManager().callEvent(event);

            }
        }
    }

    @EventHandler
    public void onExplosion(BlockExplodeEvent e) {
        List<Block> list = e.blockList();

        for (Block b : list) {
            if (b.getType().equals(Material.ANVIL)) {
                if (fileSystem.getBlocksList("steel anvil").contains(b.getLocation())) {
                    e.setCancelled(true);
                    SteelAnvilBreakEvent event = new SteelAnvilBreakEvent(e.getBlock());
                    Bukkit.getServer().getPluginManager().callEvent(event);

                }
            }
        }
    }

    @EventHandler
    public void onPistonRetract(BlockPistonRetractEvent e) {
        List<Block> list = e.getBlocks();
        for (Block b : list) {
            if (b.getType().equals(Material.ANVIL)) {
                if (fileSystem.getBlocksList("steel anvil").contains(b.getLocation())) {
                    SteelAnvilBreakEvent event = new SteelAnvilBreakEvent(e.getBlock());
                    Bukkit.getServer().getPluginManager().callEvent(event);
                }
            }
        }
    }

    @EventHandler
    public void onPistonPush(BlockPistonExtendEvent e) {
        List<Block> list = e.getBlocks();

        for (Block b : list) {
            if (b.getType().equals(Material.ANVIL)) {
                if (fileSystem.getBlocksList("steel anvil").contains(b.getLocation())) {
                    SteelAnvilBreakEvent event = new SteelAnvilBreakEvent(e.getBlock());
                    Bukkit.getServer().getPluginManager().callEvent(event);
                }
            }
        }
    }
}
