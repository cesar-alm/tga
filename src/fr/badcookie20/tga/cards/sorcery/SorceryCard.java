package fr.badcookie20.tga.cards.sorcery;

import fr.badcookie20.tga.cards.EffectCard;
import fr.badcookie20.tga.effect.Effect;
import fr.badcookie20.tga.utils.CardUtils;
import org.bukkit.inventory.ItemStack;

public class SorceryCard extends EffectCard {

    public SorceryCard(int id, String name, Rarity rarity, int manaCost, Effect... effects) {
        super(id, name, Type.SORCERY, rarity, manaCost, effects);
    }

    @Override
    public ItemStack get() {
        return CardUtils.getItem(name, Type.SORCERY, manaCost, null, effects, -1, -1, null, false);
    }
}
