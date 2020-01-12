package com.mygdx.fuegopeligro.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.fuegopeligro.Assets;
import com.mygdx.fuegopeligro.FuegoPeligro;

public class ControlButtons {

    private Stage overlay;
    private Telegram msg;

    public ControlButtons(final Batch batch, final AssetManager assets, final FuegoPeligro game) {
        overlay = new Stage(new ScreenViewport(), batch);
        msg = new Telegram();

        Skin skin = assets.get(Assets.GAME_UI_SKIN);

        final TextButton left = new TextButton("Left", skin);
        left.addListener(new ClickListener() {
            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
            }
        });

        final TextButton right = new TextButton("Right", skin);
        right.addListener(new ClickListener() {
            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
            }
        });

        final TextButton jump = new TextButton("Jump", skin);
        jump.addListener(new ClickListener() {
            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
            }
        });

        Table table = new Table();
        table.setDebug(true);
        table.add(left).colspan(5).center();
        table.add(right).colspan(5).center();
        table.add(jump).colspan(5).right();
        table.setFillParent(true);
        table.bottom();
        table.pad(15.0f);

        overlay.addActor(table);
    }

    public void resize(int width, int height) {
        overlay.getViewport().update(width, height, true);
    }

    public void render() {
        overlay.act();
        overlay.draw();
        Gdx.input.setInputProcessor(overlay);
    }

    public void dispose() {
        overlay.dispose();
    }
}
