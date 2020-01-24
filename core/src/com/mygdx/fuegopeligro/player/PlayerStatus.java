package com.mygdx.fuegopeligro.player;

/**
 * An immutable (read-only) version of {@link CurrentPlayerStatus}
 *
 * @author JDEsguerra
 */
public interface PlayerStatus {
    int DEFAULT_TIME = 600;
    int DEFAULT_LIVES = 3;
    int DEFAULT_WORLD = 1;
    int DEFAULT_LEVEL = 1;
    int CURRENT_WORLD = 1;
    int CURRENT_LEVEL = 1;
    int CURRENT_MG = 1;
    int CURRENT_EQA = 1;
    int CURRENT_HQA = 1;
    int CURRENT_RECOUNT = 0;

    /**
     * Returns the count of gathered collectibles so far.
     *
     * @return The number of collectibles owned by the player.
     */
    int getCollectibles();

    /**
     * Returns the number of remaining lives. When lives reaches zero, it's game over.
     *
     * @return The number of lives left for the player.
     */
    int getLives();

    /**
     * Returns the current score of the player.
     *
     * @return The total amount of points.
     */
    int getScore();

    /**
     * Returns the number of remaining in-game time units to finish the level. When this reaches
     * zero it's game over.
     *
     * @return The time that remains to finish the current level.
     */
    int getTime();

    /**
     * Returns the number of the level relative to the current world. Each world may contain
     * multiple levels.
     *
     * @return The number of the level the player is currently in.
     */
    int getLevel();

    /**
     * Returns the number of the world currently being played. A world represents a collection of
     * themed levels.
     *
     * @return The number of the world the player is currently in.
     */
    int getWorld();

    /**
     * Returns the last number of the world that has been played. A world represents a collection of
     * themed levels.
     *
     * @return The number of the world the player was lastly in.
     */
    int getCurrentWorld();

    /**
     * Returns the last number of the level currently that has been played. A world represents a collection of
     * themed levels.
     *
     * @return The number of the level the player was lastly in.
     */
    int getCurrentLevel();

    int getMGValue();

    int getEqaValue();

    int getHqaValue();

    int getGoResetCounter();
}
