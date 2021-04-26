package fr.badcookie20.tga.player;

import fr.badcookie20.tga.cards.Card;
import fr.badcookie20.tga.cards.Entity;
import fr.badcookie20.tga.cards.creatures.CreatureType;
import fr.badcookie20.tga.cards.mana.ManaType;
import fr.badcookie20.tga.inventories.admin.PlayerOptionsInventory;
import fr.badcookie20.tga.inventories.battle.YesCancelInventory;
import fr.badcookie20.tga.inventories.management.PlayersListInventory;
import fr.badcookie20.tga.inventories.manager.InventoriesManager;
import fr.badcookie20.tga.inventories.manager.InventoryType;
import fr.badcookie20.tga.utils.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static fr.badcookie20.tga.inventories.battle.BattleFieldInventory.FORCE_STOP;

public class TGAPlayer {

    private static Map<Player, TGAPlayer> registeredPlayers;

    public static final int SCOREBOARD_ENEMY_SLOT = 0;
    public static final int SCOREBOARD_LIFE_POINTS_SLOT = 1;
    public static final int SCOREBOARD_STATUS_SLOT = 2;
    private static final int SCOREBOARD_TURN_SLOT = 3;
    private static final int SCOREBOARD_TYPE_SLOT = 4;

    private static final String SCOREBOARD_CAT_NAME = ChatColor.GOLD + "";
    private static final String SCOREBOARD_CONT_NAME = ChatColor.GREEN + "";
    private static final String SCOREBOARD_ENEMY_CAT = SCOREBOARD_CAT_NAME + "Contre " + SCOREBOARD_CONT_NAME;
    private static final String SCOREBOARD_LIFE_POINTS_CAT = SCOREBOARD_CAT_NAME + "Points de vie : " + SCOREBOARD_CONT_NAME;
    private static final String SCOREBOARD_STATUS_CAT = SCOREBOARD_CAT_NAME + "Phase : " + SCOREBOARD_CONT_NAME;
    private static final String SCOREBOARD_TURN_CAT = SCOREBOARD_CAT_NAME + "Tour " + SCOREBOARD_CONT_NAME;
    private static final String SCOREBOARD_TYPE_CAT  = SCOREBOARD_CAT_NAME + "";

    public static final int DEFAULT_LIFE_POINTS = 6000;
    public static final int DEFAULT_MAX_TURNS = 60;
    public static final int DEFAULT_DAMAGE_MULTIPLIER = 10;

    static {
        registeredPlayers = new HashMap<>();
    }

    private Player p;

    private BattleField battlefield;
    private PotentialBattle potentialBattle;

    private List<Entity<? extends Card>> entityList;
    private ManaType manaAffinity;
    private CreatureType creatureAffinity;
    private IndividualScoreboard scoreboard;

    private Statistics stats;
    private boolean monitoring;

    /**
     * Constructor of the class
     * @param p the player represented by this object
     */
    public TGAPlayer(Player p) {
        registeredPlayers.put(p, this);

        this.p = p;
        this.battlefield = null;
        this.entityList = ConfigUtils.getCards(this);
        this.scoreboard = new IndividualScoreboard(p, ChatColor.AQUA + "Duel");

        this.manaAffinity = ConfigUtils.getManaAffinity(this);
        this.creatureAffinity = ConfigUtils.getCreatureAffinity(this);

        this.stats = Statistics.getStatistics(this);

        if(checkNew()) {
            p.sendMessage(ChatColor.AQUA + "Pour pouvoir commencer à jouer à TheGreatAffrontement, faites " + ChatColor.YELLOW + "/login");
            return;
        }

        for(Entity<? extends Card> entity : this.entityList) {
            if(ConfigUtils.getForbiddenEntities().contains(entity)) {
                p.sendMessage(ChatColor.RED + "Une ou plusieurs de vos cartes sont interdites, et donc non jouables. Regardez votre liste de cartes pour plus d'informations");
                return;
            }
        }

        this.monitoring = false;
    }

