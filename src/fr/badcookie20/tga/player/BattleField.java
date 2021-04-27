package fr.badcookie20.tga.player;

import fr.badcookie20.tga.Plugin;
import fr.badcookie20.tga.cards.*;
import fr.badcookie20.tga.cards.creatures.Creature;
import fr.badcookie20.tga.cards.creatures.CreatureCard;
import fr.badcookie20.tga.cards.mana.Mana;
import fr.badcookie20.tga.cards.mana.ManaCard;
import fr.badcookie20.tga.cards.sorcery.Sorcery;
import fr.badcookie20.tga.cards.sorcery.SorceryCard;
import fr.badcookie20.tga.effect.Effect;
import fr.badcookie20.tga.effect.EffectHook;
import fr.badcookie20.tga.exceptions.EffectException;
import fr.badcookie20.tga.inventories.manager.InventoriesManager;
import fr.badcookie20.tga.inventories.manager.InventoryType;
import fr.badcookie20.tga.utils.BukkitUtils;
import fr.badcookie20.tga.utils.Prefixes;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BattleField {

    private static final int MAX_SAME_CARD = 3;
    private static final int MAX_SAME_CARD_MANA = 5;
    private static final int MAX_DRAWING = 7;
    private static final int MAX_CARD_ON_BATTLEFIELD = 18;

    private static final List<BattleField> battleFields;

    static {
        battleFields = new ArrayList<>();
    }

    private final PotentialBattle potentialBattle;

    private List<Card> leftInDeck;
	private List<Card> onBattleField;
	private List<Card> inGraveyard;
	private List<Card> banned;

    private final List<Card> inHand;

    private int totalMana;
    private int tokens;

	private EffectHook effectHook;
    private int lifePoints;

    private int initialLifePoints;
    private int maxTurns;
    private double damageMultiplier;
    private boolean inStats;

    private TGAPlayer owner;
    private TGAPlayer enemy;

    private byte personalizationLevel;
    private Status status;
    private int id;
    private int turnNumber;

    private boolean selfBattle;

    /**
     * Creates a new battle
     * @param owner the players linked to this battlefield.
     * @param enemy its enemy.
     * @param ownerStarts if the player linked to this battlefield starts the game.
     * @param b the {@link PotentialBattle} corresponding to this battle
     *          (in particular, it contains the personalized data of the battle)
     */
    private BattleField(TGAPlayer owner, TGAPlayer enemy, boolean ownerStarts, PotentialBattle b) {
        this.potentialBattle = b;

		this.leftInDeck = new ArrayList<>();
		this.onBattleField = new ArrayList<>();
		this.inGraveyard = new ArrayList<>();
		this.banned = new ArrayList<>();
		this.inHand = new ArrayList<>();

        this.effectHook = new EffectHook();
        this.lifePoints = (Integer) b.get(PersonalizingAttribute.LIFE_POINTS);
        this.initialLifePoints = (Integer) b.get(PersonalizingAttribute.LIFE_POINTS);
        this.maxTurns = (Integer) b.get(PersonalizingAttribute.MAX_TURNS);
        this.damageMultiplier = ((Integer) b.get(PersonalizingAttribute.DAMAGE_MULTIPLIER) * PersonalizingAttribute.DAMAGE_MULTIPLIER.getMultiplier());
        this.inStats = (Boolean) b.get(PersonalizingAttribute.STATISTICS_INCREASE);

        this.owner = owner;
        this.enemy = enemy;

        this.totalMana = 0;
        this.tokens = 0;

        if(ownerStarts) {
            this.status = Status.TURN_PART_0;
        }else{
            this.status = Status.NOT_PLAYING;
        }

        if(b.isModified()) {
            this.personalizationLevel = 1;
        }else{
            this.personalizationLevel = 0;
        }
        this.id = Plugin.getInstance().newBattleField();
        this.turnNumber = 1;

        selfBattle = owner.equals(enemy);

        battleFields.add(this);
	}
	
	public List<Card> getAllCardsInBattleField() {
		return this.onBattleField;
	}
	
	public List<Card> getCards(Location loc) {
		switch(loc) {
		case BANNED_CARDS:
			return banned;
		case BATTLEFIELD:
			return onBattleField;
		case DECK:
			return leftInDeck;
		case GRAVEYARD:
			return inGraveyard;
		case HAND:
			return inHand;
		default:
			return null;
		}
	}

	public boolean hasCard(Location loc, Card c) {
        return getCards(loc).contains(c);
    }

    public boolean hasEntity(Location loc, Entity<? extends Card> entity) {
        List<Card> cardsInLoc = getCards(loc);
        for(Card card : cardsInLoc) {
            if (card.getEntity().equals(entity)) {
                return true;
            }
        }

        return false;
    }

    public Card getCardByEntity(Location loc, Entity<? extends Card> entity) {
        List<Card> cardsInLoc = getCards(loc);
        for(Card card : cardsInLoc) {
            if (card.getEntity().equals(entity)) {
                return card;
            }
        }

        return null;
    }

    public Status getStatus() {
        return status;
    }

    /**
     * Sends a card from a location to another
     * @param from the previous location
     * @param to the final location
     * @param card the card
     */
    public void send(Location from, Location to, Card card, boolean reopenInventories) {
        this.removeCard(from, card);
        this.addCard(to, card);

        Location.BATTLEFIELD.update(owner);
        Location.BATTLEFIELD.update(enemy);

        if(!reopenInventories) {
            return;
        }

        InventoriesManager.handleAsync(owner, InventoryType.BATTLEFIELD);

        if (!selfBattle) {
            InventoriesManager.handleAsync(enemy, InventoryType.BATTLEFIELD);
        }
    }
	
	public void removeCard(Location from, Card card) {
        if(from == Location.BATTLEFIELD && card instanceof EffectCard) {
            try {
                ((EffectCard) card).executeAllOpposites(this.owner);
            }catch(EffectException ex) {
                ex.printStackTrace();
            }
        }

		this.getCards(from).remove(card);
        from.update(owner);
	}

    /**
     * Adds card to the specified location on the battlefield (doesn't take care of deleting of the previous location !)
     * Adds/removes mana if necessary
     * @param to the location the card is going
     * @param card the card
     */
	public void addCard(Location to, Card card) {
        if(to == Location.BATTLEFIELD && card instanceof EffectCard) {
            try {
                ((EffectCard) card).executeAllEffects(this.owner, Effect.ExecutionTime.ON_INVOKE);
                ((EffectCard) card).executeAllEffects(this.owner, Effect.ExecutionTime.DURING_CARDLIFE);
            } catch (EffectException e) {
                e.printStackTrace();
            }
        }

        if(to == Location.HAND) {
            this.getCards(to).add(card);
        }

        if(to == Location.BATTLEFIELD) {
            if(card instanceof ManaCard) {
                ManaCard c = (ManaCard) card;

                this.totalMana += c.getAmount();

                this.enemy.sendImpossible(ChatColor.YELLOW + this.owner.getBukkitPlayer().getName() + ChatColor.AQUA + " a ajouté " + ChatColor.GREEN + c.getAmount() + ChatColor.AQUA + " mana");

                return;
            }

            if(card instanceof CastCard) {
                // We do this here. Keep in mind that one of the next blocks is executed too.
                this.totalMana -= ((CastCard) card).getManaCost();
            }

            if(card instanceof CreatureCard) {
                this.getCards(Location.BATTLEFIELD).add(card);
            }

            if(card instanceof SorceryCard) {
                this.getCards(Location.GRAVEYARD).add(card);
            }

            if(card instanceof EnchantmentCard) {
                this.getCards(Location.BATTLEFIELD).add(card);
            }
        }

        if(to == Location.GRAVEYARD) {
            this.getCards(to).add(card);
        }

        to.update(owner);
        Location.BATTLEFIELD.update(owner);
        Location.GRAVEYARD.update(owner);
    }

    /**
     * Takes care of updating the hand of the player
     */
    public void updateHand() {
        Inventory inv = this.owner.getBukkitPlayer().getInventory();

        for(int i = 0; i <=8; i++) {
            inv.setItem(i, BukkitUtils.createItemStack(Material.AIR, null, null));
        }

        for(int i = 1; i<=this.inHand.size(); i++) {
            inv.setItem(i, this.inHand.get(i - 1).createItemStack());
        }
    }

    public void addTokens(int token) {
        this.tokens += tokens;
    }

    public int getTokens() {
        return this.tokens;
    }

    public int getInitialLifePoints() {
        return initialLifePoints;
    }

    public PotentialBattle getPotentialBattle() {
        return this.potentialBattle;
    }

    public boolean isCard(Location loc, Card card) {
		return getCards(loc).contains(card);
	}
	
	public void updateCard(Card card) {
        List<Card> finalCards = new ArrayList<>();

		for(Card inBattleField : this.onBattleField) {
			if(card.isSimilar(inBattleField)) {
				finalCards.add(card);
			}else{
                finalCards.add(inBattleField);
            }
		}

		this.onBattleField = finalCards;
	}

    public void initDeck(List<Entity<? extends Card>> allCards) {
        List<Entity<? extends Card>> finalCards = new ArrayList<>();

        for(Entity<? extends Card> entity : allCards) {
            if(entity instanceof Mana) {
                if(Collections.frequency(finalCards, entity) <= MAX_SAME_CARD_MANA) {
                    finalCards.add(entity);
                }
            }else{
                if(Collections.frequency(finalCards, entity) <= MAX_SAME_CARD) {
                    finalCards.add(entity);
                }
            }
        }

        this.leftInDeck = Entity.generateAllNewCards(finalCards);

        // for(int i = MAX_DRAWING; i > 0; i--) {
        drawCard();
        // }
    }
	
	public void updateCardsInBattleField(List<? extends Card> cards) {
		for(Card card : cards) {
			this.updateCard(card);
		}
	}
	
	public void drawCard() {
        // TODO: 11/09/2016
        // this.addCard(Location.HAND, this.leftInDeck.remove(0));

        this.addCard(Location.HAND, Creature.IMMORTAL_0.generateNewCard());
        this.addCard(Location.HAND, Creature.IMMORTAL_1.generateNewCard());
        this.addCard(Location.HAND, Sorcery.DAMAGE.generateNewCard());
        this.addCard(Location.HAND, Creature.SAMPLE.generateNewCard());
        this.addCard(Location.HAND, Mana.MANA_5.generateNewCard());
        this.addCard(Location.HAND, Mana.MANA_5.generateNewCard());
	}

    public EffectHook getEffectHook() {
        return this.effectHook;
    }

    public boolean isInStats() {
        return this.inStats;
    }

    /**
     * Damages the owner of the battlefield. The value is multiplied IN the method
     * @param i the amount of damage
     * @param damager the damager
     */
    public void damage(int i, TGAPlayer damager) {
        String multiplierSuffix = this.damageMultiplier == 1 ? "" : ChatColor.GRAY + " (multiplicateur de dégats : " + ChatColor.GOLD + "x" + this.damageMultiplier + ChatColor.GRAY + ")";

        this.owner.sendImpossible(ChatColor.YELLOW + damager.getBukkitPlayer().getName() + Prefixes.ATK_DEF_MODIF + " vous a retiré " + ChatColor.RED + i*this.damageMultiplier + " points de vie" + multiplierSuffix);
        this.enemy.sendImpossible(ChatColor.GREEN + "Vous avez retiré " + ChatColor.AQUA + i*this.damageMultiplier + " points de vie" + multiplierSuffix);

        lifePoints-= (i*damageMultiplier);
        if(damager.getBattleField().getEffectHook().gainDamaged) {
            damager.getBattleField().increaseLifePoints(i);
            this.owner.updateLifePoints();
        }

        this.owner.updateLifePoints();

		if(lifePoints <= 0) {
            StopReason stopReason;
            if(inStats) {
                stopReason = StopReason.NATURAL;
            }else{
                stopReason = StopReason.NATURAL_NOT_IN_STATS;
            }

            TGAPlayer.stopBattle(this.enemy, this.owner, stopReason);
        }
    }

    public void increaseLifePoints(int i) {
        this.lifePoints+=i;
        this.owner.updateLifePoints();
    }
	
	public static BattleField getEmptyBattleField(TGAPlayer owner, TGAPlayer ennemy, boolean ownerStarts, PotentialBattle b) {
		return new BattleField(owner, ennemy, ownerStarts, b);
	}

    public TGAPlayer getEnemy() {
        return enemy;
    }

    public int getMana() {
        return totalMana;
    }

    /**
     * Modifies the status of the BattleField. If you wish to change the playing player, ONLY execute this method with
     * parameter {@link BattleField.Status} NOT_PLAYING. This method also updates the battlefield inventory of its owner
     * but doesn't handle it UNLESS the new status is TURN_PART_0 (which must at no time be called by any method but
     * this one)
     * @param newStatus the new status of the battlefield
     */
    public void setStatus(Status newStatus) {
        this.status = newStatus;

        boolean open = false;
        Status newEnemyStatus = null;

        if(newStatus == Status.NOT_PLAYING) {
            this.getCards(Location.BATTLEFIELD).stream().filter(c -> c instanceof CreatureCard).forEach(c -> ((CreatureCard) c).resetAttack());

            this.turnNumber++;

            if(this.turnNumber == this.maxTurns) {
                TGAPlayer winner = this.getLifePoints() >= this.getEnemy().getBattleField().getLifePoints() ? this.getOwner() : this.enemy;
                TGAPlayer looser = winner.equals(this.getOwner()) ? this.enemy : this.getOwner();

                TGAPlayer.stopBattle(winner, looser, StopReason.NATURAL);
                return;
            }

            newEnemyStatus = Status.TURN_PART_0;
        }

        if(newStatus == Status.TURN_PART_0) {
            this.owner.getBukkitPlayer().sendMessage(ChatColor.BOLD + "" + ChatColor.GREEN + "C'est à votre tour de jouer !");

            this.getCards(Location.BATTLEFIELD).stream().filter(c -> c instanceof CreatureCard).forEach(c -> ((CreatureCard) c).resetDef());
            this.turnNumber++;
            open = true;
        }

        this.owner.updateStatus();
        this.owner.updateTurnNumber();
        Location.BATTLEFIELD.update(owner);

        if(open) {
            this.owner.getBukkitPlayer().closeInventory();
            InventoriesManager.handleAsync(this.owner, InventoryType.BATTLEFIELD);
        }

        if(newEnemyStatus != null) {
            Status status = newEnemyStatus;
            // TODO Why async?
            Bukkit.getScheduler().runTaskAsynchronously(Plugin.getInstance(), () -> this.enemy.getBattleField().setStatus(status));
        }
    }

    public byte getPersonalizationLevel() {
        return personalizationLevel;
    }

    public boolean canAddOnBattleField() {
        return this.onBattleField.size() + 1 <= MAX_CARD_ON_BATTLEFIELD;
    }

    public int getLifePoints() {
        return lifePoints;
    }

    public static List<BattleField> getBattleFields() {
        return battleFields;
    }

    public static BattleField getBattleField(int id) {
        for(BattleField battleField : battleFields) {
            if(battleField.getID() == id) {
                return battleField;
            }
        }

        return null;
    }

    public static List<BattleField> getBattleFieldsOnce() {
        List<TGAPlayer> alreadyCalled = new ArrayList<>();
        List<BattleField> finalList = new ArrayList<>();

        for(BattleField battleField : battleFields) {
            if(alreadyCalled.contains(battleField.getOwner()) || alreadyCalled.contains(battleField.getEnemy())) {
                continue;
            }

            alreadyCalled.add(battleField.getOwner());
            alreadyCalled.add(battleField.getEnemy());
            finalList.add(battleField);
        }

        return finalList;
    }

    public TGAPlayer getOwner() {
        return owner;
    }

    public int getID() {
        return id;
    }

    public static void stopAll() {
        for(BattleField battleField : getBattleFieldsOnce()) {
            TGAPlayer.stopBattle(battleField.getOwner(), battleField.getEnemy(), StopReason.ADMIN);
        }
    }

    public static void remove(Integer... ids) {
        List<BattleField> toRemove = new ArrayList<>();

        for(int i : ids) {
            for(BattleField battleField : battleFields) {
                if(battleField.getID() == i) {
                    toRemove.add(battleField);
                }
            }
        }

        battleFields.removeAll(toRemove);
    }

    public int getTurnNumber() {
        return turnNumber;
    }

    public int getMaxTurns() {
        return maxTurns;
    }

    public List<Card> getInHand() {
        return inHand;
    }

    public boolean isSelfBattle() {
        return selfBattle;
    }

    public enum Location {
		GRAVEYARD(InventoryType.GRAVEYARD),
		HAND(null),
		BATTLEFIELD(InventoryType.BATTLEFIELD),
		BANNED_CARDS(InventoryType.BANNED_CARDS),
		DECK(null);

        private InventoryType type;

        Location(InventoryType type) {
            this.type = type;
        }

        public void update(TGAPlayer p) {
            if(type == null) {
                // hand
                p.getBattleField().updateHand();
            }else{
                // not hand
                InventoriesManager.getInstance().update(p, type);
            }
        }
    }

    public enum Status {
        TURN_PART_0("Invocation (1)"),
        TURN_BATTLE("Combat"),
        TURN_PART_2("Invocation (2)"),
        NOT_PLAYING("/");

        private String s;

        Status(String s) {
            this.s = s;
        }

        @Override
        public String toString() {
            return s;
        }

        public boolean canInvoke() {
            return this == TURN_PART_0 || this == TURN_PART_2;
        }
    }

}
