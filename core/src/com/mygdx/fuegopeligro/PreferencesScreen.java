package com.mygdx.fuegopeligro;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.fuegopeligro.ai.msg.MessageType;

/**
 * Shows game settings and other optional and important preferences.
 *
 * @author JDEsguerra
 */
public class PreferencesScreen extends AbstractScreen {

    private String volumeMusic;
    private String volumeSound;
    private String enabledMusic;
    private String enabledSound;

    private Label volumeMusicLabel;
    private Label volumeSoundEffectLabel;
    private Label musicEnabledLabel;
    private Label soundEffectEnabledLabel;

    private Stage stage;
    private Telegram msg;

    PreferencesScreen(final FuegoPeligro game) {
        super(game);
        msg = new Telegram();
        stage = new Stage(new ScreenViewport(), game.getBatch());
        stage.clear();

        Label.LabelStyle style = new Label.LabelStyle();
        style.fontColor = Color.WHITE;
        style.font = game.getAssetsManager().get(Assets.HUD_FONT);
        Skin skin = game.getAssetsManager().get(Assets.GAME_UI_SKIN);

        Table table = new Table();
        table.setFillParent(true);

        String settingsLabel = "Settings";
        volumeMusic = "Music Volume: " + Math.round(game.getPreferences().getMusicVolume());
        volumeSound = "Sound Effects Volume: " + Math.round(game.getPreferences().getSoundVolume());
        if (game.getPreferences().isMusicEnabled()) {
            enabledMusic = "Music: On";
        } else {
            enabledMusic = "Music: Off";
        }
        if (game.getPreferences().isSoundEffectsEnabled()) {
            enabledSound = "Sound Effects: On";
        } else {
            enabledSound = "Sound Effects: Off";
        }

        Label titleLabel = new Label(settingsLabel, style);
        volumeMusicLabel = new Label(volumeMusic, style);
        volumeSoundEffectLabel = new Label(volumeSound, style);
        musicEnabledLabel = new Label(enabledMusic, style);
        soundEffectEnabledLabel = new Label(enabledSound, style);

        //music volume
        final Slider volumeMusicSlider = new Slider(0f, 100f, 1f, false, skin);
        volumeMusicSlider.setValue(game.getPreferences().getMusicVolume());
        volumeMusicSlider.addListener(new DragListener() {
            @Override
            public boolean handle(Event event) {
                game.getPreferences().setMusicVolume(volumeMusicSlider.getValue());
                volumeMusic = "Music Volume: " + Math.round(volumeMusicSlider.getValue());
                volumeMusicLabel.setText(volumeMusic);
                return false;
            }
        });

        //sound effects volume
        final Slider volumeSoundEffectsSlider = new Slider(0f, 100f, 1f, false, skin);
        volumeSoundEffectsSlider.setValue(game.getPreferences().getSoundVolume());
        volumeSoundEffectsSlider.addListener(new DragListener() {
            @Override
            public boolean handle(Event event) {
                game.getPreferences().setSoundVolume(volumeSoundEffectsSlider.getValue());
                volumeSound = "Sound Effects Volume: " + Math.round(volumeSoundEffectsSlider.getValue());
                volumeSoundEffectLabel.setText(volumeSound);
                return true;
            }
        });

        //music on/off
        final CheckBox musicCheckbox = new CheckBox(null, skin);
        musicCheckbox.setChecked(game.getPreferences().isMusicEnabled());
        musicCheckbox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                boolean enabled = musicCheckbox.isChecked();
                game.getPreferences().setMusicEnabled(enabled);
                if (enabled) {
                    enabledMusic = "Music: On";
                } else {
                    enabledMusic = "Music: Off";
                }
                musicEnabledLabel.setText(enabledMusic);
            }
        });

        //sound effects on/off
        final CheckBox soundEffectsCheckbox = new CheckBox(null, skin);
        soundEffectsCheckbox.setChecked(game.getPreferences().isSoundEffectsEnabled());
        soundEffectsCheckbox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                boolean enabled = soundEffectsCheckbox.isChecked();
                game.getPreferences().setSoundEffectsEnabled(enabled);
                if (enabled) {
                    enabledSound = "Sound Effects: On";
                } else {
                    enabledSound = "Sound Effects: Off";
                }
                soundEffectEnabledLabel.setText(enabledSound);
            }
        });

        //back button to main menu
        String backButtonLabel = "Back";
        final TextButton backButton = new TextButton(backButtonLabel, skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
                msg.message = MessageType.BACK_TO_MENU.code();
                game.handleMessage(msg);
            }
        });

        table.add(titleLabel).center().colspan(2);
        table.row().pad(10, 0, 0, 10);
        table.add(volumeMusicLabel).left();
        table.add(volumeMusicSlider);
        table.row().pad(10, 0, 0, 10);
        table.add(musicEnabledLabel).left();
        table.add(musicCheckbox);
        table.row().pad(10, 0, 0, 10);
        table.add(volumeSoundEffectLabel).left();
        table.add(volumeSoundEffectsSlider);
        table.row().pad(10, 0, 0, 10);
        table.add(soundEffectEnabledLabel).left();
        table.add(soundEffectsCheckbox);
        table.row().pad(10, 0, 0, 10);
        table.add(backButton).center().colspan(2);

        stage.addActor(table);
        stage.setKeyboardFocus(table);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
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
    public void hide() {
        // TODO Auto-generated method stub
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
