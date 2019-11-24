package com.mygdx.fuegopeligro.physics;

import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.fuegopeligro.entity.Entity;

public interface BodyProcessor {

    /**
     * Regenerates the character Box2D {@link Body} according to the action it is executing. It
     * destroys the old body if necessary.
     *
     * @param character The {@link Entity} whose body should be updated.
     */
    void update(Entity character);

}
