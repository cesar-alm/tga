package fr.badcookie20.tga.cards.mana;

import fr.badcookie20.tga.cards.Card;
import fr.badcookie20.tga.utils.Logger;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public enum ManaList {

    MANA_0(new ManaCard(3000,
            new ManaAmount(new ManaNode(ManaType.UNDEFINED, 500)))),
    MANA_1(new ManaCard(3001,
            new ManaAmount(new ManaNode(ManaType.UNDEFINED, 1000)))),
    MANA_2(new ManaCard(3002,
            new ManaAmount(new ManaNode(ManaType.UNDEFINED, 1500)))),
    MANA_3(new ManaCard(3003,
            new ManaAmount(new ManaNode(ManaType.UNDEFINED, 2000)))),
    MANA_4(new ManaCard(3004,
            new ManaAmount(new ManaNode(ManaType.UNDEFINED, 2500)))),
    MANA_5(new ManaCard(3005,
            new ManaAmount(new ManaNode(ManaType.UNDEFINED, 3000)))),

    MANA_6(new ManaCard(3006,
            new ManaAmount(new ManaNode(ManaType.LIGHT, 500)))),
    MANA_7(new ManaCard(3007,
            new ManaAmount(new ManaNode(ManaType.LIGHT, 750)))),
    MANA_8(new ManaCard(3008,
            new ManaAmount(new ManaNode(ManaType.LIGHT, 1000)))),
    MANA_9(new ManaCard(3009,
            new ManaAmount(new ManaNode(ManaType.LIGHT, 1500)))),
    MANA_10(new ManaCard(3010,
            new ManaAmount(new ManaNode(ManaType.LIGHT, 2000)))),
    MANA_11(new ManaCard(3011,
            new ManaAmount(new ManaNode(ManaType.UNDEFINED, 1000),
                    new ManaNode(ManaType.LIGHT, 500)))),
    MANA_12(new ManaCard(3012,
            new ManaAmount(new ManaNode(ManaType.UNDEFINED, 1500),
    new ManaNode(ManaType.LIGHT, 750)))),

    MANA_13(new ManaCard(3013,
            new ManaAmount(new ManaNode(ManaType.DARK, 500)))),
    MANA_14(new ManaCard(3014,
            new ManaAmount(new ManaNode(ManaType.DARK, 750)))),
    MANA_15(new ManaCard(3015,
            new ManaAmount(new ManaNode(ManaType.DARK, 1000)))),
    MANA_16(new ManaCard(3016,
            new ManaAmount(new ManaNode(ManaType.DARK, 1500)))),
    MANA_17(new ManaCard(3017,
            new ManaAmount(new ManaNode(ManaType.DARK, 2000)))),
    MANA_18(new ManaCard(3018,
            new ManaAmount(new ManaNode(ManaType.UNDEFINED, 1000),
                    new ManaNode(ManaType.DARK, 500)))),
    MANA_19(new ManaCard(3019,
            new ManaAmount(new ManaNode(ManaType.UNDEFINED, 1500),
                    new ManaNode(ManaType.DARK, 750)))),

    MANA_20(new ManaCard(3020,
            new ManaAmount(new ManaNode(ManaType.WATER, 500)))),
    MANA_21(new ManaCard(3021,
            new ManaAmount(new ManaNode(ManaType.WATER, 750)))),
    MANA_22(new ManaCard(3022,
            new ManaAmount(new ManaNode(ManaType.WATER, 1000)))),
    MANA_23(new ManaCard(3023,
            new ManaAmount(new ManaNode(ManaType.WATER, 1500)))),
    MANA_24(new ManaCard(3024,
            new ManaAmount(new ManaNode(ManaType.WATER, 2000)))),
    MANA_25(new ManaCard(3025,
            new ManaAmount(new ManaNode(ManaType.UNDEFINED, 1000),
                    new ManaNode(ManaType.WATER, 500)))),
    MANA_26(new ManaCard(3026,
            new ManaAmount(new ManaNode(ManaType.UNDEFINED, 1500),
                    new ManaNode(ManaType.WATER, 750)))),

    MANA_27(new ManaCard(3027,
            new ManaAmount(new ManaNode(ManaType.WIND, 500)))),
    MANA_28(new ManaCard(3028,
            new ManaAmount(new ManaNode(ManaType.WIND, 750)))),
    MANA_29(new ManaCard(3029,
            new ManaAmount(new ManaNode(ManaType.WIND, 1000)))),
    MANA_30(new ManaCard(3030,
            new ManaAmount(new ManaNode(ManaType.WIND, 1500)))),
    MANA_31(new ManaCard(3031,
            new ManaAmount(new ManaNode(ManaType.WIND, 2000)))),
    MANA_32(new ManaCard(3032,
            new ManaAmount(new ManaNode(ManaType.UNDEFINED, 1000),
                    new ManaNode(ManaType.WIND, 500)))),
    MANA_33(new ManaCard(3033,
            new ManaAmount(new ManaNode(ManaType.UNDEFINED, 1500),
                    new ManaNode(ManaType.WIND, 750))));

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