    /**
     * use this function when the player wishes to start a new battle.
     * HOW THE POTENTIAL BATTLE SYSTEM WORKS:
     * -through this function, the <code>PotentialBattle</code> is initialized for the player requesting a new battle (unless he cancels the operation before chosing an enemy)
     * -then a <code>PotentialBattle</code> is set for the targeted player (the targeted player is not notified, but the operation is needed to prevent other players to init a battle with the same target, which would be a problem...)
     * -then, the player who requested a battle chooses a battle type. If he cancels, he and the target lose the <code>PotentialBattle</code>
     * -then, the targeted player is asked to confirm the battle request. If he doesn't do it, all <code>PotentialBattle</code> are lost. If he does it, all <code>PotentialBattle</code> are lost and the battle starts.
     */
    public void initPotentialBattleProcess() {
        // We make sure it is
        this.getBukkitPlayer().closeInventory();

        // He selects a player
        this.getBukkitPlayer().sendMessage(ChatColor.AQUA + "Choisissez le joueur à affronter :");
        InventoriesManager.getInstance().handle(this, InventoryType.PLAYERS_LIST_WITH_STATISTICS);

        // Who did he choose ?
        ItemStack clickedPlayer = NextUtils.getLastClickedItemOf(this.getBukkitPlayer());
        if(clickedPlayer.equals(PlayersListInventory.CANCEL)) {
            return;
        }

        String playerName = ((SkullMeta) clickedPlayer.getItemMeta()).getOwner();
        TGAPlayer chosenEnemy = TGAPlayer.getPlayer(Bukkit.getPlayer(playerName));

        // Init the potential battles (see comment), and the next step
        this.setPotentialBattle(new PotentialBattle(chosenEnemy));
        chosenEnemy.setPotentialBattle(new PotentialBattle(this));

        InventoriesManager.getInstance().handle(this, InventoryType.BATTLE_CHOOSER);
    }

    /**
     * Sends a message to this instance, and also opens a confirmation inventory to this instance (asking if he accepts the <code>PotentialBattle</code> requested by the specified player, in the arguments)
     * WARNING : this function DOES NOT modify  <code>PotentialBattle</code>
     * @param sender the player that has sent the invitation
     */
    public void sendBattleRequest(TGAPlayer sender) {
        p.sendMessage(Prefixes.CREATURE_PROPERTY + sender.getPotentialBattle().getEnemy().getBukkitPlayer().getName() + ChatColor.AQUA + " vous a invité à se battre en duel !");

        InventoriesManager.handleAsync(this, InventoryType.ACCEPT_BATTLE);
    }

    /**
     * Adds a card to the player's list, modifies the config and updates the Management and CardList inventories
     * @param entity the entity to add
     */
    public void addCardToList(Entity<? extends Card> entity) {
        p.sendMessage(Prefixes.CARD_NAME + entity.getName() + ChatColor.AQUA + ChatColor.ITALIC + " a été ajoutée à votre collection");
        this.entityList.add(entity);
        ConfigUtils.addCard(entity.getID(), this);
        InventoriesManager.getInstance().update(this, InventoryType.CARD_LIST);
        InventoriesManager.getInstance().update(this, InventoryType.MANAGEMENT);
    }

    /**
     * Starts the scoreboard of the player represented by this object. It sets all the value in it to default ones
     * @param enemy the enemy of this player
     */
    private void initScoreboard(TGAPlayer enemy) {
        this.scoreboard.make();

        this.scoreboard.setLine(SCOREBOARD_ENEMY_SLOT, SCOREBOARD_ENEMY_CAT + enemy.getBukkitPlayer().getName());
        this.updateLifePoints();
        this.updateStatus();
        this.updateTurnNumber();
        this.scoreboard.setLine(SCOREBOARD_TYPE_SLOT, SCOREBOARD_TYPE_CAT + (this.getBattleField().getPersonalizationLevel() == 0 ? "Partie classique" : "Partie personnalisée"));
    }

    /**
     * Updates the life points line in the scoreboard
     */
    public void updateLifePoints() {
        this.scoreboard.setLine(SCOREBOARD_LIFE_POINTS_SLOT, SCOREBOARD_LIFE_POINTS_CAT + this.getBattleField().getLifePoints());
    }

    /**
     * Updates the status line in the scoreboard
     */
    public void updateStatus() {
        this.scoreboard.setLine(SCOREBOARD_STATUS_SLOT, SCOREBOARD_STATUS_CAT + this.getBattleField().getStatus());
    }

    /**
     * Updates the turn line in the scoreboard
     */
    public void updateTurnNumber() {
        this.scoreboard.setLine(SCOREBOARD_TURN_SLOT, SCOREBOARD_TURN_CAT + this.getBattleField().getTurnNumber() + SCOREBOARD_CAT_NAME + "/" + this.getBattleField().getMaxTurns());
    }

    /**
     * Reloads the statistics of this player by looking into config
     */
    private void reloadStats() {
        this.stats = Statistics.getStatistics(this);
    }

    /**
     * Returns the statistics of this player
     * @return the statistics of this player
     */
    public Statistics getStatistics() {
        return this.stats;
    }

