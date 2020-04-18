package com.mhawthor.keylistener.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class KeyPressEvent extends Event {


    private static final HandlerList handlers = new HandlerList();
    private char key;
    private Player player;

    public KeyPressEvent(Player player, char key) {
        this.key = key;
        this.player = player;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public char getKey() {
        return key;
    }

    public Player getPlayer() {
        return player;
    }

}
