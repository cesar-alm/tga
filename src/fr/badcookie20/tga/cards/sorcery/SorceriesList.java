package fr.badcookie20.tga.cards.sorcery;

import fr.badcookie20.tga.cards.Card;
import fr.badcookie20.tga.effect.Effect;
import fr.badcookie20.tga.effect.Statement;
import fr.badcookie20.tga.utils.Prefixes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum SorceriesList {

    DAMAGE(new SorceryCard(2002,
            "Attaque féroce",
            Card.Rarity.RARE,
            2000,
            new Effect(Effect.ExecutionTime.ON_INVOKE,
                    "Infligez " + Prefixes.ATK_DEF_MODIF + "500" + Prefixes.EFFECT_DESCRIPTION + " dommages à votre adversaire",
                    0,
                    new Statement(Statement.StatementList.DAMAGE_ENEMY, 500),
                    null))),
    ENCH_DEST(new SorceryCard(2003,
            "Exterminateur",
            Card.Rarity.RARE,
            3000,
            new Effect(Effect.ExecutionTime.ON_INVOKE,
                    "Détruisez tous les enchantements de tous les terrains",
                    0,
                    new Statement(Statement.StatementList.DESTROY_CARDS, Arrays.asList(Card.Type.ENCHANTMENT)),
                    null))),
    LIFE(new SorceryCard(2001,
            "Revigorant",
            Card.Rarity.COMMON,
            1500,
            new Effect(Effect.ExecutionTime.ON_INVOKE,
                    "Gagnez " + Prefixes.ATK_DEF_MODIF + "1000" + Prefixes.EFFECT_DESCRIPTION + " points de vie",
                    0,
                    new Statement(Statement.StatementList.INCREASE_LIFE_POINTS, 1000),
                    null)));


    private SorceryCard card;

    SorceriesList(SorceryCard card) {
        this.card = card;
    }

    public SorceryCard getCard() {
        return this.card;
    }

    public static List<SorceryCard> getCards() {
        List<SorceryCard> cards = new ArrayList<>();

        for(SorceriesList c : values()) {
            cards.add(c.getCard());
        }

        return cards;
    }

}
