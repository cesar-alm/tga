package fr.badcookie20.tga.cards;

import fr.badcookie20.tga.cards.mana.ManaAmount;

/**
 * Superclass of all castable cards (ie which need mana)
 */
public abstract class CastCard extends Card {

	protected ManaAmount manaCost;
	
	public CastCard(int id, String name, Type type, Rarity rarity, ManaAmount manaCost) {
		super(id, name, type, rarity);
		
		this.manaCost = manaCost;
	}

	public ManaAmount getManaCost() {
        return manaCost;
    }

}
