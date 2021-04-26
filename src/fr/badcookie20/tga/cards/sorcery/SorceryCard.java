package fr.badcookie20.tga.cards.sorcery;

import fr.badcookie20.tga.cards.EffectCard;
import fr.badcookie20.tga.cards.Entity;
import fr.badcookie20.tga.effect.Effect;
import fr.badcookie20.tga.utils.CardUtils2;
import org.bukkit.inventory.ItemStack;

public class SorceryCard extends EffectCard {

    public SorceryCard(Entity<SorceryCard> entity, int id, String name, Rarity rarity, int manaCost, Effect... effects) {
        super(entity, id, name, Type.SORCERY, rarity, manaCost, effects);
    }

    @Override
    public ItemStack createItemStack() {
        return CardUtils2.createItemStack(name, Type.SORCERY, manaCost, null, effects, -1, -1, null, false, this.uid);
    }
}
