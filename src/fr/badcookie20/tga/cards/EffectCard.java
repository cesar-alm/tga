package fr.badcookie20.tga.cards;

import fr.badcookie20.tga.effect.Effect;
import fr.badcookie20.tga.exceptions.EffectException;
import fr.badcookie20.tga.player.TGAPlayer;

import java.util.ArrayList;
import java.util.List;

/**
 * Superclass of all cards that have effects
 */
public abstract class EffectCard extends CastCard {

	protected Effect[] effects;
	
	public EffectCard(Entity<? extends EffectCard> entity, int id, String name, Type type, Rarity rarity, int manaCost, Effect... effects) {
		super(entity, id, name, type, rarity, manaCost);
		
		this.effects = effects;
	}

    /**
     * Lance tous les effets de cette carte
     * @param p le joueur qui les possède sur son champ de bataille
     * @throws EffectException si jamais une erreur est rencontrée lors de l'exécution
     */
	public void executeAllEffects(TGAPlayer p) throws EffectException {
        for(Effect e : getEffects()) {
            e.execute(p, this);
        }
    }

    /**
     * Lance l'effet de cette carte qui est jouable à ce moment du tour.
     * Si jamais aucun effet ne correspond, cette fonction ne fait rien
     * @param p le joueur qui possède les cartes sur son champ de bataille
     * @param time le moment du tour
     * @throws EffectException si jamais une erreur est rencontrée lors de l'exécution
     */
    public void executeAllEffects(TGAPlayer p, Effect.ExecutionTime time) throws EffectException {
        for(Effect e : getEffects()) {
            if(e.getExecutionTime() == time) {
                e.execute(p, this);
            }
        }
    }

    /**
     * Retourne l'effet de cette carte qui est jouable à ce moment du tour.
     * Si jamais aucun effet ne correspond, cette fonction retourne <code>null</code>
     * N'exécute aucun effet
     * @param time le moment du tour
     * @return l'effet correspondant, ou <code>null</code>
     */
    public List<Effect> getAllEffects(Effect.ExecutionTime time) {
        // TODO rename
        List<Effect> tempEffects = new ArrayList<>();

        if(!hasEffect(time)) return null;

        for(Effect effect : effects) {
            if(effect.getExecutionTime() == time) tempEffects.add(effect);
        }

        return tempEffects;
    }

    /**
     * Indique si oui ou non cette carte possède un effet jouable à ce moment du tour.
     * @param time le moment du tour
     * @return <code>true</code> si oui
     */
    public boolean hasEffect(Effect.ExecutionTime time) {
        for(Effect effect : effects) {
            if(effect.getExecutionTime() == time) {
                return true;
            }
        }

        return false;
    }

    public void executeAllOpposites(TGAPlayer p) throws EffectException {
        for(Effect e : effects) {
            if(e.hasOpposite()) e.executeOpposite(p, this);
        }
    }

    /**
     * Retourne tous les effets de cette carte
     */
    public Effect[] getEffects() {
        return effects;
    }

}
