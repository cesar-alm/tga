package fr.badcookie20.tga.inventories.management;

import fr.badcookie20.tga.inventories.manager.InventoriesManager;
import fr.badcookie20.tga.inventories.manager.InventoryType;
import fr.badcookie20.tga.inventories.manager.SaveableInventory;
import fr.badcookie20.tga.player.TGAPlayer;
import fr.badcookie20.tga.utils.BukkitUtils;
import fr.badcookie20.tga.utils.NextUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.List;

public class StatisticsInventory extends SaveableInventory{

    private Material WON_GAMES = Material.DIAMOND;
    private Material LOST_GAMES = Material.REDSTONE;
    private Material SURRENDERED_GAMES = Material.COAL;
    private Material WON_BY_SURRENDER = Material.GOLD_INGOT;
    private ItemStack SEE_OTHER_PLAYERS = BukkitUtils.createItemStack(Material.BOOKSHELF, ChatColor.GOLD + "Voir les statistiques d'autres joueurs", null);

    @Override
    public Inventory get(TGAPlayer p) {
        if(this.isSaved(p)) {
            return this.getSavedInventory(p);
        }

        Inventory inv = Bukkit.createInventory(null, 9, ChatColor.BLACK + "Statistiques");

        inv.addItem(initStatistic(WON_GAMES, "Parties gagnées", p.getStatistics().getWonGames(), p.getStatistics().getTotalGames()));
        inv.addItem(initStatistic(LOST_GAMES, "Parties perdues", p.getStatistics().getLostGames(), p.getStatistics().getTotalGames()));
        inv.addItem(initStatistic(WON_BY_SURRENDER, "Parties abandonnées", p.getStatistics().getWonBySurrenderGames(), p.getStatistics().getTotalGames()));
        inv.addItem(initStatistic(SURRENDERED_GAMES, "Parties gagnées par abandon", p.getStatistics().getSurrenderedGames(), p.getStatistics().getTotalGames()));


        inv.setItem(inv.getSize() - 2, SEE_OTHER_PLAYERS);
        inv.setItem(8, InventoriesManager.GO_BACK);

        return saveInventory(p, inv);
    }

    @Override
    public InventoryType getType() {
        return InventoryType.STATISTICS;
    }

    @Override
    public void handleClicking(TGAPlayer p) {
        ItemStack item = NextUtils.forceNextItem(p, InventoryType.STATISTICS);

        if(item.equals(InventoriesManager.GO_BACK)) {
            InventoriesManager.getInstance().handle(p, InventoryType.MANAGEMENT);
            return;
        }else if(item.equals(SEE_OTHER_PLAYERS)) {
            InventoriesManager.getInstance().handle(p, InventoryType.PLAYERS_LIST_STATISTICS);
            return;
        }

        InventoriesManager.getInstance().handle(p, InventoryType.STATISTICS);
    }

    private ItemStack initStatistic(Material m, String name, int amount, int total) {
        String itemName = ChatColor.GREEN + name + " : " + ChatColor.GOLD + amount;
        List<String> lore = null;

        if(total != 0) {
            lore = Collections.singletonList(ChatColor.AQUA + "" + ((amount*100)/total) + "%" + ChatColor.GOLD + " des parties jouées");
        }

        return BukkitUtils.createItemStack(m, itemName, lore);
    }
}
