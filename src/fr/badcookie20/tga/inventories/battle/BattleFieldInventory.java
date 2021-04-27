package fr.badcookie20.tga.inventories.battle;

import fr.badcookie20.tga.cards.Card;
import fr.badcookie20.tga.cards.CastCard;
import fr.badcookie20.tga.cards.EnchantmentCard;
import fr.badcookie20.tga.cards.creatures.CreatureCard;
import fr.badcookie20.tga.cards.sorcery.SorceryCard;
import fr.badcookie20.tga.effect.Effect;
import fr.badcookie20.tga.exceptions.EffectException;
import fr.badcookie20.tga.inventories.manager.InventoriesManager;
import fr.badcookie20.tga.inventories.manager.InventoryType;
import fr.badcookie20.tga.inventories.manager.SaveableInventory;
import fr.badcookie20.tga.player.BattleField;
import fr.badcookie20.tga.player.StopReason;
import fr.badcookie20.tga.player.TGAPlayer;
import fr.badcookie20.tga.utils.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BattleFieldInventory extends SaveableInventory {

    private static final String INVENTORY_NAME = ChatColor.BLACK + "Champ de bataille";

    private static final int MAX_CARDS_SHOWN = 5;

    public static final ItemStack FORCE_STOP = BukkitUtils.createItemStack(Material.BEDROCK, ChatColor.RED + "ERR_EXC_PLU", null);

    private static final String END_TURN_NAME = ChatColor.GREEN + "Terminer le tour";
    private static final String BATTLE_START_NAME = ChatColor.GREEN + "Passer à l'attaque";
    private static final String END_BATTLE_NAME = ChatColor.GREEN + "Terminer l'attaque";

    private static final String CLOSE_NAME = ChatColor.GREEN + "Fermer le champ de bataille";
    private static final String REOPEN_NAME = ChatColor.GREEN + "Ouvrir le champ de bataille";

    private static final String QUIT_NAME = ChatColor.RED + "Quitter la partie";
    private static final String BANNED_NAME = ChatColor.GOLD + "Cartes bannies";
    private static final String GRAVEYARD_NAME = ChatColor.GOLD + "Cimetière";
    private static final String DECK_NAME = ChatColor.GOLD + "Deck";

    private static final int SEPARATION_LINE_START = 18;
    private static final int SEPARATION_LINE_END = 26;

    private static final int ENEMY_CARDS_SLOT = 0;
    private static final int OWN_CARDS_START_SLOT = 27;
    private static final int QUIT_SLOT = 45;
    private static final int CLOSE_SLOT = 46;
    private static final int NEXT_SLOT = 47;
    private static final int MANA_SLOT = 49;
    private static final int TOKENS_SLOT = 50;
    private static final int BANNED_SLOT = 51;
    private static final int GRAVEYARD_SLOT = 52;
    private static final int DECK_SLOT = 53;
    private static final int REOPEN_SLOT = 0;

    private static final ItemStack END_TURN = BukkitUtils.createItemStack(Material.COMPASS, END_TURN_NAME, null);
    private static final ItemStack BATTLE_START = BukkitUtils.createItemStack(Material.COMPASS, BATTLE_START_NAME, null);
    private static final ItemStack END_BATTLE = BukkitUtils.createItemStack(Material.COMPASS, END_BATTLE_NAME, null);

    public static final ItemStack REOPEN = BukkitUtils.createItemStack(Material.FEATHER, REOPEN_NAME, null);
    private static final ItemStack CLOSE = BukkitUtils.createItemStack(Material.FEATHER, CLOSE_NAME, Arrays.asList(ChatColor.AQUA + "Faites " + ChatColor.YELLOW + "/battlefield " + ChatColor.AQUA + " pour le",
            ChatColor.AQUA + "rouvrir, ou cliquez sur l'item dans",
            ChatColor.AQUA + "inventaire"));
    private static final ItemStack MANA = BukkitUtils.createItemStack(Material.EMERALD, "ERR_NOT_INIT", null);
    private static final ItemStack TOKENS = BukkitUtils.createItemStack(Material.ARMOR_STAND, "ERR_NOT_INIT", null);
    private static final ItemStack QUIT = BukkitUtils.createItemStack(Material.CACTUS, QUIT_NAME, null);
    private static final ItemStack BANNED = BukkitUtils.createItemStack(Material.BOOK, BANNED_NAME, Collections.singletonList("ERR_NOT_INIT"));
    private static final ItemStack GRAVEYARD = BukkitUtils.createItemStack(Material.WEB, GRAVEYARD_NAME, Collections.singletonList("ERR_NOT_INIT"));
    private static final ItemStack DECK = BukkitUtils.createItemStack(Material.BOOKSHELF, DECK_NAME, Collections.singletonList("ERR_NOT_INIT"));
    private static final ItemStack SEPARATION = BukkitUtils.createItemStack(Material.BONE, " ", null);

    @Override
    public Inventory get(TGAPlayer p) {
        if(this.isSaved(p)) {
            return getSavedInventory(p);
        }

        p.getBattleField().updateHand();

        Inventory inv = Bukkit.createInventory(null, 54, INVENTORY_NAME);

        inv.setItem(BANNED_SLOT, initBannedLore(p.getBattleField()));
        inv.setItem(GRAVEYARD_SLOT, initGraveyardLore(p.getBattleField()));
        inv.setItem(DECK_SLOT, initDeckLore(p.getBattleField()));

        inv.setItem(QUIT_SLOT, QUIT);
        inv.setItem(CLOSE_SLOT, CLOSE);

        for(int i = SEPARATION_LINE_START; i <= SEPARATION_LINE_END; i++) {
            inv.setItem(i, SEPARATION);
        }

        inv.setItem(MANA_SLOT, initManaItem(p.getBattleField()));
        inv.setItem(TOKENS_SLOT, initTokensItem(p.getBattleField()));

        int i = OWN_CARDS_START_SLOT;
        for(Card c : p.getBattleField().getCards(BattleField.Location.BATTLEFIELD)) {
            inv.setItem(i, c.createItemStack());
            i++;
        }

        int j = ENEMY_CARDS_SLOT;
        for(Card c : p.getBattleField().getEnemy().getBattleField().getCards(BattleField.Location.BATTLEFIELD)) {
            inv.setItem(j, c.createItemStack());
            j++;
        }

        switch(p.getBattleField().getStatus()) {
            case TURN_PART_0:
                inv.setItem(NEXT_SLOT, BATTLE_START);
                break;
            case TURN_BATTLE:
                inv.setItem(NEXT_SLOT, END_BATTLE);
                break;
            case TURN_PART_2:
                inv.setItem(NEXT_SLOT, END_TURN);
                break;
        }

        return saveInventory(p, inv);
    }

    @Override
    public InventoryType getType() {
        return InventoryType.BATTLEFIELD;
    }

    @Override
    public void handleClicking(TGAPlayer p) {
        ItemStack clicked = NextUtils.forceNextItem(p, InventoryType.BATTLEFIELD);

        if(BukkitUtils.areSimilar(GRAVEYARD, clicked)) {
            handleGraveyard(p);
            return;
        }else if(BukkitUtils.areSimilar(BANNED, clicked)){
            handleBanned(p);
            return;
        }else if(BukkitUtils.areSimilar(DECK, clicked)) {
            InventoriesManager.getInstance().handle(p, InventoryType.BATTLEFIELD);
            return;
        }else if(BukkitUtils.areSimilar(MANA, clicked)) {
            InventoriesManager.getInstance().handle(p, InventoryType.BATTLEFIELD);
            return;
        }else if(BukkitUtils.areSimilar(TOKENS, clicked)) {
            InventoriesManager.getInstance().handle(p, InventoryType.BATTLEFIELD);
            return;
        }else if(clicked.equals(QUIT)) {
            TGAPlayer.stopBattle(p, p.getBattleField().getEnemy(), StopReason.SURRENDERED);
            return;
        }else if(clicked.equals(SEPARATION)) {
            InventoriesManager.getInstance().handle(p, InventoryType.BATTLEFIELD);
            return;
        }else if(clicked.equals(FORCE_STOP)) {
            p.getBukkitPlayer().closeInventory();
            return;
        }else if(clicked.equals(BATTLE_START)) {
            p.getBattleField().setStatus(BattleField.Status.TURN_BATTLE);
            InventoriesManager.getInstance().handle(p, InventoryType.BATTLEFIELD);
            return;
        }else if(clicked.equals(END_BATTLE)) {
            p.getBattleField().setStatus(BattleField.Status.TURN_PART_2);
            InventoriesManager.getInstance().handle(p, InventoryType.BATTLEFIELD);
            return;
        }else if(clicked.equals(END_TURN)) {
            p.getBattleField().setStatus(BattleField.Status.NOT_PLAYING);
            p.getBukkitPlayer().getInventory().clear();
            p.getBukkitPlayer().getInventory().setItem(REOPEN_SLOT, REOPEN);
            return;
        }else if(clicked.equals(CLOSE)) {
            p.getBukkitPlayer().getInventory().clear();
            p.getBukkitPlayer().getInventory().setItem(REOPEN_SLOT, REOPEN);
            return;
        }

        int slot = NextUtils.getLastClickedRawSlotOf(p.getBukkitPlayer());
        // TODO We need to get the actual card!
        Card c = ItemUIDUtils.getCardByItem(clicked);

        if(c == null) {
            Logger.logError("card is null!");
            InventoriesManager.getInstance().handle(p, InventoryType.BATTLEFIELD);
            return;
        }

        if(slot >= 81) {
            if(c instanceof CastCard) {
                int availableMana = p.getBattleField().getMana();
                int cardAmount = ((CastCard) c).getManaCost();

                if(availableMana < cardAmount) {
                    p.sendImpossible("Vous n'avez pas assez de mana pour invoquer cette carte !");
                    InventoriesManager.getInstance().handle(p, InventoryType.BATTLEFIELD);
                    return;
                }
            }

            if(!p.getBattleField().canAddOnBattleField()) {
                p.sendImpossible("Vous n'avez pas assez de place sur votre champ de bataille pour ajouter une carte !");
                InventoriesManager.getInstance().handle(p, InventoryType.BATTLEFIELD);
                return;
            }

            if(!(c instanceof SorceryCard) && !p.getBattleField().getStatus().canInvoke()) {
                p.sendImpossible("Vous ne pouvez pas invoquer de créatures, d'enchantements ou de la mana durant cette phase !");
                InventoriesManager.getInstance().handle(p, InventoryType.BATTLEFIELD);
                return;
            }

            // We do not take care of removing mana or anything
            p.getBattleField().send(BattleField.Location.HAND, BattleField.Location.BATTLEFIELD, c, true);
            return;
        }

        try {
            if (slot >= 27 && slot <= 44) {
                if (p.getBattleField().getStatus() == BattleField.Status.NOT_PLAYING) {
                    InventoriesManager.getInstance().handle(p, InventoryType.BATTLEFIELD);
                    return;
                }

                if (p.getBattleField().getStatus().canInvoke()) {
                    if (c instanceof CreatureCard) {
                        CreatureCard card = (CreatureCard) c;

                        if (card.hasEffect(Effect.ExecutionTime.ABILITY)) {
                            List<Effect> allAbilities = card.getAllEffects(Effect.ExecutionTime.ABILITY);

                            for(Effect e : allAbilities) {
                                boolean doIt = p.confirmAction(e.getFullDescription());

                                if (doIt) {
                                    e.executeAll(p, card);
                                }
                            }
                        }

                        BattleField.Location.BATTLEFIELD.update(p);
                        InventoriesManager.getInstance().handle(p, InventoryType.BATTLEFIELD);
                        return;
                    }

                    if (c instanceof EnchantmentCard) {
                        InventoriesManager.getInstance().handle(p, InventoryType.BATTLEFIELD);
                        return;
                    }
                }

                if (p.getBattleField().getStatus() == BattleField.Status.TURN_BATTLE) {
                    if (c instanceof CreatureCard) {
                        CreatureCard card = (CreatureCard) c;

                        if(card.hasAttacked()) {
                            p.sendImpossible("Cette carte a déjà attaqué ce tour !");
                            InventoriesManager.getInstance().handle(p, InventoryType.BATTLEFIELD);
                            return;
                        }

                        CreatureCard targetCard = (CreatureCard) p.confirmTarget(Card.Type.CREATURE);

                        if(targetCard == null) {
                            InventoriesManager.getInstance().handle(p, InventoryType.BATTLEFIELD);
                            return;
                        }

                        CreatureCard.attack(card, targetCard, p, p.getBattleField().getEnemy());
                        InventoriesManager.getInstance().handle(p, InventoryType.BATTLEFIELD);
                        return;
                    }

                    if (c instanceof EnchantmentCard) {
                        InventoriesManager.getInstance().handle(p, InventoryType.BATTLEFIELD);
                        return;
                    }
                }

            }
        }catch(EffectException e) {
            e.printStackTrace();
            p.sendImpossible("Une erreur est survenue lors de l'exécution de l'effet");
        }

        InventoriesManager.getInstance().handle(p, InventoryType.BATTLEFIELD);
    }

    private ItemStack initManaItem(BattleField battleField) {
        String manaItemName = ChatColor.GOLD + "Mana : " + ChatColor.GREEN;
        ItemStack item = new ItemStack(MANA.getType());
        ItemMeta manaItemMeta = item.getItemMeta();

        manaItemMeta.setDisplayName(manaItemName + battleField.getMana());

        item.setItemMeta(manaItemMeta);

        return item;
    }

    private ItemStack initTokensItem(BattleField battleField) {
        String tokensItemName = ChatColor.GOLD + "Jetons : " + ChatColor.GREEN;
        ItemStack item = new ItemStack(TOKENS.getType());
        ItemMeta tokensItemMeta = item.getItemMeta();

        tokensItemMeta.setDisplayName(tokensItemName + battleField.getTokens());

        item.setItemMeta(tokensItemMeta);

        return item;
    }

    private ItemStack initDeckLore(BattleField battleField) {
        List<Card> deck = battleField.getCards(BattleField.Location.DECK);
        return initLore(deck, DECK, true);
    }

    private ItemStack initGraveyardLore(BattleField battleField) {
        List<Card> graveyard = battleField.getCards(BattleField.Location.GRAVEYARD);
        return initLore(graveyard, GRAVEYARD, false);
    }

    private ItemStack initBannedLore(BattleField battleField) {
        List<Card> banned = battleField.getCards(BattleField.Location.BANNED_CARDS);
        return initLore(banned, BANNED, false);
    }

    private ItemStack initLore(List<Card> cards, ItemStack previousItem, boolean onlyCount) {
        ItemMeta itemMeta = previousItem.getItemMeta();
        List<String> lore = new ArrayList<>();

        if(cards.isEmpty()) {
            lore.add(Prefixes.CARD_NAME + "" + ChatColor.ITALIC + "Vide");
        }else if(onlyCount){
            lore.add(Prefixes.CARD_NAME + "" + cards.size() + " cartes restantes");
        }else{
            int i = 0;

            for(Card c : cards) {
                lore.add(Prefixes.CARD_NAME + c.getName());
                i++;

                if(i == MAX_CARDS_SHOWN) {
                    lore.add(Prefixes.CARD_NAME + "...");
                    break;
                }
            }
        }

        itemMeta.setLore(lore);
        previousItem.setItemMeta(itemMeta);
        return previousItem;
    }

    private void handleBanned(TGAPlayer p) {
        if(p.getBattleField().getCards(BattleField.Location.BANNED_CARDS).isEmpty()) {
            InventoriesManager.getInstance().handle(p, InventoryType.BATTLEFIELD);
        }else{
            InventoriesManager.getInstance().handle(p, InventoryType.BANNED_CARDS);
        }
    }

    private void handleGraveyard(TGAPlayer p) {
        if(p.getBattleField().getCards(BattleField.Location.GRAVEYARD).isEmpty()) {
            InventoriesManager.getInstance().handle(p, InventoryType.BATTLEFIELD);
        }else{
            InventoriesManager.getInstance().handle(p, InventoryType.GRAVEYARD);
        }
    }
}
