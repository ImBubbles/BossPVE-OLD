package me.bubbles.bosspve.events.manager;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class Listeners implements Listener {

    private EventManager eventManager;

    public Listeners(EventManager eventManager) {
        this.eventManager=eventManager;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        eventManager.onEvent(e);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        eventManager.onEvent(e);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        eventManager.onEvent(e);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        eventManager.onEvent(e);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        eventManager.onEvent(e);
    }

    @EventHandler
    public void onPrepareAnvil(PrepareAnvilEvent e) {
        eventManager.onEvent(e);
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent e) {
        eventManager.onEvent(e);
    }

    @EventHandler
    public void onEntityDamageEntity(EntityDamageByEntityEvent e) {
        eventManager.onEvent(e);
    }

    @EventHandler
    public void onPlayerItemHeld(PlayerItemHeldEvent e) {
        eventManager.onEvent(e);
    }

}
