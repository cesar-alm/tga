package fr.badcookie20.tga.inventories.manager;

import fr.badcookie20.tga.player.TGAPlayer;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;

/**
 * Particular type of inventories. You should make your inventory extend this class when it is a long one to load, in
 * order to avoid to reload it each time the player opens it, even if it has not changed at all
 */
public abstract class SaveableInventory implements TGAInventory {

    private Map<TGAPlayer, Inventory> savedInventory;

    public SaveableInventory() {
        this.savedInventory = new HashMap<>();
    }

    /**
     * Use this function to notify that the inventory has changed, for the specified player. It will be completely reloaded next time
     * @param p the player whose inventory has changed
     */
    public void updateInventory(TGAPlayer p) {
        this.savedInventory.remove(p);
    }

    /**
     * Use this function at the end of the <code>get</code> function of the inventory
     * @param p the player whose inventory has to be saved
     * @param inventory the inventory associated
     * @return the inventory saved
     */
    public Inventory saveInventory(TGAPlayer p, Inventory inventory) {
        this.savedInventory.put(p, inventory);
        return inventory;
    }

    /**
     * Tells you exactly what its name says
     * @param p the player associated
     * @return what the name says
     */
    public boolean isSaved(TGAPlayer p) {
        return this.savedInventory.containsKey(p);
    }

    /**
     * Call this function at the beginning of the <code>get</code> function. You have to make sure it is saved first,
     * using the <code>isSaved</code> function
     * @param p the player involved
     * @return an inventory
     */
    public Inventory getSavedInventory(TGAPlayer p) {
        return this.savedInventory.get(p);
    }

}
