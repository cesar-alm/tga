package fr.badcookie20.tga.utils;

import fr.badcookie20.tga.player.Statistics;
import fr.badcookie20.tga.player.TGAPlayer;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

public class MiscUtils {

    /**
     * Returns an ItemStack containing information about the player
     * @param player the specified player
     * @param level level 0 contains only the name and the head, level 1 contains name, head, affinity and card list, and any other level contains everything
     * @return an ItemStack
     */
    public static ItemStack personalInfo(TGAPlayer player, int level) {
        ItemStack item = basicHeadItem();

        SkullMeta itemMeta = (SkullMeta) item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.AQUA + player.getBukkitPlayer().getName());
        itemMeta.setOwner(player.getBukkitPlayer().getName());

        if (level == 0) {
            item.setItemMeta(itemMeta);
            return item;
        }

        List<String> lore = new ArrayList<>();

        lore.add(ChatColor.GOLD + "Cartes possédées : " + ChatColor.GREEN + player.getCardsList(false).size());
        lore.add(ChatColor.GOLD + "Affinité : " + ChatColor.GREEN + player.getManaAffinity().getRawDisplayName());

        if (level == 1) {
            itemMeta.setLore(lore);
            item.setItemMeta(itemMeta);
            return item;
        }

        Statistics statistics = player.getStatistics();

        lore.add("");
        lore.add(ChatColor.GOLD + "Parties gagnées : " + ChatColor.GREEN + statistics.getWonGames());
        lore.add(ChatColor.GOLD + "Parties perdues : " + ChatColor.GREEN + statistics.getLostGames());
        lore.add(ChatColor.GOLD + "Parties abandonnées : " + ChatColor.GREEN + statistics.getSurrenderedGames());
        lore.add(ChatColor.GOLD + "Parties gagnées par abandon : " + ChatColor.GREEN + statistics.getWonBySurrenderGames());

        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        return item;
    }

    public static ItemStack basicHeadItem() {
        return new ItemStack(Material.SKULL_ITEM);
    }


}
