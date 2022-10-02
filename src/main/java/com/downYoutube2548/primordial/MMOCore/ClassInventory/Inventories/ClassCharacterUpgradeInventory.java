package com.downYoutube2548.primordial.MMOCore.ClassInventory.Inventories;

import com.develop.primordial.primordialdevelop.PluginAPI.PrimordialAPI;
import com.develop.primordial.primordialdevelop.PrimordialDevelop;
import com.downYoutube2548.primordial.MMOCore.ClassInventory.ClassInventory;
import com.downYoutube2548.primordial.MMOCore.ClassInventory.Inventory;
import com.downYoutube2548.primordial.Utils2;
import net.Indyuce.mmocore.MMOCore;
import net.Indyuce.mmocore.api.player.profess.PlayerClass;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class ClassCharacterUpgradeInventory extends ClassInventory {
    public ClassCharacterUpgradeInventory() {
        super("character-upgrade");
    }

    @Override
    public Inventory guiOpen(Player player, Inventory inventory, HashMap<String, Object> obj) {

        PlayerClass character = MMOCore.plugin.classManager.get((String) obj.get("character"));

        // item configure
        for (String s : getSettingKey("buttons")) {
            ItemStack item = Utils2.addStrCustomNBT(getItem(player, "buttons."+s, new String[]{}, new String[]{}), new String[]{"character"}, new String[]{character.getId()});

            switch (s) {
                case "cost" -> {
                    if (getMoneyCost(getNextCharacter(character).getId()) <= 0) continue;
                    Utils2.itemPlaceholder(item, new String[]{"%cost%"}, new String[]{Utils2.Format().format(getMoneyCost(getNextCharacter(character).getId()))});
                }
                case "money-remain" -> {
                    Utils2.itemPlaceholder(item, new String[]{"%balance%"}, new String[]{Utils2.Format().format(PrimordialDevelop.economy.getBalance(player))});
                }
                case "upgrade" -> {
                    String cause = null;
                    if (!hasEnoughItem(player, getNextCharacter(character).getId())) {
                        cause = getSettingString("upgrade-not-available.cause-text.not-enough-item");
                    } else if (!hasEnoughMoney(player, getNextCharacter(character).getId())) {
                        cause = getSettingString("upgrade-not-available.cause-text.not-enough-money");
                    }
                    if (cause != null) {
                        item = getItem(player, "upgrade-not-available", new String[]{"%cause%"}, new String[]{cause});
                    }
                }
            }

            inventory.setSlot(getSettingInteger("buttons."+s+".slot"), item, s);
        }

        // item cost
        List<ItemStack> items = getItemCostIcon(getNextCharacter(character).getId());
        List<Integer> slots = getSettingIntegerList("item-cost.slot");
        List<Integer> slot = new ArrayList<>();
        for (int i = 1; i <= slots.size() && i <= items.size(); i++) {
            slot.add(slots.get(i-1));
        }
        Collections.sort(slot);
        int i = 1;
        for (int s : slot) {
            int amount = getAmountOfItemInInv(player, Utils2.getItem(Utils2.getNBTString(items.get(i-1),"ITEM_ID")));
            int max = items.get(i-1).getAmount();
            String have = (amount >= max) ? getSettingString("item-cost.have-enough-color")+amount : getSettingString("item-cost.not-enough-color")+Utils2.Format().format(amount);
            inventory.setSlot(s, getItem(items.get(i-1),player, "item-cost", true, new String[]{"%have%", "%need%"}, new String[]{have, Utils2.Format().format(max)}), "item-cost");
            i++;
        }

        // others
        inventory.setSlot(getSettingInteger("current-character.slot"), getItem(character.getIcon(), player, "current-character", false, new String[]{"%class-name%", "%max-level%"}, new String[]{character.getName(), Utils2.Format().format(character.getMaxLevel())}), "current-character");
        inventory.setSlot(getSettingInteger("upgraded-character.slot"), getItem(getNextCharacter(character).getIcon(), player, "upgraded-character", false, new String[]{"%class-name%", "%max-level%"}, new String[]{getNextCharacter(character).getName(), Utils2.Format().format(getNextCharacter(character).getMaxLevel())}), "upgraded-character");
        return inventory;
    }

    @Override
    public void guiInteract(InventoryClickEvent event, Player player, String buttonId) {
        event.setCancelled(true);
        switch (buttonId) {
            case "back" -> {
                HashMap<String, Object> hash = new HashMap<>();
                hash.put("page", 1);
                PrimordialAPI.getClassInventoryManager().guiOpen(player, "main", hash);
                break;
            }
            case "upgrade" -> {
                ItemStack item = event.getCurrentItem();
                String old_class = Utils2.getNBTString(item, "character");
                String new_class = getNextCharacter(MMOCore.plugin.classManager.get(old_class)).getId();
                if (hasEnoughResource(player, new_class)) {
                    HashMap<String, Object> hash = new HashMap<>();
                    hash.put("old-class", old_class);
                    hash.put("new-class", new_class);
                    PrimordialAPI.getClassInventoryManager().guiOpen(player, "character-upgrade-confirm", hash);
                }
                break;
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
