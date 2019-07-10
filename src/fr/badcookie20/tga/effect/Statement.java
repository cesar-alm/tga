package fr.badcookie20.tga.effect;

import fr.badcookie20.tga.cards.Card;
import fr.badcookie20.tga.cards.CastCard;
import fr.badcookie20.tga.cards.creatures.CreatureCard;
import fr.badcookie20.tga.cards.creatures.CreatureType;
import fr.badcookie20.tga.exceptions.EffectException;
import fr.badcookie20.tga.player.BattleField;
import fr.badcookie20.tga.player.TGAPlayer;
import fr.badcookie20.tga.utils.CardUtils;
import fr.badcookie20.tga.utils.Logger;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class Statement {
	
	private StatementList statement;
	private Object[] args;
	
	public Statement(StatementList statement, Object... args) {
		this.statement = statement;
		this.args = args;
	}

	public boolean execute(TGAPlayer p) throws EffectException {
        return this.statement.execute(p, this.args);
    }

	/**
	 * Changes the attack and/or the defense of all the creatures in the battlefield of the specified player
	 * @param atk the amount of attack changed
	 * @param def the amount of defense changed
	 * @param creatureType if specified, only the creatures of the specifised type will be modified
	 * @param player if specified, will modify only the creatures in the battlefield of the player, otherwise it will
	 * modify it in all the battlefields
	 */
	public static boolean changeAttackDefValues(int atk, int def, CreatureType creatureType, TGAPlayer player) {
		try {
            BattleField battleField = player.getBattleField();
            List<CreatureCard> creatures = CardUtils.toCreatureCardList(battleField.getAllCardsInBattleField());
            List<CreatureCard> toUpdate = new ArrayList<>();

            for (CreatureCard creature : creatures) {
                if (creature.getCreatureType() != creatureType) return false;
                creature.increaseAtk(atk);
                creature.increaseDef(def);

                toUpdate.add(creature);
            }

            for(CreatureCard creature : toUpdate) {
                battleField.updateCard(creature);
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

    public static boolean invoke(boolean payCosts, Card c, BattleField.Location initialLocation, int id, boolean sacrify, TGAPlayer p) {
        if(!p.getBattleField().getCards(initialLocation).contains(c)) {
            p.sendImpossible("Vous ne possédez pas la carte dans votre main !");
            return false;
        }

        if(payCosts) {
            if(c instanceof CastCard) {
                if(!p.getBattleField().getMana().isEnough(((CastCard) c).getManaCost())) {
                    p.sendImpossible("Vous n'avez pas assez de mana pour invoquer cette carte !");
                    return false;
                }
            }
        }

        if(id != -1 && sacrify) {
            ((CreatureCard) CardUtils.getCard(id)).die(p.getBattleField(), null);
        }

        p.getBattleField().send(initialLocation, BattleField.Location.BATTLEFIELD, c, p);

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
		
		public boolean execute(TGAPlayer executing, Object... args) throws EffectException {
            try {
                if(args.length == 0) {
                    this.method.invoke(null, executing);
                    return false;
                }

                Object[] finalArgs = new Object[args.length + 1];

                int i = 0;
                for(Object arg : args) {
                    finalArgs[i] = arg;
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
