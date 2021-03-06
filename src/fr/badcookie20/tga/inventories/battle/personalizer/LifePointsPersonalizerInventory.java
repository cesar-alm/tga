package fr.badcookie20.tga.inventories.battle.personalizer;

import fr.badcookie20.tga.inventories.manager.InventoryType;
import fr.badcookie20.tga.player.PersonalizingAttribute;
import fr.badcookie20.tga.player.TGAPlayer;

public class LifePointsPersonalizerInventory extends PersonalizingPerspectiveInventory {

    public LifePointsPersonalizerInventory() {
        super(PersonalizingAttribute.LIFE_POINTS, TGAPlayer.DEFAULT_LIFE_POINTS);
    }

    @Override
    public InventoryType getType() {
        return InventoryType.BATTLE_PERSONALIZER_LIFE_POINTS;
    }
}
