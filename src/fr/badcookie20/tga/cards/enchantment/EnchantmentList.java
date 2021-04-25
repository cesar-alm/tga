package fr.badcookie20.tga.cards.enchantment;

import fr.badcookie20.tga.cards.Card.Rarity;
import fr.badcookie20.tga.cards.CardList;
import fr.badcookie20.tga.cards.EnchantmentCard;
import fr.badcookie20.tga.effect.Effect;
import fr.badcookie20.tga.effect.Statement;
import fr.badcookie20.tga.effect.Statement.StatementList;

import java.util.ArrayList;
import java.util.List;

public enum EnchantmentList implements CardList<EnchantmentCard> {

	PRECIOUS_LIFE1000(1000,
			"Vie précieuse",
			Rarity.RARE,
			2000,
			new Effect(Effect.ExecutionTime.DURING_CARDLIFE,
					"A chaque fois que vous infligez des dommages à un adversaire, gagnez en le montant",
					0,
					new Statement(StatementList.GAIN_DAMAGED),
                    new Statement(StatementList.UNGAIN_DAMAGED))),
	WEAK_STRENGTH(1001,
			"Faiblesse & Force",
			Rarity.RARE,
			1500,
			new Effect(Effect.ExecutionTime.DURING_CARDLIFE,
					"Toutes vos créatures gagnent +500/+500.",
					0,
					new Statement(StatementList.CHANGE_ATK_DEF, 500, 500, null, 1),
					new Statement(StatementList.CHANGE_ATK_DEF, -500, -500, null, 1)),
			new Effect(Effect.ExecutionTime.DURING_CARDLIFE,
					"Toutes les créatures adverses gagnent -500/-500",
					0,
					new Statement(StatementList.CHANGE_ATK_DEF, -500, -500, null, 2),
					new Statement(StatementList.CHANGE_ATK_DEF, 500, 500, null, 2)));
	
	private int id;
	private String name;
	private Rarity rarity;
	private int manaCost;
	private Effect[] effects;

	EnchantmentList(int id, String name, Rarity rarity, int manaCost, Effect... effects) {
		this.id = id;
		this.name = name;
		this.rarity = rarity;
		this.manaCost = manaCost;
		this.effects = effects;
	}

	@Override
	public EnchantmentCard getCard() {
		return new EnchantmentCard(id, name, rarity, manaCost, effects);
	}

	public static List<EnchantmentCard> getCards() {
		List<EnchantmentCard> cards = new ArrayList<>();

		for(EnchantmentList c : values()) {
			cards.add(c.getCard());
		}

		return cards;
	}
	
}
