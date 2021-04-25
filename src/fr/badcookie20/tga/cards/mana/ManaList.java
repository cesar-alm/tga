package fr.badcookie20.tga.cards.mana;

import fr.badcookie20.tga.cards.Card;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public enum ManaList {

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

    ManaList(int id, int amount) {
        this.id = id;
        this.amount = amount;
    }

    public ManaCard getCard() {
        return new ManaCard(id, amount);
	}

    public static List<ManaCard> getCards() {
        List<ManaCard> cards = new ArrayList<>();

        for(ManaList c : values()) {
            cards.add(c.getCard());
        }

        return cards;
    }

    public static Card getCard(ItemStack clicked) {
        for(Card c : getCards()) {
            String clickedLore = clicked.getItemMeta().getLore().get(0);
            String cardLore = c.get().getItemMeta().getLore().get(0);

            if(clickedLore.equals(cardLore)) return c;
        }

        return null;
    }
}
