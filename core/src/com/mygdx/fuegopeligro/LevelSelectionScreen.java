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
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.fuegopeligro.ai.msg.MessageType;

public class LevelSelectionScreen extends AbstractScreen {
    private static final String TITLE = "Stage and Level Select";
    private static final String STAGE_ONE = "Fire Station";
    private static final String STAGE_TWO = "Open Fields";
    private static final String STAGE_THREE = "Suburbs";
    private static final String STAGE_FOUR = "Downtown";
    private static final String STAGE_FIVE = "House";
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
        stageOneLabel.setAlignment(Align.center);
        stageTwoLabel.setAlignment(Align.center);
        stageThreeLabel.setAlignment(Align.center);
        stageFourLabel.setAlignment(Align.center);
        stageFiveLabel.setAlignment(Align.center);
        titleLabel.setAlignment(Align.center);
        titleLabel.setFontScale(1.2f);

        final Table table = new Table();
        final Table table2 = new Table();

        final TextButton S1L1 = new TextButton("EASY", skin);
        S1L1.addListener(new ClickListener() {
            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
                msg.message = MessageType.LOAD_NEW_GAME.code();
                game.handleMessage(msg);
            }
        });
        
        final TextButton S1L2 = new TextButton("HARD", skin);
        S1L2.addListener(new ClickListener() {
            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
                game.setLevel(1, 2);
            }
        });
        
        final TextButton S2L1 = new TextButton("EASY", skin);
        S2L1.addListener(new ClickListener() {
            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
                game.setLevel(2, 1);
            }
        });

        final TextButton S2L2 = new TextButton("HARD", skin);
        S2L2.addListener(new ClickListener() {
            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
                game.setLevel(2, 2);
            }
        });
        
        final TextButton S3L1 = new TextButton("EASY", skin);
        S3L1.addListener(new ClickListener() {
            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
                game.setLevel(3, 1);
            }
        });

        final TextButton S3L2 = new TextButton("HARD", skin);
        S3L2.addListener(new ClickListener() {
            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
                game.setLevel(3, 2);
            }
        });
        
        final TextButton S4L1 = new TextButton("EASY", skin);
        S4L1.addListener(new ClickListener() {
            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
                game.setLevel(4, 1);
            }
        });

        final TextButton S4L2 = new TextButton("HARD", skin);
        S4L2.addListener(new ClickListener() {
            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
                game.setLevel(4, 2);
            }
        });
        
        final TextButton S5L1 = new TextButton("EASY", skin);
        S5L1.addListener(new ClickListener() {
            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
                game.setLevel(5, 1);
            }
        });

        final TextButton S5L2 = new TextButton("HARD", skin);
        S5L2.addListener(new ClickListener() {
            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
                game.setLevel(5, 2);
            }
        });

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = game.getAssetsManager().get(Assets.HUD_FONT);

        TextButton backMenu = new TextButton(RETURN_TO_MENU, skin);
        backMenu.addListener(new ClickListener() {
            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
                msg.message = MessageType.BACK_TO_MENU.code();
                game.handleMessage(msg);
            }
        });

        Table S1 = new Table();
        S1.add(S1L1);
        S1.row().padTop(20);
        S1.add(S1L2);
        
        Table S2 = new Table();
        S2.add(S2L1);
        S2.row().padTop(20);
        S2.add(S2L2);
        
        Table S3 = new Table();
        S3.add(S3L1);
        S3.row().padTop(20);
        S3.add(S3L2);
        
        Table S4 = new Table();
        S4.add(S4L1);
        S4.row().padTop(20);
        S4.add(S4L2);
        
        Table S5 = new Table();
        S5.add(S5L1);
        S5.row().padTop(20);
        S5.add(S5L2);
        
        table.add(stageOneLabel).expandX().colspan(1).fill();
        table.add(stageTwoLabel).expandX().colspan(1).fill();
        table.add(stageThreeLabel).expandX().colspan(1).fill();
        table.add(stageFourLabel).expandX().colspan(1).fill();
        table.add(stageFiveLabel).expandX().colspan(1).fill();
        table.row().padTop(40);
        table.add(S1).center().expandX().colspan(1).fill();
        table.add(S2).center().expandX().colspan(1).fill();
        table.add(S3).center().expandX().colspan(1).fill();
        table.add(S4).center().expandX().colspan(1).fill();
        table.add(S5).center().expandX().colspan(1).fill();

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

        table2.add(titleLabel).colspan(3).expandX().fill();
        table2.row().padTop(stage.getHeight() / 16);
        table2.add(table).colspan(3).expandX().fill();
        table2.row().padTop(stage.getHeight() / 16);
        table2.add(backMenu).colspan(1).expandX();
        table2.center().pad(50);

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
