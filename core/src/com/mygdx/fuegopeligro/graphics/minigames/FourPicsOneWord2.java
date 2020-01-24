package com.mygdx.fuegopeligro.graphics.minigames;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.fuegopeligro.Assets;
import com.mygdx.fuegopeligro.FuegoPeligro;

public class FourPicsOneWord2 implements Disposable {
    private static final String QUESTION_LABEL = "CHECKPOINT: 4 PICS 1 WORD";
    private static final String HINT_ANSWER = "HINT";
    private static final String ENTER_ANSWER = "ENTER";

    private Stage stage;
    private TextButton enterHints;
    private TextButton enterAnswer;

    private Image answer1;
    private Image answer2;
    private Image answer3;
    private Image answer4;
    private TextField input;
    private Table table;

    public FourPicsOneWord2(final AssetManager assets, final FuegoPeligro game) {
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

        table.add(questionLabel).expand(true, false).fill().colspan(3).center();
        table.row().pad(20, 0, 0, 10);
        table.add(answer1).expand(true, false).fill().colspan(1).center();
        table.add(answer2).expand(true, false).fill().colspan(1).center();
        table.row().pad(10, 0, 0, 20);
        table.add(answer3).expand(true, false).fill().colspan(1).center();
        table.add(answer4).expand(true, false).fill().colspan(1).center();
        table.row().pad(10, 0, 0, 20);
        table.add(input).expand(true,false).fill().colspan(3).center();
        table.row().pad(10, 0, 0, 20);
        table.add(enterAnswer).expand(true, false).colspan(1).center();
        table.add(enterHints).expand(true, false).colspan(1).center();

        TextField.TextFieldStyle textFieldStyle = skin.get(TextField.TextFieldStyle.class);
        textFieldStyle.font.getData().setScale(1.8f);

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

    public void resize(final int width, final int height) {
        stage.getViewport().update(width, height);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    public Image getAnswer1() {
        return answer1;
    }

    public Image getAnswer3() {
        return answer3;
    }

    public Image getAnswer2() {
        return answer2;
    }

    public Image getAnswer4() {
        return answer4;
    }

    public Stage getStage() {
        return stage;
    }

    public Table getTable() {
        return table;
    }

    public TextButton getEnterHints() {
        return enterHints;
    }

    public TextButton getEnterAnswer() { return enterAnswer; }

    public TextField getInput() {
        return input;
    }
}
