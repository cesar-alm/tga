package fr.badcookie20.tga.cards;

import fr.badcookie20.tga.utils.ItemUIDUtils;
import fr.badcookie20.tga.utils.Prefixes;
import org.bukkit.inventory.ItemStack;

/**
 * Superclass of all cards. All cards should extend this class
 */
public abstract class Card {

    private final Entity<? extends Card> entity;

    protected String name;
    private final int id;
    private final Type type;
    private final Rarity rarity;
    protected final int uid;

    public Card(Entity<? extends Card> entity, int id, String name, Type type, Rarity rarity) {
        this.entity = entity;

        this.name = name;
        this.type = type;
        this.rarity = rarity;
        this.id = id;

        this.uid = ItemUIDUtils.registerCard(this);
    }

    public boolean isSimilar(Card card) {
        return card != null && card.name.equals(this.name) && card.type == this.type && card.rarity == this.rarity;
    }

    public String getName() {
        return this.name;
    }

    public int getID() {
        return id;
    }

    public Entity<? extends Card> getEntity() {
        return entity;
    }

    public Type getType() {
        return this.type;
    }

    public abstract ItemStack createItemStack();

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
