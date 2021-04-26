package fr.badcookie20.tga.utils;

import fr.badcookie20.tga.cards.Card;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagInt;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * This utility class is useful to keep track of which card maps to which item.
 * It prevents side-effects.
 */
public class ItemIDUtils {

    private static final String ID_TAG = "TGAItemID";
    private static final Map<Integer, Card> registeredCards;
    private static int maxId;

    static {
        registeredCards = new HashMap<>();
        maxId = 999;
    }

    /**
     * Registers a card in the system.
     * @param card the card to be registered
     * @return the id the card has been given
     */
    public static int registerCard(Card card) {
        maxId += 1;
        registeredCards.put(maxId, card);
        return maxId;
    }

    /**
     * Gets the card corresponding to the id.
     * @param id the id
     * @return a card, or <code>null</code> if this id has not been yet assigned.
     */
    public static Card getRegisteredCardByID(int id) {
        return registeredCards.get(id);
    }

    /**
     * Gets the card corresponding to the item.
     * @param item the item.
     * @return a card, or <code>null</code> if this Item has not been registered/is incorrect.
     */
    public static Card getCardByItem(ItemStack item) {
        return getRegisteredCardByID(getItemID(item));
    }

    /**
     * Adds the specified id to the specified item, in the tag.
     * Does not save it in the registered cards.
     * @param id the id in the ID_TAG tag. If the id is negative, this method does nothing.
     * @return an item with the id.
     */
    public static ItemStack addItemID(ItemStack item, int id) {
        if(id < 0) {
            return item;
        }

        net.minecraft.server.v1_8_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);

        NBTTagCompound compound = nmsStack.hasTag() ? nmsStack.getTag() : new NBTTagCompound();
        compound.set(ID_TAG, new NBTTagInt(id));
        nmsStack.setTag(compound);

        return CraftItemStack.asBukkitCopy(nmsStack);
    }

    /**
     * Returns the items id (located in the tag)
     * @param item the item whose id we wish to get.
     * @return the id, or -1 if there is no such id.
     */
    public static int getItemID(ItemStack item) {
        net.minecraft.server.v1_8_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);

        if(!nmsStack.hasTag()) {
            return -1;
        }

        NBTTagCompound compound = nmsStack.getTag();

        if(!compound.hasKey(ID_TAG)) {
            return -1;
        }

        return compound.getInt(ID_TAG);
    }

}
