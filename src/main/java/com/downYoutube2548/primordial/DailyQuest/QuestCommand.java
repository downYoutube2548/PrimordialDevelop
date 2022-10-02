package com.downYoutube2548.primordial.DailyQuest;

import com.develop.primordial.primordialdevelop.CommandManager.SubCommand;
import com.develop.primordial.primordialdevelop.PrimordialData;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.util.List;

public class QuestCommand extends SubCommand {
    public QuestCommand() {
        super("quest", "primordial.quest");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length >= 1) {
            if (args[0].equals("reset")) {
                if (args.length >= 2) {
                    OfflinePlayer player = Bukkit.getOfflinePlayer(args[1]);
                    if (player.hasPlayedBefore()) {
                        PrimordialData data = PrimordialData.get(player.getUniqueId());
                        data.getQuestManager().reset();
                    }
                }
            }
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return null;
    }
}
