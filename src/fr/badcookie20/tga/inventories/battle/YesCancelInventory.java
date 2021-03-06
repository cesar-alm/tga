package fr.badcookie20.tga.inventories.battle;

import fr.badcookie20.tga.inventories.manager.InventoryType;
import fr.badcookie20.tga.inventories.manager.UniversalInventory;
import fr.badcookie20.tga.player.TGAPlayer;
import fr.badcookie20.tga.utils.BukkitUtils;
import fr.badcookie20.tga.utils.NextUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class YesCancelInventory extends UniversalInventory {

    private static final Inventory inventory;

    public static final ItemStack YES = BukkitUtils.createItemStack(Material.SLIME_BALL, ChatColor.GREEN + "Oui", null);
    public static final ItemStack CANCEL = BukkitUtils.createItemStack(Material.REDSTONE, ChatColor.RED + "Annuler", null);

    static {
        inventory = Bukkit.createInventory(null, 27, ChatColor.BLACK + "Confirmation");

        inventory.setItem(12, YES);
        inventory.setItem(14, CANCEL);
    }

    public YesCancelInventory() {
        super(inventory);
    }

    @Override
    public InventoryType getType() {
        return InventoryType.YES_CANCEL;
    }

    @Override
    public void handleClicking(TGAPlayer p) {
        ItemStack clicked = NextUtils.forceNextItem(p, InventoryType.YES_CANCEL);

        NextUtils.updateClickedItemOf(p.getBukkitPlayer(), clicked);
    }
}
