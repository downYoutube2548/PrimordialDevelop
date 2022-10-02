package com.develop.primordial.primordialdevelop.YamlLoader;

import com.develop.primordial.primordialdevelop.PrimordialDevelop;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.time.Instant;

public class LeaveEvent implements Listener {
    private final PrimordialDevelop main;

    public LeaveEvent(PrimordialDevelop main) {
        this.main = main;
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event){
        Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
            Player player = event.getPlayer();

            SaveHashFile.saveHashFile(PrimordialDevelop.Files, PrimordialDevelop.PlayerData, PrimordialDevelop.PlayerData.get(player.getUniqueId().toString()), PrimordialDevelop.Files.get(player.getUniqueId().toString()));
            PrimordialDevelop.Files.remove(player.getUniqueId().toString());
            PrimordialDevelop.PlayerData.remove(player.getUniqueId().toString());


        });
    }
}
