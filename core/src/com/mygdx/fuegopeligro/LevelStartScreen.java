package com.mygdx.fuegopeligro;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.fuegopeligro.player.CurrentPlayerStatus;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

/**
 * Preview screen that shows stats and information about the player and the current level, before it
 * begins.
 *
 * @author JDEsguerra
 */
public class LevelStartScreen extends AbstractScreen {
    private static final String THREE_DIGITS = "%03d";
    private static final String EIGHT_DIGITS = "%08d";
    private static final String TWO_DIGITS = "%02d";
    private static final String LIVES_FORMAT = "x " + TWO_DIGITS;
    private static final String LEVEL_LABEL = "Level";
    private static final String LEVEL_FORMAT = "%s - %s";

    private static final String TIME_REGION = "time";
    private static final String LIVES_REGION = "lives";
    private static final String SMALL_CARROT_REGION = "carrot-small";

    private Stage stage;
    private Screen levelScreen;
    private int world;
    private int level;

    public LevelStartScreen(final FuegoPeligro game, int worldNumber, int levelNumber) {
        super(game);
        CurrentPlayerStatus playerStatus = game.getPlayerStatus();

        setLevel(levelNumber);
        setWorld(worldNumber);
        playerStatus.setLevel(levelNumber);
        playerStatus.setWorld(worldNumber);

        stage = new Stage(new ScreenViewport(), game.getBatch());
        Label.LabelStyle style = new Label.LabelStyle();
        style.fontColor = Color.WHITE;
        style.font = game.getAssetsManager().get(Assets.HUD_FONT);

        TextureAtlas hudAtlas = game.getAssetsManager().get(Assets.NINJA_RABBIT_ATLAS);

        Label collectiblesLabel = new Label(String.format(TWO_DIGITS, playerStatus.getCollectibles()), style);
        Label livesLabel = new Label(String.format(LIVES_FORMAT, playerStatus.getLives()), style);
        Label scoreLabel = new Label(String.format(EIGHT_DIGITS, playerStatus.getScore()), style);
        Label timeLabel = new Label(String.format(THREE_DIGITS, playerStatus.getTime()), style);

        Label controls = new Label("CONTROLS", style);
        controls.setAlignment(Align.center);
        Label left = new Label("TAP LEFT to go LEFT,", style);
        left.setAlignment(Align.right);
        Label right = new Label("TAP RIGHT to go RIGHT.", style);
        right.setAlignment(Align.left);
        Label jump = new Label("DOUBLE TAP to JUMP,", style);
        jump.setAlignment(Align.center);

        Table status = new Table();
        status.add(new Image(hudAtlas.findRegion(SMALL_CARROT_REGION))).padRight(4.0f);
        status.add(collectiblesLabel).bottom();
        status.add(scoreLabel).expandX();
        status.add(new Image(hudAtlas.findRegion(TIME_REGION))).padRight(12.0f);
        status.add(timeLabel).row();
        status.setFillParent(true);
        status.top();
        status.pad(15.0f);
        stage.addActor(status);

        Table levelInfo = new Table();
        levelInfo.add(new Label(LEVEL_LABEL, style)).expandX().right().padRight(18.0f).colspan(1);
        levelInfo.add(new Label(String.format(LEVEL_FORMAT, playerStatus.getWorld(),
                playerStatus.getLevel()), style)).expandX().left().padTop(18.0f).colspan(1).row();
        Image livesIcon = new Image(hudAtlas.findRegion(LIVES_REGION));
        levelInfo.add(livesIcon).expandX().right().spaceRight(25f).colspan(1);
        levelInfo.add(livesLabel).expandX().left().bottom().colspan(1);

        Table controlsInfo = new Table();
        controlsInfo.add(levelInfo).colspan(3).expandX().padTop(50.0f);
        controlsInfo.row().padTop(Gdx.graphics.getHeight() / 4);
        controlsInfo.add(controls).expandX().center().colspan(3);
        controlsInfo.row();
        controlsInfo.add(left).expandX().fill().left().colspan(1);
        controlsInfo.add(jump).expandX().fill().center().colspan(1);
        controlsInfo.add(right).expandX().fill().right().colspan(1);
        controlsInfo.center().pad(50);
        controlsInfo.setFillParent(true);

        stage.addActor(controlsInfo);
    }

    @Override
    public void render(final float delta) {
        stage.act(delta);
        stage.draw();

    }

    @Override
    public void resize(final int width, final int height) {
        stage.getViewport().update(width, height, true);

    }

    @Override
    public void show() {
        // Fade in / fade out effect
        stage.addAction(sequence(fadeIn(0.75f), delay(1.75f), fadeOut(0.35f), new Action() {
            @Override
            public boolean act(final float delta) {
                // Last action will move to the next screen
                if (levelScreen == null) {
                    game.setScreen(new LevelScreen(game, getWorld(), getLevel()));
                } else {
                    game.setScreen(levelScreen);
                }
                return true;
            }
        }));
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void pause() {
        // TODO Auto-generated method stub
    }

    @Override
    public void resume() {
        // TODO Auto-generated method stub

    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    public int getWorld() {
        return world;
    }

    public void setWorld(int world) {
        this.world = world;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
