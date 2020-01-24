package com.mygdx.fuegopeligro.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.fuegopeligro.FuegoPeligro;
import com.mygdx.fuegopeligro.entity.Direction;

/**
 * @author JDEsguerra
 */
public final class FiremanBodyFactory implements BodyFactory {
    private static final float NINJA_RABBIT_SCALE = 101 / FuegoPeligro.PPM;
    private static final int FOOT_FIXTURE_INDEX = 9;
    private static final String RABBIT_IDENTIFIER = "rabbit";
    private static final Vector2 INITIAL_POSITION = new Vector2(1.2f, 2.2f);

    private final BodyEditorLoader loader;
    private final BodyDef bdef;
    private final FixtureDef fdef;

    public FiremanBodyFactory(final BodyEditorLoader loader) {
        if (loader == null) {
            throw new IllegalArgumentException("'loader' cannot be null");
        }
        this.loader = loader;

        bdef = new BodyDef();
        bdef.type = BodyType.DynamicBody;
        bdef.position.set(INITIAL_POSITION);
        bdef.fixedRotation = true;
        bdef.gravityScale = 1.75f;
        // bdef.bullet = true;

        fdef = new FixtureDef();
        fdef.density = 1.0f;
        fdef.restitution = 0.0f;
        fdef.friction = 0.8f;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.mygdx.fuegopeligro.physics.BodyFactory#create(com.badlogic.gdx.physics.box2d.World,
     * com.mygdx.fuegopeligro.entity.Direction)
     */
    @Override
    public Body create(final World world, final Direction direction) {
        return create(world, bdef, direction);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.mygdx.fuegopeligro.physics.BodyFactory#create(com.badlogic.gdx.physics.box2d.World,
     * com.badlogic.gdx.physics.box2d.BodyDef)
     */
    @Override
    public Body create(final World world, final BodyDef definition) {
        return create(world, definition, Direction.RIGHT);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.mygdx.fuegopeligro.physics.BodyFactory#create(com.badlogic.gdx.physics.box2d.World,
     * com.badlogic.gdx.physics.box2d.BodyDef, com.mygdx.fuegopeligro.entity.Direction)
     */
    @Override
    public Body create(final World world, final BodyDef definition, final Direction direction) {
        Body rabbitBody = world.createBody(definition);
        loader.attachFixture(rabbitBody, RABBIT_IDENTIFIER + "-" + direction.direction(), fdef, NINJA_RABBIT_SCALE);

        Fixture footSensor = rabbitBody.getFixtureList().get(FOOT_FIXTURE_INDEX);
        footSensor.setUserData(FiremanPhysicsProcessor.FOOT_IDENTIFIER);
        footSensor.setDensity(0.0f);
        footSensor.setSensor(true);

        rabbitBody.resetMassData();

        return rabbitBody;
    }
}
