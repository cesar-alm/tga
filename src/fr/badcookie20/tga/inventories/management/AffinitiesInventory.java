package fr.badcookie20.tga.inventories.management;

import fr.badcookie20.tga.cards.creatures.CreatureType;
import fr.badcookie20.tga.cards.mana.ManaType;
import fr.badcookie20.tga.inventories.manager.InventoryType;
import fr.badcookie20.tga.inventories.manager.UniversalInventory;
import fr.badcookie20.tga.player.TGAPlayer;
import fr.badcookie20.tga.utils.BukkitUtils;
import fr.badcookie20.tga.utils.ConfigUtils;
import fr.badcookie20.tga.utils.NextUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class AffinitiesInventory extends UniversalInventory {

    private static Inventory inventory;

    static {
        inventory = Bukkit.createInventory(null, 9, ChatColor.BLACK + "Affinités");

        for(ManaType type : ManaType.values()) {
            if(type != ManaType.UNDEFINED) inventory.addItem(BukkitUtils.createItemStack(Material.BEACON, type.getDisplayName(), type.description()));
        }
    }

    public AffinitiesInventory() {
        super(inventory);
    }

    @Override
    public InventoryType getType() {
        return InventoryType.AFFINITIES;
    }

    @Override
    public void handleClicking(TGAPlayer p) {
        ItemStack clicked = NextUtils.forceNextItem(p, this.getType());

        ManaType manaType = ManaType.get(clicked.getItemMeta().getDisplayName());
        CreatureType creatureType = manaType.getCreatureType();

        p.getBukkitPlayer().sendMessage(ChatColor.AQUA + "Vous avez choisi l'affinité " + manaType + ChatColor.AQUA + " et le type de créature " + creatureType);
        p.getBukkitPlayer().sendMessage(ChatColor.GREEN + "Vous pouvez désormais commencer à chercher des cartes, présentes un peu partout sur la carte !");

        p.setCreatureAffinity(creatureType);
        p.setManaAffinity(manaType);

        ConfigUtils.setCreatureAffinity(p, creatureType);
        ConfigUtils.setManaAffinity(p, manaType);
    }
}
