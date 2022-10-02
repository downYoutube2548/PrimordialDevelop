package com.develop.primordial.primordialdevelop.CommandManager;

import com.develop.primordial.primordialdevelop.PluginAPI.PrimordialAPI;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainCommand implements CommandExecutor, TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (args.length >= 1) {
            if (PrimordialAPI.getSubCommandManager().getSubCommandMap().containsKey(args[0])) {
                if (sender.hasPermission(PrimordialAPI.getSubCommandManager().getSubCommandMap().get(args[0]).getPermission())) {
                    PrimordialAPI.getSubCommandManager().getSubCommandMap().get(args[0]).execute(sender, args);
                    return false;
                } else {
                    sender.sendMessage(ChatColor.RED+"You don't have permission!");
                }
            }
        }

        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {

        List<String> output = new ArrayList<>();

        if (args.length == 1) {
            for (String s : PrimordialAPI.getSubCommandManager().getSubCommandMap().keySet()) {
                if (sender.hasPermission(PrimordialAPI.getSubCommandManager().getSubCommandMap().get(s).getPermission())) {
                    if (s.toLowerCase(Locale.ROOT).startsWith(args[0])) {
                        output.add(s);
                    }
                }
            }
        }

        if (args.length > 1) {

            if (sender.hasPermission(PrimordialAPI.getSubCommandManager().getSubCommandMap().get(args[0]).getPermission())) {

                List<String> l = PrimordialAPI.getSubCommandManager().getSubCommandMap().get(args[0]).onTabComplete(sender, args);
                if (l != null) {
                    output.addAll(l);
                }
            }

        }



        return output;
    }
}
