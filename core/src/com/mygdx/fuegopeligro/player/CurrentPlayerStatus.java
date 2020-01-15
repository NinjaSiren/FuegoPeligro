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
    private byte lives;

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


    private byte mgValue;


    private byte eqaValue;


    private byte hqaValue;


    private byte goResetCounter;


    public CurrentPlayerStatus() {
        lives = DEFAULT_LIVES;
        time = DEFAULT_TIME;
        level = DEFAULT_LEVEL;
        world = DEFAULT_WORLD;
        currentLevel = CURRENT_LEVEL;
        currentWorld = CURRENT_WORLD;
        mgValue = CURRENT_MG;
        eqaValue = CURRENT_EQA;
        hqaValue = CURRENT_HQA;
        goResetCounter = CURRENT_RECOUNT;
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

    protected void setCollectibles(short collectibles) {
        this.collectibles = collectibles;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.mygdx.fuegopeligro.player.PlayerStatus#getLives()
     */
    @Override
    public byte getLives() {
        return lives;
    }

    protected void setLives(byte lives) {
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

    protected void setScore(int score) {
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

    protected void setTime(short time) {
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

    protected void setLevel(byte level) {
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

    protected void setWorld(byte world) {
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

    protected void setCurrentWorld(byte currentWorlds) {
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

    protected void setCurrentLevel(byte currentLevels) {
        this.currentLevel = currentLevels;
    }

    @Override
    public byte getMGValue() { return mgValue; }

    protected void setMGValue(byte mgValue) { this.mgValue = mgValue; }

    @Override
    public byte getEqaValue() { return eqaValue; }

    protected void setEqavalue(byte eqaValue) { this.eqaValue = eqaValue; }

    @Override
    public byte getHqaValue() { return hqaValue; }

    protected void setHqaValue (byte hqaValue) { this.hqaValue = hqaValue; }

    @Override
    public byte getGoResetCounter() { return goResetCounter; }

    protected void setGoResetCounter(byte goResetCounter) { this.goResetCounter = goResetCounter; }

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
        mgValue = 1;
        eqaValue = 1;
        hqaValue = 1;
        goResetCounter = 0;
    }

    private int prevScore;
    private short prevCollectibles;

    public void resetGameOver() {
        lives = DEFAULT_LIVES;
        time = DEFAULT_TIME;
        if(goResetCounter == 0) {
            prevScore = score;
            prevCollectibles = collectibles;
            goResetCounter = +1;
        } else if(goResetCounter >= 1) {
            score = prevScore;
            collectibles = prevCollectibles;
        }
    }

    public void levelEndOver() {
        lives = DEFAULT_LIVES;
        time = DEFAULT_TIME;
        goResetCounter = 0;
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
