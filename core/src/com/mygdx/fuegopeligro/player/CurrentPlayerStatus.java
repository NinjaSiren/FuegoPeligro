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
    private int collectibles;

    /**
     * How many lives are left for the left.
     */
    private int lives;

    /**
     * The current score points.
     */
    private int score;

    /**
     * Remaining time to complete the level (in seconds).
     */
    private int time;

    /**
     * Current level number relative to the world.
     */
    private int level;

    /**
     * The current world number.
     */
    private int world;


    private int currentWorld;


    private int currentLevel;


    private int mgValue;


    private int eqaValue;


    private int hqaValue;


    private int goResetCounter;


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
    public int getCollectibles() {
        return collectibles;
    }

    public void setCollectibles(int collectibles) {
        this.collectibles = collectibles;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.mygdx.fuegopeligro.player.PlayerStatus#getLives()
     */
    @Override
    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
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

    public void setScore(int score) {
        this.score = score;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.mygdx.fuegopeligro.player.PlayerStatus#getTime()
     */
    @Override
    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.mygdx.fuegopeligro.player.PlayerStatus#getLevel()
     */
    @Override
    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.mygdx.fuegopeligro.player.PlayerStatus#getWorld()
     */
    @Override
    public int getWorld() {
        return world;
    }

    public void setWorld(int world) {
        this.world = world;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.mygdx.fuegopeligro.player.PlayerStatus#getWorld()
     */
    @Override
    public int getCurrentWorld() {
        return world;
    }

    public void setCurrentWorld(int currentWorlds) {
        this.world = currentWorlds;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.mygdx.fuegopeligro.player.PlayerStatus#getLevel()
     */
    @Override
    public int getCurrentLevel() {
        return level;
    }

    public void setCurrentLevel(int currentLevels) {
        this.level = currentLevels;
    }

    @Override
    public int getMGValue() { return mgValue; }

    public void setMGValue(int mgValue) { this.mgValue = mgValue; }

    @Override
    public int getEqaValue() { return eqaValue; }

    public void setEqavalue(int eqaValue) { this.eqaValue = eqaValue; }

    @Override
    public int getHqaValue() { return hqaValue; }

    public void setHqaValue (int hqaValue) { this.hqaValue = hqaValue; }

    @Override
    public int getGoResetCounter() { return goResetCounter; }

    public void setGoResetCounter(int goResetCounter) { this.goResetCounter = goResetCounter; }

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
    private int prevCollectibles;

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