    /**
     * Checks if this player is new or not
     * @return <code>true</code> if the player is new and <code>false</code> if not
     */
    public boolean checkNew() {
        return manaAffinity == null || creatureAffinity == null;
    }

    /**
     * Disconnects this player (but doesn't kick him). It stops the battle if there is one and destroys his scoreboard
     */
    public void disconnect() {
        if(this.potentialBattle != null) {
            this.potentialBattle.getEnemy().sendImpossible(ChatColor.YELLOW + this.getBukkitPlayer().getName() + " s'est deconnecté ! Vous ne pouvez plus faire de duel avec lui");
            NextUtils.updateClickedItemOf(this.potentialBattle.getEnemy().getBukkitPlayer(), FORCE_STOP);
        }

        if(this.battlefield != null) {
            stopBattle(this, this.battlefield.getEnemy(), StopReason.SURRENDERED);
        }

        destroyScoreboard();
    }

    /**
     * Sends a message to this player starting with red
     * @param string the message to be sent
     */
    public void sendImpossible(String string) {
        this.p.sendMessage(ChatColor.RED + string);
    }

    /**
     * Reloads the cards of this object by looking to the config file
     */
    public void reloadCards() {
        this.entityList = ConfigUtils.getCards(this);
    }

    /**
     * Destroys the scoreboard of this player
     */
    private void destroyScoreboard() {
        this.scoreboard.end();
    }

    /**
     * Returns the Bukkit player related to this object
     * @return the Bukkit player.
     */
    public Player getBukkitPlayer() {
        return this.p;
    }

    /**
     * Returns the battlefield of this player. If it's <code>null</code>, the player isn't fighting with any other player
     * @return the battlefield of this player
     */
    public BattleField getBattleField() {
        return this.battlefield;
    }

    /**
     * Returns <code>true</code> if the player is in battle, <code>false</code> otherwise
     */
    public boolean isInBattle() {
        return this.getBattleField() != null;
    }

    /**
     * Returns the cards list of this player
     * @param showManaCards will show or not the mana cards in the list
     * @return the cards list of the player
     */
    public List<Entity<? extends Card>> getCardsList(boolean showManaCards) {
        // TODO if showManaCards
        return entityList;
        // return CardUtils2.sortInverted(Card.Type.MANA, entityList);
    }

    /**
     * Returns the mana affinity of this player
     * @return the mana affinity
     */
    public ManaType getManaAffinity() {
        return this.manaAffinity;
    }

    /**
     * Sets the mana affinity only if it's <code>null</code>
     * @param type the new mana affinity
     */
    public void setManaAffinity(ManaType type) {
        if(this.manaAffinity == null) this.manaAffinity = type;
    }

    /**
     * Returns the creature affinity of this player
     * @return the creature affinity
     */
    public CreatureType getCreatureAffinity() {
        return this.creatureAffinity;
    }

    public boolean isMonitoring() {
        return monitoring;
    }

    public void invertMonitor() {
        this.monitoring = !monitoring;
    }

    /**
     * Sets the creature affinity only if it's <code>null</code>
     * @param type the new creature affinity
     */
    public void setCreatureAffinity(CreatureType type) { if(this.creatureAffinity == null) this.creatureAffinity = type; }

    public PotentialBattle getPotentialBattle() {
        return this.potentialBattle;
    }

    public void setPotentialBattle(PotentialBattle potentialBattle) {
        this.potentialBattle = potentialBattle;
    }

    public Card confirmTarget(Card.Type type) {
        p.sendMessage(ChatColor.GREEN + "Veillez choisir la carte cible :");

        if(type == null) {
            InventoriesManager.getInstance().handle(this, InventoryType.SELECT_TARGET_ALL);
        }else {
            switch (type) {
                case CREATURE:
                    InventoriesManager.getInstance().handle(this, InventoryType.SELECT_TARGET_CREATURE);
                    break;
                case ENCHANTMENT:
                    InventoriesManager.getInstance().handle(this, InventoryType.SELECT_TARGET_ENCHANTMENT);
                    break;
                default:
                    Logger.logError("Selector inventory for " + type + " type is unavailable");
                    break;
            }
        }

        return getLastSelectedTarget(type);
    }

    private Card getLastSelectedTarget(Card.Type type) {
        ItemStack result = NextUtils.getLastClickedItemOf(this.getBukkitPlayer());

        if(result.equals(YesCancelInventory.CANCEL)) {
            return null;
        }

        int clickedSlot = NextUtils.getLastClickedRawSlotOf(this.getBukkitPlayer());

        if(type == null) {
            return this.getBattleField().getEnemy().getBattleField().getCards(BattleField.Location.BATTLEFIELD).get(clickedSlot);
        }else{
            return CardUtils2.sort(this.getBattleField().getEnemy().getBattleField().getCards(BattleField.Location.BATTLEFIELD), type).get(clickedSlot);
        }
    }

