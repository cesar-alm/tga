package fr.badcookie20.tga.cards.creatures;

import fr.badcookie20.tga.utils.Prefixes;

public enum CreatureProperty {

    INDESTRUCTIBLE("Indestructible"),
    BACK_TO_HAND_1("Retour au propriétaire 1"),
    BACK_TO_HAND_2("Retour au propriétaire 2"),
    BACK_TO_HAND_3("Retour au propriétaire 3"),
    TOTAL_GLASS_1("Miroir total 1"),
    TOTAL_GLASS_2("Miroir total 2"),
    UNCONTROLABLE("Incontrôlable"),
    FLYING("Vole"),
    STATIC("Statique"),
    OVERPOWERED("Surpassement"),
    VISIONNARY("Visionnaire"),
    IMMOBILISATION("Immobilisation");

    private final String displayName;

    CreatureProperty(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return Prefixes.CREATURE_PROPERTY + this.displayName + Prefixes.EFFECT_DESCRIPTION;
    }

    @Override
    public String toString() {
        return getDisplayName();
    }

}

