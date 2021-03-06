package fr.badcookie20.tga.inventories.management;

import fr.badcookie20.tga.inventories.manager.InventoryType;
import fr.badcookie20.tga.inventories.manager.SaveableInventory;
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

public class PlayersListInventory extends SaveableInventory {

    public static ItemStack CANCEL = BukkitUtils.createItemStack(Material.ARROW, ChatColor.GREEN + "Annuler", null);

    @Override
    public Inventory get(TGAPlayer p) {
        // todo prevent displaying himself

        int playersAmount = Bukkit.getOnlinePlayers().size();

        Inventory inv = BukkitUtils.createInventory(playersAmount + 1, ChatColor.BLACK + "Joueurs");

        for(Player player : Bukkit.getOnlinePlayers()) {
            if(TGAPlayer.getPlayer(player).getBattleField() != null || TGAPlayer.getPlayer(player).getPotentialBattle() != null) {
                continue;
            }

            ItemStack item = new ItemStack(Material.SKULL_ITEM);
            SkullMeta itemMeta = (SkullMeta) item.getItemMeta();
            itemMeta.setDisplayName(ChatColor.GOLD + player.getDisplayName());
            itemMeta.setOwner(player.getName());
            item.setItemMeta(itemMeta);
            inv.addItem(item);
        }

        inv.setItem(inv.getSize() - 1, CANCEL);

        return inv;
    }

    @Override
    public InventoryType getType() {
        return InventoryType.PLAYERS_LIST;
    }

    @Override
    public void handleClicking(TGAPlayer p) {
        ItemStack clicked = NextUtils.forceNextItem(p, InventoryType.PLAYERS_LIST);

        NextUtils.updateClickedItemOf(p.getBukkitPlayer(), clicked);
    }
}
