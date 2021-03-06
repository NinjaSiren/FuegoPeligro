package com.mygdx.fuegopeligro.physics;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.fuegopeligro.FuegoPeligro;
import com.mygdx.fuegopeligro.entity.Direction;

/**
 * @author JDEsguerra
 */
public final class CoinsBodyFactory implements BodyFactory {
    private static final float CARROT_SCALE = 61 / FuegoPeligro.PPM;

    private final BodyEditorLoader loader;
    private final BodyDef bdef;
    private final FixtureDef fdef;

    public CoinsBodyFactory(final BodyEditorLoader loader) {
        if (loader == null) {
            throw new IllegalArgumentException("'loader' cannot be null");
        }
        this.loader = loader;

        bdef = new BodyDef();
        bdef.type = BodyType.DynamicBody;
        bdef.position.set(0, 0);
        bdef.fixedRotation = true;

        fdef = new FixtureDef();
        fdef.isSensor = true;
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

    @Override
    public Body create(final World world, final BodyDef definition) {
        return create(world, definition, null);
    }

    @Override
    public Body create(final World world, final BodyDef definition, final Direction direction) {
        Body rabbitBody = world.createBody(definition);
        loader.attachFixture(rabbitBody, CoinsPhysicsProcessor.COINS_IDENTIFIER, fdef, CARROT_SCALE);
        rabbitBody.getFixtureList().first().setUserData(CoinsPhysicsProcessor.COINS_IDENTIFIER);
        return rabbitBody;
    }
}
