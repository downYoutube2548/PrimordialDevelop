package com.develop.primordial.primordialdevelop;

import com.develop.primordial.primordialdevelop.PluginAPI.PrimordialAPI;
import com.downYoutube2548.primordial.DailyQuest.Quests;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class PrimordialData {
    private UUID uuid;

    public static PrimordialData get(UUID uuid) {
        return new PrimordialData(uuid);
    }

    private PrimordialData(UUID uuid) {
        this.uuid = uuid;
    }

    public void openCharacterMenu(int page) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(this.uuid);
        if (player.isOnline()) {
            HashMap<String, Object> hash = new HashMap<>();
            hash.put("page", page);
            PrimordialAPI.getClassInventoryManager().guiOpen((Player)player, "main", hash);
        }
    }

    public void setCharacter(String group) {

    }

    public void giveCharacter(String group) {

    }

    public void removeCharacter(String group) {

    }

    public void getCharacter() {

    }

    public YamlConfiguration getYamlData() {
        return PrimordialDevelop.PlayerData.get(this.uuid.toString());
    }

    public Quests getQuestManager() {
        return new Quests(this.uuid, this);
    }
}
