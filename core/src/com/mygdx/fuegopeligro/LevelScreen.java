package com.mygdx.fuegopeligro;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.fuegopeligro.ai.msg.MessageType;
import com.mygdx.fuegopeligro.entity.Entity;
import com.mygdx.fuegopeligro.entity.EntityFactory;
import com.mygdx.fuegopeligro.graphics.BoundedCamera;
import com.mygdx.fuegopeligro.graphics.hud.StatusBar;
import com.mygdx.fuegopeligro.map.LevelFactory;
import com.mygdx.fuegopeligro.map.LevelRenderer;
import com.mygdx.fuegopeligro.physics.BodyEditorLoader;
import com.mygdx.fuegopeligro.player.PlayerStatusObserver;

/**
 * @author JDEsguerra
 */
public class LevelScreen extends AbstractScreen {
    private Telegram msg;
    private static final float GRAVITY = -9.8f;
    private static final String BODIES_DEFINITION_FILE = "bodies.json";

    private static final float TIME_STEP = 1 / 300f;
    private static final int POSITION_ITERATIONS = 3;
    private static final int VELOCITY_ITERATIONS = 8;

    private final World world;
    private final ScreenViewport viewport;
    private final Entity ninjaRabbit;
    private final Entity environment;
    private final StatusBar hud;
    private float accumulator;

    public LevelScreen(final FuegoPeligro game) {
        super(game);
        msg = new Telegram();
        world = new World(new Vector2(0.0f, GRAVITY), true);
        BodyEditorLoader bodyLoader = new BodyEditorLoader(Gdx.files.internal(BODIES_DEFINITION_FILE));
        hud = new StatusBar(game.getBatch(), game.getAssetsManager());
        hud.getPause().addListener(new ClickListener() {
            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
                msg.message = MessageType.BACK_TO_MENU.code();
                game.handleMessage(msg);
            }
        });
        ninjaRabbit = EntityFactory.createNinjaRabbit(game, world, bodyLoader, game.getAssetsManager(), game.getPlayerStatus(), hud);
        LevelRenderer mapRenderer = LevelFactory.create(world, bodyLoader, game.getBatch(), game.getAssetsManager(), game.getPlayerStatus()
                        .getWorld(), game.getPlayerStatus().getLevel(),
                1 / FuegoPeligro.PPM);
        environment = EntityFactory.createEnvironment(game, world, game.getBatch(), mapRenderer, game.getAssetsManager(),
                game.getPlayerStatus(), (PlayerStatusObserver[]) null);
        viewport = new ScreenViewport();
        viewport.setUnitsPerPixel(1 / FuegoPeligro.PPM);
        viewport.setCamera(new BoundedCamera(0.0f,
                mapRenderer.getTiledMap().getProperties().get("width", Integer.class).floatValue()
                        * mapRenderer.getTiledMap().getProperties().get("tilewidth", Integer.class).floatValue()
                        / FuegoPeligro.PPM));
    }

    /*
     * (non-Javadoc)
     *
     * @see com.badlogic.gdx.Screen#render(float)
     */
    @Override
    public void render(final float delta) {
        accumulator += Math.min(delta, 0.25f);
        while (accumulator >= TIME_STEP) {
            world.step(TIME_STEP, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
            accumulator -= TIME_STEP;
        }

        ninjaRabbit.update(viewport.getCamera());
        viewport.getCamera().update();
        game.getBatch().setProjectionMatrix(viewport.getCamera().combined);
        environment.update(viewport.getCamera());

        game.getBatch().begin();
        ninjaRabbit.step(game.getBatch());
        environment.step(game.getBatch());
        game.getBatch().end();

        MessageManager.getInstance().update();

        hud.render();
    }

    /*
     * (non-Javadoc)
     *
     * @see com.badlogic.gdx.Screen#resize(int, int)
     */
    @Override
    public void resize(final int width, final int height) {
        viewport.update(width, height, true);
        hud.resize(width, height);
        ninjaRabbit.resize(width, height);
        environment.resize(width, height);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.badlogic.gdx.Screen#show()
     */
    @Override
    public void show() {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     *
     * @see com.badlogic.gdx.Screen#pause()
     */
    @Override
    public void pause() {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     *
     * @see com.badlogic.gdx.Screen#resume()
     */
    @Override
    public void resume() {
        // TODO Auto-generated method stub

    }

    @Override
    public void hide() {
        // TODO Auto-generated method stub
    }

    /*
     * (non-Javadoc)
     *
     * @see com.badlogic.gdx.Screen#dispose()
     */
    @Override
    public void dispose() {
        ninjaRabbit.dispose();
        environment.dispose();
        world.dispose();
        hud.dispose();
    }
}
