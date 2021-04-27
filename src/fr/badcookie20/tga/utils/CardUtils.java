package fr.badcookie20.tga.utils;

import fr.badcookie20.tga.cards.Card;
import fr.badcookie20.tga.cards.Card.Type;
import fr.badcookie20.tga.cards.Entity;
import fr.badcookie20.tga.cards.FoundReason;
import fr.badcookie20.tga.cards.creatures.CreatureCard;
import fr.badcookie20.tga.cards.creatures.CreatureProperty;
import fr.badcookie20.tga.cards.creatures.CreatureType;
import fr.badcookie20.tga.cards.mana.ManaType;
import fr.badcookie20.tga.effect.Effect;
import fr.badcookie20.tga.player.TGAPlayer;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CardUtils {

	public static List<Card> sort(List<Card> cards, Type type) {
		List<Card> list = new ArrayList<>();
		
		for(Card card : cards) {
			if(card.getType() == type) {
				list.add(card);
			}
		}
		
		return list;
	}

	public static List<CreatureCard> toCreatureCardList(List<Card> cards) {
		List<CreatureCard> creatures = new ArrayList<>();
		
		for(Card c : cards) {
			creatures.add((CreatureCard) c);
		}
		
		return creatures;
	}

	public static ItemStack createItemStack(String name, Type type, int cost, CreatureType creatureType, Effect[] effects, int atk, int def, List<CreatureProperty> properties, boolean tapped, int uid) {
		ItemStack item = null;

		if(type == Type.MANA) {
		    item = new ItemStack(Material.SAPLING);
        }else{
		    item =  new ItemStack(Material.PAPER);
        }

		ItemMeta itemMeta = item.getItemMeta();
		
		itemMeta.setDisplayName(Prefixes.CARD_NAME + name);
		List<String> lore = new ArrayList<>();

        lore.add("");

        if(type == Type.MANA) {
            lore.add(Prefixes.EFFECT_DESCRIPTION + "Ajoute " + cost + Prefixes.EFFECT_DESCRIPTION + " à votre mana");
        }else {
            lore.add(type + (creatureType != null ? ChatColor.GRAY + " (" + creatureType + ChatColor.GRAY + ")" : ""));
            lore.add(Prefixes.CREATURE_PROPERTY + "Coût : " + cost);

            if (properties != null && !properties.isEmpty()) {
                lore.add("");
                for (CreatureProperty p : properties) {
                    lore.add(p.toString());
                }
            }

            if (effects != null && effects.length != 0) {
                for (Effect e : effects) {
                    lore.add("");
                    lore.addAll(BukkitUtils.cut(e.getFullDescription()));
                }
            }

            if (tapped) {
                lore.add("");
                lore.add(ChatColor.RED + "" + ChatColor.ITALIC + "Cette carte ne peut plus attaquer ni");
                lore.add(ChatColor.RED + "" + ChatColor.ITALIC + "exécuter d'effets ce tour");
            }

            if (type == Type.CREATURE) {
                lore.add(" ");
                lore.add(Prefixes.CREATURE_PROPERTY + atk + "/" + def);
            }
        }

		itemMeta.setLore(lore);
		item.setItemMeta(itemMeta);


		if(uid >= 0) {
            item = ItemUIDUtils.addItemUID(item, uid);
        }
		
		return item;
	}

    /**
     * Returns a random card. If its a creature, it will fit the CreatureType argument. If its another card, it will return
     * one that has either no mana specification or at least this mana type
     * @param manaType the sorting type
     * @param creatureType the sorting creature type
     * @return a random card
     */
	public static Entity<? extends Card> getRandomCard(ManaType manaType, CreatureType creatureType) {
        Random r = new Random();
        int index = r.nextInt(Entity.allEntities.size());
        return Entity.allEntities.get(index);

	}

    /**
     * Either gives a random card fitting the player's affinity or nothing (depending on odds)
     * @param tgaPlayer the player
     */
    //ok
    public static void giveRandomCard(TGAPlayer tgaPlayer, FoundReason reason) {
        if(tgaPlayer == null || reason == null) return;

        if(tgaPlayer.getManaAffinity() == null || tgaPlayer.getCreatureAffinity() == null) {
            Logger.logWarning("Player " + tgaPlayer.getBukkitPlayer() + " activated an event which involved him to get a random card; aborted. (reason: unspecified mana or creature affinity)");
            return;
        }

        Random r = new Random();
        int result = r.nextInt(reason.getOdds());

        if(result == 1) {
            Entity<? extends Card> entity = getRandomCard(tgaPlayer.getManaAffinity(), tgaPlayer.getCreatureAffinity());

            tgaPlayer.getBukkitPlayer().sendMessage(ChatColor.AQUA + reason.getMessage() + " Vous recevez " + Prefixes.CARD_NAME + entity.getName() + ChatColor.AQUA + " !");
            tgaPlayer.addCardToList(entity);
        }
    }

}

