package fr.badcookie20.tga.inventories.manager;

import fr.badcookie20.tga.player.TGAPlayer;
import org.bukkit.inventory.Inventory;

/**
 * Interface of the inventories used in this game. All the inventories used should implement this interface, in order to
 * use the power of the InventoriesManager class
 */
public interface TGAInventory {

    /**
     * Returns this inventory, personalized to the player
     * @param p the player you want it personalized to
     * @return a personalized inventory
     */
    Inventory get(TGAPlayer p);

    /**
     * Returns the type of the inventory
     * @return the type of the inventory
     */
    InventoryType getType();

    /**
     * Handles the clicking (ie the interaction of the player with the inventory). Always make sure to call asynchronously !!
     * @param p the player associated with the inventory
     */
    void handleClicking(TGAPlayer p);

}
