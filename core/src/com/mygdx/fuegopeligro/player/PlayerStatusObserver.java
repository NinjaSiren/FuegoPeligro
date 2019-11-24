package com.mygdx.fuegopeligro.player;

/**
 * @author JDEsguerra
 */
public interface PlayerStatusObserver {
    /**
     * Callback to be called when an instance of {@link PlayerStatus} changes its states, for
     * example because a player has lost a live or has collected an item.
     *
     * @param status
     */
    void onPlayerStatusChange(final PlayerStatus event);
}
