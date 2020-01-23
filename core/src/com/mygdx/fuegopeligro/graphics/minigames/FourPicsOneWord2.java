package com.mygdx.fuegopeligro.graphics.minigames;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.fuegopeligro.Assets;

public class FourPicsOneWord2 implements Disposable, Screen {
    private static final String QUESTION_LABEL = "CHECKPOINT: 4 PICS 1 WORD";
    private static final String HINT_ANSWER = "HINT";
    private static final String ENTER_ANSWER = "ENTER";

    public Stage stage;
    public TextButton enterHints;
    public TextButton enterAnswer;

    public Image answer1;
    public Image answer2;
    public Image answer3;
    public Image answer4;
    public TextField input;
    public Table table;

    public FourPicsOneWord2(final AssetManager assets, final Batch batch) {
        stage = new Stage(new ScreenViewport(), batch);

        Label.LabelStyle style = new Label.LabelStyle();
        AssetManager assetManager = new AssetManager();
        assetManager.load(Assets.GAME_UI_SKIN);
        assetManager.finishLoading();
        Skin skin = assetManager.get(Assets.GAME_UI_SKIN);

        style.fontColor = Color.WHITE;
        style.font = assets.get(Assets.HUD_FONT);
        Label questionLabel = new Label(QUESTION_LABEL, style);

        // Images
        answer1 = new Image();
        answer2 = new Image();
        answer3 = new Image();
        answer4 = new Image();

        // Textfield
        input = new TextField("", skin);

        // enter answer
        enterAnswer = new TextButton(ENTER_ANSWER, skin);

        // enter hints
        enterHints = new TextButton(HINT_ANSWER, skin);

        table = new Table();
        table.setFillParent(true);
        table.setVisible(true);

        table.add(questionLabel).expand(true, false);
        table.row().pad(20, 0, 0, 10);
        table.add(answer1).expand(true, false).size(stage.getWidth()/4, stage.getHeight()/4);
        table.add(answer2).expand(true, false).size(stage.getWidth()/4, stage.getHeight()/4);
        table.row().pad(10, 0, 0, 20);
        table.add(answer3).expand(true, false).size(stage.getWidth()/4, stage.getHeight()/4);
        table.add(answer4).expand(true, false).size(stage.getWidth()/4, stage.getHeight()/4);
        table.row().pad(10, 0, 0, 20);
        table.add(input).expand(true,false).size(stage.getWidth()/2, stage.getHeight()/14);
        table.row().pad(10, 0, 0, 20);
        table.add(enterAnswer).expand(true, false);
        table.add(enterHints).expand(true, false);

        stage.addActor(table);
        stage.setKeyboardFocus(table);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(final float delta) {
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

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
