package com.mygdx.fuegopeligro.minigames;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.fuegopeligro.Assets;
import com.mygdx.fuegopeligro.FuegoPeligro;
import com.mygdx.fuegopeligro.ai.msg.MessageType;
import com.mygdx.fuegopeligro.graphics.LevelGraphicsProcessor;

public class FourPicsOneWord implements Disposable {
    private static final String QUESTION_LABEL = "CHECKPOINT: 4 PICS 1 WORD";
    private static final String ENTER_ANSWER = "ENTER";
    private static final String HINT_ANSWER = "HINT";

    private final Stage stage;
    private final ShapeRenderer overlay;
    private final Telegram msg;

    public FourPicsOneWord(final Batch batch, final AssetManager assets, final FuegoPeligro game, final LevelGraphicsProcessor levelGraphicsProcessor){
        stage = new Stage(new ScreenViewport(), batch);
        msg = new Telegram();

        Label.LabelStyle style = new Label.LabelStyle();
        AssetManager assetManager = new AssetManager();
        assetManager.load(Assets.GAME_UI_SKIN);
        assetManager.finishLoading();
        Skin skin = assetManager.get(Assets.GAME_UI_SKIN);

        style.fontColor = Color.WHITE;
        style.font = assets.get(Assets.HUD_FONT);
        Label QuestionLabel = new Label(QUESTION_LABEL, style);

        style.fontColor = Color.WHITE;
        style.font = assets.get(Assets.HUD_FONT);
        Label QuestionText = new Label("", style);

        TextButton answer1 = new TextButton("", skin);
        answer1.addListener(new ClickListener() {
            @Override
            public void clicked(final InputEvent event, final float x, final float y) {

            }
        });
        TextButton answer2 = new TextButton("", skin);
        answer2.addListener(new ClickListener() {
            @Override
            public void clicked(final InputEvent event, final float x, final float y) {

            }
        });
        TextButton answer3 = new TextButton("", skin);
        answer3.addListener(new ClickListener() {
            @Override
            public void clicked(final InputEvent event, final float x, final float y) {

            }
        });
        TextButton answer4 = new TextButton("", skin);
        answer4.addListener(new ClickListener() {
            @Override
            public void clicked(final InputEvent event, final float x, final float y) {

            }
        });

        // enter answer
        TextButton enterAnswer = new TextButton(ENTER_ANSWER, skin);
        enterAnswer.addListener(new ClickListener() {
            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
                disposed(levelGraphicsProcessor);
            }
        });

        // enter hints
        TextButton enterHints = new TextButton(HINT_ANSWER, skin);
        enterHints.addListener(new ClickListener() {
            @Override
            public void clicked(final InputEvent event, final float x, final float y) {

            }
        });

        Table table = new Table();
        table.setFillParent(true);
        table.setDebug(true);

        table.add(QuestionLabel).expand(true, false).center();
        table.row().pad(20, 0, 0, 10);
        table.add(QuestionText).expand(true, false);
        table.row().pad(10, 0, 0, 20);
        table.add(answer1).expand(true, false);
        table.add(answer2).expand(true, false);
        table.row().pad(10, 0, 0, 20);
        table.add(answer3).expand(true, false);
        table.add(answer4).expand(true, false);
        table.row().pad(10, 0, 0, 20);
        table.add(enterAnswer).expand(true, false);
        table.add(enterHints).expand(true, false);

        stage.addActor(table);
        stage.setKeyboardFocus(table);
        overlay = new ShapeRenderer();
    }

    public void render(final float delta) {
        Gdx.gl20.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        overlay.setProjectionMatrix(stage.getViewport().getCamera().combined);
        overlay.begin(ShapeRenderer.ShapeType.Filled);
        overlay.setColor(0.1f, 0.1f, 0.1f, 0.33f);
        overlay.rect(-10, -10, stage.getViewport().getWorldWidth() + 20,
                stage.getViewport().getWorldHeight() + 20);
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
        stage.dispose();
        overlay.dispose();
    }

    private void disposed(final LevelGraphicsProcessor levelGraphicsProcessor) {
        msg.message = MessageType.END_MINIGAME_OVERLAY.code();
        levelGraphicsProcessor.handleMessage(msg);
    }
}
