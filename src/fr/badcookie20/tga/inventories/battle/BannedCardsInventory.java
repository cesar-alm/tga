package fr.badcookie20.tga.inventories.battle;

import fr.badcookie20.tga.cards.Card;
import fr.badcookie20.tga.inventories.manager.InventoriesManager;
import fr.badcookie20.tga.inventories.manager.InventoryType;
import fr.badcookie20.tga.inventories.manager.SaveableInventory;
import fr.badcookie20.tga.player.BattleField;
import fr.badcookie20.tga.player.TGAPlayer;
import fr.badcookie20.tga.utils.BukkitUtils;
import fr.badcookie20.tga.utils.NextUtils;
import org.bukkit.ChatColor;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

import static fr.badcookie20.tga.inventories.battle.BattleFieldInventory.FORCE_STOP;
import static fr.badcookie20.tga.inventories.manager.InventoriesManager.GO_BACK;

public class BannedCardsInventory extends SaveableInventory {

    @Override
    public Inventory get(TGAPlayer p) {
        if(this.isSaved(p)) {
            return this.getSavedInventory(p);
        }

        List<Card> cards = p.getBattleField().getCards(BattleField.Location.BANNED);

        Inventory inv = BukkitUtils.createInventory(cards, 1, ChatColor.BLACK + "Cartes bannies");

        for(Card c : cards) {
            inv.addItem(c.get());
        }

        inv.setItem(inv.getSize() - 1, GO_BACK);

        return saveInventory(p, inv);
    }

    @Override
    public InventoryType getType() {
        return InventoryType.BANNED_CARDS;
    }

    @Override
    public void handleClicking(TGAPlayer p) {
        ItemStack item = NextUtils.forceNextItem(p, InventoryType.BANNED_CARDS);

        if(item.equals(GO_BACK)) {
            InventoriesManager.getInstance().handle(p, InventoryType.BATTLEFIELD);
        }else if(item.equals(FORCE_STOP)) {
            p.getBukkitPlayer().closeInventory();
        }else{
            InventoriesManager.getInstance().handle(p, InventoryType.BANNED_CARDS);
        }
    }
}
