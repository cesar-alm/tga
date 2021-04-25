package fr.badcookie20.tga.cards.creatures;

import fr.badcookie20.tga.cards.Card;
import fr.badcookie20.tga.cards.CardList;
import fr.badcookie20.tga.effect.Effect;
import fr.badcookie20.tga.effect.Statement;
import fr.badcookie20.tga.effect.Statement.StatementList;
import fr.badcookie20.tga.player.BattleField;
import fr.badcookie20.tga.utils.Prefixes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum CreaturesList implements CardList<CreatureCard> {

    MESSENGER_0(2,
            "Nairuu, Messager des Dieux",
            Card.Rarity.RARE,
            2500,
            CreatureType.ANGEL,
            2500,
            100,
            null,
            new Effect(Effect.ExecutionTime.DURING_CARDLIFE, "Toutes les créatures " + CreatureType.ANGEL + Prefixes.EFFECT_DESCRIPTION + " que vous possédez gagnent " + Prefixes.ATK_DEF_MODIF + "+500/+500",
                    0,
                    new Statement(StatementList.CHANGE_ATK_DEF, 500, 500, CreatureType.ANGEL, 1),
                    new Statement(StatementList.CHANGE_ATK_DEF, -500, -500, CreatureType.ANGEL, 1))),
    IMMORTAL_1(1,
            "L'immortel renforcé",
            Card.Rarity.RARE,
            2000,
            CreatureType.ANGEL,
            2400,
            0,
            Arrays.asList(CreatureProperty.INDESTRUCTIBLE, CreatureProperty.BACK_TO_HAND_3),
            new Effect(Effect.ExecutionTime.DURING_CARDLIFE, "Toutes les créatures " + CreatureType.ANGEL + Prefixes.EFFECT_DESCRIPTION + " gagnent " + Prefixes.ATK_DEF_MODIF + "+750/0" + Prefixes.EFFECT_DESCRIPTION + ", " + CreatureProperty.INDESTRUCTIBLE + Prefixes.EFFECT_DESCRIPTION + " et " + CreatureProperty.BACK_TO_HAND_1,
                    0,
                    new Statement(StatementList.CHANGE_ATK_DEF, 750, 0, CreatureType.ANGEL, 0),
                    new Statement(StatementList.CHANGE_ATK_DEF, -750, 0, CreatureType.ANGEL, 0))),
    IMMORTAL_0(0,
            "L'immortel naissant",
            Card.Rarity.RARE,
            500,
            CreatureType.ANGEL,
            1200,
            0,
            Collections.singletonList(CreatureProperty.INDESTRUCTIBLE),
            new Effect(Effect.ExecutionTime.DURING_CARDLIFE, "Toutes les créatures " + CreatureType.ANGEL + Prefixes.EFFECT_DESCRIPTION + " gagnent " + Prefixes.ATK_DEF_MODIF + " +500/-200 " + Prefixes.EFFECT_DESCRIPTION + " et " + CreatureProperty.INDESTRUCTIBLE,
                    0,
                    new Statement(StatementList.CHANGE_ATK_DEF, 500, -200, CreatureType.ANGEL, 0),
                    new Statement(StatementList.CHANGE_ATK_DEF, 500, -200, CreatureType.ANGEL, 0)),
            new Effect(Effect.ExecutionTime.ABILITY, "Invoquez " + Prefixes.CARD_NAME + IMMORTAL_1.getCard().getName() + Prefixes.EFFECT_DESCRIPTION + " depuis votre main sans payer de coût supplémentaires. Sacrifiez cette carte",
                    500,
                    new Statement(StatementList.INVOKE, false, IMMORTAL_1.getCard(), BattleField.Location.HAND, 0, true),
                    null)),
    SAMPLE(999,
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
                    null));

    private final int id;
    private final String name;
    private final Card.Rarity rarity;
    private final int manaCost;
    private final CreatureType creatureType;
    private final int atk;
    private final int def;
    private final List<CreatureProperty> properties;
    private final Effect[] effects;


    CreaturesList(int id, String name, Card.Rarity rarity, int manaCost, CreatureType creatureType, int atk, int def, List<CreatureProperty> properties, Effect... effects) {
        this.id = id;
        this.name = name;
        this.rarity = rarity;
        this.manaCost = manaCost;
        this.creatureType = creatureType;
        this.atk = atk;
        this.def = def;
        this.properties = properties;
        this.effects = effects;
    }

    public static List<CreatureCard> getCards() {
        List<CreatureCard> cards = new ArrayList<>();

        for (CreaturesList c : values()) {
            cards.add(c.getCard());
        }

        return cards;
    }

    @Override
    public CreatureCard getCard() {
        return new CreatureCard(id, name, rarity, manaCost, creatureType, atk, def, properties, effects);
    }

}
