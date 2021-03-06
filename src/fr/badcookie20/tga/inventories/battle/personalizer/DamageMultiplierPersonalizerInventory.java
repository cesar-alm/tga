package fr.badcookie20.tga.inventories.battle.personalizer;

import fr.badcookie20.tga.inventories.manager.InventoryType;
import fr.badcookie20.tga.player.PersonalizingAttribute;
import fr.badcookie20.tga.player.TGAPlayer;

public class DamageMultiplierPersonalizerInventory extends PersonalizingPerspectiveInventory {

    public DamageMultiplierPersonalizerInventory() {
        super(PersonalizingAttribute.DAMAGE_MULTIPLIER, TGAPlayer.DEFAULT_DAMAGE_MULTIPLIER);
    }

    @Override
    public InventoryType getType() {
        return InventoryType.BATTLE_PERSONALIZER_DAMAGE_MULTIPLIER;
    }
}
