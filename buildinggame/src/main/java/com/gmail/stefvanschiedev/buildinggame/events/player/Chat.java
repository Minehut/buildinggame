package com.gmail.stefvanschiedev.buildinggame.events.player;

import com.gmail.stefvanschiedev.buildinggame.managers.arenas.ArenaManager;
import com.gmail.stefvanschiedev.buildinggame.utils.arena.Arena;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * Handles chat events
 *
 * @since 2.2.1
 */
public class Chat implements Listener {

    /**
     * Handles chat events
     *
     * @param e an asynchronized event representing a player chatting
     * @see AsyncPlayerChatEvent
     * @since 2.2.1
     */
    @EventHandler
    public void onAsyncPlayerChat(AsyncPlayerChatEvent e) {
        Arena arena = ArenaManager.getInstance().getArena(e.getPlayer());

        if (arena == null)
            return;

        e.getRecipients().removeIf(player -> !arena.contains(player));
    }
}