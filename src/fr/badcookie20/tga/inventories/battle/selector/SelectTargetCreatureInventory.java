package fr.badcookie20.tga.inventories.battle.selector;

import fr.badcookie20.tga.cards.Card;
import fr.badcookie20.tga.inventories.manager.InventoryType;
import fr.badcookie20.tga.player.BattleField;
import fr.badcookie20.tga.player.TGAPlayer;
import org.bukkit.inventory.Inventory;

public class SelectTargetCreatureInventory extends TargetSelectorInventory {

    @Override
    public Inventory get(TGAPlayer p) {
        /*TGAPlayer enemy = p.getBattleField().getEnemy();
        List<Card> cards = enemy.getBattleField().getCards(BattleField.Location.BATTLEFIELD);
        List<Card> creatureCards = CardUtils.sort(cards, Card.Type.CREATURE);

        Inventory inv = BukkitUtils.createInventory(creatureCards, 1, ChatColor.BLACK + "Choisissez une cible");

        for(Card c : creatureCards) {
            inv.addItem(c.get());
        }

        inv.setItem(inv.getSize() - 1, YesCancelInventory.CANCEL);*/

        return getDefault(BattleField.Location.BATTLEFIELD, Card.Type.CREATURE, p);
    }

    @Override
    public InventoryType getType() {
        return InventoryType.SELECT_TARGET_CREATURE;
    }

    /*@Override
    public void handleClicking(TGAPlayer p) {
        ItemStack clicked = NextUtils.forceNextItem(p, InventoryType.SELECT_TARGET_CREATURE);

        NextUtils.updateClickedItemOf(p.getBukkitPlayer(), clicked);
    }*/
}
