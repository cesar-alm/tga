package fr.badcookie20.tga.inventories.admin;

import fr.badcookie20.tga.cards.Card;
import fr.badcookie20.tga.inventories.manager.InventoriesManager;
import fr.badcookie20.tga.inventories.manager.InventoryType;
import fr.badcookie20.tga.inventories.manager.SaveableInventory;
import fr.badcookie20.tga.player.TGAPlayer;
import fr.badcookie20.tga.utils.*;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ForbiddenInventory extends SaveableInventory {

    private static final ItemStack ALLOW_ALL = BukkitUtils.createItemStack(Material.SLIME_BALL, ChatColor.GOLD + "Autoriser toutes les cartes", null);
    public static final String FORBIDDEN = ChatColor.RED + " (Interdite)";
    public static final String ALLOWED = ChatColor.GREEN + " (Autorisée)";

    @Override
    public Inventory get(TGAPlayer p) {
        List<? extends Card> cards = CardUtils.sortInverted(Card.Type.MANA, CardUtils.getAllCards());
        List<? extends Card> forbiddenCards = ConfigUtils.getForbiddenCards();

        Inventory inv = BukkitUtils.createInventory(cards, 2, ChatColor.BLACK + "Cartes interdites");

        for(Card c : cards) {
            ItemStack item = c.get();
            ItemMeta itemMeta = item.getItemMeta();
            String name = itemMeta.getDisplayName();
            List<String> lore = itemMeta.getLore();

            lore.add("");

            if(forbiddenCards.contains(c)) {
                itemMeta.setDisplayName(name + FORBIDDEN);
                lore.add(ChatColor.GREEN + "" + ChatColor.ITALIC + "->Autoriser");
                itemMeta.setLore(lore);
            }else{
                itemMeta.setDisplayName(name + ALLOWED);
                lore.add(ChatColor.RED + "" + ChatColor.ITALIC + "->Interdire");
                itemMeta.setLore(lore);
            }

            item.setItemMeta(itemMeta);
            inv.addItem(item);
        }

        inv.setItem(inv.getSize() - 2, ALLOW_ALL);
        inv.setItem(inv.getSize() - 1, InventoriesManager.GO_BACK);

        return inv;
    }

    @Override
    public InventoryType getType() {
        return InventoryType.FORBIDDEN;
    }

    @Override
    public void handleClicking(TGAPlayer p) {
        ItemStack clicked = NextUtils.forceNextItem(p, InventoryType.FORBIDDEN);

        if(clicked.equals(ALLOW_ALL)) {
            ConfigUtils.emptyForbiddenCards();
            InventoriesManager.getInstance().handle(p, InventoryType.FORBIDDEN);
            return;
        }else if(clicked.equals(InventoriesManager.GO_BACK)) {
            InventoriesManager.getInstance().handle(p, InventoryType.ADMIN);
            return;
        }

        Card c = CardUtils.getCard(clicked);

        if(c == null) {
            Logger.logError("card is null!");
            return;
        }

        if(ConfigUtils.getForbiddenCards().contains(c)) {
            ConfigUtils.removeForbiddenCard(c);
        }else{
            ConfigUtils.addForbiddenCard(c);
        }

        InventoriesManager.getInstance().handle(p, InventoryType.FORBIDDEN);
    }

}
