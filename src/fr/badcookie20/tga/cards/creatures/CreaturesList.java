package fr.badcookie20.tga.cards.creatures;

import fr.badcookie20.tga.cards.Card;
import fr.badcookie20.tga.effect.Effect;
import fr.badcookie20.tga.effect.Statement;
import fr.badcookie20.tga.effect.Statement.StatementList;
import fr.badcookie20.tga.player.BattleField;
import fr.badcookie20.tga.utils.Logger;
import fr.badcookie20.tga.utils.Prefixes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum CreaturesList {
	MESSENGER_0(new CreatureCard(2,
            "Nairuu, Messager des Dieux",
            Card.Rarity.RARE,
            2500,
            CreatureType.ANGEL,
            2500,
            100,
            null,
            new Effect(Effect.ExecutionTime.DURING_CARDLIFE, "Toutes les créatures " + CreatureType.ANGEL + Prefixes.EFFECT_DESCRIPTION + " que vous possédez gagnent " + Prefixes.ATK_DEF_MODIF + "+500/+500",
                    0,
                    new Statement(StatementList.CHANGE_ATK_DEF, 500, 500, CreatureType.ANGEL),
                    null))),
	IMMORTAL_1(new CreatureCard(1,
            "L'immortel renforcé",
            Card.Rarity.RARE,
            2000,
            CreatureType.ANGEL,
            2400,
            0,
            Arrays.asList(CreatureProperty.INDESTRUCTIBLE, CreatureProperty.BACK_TO_HAND_3),
            new Effect(Effect.ExecutionTime.DURING_CARDLIFE, "Toutes les créatures " + CreatureType.ANGEL + Prefixes.EFFECT_DESCRIPTION + " gagnent " + Prefixes.ATK_DEF_MODIF + "+750/0" + Prefixes.EFFECT_DESCRIPTION + ", " + CreatureProperty.INDESTRUCTIBLE + Prefixes.EFFECT_DESCRIPTION + " et " + CreatureProperty.BACK_TO_HAND_1,
                    0,
                    new Statement(StatementList.CHANGE_ATK_DEF, 750, 0, CreatureType.ANGEL),
                    null))),
	IMMORTAL_0(new CreatureCard(0,
            "L'immortel naissant",
			Card.Rarity.RARE,
			500,
			CreatureType.ANGEL,
			1200,
			0,
            Collections.singletonList(CreatureProperty.INDESTRUCTIBLE),
			new Effect(Effect.ExecutionTime.DURING_CARDLIFE, "Toutes les créatures " + CreatureType.ANGEL + Prefixes.EFFECT_DESCRIPTION + " gagnent " + Prefixes.ATK_DEF_MODIF + " +500/-200 " + Prefixes.EFFECT_DESCRIPTION + " et " + CreatureProperty.INDESTRUCTIBLE,
					0,
					new Statement(StatementList.CHANGE_ATK_DEF, 500, -200, CreatureType.ANGEL),
					null),
            new Effect(Effect.ExecutionTime.ABILITY, "Invoquez " + Prefixes.CARD_NAME + IMMORTAL_1.getCard().getName() + Prefixes.EFFECT_DESCRIPTION + " depuis votre main sans payer de coût supplémentaires. Sacrifiez cette carte",
                    500,
                    new Statement(StatementList.INVOKE, false, IMMORTAL_1.getCard(), BattleField.Location.HAND, 0, true),
                    null))),
	SAMPLE(new CreatureCard(999,
			"sample",
			Card.Rarity.ULTRA_RARE,
			500,
			CreatureType.ANGEL,
			500,
			500,
			null,
			new Effect(Effect.ExecutionTime.ABILITY, "+1000",
					0,
					new Statement(StatementList.INCREASE_LIFE_POINTS, 1000),
					null)));

	private CreatureCard card;
	
	CreaturesList(CreatureCard card) {
		this.card = card;
	}
	
	public CreatureCard getCard() {
        if(this.card == null) Logger.logError("card is null");
		return this.card;
	}

    public static List<CreatureCard> getCards() {
        List<CreatureCard> cards = new ArrayList<>();

        for(CreaturesList c : values()) {
            cards.add(c.getCard());
        }

        return cards;
    }

}
