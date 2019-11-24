/**
 *
 */
package com.mygdx.fuegopeligro.entity;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.mygdx.fuegopeligro.audio.AudioProcessor;
import com.mygdx.fuegopeligro.graphics.GraphicsProcessor;
import com.mygdx.fuegopeligro.physics.PhysicsProcessor;
import com.mygdx.fuegopeligro.player.PlayerStatusProcessor;

/**
 * An {@link Entity} representing the entire level as separate audio, physical and graphics
 * components.
 *
 * @author JDEsguerra
 *
 */
public class Environment extends Entity {

    /**
     * A component that updates the status of the player during the gaming session according to the
     * Environments inner state.
     */
    private final PlayerStatusProcessor player;

    public Environment(final GraphicsProcessor graphics, final PhysicsProcessor physics, final AudioProcessor audio,
                       final PlayerStatusProcessor player) {
        super(graphics, physics, audio);
        this.player = player;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.mygdx.fuegopeligro.entity.Entity#step(com.badlogic.gdx.graphics.g2d.Batch)
     */
    @Override
    public void step(final Batch batch) {
        player.update(this);
        super.step(batch);
    }
}
