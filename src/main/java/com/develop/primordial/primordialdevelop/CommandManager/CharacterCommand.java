package com.develop.primordial.primordialdevelop.CommandManager;

import com.develop.primordial.primordialdevelop.PluginAPI.PrimordialAPI;
import com.develop.primordial.primordialdevelop.PrimordialData;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class CharacterCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        PrimordialData primordialPlayer = null;
        if (args.length == 1 && sender.hasPermission("primordial.admin") && Bukkit.getOfflinePlayer(args[0]).isOnline()) {
            if (PrimordialAPI.getClassInventoryManager().getClassInventoryMap().containsKey("main") && Bukkit.getPlayer(args[0]) != null) {
                primordialPlayer = PrimordialData.get(Bukkit.getPlayer(args[0]).getUniqueId());
            }
            return false;
        }

        if (sender instanceof Player player) {
            if (PrimordialAPI.getClassInventoryManager().getClassInventoryMap().containsKey("main")) {
                primordialPlayer = PrimordialData.get(player.getUniqueId());
            }
        }

        if (primordialPlayer != null) {
            primordialPlayer.openCharacterMenu(1);
        }

        return false;
    }
}