    public boolean confirmAction(String message) {
        p.sendMessage(ChatColor.GREEN + "Confirmer : " + message + ChatColor.GREEN + " ?");

        InventoriesManager.getInstance().handle(this, InventoryType.YES_CANCEL);
        return getLastConfirmation();
    }

    public boolean getLastConfirmation() {
        ItemStack result = NextUtils.getLastClickedItemOf(p);

        if(result.equals(YesCancelInventory.YES)) {
            return true;
        }

        return false;
    }

    public void confirmActionOnPlayer(TGAPlayer choosenPlayer) {
        InventoriesManager.getInstance().handle(this, InventoryType.PLAYER_OPTIONS);
        getLastActionOnPlayer(choosenPlayer);
    }

    private void getLastActionOnPlayer(TGAPlayer choosenPlayer) {
        ItemStack result = NextUtils.getLastClickedItemOf(p);

        if(result.equals(InventoriesManager.GO_BACK)) {
            InventoriesManager.getInstance().handle(this, InventoryType.PLAYERS_LIST_ADMIN);
            return;
        }

        if(result.equals(PlayerOptionsInventory.BAN)) {
            // TODO: 05/11/2016
            this.sendImpossible(ChatColor.GREEN + "Le joueur " + ChatColor.YELLOW + choosenPlayer.getBukkitPlayer().getName() + ChatColor.GREEN + " a bien été banni");
        }else if(result.equals(PlayerOptionsInventory.ERASE_DATA)) {
            ConfigUtils.erase(choosenPlayer);
            this.sendImpossible(ChatColor.GREEN + "Les données de " + ChatColor.YELLOW + choosenPlayer.getBukkitPlayer().getName() + ChatColor.GREEN + " ont bien été effacées");
            choosenPlayer.sendImpossible("Vos données ont été effacées par un administrateur !");
            choosenPlayer.getBukkitPlayer().kickPlayer(ChatColor.RED + "Vous avez été kick par " + this.getBukkitPlayer().getName());
        }else if(result.equals(PlayerOptionsInventory.KICK)) {
            this.sendImpossible(ChatColor.GREEN + "Le joueur " + ChatColor.YELLOW + choosenPlayer.getBukkitPlayer().getName() + ChatColor.GREEN + " a bien été kick");
            choosenPlayer.getBukkitPlayer().kickPlayer(ChatColor.RED + "Vous avez été kick par " + this.getBukkitPlayer().getName());
        }
    }

    /**
     * Starts a new battle!
     * WARNING : this function RESETS the <code>PotentialBattle</code> for both players
     * This function sends messages, starts the scoreboard and the Battlefield Inventory
     * @param p1 player 1
     * @param p2 player 2
     */
    public static void createBattle(TGAPlayer p1, TGAPlayer p2) {
        PotentialBattle b = p2.getPotentialBattle();

        p1.setPotentialBattle(null);
        p2.setPotentialBattle(null);

        p1.getBukkitPlayer().sendMessage(ChatColor.GREEN + "Début du duel !");
        p2.getBukkitPlayer().sendMessage(ChatColor.GREEN + "Début du duel !");

        if(b.isModified()) {
            String messageAnnounce = ChatColor.GREEN + "Cette partie est personalisée : ";

            p1.sendImpossible(messageAnnounce);
            p2.sendImpossible(messageAnnounce);

            for(PersonalizingAttribute o : PersonalizingAttribute.values()) {
                String message = ChatColor.GOLD + "" + ChatColor.BOLD + o.getValueName() + ChatColor.GOLD + " : " + ChatColor.RESET + ChatColor.AQUA + b.get(o);
                p1.sendImpossible(message);
                p2.sendImpossible(message);
            }
        }

        if(!p1.equals(p2)) {
            int result  = new Random().nextInt(2);
            System.out.println("result= " + result);

            boolean starts;

            if(result == 0) {
                starts = true;
            }else{
                starts = false;
            }

            p1.battlefield = BattleField.getEmptyBattleField(p1, p2, starts, b);
            p2.battlefield = BattleField.getEmptyBattleField(p2, p1, !starts, b);
        }else{
            p1.battlefield = BattleField.getEmptyBattleField(p1, p2, true, b);
        }

        p1.initScoreboard(p2);
        if(!p1.equals(p2)) p2.initScoreboard(p1);

        p1.battlefield.initDeck(p1.entityList);
        if(!p1.equals(p2)) p2.battlefield.initDeck(p2.entityList);

        notify(p1.getBukkitPlayer().getName() + " et " + p1.getBukkitPlayer().getName() + " ont débuté un duel");

        InventoriesManager.handleAsync(p1, InventoryType.BATTLEFIELD);
        if(!p1.equals(p2)) InventoriesManager.handleAsync(p2, InventoryType.BATTLEFIELD);
    }

