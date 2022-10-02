package com.downYoutube2548.primordial.MMOCore.ClassInventory;

import com.develop.primordial.primordialdevelop.PluginAPI.PrimordialAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;

public class ClassInventoryManager implements Listener {
    private final HashMap<String, ClassInventory> classInventoryMap = new HashMap<>();
    private final HashMap<String, com.downYoutube2548.primordial.MMOCore.ClassInventory.Inventory> inventory = new HashMap<>();

    public void registerClassInventory(ClassInventory classInventory) {
        classInventoryMap.put(classInventory.getId(), classInventory);
    }
    public void unregisterClassInventory(String s) {
        classInventoryMap.remove(s);
    }

    public HashMap<String, ClassInventory> getClassInventoryMap() { return classInventoryMap; }
    public ClassInventory getInventory(String id) { return classInventoryMap.get(id); }

    @EventHandler
    public void onInvClick(InventoryClickEvent e) {
        for (ClassInventory c : PrimordialAPI.getClassInventoryManager().getClassInventoryMap().values()) {
            if (e.getWhoClicked() instanceof Player player && isInstanceOf(e.getInventory().getHolder().getClass(), c)) {
                //player.sendMessage(e.getInventory().getHolder().getClass().getTypeName());

                c.guiInteract(e, player, inventory.get(c.getId()).getButtonId(e.getSlot()));
                return;
            }
        }
    }

    @EventHandler
    public void onInvDrag(InventoryDragEvent e) {
        for (ClassInventory c : PrimordialAPI.getClassInventoryManager().getClassInventoryMap().values()) {
            if (e.getWhoClicked() instanceof Player player && isInstanceOf(e.getInventory().getHolder().getClass(), c)) {
                //player.sendMessage(e.getInventory().getHolder().getClass().getTypeName());

                e.setCancelled(true);
                return;
            }
        }
    }

    public void guiOpen(Player player, String Id, HashMap<String, Object> obj) {
        ClassInventory classInventory = PrimordialAPI.getClassInventoryManager().getInventory(Id);
        Inventory bukkitInventory = Bukkit.createInventory(classInventory, classInventory.getSize(), classInventory.getTitle());
        com.downYoutube2548.primordial.MMOCore.ClassInventory.Inventory inventory = new com.downYoutube2548.primordial.MMOCore.ClassInventory.Inventory(bukkitInventory);
        com.downYoutube2548.primordial.MMOCore.ClassInventory.Inventory inv = classInventory.guiOpen(player, inventory, obj);
        player.openInventory(
                (inv != null) ? inv.getBukkitInventory() : bukkitInventory
        );
        this.inventory.put(Id, inventory);
    }

    public boolean isInstanceOf(Class<?> clazz, Object obj){
        return clazz.isInstance(obj);
    }

}
