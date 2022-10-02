package com.downYoutube2548.primordial.MMOCore.ClassInventory.Inventories;

import com.develop.primordial.primordialdevelop.PluginAPI.PrimordialAPI;
import com.develop.primordial.primordialdevelop.PrimordialDevelop;
import com.downYoutube2548.primordial.MMOCore.ClassInventory.ClassInventory;
import com.downYoutube2548.primordial.MMOCore.ClassInventory.Inventory;
import com.downYoutube2548.primordial.Utils2;
import net.Indyuce.mmocore.MMOCore;
import net.Indyuce.mmocore.api.player.PlayerData;
import net.Indyuce.mmocore.api.player.profess.PlayerClass;
import net.luckperms.api.model.user.User;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Locale;

public class CharacterUpgradeConfirm extends ClassInventory {
    public CharacterUpgradeConfirm() {
        super("character-upgrade-confirm");
    }

    @Override
    public Inventory guiOpen(Player player, Inventory inventory, HashMap<String, Object> obj) {
        String old_class = String.valueOf(obj.get("old-class"));
        String new_class = String.valueOf(obj.get("new-class"));

        for (String s : getSettingKey("buttons")) {
            ItemStack item = Utils2.addStrCustomNBT(getItem(player, "buttons."+s, new String[]{}, new String[]{}), new String[]{"old-class", "new-class"}, new String[]{old_class, new_class});

            inventory.setSlot(getSettingInteger("buttons."+s+".slot"), item, s);
        }

        return inventory;
    }

    @Override
    public void guiInteract(InventoryClickEvent event, Player player, String buttonId) {
        event.setCancelled(true);
        switch (buttonId) {
            case "back" -> {
                String old_class = Utils2.getNBTString(event.getCurrentItem(), "old-class");
                HashMap<String, Object> hash = new HashMap<>();
                hash.put("character", old_class);
                PrimordialAPI.getClassInventoryManager().guiOpen(player, "character-upgrade", hash);
                break;
            }
            case "confirm" -> {
                PlayerData mmoPlayer = PlayerData.get(player);

                String old_class = Utils2.getNBTString(event.getCurrentItem(), "old-class");
                String new_class = Utils2.getNBTString(event.getCurrentItem(), "new-class");
                if (hasEnoughResource(player, new_class)) {
                    for (ItemStack item : getItemCost(new_class)) {
                        player.getInventory().removeItem(item);
                    }
                    PrimordialDevelop.economy.withdrawPlayer(player, getMoneyCost(new_class));
                    if (mmoPlayer.getProfess().getId().equals(MMOCore.plugin.classManager.get(old_class).getId())) {
                        mmoPlayer.setClass(MMOCore.plugin.classManager.get(new_class));
                        if (getGroup(new_class) != null && PrimordialDevelop.configManager.getString("weapon-type."+getGroup(new_class)) != null) {
                            mmoPlayer.setAttribute(PrimordialDevelop.configManager.getString("weapon-type."+getGroup(new_class)), 1);
                        }
                        player.sendMessage(Utils2.translatePlaceholder(Utils2.messageWithColor("message.upgrade-class"), new String[]{"%class%"}, new String[]{MMOCore.plugin.classManager.get(new_class).getName()}));
                        User user = PrimordialDevelop.luckPerms.getUserManager().getUser(player.getUniqueId());
                        if (user != null) {
                            Utils2.addPermission(user, "primordial.class."+new_class.toLowerCase(Locale.ROOT));
                            Utils2.removePermission(user, "primordial.class."+old_class.toLowerCase(Locale.ROOT));
                        }
                        player.closeInventory();
                        player.playSound(player.getLocation(), Sound.ITEM_TOTEM_USE, 10, 1);
                    } else {
                        PlayerClass old_profess = mmoPlayer.getProfess();
                        Utils2.setClass(player, MMOCore.plugin.classManager.get(old_class));
                        mmoPlayer.setClass(MMOCore.plugin.classManager.get(new_class));
                        if (getGroup(new_class) != null && PrimordialDevelop.configManager.getString("weapon-type."+getGroup(new_class)) != null) {
                            mmoPlayer.setAttribute(PrimordialDevelop.configManager.getString("weapon-type."+getGroup(new_class)), 1);
                        }
                        Utils2.setClass(player, old_profess);
                        player.sendMessage(Utils2.translatePlaceholder(Utils2.messageWithColor("message.upgrade-class"), new String[]{"%class%"}, new String[]{MMOCore.plugin.classManager.get(new_class).getName()}));
                        User user = PrimordialDevelop.luckPerms.getUserManager().getUser(player.getUniqueId());
                        if (user != null) {
                            Utils2.addPermission(user, "primordial.class."+new_class.toLowerCase(Locale.ROOT));
                            Utils2.removePermission(user, "primordial.class."+old_class.toLowerCase(Locale.ROOT));
                        }
                        player.closeInventory();
                        player.playSound(player.getLocation(), Sound.ITEM_TOTEM_USE, 10, 1);
                    }
                }
            }
        }
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
