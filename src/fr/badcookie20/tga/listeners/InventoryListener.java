package fr.badcookie20.tga.listeners;

import fr.badcookie20.tga.cards.FoundReason;
import fr.badcookie20.tga.events.PlayerChestOpenEvent;
import fr.badcookie20.tga.inventories.battle.BattleFieldInventory;
import fr.badcookie20.tga.inventories.manager.InventoriesManager;
import fr.badcookie20.tga.inventories.manager.InventoryType;
import fr.badcookie20.tga.player.BattleField;
import fr.badcookie20.tga.player.TGAPlayer;
import fr.badcookie20.tga.utils.CardUtils2;
import fr.badcookie20.tga.utils.NextUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class InventoryListener implements Listener {

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();

        NextUtils.updateClickedItemOf(p, NextUtils.CLOSED_INVENTORY);
    }

    @EventHandler
    public void onInventory(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();

        if(NextUtils.needsClickingCancel(p)) {
            NextUtils.updateClickingsCancel(p);
            e.setCancelled(true);
        }

        NextUtils.updateClickedItemOf(p, e.getCurrentItem());
        NextUtils.updateClickedRawSlotOf(p, e.getRawSlot());
        NextUtils.updateClickedInventoryOf(p, e.getInventory());
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        TGAPlayer tgaPlayer = TGAPlayer.getPlayer(p);

        if(e.getItem() != null) {
            if (e.getItem().equals(BattleFieldInventory.REOPEN)) {
                BattleField.Location.BATTLEFIELD.update(tgaPlayer);
                InventoriesManager.handleAsync(tgaPlayer, InventoryType.BATTLEFIELD);
                return;
            }
        }

        Block clicked = e.getClickedBlock();

        if(clicked == null || clicked.getType() == Material.AIR) return;

        Material type = clicked.getType();
        Action action = e.getAction();

        if(type == Material.CHEST && action == Action.RIGHT_CLICK_BLOCK) {
            Bukkit.getServer().getPluginManager().callEvent(new PlayerChestOpenEvent(p, (Chest) e.getClickedBlock().getState()));
            return;
        }

        if(type == Material.DOUBLE_PLANT || type == Material.LONG_GRASS) {
            if(action == Action.LEFT_CLICK_BLOCK) {
                CardUtils2.giveRandomCard(tgaPlayer, FoundReason.PLANT);
            }
        }
    }

}
