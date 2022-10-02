package com.downYoutube2548.primordial.DamageHandle;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class FallDamage implements Listener {

    @EventHandler
    public void onFallDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player player && (e.getCause().equals(EntityDamageEvent.DamageCause.FALL))) {
            float height = e.getEntity().getFallDistance();
            double damage = ((0.01002 * (height * height) - 0.16) / 100) * (player.getMaxHealth());
            if (damage < 0) { damage = 0; };
            e.setDamage(damage + e.getEntity().getFallDistance());
            /*e
            Bukkit.broadcastMessage(player.getName()+": Fall "+e.getDamage()+" -> "+(e.getDamage()/100.0)*player.getMaxHealth());
            e.setDamage((e.getDamage()/100.0)*player.getMaxHealth());*/
        }
    }
}
