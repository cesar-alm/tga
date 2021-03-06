package fr.badcookie20.tga.inventories.manager;

import fr.badcookie20.tga.player.TGAPlayer;
import org.bukkit.inventory.Inventory;

/**
 * Specific type of inventory, where its content does not depend on the player
 */
public abstract class UniversalInventory implements TGAInventory {

    private Inventory inventory;

    public UniversalInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    /**
     * Returns the inventory
     * @param p the player you want it personalized to (it is obviously not used !)
     * @return the inventory
     */
    @Override
    public Inventory get(TGAPlayer p) {
        return this.inventory;
    }
}
