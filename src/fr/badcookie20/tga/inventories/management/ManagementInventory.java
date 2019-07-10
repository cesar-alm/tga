package fr.badcookie20.tga.inventories.management;

import fr.badcookie20.tga.inventories.manager.InventoriesManager;
import fr.badcookie20.tga.inventories.manager.InventoryType;
import fr.badcookie20.tga.inventories.manager.SaveableInventory;
import fr.badcookie20.tga.player.PotentialBattle;
import fr.badcookie20.tga.player.TGAPlayer;
import fr.badcookie20.tga.utils.BukkitUtils;
import fr.badcookie20.tga.utils.NextUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;
import java.util.Collections;

public class ManagementInventory extends SaveableInventory {

    private static final ItemStack ACCESS_CARD_LIST = BukkitUtils.createItemStack(Material.PAPER,
            ChatColor.GREEN + "Liste de cartes",
            null);
    private static final ItemStack BATTLE = BukkitUtils.createItemStack(Material.GOLD_SWORD,
            ChatColor.GREEN + "Lancer un duel",
            Arrays.asList(ChatColor.GOLD + "Vous permet de lancer un duel contre", ChatColor.GOLD + "un autre joueur"));
    private static final ItemStack STATISTICS = BukkitUtils.createItemStack(Material.BOOK, ChatColor.GREEN + "Statistiques", null);
    private static final ItemStack ME = BukkitUtils.createItemStack(Material.DIAMOND,
            "ERR_NOT_INIT",
            Collections.singletonList("ERR_NOT_INIT"));

    @Override
    public Inventory get(TGAPlayer p) {
        if(this.isSaved(p)) {
            return this.getSavedInventory(p);
        }

        Inventory inventory = Bukkit.createInventory(null, 9, ChatColor.BLACK + "Gestion");

        inventory.setItem(0, BATTLE);

        ItemMeta itemMeta = ME.getItemMeta();
        itemMeta.setDisplayName(ChatColor.AQUA + p.getBukkitPlayer().getName());
        itemMeta.setLore(Arrays.asList(
                ChatColor.GOLD + "Cartes possédées : " + ChatColor.GREEN + p.getCardsList(false).size(),
                ChatColor.GOLD + "Affinité : " + ChatColor.GREEN + p.getManaAffinity().getRawDisplayName()));

        ME.setItemMeta(itemMeta);

        inventory.setItem(4, ME);
        inventory.setItem(7, STATISTICS);
        inventory.setItem(8, ACCESS_CARD_LIST);

        return saveInventory(p, inventory);
    }

    @Override
    public InventoryType getType() {
        return InventoryType.MANAGEMENT;
    }

    @Override
    public void handleClicking(TGAPlayer p) {
        ItemStack clicked = NextUtils.getNextClickedItemOf(p.getBukkitPlayer(), true);

        if(clicked == null) return;

        if(BukkitUtils.areSimilar(ACCESS_CARD_LIST, clicked)) {
            p.getBukkitPlayer().closeInventory();
            new Thread(() -> InventoriesManager.getInstance().handle(p, InventoryType.CARD_LIST)).start();
            return;
        }

        if (clicked.equals(STATISTICS)) {
            p.getBukkitPlayer().closeInventory();
            new Thread(() -> InventoriesManager.getInstance().handle(p, InventoryType.STATISTICS)).start();
            return;
        }

        if(BukkitUtils.areSimilar(BATTLE, clicked)) {
            p.getBukkitPlayer().closeInventory();

            p.getBukkitPlayer().sendMessage(ChatColor.AQUA + "Choisissez le joueur à affronter :");

            InventoriesManager.getInstance().handle(p, InventoryType.PLAYERS_LIST_WITH_STATISTICS);

            ItemStack clickedPlayer = NextUtils.getLastClickedItemOf(p.getBukkitPlayer());
            if(clickedPlayer.equals(PlayersListInventory.CANCEL)) {
                return;
            }

            String playerName = ((SkullMeta) clickedPlayer.getItemMeta()).getOwner();
            TGAPlayer enemy = TGAPlayer.getPlayer(Bukkit.getPlayer(playerName));

            p.setPotentialBattle(new PotentialBattle(enemy));
            enemy.setPotentialBattle(new PotentialBattle(p));

            InventoriesManager.getInstance().handle(p, InventoryType.BATTLE_CHOOSER);

            return;
        }

        if(BukkitUtils.areSimilar(ME, clicked)) {
            p.getBukkitPlayer().closeInventory();
            new Thread(() -> InventoriesManager.getInstance().handle(p, InventoryType.MANAGEMENT)).start();
            return;
        }
    }
}
