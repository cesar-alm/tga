package fr.badcookie20.tga.inventories.admin;

import fr.badcookie20.tga.inventories.manager.InventoriesManager;
import fr.badcookie20.tga.inventories.manager.InventoryType;
import fr.badcookie20.tga.inventories.manager.SaveableInventory;
import fr.badcookie20.tga.player.BattleField;
import fr.badcookie20.tga.player.TGAPlayer;
import fr.badcookie20.tga.utils.BukkitUtils;
import fr.badcookie20.tga.utils.ConfigUtils;
import fr.badcookie20.tga.utils.NextUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;

public class AdminInventory extends SaveableInventory {

    private static final ItemStack RELOAD_SERVER = BukkitUtils.createItemStack(Material.ANVIL, ChatColor.RED + "Reload le serveur", null);
    private static final ItemStack PLAYERS = BukkitUtils.createItemStack(Material.EXP_BOTTLE, ChatColor.GOLD + "Gérer les joueurs", null);

    private static final String FORBIDDEN_NAME = ChatColor.GOLD + "Cartes interdites";
    private static final String CURRENT_BATTLES_NAME = ChatColor.GOLD + "Duels en cours";
    private static final String MONITOR_EVERYTHING_NAME = ChatColor.GOLD + "Tout observer ";

    private static final String ENABLED = ChatColor.GREEN + "(Activé)";
    private static final String DISABLED = ChatColor.RED + "(Désactive)";

    private static final Material FORBIDDEN = Material.JUKEBOX;
    private static final Material CURRENT_BATTLES = Material.GOLD_SWORD;
    private static final Material MONITOR_EVERYTHING = Material.ENDER_PEARL;

    @Override
    public Inventory get(TGAPlayer p) {
        Inventory inv = Bukkit.createInventory(null, 18, ChatColor.BLACK + "Administration");

        inv.setItem(1, BukkitUtils.createItemStack(MONITOR_EVERYTHING, MONITOR_EVERYTHING_NAME + (p.isMonitoring() ? ENABLED : DISABLED), null));
        inv.setItem(3, BukkitUtils.createItemStack(FORBIDDEN, FORBIDDEN_NAME,
                Collections.singletonList(ChatColor.GOLD + "" + ConfigUtils.getForbiddenEntities().size() + ChatColor.GREEN + " carte(s) interdite(s)")));
        inv.setItem(5, BukkitUtils.createItemStack(CURRENT_BATTLES, CURRENT_BATTLES_NAME,
                Collections.singletonList(ChatColor.GOLD + "" + BattleField.getBattleFieldsOnce().size() + ChatColor.GREEN + " duel(s) en cours")));
        inv.setItem(7, RELOAD_SERVER);
        inv.setItem(13, PLAYERS);

        inv.setItem(inv.getSize() - 1, InventoriesManager.CLOSE);

        return inv;
    }

    @Override
    public InventoryType getType() {
        return InventoryType.ADMIN;
    }

    @Override
    public void handleClicking(TGAPlayer p) {
        ItemStack clicked = NextUtils.forceNextItem(p, InventoryType.ADMIN);
        Material type = clicked.getType();

        if(clicked.equals(InventoriesManager.CLOSE)) return;

        if(type.equals(FORBIDDEN)) {
            InventoriesManager.getInstance().handle(p, InventoryType.FORBIDDEN);
            return;
        }else if(type.equals(CURRENT_BATTLES)) {
            InventoriesManager.getInstance().handle(p, InventoryType.CURRENT_BATTLES);
            return;
        }else if(clicked.equals(RELOAD_SERVER)) {
            Bukkit.getServer().reload();
            return;
        }else if(type.equals(MONITOR_EVERYTHING)) {
            p.invertMonitor();
            InventoriesManager.getInstance().handle(p, InventoryType.ADMIN);
            return;
        }else if(clicked.equals(PLAYERS)) {
            InventoriesManager.getInstance().handle(p, InventoryType.PLAYERS_LIST_ADMIN);
            return;
        }

        InventoriesManager.getInstance().handle(p, InventoryType.ADMIN);
    }
}
