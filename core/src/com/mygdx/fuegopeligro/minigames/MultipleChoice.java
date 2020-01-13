package com.mygdx.fuegopeligro.minigames;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
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
import com.mygdx.fuegopeligro.entity.NinjaRabbit;
import com.mygdx.fuegopeligro.input.NinjaRabbitInputProcessor;

/**
 * Shows a transparent overlay splash screen with a "Minigame" legend every time the player in in
 * a checkpoint.
 *
 * @author JDEsguerra
 */
public class MultipleChoice implements Disposable {
    private static final String QUESTION_LABEL = "CHECKPOINT: MULTIPLE CHOICE";
    private static final String ENTER_ANSWER = "ENTER";
    private static final String HINT_ANSWER = "HINT";

    public final Stage stage;
    private final NinjaRabbit ninja;
    private final Label QuestionLabel;
    private final Label QuestionText;
    private final TextButton answer1;
    private final TextButton answer2;
    private final TextButton answer3;
    private final TextButton answer4;
    public final TextButton enterAnswer;
    private final TextButton enterHints;
    public final Table table;

    public MultipleChoice(final AssetManager assets, final FuegoPeligro game,
                          final NinjaRabbit ninjaRabbit) {
        stage = new Stage(new ScreenViewport(), game.getBatch());
        ninja = ninjaRabbit;

        Label.LabelStyle style = new Label.LabelStyle();
        AssetManager assetManager = new AssetManager();
        assetManager.load(Assets.GAME_UI_SKIN);
        assetManager.finishLoading();
        Skin skin = assetManager.get(Assets.GAME_UI_SKIN);

        style.fontColor = Color.WHITE;
        style.font = assets.get(Assets.HUD_FONT);
        QuestionLabel = new Label(QUESTION_LABEL, style);

        style.fontColor = Color.WHITE;
        style.font = assets.get(Assets.HUD_FONT);
        QuestionText = new Label("", style);

        answer1 = new TextButton("", skin);
        answer1.addListener(new ClickListener() {
            @Override
            public void clicked(final InputEvent event, final float x, final float y) {

            }
        });
        answer2 = new TextButton("", skin);
        answer2.addListener(new ClickListener() {
            @Override
            public void clicked(final InputEvent event, final float x, final float y) {

            }
        });
        answer3 = new TextButton("", skin);
        answer3.addListener(new ClickListener() {
            @Override
            public void clicked(final InputEvent event, final float x, final float y) {

            }
        });
        answer4 = new TextButton("", skin);
        answer4.addListener(new ClickListener() {
            @Override
            public void clicked(final InputEvent event, final float x, final float y) {

            }
        });

        // enter answer
        enterAnswer = new TextButton(ENTER_ANSWER, skin);
        enterAnswer.addListener(new ClickListener() {
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
        table.setVisible(false);

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
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(new NinjaRabbitInputProcessor(ninja));
        inputMultiplexer.addProcessor(stage);
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    public void resize(final int width, final int height) { stage.getViewport().update(width, height); }

    @Override
    public void dispose() { stage.dispose(); }
}