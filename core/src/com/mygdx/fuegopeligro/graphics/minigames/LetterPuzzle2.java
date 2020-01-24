package com.mygdx.fuegopeligro.graphics.minigames;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.fuegopeligro.Assets;
import com.mygdx.fuegopeligro.FuegoPeligro;

public class LetterPuzzle2 implements Disposable {
    private static final String QUESTION_LABEL = "CHECKPOINT: LETTER PUZZLE";
    private static final String HINT_ANSWER = "HINT";

    private Stage stage;
    private TextButton enterHints;
    private Table table;

    private Label questionText;
    private TextButton answer1;
    private TextButton answer2;
    private TextButton answer3;
    private TextButton answer4;

    public LetterPuzzle2(final AssetManager assets, final FuegoPeligro game) {
        stage = new Stage(new ScreenViewport(), game.getBatch());

        Label.LabelStyle style = new Label.LabelStyle();
        AssetManager assetManager = new AssetManager();
        assetManager.load(Assets.GAME_UI_SKIN);
        assetManager.finishLoading();
        Skin skin = assetManager.get(Assets.GAME_UI_SKIN);

        style.fontColor = Color.WHITE;
        style.font = assets.get(Assets.HUD_FONT);
        Label questionLabel = new Label(QUESTION_LABEL, style);
        questionLabel.setAlignment(Align.center);

        style.fontColor = Color.WHITE;
        style.font = assets.get(Assets.HUD_FONT);
        questionText = new Label("", style);
        questionText.setAlignment(Align.center);

        // Buttons
        answer1 = new TextButton("", skin);
        answer2 = new TextButton("", skin);
        answer3 = new TextButton("", skin);
        answer4 = new TextButton("", skin);

        // enter hints
        enterHints = new TextButton(HINT_ANSWER, skin);

        table = new Table();
        table.setFillParent(true);

        table.add(questionLabel).expand(true, false).fill().colspan(3).center();
        table.row().pad(20, 0, 0, 10);
        table.add(questionText).expand(true, false).fill().colspan(3).center();
        table.row().pad(10, 0, 0, 20);
        table.add(answer1).expand(true, false).fill().colspan(1).center();
        table.add(answer2).expand(true, false).fill().colspan(1).center();
        table.row().pad(10, 0, 0, 20);
        table.add(answer3).expand(true, false).fill().colspan(1).center();
        table.add(answer4).expand(true, false).fill().colspan(1).center();
        table.row().pad(10, 0, 0, 20);
        table.add(enterHints).expand(true, false).colspan(3).center();

        stage.addActor(table);
        stage.setKeyboardFocus(table);
    }

    public void render(float delta) {
        Gdx.gl20.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        Gdx.gl20.glDisable(GL20.GL_BLEND);
        stage.getBatch().end();
        stage.act(delta);
        stage.draw();
        stage.getBatch().begin();
    }

    public void resize(final int width, final int height) {
        stage.getViewport().update(width, height);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    public TextButton getEnterHints() {
        return enterHints;
    }

    public Table getTable() {
        return table;
    }

    public TextButton getAnswer1() {
        return answer1;
    }

    public TextButton getAnswer2() {
        return answer2;
    }

    public TextButton getAnswer3() {
        return answer3;
    }

    public TextButton getAnswer4() {
        return answer4;
    }

    public Stage getStage() {
        return stage;
    }

    public Label getQuestionText() {
        return questionText;
    }
}
