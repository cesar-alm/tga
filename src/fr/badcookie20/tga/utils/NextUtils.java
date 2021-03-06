package fr.badcookie20.tga.utils;

import fr.badcookie20.tga.inventories.manager.InventoriesManager;
import fr.badcookie20.tga.inventories.manager.InventoryType;
import fr.badcookie20.tga.player.TGAPlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.spigotmc.AsyncCatcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 */

public class NextUtils {

    public static ItemStack CLOSED_INVENTORY = BukkitUtils.createItemStack(Material.BEDROCK, "ERR_CLOSED", null);

    private static Map<Player, String> sayings;
    private static List<Player> cancelSayings;

    private static Map<Player, ItemStack> clickedItems;
    private static Map<Player, Integer> clickedSlots;
    private static Map<Player, Inventory> clickedInventories;
    private static List<Player> cancelClicking;

    static {
        sayings = new HashMap<>();
        cancelSayings = new ArrayList<>();

        clickedItems = new HashMap<>();
        cancelClicking = new ArrayList<>();

        clickedSlots = new HashMap<>();

        clickedInventories = new HashMap<>();
    }

    public static String getNextMessageOf(Player p, boolean cancelIt) {
        if(!assertAsynCatcher()) return null;

        if(cancelIt && !cancelSayings.contains(p)) cancelSayings.add(p);

        String previousMessage = getLastMessageOf(p);

        if(previousMessage != null) {
            updateChatOf(p, null);
        }

        while (getLastMessageOf(p) == null) {}

        return BukkitUtils.placeColor(getLastMessageOf(p));
    }

    public static String getLastMessageOf(Player p) {
        return sayings.get(p);
    }

    public synchronized static void updateChatOf(Player p, String message) {
        if(sayings.containsKey(p)) {
            sayings.replace(p, message);
        }else{
            sayings.put(p, message);
        }
    }

    public synchronized static boolean needsSayingCancel(Player p) {
        return cancelSayings.contains(p);
    }

    public synchronized static void updateSayingCancel(Player p) {
        cancelSayings.remove(p);
    }

    public synchronized static boolean needsClickingCancel(Player p) { return cancelClicking.contains(p); }

    public synchronized static void updateClickingsCancel(Player p) {
        cancelClicking.remove(p);
    }

    public static ItemStack getNextClickedItemOf(Player p, boolean cancelIt) {
        if (!assertAsynCatcher()) return null;

        if(cancelIt && !cancelClicking.contains(p)) cancelClicking.add(p);
        ItemStack previousClick = getLastClickedItemOf(p);

        if(previousClick != null) {
            updateClickedItemOf(p, null);
        }

        while (getLastClickedItemOf(p) == null) {
        }

        if (getLastClickedItemOf(p) == CLOSED_INVENTORY) return null;

        return getLastClickedItemOf(p);
    }

    public static ItemStack forceNextItem(TGAPlayer p, InventoryType type) {
        ItemStack item;

        int i = 0;

        do {
            if(i != 0) {
                InventoriesManager.getInstance().openInventory(p, type);
            }

            item = getNextClickedItemOf(p.getBukkitPlayer(), true);

            if(item != null && item.getType() == Material.AIR) {
                i = -1;
            }

            i++;
        }while(item == null || item.getType() == Material.AIR || item.equals(CLOSED_INVENTORY));

        p.getBukkitPlayer().closeInventory();

        return item;
    }

    public static ItemStack getLastClickedItemOf(Player p) {
        return clickedItems.get(p);
    }

    public synchronized static void updateClickedItemOf(Player p, ItemStack item) {
        if(clickedItems.containsKey(p)) {
            clickedItems.replace(p, item);
        }else{
            clickedItems.put(p, item);
        }
    }

    public static int getLastClickedRawSlotOf(Player p) {
        return clickedSlots.get(p);
    }

    public synchronized static void updateClickedRawSlotOf(Player p, int slot) {
        if(clickedSlots.containsKey(p)) {
            clickedSlots.replace(p, slot);
        }else{
            clickedSlots.put(p, slot);
        }
    }

    public static Inventory getLastClickedInventoryOf(Player p) {
        return clickedInventories.get(p);
    }

    public static void updateClickedInventoryOf(Player p, Inventory inventory) {
        if(clickedInventories.containsKey(p)) {
            clickedInventories.replace(p, inventory);
        }else{
            clickedInventories.put(p, inventory);
        }
    }

    private static boolean assertAsynCatcher() {
        return !AsyncCatcher.enabled;
    }
}
