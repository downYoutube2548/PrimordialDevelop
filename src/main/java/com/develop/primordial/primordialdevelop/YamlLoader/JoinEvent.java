package com.develop.primordial.primordialdevelop.YamlLoader;

import com.develop.primordial.primordialdevelop.PrimordialDevelop;
import com.downYoutube2548.primordial.Utils2;
import net.Indyuce.mmocore.MMOCore;
import net.Indyuce.mmocore.api.player.PlayerData;
import net.Indyuce.mmocore.api.player.profess.PlayerClass;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinEvent implements Listener {
    private final PrimordialDevelop main;

    public JoinEvent(PrimordialDevelop main) {
        this.main = main;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
            Player player = event.getPlayer();
            LoadHashFile.loadHashFile(PrimordialDevelop.Files, PrimordialDevelop.PlayerData, main.getDataFolder() + "/PlayerData", player.getUniqueId().toString());
        });

        PlayerData mmoPlayer = PlayerData.get(event.getPlayer().getUniqueId());
        PlayerClass playerClass = mmoPlayer.getProfess();
        if (Utils2.isCharacterValid(playerClass)) {
            String defaultGroup = PrimordialDevelop.configManager.getString("default.character");
            if (!Utils2.hasPermForGroup(event.getPlayer(), defaultGroup)) {
                User user = PrimordialDevelop.luckPerms.getUserManager().getUser(event.getPlayer().getUniqueId());
                assert user != null;
                Utils2.addPermission(user, "primordial.class." + Utils2.getCharacterFromGroup(defaultGroup, 0).getId());
            }
            if (PrimordialDevelop.configManager.getBoolean("default.auto-apply-invalid-class.join")) {
                Bukkit.getScheduler().runTaskLater(PrimordialDevelop.getInstance(), () -> {
                    Utils2.setCharacterGroup(event.getPlayer(), defaultGroup);
                }, 20);
            }
        }
    }
}
