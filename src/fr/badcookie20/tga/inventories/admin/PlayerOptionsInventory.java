package fr.badcookie20.tga.inventories.admin;

import fr.badcookie20.tga.inventories.manager.InventoriesManager;
import fr.badcookie20.tga.inventories.manager.InventoryType;
import fr.badcookie20.tga.inventories.manager.SaveableInventory;
import fr.badcookie20.tga.player.TGAPlayer;
import fr.badcookie20.tga.utils.BukkitUtils;
import fr.badcookie20.tga.utils.NextUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;

public class PlayerOptionsInventory extends SaveableInventory {

    public static final ItemStack KICK = BukkitUtils.createItemStack(Material.SADDLE, ChatColor.GREEN + "Kick le joueur", null);
    public static final ItemStack BAN = BukkitUtils.createItemStack(Material.FLINT_AND_STEEL, ChatColor.GREEN + "Bannir le joueur", null);
    public static final ItemStack ERASE_DATA = BukkitUtils.createItemStack(Material.SPONGE, ChatColor.GREEN + "Effacer les données du joueur",
            Collections.singletonList(ChatColor.GOLD + "" + ChatColor.ITALIC + "Cette opération va kicker le joueur"));

    @Override
    public Inventory get(TGAPlayer p) {
        Inventory inventory = Bukkit.createInventory(null, 9, ChatColor.BLACK + "Options du joueur");

        inventory.setItem(3, KICK);
        inventory.setItem(4, BAN);
        inventory.setItem(5, ERASE_DATA);

        inventory.setItem(inventory.getSize() - 1, InventoriesManager.GO_BACK);

        return inventory;
    }

    @Override
    public InventoryType getType() {
        return InventoryType.PLAYER_OPTIONS;
    }

    @Override
    public void handleClicking(TGAPlayer p) {
        ItemStack clicked = NextUtils.forceNextItem(p, InventoryType.PLAYER_OPTIONS);

        NextUtils.updateClickedItemOf(p.getBukkitPlayer(), clicked);
    }
}
