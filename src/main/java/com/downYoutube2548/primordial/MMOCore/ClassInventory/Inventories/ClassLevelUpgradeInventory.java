package com.downYoutube2548.primordial.MMOCore.ClassInventory.Inventories;

import com.develop.primordial.primordialdevelop.PluginAPI.PrimordialAPI;
import com.develop.primordial.primordialdevelop.PrimordialDevelop;
import com.downYoutube2548.primordial.MMOCore.ClassInventory.ClassInventory;
import com.downYoutube2548.primordial.MMOCore.ClassInventory.Inventory;
import com.downYoutube2548.primordial.Utils2;
import de.tr7zw.nbtapi.NBTItem;
import net.Indyuce.mmocore.MMOCore;
import net.Indyuce.mmocore.api.player.PlayerData;
import net.Indyuce.mmocore.api.player.profess.PlayerClass;
import net.Indyuce.mmocore.api.player.profess.SavedClassInformation;
import net.Indyuce.mmocore.experience.EXPSource;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class ClassLevelUpgradeInventory extends ClassInventory {

    public ClassLevelUpgradeInventory() {
        super("level-upgrade");
    }

    @Override
    public Inventory guiOpen(Player player, Inventory inventory, HashMap<String, Object> obj) {

        int amount = (int) obj.get("amount");
        PlayerClass character = MMOCore.plugin.classManager.get((String) obj.get("character"));
        String upgrader = (String) obj.get("upgrader");

        if (amount > getRequiredUpgraderAmount(player, character, upgrader)) {
            amount = getRequiredUpgraderAmount(player, character, upgrader);
        }





        for (String s : getSettingKey("buttons")) {
            String[] array1 = new String[]{};
            String[] array2 = new String[]{};
            if (s.equals("upgrade_I") || s.equals("upgrade_II") || s.equals("upgrade_III")) {
                array1 = new String[]{"%amount%"};
                ItemStack upgrade_item = Utils2.getItem(getSettingString("buttons."+s+".item"));
                array2 = new String[]{Utils2.Format().format(getAmountOfItemInInv(player, upgrade_item))};
            }
            ItemStack item = Utils2.addStrCustomNBT(getItem(player, "buttons."+s, array1, array2), new String[]{"character", "amount", "upgrader", "upgrader_remain"}, new String[]{character.getId(), String.valueOf(amount), upgrader, String.valueOf(getAmountOfItemInInv(player, Utils2.getItem(getSettingString("buttons."+upgrader+".item"))))});
            switch (s) {
                case "upgrade_I" -> {
                    ItemStack upgrade_item = Utils2.getItem(getSettingString("buttons.upgrade_I.item"));
                    int a = getAmountOfItemInInv(player, upgrade_item);
                    if (a == 0) {
                        a = 1;
                    }
                    item.setAmount(a);
                    if (upgrader.equals("upgrade_I")) {
                        shiningItem(item);
                    }
                    break;
                }
                case "upgrade_II" -> {
                    ItemStack upgrade_item = Utils2.getItem(getSettingString("buttons.upgrade_II.item"));
                    int a = getAmountOfItemInInv(player, upgrade_item);
                    if (a == 0) {
                        a = 1;
                    }
                    item.setAmount(a);
                    if (upgrader.equals("upgrade_II")) {
                        shiningItem(item);
                    }
                    break;
                }
                case "upgrade_III" -> {
                    ItemStack upgrade_item = Utils2.getItem(getSettingString("buttons.upgrade_III.item"));
                    int a = getAmountOfItemInInv(player, upgrade_item);
                    if (a == 0) {
                        a = 1;
                    }
                    item.setAmount(a);
                    if (upgrader.equals("upgrade_III")) {
                        shiningItem(item);
                    }
                    break;
                }
                case "cost" -> {

                    if (getUpgradeCost(upgrader)*amount <= 0) { continue; }

                    Utils2.itemPlaceholder(item, new String[]{"%cost%"}, new String[]{Utils2.Format().format((long) getSettingInteger("buttons." + upgrader + ".cost") *amount)});
                    break;
                }
                case "money-remain" -> {
                    Utils2.itemPlaceholder(item, new String[]{"%balance%"}, new String[]{Utils2.Format().format(PrimordialDevelop.economy.getBalance(player))});
                    break;
                }
                case "confirm" -> {
                    String cause = null;
                    if (!hasEnoughItem(player, upgrader, amount)) {
                        cause = getSettingString("upgrade-not-available.cause-text.not-enough-item");
                    } else if (!hasEnoughMoney(player, upgrader, amount)) {
                        cause = getSettingString("upgrade-not-available.cause-text.not-enough-money");
                    }
                    if (cause != null) {
                        item = getItem(player, "upgrade-not-available", new String[]{"%cause%"}, new String[]{cause});
                    }
                }
            }
            inventory.setSlot(getSettingInteger("buttons."+s+".slot"), item, s);

        }
        // selected upgrader
        ItemStack upgrade_selected_icon = Utils2.getItem(getSettingString("buttons."+upgrader+".item"));
        ItemMeta meta1 = upgrade_selected_icon.getItemMeta();
        assert meta1 != null;
        meta1.setLore(Utils2.translatePapi(player, Utils2.translatePlaceholder(getSettingStringList("selected_upgrader.lore"), new String[]{"%amount%"}, new String[]{Utils2.Format().format(amount)})));
        upgrade_selected_icon.setItemMeta(meta1);
        upgrade_selected_icon.setAmount(amount);
        inventory.setSlot(getSettingInteger("selected_upgrader.slot"), upgrade_selected_icon, "selected_upgrader");

        PlayerData mmoPlayer = PlayerData.get(player);
        int level;
        int xp;
        if (mmoPlayer.getProfess().equals(character)) {
            level = mmoPlayer.getLevel();
            xp = mmoPlayer.getExperience();
        } else {

            SavedClassInformation save_class = mmoPlayer.getClassInfo(character);

            if (save_class != null) {
                level = save_class.getLevel();
                xp = save_class.getExperience();
            } else {
                level = 1;
                xp = 0;
            }

        }
        int max_level = character.getMaxLevel();
        int max_xp = character.getExpCurve().getExperience(level+1);
        double percent = ((double)xp/(double)max_xp)*100;

        // selected character
        ItemStack character_selected_icon = getItem(character.getIcon(), player, "selected_character", false, new String[]{"%character_name%", "%level%", "%max_level%", "%xp_process_bar%", "%xp_percent%", "%xp%", "%max_xp%"}, new String[]{character.getName(), Utils2.Format().format(level), Utils2.Format().format(max_level), Utils2.createBar((double)xp, (double)max_xp, getSettingInteger("selected_character.bar_length"), getSettingString("selected_character.bar_color"), getSettingString("selected_character.bar_empty_color"), getSettingString("selected_character.bar_symbol")), Utils2.Format().format((int)percent)+"%", Utils2.Format().format(xp), Utils2.Format().format(max_xp)});
        inventory.setSlot(getSettingInteger("selected_character.slot"), character_selected_icon, "selected_character");

        xpBar(inventory, percent);

        if (PrimordialDevelop.debug) {
            Bukkit.broadcastMessage("---------------------------");
            Bukkit.broadcastMessage("Current: " + mmoPlayer.getProfess().getName());
            Bukkit.broadcastMessage("Character: " + character.getName());
            Bukkit.broadcastMessage("Level: " + level);
            Bukkit.broadcastMessage("Max Level: " + max_level);
            Bukkit.broadcastMessage("Xp: " + xp);
            Bukkit.broadcastMessage("Max Xp: " + max_xp);
            Bukkit.broadcastMessage("---------------------------");
        }

        return inventory;
    }

    private int getUpgradeCost(String upgrader) {
        return getSettingInteger("buttons."+upgrader+".cost");
    }

    @Override
    public void guiInteract(InventoryClickEvent event, Player player, String buttonId) {
        event.setCancelled(true);
        switch (buttonId) {
            case "upgrade_I" -> {
                HashMap<String, Object> hash = new HashMap<>();
                ItemStack item = event.getCurrentItem();
                NBTItem nbt = new NBTItem(item);
                String character = nbt.getString("character");
                hash.put("amount", 1);
                hash.put("character", character);
                hash.put("upgrader", "upgrade_I");
                PrimordialAPI.getClassInventoryManager().guiOpen(player, getId(), hash);
                break;
            }
            case "upgrade_II" -> {
                HashMap<String, Object> hash = new HashMap<>();
                ItemStack item = event.getCurrentItem();
                NBTItem nbt = new NBTItem(item);
                String character = nbt.getString("character");
                hash.put("amount", 1);
                hash.put("character", character);
                hash.put("upgrader", "upgrade_II");
                PrimordialAPI.getClassInventoryManager().guiOpen(player, getId(), hash);
                break;
            }
            case "upgrade_III" -> {
                HashMap<String, Object> hash = new HashMap<>();
                ItemStack item = event.getCurrentItem();
                NBTItem nbt = new NBTItem(item);
                String character = nbt.getString("character");
                hash.put("amount", 1);
                hash.put("character", character);
                hash.put("upgrader", "upgrade_III");
                PrimordialAPI.getClassInventoryManager().guiOpen(player, getId(), hash);
                break;
            }
            case "increase" -> {
                HashMap<String, Object> hash = new HashMap<>();
                ItemStack item = event.getCurrentItem();
                NBTItem nbt = new NBTItem(item);
                String amount = nbt.getString("amount");
                String character = nbt.getString("character");
                String upgrader = nbt.getString("upgrader");
                int max = Integer.parseInt(nbt.getString("upgrader_remain"));
                if (event.getClick().equals(ClickType.LEFT) && Integer.parseInt(amount) + 1 <= max) {
                    hash.put("amount", Integer.parseInt(amount) + 1);
                } else if (event.getClick().equals(ClickType.SHIFT_LEFT) && Integer.parseInt(amount) + 1 <= max) {
                    int a = max - Integer.parseInt(amount);
                    int out = 0;
                    if (a < 10) {
                        out = 10 - a;
                    }
                    hash.put("amount", Integer.parseInt(amount) + 10 - out);
                } else if (event.getClick().equals(ClickType.RIGHT) && Integer.parseInt(amount) + 1 <= max) {
                    hash.put("amount", max);
                } else {
                    return;
                }

                hash.put("character", character);
                hash.put("upgrader", upgrader);
                PrimordialAPI.getClassInventoryManager().guiOpen(player, getId(), hash);
                break;
            }
            case "decrease" -> {
                HashMap<String, Object> hash = new HashMap<>();
                ItemStack item = event.getCurrentItem();
                NBTItem nbt = new NBTItem(item);
                String amount = nbt.getString("amount");
                String character = nbt.getString("character");
                String upgrader = nbt.getString("upgrader");
                if (event.getClick().equals(ClickType.LEFT) && Integer.parseInt(amount) - 1 > 0) {
                    hash.put("amount", Integer.parseInt(amount) - 1);
                } else if (event.getClick().equals(ClickType.SHIFT_LEFT) && Integer.parseInt(amount) - 1 > 0) {
                    int a = Integer.parseInt(amount);
                    int out = 0;
                    if (a <= 10) {
                        out = 10 - a + 1;
                    }
                    hash.put("amount", Integer.parseInt(amount) - 10 + out);
                } else if (event.getClick().equals(ClickType.RIGHT) && Integer.parseInt(amount) - 1 > 0) {
                    hash.put("amount", 1);
                } else {
                    return;
                }

                hash.put("character", character);
                hash.put("upgrader", upgrader);
                PrimordialAPI.getClassInventoryManager().guiOpen(player, getId(), hash);
                break;
            }
            case "confirm" -> {
                HashMap<String, Object> hash = new HashMap<>();
                PlayerData mmoPlayer = PlayerData.get(player);

                ItemStack item = event.getCurrentItem();
                NBTItem nbt = new NBTItem(item);

                String amount = nbt.getString("amount");
                String character = nbt.getString("character");
                String upgrader = nbt.getString("upgrader");

                hash.put("amount", Integer.parseInt(amount));
                hash.put("character", character);
                hash.put("upgrader", upgrader);

                PlayerClass old_class = mmoPlayer.getProfess();
                PlayerClass selected_class = MMOCore.plugin.classManager.get(character);

                ItemStack upgrade_item = Utils2.getItem(getSettingString("buttons." + upgrader + ".item"));
                int cost = getUpgradeCost(upgrader)*Integer.parseInt(amount);
                if (!old_class.equals(selected_class)) {
                    if (hasEnoughItem(player, upgrader, Integer.parseInt(amount)) && hasEnoughMoney(player, upgrader, Integer.parseInt(amount)) && Utils2.getClassLevel(mmoPlayer, selected_class) < selected_class.getMaxLevel()) {
                        Utils2.setClass(player, selected_class);
                        //player.sendMessage("1" + expAmount(upgrader, Integer.parseInt(amount)));
                        mmoPlayer.giveExperience(expAmount(player, upgrader, Integer.parseInt(amount), MMOCore.plugin.classManager.get(character)), EXPSource.VANILLA);
                        removeItem(player, upgrade_item, Integer.parseInt(amount));
                        Utils2.setClass(player, old_class);
                        PrimordialDevelop.economy.withdrawPlayer(player, cost);

                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 2);
                        if (mmoPlayer.getClassInfo(selected_class).getLevel() >= selected_class.getMaxLevel()) {
                            if (getNextCharacter(selected_class) != null) {
                                HashMap<String, Object> hash2 = new HashMap<>();
                                hash2.put("character", selected_class.getId());
                                PrimordialAPI.getClassInventoryManager().guiOpen(player, "character-upgrade", hash2);
                            }
                        } else {
                            PrimordialAPI.getClassInventoryManager().guiOpen(player, getId(), hash);
                        }
                    }
                } else {
                    if (hasEnoughItem(player, upgrader, Integer.parseInt(amount)) && hasEnoughMoney(player, upgrader, Integer.parseInt(amount)) && Utils2.getClassLevel(mmoPlayer, selected_class) < selected_class.getMaxLevel()) {
                        //player.sendMessage("2" + expAmount(upgrader, Integer.parseInt(amount)));

                        mmoPlayer.giveExperience(expAmount(player, upgrader, Integer.parseInt(amount), MMOCore.plugin.classManager.get(character)), EXPSource.VANILLA);
                        removeItem(player, upgrade_item, Integer.parseInt(amount));
                        PrimordialDevelop.economy.withdrawPlayer(player, cost);

                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 2);
                        if (mmoPlayer.getLevel() >= selected_class.getMaxLevel()) {
                            if (getNextCharacter(selected_class) != null) {
                                HashMap<String, Object> hash2 = new HashMap<>();
                                hash2.put("character", selected_class.getId());
                                PrimordialAPI.getClassInventoryManager().guiOpen(player, "character-upgrade", hash2);
                            }
                        } else {
                            PrimordialAPI.getClassInventoryManager().guiOpen(player, getId(), hash);
                        }
                    }
                }


                break;
            }
            case "back" -> {
                HashMap<String, Object> hash = new HashMap<>();
                hash.put("page", 1);
                PrimordialAPI.getClassInventoryManager().guiOpen(player, "main", hash);
                break;
            }
        }
    }

    private void removeItem(Player player, ItemStack item, int amount) {
        item.setAmount(amount);
        player.getInventory().removeItem(item);
    }

    private int expAmount(Player player, String upgrader, int amount, PlayerClass character) {
        int total_exp = getExpAmount(upgrader, amount);
        if (total_exp > getRequiredExp(player, character)) {
            total_exp = getRequiredExp(player,character);
        }

        return total_exp;
    }

    private int getExpAmount(String upgrader, int amount) {
        int exp = switch (upgrader) {
            case "upgrade_I" -> 100;
            case "upgrade_II" -> 500;
            case "upgrade_III" -> 2000;
            default -> 0;
        };

        return exp * amount;
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
    private ItemStack shiningItem(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.addEnchant(Enchantment.DURABILITY, 1, false);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
        return item;
    }

    private int getValueFromPercent(double percent, int max_value) {
        return (int)Math.ceil((percent/100)*max_value);
    }

    private void xpBar(Inventory inventory, double percent) {
        if (getSettingString("xp-bar.bar-slot") == null || getSettingString("xp-bar.bar-slot").equals("")) return;

        String all_slot = getSettingString("xp-bar.bar-slot");
        String[] line_slot = all_slot.split(";");

        int spaces = line_slot.length;
        int value = getValueFromPercent(percent, spaces);

        for (String slot_list : line_slot) {
            ItemStack item;
            if (value > 0) {
                item = new ItemStack(Material.valueOf(getSettingString("xp-bar.bar-material")));
            } else {
                item = new ItemStack(Material.valueOf(getSettingString("xp-bar.bar-empty-material")));
            }
            ItemMeta meta = item.getItemMeta();
            assert meta != null;
            meta.setDisplayName(" ");
            item.setItemMeta(meta);
            String[] each_slot_list = slot_list.split(",");
            for (String each_slot : each_slot_list) {
                inventory.setSlot(Integer.parseInt(each_slot), item, "xp-bar");
            }
            if (value > 0) value--;
        }
    }

    private boolean hasEnoughMoney(Player player, String upgrader, int amount) {

        int cost = getUpgradeCost(upgrader)*amount;

        return PrimordialDevelop.economy.getBalance(player) >= cost;
    }

    private boolean hasEnoughItem(Player player, String upgrader, int amount) {

        ItemStack upgrade_item = Utils2.getItem(getSettingString("buttons." + upgrader + ".item"));

        return getAmountOfItemInInv(player, upgrade_item) >= amount;
    }

    private int getRequiredExp(Player player, PlayerClass character) {
        PlayerData mmoPlayer = PlayerData.get(player);
        SavedClassInformation savedClassInfo = mmoPlayer.getClassInfo(character);
        int level;
        int exp = 0;
        int exph;
        if (savedClassInfo == null) {
            for (int i = 1; i <= character.getMaxLevel(); i++) {
                exp = exp+character.getExpCurve().getExperience(i);
            }
        } else {
            if (mmoPlayer.getProfess().getId().equals(character.getId())) {
                level = mmoPlayer.getLevel();
                exph = mmoPlayer.getExperience();
            } else {
                level = savedClassInfo.getLevel();
                exph = savedClassInfo.getExperience();
            }
            for (int i = level + 1; i <= character.getMaxLevel(); i++) {
                exp = exp + character.getExpCurve().getExperience(i);
            }
            exp = exp - exph;
        }
        return exp;
    }

    private int getRequiredUpgraderAmount(Player player, PlayerClass character, String upgrader) {

        int required_exp = getRequiredExp(player, character);

        return (int) Math.ceil((double) required_exp/getExpAmount(upgrader, 1));

    }
}
