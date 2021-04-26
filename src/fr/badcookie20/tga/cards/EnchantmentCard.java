package fr.badcookie20.tga.cards;

import fr.badcookie20.tga.effect.Effect;
import fr.badcookie20.tga.utils.CardUtils2;
import org.bukkit.inventory.ItemStack;

public class EnchantmentCard extends EffectCard {

	public EnchantmentCard(Entity<EnchantmentCard> entity, int id, String name, Rarity rarity, int manaCost, Effect... effects) {
		super(entity, id, name, Type.ENCHANTMENT, rarity, manaCost, effects);
	}

	@Override
	public ItemStack createItemStack() {
		return CardUtils2.createItemStack(name, Type.ENCHANTMENT, manaCost, null, effects, -1, -1, null, false, this.uid);
	}

}
