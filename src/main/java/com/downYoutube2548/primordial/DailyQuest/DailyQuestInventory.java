package com.downYoutube2548.primordial.DailyQuest;

import com.downYoutube2548.primordial.MMOCore.ClassInventory.ClassInventory;
import com.downYoutube2548.primordial.MMOCore.ClassInventory.Inventory;
import com.downYoutube2548.primordial.Utils2;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class DailyQuestInventory extends ClassInventory {
    public DailyQuestInventory() {
        super("quest");
    }

    @Override
    public Inventory guiOpen(Player player, Inventory inventory, HashMap<String, Object> obj) {

        for (String s : getSettingKey("buttons")) {
            ItemStack item = Utils2.addStrCustomNBT(getItem(player, "buttons."+s, new String[]{}, new String[]{}), new String[]{}, new String[]{});

            inventory.setSlot(getSettingInteger("buttons."+s+".slot"), item, s);
        }

        return inventory;
    }

    @Override
    public void guiInteract(InventoryClickEvent event, Player player, String buttonId) {
        event.setCancelled(true);
    }

    @Override
    public int guiSize() {
        return getSettingInteger("size");
    }

    @Override
    public String guiTitle() {
        return getSettingString("title");
    }

    @NotNull
    @Override
    public org.bukkit.inventory.Inventory getInventory() {
        return null;
    }
}
