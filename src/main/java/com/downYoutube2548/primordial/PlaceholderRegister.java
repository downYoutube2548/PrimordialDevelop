package com.downYoutube2548.primordial;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlaceholderRegister extends PlaceholderExpansion {
    @Override
    public @NotNull String getIdentifier() {
        return "primordial";
    }

    @Override
    public @NotNull String getAuthor() {
        return "DT";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    @Override
    public @Nullable String onRequest(OfflinePlayer offlinePlayer, @NotNull String params) {
        String[] s = params.split("_");
        if (offlinePlayer.isOnline()) {
            Player player = Bukkit.getPlayer(offlinePlayer.getName());
            if (s[0].equals("falldistance")) {
                return String.valueOf(player.getFallDistance());
            }
        }
        return null;
    }
}
