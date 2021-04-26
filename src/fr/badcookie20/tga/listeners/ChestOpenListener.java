package fr.badcookie20.tga.listeners;

import fr.badcookie20.tga.cards.FoundReason;
import fr.badcookie20.tga.events.PlayerChestOpenEvent;
import fr.badcookie20.tga.player.TGAPlayer;
import fr.badcookie20.tga.utils.CardUtils2;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ChestOpenListener implements Listener{

    @EventHandler
    public void onChest(PlayerChestOpenEvent e) {
        Player p = e.getPlayer();
        TGAPlayer tgaPlayer = TGAPlayer.getPlayer(p);
        CardUtils2.giveRandomCard(tgaPlayer, FoundReason.CHEST);
    }

}
