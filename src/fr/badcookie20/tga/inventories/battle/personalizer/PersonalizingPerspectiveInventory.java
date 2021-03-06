package fr.badcookie20.tga.inventories.battle.personalizer;

import fr.badcookie20.tga.inventories.manager.InventoriesManager;
import fr.badcookie20.tga.inventories.manager.InventoryType;
import fr.badcookie20.tga.inventories.manager.SaveableInventory;
import fr.badcookie20.tga.player.PersonalizingAttribute;
import fr.badcookie20.tga.player.PotentialBattle;
import fr.badcookie20.tga.player.TGAPlayer;
import fr.badcookie20.tga.utils.BukkitUtils;
import fr.badcookie20.tga.utils.NextUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public abstract class PersonalizingPerspectiveInventory extends SaveableInventory {

    protected static final String MIN_MAX = ChatColor.GRAY + "" + ChatColor.ITALIC;
    protected static final Material LESS = Material.REDSTONE_BLOCK;
    protected static final Material MORE = Material.SLIME_BLOCK;

    private PersonalizingAttribute personalizingAttribute;
    private Object defaultValue;

    public PersonalizingPerspectiveInventory(PersonalizingAttribute personalizingAttribute, Object defaultValue) {
        this.personalizingAttribute = personalizingAttribute;
        this.defaultValue = defaultValue;
    }

    @Override
    public Inventory get(TGAPlayer p) {
        Inventory inv = Bukkit.createInventory(null, 9, ChatColor.BLACK + this.personalizingAttribute.getRawValueName());

        inv.setItem(2, getLess(this.personalizingAttribute.getModifyRange(), p.getPotentialBattle().get(this.personalizingAttribute)));
        inv.setItem(4, getActual(p, this.personalizingAttribute, defaultValue));
        inv.setItem(6, getMore(this.personalizingAttribute.getModifyRange(), p.getPotentialBattle().get(this.personalizingAttribute)));

        inv.setItem(inv.getSize() -1, BattlePersonalizerInventory.VALIDATE);

        return inv;
    }

    @Override
    public void handleClicking(TGAPlayer p) {
        ItemStack clicked = NextUtils.forceNextItem(p, getType());

        if(clicked.getType() == LESS) {
            decrement(p, this.personalizingAttribute);
        }else if(clicked.getType() == MORE) {
            increment(p, this.personalizingAttribute);
        }else if(clicked.equals(BattlePersonalizerInventory.VALIDATE)) {
            InventoriesManager.getInstance().handle(p, InventoryType.BATTLE_PERSONALIZER);
            return;
        }

        InventoriesManager.getInstance().handle(p, this.getType());
    }

    /**
     * Returns the ItemStack displaying the actual value of the personalized attribute
     * @param p the player which is personalizing the battle
     * @param personalizingAttribute the attribute that is being personalized
     * @param defaultValue the default value of this attribute
     * @return the associated ItemStack
     */
    public static ItemStack getActual(TGAPlayer p, PersonalizingAttribute personalizingAttribute, Object defaultValue) {
        return BukkitUtils.createItemStack(personalizingAttribute.getMaterial(), ChatColor.GREEN + personalizingAttribute.getValueName(), Arrays.asList(
                ChatColor.GOLD + "Valeur actuelle : " + ChatColor.AQUA + BukkitUtils.booleanFilterer(p.getPotentialBattle().get(personalizingAttribute)),
                ChatColor.GOLD + "Valeur par d??faut : " + ChatColor.AQUA + defaultValue,
                (personalizingAttribute.getModifyRange() != -1 ? MIN_MAX + "Min : " + ChatColor.AQUA + personalizingAttribute.getMin() + MIN_MAX + " / Max : " + ChatColor.AQUA + personalizingAttribute.getMax() : null)));
    }


    public static ItemStack getLess(int modificationRange, Object actualValue) {
        String name = "" + (modificationRange == -1 ? (ChatColor.AQUA + "Non") : ChatColor.RED + "-"  + modificationRange);
        return BukkitUtils.createItemStack(LESS, name, null);
    }

    public static ItemStack getMore(int modificationRange, Object actualValue) {
        String name = "" + (modificationRange == -1 ? (ChatColor.AQUA + "Oui") : ChatColor.GREEN + "+"  + modificationRange);
        return BukkitUtils.createItemStack(MORE, name, null);
    }

    /**
     * Decrements the value for the personalized battle
     * @param p the player personalizing it
     * @param personalizingAttribute the attribute being personalized
     */
    public static void decrement(TGAPlayer p, PersonalizingAttribute personalizingAttribute) {
        PotentialBattle b = p.getPotentialBattle();

        if(personalizingAttribute.getModifyRange() == -1) {
            b.set(personalizingAttribute, false);
        }else{
            Integer newValue = (Integer) b.get(personalizingAttribute) - personalizingAttribute.getModifyRange();
            if(newValue < 0) {
                newValue = 0;
            }

            if(newValue < personalizingAttribute.getMin() || newValue > personalizingAttribute.getMax()) {
                newValue = (Integer) b.get(personalizingAttribute);
            }

            b.set(personalizingAttribute, newValue);
        }

        p.setPotentialBattle(b);
    }

    /**
     * Increments the value for the personalized battle
     * @param p the player personalizing it
     * @param personalizingAttribute the attribute being personalized
     */
    public static void increment(TGAPlayer p, PersonalizingAttribute personalizingAttribute) {
        PotentialBattle b = p.getPotentialBattle();

        if (personalizingAttribute.getModifyRange() == -1) {
            b.set(personalizingAttribute, true);
        } else {
            int newValue = (Integer) b.get(personalizingAttribute) + personalizingAttribute.getModifyRange();

            if(newValue < personalizingAttribute.getMin() || newValue > personalizingAttribute.getMax()) {
                newValue = (Integer) b.get(personalizingAttribute);
            }

            b.set(personalizingAttribute, newValue);
        }

        p.setPotentialBattle(b);
    }

}
