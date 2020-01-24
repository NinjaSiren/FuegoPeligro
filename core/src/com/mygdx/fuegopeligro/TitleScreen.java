package com.mygdx.fuegopeligro;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.fuegopeligro.ai.msg.MessageType;

/**
 * Shows the game title and gives the player the option to start it or exit. This is the first
 * screen shown.
 *
 * @author JDEsguerra
 */
public class TitleScreen extends AbstractScreen {
    private static final String CARROT_REGION = "carrot";
    private static final String TITLE = "Fuego Peligro";
    private static final String BEGIN_OPTION = "New Game";
    private static final String RETURN_OPTION = "Continue Game";
    private static final String PREFS_OPTION = "Settings";
    private static final String EXIT_OPTION = "Exit game";

    private final Stage stage;
    private final Telegram msg;
    private final float x1, y1;

    TitleScreen(final FuegoPeligro game) {
        super(game);
        stage = new Stage(new ScreenViewport(), game.getBatch());
        stage.clear();
        msg = new Telegram();

        Label.LabelStyle style = new Label.LabelStyle();
        style.fontColor = Color.WHITE;
        style.font = game.getAssetsManager().get(Assets.HUD_FONT);
        Image logo = new Image(game.getAssetsManager().get(Assets.NINJA_RABBIT_ATLAS).findRegion(CARROT_REGION));

        Label titleLabel = new Label(TITLE, style);
        titleLabel.setFontScale(1.2f);
        final Table table = new Table();
        table.add(logo).colspan(2).row();
        table.add(titleLabel).padBottom(60.0f).colspan(2).row();

        final Image exitIcon, beginIcon, returnIcon, prefsIcon;
        exitIcon = new Image(game.getAssetsManager().get(Assets.SWORD));
        exitIcon.setVisible(false);
        beginIcon = new Image(game.getAssetsManager().get(Assets.SWORD));
        returnIcon = new Image(game.getAssetsManager().get(Assets.SWORD));
        returnIcon.setVisible(false);
        prefsIcon = new Image(game.getAssetsManager().get(Assets.SWORD));
        prefsIcon.setVisible(false);

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = game.getAssetsManager().get(Assets.HUD_FONT);
        x1 = buttonStyle.font.getData().scaleX;
        y1 = buttonStyle.font.getData().scaleY;
        buttonStyle.font.getData().setScale(stage.getWidth() / 512);

        TextButton beginButton = new TextButton(BEGIN_OPTION, buttonStyle);
        beginButton.setSize(stage.getWidth() / 4, stage.getHeight() / 4);
        beginButton.addListener(new ClickListener() {
            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
                msg.message = MessageType.LEVEL_SELECTION.code();
                game.handleMessage(msg);
                buttonStyle.font.getData().setScale(x1, y1);
            }

            @Override
            public void enter(final InputEvent event, final float x, final float y, final int pointer, final Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);
                beginIcon.setVisible(true);
                returnIcon.setVisible(false);
                prefsIcon.setVisible(false);
                exitIcon.setVisible(false);
            }
        });

        TextButton returnButton = new TextButton(RETURN_OPTION, buttonStyle);
        returnButton.setSize(stage.getWidth() / 4, stage.getHeight() / 4);
        returnButton.addListener(new ClickListener() {
            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
                msg.message = MessageType.LOAD_CURRENT_GAME.code();
                game.handleMessage(msg);
                buttonStyle.font.getData().setScale(x1, y1);
            }

            @Override
            public void enter(final InputEvent event, final float x, final float y, final int pointer, final Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);
                beginIcon.setVisible(false);
                returnIcon.setVisible(true);
                prefsIcon.setVisible(false);
                exitIcon.setVisible(false);
            }
        });

        TextButton prefsButton = new TextButton(PREFS_OPTION, buttonStyle);
        prefsButton.setSize(stage.getWidth() / 4, stage.getHeight() / 4);
        prefsButton.addListener(new ClickListener() {
            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
                msg.message = MessageType.PREFERENCES_SCREEN.code();
                game.handleMessage(msg);
                buttonStyle.font.getData().setScale(x1, y1);
            }

            @Override
            public void enter(final InputEvent event, final float x, final float y, final int pointer, final Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);
                beginIcon.setVisible(false);
                returnIcon.setVisible(false);
                prefsIcon.setVisible(true);
                exitIcon.setVisible(false);
            }
        });

        TextButton exitButton = new TextButton(EXIT_OPTION, buttonStyle);
        exitButton.setSize(stage.getWidth() / 4, stage.getHeight() / 4);
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
                Gdx.app.exit();
                buttonStyle.font.getData().setScale(x1, y1);
            }

            @Override
            public void enter(final InputEvent event, final float x, final float y, final int pointer, final Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);
                beginIcon.setVisible(false);
                returnIcon.setVisible(false);
                prefsIcon.setVisible(false);
                exitIcon.setVisible(true);
            }
        });

        table.setBackground(new TextureRegionDrawable(new TextureRegion(new Texture("menubg.png"))));
        table.add(beginIcon);
        table.add(beginButton).expand(true, true).center().row();
        table.add(returnIcon);
        table.add(returnButton).expand(true, true).center().row();
        table.add(prefsIcon);
        table.add(prefsButton).expand(true, true).center().row();
        table.add(exitIcon);
        table.add(exitButton).expand(true, true).center().row();
        table.setFillParent(true);

        table.addListener(new InputListener() {
            @Override
            public boolean keyDown(final InputEvent event, final int keycode) {
                switch (keycode) {
                    case Keys.W:
                    case Keys.UP:
                    case Keys.S:
                    case Keys.DOWN:
                    case Keys.SPACE:
                    case Keys.ENTER:
                    case Keys.BUTTON_A:
                }
                return super.keyDown(event, keycode);
            }
        });

        stage.addActor(table);
        stage.setKeyboardFocus(table);
        Gdx.input.setInputProcessor(stage);
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
        // TODO Auto-generated method stub

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

}
