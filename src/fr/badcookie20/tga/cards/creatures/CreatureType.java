package fr.badcookie20.tga.cards.creatures;

import fr.badcookie20.tga.utils.BukkitUtils;
import fr.badcookie20.tga.utils.Prefixes;

public enum CreatureType {
	
	ANGEL(0, "Ange"),
	MARINE(1, "Créature Marine"),
	DEMON(2, "Démon"),
	FLYING(3, "Créature Volante");

	private int id;
	private String displayName;
	
	CreatureType(int id, String displayName) {
        this.id = id;
		this.displayName = displayName;
	}
	
	public String getDisplayName() {
		return Prefixes.CREATURE_PROPERTY + this.displayName;
	}

    public String getRawDisplayName() {
        return this.displayName;
    }

    public int getID() {
        return this.id;
    }
	
	@Override
	public String toString() {
		return this.getDisplayName();
	}

    public static CreatureType get(String s) {
        for(CreatureType type : values()) {
            if(type.displayName.equals(s)) return type;
            if(type.displayName.equals(BukkitUtils.removeFirstColor(s))) return type;
        }

        return null;
    }

    public static CreatureType get(int id) {
        for(CreatureType type : values()) {
            if(type.id == id) return type;
        }

        return null;
    }

}
