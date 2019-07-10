package fr.badcookie20.tga.cards;

import fr.badcookie20.tga.utils.Prefixes;
import org.bukkit.inventory.ItemStack;

/**
 * Superclass of all cards. All cards should extend this class
 */
public abstract class Card {

    protected String name;
    private int id;
    private Type type;
    private Rarity rarity;

    public Card(int id, String name, Type type, Rarity rarity) {
        this.name = name;
        this.type = type;
        this.rarity = rarity;
        this.id = id;
    }

    public Type getType() {
        return this.type;
    }

    public boolean isSimilar(Card card) {
        return card != null && card.name == this.name && card.type == this.type && card.rarity == this.rarity;
    }

    public String getName() {
        return this.name;
    }

    public abstract ItemStack get();

    public int getID() {
        return id;
    }

    public enum Type {
        MANA("Mana"),
        CREATURE("Créature"),
        ENCHANTMENT("Enchantement"),
        SORCERY("Sortilège");

        private String typeName;

        Type(String name) {
            this.typeName = name;
        }

        public String getTypeName() {
            return Prefixes.CREATURE_PROPERTY + this.typeName;
        }

        @Override
        public String toString() {
            return getTypeName();
        }
    }

    public enum Rarity {
        COMMON,
        RARE,
        SUPER_RARE,
        ULTRA_RARE,
        ULTIMATE_RARE;
    }

}
