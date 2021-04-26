package fr.badcookie20.tga.effect;

import fr.badcookie20.tga.cards.Card;
import fr.badcookie20.tga.exceptions.EffectException;
import fr.badcookie20.tga.player.TGAPlayer;
import fr.badcookie20.tga.utils.Prefixes;
import net.md_5.bungee.api.ChatColor;

public class Effect {

	private ExecutionTime type;
	private String description;
	private int extraCost;
	private Statement statement;
	// TODO check opposites
	private Statement opposite;

	/**
	 * Constructor of an effect
	 * @param type when can it be executed?
	 * @param description its description
	 * @param extraCost the extra cost of the effect, when executing it (<code>null</code> if no extra cost)
	 * @param statement statement executed when invoked
	 * @param opposite statement executed when card destroyed (use for life-card effects)
	 */
	public Effect(ExecutionTime type, String description, int extraCost, Statement statement, Statement opposite) {
		this.type = type;
		this.description = description;
		this.extraCost = extraCost;
		this.statement = statement;
        this.opposite = opposite;
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
	
	public boolean execute(TGAPlayer p, Card source) throws EffectException {
        if(this.extraCost > 0) {
            if(p.getBattleField().getMana() < extraCost) {
                p.sendImpossible("Vous n'avez pas assez de mana pour exécuter cet effet !");
                return false;
            }
        }

        boolean did = this.statement.execute(p, source);

        if(did) {
            p.getBattleField().getEnemy().getBukkitPlayer().sendMessage(ChatColor.YELLOW + p.getBukkitPlayer().getName() + Prefixes.CARD_NAME + " a exécuté l'effet " + Prefixes.EFFECT_DESCRIPTION + this.description + Prefixes.CARD_NAME);
            p.getBukkitPlayer().sendMessage(Prefixes.CARD_NAME + "L'effet " + Prefixes.EFFECT_DESCRIPTION + this.description + Prefixes.CARD_NAME + " vient d'être exécuté !");
        }else{
            p.sendImpossible("L'effet n'a pas pu être exécuté !");
        }

        return did;
    }

    public boolean hasOpposite() {
        return this.opposite != null;
    }

    public void executeOpposite(TGAPlayer p, Card source) throws EffectException {
        this.opposite.execute(p, source);
    }

    public enum ExecutionTime {
		ON_INVOKE("Invocation>"),
		ON_DEATH("Mort>"),
		DURING_CARDLIFE("∞>"),
		ABILITY("Abilité>"),
		RAPID_ABILITY("Abilité Rapide>");
		
		private String customName;
		
		private ExecutionTime(String customName) {
			this.customName = customName;
		}
		
		public String getCustomName() {
			return Prefixes.EXECUTION_TIME_PREFIX + this.customName;
		}
		
	}
	
}
