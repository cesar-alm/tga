package fr.badcookie20.tga.inventories.admin;

import fr.badcookie20.tga.inventories.manager.InventoriesManager;
import fr.badcookie20.tga.inventories.manager.InventoryType;
import fr.badcookie20.tga.inventories.manager.SaveableInventory;
import fr.badcookie20.tga.player.BattleField;
import fr.badcookie20.tga.player.PersonalizingAttribute;
import fr.badcookie20.tga.player.StopReason;
import fr.badcookie20.tga.player.TGAPlayer;
import fr.badcookie20.tga.utils.BukkitUtils;
import fr.badcookie20.tga.utils.NextUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class CurrentBattlesInventory extends SaveableInventory {

    private static final ItemStack END_ALL = BukkitUtils.createItemStack(Material.REDSTONE, ChatColor.RED + "Stopper tous les duels", null);
    private static final String AGAINST_PREFIX = ChatColor.GOLD + " <=> " + ChatColor.GREEN;

    @Override
    public Inventory get(TGAPlayer p) {
        List<BattleField> battleFields = BattleField.getBattleFieldsOnce();

        Inventory inv = BukkitUtils.createInventory(battleFields.size() + 2, ChatColor.BLACK + "Duels en cours");

        for(BattleField battleField : battleFields) {
            TGAPlayer p1 = battleField.getOwner();
            TGAPlayer p2 = battleField.getEnemy();

            boolean personalized = battleField.getPersonalizationLevel() != 0;

            String name = ChatColor.AQUA + "" + battleField.getID() + "~" + ChatColor.GREEN + p1.getBukkitPlayer().getName() + AGAINST_PREFIX + p2.getBukkitPlayer().getName();
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GOLD + "PV : " + ChatColor.GREEN + "" + p1.getBattleField().getLifePoints() + AGAINST_PREFIX + p2.getBattleField().getLifePoints());
            lore.add("");
            lore.add(ChatColor.GOLD + "Tour #" + ChatColor.GREEN + "" + battleField.getTurnNumber());
            lore.add(ChatColor.GREEN + (personalized ? "Partie personnalis??e" : "Partie classique"));

            if(personalized) {
                lore.add("");
                for(PersonalizingAttribute o : PersonalizingAttribute.values()) {
                    lore.add(ChatColor.GOLD + o.getValueName() + " : " + ChatColor.GREEN + battleField.getPotentialBattle().get(o));
                }
            }

            lore.add("");
            lore.add(ChatColor.GREEN + ""  + ChatColor.ITALIC + "Cliquez pour arr??ter ce duel");

            inv.addItem(BukkitUtils.createItemStack(Material.GOLD_SWORD, name, lore));
        }

        inv.setItem(inv.getSize() - 2, END_ALL);
        inv.setItem(inv.getSize() - 1, InventoriesManager.GO_BACK);

        return inv;
    }

    @Override
    public InventoryType getType() {
        return InventoryType.CURRENT_BATTLES;
    }

    @Override
    public void handleClicking(TGAPlayer p) {
        ItemStack clicked = NextUtils.forceNextItem(p, InventoryType.CURRENT_BATTLES);

        if(clicked.equals(InventoriesManager.GO_BACK)) {
            InventoriesManager.getInstance().handle(p, InventoryType.ADMIN);
            return;
        }else if(clicked.equals(END_ALL)) {
            BattleField.stopAll();
            InventoriesManager.getInstance().handle(p, InventoryType.ADMIN);
            return;
        }

        String name = clicked.getItemMeta().getDisplayName();

        String[] split = name.split("~");
        String idWithChatColor = split[0];
        String idString = BukkitUtils.removeFirstColor(idWithChatColor);

        int id = Integer.parseInt(idString);
        BattleField battleField = BattleField.getBattleField(id);

        if(battleField == null) {
            p.sendImpossible("Le duel selectionn?? n'existe pas !");
            InventoriesManager.getInstance().handle(p, InventoryType.CURRENT_BATTLES);
            return;
        }

        TGAPlayer.stopBattle(battleField.getOwner(), battleField.getEnemy(), StopReason.ADMIN);
        InventoriesManager.getInstance().handle(p, InventoryType.CURRENT_BATTLES);
    }
}
