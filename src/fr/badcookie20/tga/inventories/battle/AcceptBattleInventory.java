package fr.badcookie20.tga.inventories.battle;

import fr.badcookie20.tga.inventories.manager.InventoriesManager;
import fr.badcookie20.tga.inventories.manager.InventoryType;
import fr.badcookie20.tga.inventories.manager.UniversalInventory;
import fr.badcookie20.tga.player.TGAPlayer;
import fr.badcookie20.tga.utils.BukkitUtils;
import fr.badcookie20.tga.utils.NextUtils;
import fr.badcookie20.tga.utils.Prefixes;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import static fr.badcookie20.tga.inventories.battle.BattleFieldInventory.FORCE_STOP;

public class AcceptBattleInventory extends UniversalInventory {

    private static Inventory inventory;

    private static final ItemStack YES = BukkitUtils.createItemStack(Material.SLIME_BLOCK, ChatColor.GREEN + "Oui", null);
    private static final ItemStack NO = BukkitUtils.createItemStack(Material.REDSTONE_BLOCK, ChatColor.RED + "Non", null);

    static {
        inventory = Bukkit.createInventory(null, 9, ChatColor.BLACK + "Accepter le duel");

        inventory.setItem(3, YES);
        inventory.setItem(5, NO);
    }

    public AcceptBattleInventory() {
        super(inventory);
    }

    @Override
    public InventoryType getType() {
        return InventoryType.ACCEPT_BATTLE;
    }

    @Override
    public void handleClicking(TGAPlayer p) {
        ItemStack clicked = NextUtils.forceNextItem(p, InventoryType.ACCEPT_BATTLE);

        if(clicked.equals(YES)) {
            // Let's start a battle!
            TGAPlayer ennemy = p.getPotentialBattle().getEnemy();
            TGAPlayer.createBattle(p, ennemy);
        }else if(clicked.equals(NO)) {
            // Oh no, let's reset all <code>PotentialBattle</code>
            p.getPotentialBattle().getEnemy().sendImpossible(Prefixes.CREATURE_PROPERTY + p.getBukkitPlayer().getName() + ChatColor.RED + " a refusé le duel");

            p.getPotentialBattle().getEnemy().setPotentialBattle(null);
            p.setPotentialBattle(null);
        }else if(clicked.equals(FORCE_STOP)) {
                p.getBukkitPlayer().closeInventory();
        }else{
            InventoriesManager.getInstance().handle(p, InventoryType.ACCEPT_BATTLE);
        }

        return;
    }
}
