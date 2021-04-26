package fr.badcookie20.tga.cards.creatures;

import fr.badcookie20.tga.cards.Card;
import fr.badcookie20.tga.cards.EffectCard;
import fr.badcookie20.tga.cards.Entity;
import fr.badcookie20.tga.effect.Effect;
import fr.badcookie20.tga.player.BattleField;
import fr.badcookie20.tga.player.BattleField.Location;
import fr.badcookie20.tga.player.TGAPlayer;
import fr.badcookie20.tga.utils.CardUtils2;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a creature card
 */
public class CreatureCard extends EffectCard {

	private final CreatureType creatureType;
	private List<CreatureProperty> properties;
	private int atk;
	private int def;
    private boolean hasAttacked;
	
	private int tempDef;

	public CreatureCard(Entity<CreatureCard> entity, int id, String name, Rarity rarity, int manaCost, CreatureType creatureType, int atk, int def, List<CreatureProperty> properties, Effect... effects) {
		super(entity, id, name, Type.CREATURE, rarity, manaCost, effects);
		
		this.creatureType = creatureType;
		this.atk = atk;
		this.def = def;
		this.properties = properties;

		if(this.properties == null) {
            this.properties = new ArrayList<>();
        }
	}
	
	/**
	 * Augmente l'attaque du montant spÃ©cifiÃ© (peut Ãªtre nÃ©gatif)
	 */
	public void increaseAtk(int amount) {
		atk+=amount;
	}

	/**
	 * Augmente la dÃ©fense du montant spÃ©cifiÃ© (peut Ãªtre nÃ©gatif)
	 */
	public void increaseDef(int amount) {
		def+=amount;
	}

	/**
	 * Fait des dommages Ã  cette carte.
	 * Pour annuler ces dommage, utiliser resetDef
	 */
	public void damage(int amount) {
		tempDef-=amount;
	}

	/**
	 * RÃ©initialise les dommages faits Ã  cette carte
	 */
	public void resetDef() {
		tempDef = def;
	}
	
	public void die(BattleField b) {
		resetDef();
		if(this.hasProperty(CreatureProperty.BACK_TO_HAND_2) || this.hasProperty(CreatureProperty.BACK_TO_HAND_3)) {
			b.send(Location.BATTLEFIELD, Location.HAND, this);
		}else{
			b.send(Location.BATTLEFIELD, Location.GRAVEYARD, this);
		}
	}

    public void setAttacked() {
		// TODO: 21/10/2016  
		this.hasAttacked = true;
    }
	
	@Override
	public ItemStack createItemStack() {
		return CardUtils2.createItemStack(name, Card.Type.CREATURE, this.manaCost, this.creatureType, this.effects, atk, tempDef, properties, hasAttacked, this.uid);
	}

	public void resetAttack() {
		this.hasAttacked = false;
	}

	public int getAttackPower() {
		return atk;
	}

	public boolean hasAttacked() {
		return this.hasAttacked;
	}

	public CreatureType getCreatureType() {
		return this.creatureType;
	}

	public boolean hasProperty(CreatureProperty property) {
		return this.properties.contains(property);
	}




	
	public static void attack(CreatureCard attacking, CreatureCard target, TGAPlayer attackingP, TGAPlayer targetP) {
        if (attacking.hasProperty(CreatureProperty.IMMOBILISATION)) {
            attackingP.sendImpossible("Vous ne pouvez pas attaquer avec cette carte car elle possède immobilisation");
            return;
        }

        if (target.hasProperty(CreatureProperty.INDESTRUCTIBLE)) {
            targetP.sendImpossible("Vous ne pouvez pas attaquer cette carte car elle possède indestructible");
            return;
        }

        if (attacking.atk >= target.tempDef) {
            targetP.sendImpossible(ChatColor.YELLOW + attackingP.getBukkitPlayer().getName() + ChatColor.RED + " a tué votre carte " + ChatColor.GREEN + attacking.getName());
            attackingP.sendImpossible(ChatColor.GREEN + "Votre carte a tué la créature adverse !");

            // target dead
            target.die(targetP.getBattleField());

            if (attacking.hasProperty(CreatureProperty.OVERPOWERED)) {
                targetP.getBattleField().damage(attacking.atk - target.tempDef, attackingP);
            }
        } else {
            // decrease tempDef from target
            target.damage(attacking.atk);
            targetP.sendImpossible(ChatColor.YELLOW + attackingP.getBukkitPlayer().getName() + ChatColor.RED + " a infligé " + ChatColor.RED + attacking.atk + ChatColor.RED + " dommages à " + ChatColor.GREEN + attacking.getName());
            attackingP.sendImpossible(ChatColor.GREEN + "Votre créature a infligé " + ChatColor.AQUA + attacking.atk + ChatColor.GREEN + " à votre cible !");
        }

        Location.BATTLEFIELD.update(attackingP);
		Location.BATTLEFIELD.update(targetP);
		Location.GRAVEYARD.update(attackingP);
		Location.GRAVEYARD.update(targetP);
    }
}

