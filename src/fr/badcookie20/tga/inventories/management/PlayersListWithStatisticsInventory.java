package fr.badcookie20.tga.inventories.management;

import fr.badcookie20.tga.inventories.manager.InventoryType;
import fr.badcookie20.tga.inventories.manager.SaveableInventory;
import fr.badcookie20.tga.player.TGAPlayer;
import fr.badcookie20.tga.utils.BukkitUtils;
import fr.badcookie20.tga.utils.MiscUtils;
import fr.badcookie20.tga.utils.NextUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * Slightly different inventory compared to {@link PlayersListInventory} and {@link PlayersStatisticListInventory}.
 * Here, it displays an inventory as a {@link PlayersStatisticListInventory} but behaves as a {@link PlayersListInventory}
 */
public class PlayersListWithStatisticsInventory extends SaveableInventory {

    @Override
    public Inventory get(TGAPlayer p) {
        // todo prevent displaying himself

        int playersAmount = Bukkit.getOnlinePlayers().size();

        Inventory inv = BukkitUtils.createInventory(playersAmount + 1, ChatColor.BLACK + "Joueurs");

        for(Player player : Bukkit.getOnlinePlayers()) {
            inv.addItem(MiscUtils.personalInfo(TGAPlayer.getPlayer(player), 2));
        }

        inv.setItem(inv.getSize() - 1, PlayersListInventory.CANCEL);

        return inv;
    }

    @Override
    public InventoryType getType() {
        return InventoryType.PLAYERS_LIST_WITH_STATISTICS;
    }

    @Override
    public void handleClicking(TGAPlayer p) {
        ItemStack clicked = NextUtils.forceNextItem(p, InventoryType.PLAYERS_LIST_WITH_STATISTICS);

        NextUtils.updateClickedItemOf(p.getBukkitPlayer(), clicked);
    }
}
