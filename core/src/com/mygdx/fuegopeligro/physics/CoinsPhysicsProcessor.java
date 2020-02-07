package com.mygdx.fuegopeligro.physics;

import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.mygdx.fuegopeligro.ai.fsm.CarrotState;
import com.mygdx.fuegopeligro.ai.msg.MessageType;
import com.mygdx.fuegopeligro.entity.Collectible;
import com.mygdx.fuegopeligro.entity.Entity;

/**
 * @author JDEsguerra
 */
public class CoinsPhysicsProcessor implements PhysicsProcessor {
    public static final String COINS_IDENTIFIER = "carrot";

    private static final float MAX_DISTANCE = 0.18f;
    private static final float VERTICAL_VELOCITY = 0.21f;
    private float origin;

    @Override
    public void beginContact(final Contact contact) {
        if (COINS_IDENTIFIER.equals(contact.getFixtureA().getUserData())) {
            collectCoins(contact.getFixtureA());
        } else if (COINS_IDENTIFIER.equals(contact.getFixtureB().getUserData())) {
            collectCoins(contact.getFixtureB());
        }
    }

    private void collectCoins(final Fixture fixture) {
        Collectible coins = (Collectible) fixture.getBody().getUserData();
        if (!coins.isCollected()) {
            coins.setCollected(true);
            MessageManager.getInstance().dispatchMessage(null, MessageType.COINS_COLLECTED.code(), coins);
        }
    }

    @Override
    public void endContact(final Contact contact) {

    }

    @Override
    public void preSolve(final Contact contact, final Manifold oldManifold) {

    }

    @Override
    public void postSolve(final Contact contact, final ContactImpulse impulse) {

    }

    @Override
    public void update(final Entity character) {
        Body body = character.getBody();

        if (((Collectible) character).isCollected()) {
            body.getWorld().destroyBody(body);
        } else {
            Vector2 position = character.getBody().getPosition();
            if (!character.isInState(CarrotState.UP)) {
                origin = position.y;
                body.setLinearVelocity(0.0f, VERTICAL_VELOCITY);
                character.changeState(CarrotState.UP);
            } else if (position.y - origin > MAX_DISTANCE) {
                body.setLinearVelocity(0.0f, -VERTICAL_VELOCITY);
            } else if (position.y <= origin) {
                character.changeState(CarrotState.DOWN);
            }
        }
    }

    @Override
    public void dispose() {

    }
}
