package fr.badcookie20.tga.cards;

import fr.badcookie20.tga.effect.Effect;
import fr.badcookie20.tga.utils.CardUtils;
import org.bukkit.inventory.ItemStack;

public class EnchantmentCard extends EffectCard {

	public EnchantmentCard(int id, String name, Rarity rarity, int manaCost, Effect... effects) {
		super(id, name, Type.ENCHANTMENT, rarity, manaCost, effects);
	}

	@Override
	public ItemStack get() {
		return CardUtils.getItem(name, Type.ENCHANTMENT, manaCost, null, effects, -1, -1, null, false);
	}

}
