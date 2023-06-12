package me.bubbles.bosspve.events;

import me.bubbles.bosspve.BossPVE;
import me.bubbles.bosspve.events.manager.Event;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;

public class AnvilEnchantedBook extends Event {

    public AnvilEnchantedBook(BossPVE plugin) {
        super(plugin, InventoryClickEvent.class);
    }

    @Override
    public void onEvent(org.bukkit.event.Event event) {
        InventoryClickEvent e = (InventoryClickEvent) event;
        if(!(e.getClickedInventory() instanceof AnvilInventory)) {
            return;
        }
        if(!e.getSlotType().equals(InventoryType.SlotType.RESULT)) {
            return;
        }
        if(e.getWhoClicked().getInventory().firstEmpty()==-1) {
            return;
        }
        AnvilInventory inventory = (AnvilInventory) e.getClickedInventory();
        ItemStack firstSlot = inventory.getContents()[0];
        ItemStack secondSlot = inventory.getContents()[1];
        ItemStack thirdSlot = e.getCurrentItem();
        if(firstSlot==null||secondSlot==null||thirdSlot==null) {
            return;
        }
        if(!(secondSlot.getType().equals(Material.ENCHANTED_BOOK))) {
            return;
        }
        Player player = (Player) e.getWhoClicked();
        player.getInventory().addItem(thirdSlot);
        inventory.setItem(0,null);
        inventory.setItem(1,null);
        inventory.setItem(2,null);
    }

}
