package com.mygdx.fuegopeligro.graphics.minigames;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.fuegopeligro.Assets;

public class LetterPuzzle implements Disposable, Screen {
    private static final String QUESTION_LABEL = "CHECKPOINT: LETTER PUZZLE";
    private static final String HINT_ANSWER = "HINT";

    public Stage stage;
    public TextButton enterHints;
    public Table table;

    public Label questionText;
    public TextButton answer1;
    public TextButton answer2;
    public TextButton answer3;
    public TextButton answer4;

    public LetterPuzzle(final AssetManager assets, final Batch batch) {
        stage = new Stage(new ScreenViewport(), batch);

        Label.LabelStyle style = new Label.LabelStyle();
        AssetManager assetManager = new AssetManager();
        assetManager.load(Assets.GAME_UI_SKIN);
        assetManager.finishLoading();
        Skin skin = assetManager.get(Assets.GAME_UI_SKIN);

        style.fontColor = Color.WHITE;
        style.font = assets.get(Assets.HUD_FONT);
        Label questionLabel = new Label(QUESTION_LABEL, style);

        style.fontColor = Color.WHITE;
        style.font = assets.get(Assets.HUD_FONT);
        questionText = new Label("", style);

        // Buttons
        answer1 = new TextButton("", skin);
        answer2 = new TextButton("", skin);
        answer3 = new TextButton("", skin);
        answer4 = new TextButton("", skin);

        // enter hints
        enterHints = new TextButton(HINT_ANSWER, skin);

        table = new Table();
        table.setFillParent(true);

        table.add(questionLabel).expand(true, false).center();
        table.row().pad(20, 0, 0, 10);
        table.add(questionText).expand(true, false).center();
        table.row().pad(10, 0, 0, 20);
        table.add(answer1).expand(true, false);
        table.add(answer2).expand(true, false);
        table.row().pad(10, 0, 0, 20);
        table.add(answer3).expand(true, false);
        table.add(answer4).expand(true, false);
        table.row().pad(10, 0, 0, 20);
        table.add(enterHints).expand(true, false);

        stage.addActor(table);
        stage.setKeyboardFocus(table);
    }

    @Override
    public void show() {
        table.setVisible(true);
    }

    @Override
    public void render(float delta) {
        Gdx.gl20.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        Gdx.gl20.glDisable(GL20.GL_BLEND);
        stage.getBatch().end();
        stage.act(delta);
        stage.draw();
        stage.getBatch().begin();
    }

    @Override
    public void resize(final int width, final int height) {
        stage.getViewport().update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        table.setVisible(false);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
