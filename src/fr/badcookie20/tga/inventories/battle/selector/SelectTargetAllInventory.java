package fr.badcookie20.tga.inventories.battle.selector;

import fr.badcookie20.tga.inventories.manager.InventoryType;
import fr.badcookie20.tga.player.BattleField;
import fr.badcookie20.tga.player.TGAPlayer;
import org.bukkit.inventory.Inventory;

public class SelectTargetAllInventory extends TargetSelectorInventory {

    @Override
    public Inventory get(TGAPlayer p) {
        /*TGAPlayer enemy = p.getBattleField().getEnemy();
        List<? extends Card> cards = enemy.getBattleField().getCards(BattleField.Location.BATTLEFIELD);

        Inventory inv = BukkitUtils.createInventory(cards, 1, ChatColor.BLACK + "Choisissez une cible");

        for(Card c : cards) {
            inv.addItem(c.get());
        }

        inv.setItem(inv.getSize() - 1, YesCancelInventory.CANCEL);*/

        return getDefault(BattleField.Location.BATTLEFIELD, null, p);
    }

    @Override
    public InventoryType getType() {
        return InventoryType.SELECT_TARGET_ALL;
    }

    /*@Override
    public void handleClicking(TGAPlayer p) {
        ItemStack clicked = NextUtils.forceNextItem(p, InventoryType.SELECT_TARGET_ALL);

        NextUtils.updateClickedItemOf(p.getBukkitPlayer(), clicked);
    }*/
}
