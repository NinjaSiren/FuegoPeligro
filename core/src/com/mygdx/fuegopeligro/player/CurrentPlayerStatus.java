/**
 *
 */
package com.mygdx.fuegopeligro.player;

import com.badlogic.gdx.utils.Pool.Poolable;

/**
 * Keeps track of player associated data, such as total score, count of collectibles gathered,
 * number of lives and time left.
 *
 * @author JDEsguerra
 *
 */
public final class CurrentPlayerStatus implements PlayerStatus, Poolable {

    /**
     * How many collectibles have the player gathered.
     */
    private short collectibles;

    /**
     * How many lives are left for the left.
     */
    private short lives;

    /**
     * The current score points.
     */
    private int score;

    /**
     * Remaining time to complete the level (in seconds).
     */
    private short time;

    /**
     * Current level number relative to the world.
     */
    private byte level;

    /**
     * The current world number.
     */
    private byte world;


    private byte currentWorld;


    private byte currentLevel;

    public CurrentPlayerStatus() {
        lives = DEFAULT_LIVES;
        time = DEFAULT_TIME;
        level = DEFAULT_LEVEL;
        world = DEFAULT_WORLD;
        currentLevel = CURRENT_LEVEL;
        currentWorld = CURRENT_WORLD;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.mygdx.fuegopeligro.player.PlayerStatus#getCollectibles()
     */
    @Override
    public short getCollectibles() {
        return collectibles;
    }

    protected void setCollectibles(final short collectibles) {
        this.collectibles = collectibles;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.mygdx.fuegopeligro.player.PlayerStatus#getLives()
     */
    @Override
    public short getLives() {
        return lives;
    }

    protected void setLives(final short lives) {
        this.lives = lives;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.mygdx.fuegopeligro.player.PlayerStatus#getScore()
     */
    @Override
    public int getScore() {
        return score;
    }

    protected void setScore(final int score) {
        this.score = score;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.mygdx.fuegopeligro.player.PlayerStatus#getTime()
     */
    @Override
    public short getTime() {
        return time;
    }

    protected void setTime(final short time) {
        this.time = time;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.mygdx.fuegopeligro.player.PlayerStatus#getLevel()
     */
    @Override
    public byte getLevel() {
        return level;
    }

    protected void setLevel(final byte level) {
        this.level = level;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.mygdx.fuegopeligro.player.PlayerStatus#getWorld()
     */
    @Override
    public byte getWorld() {
        return world;
    }

    protected void setWorld(final byte world) {
        this.world = world;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.mygdx.fuegopeligro.player.PlayerStatus#getWorld()
     */
    @Override
    public byte getCurrentWorld() {
        return world;
    }

    protected void setCurrentWorld(final byte currentWorlds) {
        this.currentWorld = currentWorlds;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.mygdx.fuegopeligro.player.PlayerStatus#getLevel()
     */
    @Override
    public byte getCurrentLevel() {
        return level;
    }

    protected void setCurrentLevel(final byte currentLevels) {
        this.currentLevel = currentLevels;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.badlogic.gdx.utils.Pool.Poolable#reset()
     */
    @Override
    public void reset() {
        lives = DEFAULT_LIVES;
        time = DEFAULT_TIME;
        level = DEFAULT_LEVEL;
        world = DEFAULT_WORLD;
        currentWorld = 1;
        currentLevel = 1;
        collectibles = 0;
        score = 0;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[collectibles=").append(collectibles).append(", lives=").append(lives).append(", score=")
                .append(score).append(", time=").append(time).append("]");
        return builder.toString();
    }

}
