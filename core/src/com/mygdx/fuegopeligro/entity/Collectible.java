package com.mygdx.fuegopeligro.entity;

import com.mygdx.fuegopeligro.ai.fsm.CarrotState;
import com.mygdx.fuegopeligro.audio.AudioProcessor;
import com.mygdx.fuegopeligro.graphics.GraphicsProcessor;
import com.mygdx.fuegopeligro.physics.PhysicsProcessor;

/**
 * @author JDEsguerra
 */
public class Collectible extends Entity {
    /**
     * Signals whether this {@link Collectible} has been collected by the main character or not.
     */
    private boolean collected;

    public Collectible(final GraphicsProcessor graphics, final PhysicsProcessor physics, final AudioProcessor audio) {
        super(graphics, physics, audio, CarrotState.DOWN);
    }

    /**
     * Indicates whether this {@link Collectible} has already been picked up by the main character.
     *
     * @return true if it has been collected; false, otherwise.
     */
    public boolean isCollected() {
        return collected;
    }

    /**
     * Used to indicate if this {@link Collectible} has been collected by the main character.
     *
     * @param collected wheter it has been gatehred or not.
     */
    public void setCollected(final boolean collected) {
        this.collected = collected;
    }
}
