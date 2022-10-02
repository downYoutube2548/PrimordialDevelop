package com.develop.primordial.primordialdevelop.CommandManager;

import org.bukkit.command.CommandSender;

import java.util.List;

public abstract class SubCommand {

    private final String name;
    private final String permission;

    public SubCommand(String name, String permission) {
        this.name = name;
        this.permission = permission;
    }

    public String getName() { return name; }
    public String getPermission() { return this.permission; }

    public abstract void execute(CommandSender sender, String[] args);
    public abstract List<String> onTabComplete(CommandSender sender, String[] args);


}
