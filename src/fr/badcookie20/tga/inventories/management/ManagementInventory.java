package fr.badcookie20.tga.inventories.management;

import fr.badcookie20.tga.inventories.manager.InventoriesManager;
import fr.badcookie20.tga.inventories.manager.InventoryType;
import fr.badcookie20.tga.inventories.manager.SaveableInventory;
import fr.badcookie20.tga.player.TGAPlayer;
import fr.badcookie20.tga.utils.BukkitUtils;
import fr.badcookie20.tga.utils.MiscUtils;
import fr.badcookie20.tga.utils.NextUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class ManagementInventory extends SaveableInventory {

    private static final ItemStack ACCESS_CARD_LIST = BukkitUtils.createItemStack(Material.PAPER,
            ChatColor.GREEN + "Liste de cartes",
            null);
    private static final ItemStack BATTLE = BukkitUtils.createItemStack(Material.GOLD_SWORD,
            ChatColor.GREEN + "Lancer un duel",
            Arrays.asList(ChatColor.GOLD + "Vous permet de lancer un duel contre", ChatColor.GOLD + "un autre joueur"));
    private static final ItemStack STATISTICS = BukkitUtils.createItemStack(Material.BOOK, ChatColor.GREEN + "Statistiques", null);

    @Override
    public Inventory get(TGAPlayer p) {
        if(this.isSaved(p)) {
            return this.getSavedInventory(p);
        }

        Inventory inventory = Bukkit.createInventory(null, 9, ChatColor.BLACK + "Gestion");

        inventory.setItem(0, BATTLE);
        inventory.setItem(4, MiscUtils.personalInfo(p, 1));
        inventory.setItem(7, STATISTICS);
        inventory.setItem(8, ACCESS_CARD_LIST);

        return saveInventory(p, inventory);
    }

    @Override
    public InventoryType getType() {
        return InventoryType.MANAGEMENT;
    }

    @Override
    public void handleClicking(TGAPlayer p) {
        ItemStack clicked = NextUtils.getNextClickedItemOf(p.getBukkitPlayer(), true);

        if(clicked == null) return;

        if(BukkitUtils.areSimilar(ACCESS_CARD_LIST, clicked)) {
            p.getBukkitPlayer().closeInventory();
            InventoriesManager.handleAsync(p, InventoryType.CARD_LIST);
            return;
        }

        if (clicked.equals(STATISTICS)) {
            p.getBukkitPlayer().closeInventory();
            InventoriesManager.handleAsync(p, InventoryType.STATISTICS);
            return;
        }

        if(BukkitUtils.areSimilar(BATTLE, clicked)) {
            p.initPotentialBattleProcess();
        }

        if(BukkitUtils.areSimilar(MiscUtils.basicHeadItem(), clicked)) {
            p.getBukkitPlayer().closeInventory();
            InventoriesManager.handleAsync(p,InventoryType.MANAGEMENT);
        }
    }

}
