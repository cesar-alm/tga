package fr.badcookie20.tga.inventories.battle.personalizer;

import fr.badcookie20.tga.inventories.manager.InventoryType;
import fr.badcookie20.tga.player.PersonalizingAttribute;
import fr.badcookie20.tga.player.TGAPlayer;

public class MaxTurnsPersonalizerInventory extends PersonalizingPerspectiveInventory {

    public MaxTurnsPersonalizerInventory() {
        super(PersonalizingAttribute.MAX_TURNS, TGAPlayer.DEFAULT_MAX_TURNS);
    }

    @Override
    public InventoryType getType() {
        return InventoryType.BATTLE_PERSONALIZER_MAX_TURNS;
    }
}
