package fr.badcookie20.tga.cards.mana;

import fr.badcookie20.tga.cards.Card;
import fr.badcookie20.tga.utils.Prefixes;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class ManaCard extends Card {

	private int fixedAmount;
	private int amount;
	
	public ManaCard(int id, int amount) {
		super(id, "Mana", Type.MANA, Rarity.COMMON);

		this.amount = amount;
        this.fixedAmount = amount;
	}
	
	public int getAmount() {
		return this.amount;
	}

	@Override
	public ItemStack get() {
		// TODO use fr.badcookie20.tga.utils.CardUtils.getItem
		ItemStack item = new ItemStack(Material.SAPLING);
		ItemMeta itemMeta = item.getItemMeta();
		
		itemMeta.setDisplayName(Prefixes.CARD_NAME + "Mana");
		itemMeta.setLore(Arrays.asList(Prefixes.EFFECT_DESCRIPTION + "Ajoute " + this.amount + Prefixes.EFFECT_DESCRIPTION + " à votre mana"));
		
		item.setItemMeta(itemMeta);
		
		return item;
	}

	public void resetAmount() {
		this.amount = fixedAmount;
	}
}
