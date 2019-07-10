package fr.badcookie20.tga.cards;

import fr.badcookie20.tga.cards.mana.ManaAmount;
import fr.badcookie20.tga.effect.Effect;
import fr.badcookie20.tga.exceptions.EffectException;
import fr.badcookie20.tga.player.TGAPlayer;

/**
 * Superclass of all cards that have effects
 */
public abstract class EffectCard extends CastCard {

	protected Effect[] effects;
	
	public EffectCard(int id, String name, Type type, Rarity rarity, ManaAmount manaCost, Effect... effects) {
		super(id, name, type, rarity, manaCost);
		
		this.effects = effects;
	}

	public Effect[] getEffects() {
		return effects;
	}

	public void executeEffects(TGAPlayer p) throws EffectException {
        for(Effect e : getEffects()) {
            e.execute(p);
        }
    }

    public void executeEffect(TGAPlayer p, Effect.ExecutionTime time) throws EffectException {
        for(Effect e : getEffects()) {
            if(e.getExecutionTime() == time) {
                e.execute(p);
            }
        }
    }

    public Effect getEffect(Effect.ExecutionTime time) {
        if(!hasEffect(time)) return null;

        for(Effect effect : effects) {
            if(effect.getExecutionTime() == time) return effect;
        }

        return null;
    }

    public boolean hasEffect(Effect.ExecutionTime time) {
        for(Effect effect : effects) {
            if(effect.getExecutionTime() == time) {
                return true;
            }
        }

        return false;
    }

}
