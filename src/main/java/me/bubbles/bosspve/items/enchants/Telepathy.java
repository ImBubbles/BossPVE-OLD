package me.bubbles.bosspve.items.enchants;

import me.bubbles.bosspve.items.manager.enchant.Enchant;
import me.bubbles.bosspve.items.manager.Item;
import me.bubbles.bosspve.items.manager.ItemManager;
import org.bukkit.Material;

import java.util.List;

public class Telepathy extends Enchant {

    public Telepathy(ItemManager itemManager) {
        super(itemManager, "Telepathy", Material.BOOK, 1);
        getEnchantItem().setDisplayName("&5Telepathy");
        allowedTypes.addAll(
                List.of(
                        Item.Type.WEAPON,
                        Item.Type.ENCHANT
                )
        );
    }

    @Override
    public String getDescription() {
        return "Teleports items into your inventory";
    }

}
