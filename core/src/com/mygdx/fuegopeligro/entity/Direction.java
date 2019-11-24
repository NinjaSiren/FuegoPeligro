package com.mygdx.fuegopeligro.entity;

/**
 * Indicates whether a given {@link Entity} is facing left or right. This eliminates the need to
 * check for its horizontal velocity and takes into account the facing direction when it's not
 * moving at all.
 *
 * @author JDEsguerra
 */
public enum Direction {
    /**
     * The {@link Entity} is facing left.
     */
    LEFT("left"),
    /**
     * The {@link Entity} is facing right.
     */
    RIGHT("right");

    /**
     * A human readable description of the value of the enum. Useful for printing and debugging.
     */
    private String direction;

    Direction(final String direction) {
        this.direction = direction;
    }

    public String direction() {
        return direction;
    }

    @Override
    public String toString() {
        return direction;
    }
}
