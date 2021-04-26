package fr.badcookie20.tga.cards;

import fr.badcookie20.tga.cards.creatures.Creature;
import fr.badcookie20.tga.cards.enchantment.Enchantment;
import fr.badcookie20.tga.cards.mana.Mana;
import fr.badcookie20.tga.cards.sorcery.Sorcery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public interface Entity<T extends Card> {

    List<Entity<? extends Card>> allEntities = generateAllEntities();

    public int getID();

    public String getName();

    public T generateNewCard();

    static Entity<? extends Card> getEntity(int id) {
        for(Entity<? extends Card> entity : allEntities) {
            if(entity.getID() == id) {
                return entity;
            }
        }

        return null;
    }

    static List<Card> generateAllNewCards(List<Entity<? extends Card>> entities) {
        List<Card> cards = new ArrayList<>();

        for(Entity<? extends Card> entity : entities) {
            cards.add(entity.generateNewCard());
        }

        return cards;
    }


    /**
     * This function adds all the entities to the field allEntities.
     * Should only be used once (on initialisation); afterwards, access directly the list.
     * @return the list of entities.
     */
    static List<Entity<? extends Card>> generateAllEntities() {
        List<Entity<? extends Card>> allEntities = new ArrayList<>(Arrays.asList(Creature.values()));
        allEntities.addAll(Arrays.asList(Enchantment.values()));
        allEntities.addAll(Arrays.asList(Mana.values()));
        allEntities.addAll(Arrays.asList(Sorcery.values()));
        return allEntities;
    }

}
