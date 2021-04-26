package fr.badcookie20.tga.utils;

import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagInt;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public class ItemIDUtils {

    private static final String ID_TAG = "TGAItemID";

    /**
     * Adds the specified id to the specified item, in the tag
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
