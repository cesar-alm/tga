package fr.badcookie20.tga.effect;

import fr.badcookie20.tga.cards.Card;
import fr.badcookie20.tga.exceptions.EffectException;
import fr.badcookie20.tga.player.TGAPlayer;
import fr.badcookie20.tga.utils.Prefixes;
import net.md_5.bungee.api.ChatColor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Effect {

	private final ExecutionTime type;
	private final String description;
	private final int extraCost;
	private final List<Statement> statements;
	private final List<Statement> opposites;

	/**
	 * Constructor of an effect
	 * @param type when can it be executed?
	 * @param description its description
	 * @param extraCost the extra cost of the effect, when executing it (<code>null</code> if no extra cost)
	 * @param statements statements executed when invoked
	 * @param opposites statements executed when card destroyed (use for life-card effects)
	 */
	public Effect(ExecutionTime type, String description, int extraCost, List<Statement> statements, List<Statement> opposites) {
		this.type = type;
		this.description = description;
		this.extraCost = extraCost;

		if(statements == null) {
			this.statements = new ArrayList<>();
		}else {
			this.statements = statements;
		}

		if(opposites == null) {
			this.opposites = new ArrayList<>();
		}else{
			this.opposites = opposites;
		}
	}

	/**
	 * Constructor of an effect
	 * @param type when can it be executed?
	 * @param description its description
	 * @param extraCost the extra cost of the effect, when executing it (<code>null</code> if no extra cost)
	 * @param statement statement executed when invoked
	 * @param opposite statements executed when card destroyed (use for life-card effects)
	 */
	public Effect(ExecutionTime type, String description, int extraCost, Statement statement, Statement opposite) {
		this(type, description, extraCost, Collections.singletonList(statement), Collections.singletonList(opposite));
	}
	
	public ExecutionTime getExecutionTime() {
		return this.type;
	}
	
	public String getDescription() {
		return this.description;	
	}
	
	public String getFullDescription() {
		return this.type.getCustomName() + (this.extraCost > 0 ? ChatColor.GRAY + "(" + this.extraCost + ChatColor.GRAY + ") " : "") + Prefixes.EFFECT_DESCRIPTION + this.description;
	}
	
	public int getExtraCost() {
		return this.extraCost;
	}
	
	public boolean executeAll(TGAPlayer p, Card source) throws EffectException {
        if(this.extraCost > 0) {
            if(p.getBattleField().getMana() < extraCost) {
                p.sendImpossible("Vous n'avez pas assez de mana pour exécuter cet effet !");
                return false;
            }
        }

        boolean didAll = true;

        for(Statement statement : this.statements) {
			boolean did = statement.execute(p, source);
			if(!did) didAll = false;
		}

        if(didAll) {
            p.getBattleField().getEnemy().getBukkitPlayer().sendMessage(ChatColor.YELLOW + p.getBukkitPlayer().getName() + Prefixes.CARD_NAME + " a exécuté l'effet " + Prefixes.EFFECT_DESCRIPTION + this.description + Prefixes.CARD_NAME);
            p.getBukkitPlayer().sendMessage(Prefixes.CARD_NAME + "L'effet " + Prefixes.EFFECT_DESCRIPTION + this.description + Prefixes.CARD_NAME + " vient d'être exécuté !");
        }else{
            p.sendImpossible("L'effet n'a pas pu être exécuté entièrement");
        }

        return didAll;
    }

    public boolean hasOpposites() {
        return this.opposites.isEmpty();
    }

    public void executeAllOpposites(TGAPlayer p, Card source) throws EffectException {
		for(Statement opposite : opposites) {
			opposite.execute(p, source);
		}
    }

    public enum ExecutionTime {
		ON_INVOKE("Invocation>"),
		ON_DEATH("Mort>"),
		DURING_CARDLIFE("∞>"),
		ABILITY("Abilité>"),
		RAPID_ABILITY("Abilité Rapide>");
		
		private final String customName;
		
		ExecutionTime(String customName) {
			this.customName = customName;
		}
		
		public String getCustomName() {
			return Prefixes.EXECUTION_TIME_PREFIX + this.customName;
		}
		
	}
	
}
