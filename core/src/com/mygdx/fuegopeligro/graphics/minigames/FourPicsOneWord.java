package com.mygdx.fuegopeligro.graphics.minigames;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.fuegopeligro.Assets;
import com.mygdx.fuegopeligro.FuegoPeligro;
import com.mygdx.fuegopeligro.player.CurrentPlayerStatus;

public class FourPicsOneWord implements Disposable {
    private static final String QUESTION_LABEL = "CHECKPOINT: 4 PICS 1 WORD";
    private static final String HINT_ANSWER = "HINT";
    private int size = 1024;

    private String TEXTURE_VALUE_1 = "";
    private String TEXTURE_VALUE_2 = "";
    private String TEXTURE_VALUE_3 = "";
    private String TEXTURE_VALUE_4 = "";

    public final Stage stage;
    public final TextButton enterHints;
    private final Table table;

    public Label questionText;
    public Image answer1;
    public Image answer2;
    public Image answer3;
    public Image answer4;

    public FourPicsOneWord(final AssetManager assets, final FuegoPeligro game,
                          CurrentPlayerStatus status) {
        stage = new Stage(new ScreenViewport(), game.getBatch());

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
        
        answer1 = new Image();
        answer1.addListener(new ClickListener() {
            @Override
            public void clicked(final InputEvent event, final float x, final float y) {

            }
        });

        answer2 = new Image();
        answer2.addListener(new ClickListener() {
            @Override
            public void clicked(final InputEvent event, final float x, final float y) {

            }
        });

        answer3 = new Image();
        answer3.addListener(new ClickListener() {
            @Override
            public void clicked(final InputEvent event, final float x, final float y) {

            }
        });

        answer4 = new Image();
        answer4.addListener(new ClickListener() {
            @Override
            public void clicked(final InputEvent event, final float x, final float y) {

            }
        });

        // enter hints
        enterHints = new TextButton(HINT_ANSWER, skin);
        enterHints.addListener(new ClickListener() {
            @Override
            public void clicked(final InputEvent event, final float x, final float y) {

            }
        });

        table = new Table();
        table.setVisible(true);
        table.setFillParent(true);
        table.setDebug(true);

        table.add(questionLabel).expand(true, false).center();
        table.row().pad(20, 0, 0, 10);
        table.add(answer1).expand(true, false).size(stage.getWidth()/4, stage.getHeight()/4);
        table.add(answer2).expand(true, false).size(stage.getWidth()/4, stage.getHeight()/4);
        table.row().pad(10, 0, 0, 20);
        table.add(answer3).expand(true, false).size(stage.getWidth()/4, stage.getHeight()/4);
        table.add(answer4).expand(true, false).size(stage.getWidth()/4, stage.getHeight()/4);
        table.row().pad(10, 0, 0, 20);
        table.add(enterHints).expand(true, false);

        stage.addActor(table);
        stage.setKeyboardFocus(table);
    }

    public void render(final float delta) {
        Gdx.gl20.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        Gdx.gl20.glDisable(GL20.GL_BLEND);
        stage.getBatch().end();
        stage.act(delta);
        stage.draw();
        stage.getBatch().begin();
    }

    public void resize(final int width, final int height) { stage.getViewport().update(width, height); }

    @Override
    public void dispose() {
        stage.dispose();
    }

    public void setVisible(boolean value) {
        table.setVisible(value);
    }
}
