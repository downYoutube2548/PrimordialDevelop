package com.develop.primordial.primordialdevelop.CommandManager.DefaultCommands;

import com.develop.primordial.primordialdevelop.CommandManager.SubCommand;
import com.develop.primordial.primordialdevelop.PrimordialDevelop;
import com.downYoutube2548.primordial.Utils2;
import org.bukkit.command.CommandSender;

import java.util.List;

public class ReloadCommand extends SubCommand {
    private final PrimordialDevelop main;

    public ReloadCommand(PrimordialDevelop main) {
        super("reload", "primordial.reload");
        this.main = main;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        PrimordialDevelop.loadConfig(main);
        PrimordialDevelop.debug = PrimordialDevelop.configManager.getBoolean("debug");
        sender.sendMessage(Utils2.colorize(PrimordialDevelop.configManager.getString("message.reload-complete")));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return null;
    }
}
