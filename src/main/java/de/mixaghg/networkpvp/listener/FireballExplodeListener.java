package de.mixaghg.networkpvp.listener;

import org.bukkit.entity.Fireball;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

public class FireballExplodeListener implements Listener {

    @EventHandler
    public void onExplode(EntityExplodeEvent event) {
        if (event.getEntity() instanceof Fireball || event.getEntity() instanceof TNTPrimed) {
            event.blockList().clear();
        }
    }
}