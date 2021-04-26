package fr.badcookie20.tga.inventories.management;

import fr.badcookie20.tga.cards.Card;
import fr.badcookie20.tga.cards.mana.ManaCard;
import fr.badcookie20.tga.inventories.admin.ForbiddenInventory;
import fr.badcookie20.tga.inventories.manager.InventoriesManager;
import fr.badcookie20.tga.inventories.manager.InventoryType;
import fr.badcookie20.tga.inventories.manager.SaveableInventory;
import fr.badcookie20.tga.player.TGAPlayer;
import fr.badcookie20.tga.utils.BukkitUtils;
import fr.badcookie20.tga.utils.ConfigUtils;
import fr.badcookie20.tga.utils.NextUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CardListInventory extends SaveableInventory {

    @Override
    public Inventory get(TGAPlayer p) {
        if(this.isSaved(p)) {
            return this.getSavedInventory(p);
        }

        Inventory inventory = BukkitUtils.createInventory(p.getCardsList(false), 2, "Liste de Cartes");

        int manaCards = 0;

        for(Card c : p.getCardsList(false)) {
            if(c instanceof ManaCard) {
                manaCards++;
                continue;
            }

            ItemStack item = c.get();

            if(ConfigUtils.getForbiddenCards().contains(c)) {
                ItemMeta itemMeta = item.getItemMeta();

                itemMeta.setDisplayName(itemMeta.getDisplayName() + ForbiddenInventory.FORBIDDEN);
                item.setItemMeta(itemMeta);
            }

            inventory.addItem(item);
        }

        ItemStack manaItem = BukkitUtils.createItemStack(Material.SAPLING, ChatColor.GREEN + "Mana", null);
        manaItem.setAmount(manaCards);

        inventory.setItem(inventory.firstEmpty(), manaItem);
        inventory.setItem(inventory.getSize() - 1, InventoriesManager.GO_BACK);

        return saveInventory(p, inventory);
    }

    @Override
    public InventoryType getType() {
        return InventoryType.CARD_LIST;
    }

    @Override
    public void handleClicking(TGAPlayer p) {
        ItemStack item = NextUtils.forceNextItem(p, InventoryType.CARD_LIST);

        if(item.equals(InventoriesManager.GO_BACK)) {
            InventoriesManager.getInstance().handle(p, InventoryType.MANAGEMENT);
            return;
        }

        InventoriesManager.getInstance().handle(p, InventoryType.CARD_LIST);
    }
}
