package com.mygdx.fuegopeligro;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.fuegopeligro.ai.msg.MessageType;
import com.mygdx.fuegopeligro.map.LevelFactory;

public class LevelSelectionScreen extends AbstractScreen {
    private static final String TITLE = "Stage and Level Select";
    private static final String EASY = "Level 1";
    private static final String HARD = "Level 2";
    private static final String STAGE_ONE = "Fire Station";
    private static final String STAGE_TWO = "Open Fields";
    private static final String STAGE_THREE = "Suburbs";
    private static final String STAGE_FOUR = "Downtown";
    private static final String STAGE_FIVE = "House";
    private static final String SELECT_LEVEL = "Start Game";
    private static final String RETURN_TO_MENU = "Back";

    private final Stage stage;
    private final Telegram msg;

    LevelSelectionScreen(final FuegoPeligro game) {
        super(game);
        msg = new Telegram();

        stage = new Stage(new ScreenViewport(), game.getBatch());
        stage.clear();

        Label.LabelStyle style = new Label.LabelStyle();
        style.fontColor = Color.WHITE;
        style.font = game.getAssetsManager().get(Assets.HUD_FONT);
        Skin skin = game.getAssetsManager().get(Assets.GAME_UI_SKIN);

        Label titleLabel = new Label(TITLE, style);
        Label stageOneLabel = new Label(STAGE_ONE, style);
        Label stageTwoLabel = new Label(STAGE_TWO, style);
        Label stageThreeLabel = new Label(STAGE_THREE, style);
        Label stageFourLabel = new Label(STAGE_FOUR, style);
        Label stageFiveLabel = new Label(STAGE_FIVE, style);
        titleLabel.setFontScale(1.2f);

        final Table table = new Table();
        final Table table2 = new Table();

        final SelectBox<String> stageOne = new SelectBox<>(skin);
        final SelectBox<String> stageTwo = new SelectBox<>(skin);
        final SelectBox<String> stageThree = new SelectBox<>(skin);
        final SelectBox<String> stageFour = new SelectBox<>(skin);
        final SelectBox<String> stageFive = new SelectBox<>(skin);

        stageOne.setItems(EASY, HARD);
        stageTwo.setItems(EASY, HARD);
        stageThree.setItems(EASY, HARD);
        stageFour.setItems(EASY, HARD);
        stageFive.setItems(EASY, HARD);

        stageOne.pack();
        stageTwo.pack();
        stageThree.pack();
        stageFour.pack();
        stageFive.pack();

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = game.getAssetsManager().get(Assets.HUD_FONT);

        TextButton selectLevel = new TextButton(SELECT_LEVEL, skin);
        selectLevel.addListener(new ClickListener() {
            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
                switch (stageOne.getSelected()) {
                    case EASY:
                        LevelFactory.LEVEL_MAP_FILE = "map/stage.%s.1.tmx";
                        msg.message = MessageType.LOAD_NEW_GAME.code();
                        game.handleMessage(msg);
                        break;
                    case HARD:
                        LevelFactory.LEVEL_MAP_FILE = "map/stage.%s.2.tmx";
                        msg.message = MessageType.LOAD_NEW_GAME.code();
                        game.handleMessage(msg);
                        break;
                }
            }
        });

        TextButton backMenu = new TextButton(RETURN_TO_MENU, skin);
        selectLevel.addListener(new ClickListener() {
            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
                msg.message = MessageType.BACK_TO_MENU.code();
                game.handleMessage(msg);
            }
        });

        table.add(stageOneLabel).pad(0, 0, 0, 20);
        table.add(stageTwoLabel).pad(0, 20, 0, 20);
        table.add(stageThreeLabel).pad(0, 20, 0, 20);
        table.add(stageFourLabel).pad(0, 20, 0, 20);
        table.add(stageFiveLabel).pad(0, 20, 0, 0);
        table.row().pad(10, 0, 0, 0);
        table.add(stageOne).center();
        table.add(stageTwo).center();
        table.add(stageThree).center();
        table.add(stageFour).center();
        table.add(stageFive).center();

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

        ScrollPane scrollWindow = new ScrollPane(table, skin);
        scrollWindow.layout();
        scrollWindow.setScrollBarTouch(true);
        scrollWindow.setSmoothScrolling(true);
        scrollWindow.setFlingTime(0.1f);
        table2.add(titleLabel).colspan(2);
        table2.row().pad(stage.getHeight() / 16, 0, 0, 0);
        table2.add(scrollWindow).colspan(2);
        table2.row().pad(stage.getHeight() / 16, 0, 0, 0);
        table2.add(selectLevel).colspan(1);
        table2.add(backMenu).colspan(1);

        table2.setFillParent(true);
        table2.setDebug(true);
        stage.addActor(table2);
        stage.setKeyboardFocus(table);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {
        // TODO Auto-generated method stub
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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
