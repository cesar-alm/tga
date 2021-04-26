package fr.badcookie20.tga.cards.mana;

import fr.badcookie20.tga.cards.Card;
import fr.badcookie20.tga.cards.Entity;
import fr.badcookie20.tga.utils.CardUtils2;
import org.bukkit.inventory.ItemStack;

public class ManaCard extends Card {

	private int fixedAmount;
	private int amount;
	
	public ManaCard(Entity<ManaCard> card, int id, int amount) {
		super(card, id, "Mana", Type.MANA, Rarity.COMMON);

		this.amount = amount;
        this.fixedAmount = amount;
	}
	
	public int getAmount() {
		return this.amount;
	}

	@Override
	public ItemStack createItemStack() {
		return CardUtils2.createItemStack("Mana", Card.Type.MANA, this.amount, null, null, -1, -1, null, false, this.uid);
	}

	public void resetAmount() {
		this.amount = fixedAmount;
	}
}
