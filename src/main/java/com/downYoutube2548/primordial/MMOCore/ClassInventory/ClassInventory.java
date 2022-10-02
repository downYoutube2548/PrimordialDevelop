package com.downYoutube2548.primordial.MMOCore.ClassInventory;

import com.develop.primordial.primordialdevelop.PrimordialDevelop;
import com.downYoutube2548.primordial.Utils2;
import net.Indyuce.mmocore.MMOCore;
import net.Indyuce.mmocore.api.player.profess.PlayerClass;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public abstract class ClassInventory implements InventoryHolder {
    public abstract Inventory guiOpen(Player player, Inventory inventory, HashMap<String, Object> obj);
    public abstract void guiInteract(InventoryClickEvent event, Player player, String buttonId);
    public abstract int guiSize();
    public abstract String guiTitle();

    private final String Id;
    private final int size;
    private final String title;

    public ClassInventory(String Id) {
        this.Id = Id;
        if (guiSize() == 0) { this.size = 54; }
        else { this.size = guiSize(); }

        if (guiTitle() == null) { this.title = ""; }
        else { this.title = guiTitle(); }
    }

    public String getId() { return this.Id; }
    public int getSize() { return this.size; }
    public String getTitle() { return this.title; }

    public String getSettingString(String path) {
        return PrimordialDevelop.configManager.getString("menus."+this.Id+"."+path);
    }
    public Integer getSettingInteger(String path) {
        return PrimordialDevelop.configManager.getInt("menus."+this.Id+"."+path);
    }
    public List<String> getSettingStringList(String path) {
        return PrimordialDevelop.configManager.getStringList("menus."+this.Id+"."+path);
    }
    public List<Integer> getSettingIntegerList(String path) {
        return PrimordialDevelop.configManager.getIntegerList("menus."+this.Id+"."+path);
    }
    public Set<String> getSettingKey(String path) {
        return PrimordialDevelop.configManager.getConfigurationSection("menus."+this.Id+"."+path).getKeys(false);
    }
    public Boolean getSettingBoolean(String path) {
        return PrimordialDevelop.configManager.getBoolean("menus."+this.Id+"."+path);
    }

    public ItemStack getItem(Player player, String path, String[] target, String[] replacement) {
        return Utils2.itemCreator(player,"menus."+this.Id+"."+path, target, replacement);
    }

    public ItemStack getItem(ItemStack item, Player player, String path, boolean add,String[] target, String[] replacement) {
        return Utils2.itemCreator(item, player, add, "menus."+this.Id+"."+path, target, replacement);
    }

    protected String getGroup(String character) {

        Map<String, Object> map = PrimordialDevelop.configManager.getConfigurationSection("characters").getValues(false);

        for (String character_group : map.keySet()) {
            List<String> l = convertObjectToList(map.get(character_group));
            if (l.contains(character)) {
                return character_group;
            }
        }
        return null;
    }

    protected PlayerClass getNextCharacter(PlayerClass current_character) {
        int i = 0;
        int number = 0;
        List<String> list_character = PrimordialDevelop.configManager.getStringList("characters."+getGroup(current_character.getId()));
        for (String s : list_character) {
            if (s.equals(current_character.getId())) {
                number = i;
                break;
            }
            i++;
        }
        if (list_character.size() >= number+1) {
            return MMOCore.plugin.classManager.get(list_character.get(number + 1));
        } else { return null; }
    }

    protected PlayerClass getCharacterFromGroup(String group, int n) {
        List<String> list_character = PrimordialDevelop.configManager.getStringList("characters."+group);
        return MMOCore.plugin.classManager.get(list_character.get(n));
    }

    protected List<String> convertObjectToList(Object obj) {
        List<?> l = (List<?>)obj;
        List<String> l2 = new ArrayList<>();
        for (Object o : l) {
            l2.add(String.valueOf(o));
        }
        return l2;
    }

    protected int getAmountOfItemInInv(Player player, ItemStack item) {
        int i = 0;
        org.bukkit.inventory.Inventory inventory = player.getInventory();
        for (ItemStack is : inventory.getContents()) {
            if (is != null && is.getType() != Material.AIR) {
                int amount = is.getAmount();
                if (is.isSimilar(item)) {
                    i = i + amount;
                }
            }
        }
        return i;
    }

    // return true if player have enough item and money
    protected boolean hasEnoughResource(Player player, String character_id) {
        boolean output = true;
        for (ItemStack item : getItemCost(character_id)) {
            if (getAmountOfItemInInv(player, item) < item.getAmount()) {
                output = false;
                break;
            }
        }
        double balance = PrimordialDevelop.economy.getBalance(player);
        return output && (balance >= getMoneyCost(character_id));
    }

    protected boolean hasEnoughItem(Player player, String character_id) {
        boolean output = true;
        for (ItemStack item : getItemCost(character_id)) {
            if (getAmountOfItemInInv(player, item) < item.getAmount()) {
                output = false;
                break;
            }
        }
        return output;
    }

    protected boolean hasEnoughMoney(Player player, String character_id) {
        double balance = PrimordialDevelop.economy.getBalance(player);
        return balance >= getMoneyCost(character_id);
    }

    // return real item cost
    protected List<ItemStack> getItemCost(String character_id) {
        List<ItemStack> output = new ArrayList<>();
        List<String> all_cost = PrimordialDevelop.configManager.getStringList("upgrade-cost."+character_id);
        for (String each_cost : all_cost) {
            if (each_cost.startsWith("minecraft") || each_cost.startsWith("mmoitem")) {
                ItemStack item = Utils2.getItem(each_cost.split(";")[0]);
                item.setAmount(Integer.parseInt(each_cost.split(";")[1]));
                output.add(item);
            }
        }
        return output;
    }

    // return item cost icon
    protected List<ItemStack> getItemCostIcon(String character_id) {
        List<ItemStack> output = new ArrayList<>();
        List<String> all_cost = PrimordialDevelop.configManager.getStringList("upgrade-cost."+character_id);
        for (String each_cost : all_cost) {
            if (each_cost.startsWith("minecraft") || each_cost.startsWith("mmoitem")) {
                ItemStack item = Utils2.getItem(each_cost.split(";")[0]);
                item.setAmount(Integer.parseInt(each_cost.split(";")[1]));
                item = Utils2.addStrCustomNBT(item, "ITEM_ID", each_cost.split(";")[0]);
                output.add(item);
            }
        }
        return output;
    }

    // return money cost
    protected double getMoneyCost(String character_id) {
        double output = 0;
        List<String> all_cost = PrimordialDevelop.configManager.getStringList("upgrade-cost."+character_id);
        for (String each_cost : all_cost) {
            if (each_cost.startsWith("balance")) {
                output = output + Double.parseDouble(each_cost.split(";")[1]);
            }
        }

        return output;
    }

    protected boolean isCharacterValid(PlayerClass character) {
        return getGroup(character.getId()) != null;
    }
}
