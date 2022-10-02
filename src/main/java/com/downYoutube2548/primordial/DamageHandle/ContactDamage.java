package com.downYoutube2548.primordial.DamageHandle;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class ContactDamage implements Listener {

    @EventHandler
    public void onContactDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player player && (e.getCause().equals(EntityDamageEvent.DamageCause.CONTACT))) {
            e.setDamage((1.0 / 100.0) * player.getMaxHealth());
        }
    }
}
