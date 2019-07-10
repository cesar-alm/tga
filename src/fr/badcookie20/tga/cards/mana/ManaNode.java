package fr.badcookie20.tga.cards.mana;

import fr.badcookie20.tga.utils.Prefixes;

public class ManaNode {

	private ManaType type;
	private int amount;

	public ManaNode(ManaType type, int amount) {
		this.type = type;
		this.amount = amount;
	}
	
	public ManaType getType() {
		return this.type;
	}
	
	public int getAmount() {
		return this.amount;
	}

	@Override
    public String toString() {
        return Prefixes.MANA_TYPE + getAmount() + " " + getType();
    }

}
