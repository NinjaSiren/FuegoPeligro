package com.mygdx.fuegopeligro.graphics;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.mygdx.fuegopeligro.Assets;
import com.mygdx.fuegopeligro.entity.Entity;

import net.dermetfan.gdx.graphics.g2d.Box2DSprite;

/**
 * @author JDEsguerra
 */
public class CarrotGraphicsProcessor implements GraphicsProcessor {
    private static final String CARROT_REGION = "carrot";
    private final Box2DSprite carrot;

    public CarrotGraphicsProcessor(final AssetManager manager) {
        carrot = new Box2DSprite(manager.get(Assets.NINJA_RABBIT_ATLAS).findRegion(CARROT_REGION));
    }

    @Override
    public void update(final Entity character, final Camera camera) {
        // Does nothing for this entity

    }

    @Override
    public void draw(final Entity character, final Batch batch) {
        carrot.draw(batch, character.getBody());
    }

    @Override
    public void resize(final int width, final int height) {
        // TODO Auto-generated method stub

    }

    @Override
    public void dispose() {

    }

}
