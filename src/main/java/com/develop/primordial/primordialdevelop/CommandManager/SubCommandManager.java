package com.develop.primordial.primordialdevelop.CommandManager;

import java.util.HashMap;

public class SubCommandManager {
    private final HashMap<String, SubCommand> commandMap = new HashMap<>();

    public void registerSubCommand(SubCommand subCommand) {
        commandMap.put(subCommand.getName(), subCommand);
    }
    public void unregisterSubCommand(SubCommand subCommand) {
        commandMap.remove(subCommand.getName());
    }

    public HashMap<String, SubCommand> getSubCommandMap() { return commandMap; }
}
