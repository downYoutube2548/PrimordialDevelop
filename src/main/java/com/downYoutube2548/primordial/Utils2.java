package com.downYoutube2548.primordial;

import com.develop.primordial.primordialdevelop.PrimordialDevelop;
import com.downyoutube.devcurrency.devcurrency.DevCurrency;
import com.downyoutube.devcurrency.devcurrency.Utils;
import de.tr7zw.nbtapi.NBTItem;
import me.clip.placeholderapi.PlaceholderAPI;
import net.Indyuce.mmocore.MMOCore;
import net.Indyuce.mmocore.api.event.PlayerChangeClassEvent;
import net.Indyuce.mmocore.api.player.PlayerData;
import net.Indyuce.mmocore.api.player.profess.PlayerClass;
import net.Indyuce.mmocore.api.player.profess.SavedClassInformation;
import net.Indyuce.mmoitems.MMOItems;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.NodeEqualityPredicate;
import net.luckperms.api.util.Tristate;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils2 extends Utils {
    public static List<String> translatePapi(Player player, List<String> l) {
        List<String> out = new ArrayList<>();
        for (String s : l) {
            out.add(PlaceholderAPI.setPlaceholders(player, s));
        }
        return out;
    }

    public static String translatePapi(Player player, String l) {
        return PlaceholderAPI.setPlaceholders(player, l);
    }


    public static ItemStack itemCreator(Player player, String path, String[] target, String[] replacement) {
        ItemStack item = new ItemStack(Material.valueOf(PrimordialDevelop.configManager.getString(path+".type")), PrimordialDevelop.configManager.getInt(path+".count"));
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return null;
        } else {
            meta.setDisplayName(translatePapi(player, translatePlaceholder(DevCurrency.colorize(PrimordialDevelop.configManager.getString(path+".name")), target, replacement)));
            meta.setLore(translatePapi(player, translatePlaceholder(PrimordialDevelop.configManager.getStringList(path+".lore"), target, replacement)));
            if (PrimordialDevelop.configManager.getBoolean(path + ".glowing")) {
                meta.addEnchant(Enchantment.DURABILITY, 0, true);
                meta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ENCHANTS});
            }
            meta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES});

            meta.setCustomModelData(PrimordialDevelop.configManager.getInt(path + ".model"));

            item.setItemMeta(meta);
            return item;
        }
    }

    public static ItemStack itemCreator(ItemStack item, Player player, boolean add, String path, String[] target, String[] replacement) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return null;
        } else {
            if (PrimordialDevelop.configManager.getString(path+".name") != null) {
                meta.setDisplayName(translatePapi(player, translatePlaceholder(DevCurrency.colorize(PrimordialDevelop.configManager.getString(path + ".name")), target, replacement)));
            }
            if (PrimordialDevelop.configManager.getStringList(path+".lore") != null) {
                List<String> l = new ArrayList<>();
                if (add) {
                    if (meta.getLore() != null) l.addAll(meta.getLore());
                }
                l.addAll(translatePapi(player, translatePlaceholder(PrimordialDevelop.configManager.getStringList(path + ".lore"), target, replacement)));
                meta.setLore(l);
            }
            if (PrimordialDevelop.configManager.getBoolean(path + ".glowing")) {
                meta.addEnchant(Enchantment.DURABILITY, 0, true);
                meta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ENCHANTS});
            }
            meta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES});

            if (PrimordialDevelop.configManager.getInt(path + ".model") != 0) meta.setCustomModelData(PrimordialDevelop.configManager.getInt(path + ".model"));

            item.setItemMeta(meta);
            return item;
        }
    }

    public static String colorize(String s) {
        if (s != null && !s.equals("")) {
            if (Bukkit.getVersion().contains("1.16") || Bukkit.getVersion().contains("1.17") || Bukkit.getVersion().contains("1.18")) {
                Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");

                for (Matcher match = pattern.matcher(s); match.find(); match = pattern.matcher(s)) {
                    String hexColor = s.substring(match.start(), match.end());
                    s = s.replace(hexColor, net.md_5.bungee.api.ChatColor.of(hexColor).toString());
                }

            }
            return net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', s);
        } else {
            return "";
        }
    }

    public static String messageWithColor(String path) {
        return colorize(PrimordialDevelop.configManager.getString(path));
    }

    public static ItemStack getItem(String id) {
        String[] a = id.split(":");
        String type = a[0];
        ItemStack item = new ItemStack(Material.AIR);
        if (type.equals("minecraft")) {
            item = new ItemStack(Material.valueOf(a[1]));
            if (a[2] != null && Integer.parseInt(a[2]) != 0) {
                ItemMeta meta = item.getItemMeta();
                assert meta != null;
                meta.setCustomModelData(Integer.parseInt(a[2]));
                item.setItemMeta(meta);
            }
        } else if (type.equals("mmoitem")) {
            item = MMOItems.plugin.getItem(a[1], a[2]);
        }
        assert item != null;
        item.setAmount(1);
        return item;
    }

    public static String createBar(Double value, Double maxValue, Integer barAmount, String barColor, String emptyColor, String barChar) {
        double req = maxValue / barAmount;
        double percent = (value/maxValue)*100;
        StringBuilder p = new StringBuilder();
        int i = 1;
        while (i <= barAmount) {
            if ((i*req) <= (percent/100)*maxValue) {
                p.append(colorize(barColor + barChar + "&r"));
            } else {
                p.append(colorize(emptyColor + barChar + "&r"));
            }
            i++;
        }
        return p.toString();
    }

    public static void setClass(Player player, PlayerClass profess) {
        PlayerData data = PlayerData.get(player);

        PlayerChangeClassEvent called = new PlayerChangeClassEvent(data, profess);
        Bukkit.getPluginManager().callEvent(called);

        (data.hasSavedClass(profess) ? data.getClassInfo(profess) : new SavedClassInformation(MMOCore.plugin.dataProvider.getDataManager().getDefaultData())).load(profess, data);
        if (getGroup(profess.getId()) != null && PrimordialDevelop.configManager.getString("weapon-type."+getGroup(profess.getId())) != null) {
            data.setAttribute(PrimordialDevelop.configManager.getString("weapon-type."+getGroup(profess.getId())), 1);
        }

    }

    public static int getClassLevel(PlayerData mmoPlayer, PlayerClass clazz) {
        if (mmoPlayer.getProfess().equals(clazz)) {
            return mmoPlayer.getLevel();
        } else if (mmoPlayer.hasSavedClass(clazz)) {
            return mmoPlayer.getClassInfo(clazz).getLevel();
        } else {
            return 1;
        }
    }

    public static ItemStack itemPlaceholder(ItemStack item, String[] target, String[] replacement) {

        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(translatePlaceholder(meta.getDisplayName(), target, replacement));
            if (meta.getLore() != null) {
                meta.setLore(translatePlaceholder(meta.getLore(), target, replacement));
            }
        }
        item.setItemMeta(meta);
        return item;
    }

    public static void sendMessage(Object message) {
        if (message == null) {
            Bukkit.broadcastMessage("null");
        } else if (message.getClass() != String.class) {
            Bukkit.broadcastMessage(String.valueOf(message));
        }
    }
    public static void sendMessage(int message) {
        Bukkit.broadcastMessage(String.valueOf(message));
    }
    public static void sendMessage(double message) {
        Bukkit.broadcastMessage(String.valueOf(message));
    }
    public static void sendMessage(long message) {
        Bukkit.broadcastMessage(String.valueOf(message));
    }
    public static void sendMessage(float message) {
        Bukkit.broadcastMessage(String.valueOf(message));
    }
    public static void sendMessage(char message) {
        Bukkit.broadcastMessage(String.valueOf(message));
    }
    public static void sendMessage() {
        Bukkit.broadcastMessage("");
    }
    public static void sendMessage(@Nullable String message) {
        Bukkit.broadcastMessage(Objects.requireNonNullElse(message, "null"));
    }
    public static void sendMessage(boolean message) {
        String s;
        if (message) s = "true";
        else s = "false";
        Bukkit.broadcastMessage(s);
    }

    public static String getNBTString(ItemStack item, String key) {
        NBTItem nbt = new NBTItem(item);
        return nbt.getString(key);
    }

    public static void addPermission(User user, String permission) {
        // Add the permission
        user.data().add(Node.builder(permission).build());

        // Now we need to save changes.
        PrimordialDevelop.luckPerms.getUserManager().saveUser(user);
    }

    public static void removePermission(User user, String permission) {

        user.data().remove(Node.builder(permission).build());

        PrimordialDevelop.luckPerms.getUserManager().saveUser(user);
    }

    public static String getGroup(String character) {

        Map<String, Object> map = PrimordialDevelop.configManager.getConfigurationSection("characters").getValues(false);

        for (String character_group : map.keySet()) {
            List<String> l = convertObjectToList(map.get(character_group));
            if (l.contains(character)) {
                return character_group;
            }
        }
        return null;
    }

    public static List<String> convertObjectToList(Object obj) {
        List<?> l = (List<?>)obj;
        List<String> l2 = new ArrayList<>();
        for (Object o : l) {
            l2.add(String.valueOf(o));
        }
        return l2;
    }

    public static boolean isCharacterValid(PlayerClass character) {
        return getGroup(character.getId()) != null;
    }

    public static String setCharacterGroup(Player player, String group) {
        if (!PrimordialDevelop.configManager.getConfigurationSection("characters").getKeys(false).contains(group)) { return "group-invalid"; }
        PlayerClass character = null;
        List<String> playerClasses = PrimordialDevelop.configManager.getStringList("characters."+group);
        Collections.reverse(playerClasses);

        // Check if player have specific class
        for (String playerClass : playerClasses) {
            if (Utils2.hasPermissionIgnoredOP(player,"primordial.class." + playerClass.toLowerCase(Locale.ROOT))) {
                character = MMOCore.plugin.classManager.get(playerClass);
                break;
            }
        }
        if (character == null) { return "no-permission-found"; }
        setClass(player, character);
        return "done";
    }

    public static PlayerClass getAvailableCharacterFromGroup(Player player, String group) {
        if (!PrimordialDevelop.configManager.getConfigurationSection("characters").getKeys(false).contains(group)) { return null; }
        PlayerClass character = null;
        List<String> playerClasses = PrimordialDevelop.configManager.getStringList("characters."+group);
        Collections.reverse(playerClasses);

        // Check if player have specific class
        for (String playerClass : playerClasses) {
            if (Utils2.hasPermissionIgnoredOP(player, "primordial.class." + playerClass.toLowerCase(Locale.ROOT))) {
                character = MMOCore.plugin.classManager.get(playerClass);
                break;
            }
        }
        return character;
    }

    public static PlayerClass getCharacterFromGroup(String group, int n) {
        List<String> list_character = PrimordialDevelop.configManager.getStringList("characters."+group);
        return MMOCore.plugin.classManager.get(list_character.get(n));
    }

    public static boolean hasPermForGroup(Player player, String group) {
        if (!PrimordialDevelop.configManager.getConfigurationSection("characters").getKeys(false).contains(group)) { return false; }
        PlayerClass character = null;
        List<String> playerClasses = PrimordialDevelop.configManager.getStringList("characters."+group);
        Collections.reverse(playerClasses);

        // Check if player have specific class
        for (String playerClass : playerClasses) {
            if (Utils2.hasPermissionIgnoredOP(player, "primordial.class." + playerClass.toLowerCase(Locale.ROOT))) {
                character = MMOCore.plugin.classManager.get(playerClass);
                break;
            }
        }
        return character != null;
    }

    public static boolean hasPermissionIgnoredOP(Player player, String permission) {
        User user = PrimordialDevelop.luckPerms.getUserManager().getUser(player.getUniqueId());
        return user.data().contains(Node.builder(permission).build(), NodeEqualityPredicate.EXACT).asBoolean();
    }

    public static int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}
