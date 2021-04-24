package fr.badcookie20.tga.cards.mana;

import fr.badcookie20.tga.cards.Card;
import fr.badcookie20.tga.utils.Logger;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public enum ManaList {

    MANA_0(new ManaCard(3000,
            500)),
    MANA_1(new ManaCard(3001,
            1000)),
    MANA_2(new ManaCard(3002,
            1500)),
    MANA_3(new ManaCard(3003,
            2000)),
    MANA_4(new ManaCard(3004,
            2500)),
    MANA_5(new ManaCard(3005,
            3000));

    private ManaCard card;
	
	ManaList(ManaCard card) {
		this.card = card;
	}
	
	public ManaCard getCard() {
        if(this.card == null) Logger.logError("card is null");
		return this.card;
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
