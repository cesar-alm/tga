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
public class ItemUIDUtils {

    private static final String UID_TAG = "TGAItemUID";
    private static final Map<Integer, Card> registeredCards;
    private static int maxUId;

    static {
        registeredCards = new HashMap<>();
        maxUId = 999;
    }

    /**
     * Registers a card in the system.
     * @param card the card to be registered
     * @return the uid the card has been given
     */
    public static int registerCard(Card card) {
        maxUId += 1;
        registeredCards.put(maxUId, card);
        return maxUId;
    }

    /**
     * Gets the card corresponding to the uid.
     * @param uid the uid
     * @return a card, or <code>null</code> if this uid has not been yet assigned.
     */
    public static Card getRegisteredCardByUID(int uid) {
        return registeredCards.get(uid);
    }

    /**
     * Gets the card corresponding to the item.
     * @param item the item.
     * @return a card, or <code>null</code> if this Item has not been registered/is incorrect.
     */
    public static Card getCardByItem(ItemStack item) {
        return getRegisteredCardByUID(getItemUID(item));
    }

    /**
     * Adds the specified uid to the specified item, in the tag.
     * Does not save it in the registered cards.
     * @param uid the uid in the ID_TAG tag. If the uid is negative, this method does nothing.
     * @return an item with the uid.
     */
    public static ItemStack addItemUID(ItemStack item, int uid) {
        if(uid < 0) {
            return item;
        }

        net.minecraft.server.v1_8_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);

        NBTTagCompound compound = nmsStack.hasTag() ? nmsStack.getTag() : new NBTTagCompound();
        compound.set(UID_TAG, new NBTTagInt(uid));
        nmsStack.setTag(compound);

        return CraftItemStack.asBukkitCopy(nmsStack);
    }

    /**
     * Returns the items uid (located in the tag)
     * @param item the item whose uid we wish to get.
     * @return the uid, or -1 if there is no such uid.
     */
    public static int getItemUID(ItemStack item) {
        net.minecraft.server.v1_8_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);

        if(!nmsStack.hasTag()) {
            return -1;
        }

        NBTTagCompound compound = nmsStack.getTag();

        if(!compound.hasKey(UID_TAG)) {
            return -1;
        }

        return compound.getInt(UID_TAG);
    }

}
