package fr.badcookie20.tga.inventories.battle.personalizer;

import fr.badcookie20.tga.inventories.manager.InventoryType;
import fr.badcookie20.tga.player.PersonalizingAttribute;

public class StatisticsPersonalizerInventory extends PersonalizingPerspectiveInventory {

    public StatisticsPersonalizerInventory() {
        super(PersonalizingAttribute.STATISTICS_INCREASE, "Oui");
    }

    @Override
    public InventoryType getType() {
        return InventoryType.BATTLE_PERSONALIZER_STATISTICS;
    }
}
