package fr.badcookie20.tga.cards.mana;

import fr.badcookie20.tga.cards.creatures.CreatureType;
import fr.badcookie20.tga.utils.BukkitUtils;
import fr.badcookie20.tga.utils.Prefixes;
import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.List;

public enum ManaType {

    UNDEFINED(-1, "", null),
    WIND(0, "Vent", CreatureType.FLYING),
    LIGHT(1, "Lumière", CreatureType.ANGEL),
    WATER(2, "Eau", CreatureType.MARINE),
    DARK(3, "Ténèbres", CreatureType.DEMON);

    private int id;
    private String displayName;
    private CreatureType creatureType;

    ManaType(int id, String displayName, CreatureType creatureType) {
        this.id = id;
        this.displayName = displayName;
        this.creatureType = creatureType;
    }

    public String getDisplayName() {
        return Prefixes.MANA_TYPE + this.displayName;
    }

    public String getRawDisplayName() {
        return this.displayName;
    }

    public int getID() {
        return this.id;
    }

    @Override
    public String toString() {
        return getDisplayName();
    }

    public static ManaType get(String s) {
        for(ManaType type : values()) {
            if(type.displayName.equals(s)) return type;
            if(type.displayName.equals(BukkitUtils.removeFirstColor(s))) return type;
        }

        return null;
    }

    public static ManaType get(int id) {
        for(ManaType type : values()) {
            if(type.id == id) return type;
        }

        return null;
    }

    public List<String> description() {
        return Arrays.asList(ChatColor.GOLD + ">Choisissez l'affinité " + this.getDisplayName(),
                Prefixes.EFFECT_DESCRIPTION + "Le type de créatures correspondant est " + this.creatureType);
    }

    public CreatureType getCreatureType() {
        return creatureType;
    }
}
