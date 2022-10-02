package com.downYoutube2548.primordial.MMOCore.ClassInventory.Inventories;

import com.develop.primordial.primordialdevelop.PluginAPI.PrimordialAPI;
import com.develop.primordial.primordialdevelop.PrimordialDevelop;
import com.downYoutube2548.primordial.MMOCore.ClassInventory.ClassInventory;
import com.downYoutube2548.primordial.MMOCore.ClassInventory.Inventory;
import com.downYoutube2548.primordial.MMOCore.ClassInventory.InventoryPage;
import com.downYoutube2548.primordial.Utils2;
import de.tr7zw.nbtapi.NBTItem;
import net.Indyuce.mmocore.MMOCore;
import net.Indyuce.mmocore.api.player.PlayerData;
import net.Indyuce.mmocore.api.player.attribute.PlayerAttributes;
import net.Indyuce.mmocore.api.player.profess.PlayerClass;
import net.Indyuce.mmocore.api.player.profess.SavedClassInformation;
import net.Indyuce.mmocore.skill.Skill;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class ClassManageInventory extends ClassInventory {
    public ClassManageInventory() {
        super("main");
    }

    @Override
    public Inventory guiOpen(Player player, Inventory inventory, HashMap<String, Object> obj) {

        int page = (int) obj.get("page");
        PlayerClass selectedClass = MMOCore.plugin.classManager.get((String) obj.get("class"));

        PlayerData mmoPlayer = PlayerData.get(player.getUniqueId());

        if (selectedClass == null) {
            selectedClass = mmoPlayer.getProfess();
        }

        if (!isCharacterValid(selectedClass)) {
            String defaultGroup = PrimordialDevelop.configManager.getString("default.character");
            if (!Utils2.hasPermForGroup(player, defaultGroup)) {
                User user = PrimordialDevelop.luckPerms.getUserManager().getUser(player.getUniqueId());
                assert user != null;
                Utils2.addPermission(user, "primordial.class." + Utils2.getCharacterFromGroup(defaultGroup, 0).getId());
            }
            if (PrimordialDevelop.configManager.getBoolean("default.auto-apply-invalid-class.run-main-command")) {
                Utils2.setCharacterGroup(player, defaultGroup);
            }
            selectedClass = Utils2.getAvailableCharacterFromGroup(player, defaultGroup);
            if (selectedClass == null) {
                player.sendMessage(Utils2.colorize(PrimordialDevelop.configManager.getString("message.class-not-found-in-system")));
                return null;
            }
        }

        List<PlayerClass> listClass = new ArrayList<>();

        for (String characters : PrimordialDevelop.configManager.getConfigurationSection("characters").getKeys(false)) {

            List<String> playerClasses = PrimordialDevelop.configManager.getStringList("characters."+characters);
            Collections.reverse(playerClasses);

            // Check if player have specific class
            for (String playerClass : playerClasses) {
                if (Utils2.hasPermissionIgnoredOP(player, "primordial.class." + playerClass.toLowerCase(Locale.ROOT))) {
                    listClass.add(MMOCore.plugin.classManager.get(playerClass));
                    break;
                }
            }
        }

        List<Integer> listSlot = Arrays.asList(10,11,12,19,20,21,28,29,30,37,38,39);

        int l = ((page * 3) - 2);
        for (int i = 1; i <= listSlot.size() && i <= listClass.size() && l <= listClass.size(); i++) {
            PlayerClass playerClass = listClass.get(l-1);
            ItemStack item = playerClass.getIcon();

            // Enchant Item if its selected class
            if (playerClass.equals(selectedClass)) {
                ItemMeta metaIcon = item.getItemMeta();
                assert metaIcon != null;
                metaIcon.addEnchant(Enchantment.DURABILITY, 1, false);
                metaIcon.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                item.setItemMeta(metaIcon);
            }

            // get level of each class
            int level;
            if (mmoPlayer.getProfess().equals(playerClass)) {
                level = mmoPlayer.getLevel();
            } else {

                SavedClassInformation save_class = mmoPlayer.getClassInfo(playerClass);

                if (save_class != null) {
                    level = save_class.getLevel();
                } else {
                    level = 1;
                }

            }
            int max_level = playerClass.getMaxLevel();

            // show Class icon in specific slot
            inventory.setSlot(listSlot.get(i - 1), Utils2.addStrCustomNBT(
                    getItem(
                            item,
                            player,
                            "class-icon",
                            true,
                            new String[]{"%class-name%", "%level%", "%max_level%"},
                            new String[]{playerClass.getName(), Utils2.Format().format(level), Utils2.Format().format(max_level)}
                    ),
                    new String[]{"character", "page"},
                    new String[]{playerClass.getId(), String.valueOf(page)}
                    ),
                    "class-icon"
            );
            l++;
        }

        // other button

        ItemStack itemClass = selectedClass.getIcon();
        ItemMeta metaClass = itemClass.getItemMeta();
        metaClass.setDisplayName(selectedClass.getName());
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.addAll(selectedClass.getDescription());
        metaClass.setDisplayName(selectedClass.getName());
        metaClass.setLore(lore);
        itemClass.setItemMeta(metaClass);
        inventory.setSlot(getSettingInteger("selected-class-icon.slot"), itemClass, "selected-class");

        for (String s : getSettingKey("buttons")) {
            ItemStack item;
            if (s.equals("up")) {
                if (InventoryPage.isPageValid(listClass.size(), page-1, 3)) {
                    item = Utils2.addStrCustomNBT(getItem(player, "buttons."+s, new String[]{}, new String[]{}), new String[]{"character", "page"}, new String[]{selectedClass.getId(), String.valueOf(page)});
                } else {
                    item = new ItemStack(Material.valueOf(getSettingString("buttons.up.not-available-type")));
                }
            } else if (s.equals("down")) {
                if (InventoryPage.isPageValid(listClass.size(), page+1, 3)) {
                    item = Utils2.addStrCustomNBT(getItem(player, "buttons."+s, new String[]{}, new String[]{}), new String[]{"character", "page"}, new String[]{selectedClass.getId(), String.valueOf(page)});
                } else {
                    item = new ItemStack(Material.valueOf(getSettingString("buttons.down.not-available-type")));
                }
            } else {
                item = Utils2.addStrCustomNBT(getItem(player, "buttons."+s, new String[]{}, new String[]{}), new String[]{"character", "page"}, new String[]{selectedClass.getId(), String.valueOf(page)});
            }
            inventory.setSlot(getSettingInteger("buttons."+s+".slot"), item, s);
        }

        return inventory;
    }

    @Override
    public void guiInteract(InventoryClickEvent event, Player player, String buttonId) {
        event.setCancelled(true);
        switch (buttonId) {
            case "up" -> {
                ItemStack item = event.getCurrentItem();
                assert item != null;
                NBTItem nbt = new NBTItem(item);
                int page = Integer.parseInt(nbt.getString("page"));

                HashMap<String, Object> hash = new HashMap<>();
                hash.put("page", page - 1);
                PrimordialAPI.getClassInventoryManager().guiOpen(player, getId(), hash);
                break;
            }
            case "down" -> {
                ItemStack item = event.getCurrentItem();
                assert item != null;
                NBTItem nbt = new NBTItem(item);
                int page = Integer.parseInt(nbt.getString("page"));

                HashMap<String, Object> hash = new HashMap<>();
                hash.put("page", page + 1);
                PrimordialAPI.getClassInventoryManager().guiOpen(player, getId(), hash);
                break;
            }
            case "class-icon" -> {
                HashMap<String, Object> hash = new HashMap<>();
                ItemStack item = event.getCurrentItem();
                assert item != null;
                NBTItem nbt = new NBTItem(item);
                String character = nbt.getString("character");
                int page = Integer.parseInt(nbt.getString("page"));

                hash.put("page", page);
                hash.put("class", character);

                PrimordialAPI.getClassInventoryManager().guiOpen(player, "main", hash);
                break;
            }
            case "select" -> {
                ItemStack item = event.getCurrentItem();
                assert item != null;
                NBTItem nbt = new NBTItem(item);
                String character = nbt.getString("character");
                PlayerData mmoPlayer = PlayerData.get(player);
                PlayerClass oldClass = mmoPlayer.getProfess();
                PlayerClass clazz = MMOCore.plugin.classManager.get(character);

                if (mmoPlayer.getProfess() != clazz) {
                    String className = clazz.getName();

                    // save skill bound
                    if (getGroup(oldClass.getId()) != null) {
                        if (!mmoPlayer.getBoundSkills().isEmpty()) {
                            StringBuilder skills = new StringBuilder();
                            for (Skill.SkillInfo skill : mmoPlayer.getBoundSkills()) {
                                String skillId = skill.getSkill().getId();
                                skills.append(skillId).append(":");
                            }
                            skills.deleteCharAt(skills.length() - 1);
                            PrimordialDevelop.PlayerData.get(player.getUniqueId().toString()).set("SkillBound." + getGroup(oldClass.getId()), skills.toString());
                        }
                    }

                    // set player class
                    player.sendMessage(Utils2.translatePlaceholder(Utils2.messageWithColor("message.change-class"), new String[]{"%class%"}, new String[]{className}));
                    Utils2.setClass(player, clazz);
                    //player.sendMessage(PrimordialDevelop.configManager.getString("weapon-type."+getGroup(clazz.getId())));
                    //moPlayer.setAttribute(PrimordialDevelop.configManager.getString("weapon-type."+getGroup(clazz.getId())), 1);
                    PlayerAttributes.AttributeInstance instance = mmoPlayer.getAttributes().getInstance(MMOCore.plugin.attributeManager.get(PrimordialDevelop.configManager.getString("weapon-type."+getGroup(clazz.getId()))));
                    instance.setBase(Math.min(MMOCore.plugin.attributeManager.get(PrimordialDevelop.configManager.getString("weapon-type."+getGroup(clazz.getId()))).getMax(), 1));
                    player.closeInventory();

                    // set skill bound
                    if (getGroup(clazz.getId()) != null) {
                        String savedBoundSkill = PrimordialDevelop.PlayerData.get(player.getUniqueId().toString()).getString("SkillBound." + getGroup(clazz.getId()));
                        String defaultBoundSkill = PrimordialDevelop.configManager.getString("default-bound-skill."+getGroup(clazz.getId()));
                        String[] skills = null;
                        if (savedBoundSkill != null) {
                            skills = savedBoundSkill.split(":");

                        } else if (defaultBoundSkill != null) {
                            skills = defaultBoundSkill.split(":");
                        }
                        if (skills != null) {
                            int i = 0;
                            for (String s : skills) {
                                Skill skill = MMOCore.plugin.skillManager.get(s);
                                Skill.SkillInfo skillInfo = skill.newSkillInfo(mmoPlayer.getSkillLevel(skill));
                                mmoPlayer.setBoundSkill(i, skillInfo);
                                i++;
                            }
                        }
                    }
                }
                break;
            }
            case "upgrade" -> {
                PlayerData mmoPlayer = PlayerData.get(player);
                HashMap<String, Object> hash = new HashMap<>();
                ItemStack item = event.getCurrentItem();
                assert item != null;
                NBTItem nbt = new NBTItem(item);
                String character = nbt.getString("character");
                PlayerClass clazz = MMOCore.plugin.classManager.get(character);
                hash.put("character", character);

                if (Utils2.getClassLevel(mmoPlayer, clazz) < clazz.getMaxLevel()) {
                    int amount = 1;
                    hash.put("amount", amount);
                    hash.put("upgrader", "upgrade_I");

                    PrimordialAPI.getClassInventoryManager().guiOpen(player, "level-upgrade", hash);
                } else {
                    if (getNextCharacter(clazz) != null) {
                        PrimordialAPI.getClassInventoryManager().guiOpen(player, "character-upgrade", hash);
                    }
                }
                break;
            }
            case "attr" -> {
                player.performCommand("mmocore:attributes");
            }
            case "skill" -> {
                player.performCommand("mmocore:skills");
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
