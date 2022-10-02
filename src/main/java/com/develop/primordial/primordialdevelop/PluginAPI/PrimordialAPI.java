package com.develop.primordial.primordialdevelop.PluginAPI;

import com.develop.primordial.primordialdevelop.CommandManager.SubCommandManager;
import com.develop.primordial.primordialdevelop.PrimordialDevelop;
import com.downYoutube2548.primordial.MMOCore.ClassInventory.ClassInventoryManager;

public class PrimordialAPI {
    public static SubCommandManager getSubCommandManager() { return PrimordialDevelop.subCommandManager; }
    public static ClassInventoryManager getClassInventoryManager() { return PrimordialDevelop.classInventoryManager; }
}
