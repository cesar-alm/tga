package fr.badcookie20.tga.listeners;

import fr.badcookie20.tga.cards.FoundReason;
import fr.badcookie20.tga.player.TGAPlayer;
import fr.badcookie20.tga.utils.CardUtils2;
import fr.badcookie20.tga.utils.NextUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;

public class DefaultListener implements Listener {

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        Player p = e.getPlayer();
        TGAPlayer pp = TGAPlayer.getPlayer(p);

        if(pp.getBattleField() != null) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        if(NextUtils.needsSayingCancel(e.getPlayer())) {
            e.setCancelled(true);
            NextUtils.updateSayingCancel(e.getPlayer());
        }

        NextUtils.updateChatOf(e.getPlayer(), e.getMessage());
    }

    @EventHandler
    public void onKill(EntityDeathEvent e) {
        LivingEntity dead = e.getEntity();

        if(dead instanceof Player) {
            return;
        }

        Player killer = dead.getKiller();

        CardUtils2.giveRandomCard(TGAPlayer.getPlayer(killer), FoundReason.MOB);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        Entity damaged = e.getEntity();

        if(!(damaged instanceof Player)) return;

        TGAPlayer p = TGAPlayer.getPlayer((Player) e.getEntity());
        if(p.getBattleField() != null) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onXP(PlayerExpChangeEvent e) {
        TGAPlayer p = TGAPlayer.getPlayer(e.getPlayer());

        int gained = e.getAmount();

        if(gained >= 5) {
            CardUtils2.giveRandomCard(p, FoundReason.XP_GAIN);
        }
    }

}
