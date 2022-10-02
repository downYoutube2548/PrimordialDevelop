package com.downYoutube2548.primordial.DamageHandle;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class FireDamage implements Listener {

    @EventHandler
    public void onFireDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player player && (e.getCause().equals(EntityDamageEvent.DamageCause.FIRE) || e.getCause().equals(EntityDamageEvent.DamageCause.FIRE_TICK))) {
            e.setDamage((1.0/100.0)*player.getMaxHealth());
        }
    }
}
