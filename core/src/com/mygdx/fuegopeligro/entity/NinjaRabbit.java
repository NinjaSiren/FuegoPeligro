package com.mygdx.fuegopeligro.entity;

import com.badlogic.gdx.graphics.Camera;
import com.mygdx.fuegopeligro.ai.fsm.NinjaRabbitState;
import com.mygdx.fuegopeligro.audio.AudioProcessor;
import com.mygdx.fuegopeligro.graphics.GraphicsProcessor;
import com.mygdx.fuegopeligro.physics.BodyProcessor;
import com.mygdx.fuegopeligro.physics.PhysicsProcessor;
import com.mygdx.fuegopeligro.player.PlayerStatusProcessor;

/**
 * An animated entity representing the main character in the game (a ninja rabbit). Holds the state
 * of the character at any given time, given the user input. Each state corresponds to some key
 * frame in a certain animation.
 *
 * @author JDEsguerra
 */
public class NinjaRabbit extends Entity {
    /**
     * A component used to change the body of this {@link Entity} according the action being
     * executed or the {@link Direction} that is facing, if necessary.
     */
    private final BodyProcessor bodyProcessor;

    /**
     * A component that handles the score, lives and other player-related data.
     */
    private final PlayerStatusProcessor player;

    NinjaRabbit(final PlayerStatusProcessor player, final BodyProcessor bodyProcessor, final GraphicsProcessor graphics,
                final PhysicsProcessor physics,
                final AudioProcessor audio) {
        super(graphics, physics, audio, NinjaRabbitState.IDLE);
        this.bodyProcessor = bodyProcessor;
        this.player = player;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.mygdx.fuegopeligro.entity.Entity#update(com.badlogic.gdx.graphics.Camera)
     */
    @Override
    public void update(final Camera camera) {
        player.update(this);
        super.update(camera);
        bodyProcessor.update(this);
    }
}
