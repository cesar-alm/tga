package fr.badcookie20.tga.utils;

import fr.badcookie20.tga.cards.*;
import fr.badcookie20.tga.cards.Card.Type;
import fr.badcookie20.tga.cards.creatures.CreatureCard;
import fr.badcookie20.tga.cards.creatures.CreatureProperty;
import fr.badcookie20.tga.cards.creatures.CreatureType;
import fr.badcookie20.tga.cards.creatures.CreaturesList;
import fr.badcookie20.tga.cards.enchantment.EnchantmentList;
import fr.badcookie20.tga.cards.mana.ManaCard;
import fr.badcookie20.tga.cards.mana.ManaList;
import fr.badcookie20.tga.cards.mana.ManaType;
import fr.badcookie20.tga.cards.sorcery.SorceriesList;
import fr.badcookie20.tga.cards.sorcery.SorceryCard;
import fr.badcookie20.tga.effect.Effect;
import fr.badcookie20.tga.exceptions.EffectException;
import fr.badcookie20.tga.inventories.admin.ForbiddenInventory;
import fr.badcookie20.tga.player.TGAPlayer;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagInt;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CardUtils {

    private static final List<Card> cardsList;

    static {
        cardsList = new ArrayList<>();

        cardsList.addAll(CreaturesList.getCards());
        cardsList.addAll(EnchantmentList.getCards());
        cardsList.addAll(SorceriesList.getCards());
        cardsList.addAll(ManaList.getCards());
    }

	public static List<Card> sort(List<Card> cards, Card.Type type) {
		List<Card> list = new ArrayList<>();
		
		for(Card card : cards) {
			if(card.getType() == type) {
				list.add(card);
			}
		}
		
		return list;
	}

    public static List<? extends Card> sortInverted(Type type, List<Card> cardsList) {
        List<Card> list = new ArrayList<>();

        for(Card card : cardsList) {
            if(card.getType() != type) {
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

    public static Card getCard(int id) {
        for(Card c : cardsList) {
            if(c.getID() == id) return c;
        }

        return null;
    }
	
	public static ItemStack getItem(String name, Card.Type type, int cost, CreatureType creatureType, Effect[] effects, int atk, int def, List<CreatureProperty> properties, boolean tapped, int hashCode) {
		ItemStack item = new ItemStack(Material.PAPER);
		ItemMeta itemMeta = item.getItemMeta();
		
		itemMeta.setDisplayName(Prefixes.CARD_NAME + name);
		List<String> lore = new ArrayList<>();

        lore.add("");

		lore.add(type + (creatureType != null ? ChatColor.GRAY + " (" + creatureType + ChatColor.GRAY + ")" : ""));
		lore.add(Prefixes.CREATURE_PROPERTY + "Coût : " + cost);

        if(properties != null && !properties.isEmpty()) {
            lore.add("");
            for(CreatureProperty p : properties) {
                lore.add(p.toString());
            }
        }
		
		if(effects != null && effects.length != 0) {
			for(Effect e : effects) {
                lore.add("");
				lore.addAll(BukkitUtils.cut(e.getFullDescription()));
			}
		}

        if(tapped) {
            lore.add("");
            lore.add(ChatColor.RED + "" + ChatColor.ITALIC + "Cette carte ne peut plus attaquer ni");
            lore.add(ChatColor.RED + "" + ChatColor.ITALIC + "exécuter d'effets ce tour");
        }
		
		if(type == Type.CREATURE) {
            lore.add(" ");
			lore.add(Prefixes.CREATURE_PROPERTY + atk + "/" + def);
		}

		itemMeta.setLore(lore);
		item.setItemMeta(itemMeta);


		if(hashCode >= 0) {
            net.minecraft.server.v1_8_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);

            NBTTagCompound compound = nmsStack.hasTag() ? nmsStack.getTag() : new NBTTagCompound();
            compound.set("TGAItemHash", new NBTTagInt(hashCode));
            nmsStack.setTag(compound);

            item = CraftItemStack.asBukkitCopy(nmsStack);
        }

		
		return item;
	}

	public static void executeOpposite(EffectCard card, TGAPlayer p) throws EffectException {
		for(Effect e : card.getEffects()) {
            if(e.hasOpposite()) e.executeOpposite(p);
        }
	}

    public static List<Card> getAllCards() {
        return cardsList;
    }

    /**
     * Returns a random card. If its a creature, it will fit the CreatureType argument. If its another card, it will return
     * one that has either no mana specification or at least this mana type
     * @param manaType the sorting type
     * @param creatureType the sorting creature type
     * @return a random card
     */
	public static Card getRandomCard(ManaType manaType, CreatureType creatureType) {
        Random r = new Random();
        int result = r.nextInt(11);

        switch(result) {
            case 0:
            case 1:
            case 2:
            case 3:
                return getRandomCreature(creatureType);
            case 4:
            case 5:
            case 6:
                return getRandomEnchantment(manaType);
            case 7:
            case 8:
            case 9:
            case 10:
                return getRandomSorcery(manaType);
            default:
                return null;
        }
	}

    /**
     * Returns a random sorcery card that has either no mana specification or at least the specified mana type
     * @param manaType the sorting type
     * @return a random sorcery card
     */
    private static Card getRandomSorcery(ManaType manaType) {
        List<SorceryCard> c = SorceriesList.getCards();
        return getRandomCastCard(manaType, c);
    }

    /**
     * Returns a random mana card which fits the affinity of the player
     * @param manaAffinity the sorting type
     * @return a random mana card;
     */
    private static Card getRandomManaCard(ManaType manaAffinity) {
        List<ManaCard> c = ManaList.getCards();

        while(true) {
            int random = new Random().nextInt(c.size());

            return c.get(random);
        }
    }

    /**
     * Returns a random creature card of the specified type
     * @param creatureType the sorting type
     * @return a random creature card
     */
    private static Card getRandomCreature(CreatureType creatureType) {
        List<CreatureCard> c = CreaturesList.getCards();

        while(true) {
            int random = new Random().nextInt(CreaturesList.values().length);
            if (c.get(random).getCreatureType() == creatureType) {
                return c.get(random);
            }
        }
    }

    /**
     * Returns a random enchantment card that has either no mana specification or at least the specified mana type
     * @param manaType the sorting type
     * @return a random enchantment card
     */
    private static Card getRandomEnchantment(ManaType manaType) {
        List<EnchantmentCard> c = EnchantmentList.getCards();
        return getRandomCastCard(manaType, c);
    }

    /**
     * Returns a random cast card that has either no mana specification or at leat the specified mana type
     * @param manaType the sorting type
     * @param source the cast card list
     * @return a random cast card from the specified list
     */
    private static CastCard getRandomCastCard(ManaType manaType, List<? extends CastCard> source) {
        while(true) {
            int random = new Random().nextInt(source.size());
            return source.get(random);
        }
    }

    /**
     * Either gives a random card fitting the player's affinity or nothing (depending on odds)
     * @param tgaPlayer the player
     */
    public static void giveRandomCard(TGAPlayer tgaPlayer, FoundReason reason) {
        if(tgaPlayer == null || reason == null) return;

        if(tgaPlayer.getManaAffinity() == null || tgaPlayer.getCreatureAffinity() == null) {
            Logger.logWarning("Player " + tgaPlayer.getBukkitPlayer() + " activated an event which involved him to get a random card; aborted. (reason: unspecified mana or creature affinity)");
            return;
        }

        Random r = new Random();
        int result = r.nextInt(reason.getOdds());

        if(result == 1) {
            Card c;
            if(reason == FoundReason.XP_GAIN) {
                c = getRandomManaCard(tgaPlayer.getManaAffinity());
            }else{
                c = getRandomCard(tgaPlayer.getManaAffinity(), tgaPlayer.getCreatureAffinity());
            }

            tgaPlayer.getBukkitPlayer().sendMessage(ChatColor.AQUA + reason.getMessage() + " Vous recevez " + Prefixes.CARD_NAME + c.getName() + ChatColor.AQUA + " !");
            tgaPlayer.addCardToList(c);
        }
    }

    /**
     * Returns a new card instance that matches the {@link ItemStack} that the player has clicked.
     * Warning: it returns a new instance. It is not the actual card the player has in his battlefield, hand, etc.
     * Should be used in generic inventories (ex: statistics)
     * Should not be used in battlefields (see getActualCard)
     * @param clicked the clicked {@link ItemStack}
     * @return a new card instance.
     */
    public static Card getCard(ItemStack clicked) {
        // todo: get the actual card (not another instance)

        String name = BukkitUtils.removeFirstColor(clicked.getItemMeta().getDisplayName());

        if(name.contains(ForbiddenInventory.ALLOWED)) {
            name = name.replace(ForbiddenInventory.ALLOWED, "");
        }else if(name.contains(ForbiddenInventory.FORBIDDEN)) {
            name = name.replace(ForbiddenInventory.FORBIDDEN, "");
        }

        for(Card c : cardsList) {
            if(name.equals("Mana")) {
                if(c.getName().contains("Mana") || c.getName().equals("Mana")) {
                    if(c.get().getItemMeta().getLore().get(0).equals(clicked.getItemMeta().getLore().get(0))) {
                        return c;
                    }else{
                        continue;
                    }
                }
            }

            if(c.getName().equals(name)) {
                return c;
            }
        }

        return null;
    }
}

