package com.downYoutube2548.primordial.MMOCore.ClassInventory;

import com.downYoutube2548.primordial.Utils2;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Inventory {

    private final org.bukkit.inventory.Inventory bukkitInventory;

    public Inventory(org.bukkit.inventory.Inventory bukkitInventory) {
        this.bukkitInventory = bukkitInventory;
    }

    public void setSlot(int slot, ItemStack itemStack, String id) {
        if (itemStack == null || itemStack.getType().equals(Material.AIR)) return;
        ItemStack item = Utils2.addStrCustomNBT(itemStack, "buttonId", id);
        this.bukkitInventory.setItem(slot, item);
    }

    protected org.bukkit.inventory.Inventory getBukkitInventory() {
        return this.bukkitInventory;
    }

    protected String getButtonId(int slot) {
        ItemStack item = this.bukkitInventory.getItem(slot);
        if (item == null || item.getType().equals(Material.AIR)) return "null";
        NBTItem nbt = new NBTItem(item);
        return nbt.getString("buttonId");
    }
}
