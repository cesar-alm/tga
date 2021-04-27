package fr.badcookie20.tga.effect;

import fr.badcookie20.tga.cards.Card;
import fr.badcookie20.tga.cards.CastCard;
import fr.badcookie20.tga.cards.Entity;
import fr.badcookie20.tga.cards.FooCard;
import fr.badcookie20.tga.cards.creatures.CreatureCard;
import fr.badcookie20.tga.cards.creatures.CreatureType;
import fr.badcookie20.tga.exceptions.EffectException;
import fr.badcookie20.tga.player.BattleField;
import fr.badcookie20.tga.player.TGAPlayer;
import fr.badcookie20.tga.utils.CardUtils;
import fr.badcookie20.tga.utils.Logger;

import java.lang.reflect.Method;
import java.util.List;

public class Statement {
	
	private StatementList statement;
	private Object[] args;
	
	public Statement(StatementList statement, Object... args) {
		this.statement = statement;
		this.args = args;
	}

	public boolean execute(TGAPlayer p, Card source) throws EffectException {
        return this.statement.execute(p, source, this.args);
    }

	/**
	 * Changes the attack and/or the defense of all the creatures in the battlefield
	 * @param atk the amount of attack changed
	 * @param def the amount of defense changed
	 * @param creatureType if specified, only the creatures of the specified type will be modified
     * @param who 0 to change everyone's cards; 1 to change the player's cards; 2 to change the enemy's cards
     * @param cardExecutingEffect the card that launches this effect (or null if not applicable)
     * @param includeCard if <code>false</code> and cardExecutingEffect is specified, will not apply these changes to this card.
	 * @param player the player launching the effect
	 */
	public static boolean changeAttackDefValues(int atk, int def, CreatureType creatureType, int who, Card cardExecutingEffect, boolean includeCard, TGAPlayer player) {
        BattleField battleField = player.getBattleField();

        if(battleField.isSelfBattle()) {
            who = 1;
        }

		try {
		    if(who == 2) {
		        // let's be smart
		        return changeAttackDefValues(atk, def, creatureType, 1, null, true, battleField.getEnemy());
            }

		    List<CreatureCard> creatures = CardUtils.toCreatureCardList(battleField.getAllCardsInBattleField());

		    for (CreatureCard creature : creatures) {
		        if(creature.getCreatureType() != creatureType) continue;
		        if(!includeCard && cardExecutingEffect != creature) {
                    creature.increaseAtk(atk);
                    creature.increaseDef(def);
                }
		    }

		    if(who == 0) {
		        // We also do it for the enemy
		        return changeAttackDefValues(atk, def, creatureType, 1, null, true, battleField.getEnemy());
            }
        }catch(Exception ex) {
            ex.printStackTrace();
        }

        return true;
	}
	
	public static boolean enableGainDamaged(TGAPlayer p) {
		p.getBattleField().getEffectHook().gainDamaged = true;
        return true;
	}

    public static boolean disableGainDamaged(TGAPlayer p) {
        p.getBattleField().getEffectHook().gainDamaged = false;
        return true;
    }

	public static boolean increaseLifePoints(int amount, TGAPlayer p) {
        p.getBattleField().increaseLifePoints(amount);
        return true;
    }

    public static boolean damageEnemy(int amount, TGAPlayer p) {
        p.getBattleField().getEnemy().getBattleField().damage(amount, p);
        return true;
    }

    public static boolean invoke(boolean payCosts, Entity<? extends Card> entityToBeInvoked, BattleField.Location entityToBeInvokedLocation, Card cardExecutingEffect, boolean sacrify, TGAPlayer p) {
        if(!p.getBattleField().hasEntity(entityToBeInvokedLocation, entityToBeInvoked)) {
            p.sendImpossible("Vous ne possédez pas la carte à l'endroit nécessaire !");
            return false;
        }

        Card c = p.getBattleField().getCardByEntity(entityToBeInvokedLocation, entityToBeInvoked);

        if(payCosts) {
            if(c instanceof CastCard) {
                if(p.getBattleField().getMana() < ((CastCard) c).getManaCost()) {
                    p.sendImpossible("Vous n'avez pas assez de mana pour invoquer cette carte !");
                    return false;
                }
            }
        }

        if(sacrify) {
            if(cardExecutingEffect == null) {
                throw new NullPointerException("Cannot sacrifice a card if it is not specified");
            }else{
                if(cardExecutingEffect instanceof CreatureCard) {
                    // May have special behaviour
                    ((CreatureCard) cardExecutingEffect).die(p.getBattleField(), false);
                }else{
                    p.getBattleField().send(BattleField.Location.BATTLEFIELD, BattleField.Location.GRAVEYARD, cardExecutingEffect, false);
                }
            }
        }

        p.getBattleField().send(entityToBeInvokedLocation, BattleField.Location.BATTLEFIELD, c, false);

        return true;
    }

    public static boolean destroyCards(List<Card.Type> types, TGAPlayer p) {
	    // TODO
	    return false;
    }

	public enum StatementList {
        INVOKE("invoke"),
		CHANGE_ATK_DEF("changeAttackDefValues"),
		GAIN_DAMAGED("enableGainDamaged"),
        UNGAIN_DAMAGED("disableGainDamaged"),
		INCREASE_LIFE_POINTS("increaseLifePoints"),
        DAMAGE_ENEMY("damageEnemy"),
		DESTROY_CARDS("destroyCards");
		
		private Method method;
		
		private StatementList(String methodName) {
			Method[] methods = Statement.class.getDeclaredMethods();
			
			for(Method m : methods) {
				if(m.getName().equals(methodName)) {
					this.method = m;
				}
			}
			
			if(method == null) Logger.logError("Couldn't find method for " + methodName + "!");
		}
		
		public boolean execute(TGAPlayer executing, Card source, Object... args) throws EffectException {
            try {
                if(args.length == 0) {
                    this.method.invoke(null, executing);
                    return false;
                }

                Object[] finalArgs = new Object[args.length + 1];

                int i = 0;
                for(Object arg : args) {
                    if(arg instanceof FooCard) {
                        finalArgs[i] = source;
                    }else {
                        finalArgs[i] = arg;
                    }
                    i++;
                }

                finalArgs[i] = executing;

                return (boolean) this.method.invoke(null, finalArgs);
            }catch(Exception ex) {
                ex.printStackTrace();
                throw new EffectException("An unexpected exception occured while executing effect " + this.method.getName());
            }
		}
	}
	
}
