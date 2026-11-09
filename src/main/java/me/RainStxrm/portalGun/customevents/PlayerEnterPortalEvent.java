package me.RainStxrm.portalGun.customevents;

import me.RainStxrm.portalGun.entities.Portal;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import javax.sound.sampled.Port;

public class PlayerEnterPortalEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    Player player;
    Portal portal;

    public PlayerEnterPortalEvent(Player player, Portal portal) {
        this.player = player;
        this.portal = portal;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public Player getPlayer() {
        return player;
    }

    public Portal getPortal() {
        return portal;
    }
}
