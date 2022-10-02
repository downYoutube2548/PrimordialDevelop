package com.downYoutube2548.primordial.MMOCore.SubCommands;

import com.develop.primordial.primordialdevelop.CommandManager.SubCommand;
import com.develop.primordial.primordialdevelop.PluginAPI.PrimordialAPI;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class GuiCommand extends SubCommand {
    public GuiCommand() {
        super("gui", "primordial.gui");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof Player player) {

            HashMap<String, Object> hash = new HashMap<>();
            hash.put("page", 1);

            if (PrimordialAPI.getClassInventoryManager().getClassInventoryMap().containsKey(args[1])) PrimordialAPI.getClassInventoryManager().guiOpen(player, args[1], hash);
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        List<String> output = new ArrayList<>();
        if (args.length == 2) {
            for (String s : PrimordialAPI.getClassInventoryManager().getClassInventoryMap().keySet()) {
                if (s.toLowerCase(Locale.ROOT).startsWith(args[1])) {
                    output.add(s);
                }
            }
        }

        return output;
    }
}
