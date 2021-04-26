package fr.badcookie20.tga.cards;


/**
 * Superclass of all castable cards (ie which need mana)
 */
public abstract class CastCard extends Card {

	protected int manaCost;
	
	public CastCard(Entity<? extends CastCard> entity, int id, String name, Type type, Rarity rarity, int manaCost) {
		super(entity, id, name, type, rarity);
		
		this.manaCost = manaCost;
	}

	public int getManaCost() {
        return manaCost;
    }

}
