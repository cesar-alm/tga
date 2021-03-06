package fr.badcookie20.tga.cards.creatures;

import fr.badcookie20.tga.utils.Prefixes;

public enum CreatureProperty {

    INDESTRUCTIBLE("Indestructible", "Ne peut pas être attaquée"),
    BACK_TO_HAND_1("Retour au propriétaire 1", "Revient dans la main de son propriétaire lors d'un retour au deck"),
    BACK_TO_HAND_2("Retour au propriétaire 2", "Revient dans la main de son propriétaire lors d'un retour au deck ou au cimetière"),
    BACK_TO_HAND_3("Retour au propriétaire 3", "Revient dans la main de son propriétaire dès qu'elle quitte le terrain"),
    TOTAL_GLASS_1("Miroir total 1", "Ne peut pas être la cible d'un effet direct"),
    TOTAL_GLASS_2("Miroir total 2", "Ne peut pas être la cible d'un effet"),
    UNCONTROLABLE("Incontrôlable", "Ne peut pas être contrôlée par l'adversaire"),
    FLYING("Vol", "Peut attaquer directement l'adversaire"),
    STATIC("Statique", "Son attaque et sa défense ne peuvent pas être modifiées"),
    OVERPOWERED("Surpassement", "Les points d'attaque qui ne sont pas encaissés par la défense d'un monstre sont infligés à l'adversaire"),
    VISIONNARY("Visionnaire", "??"),
    IMMOBILISATION("Immobilisation", "Ne peut pas attaquer");

    private final String displayName;
    private final String description;

    CreatureProperty(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return Prefixes.CREATURE_PROPERTY + this.displayName + Prefixes.EFFECT_DESCRIPTION;
    }

    @Override
    public String toString() {
        return getDisplayName();
    }

}

