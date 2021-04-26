package fr.badcookie20.tga.inventories.manager;

import fr.badcookie20.tga.Plugin;
import fr.badcookie20.tga.inventories.admin.*;
import fr.badcookie20.tga.inventories.battle.*;
import fr.badcookie20.tga.inventories.battle.personalizer.*;
import fr.badcookie20.tga.inventories.battle.selector.SelectTargetAllInventory;
import fr.badcookie20.tga.inventories.battle.selector.SelectTargetCreatureInventory;
import fr.badcookie20.tga.inventories.battle.selector.SelectTargetEnchantmentInventory;
import fr.badcookie20.tga.inventories.management.*;
import fr.badcookie20.tga.player.TGAPlayer;
import fr.badcookie20.tga.utils.BukkitUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class InventoriesManager {

    public static final ItemStack GO_BACK = BukkitUtils.createItemStack(Material.ARROW, ChatColor.AQUA + "Retour", null);
    public static final ItemStack CLOSE = BukkitUtils.createItemStack(Material.ARROW, ChatColor.AQUA + "Fermer", null);

    private static InventoriesManager instance;

    private List<TGAInventory> inventories;

    /**
     * Constructor of the inventories. Basically, registers all the inventories.
     */
    private InventoriesManager() {
        instance = this;

        this.inventories = new ArrayList<>();

        inventories.add(new CardListInventory());
        inventories.add(new AffinitiesInventory());
        inventories.add(new ManagementInventory());
        inventories.add(new AcceptBattleInventory());
        inventories.add(new BattleFieldInventory());
        inventories.add(new GraveyardInventory());
        inventories.add(new BannedCardsInventory());
        inventories.add(new StatisticsInventory());
        inventories.add(new YesCancelInventory());
        inventories.add(new PlayersListInventory());
        inventories.add(new PlayersStatisticListInventory());
        inventories.add(new SelectTargetAllInventory());
        inventories.add(new SelectTargetEnchantmentInventory());
        inventories.add(new SelectTargetCreatureInventory());
        inventories.add(new AdminInventory());
        inventories.add(new ForbiddenInventory());
        inventories.add(new CurrentBattlesInventory());
        inventories.add(new BattleTypeInventory());
        inventories.add(new BattlePersonalizerInventory());
        inventories.add(new LifePointsPersonalizerInventory());
        inventories.add(new DamageMultiplierPersonalizerInventory());
        inventories.add(new StatisticsPersonalizerInventory());
        inventories.add(new MaxTurnsPersonalizerInventory());
        inventories.add(new PlayerOptionsInventory());
        inventories.add(new PlayersAdminListInventory());
        inventories.add(new PlayersListWithStatisticsInventory());
    }

    /**
     * Getter for inventories.
     * @param p the player that is reading the inventory
     * @param type the type of the inventory
     * @return a personalized inventory to the specified player
     */
    public Inventory getInventory(TGAPlayer p, InventoryType type) {
        return getInventory(type).get(p);
    }

    /**
     * Generic getter for inventories
     * @param type the type of the inventory
     * @return a generic inventory instance, not specified to any player
     */
    public TGAInventory getInventory(InventoryType type) {
        for(TGAInventory inventory : inventories) {
            if(inventory.getType() == type) {
                return inventory;
            }
        }

        return null;
    }

    /**
     * Opens the specified inventory to the specified player
     * @param p the player that is reading the inventory
     * @param type the type of the inventory
     */
    public void openInventory(TGAPlayer p, InventoryType type) {
        p.getBukkitPlayer().openInventory(getInventory(p, type));
    }

    /**
     * Opens + handles the opening of the inventory. Always make sure you call it asynchronously !!
     * @param p the player that is reading the inventory
     * @param type the type of the inventory
     */
    public void handle(TGAPlayer p, InventoryType type) {
        openInventory(p, type);
        getInventory(type).handleClicking(p);
    }

    /**
     * Notifies the specified inventory that it has been updated since the last time the player opened it.
     * For clarity, prefer using BattleField.Location.xxx.update(p) when applicable.
     * @param p the player whose inventory has changed
     * @param type the type of the inventory
     */
    public void update(TGAPlayer p, InventoryType type) {
        SaveableInventory inventory = (SaveableInventory) getInventory(type);
        inventory.updateInventory(p);

        if(type == InventoryType.BATTLEFIELD && p.getBattleField() != null) {
            inventory.updateInventory(p.getBattleField().getEnemy());
        }
    }

    /**
     * Use it to init the inventories, ONLY ONCE !
     */
    public static void init() {
        if(instance == null) new InventoriesManager();
    }

    public static void handleAsync(TGAPlayer p, InventoryType type) {
        Bukkit.getScheduler().runTaskAsynchronously(Plugin.getInstance(), () -> getInstance().handle(p, type));
    }

    public static InventoriesManager getInstance() {return instance; }

}
