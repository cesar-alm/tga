package fr.badcookie20.tga.utils;

import fr.badcookie20.tga.Plugin;
import fr.badcookie20.tga.cards.Card;
import fr.badcookie20.tga.cards.Entity;
import fr.badcookie20.tga.cards.creatures.CreatureType;
import fr.badcookie20.tga.cards.mana.ManaType;
import fr.badcookie20.tga.inventories.manager.InventoriesManager;
import fr.badcookie20.tga.inventories.manager.InventoryType;
import fr.badcookie20.tga.player.TGAPlayer;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class ConfigUtils {

    private static final String PLAYERS_EXTENSION = "players";
    private static final String CARDS_LIST_EXTENSION = "cards";
    private static final String MANA_AFFINITY_EXTENSION = "manaa";
    private static final String CREATURE_AFFINITY_EXTENSION = "creaturea";

    private static final String FORBIDDEN_PREFIX = "forbidden";

    private static final String STATISTIC_EXTENSION = "stats";
    public static final String WON_EXTENSION = "won";
    public static final String LOST_EXTENSION = "lost";
    public static final String SURRENDERED_EXTENSION = "sur";
    public static final String WON_BY_SURRENDER_EXTENSION = "wbs";

    public static void increaseStatistic(TGAPlayer p, String extension, int previousValue) {
        Plugin.getInstance().reloadConfig();

        String path = PLAYERS_EXTENSION + "." + p.getBukkitPlayer().getDisplayName() + "." + STATISTIC_EXTENSION + "." + extension;
        FileConfiguration config = Plugin.getInstance().getConfig();

        config.set(path, previousValue + 1);

        Plugin.getInstance().saveConfig();

        InventoriesManager.getInstance().update(p, InventoryType.STATISTICS);
    }

    public static int getStatistic(TGAPlayer p, String extension) {
        Plugin.getInstance().reloadConfig();

        String path = PLAYERS_EXTENSION + "." + p.getBukkitPlayer().getDisplayName() + "." + STATISTIC_EXTENSION + "." + extension;
        FileConfiguration config = Plugin.getInstance().getConfig();

        if(!config.isSet(path)) {
            return 0;
        }

        return config.getInt(path);
    }

    /**
     * Adds a card to the cards list of the specified player
     * @param id the card id
     * @param p the player
     */
    public synchronized static void addCard(int id, TGAPlayer p) {
        String newCardList = writeCard(id, p);

        Plugin.getInstance().reloadConfig();

        FileConfiguration config = Plugin.getInstance().getConfig();
        config.set(PLAYERS_EXTENSION + "." + p.getBukkitPlayer().getDisplayName() + "." + CARDS_LIST_EXTENSION, newCardList);

        Plugin.getInstance().saveConfig();

        p.reloadCards();
    }

    /**
     * Returns the string which contains all the cards of the specified player, including the specified card. It doesn't
     * write anything in the configuration file
     * @param newId the new card id
     * @param p the player
     * @return a string
     */
    private synchronized static String writeCard(int newId, TGAPlayer p) {
        String cards = getCardsString(p);

        if(cards == null || cards.isEmpty()) {
            return newId + "_1|";
        }

        String newCards = "";
        boolean found = false;

        String[] s1 = cards.split("#");
        for(String card : s1) {
            String[] s2 = card.split("_");
            int id = Integer.parseInt(s2[0]);
            int amount = Integer.parseInt(s2[1]);

            if(id == newId) {
                found = true;
                newCards+=id + "_" + (amount+1);
            }else{
                newCards+=id + "_" + amount;
            }

            newCards+="#";
        }

        if(!found) {
            newCards+=newId + "_1#";
        }

        return newCards;
    }

    /**
     * Returns the card list of the specified player
     * @param p the player
     * @return the player's card list
     */
    public static List<Entity<? extends Card>> getCards(TGAPlayer p) {
        String cards = getCardsString(p);

        if(cards == null || cards.isEmpty()) return new ArrayList<>();

        List<Entity<? extends Card>> entityList = new ArrayList<>();

        String[] s1 = cards.split("#");

        for(String card : s1) {
            String[] s2 = card.split("_");

            int id = Integer.parseInt(s2[0]);
            int amount = Integer.parseInt(s2[1]);

            Entity<? extends Card> entity = Entity.getEntity(id);
            if(entity == null) {
                Logger.logError("Unknown card with id " + id);
                p.getBukkitPlayer().sendMessage(ChatColor.RED + "Error while loading your card list! (reason: unknown card with id " + id + ")");
                continue;
            }

            for(int i = amount; i>0; i--) {
                entityList.add(entity);
            }
        }

        return entityList;
    }

    /**
     * Returns the string that contains all the cards of the player
     * @param p the player
     * @return a string
     */
    private static String getCardsString(TGAPlayer p) {
        Plugin.getInstance().reloadConfig();

        FileConfiguration config = Plugin.getInstance().getConfig();

        String playerName = p.getBukkitPlayer().getName();
        String path = PLAYERS_EXTENSION + "." + playerName + "." + CARDS_LIST_EXTENSION;

        if(!config.isSet(path) || config.get(path) == null) {
            return null;
        }

        return (String) config.get(path);
    }

    /**
     * Returns the mana affinity of the specified player
     * @param p the player
     * @return the player's mana affinity
     */
    public static ManaType getManaAffinity(TGAPlayer p) {
        FileConfiguration config = Plugin.getInstance().getConfig();
        String path = PLAYERS_EXTENSION + "." + p.getBukkitPlayer().getDisplayName() + "." + MANA_AFFINITY_EXTENSION;

        if(!config.isSet(path) || config.get(path) == null) return null;

        int result = config.getInt(path);

        return ManaType.get(result);
    }

    /**
     * Sets the player's mana affinity to the specified one
     * @param p the player
     * @param type the new mana affinity
     */
    public static void setManaAffinity(TGAPlayer p, ManaType type) {
        Plugin.getInstance().reloadConfig();

        FileConfiguration config = Plugin.getInstance().getConfig();
        String path = PLAYERS_EXTENSION + "." + p.getBukkitPlayer().getDisplayName() + "." + MANA_AFFINITY_EXTENSION;

        config.set(path, type.getID());

        Plugin.getInstance().saveConfig();
    }

    /**
     * Returns the creature affinity of the specified player
     * @param p the player
     * @return the player's creature affinity
     */
    public static CreatureType getCreatureAffinity(TGAPlayer p) {
        FileConfiguration config = Plugin.getInstance().getConfig();
        String path = PLAYERS_EXTENSION + "." + p.getBukkitPlayer().getDisplayName() + "." + CREATURE_AFFINITY_EXTENSION;

        if(!config.isSet(path) || config.get(path) == null) return null;

        int result = config.getInt(path);

        return CreatureType.get(result);
    }

    /**
     * Sets the player's creature affinity to the specified one
     * @param p the player
     * @param type the new creature affinity
     */
    public static void setCreatureAffinity(TGAPlayer p, CreatureType type) {
        Plugin.getInstance().reloadConfig();

        FileConfiguration config = Plugin.getInstance().getConfig();
        String path = PLAYERS_EXTENSION + "." + p.getBukkitPlayer().getDisplayName() + "." + CREATURE_AFFINITY_EXTENSION;

        config.set(path, type.getID());

        Plugin.getInstance().saveConfig();
    }

    public static List<Entity<? extends Card>> getForbiddenEntities() {
        Plugin.getInstance().reloadConfig();

        FileConfiguration config = Plugin.getInstance().getConfig();
        String path = FORBIDDEN_PREFIX;

        if(!config.isSet(path) || config.get(path) == null) return new ArrayList<>();

        String cardsString = config.getString(path);

        if(cardsString.isEmpty() || !cardsString.contains("#")) return new ArrayList<>();

        List<Entity<? extends Card>> cards = new ArrayList<>();
        for(String s : cardsString.split("#")) {
            int id = Integer.parseInt(s);
            cards.add(Entity.getEntity(id));
        }

        return cards;
    }

    public static void addForbiddenCard(Card c) {
        Plugin.getInstance().reloadConfig();

        FileConfiguration config = Plugin.getInstance().getConfig();
        String path = FORBIDDEN_PREFIX;

        if(!config.isSet(path) || config.get(path) == null) {
            config.set(path, c.getID() + "#");
            return;
        }

        String cardsString = config.getString(path);
        cardsString += c.getID() + "#";
        config.set(path, cardsString);

        Plugin.getInstance().saveConfig();
    }

    public static void removeForbiddenCard(Card c) {
        Plugin.getInstance().reloadConfig();

        FileConfiguration config = Plugin.getInstance().getConfig();
        String path = FORBIDDEN_PREFIX;

        if(!config.isSet(path) || config.get(path) == null) {
            config.set(path, "");
            return;
        }

        String cardsString = config.getString(path);

        if(cardsString.isEmpty() || !cardsString.contains("#")) return;

        String newString = "";

        for(String s : cardsString.split("#")) {
            if(Integer.parseInt(s) != c.getID()) {
                newString+=s + "#";
            }
        }

        config.set(path, newString);

        Plugin.getInstance().saveConfig();
    }

    public static void emptyForbiddenCards() {
        Plugin.getInstance().reloadConfig();

        FileConfiguration config = Plugin.getInstance().getConfig();

        config.set(FORBIDDEN_PREFIX, "");

        Plugin.getInstance().saveConfig();
    }

    public static void erase(TGAPlayer choosenPlayer) {
        Plugin.getInstance().reloadConfig();

        String path = PLAYERS_EXTENSION + "." + choosenPlayer.getBukkitPlayer().getName();
        FileConfiguration config = Plugin.getInstance().getConfig();

        config.set(path, null);

        Plugin.getInstance().saveConfig();
    }
}