    public static void stopBattle(TGAPlayer winner, TGAPlayer looser, StopReason stopReason) {
        notify(winner.getBukkitPlayer().getName() + " et " + looser.getBukkitPlayer().getName() + " ont fini leur duel (raison=" + stopReason + ")");

        if(stopReason == StopReason.SURRENDERED) {
            winner.sendImpossible("Vous avez arrêté le duel");
            looser.sendImpossible("Votre adversaire a arrêté le duel");

            if(winner.getBattleField().isInStats()) {
                ConfigUtils.increaseStatistic(winner, ConfigUtils.SURRENDERED_EXTENSION, winner.stats.getSurrenderedGames());
                ConfigUtils.increaseStatistic(looser, ConfigUtils.WON_BY_SURRENDER_EXTENSION, winner.stats.getWonBySurrenderGames());
            }
        }else if(stopReason == StopReason.NATURAL || stopReason == StopReason.NATURAL_NOT_IN_STATS){
            winner.getBukkitPlayer().sendMessage(ChatColor.GREEN + "Félicitations, vous avez gagné la partie !");
            looser.sendImpossible("Vous avez perdu la partie !");

            if(stopReason == StopReason.NATURAL) {
                ConfigUtils.increaseStatistic(winner, ConfigUtils.WON_EXTENSION, winner.stats.getWonGames());
                ConfigUtils.increaseStatistic(looser, ConfigUtils.LOST_EXTENSION, winner.stats.getLostGames());
            }
        }else if(stopReason == StopReason.ADMIN) {
            winner.sendImpossible("Un administrateur a arrêté le duel");
            looser.sendImpossible("Un administrateur a arrêté le duel");
        }

        int id1 = winner.getBattleField().getID();
        int id2 = looser.getBattleField().getID();

        BattleField.remove(id1, id2);

        winner.battlefield = null;
        looser.battlefield = null;

        winner.destroyScoreboard();
        looser.destroyScoreboard();

        winner.getBukkitPlayer().getInventory().clear();
        looser.getBukkitPlayer().getInventory().clear();

        NextUtils.updateClickedItemOf(winner.getBukkitPlayer(), FORCE_STOP);
        NextUtils.updateClickedItemOf(looser.getBukkitPlayer(), FORCE_STOP);

        BattleField.Location.BATTLEFIELD.update(winner);
        BattleField.Location.BATTLEFIELD.update(looser);
        BattleField.Location.GRAVEYARD.update(winner);
        BattleField.Location.GRAVEYARD.update(looser);
        BattleField.Location.BANNED_CARDS.update(winner);
        BattleField.Location.BANNED_CARDS.update(looser);

        InventoriesManager.getInstance().update(winner, InventoryType.PLAYERS_LIST_STATISTICS);
        InventoriesManager.getInstance().update(looser, InventoryType.PLAYERS_LIST_STATISTICS);

        InventoriesManager.getInstance().update(winner, InventoryType.PLAYERS_LIST_WITH_STATISTICS);
        InventoriesManager.getInstance().update(looser, InventoryType.PLAYERS_LIST_WITH_STATISTICS);

        winner.reloadStats();
        looser.reloadStats();
    }

    public static TGAPlayer getPlayer(Player p) {
        return registeredPlayers.get(p);
    }

    public static void notify(String message) {
        registeredPlayers.values().stream().filter(p -> p.monitoring).forEach(p -> p.getBukkitPlayer().sendMessage(ChatColor.GOLD + "" + ChatColor.ITALIC + message));
    }

    /**
     * Breaks the links between TGAPlayers and Bukkit Players. It also destroys all possible battles. This method must
     * ONLY be used when a reload happens
     */
    public static void disconnectAllPlayers() {
        for(TGAPlayer p : registeredPlayers.values()) {
            if(p.getBattleField() != null) p.sendImpossible("Fin du duel (reload du serveur) ; cette partie ne compte pas dans les statistiques");

            p.getBukkitPlayer().closeInventory();
            p.getBukkitPlayer().getInventory().clear();
            p.destroyScoreboard();
        }
    }
}
