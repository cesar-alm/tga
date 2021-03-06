package fr.badcookie20.tga.inventories.admin;

import fr.badcookie20.tga.inventories.manager.InventoriesManager;
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PlayersAdminListInventory extends SaveableInventory {


    @Override
    public Inventory get(TGAPlayer p) {
        Collection<? extends Player> players = Bukkit.getOnlinePlayers();

        Inventory inv = BukkitUtils.createInventory(players.size() + 1, ChatColor.BLACK + "Joueurs");

        for(Player pp : players) {
            TGAPlayer tgaPlayer = TGAPlayer.getPlayer(pp);

            inv.addItem(getItem(tgaPlayer));
        }

        inv.setItem(inv.getSize() - 1, InventoriesManager.GO_BACK);

        return inv;
    }

    private ItemStack getItem(TGAPlayer p) {
        ItemStack item = new ItemStack(Material.SKULL_ITEM);
        SkullMeta itemMeta = (SkullMeta) item.getItemMeta();

        itemMeta.setDisplayName(ChatColor.GOLD + p.getBukkitPlayer().getName());
        itemMeta.setOwner(p.getBukkitPlayer().getName());

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GREEN + "Cliquez pour afficher les options");
        itemMeta.setLore(lore);

        item.setItemMeta(itemMeta);

        return item;
    }

    @Override
    public InventoryType getType() {
        return InventoryType.PLAYERS_LIST_ADMIN;
    }

    @Override
    public void handleClicking(TGAPlayer p) {
        ItemStack clicked = NextUtils.forceNextItem(p, InventoryType.PLAYERS_LIST_ADMIN);

        if(clicked.equals(InventoriesManager.GO_BACK)) {
            InventoriesManager.getInstance().handle(p, InventoryType.ADMIN);
            return;
        }

        String playerName = BukkitUtils.removeFirstColor(clicked.getItemMeta().getDisplayName());

        TGAPlayer choosen = TGAPlayer.getPlayer(Bukkit.getPlayer(playerName));

        /*if(choosen.equals(p)) {
            p.sendImpossible("Vous ne pouvez pas modifier vos propres informations, par mesure de s??curit?? !");
            InventoriesManager.getInstance().handle(p, InventoryType.PLAYERS_LIST_ADMIN);
            return;
        }*/

        p.confirmActionOnPlayer(choosen);
    }
}
