package com.mygdx.fuegopeligro.entity;

import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.fuegopeligro.audio.AudioProcessor;
import com.mygdx.fuegopeligro.graphics.GraphicsProcessor;
import com.mygdx.fuegopeligro.physics.PhysicsProcessor;

/**
 * Any game object with any physics, audio and/or graphics related behavior should extend this.
 *
 * @author JDEsguerra
 */
public abstract class Entity implements Disposable {
    /**
     * Handler of every possible state this entity may be in.
     */
    private final StateMachine stateMachine;
    /**
     * A {@link GraphicsProcessor} used to draw the current sprite on the screen.
     */
    private final GraphicsProcessor graphics;
    /**
     * A {@link PhysicsProcessor} to handle collisions and movement.
     */
    private final PhysicsProcessor physics;
    /**
     * An {@link AudioProcessor} that handles audio events according to this entity's state.
     */
    private final AudioProcessor audio;
    /**
     * The Box2D {@link Body} containing all physical properties of this entity.
     */
    private Body body;
    /**
     * The direction the body of this entity if facing (left or right).
     */
    private Direction direction;

    public Entity(final GraphicsProcessor graphics, final PhysicsProcessor physics, final AudioProcessor audio) {
        this(graphics, physics, audio, null);
    }

    public Entity(final GraphicsProcessor graphics, final PhysicsProcessor physics, final AudioProcessor audio,
                  final State<Entity> initialState) {
        this.graphics = graphics;
        this.physics = physics;
        this.audio = audio;
        stateMachine = new DefaultStateMachine(this, initialState);
    }

    /**
     * Readies the state of this entity before the next step, updating before rendering. Should be
     * called before {@link Entity#step(Batch)}.
     *
     * @param camera The instance of the {@link Camera} used to show things on the screen.
     */
    public void update(final Camera camera) {
        graphics.update(this, camera);
        stateMachine.update();
    }

    /**
     * Draws the current {@link Sprite} and handles physics and audio interaction according to this
     * {@link Entity} inner state (e.g. the action it is performing: jumping, walking,
     * etc.).
     * <p>
     * Must be called in every render step inside a {@link Batch#begin()} and {@link Batch#end()}
     * block.
     *
     * @param batch The {@link Batch} to use for drawing.
     */
    public void step(final Batch batch) {
        physics.update(this);
        audio.update(this);
        graphics.draw(this, batch);
    }

    /**
     * Updates the sizes of the elements drawn on the screen to correspond to the new width and
     * height.
     *
     * @param width  The new width of the viewport (in pixels).
     * @param height The new height of the viewport (in pixels).
     */
    public void resize(final int width, final int height) {
        graphics.resize(width, height);
    }

    /**
     * Sets a new {@link State} for this {@link Entity} to be performed in the next execution step
     * for this character.
     *
     * @param newState The state to make the transition to. Each {@link Entity} may define its own.
     * @see StateMachine#changeState(State)
     */
    public void changeState(final State<Entity> newState) {
        stateMachine.changeState(newState);
    }

    /**
     * Whether this entity is performing the given state or not.
     *
     * @param state The state to test against the current actions of the entity.
     * @return true if this entity is executing the state; false, otherwise.
     * @see StateMachine#isInState(State)
     */
    public boolean isInState(final State<Entity> state) {
        return state != null && stateMachine.isInState(state);
    }

    /**
     * Returns the current state of this {@link Entity}.
     *
     * @return The {@link State} this entity is currently in.
     * @see StateMachine#getCurrentState()
     */
    public State<Entity> getCurrentState() {
        return stateMachine.getCurrentState();
    }

    /*
     * (non-Javadoc)
     *
     * @see com.badlogic.gdx.utils.Disposable#dispose()
     */
    @Override
    public void dispose() {
        audio.dispose();
        graphics.dispose();
        physics.dispose();

    }

    public Body getBody() {
        return body;
    }

    public void setBody(final Body body) {
        this.body = body;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(final Direction direction) {
        this.direction = direction;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(getClass().getSimpleName())
                .append(" [state=")
                .append(stateMachine.getCurrentState()).append("]");
        return builder.toString();
    }

}
