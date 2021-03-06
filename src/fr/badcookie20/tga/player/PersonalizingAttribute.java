package fr.badcookie20.tga.player;

import fr.badcookie20.tga.inventories.manager.InventoryType;
import fr.badcookie20.tga.utils.Logger;
import org.bukkit.ChatColor;
import org.bukkit.Material;

public enum PersonalizingAttribute {

    LIFE_POINTS(500, "Points de vie initiaux", Material.GHAST_TEAR, InventoryType.BATTLE_PERSONALIZER_LIFE_POINTS, 1, 500, 10000),
    MAX_TURNS(5, "Nombre de tours", Material.SIGN, InventoryType.BATTLE_PERSONALIZER_MAX_TURNS, 1, 5, 100),
    DAMAGE_MULTIPLIER(1, "Multiplicateur de dommages", Material.DIAMOND_AXE, InventoryType.BATTLE_PERSONALIZER_DAMAGE_MULTIPLIER, 0.1, 1, 100),
    STATISTICS_INCREASE(-1, "Entre dans les statistiques", Material.COMPASS, InventoryType.BATTLE_PERSONALIZER_STATISTICS, 1, 0, 0);

    private InventoryType inventory;
    private int modifyRange;
    private String valueName;
    private Material material;
    private double multiplier;
    private int min;
    private int max;

    /**
     * Constructor of the enum
     * @param modifyRange Value between 0 (exclusive) and the infinite if it's a number and -1 if it's a boolean
     * @param valueName the name of the personalizing value
     * @param material the Material of the personalizing object
     * @param inventory its inventory
     * @param multiplier the value multiplier (used if {@link #multiplier} != 1)
     * @param min the minimum value that this parameter can take (ignored if {@link #modifyRange} equals -1)
     * @param max the maximum value that this parameter can take (ignored if {@link #modifyRange} equals -1)
     */
    PersonalizingAttribute(int modifyRange, String valueName, Material material, InventoryType inventory, double multiplier, int min, int max) {
        this.modifyRange = modifyRange;
        this.valueName = valueName;
        this.material = material;
        this.inventory = inventory;
        this.multiplier = multiplier;
        this.min = min;
        this.max = max;
    }

    public int getModifyRange() {
        return this.modifyRange;
    }

    public String getValueName() {
        return valueName + (this.multiplier != 1 ? ChatColor.GRAY + " (x" + this.multiplier + ")" : "");
    }

    public String getRawValueName() {
        return valueName;
    }

    public Material getMaterial() {
        return this.material;
    }

    public double getMultiplier() {
        return this.multiplier;
    }

    public InventoryType getInventory() {
        return this.inventory;
    }

    public int getMax() {
        return max;
    }

    public int getMin() {
        return min;
    }

    public double getMultipliedMin() {
        return this.min * this.multiplier;
    }

    public double getMultipliedMax() {
        return this.max * this.multiplier;
    }

    public static PersonalizingAttribute getPersonalizingObject(String name) {
        for(PersonalizingAttribute personalizingAttribute : values()) {
            if(personalizingAttribute.getValueName().equals(name)) {
                return personalizingAttribute;
            }
        }

        Logger.logError("personalizing object with name " + name + " doesn't exist !");

        return null;
    }
}
