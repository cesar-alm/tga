package fr.badcookie20.tga.cards.mana;

import fr.badcookie20.tga.cards.Entity;

import java.util.ArrayList;
import java.util.List;

public enum Mana implements Entity<ManaCard> {

    MANA_0(3000,
            500),
    MANA_1(3001,
            1000),
    MANA_2(3002,
            1500),
    MANA_3(3003,
            2000),
    MANA_4(3004,
            2500),
    MANA_5(3005,
            3000);

    private int id;
    private int amount;

    Mana(int id, int amount) {
        this.id = id;
        this.amount = amount;
    }

    @Override
    public ManaCard generateNewCard() {
        return new ManaCard(this, id, amount);
	}

    public static List<ManaCard> generateAllNewCards() {
        List<ManaCard> cards = new ArrayList<>();

        for(Mana c : values()) {
            cards.add(c.generateNewCard());
        }

        return cards;
    }

    @Override
    public int getID() {
        return this.id;
    }

    @Override
    public String getName() {
        return "Mana";
    }


}
