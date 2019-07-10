package fr.badcookie20.tga.cards.enchantment;

import fr.badcookie20.tga.cards.Card.Rarity;
import fr.badcookie20.tga.cards.EnchantmentCard;
import fr.badcookie20.tga.cards.mana.ManaAmount;
import fr.badcookie20.tga.cards.mana.ManaNode;
import fr.badcookie20.tga.cards.mana.ManaType;
import fr.badcookie20.tga.effect.Effect;
import fr.badcookie20.tga.effect.Statement;
import fr.badcookie20.tga.effect.Statement.StatementList;

import java.util.ArrayList;
import java.util.List;

public enum EnchantmentList {

	PRECIOUS_LIFE(new EnchantmentCard(1000,
			"Vie précieuse",
			Rarity.RARE,
			new ManaAmount(new ManaNode(ManaType.UNDEFINED, 2000)),
			new Effect(Effect.ExecutionTime.DURING_CARDLIFE,
					"A chaque fois que vous infligez des dommages à un adversaire, gagnez en le montant",
					null,
					new Statement(StatementList.GAIN_DAMAGED),
                    new Statement(StatementList.UNGAIN_DAMAGED))));
	
	private EnchantmentCard card;
	
	EnchantmentList(EnchantmentCard card) {
		this.card = card;
	}
	
	public EnchantmentCard getCard() {
		return this.card;
	}

	public static List<EnchantmentCard> getCards() {
		List<EnchantmentCard> cards = new ArrayList<>();

		for(EnchantmentList c : values()) {
			cards.add(c.getCard());
		}

		return cards;
	}
	
}
