package fr.badcookie20.tga.inventories.battle;

import fr.badcookie20.tga.inventories.management.PlayersListInventory;
import fr.badcookie20.tga.inventories.manager.InventoriesManager;
import fr.badcookie20.tga.inventories.manager.InventoryType;
import fr.badcookie20.tga.inventories.manager.UniversalInventory;
import fr.badcookie20.tga.player.PersonalizingAttribute;
import fr.badcookie20.tga.player.PotentialBattle;
import fr.badcookie20.tga.player.TGAPlayer;
import fr.badcookie20.tga.utils.BukkitUtils;
import fr.badcookie20.tga.utils.NextUtils;
import fr.badcookie20.tga.utils.Prefixes;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class BattleTypeInventory extends UniversalInventory {

    private static final ItemStack DEFAULT = BukkitUtils.createItemStack(Material.GOLD_SWORD, ChatColor.GREEN + "Duel classique", null);
    private static final ItemStack DEFAULT_NO_STATS = BukkitUtils.createItemStack(Material.IRON_SWORD, ChatColor.GREEN + "Pour du beurre", null);
    private static final ItemStack PERSONALIZED = BukkitUtils.createItemStack(Material.DIAMOND_SWORD, ChatColor.GREEN + "Duel personnalis√©", null);

    private static Inventory inventory;

    static {
        inventory = Bukkit.createInventory(null, 9, ChatColor.BLACK + "Type de duel");

        inventory.setItem(1, DEFAULT);
        inventory.setItem(4, DEFAULT_NO_STATS);
        inventory.setItem(7, PERSONALIZED);
        inventory.setItem(inventory.getSize() - 1, PlayersListInventory.CANCEL);
    }

    public BattleTypeInventory() {
        super(inventory);
    }

    @Override
    public InventoryType getType() {
        return InventoryType.BATTLE_CHOOSER;
    }

    @Override
    public void handleClicking(TGAPlayer p) {
        ItemStack clicked = NextUtils.forceNextItem(p, InventoryType.BATTLE_CHOOSER);

        TGAPlayer enemy = p.getPotentialBattle().getEnemy();

        if(clicked.equals(DEFAULT)) {
            enemy.sendBattleRequest(p);
            p.getBukkitPlayer().sendMessage(ChatColor.GREEN + "Invitation envoy√©e √† " + Prefixes.CREATURE_PROPERTY + enemy.getBukkitPlayer().getName());
            return;
        }

        if(clicked.equals(DEFAULT_NO_STATS)) {
            PotentialBattle potentialBattle = p.getPotentialBattle();
            potentialBattle.set(PersonalizingAttribute.STATISTICS_INCREASE, false);
            p.setPotentialBattle(potentialBattle);

            enemy.sendBattleRequest(p);
            p.getBukkitPlayer().sendMessage(ChatColor.GREEN + "Invitation envoy√©e √† " + Prefixes.CREATURE_PROPERTY + enemy.getBukkitPlayer().getName());
            return;
        }

        if(clicked.equals(PERSONALIZED)) {
            InventoriesManager.getInstance().handle(p, InventoryType.BATTLE_PERSONALIZER);
            return;
        }

        if(clicked.equals(PlayersListInventory.CANCEL)) {
            // Don't forget to reset all <code>PotentialBattle</code> !
            p.getBukkitPlayer().closeInventory();

            p.getPotentialBattle().getEnemy().setPotentialBattle(null);
            p.setPotentialBattle(null);
        }
    }
}
