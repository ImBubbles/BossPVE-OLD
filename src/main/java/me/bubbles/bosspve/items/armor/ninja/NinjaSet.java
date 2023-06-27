package me.bubbles.bosspve.items.armor.ninja;

import me.bubbles.bosspve.BossPVE;
import me.bubbles.bosspve.items.manager.armor.Armor;
import me.bubbles.bosspve.items.manager.armor.ArmorSet;
import me.bubbles.bosspve.util.UtilChances;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class NinjaSet extends ArmorSet {

    public NinjaSet(BossPVE plugin) {
        super(plugin);
    }

    @Override
    public void onEvent(Event event) {
        if(event instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) event;
            if(!(e.getDamager() instanceof Player)) {
                return;
            }
            Player player = (Player) e.getDamager();
            if(!wearingFullSet(player,true)) {
                return;
            }
            if(!UtilChances.rollTheDice(1,100,10)) {
                return;
            }
            Entity entity = e.getEntity();
            if(entity instanceof Player) {
                return;
            }
            if(entity.isDead()) {
                return;
            }
            entity.setLastDamageCause(new EntityDamageByEntityEvent(player, entity, EntityDamageEvent.DamageCause.CUSTOM, 1000));
        }
    }

    @Override
    public Armor getBoots() {
        return new NinjaBoots(plugin);
    }

    @Override
    public Armor getPants() {
        return new NinjaPants(plugin);
    }

    @Override
    public Armor getChestplate() {
        return new NinjaChestplate(plugin);
    }

    @Override
    public Armor getHelmet() {
        return new NinjaHelmet(plugin);
    }

}
