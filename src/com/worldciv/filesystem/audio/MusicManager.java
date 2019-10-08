package com.worldciv.filesystem.audio;

import com.xxmicloxx.NoteBlockAPI.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.io.InputStream;

import static com.worldciv.the60th.Main.plugin;
import static com.worldciv.utility.utilityStrings.worldciv;

public class MusicManager implements Listener {

    @EventHandler
    public void onNoteBlockCrouchHit(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        ItemStack item = e.getItem();
        Action action = e.getAction();

        if (item == null || e.getClickedBlock() == null || action == null) {
            return;
        }

        if (item.getType().equals(Material.NOTE_BLOCK)) {
            if (p.isSneaking()) {

                if(item.getItemMeta() == null){
                    return;
                }

                if (item.getItemMeta().getDisplayName().equalsIgnoreCase("Guren no Yumiya")) {

                    if (action.equals(Action.LEFT_CLICK_BLOCK)) {
                        playGurenNoYumiya(p);
                    }
                }
            }
        }

    }

    public void playGurenNoYumiya(Player p) {

        String filePath = "com/worldciv/filesystem/audio/GurenNoYumiya.nbs";
        String song_name = "Guren no Yumiya";

        InputStream in = plugin.getResource(filePath);

        Song s = NBSDecoder.parse(in);
        SongPlayer sp = new RadioSongPlayer(s);



        if (NoteBlockPlayerMain.isReceivingSong(p)) {
            NoteBlockPlayerMain.stopPlaying(p);
            p.sendMessage(worldciv + ChatColor.GRAY + " You have stopped playing: " + ChatColor.YELLOW + song_name + ChatColor.GRAY + ".");
        } else {
            sp.setAutoDestroy(true);
            sp.addPlayer(p);
            sp.setPlaying(true);


            p.sendMessage(worldciv + ChatColor.GRAY + " You are now listening to: " + ChatColor.YELLOW + song_name + ChatColor.GRAY + ".");


        }

    }


}
