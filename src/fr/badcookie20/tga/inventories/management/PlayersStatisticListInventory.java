package fr.badcookie20.tga.inventories.management;

import fr.badcookie20.tga.inventories.manager.InventoriesManager;
import fr.badcookie20.tga.inventories.manager.InventoryType;
import fr.badcookie20.tga.inventories.manager.SaveableInventory;
import fr.badcookie20.tga.player.Statistics;
import fr.badcookie20.tga.player.TGAPlayer;
import fr.badcookie20.tga.utils.BukkitUtils;
import fr.badcookie20.tga.utils.NextUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

public class PlayersStatisticListInventory extends SaveableInventory {

    @Override
    public Inventory get(TGAPlayer p) {
        // todo prevent displaying himself

        int playersAmount = Bukkit.getOnlinePlayers().size();

        Inventory inv = BukkitUtils.createInventory(playersAmount + 1, ChatColor.BLACK + "Joueurs");

        for(Player player : Bukkit.getOnlinePlayers()) {
            inv.addItem(buildStatisticItem(player));
        }

        inv.setItem(inv.getSize() - 1, InventoriesManager.GO_BACK);

        return inv;
    }

    @Override
    public InventoryType getType() {
        return InventoryType.PLAYERS_LIST_STATISTICS;
    }

    @Override
    public void handleClicking(TGAPlayer p) {
        ItemStack clicked = NextUtils.forceNextItem(p, InventoryType.PLAYERS_LIST_STATISTICS);

        if(clicked.equals(InventoriesManager.GO_BACK)) {
            InventoriesManager.getInstance().handle(p, InventoryType.STATISTICS);
            return;
        }

        InventoriesManager.getInstance().handle(p, InventoryType.PLAYERS_LIST_STATISTICS);
    }

    public static ItemStack buildStatisticItem(Player player) {
        ItemStack item = new ItemStack(Material.SKULL_ITEM);
        SkullMeta itemMeta = (SkullMeta) item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.AQUA + player.getName());
        itemMeta.setOwner(player.getName());

        TGAPlayer otherP = TGAPlayer.getPlayer(player);

        List<String> lore = new ArrayList<>();
        Statistics statistics = otherP.getStatistics();


        lore.add(ChatColor.GOLD + "Affinité : " + ChatColor.GREEN + otherP.getManaAffinity());
        lore.add(ChatColor.GOLD + "Cartes possédées : " + ChatColor.GREEN + otherP.getCardsList(false).size());
        lore.add("");
        lore.add(ChatColor.GOLD + "Parties gagnées : " + ChatColor.GREEN + statistics.getWonGames());
        lore.add(ChatColor.GOLD + "Parties perdues : " + ChatColor.GREEN + statistics.getLostGames());
        lore.add(ChatColor.GOLD + "Parties abandonnées : " + ChatColor.GREEN + statistics.getSurrenderedGames());
        lore.add(ChatColor.GOLD + "Parties gagnées par abandon : " + ChatColor.GREEN + statistics.getWonBySurrenderGames());

        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);

        return item;
    }

}
