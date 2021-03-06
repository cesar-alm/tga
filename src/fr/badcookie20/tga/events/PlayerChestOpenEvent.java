package fr.badcookie20.tga.events;

import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerChestOpenEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private Player p;
    private Chest chest;

    public PlayerChestOpenEvent(Player p, Chest chest) {
        this.p = p;
        this.chest = chest;
    }

    public Player getPlayer() {
        return p;
    }

    public Chest getChest() {
        return chest;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
