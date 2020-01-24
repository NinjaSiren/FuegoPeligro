package com.mygdx.fuegopeligro.graphics;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.mygdx.fuegopeligro.Assets;
import com.mygdx.fuegopeligro.FuegoPeligro;
import com.mygdx.fuegopeligro.entity.Direction;
import com.mygdx.fuegopeligro.entity.Entity;

import net.dermetfan.gdx.graphics.g2d.AnimatedBox2DSprite;
import net.dermetfan.gdx.graphics.g2d.AnimatedSprite;
import net.dermetfan.gdx.graphics.g2d.Box2DSprite;

/**
 * @author JDEsguerra
 */
public class CheckpointGraphicsProcessor implements GraphicsProcessor {
    private static final String CARROT_REGION = "carrot";

    private final TextureAtlas textureAtlas;
    private final Box2DSprite carrot;
    private final AnimatedBox2DSprite carrotMoving;

    public CheckpointGraphicsProcessor(final AssetManager manager) {
        textureAtlas = manager.get(Assets.NINJA_RABBIT_ATLAS);

        Array<Sprite> carrots = textureAtlas.createSprites(CARROT_REGION);
        carrot = new Box2DSprite(carrots.first());
        carrot.setAdjustHeight(false);
        carrot.setSize(carrot.getWidth() / FuegoPeligro.PPM,
                carrot.getHeight() / FuegoPeligro.PPM);

        Animation animation = new Animation(1 / 12.0f, carrots, Animation.PlayMode.LOOP);
        carrotMoving = new AnimatedBox2DSprite(new AnimatedSprite(animation));
        carrotMoving.setAdjustSize(false);
        carrotMoving.setSize(carrotMoving.getWidth() / FuegoPeligro.PPM,
                carrotMoving.getHeight() / FuegoPeligro.PPM);
    }

    @Override
    public void update(final Entity character, final Camera camera) {
        // Does nothing for this entity

    }

    @Override
    public void draw(final Entity character, final Batch batch) {
        Box2DSprite frame = null;
        carrotMoving.flipFrames(Direction.RIGHT.equals(character.getDirection()) == carrotMoving.isFlipX(),
                false, false);
        frame = carrotMoving;
        frame.draw(batch, character.getBody());
    }

    @Override
    public void resize(final int width, final int height) {
        // TODO Auto-generated method stub

    }

    @Override
    public void dispose() {

    }

}
