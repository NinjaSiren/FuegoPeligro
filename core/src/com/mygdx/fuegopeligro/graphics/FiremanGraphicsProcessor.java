package com.mygdx.fuegopeligro.graphics;

import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mygdx.fuegopeligro.Assets;
import com.mygdx.fuegopeligro.FuegoPeligro;
import com.mygdx.fuegopeligro.ai.fsm.FiremanState;
import com.mygdx.fuegopeligro.ai.msg.MessageType;
import com.mygdx.fuegopeligro.entity.Direction;
import com.mygdx.fuegopeligro.entity.Entity;

import net.dermetfan.gdx.graphics.g2d.AnimatedBox2DSprite;
import net.dermetfan.gdx.graphics.g2d.AnimatedSprite;
import net.dermetfan.gdx.graphics.g2d.Box2DSprite;
import net.dermetfan.gdx.physics.box2d.Box2DUtils;

/**
 * @author JDEsguerra
 */
public class FiremanGraphicsProcessor implements GraphicsProcessor, Telegraph {
    private static final String WALK_REGION = "walk";
    private static final String JUMP_REGION = "jump";
    private static Vector2 RESPAWN_POSITION = new Vector2(0.6f, 3.2f);

    private final TextureAtlas textureAtlas;
    private final Box2DSprite standingSprite;
    private final AnimatedBox2DSprite walkRightSprite;
    private final AnimatedBox2DSprite walkLeftSprite;
    private final AnimatedBox2DSprite jumpSprite;

    public FiremanGraphicsProcessor(final AssetManager assets) {
        textureAtlas = assets.get(Assets.NINJA_RABBIT_ATLAS);

        Array<Sprite> walkingSprites = textureAtlas.createSprites(WALK_REGION);
        standingSprite = new Box2DSprite(walkingSprites.first());
        standingSprite.setAdjustSize(false);
        standingSprite.setSize(standingSprite.getWidth() / FuegoPeligro.PPM,
                standingSprite.getHeight() / FuegoPeligro.PPM);

        Animation animation = new Animation(1 / 12.0f, walkingSprites, PlayMode.LOOP);
        walkRightSprite = new AnimatedBox2DSprite(new AnimatedSprite(animation));
        walkRightSprite.setAdjustSize(false);
        walkRightSprite.setSize(walkRightSprite.getWidth() / FuegoPeligro.PPM,
                walkRightSprite.getHeight() / FuegoPeligro.PPM);

        animation = new Animation(1 / 12.0f, textureAtlas.createSprites(WALK_REGION), PlayMode.LOOP);
        walkLeftSprite = new AnimatedBox2DSprite(new AnimatedSprite(animation));
        walkLeftSprite.flipFrames(true, false, true);
        walkLeftSprite.setAdjustSize(false);
        walkLeftSprite.setSize(walkLeftSprite.getWidth() / FuegoPeligro.PPM,
                walkLeftSprite.getHeight() / FuegoPeligro.PPM);

        animation = new Animation(1 / 10.0f, textureAtlas.createSprites(JUMP_REGION));
        jumpSprite = new AnimatedBox2DSprite(new AnimatedSprite(animation));
        jumpSprite.setAdjustSize(false);
        jumpSprite.setSize(jumpSprite.getWidth() / FuegoPeligro.PPM,
                jumpSprite.getHeight() / FuegoPeligro.PPM);

        MessageManager.getInstance().addListener(this, MessageType.DEAD.code());
    }

    /*
     * (non-Javadoc)
     *
     * @see com.mygdx.fuegopeligro.graphics.GraphicsProcessor#update(com.mygdx.fuegopeligro.entity.Entity,
     * com.badlogic.gdx.graphics.Camera)
     */
    @Override
    public void update(final Entity character, final Camera camera) {
        camera.position.x = character.getBody() == null ? 0.0f :
                character.getBody().getPosition().x + camera.viewportWidth * 0.25f;
        /*
        LEFT FOR THE MEAN TIME
         */
        RESPAWN_POSITION = new Vector2(camera.position.x - camera.viewportWidth * 0.125f, camera.position.y);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.mygdx.fuegopeligro.entity.GraphicsProcessor#draw(com.badlogic.gdx.graphics.g2d.Batch)
     */
    @Override
    public void draw(final Entity character, final Batch batch) {
        Box2DSprite frame = null;

        if (character.isInState(FiremanState.JUMP)) {
            jumpSprite.flipFrames(Direction.RIGHT.equals(character.getDirection()) == jumpSprite.isFlipX(), false, false);
            frame = jumpSprite;
        } else if (character.isInState(FiremanState.RIGHT)) {
            frame = walkRightSprite;
            character.setDirection(Direction.RIGHT);
        } else if (character.isInState(FiremanState.LEFT)) {
            frame = walkLeftSprite;
            character.setDirection(Direction.LEFT);
        } else {
            standingSprite.flip(Direction.RIGHT.equals(character.getDirection()) == standingSprite.isFlipX(), false);
            frame = standingSprite;
            jumpSprite.setTime(0.0f);
        }

        // Following numbers came from voodoo
        frame.setPosition(
                -frame.getWidth() * 0.5f +
                        Box2DUtils.width(character.getBody()) / (Direction.RIGHT.equals(character.getDirection())
                                ? 2.8f : 1.55f),
                -frame.getHeight() * 0.5f + Box2DUtils.width(character.getBody()) + 0.36f);

        frame.draw(batch, character.getBody());
    }

    @Override
    public boolean handleMessage(final Telegram msg) {
        Entity character = (Entity) msg.extraInfo;
        character.getBody().setTransform(RESPAWN_POSITION, character.getBody().getAngle());
        character.changeState(FiremanState.IDLE);
        character.setDirection(Direction.RIGHT);
        return true;
    }

    @Override
    public void dispose() {

    }

    @Override
    public void resize(final int width, final int height) {
    }
}
