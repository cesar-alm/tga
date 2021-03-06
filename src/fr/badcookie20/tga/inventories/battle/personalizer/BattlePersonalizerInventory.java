package fr.badcookie20.tga.inventories.battle.personalizer;

import fr.badcookie20.tga.inventories.manager.InventoriesManager;
import fr.badcookie20.tga.inventories.manager.InventoryType;
import fr.badcookie20.tga.inventories.manager.SaveableInventory;
import fr.badcookie20.tga.player.PersonalizingAttribute;
import fr.badcookie20.tga.player.TGAPlayer;
import fr.badcookie20.tga.utils.BukkitUtils;
import fr.badcookie20.tga.utils.NextUtils;
import fr.badcookie20.tga.utils.Prefixes;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class BattlePersonalizerInventory extends SaveableInventory {

    public static final ItemStack VALIDATE = BukkitUtils.createItemStack(Material.SLIME_BALL, ChatColor.GREEN + "Valider", null);

    @Override
    public Inventory get(TGAPlayer p) {
        Inventory inv = Bukkit.createInventory(null, 18, ChatColor.BLACK + "Personaliser le duel");

        ItemStack itemLifePoints = PersonalizingPerspectiveInventory.getActual(p, PersonalizingAttribute.LIFE_POINTS, TGAPlayer.DEFAULT_LIFE_POINTS);
        ItemStack itemMaxTurns = PersonalizingPerspectiveInventory.getActual(p, PersonalizingAttribute.MAX_TURNS, TGAPlayer.DEFAULT_MAX_TURNS);
        ItemStack itemMultiplier = PersonalizingPerspectiveInventory.getActual(p, PersonalizingAttribute.DAMAGE_MULTIPLIER, TGAPlayer.DEFAULT_DAMAGE_MULTIPLIER);
        ItemStack itemStatistics = PersonalizingPerspectiveInventory.getActual(p, PersonalizingAttribute.STATISTICS_INCREASE, true);

        inv.addItem(itemLifePoints);
        inv.addItem(itemMaxTurns);
        inv.addItem(itemMultiplier);
        inv.addItem(itemStatistics);

        inv.setItem(inv.getSize() -1, VALIDATE);

        return inv;
    }

    @Override
    public InventoryType getType() {
        return InventoryType.BATTLE_PERSONALIZER;
    }

    @Override
    public void handleClicking(TGAPlayer p) {
        ItemStack clicked = NextUtils.forceNextItem(p, InventoryType.BATTLE_PERSONALIZER);

        if(clicked.equals(VALIDATE)) {
            TGAPlayer enemy = p.getPotentialBattle().getEnemy();

            enemy.sendBattleRequest(p);
            p.getBukkitPlayer().sendMessage(ChatColor.GREEN + "Invitation envoy??e ?? " + Prefixes.CREATURE_PROPERTY + enemy.getBukkitPlayer().getName());
            return;
        }

        String name = BukkitUtils.removeFirstColor(clicked.getItemMeta().getDisplayName());

        InventoriesManager.getInstance().handle(p, PersonalizingAttribute.getPersonalizingObject(name).getInventory());
    }
}
