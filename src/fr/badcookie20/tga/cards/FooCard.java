package fr.badcookie20.tga.cards;

import org.bukkit.inventory.ItemStack;

public class FooCard extends Card {

    public FooCard() {
        super(null, -1, "FOO", null, null);
    }

    @Override
    public ItemStack createItemStack() {
        System.out.println("Attempting to create ItemStack for the FooCard");
        return null;
    }
}
