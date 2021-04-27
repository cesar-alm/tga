package fr.badcookie20.tga.inventories.battle.selector;

import fr.badcookie20.tga.cards.Card;
import fr.badcookie20.tga.inventories.battle.YesCancelInventory;
import fr.badcookie20.tga.inventories.manager.SaveableInventory;
import fr.badcookie20.tga.player.BattleField;
import fr.badcookie20.tga.player.TGAPlayer;
import fr.badcookie20.tga.utils.BukkitUtils;
import fr.badcookie20.tga.utils.CardUtils;
import fr.badcookie20.tga.utils.NextUtils;
import org.bukkit.ChatColor;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * Specific type of inventory. All the inventories that select cards in a specific part of the battlefield (the
 * battlefield itself, the graveyard, etc.) should extend this class
 */
public abstract class TargetSelectorInventory extends SaveableInventory {

    protected Inventory getDefault(BattleField.Location location, Card.Type type, TGAPlayer p) {
        TGAPlayer enemy = p.getBattleField().getEnemy();

        List<? extends Card> cards;

        if(type == null) {
            cards = enemy.getBattleField().getCards(location);
        }else{
            cards = CardUtils.sort(enemy.getBattleField().getCards(location), type);
        }

        Inventory inv = BukkitUtils.createInventory(cards, 1, ChatColor.BLACK + "Choisissez une cible");

        for(Card c : cards) {
            inv.addItem(c.createItemStack());
        }

        inv.setItem(inv.getSize() - 1, YesCancelInventory.CANCEL);

        return inv;
    }

    @Override
    public void handleClicking(TGAPlayer p) {
        ItemStack clicked = NextUtils.forceNextItem(p, this.getType());

        NextUtils.updateClickedItemOf(p.getBukkitPlayer(), clicked);
    }

}
