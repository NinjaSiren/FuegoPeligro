package com.mygdx.fuegopeligro.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.mygdx.fuegopeligro.FuegoPeligro;
import com.mygdx.fuegopeligro.GameOverOverlay;
import com.mygdx.fuegopeligro.LevelEndOverlay;
import com.mygdx.fuegopeligro.ai.msg.MessageType;
import com.mygdx.fuegopeligro.entity.Entity;
import com.mygdx.fuegopeligro.map.LevelRenderer;

/**
 * @author JDEsguerra
 */
public class LevelGraphicsProcessor implements GraphicsProcessor, Telegraph {
    private final LevelRenderer mapRenderer;
    private final GameOverOverlay gameOver;
    private final LevelEndOverlay levelEnd;

    private boolean renderGameOver;
    private boolean renderLevelEnd;

    public LevelGraphicsProcessor(final AssetManager assets, final Batch batch, final LevelRenderer mapRenderer, final FuegoPeligro game) {
        gameOver = new GameOverOverlay(batch, assets, game);
        levelEnd = new LevelEndOverlay(batch, assets, game);
        this.mapRenderer = mapRenderer;
        MessageManager.getInstance().addListeners(this, MessageType.GAME_OVER.code());
        MessageManager.getInstance().addListeners(this, MessageType.FINISH_LEVEL.code());
    }

    @Override
    public void update(final Entity character, final Camera camera) {
        mapRenderer.render((OrthographicCamera) camera);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.mygdx.fuegopeligro.graphics.GraphicsProcessor#draw(com.mygdx.fuegopeligro.entity.Entity,
     * com.badlogic.gdx.graphics.g2d.Batch)
     */
    @Override
    public void draw(final Entity entity, final Batch batch) {
        mapRenderer.update();

        if (renderGameOver) {
            gameOver.render(Gdx.graphics.getDeltaTime());
        } else if (renderLevelEnd) {
            levelEnd.render(Gdx.graphics.getDeltaTime());
        }
    }

    @Override
    public boolean handleMessage(final Telegram msg) {
        renderGameOver = msg.message == MessageType.GAME_OVER.code();
        renderLevelEnd = msg.message == MessageType.FINISH_LEVEL.code();
        return true;
    }

    @Override
    public void resize(final int width, final int height) {
        gameOver.resize(width, height);
    }

    @Override
    public void dispose() {
        gameOver.dispose();
    }
}
