package com.mygdx.fuegopeligro;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.fuegopeligro.ai.msg.MessageType;

/**
 * Shows a transparent overlay splash screen with a "Game Over" legend every time the player runs
 * out of lives.
 *
 * @author JDEsguerra
 */
public class LevelEndOverlay implements Disposable {
    private static final String LEVEL_END_TEXT = "LEVEL FINISHED";
    private static final String RESTART_LEVEL_LABEL = "RESTART";
    private static final String NEXT_LEVEL_LABEL = "NEXT";
    private static final String RETURN_TO_MENU = "MENU";

    private final Stage stage;
    private final ShapeRenderer overlay;
    private final Telegram msg;

    public LevelEndOverlay(final Batch batch, final AssetManager assets, final FuegoPeligro game) {
        stage = new Stage(new ScreenViewport(), batch);
        msg = new Telegram();

        Label.LabelStyle style = new Label.LabelStyle();
        style.fontColor = Color.WHITE;
        style.font = assets.get(Assets.HUD_FONT);
        Label gameOver = new Label(LEVEL_END_TEXT, style);

        AssetManager assetManager = new AssetManager();
        assetManager.load(Assets.GAME_UI_SKIN);
        assetManager.finishLoading();
        Skin skin = assetManager.get(Assets.GAME_UI_SKIN);

        // restart level
        TextButton restartLevel = new TextButton(RESTART_LEVEL_LABEL, skin);
        restartLevel.addCaptureListener(new ClickListener() {
            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
                msg.message = MessageType.RESET_CURRENT_LEVEL.code();
                game.handleMessage(msg);
            }
        });

        // next level
        TextButton nextLevel = new TextButton(NEXT_LEVEL_LABEL, skin);
        nextLevel.addListener(new ClickListener() {
            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
                msg.message = MessageType.LOAD_NEXT_LEVEL.code();
                game.handleMessage(msg);
            }
        });

        // return to menu
        TextButton returnToMenu = new TextButton(RETURN_TO_MENU, skin);
        returnToMenu.addListener(new ClickListener() {
            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
                msg.message = MessageType.BACK_TO_MENU.code();
                game.handleMessage(msg);
            }
        });

        Table table = new Table();
        table.setFillParent(true);

        table.add(gameOver).expand(true, false);
        table.row().pad(20, 0, 0, 10);
        table.add(nextLevel).expand(true, false);
        table.row().pad(10, 0, 0, 10);
        table.add(restartLevel).expand(true, false);
        table.row().pad(10, 0, 0, 20);
        table.add(returnToMenu).expand(true, false);

        table.addListener(new InputListener() {
            @Override
            public boolean keyDown(final InputEvent event, final int keycode) {
                switch (keycode) {
                    case Input.Keys.W:
                    case Input.Keys.UP:
                    case Input.Keys.S:
                    case Input.Keys.DOWN:
                    case Input.Keys.SPACE:
                    case Input.Keys.ENTER:
                    case Input.Keys.BUTTON_A:
                }
                return super.keyDown(event, keycode);
            }
        });

        stage.addActor(table);
        stage.setKeyboardFocus(table);
        overlay = new ShapeRenderer();
    }

    public void render(final float delta) {
        Gdx.gl20.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        overlay.setProjectionMatrix(stage.getViewport().getCamera().combined);
        overlay.begin(ShapeType.Filled);
        overlay.setColor(0.1f, 0.1f, 0.1f, 0.33f);
        overlay.rect(-10, -10, stage.getViewport().getWorldWidth() + 20, stage.getViewport().getWorldHeight() + 20);
        overlay.end();
        Gdx.gl20.glDisable(GL20.GL_BLEND);

        stage.getBatch().end();
        stage.act(delta);
        stage.draw();
        stage.getBatch().begin();
        Gdx.input.setInputProcessor(stage);
    }

    public void resize(final int width, final int height) {
        stage.getViewport().update(width, height);
    }

    @Override
    public void dispose() {
        overlay.dispose();
        stage.dispose();
    }
}
