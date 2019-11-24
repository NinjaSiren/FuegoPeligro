package com.mygdx.fuegopeligro.physics;

import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.fuegopeligro.entity.Entity;

/**
 * @author JDEsguerra
 */
public interface PhysicsProcessor extends ContactListener, Disposable {

    /**
     * Updates all physics related concerns for the given entity on the current frame.
     *
     * @param character The game entity whose physical properties are to be updated.
     */
    void update(final Entity character);
}
