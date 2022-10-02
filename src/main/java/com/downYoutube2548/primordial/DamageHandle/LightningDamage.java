package com.downYoutube2548.primordial.DamageHandle;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class LightningDamage implements Listener {

    @EventHandler
    public void onLightningDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player player && (e.getCause().equals(EntityDamageEvent.DamageCause.LIGHTNING))) {
            e.setDamage(50);
        }
    }
}
