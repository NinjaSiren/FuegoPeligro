package com.mygdx.fuegopeligro.audio;

import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Music.OnCompletionListener;
import com.badlogic.gdx.maps.MapProperties;
import com.mygdx.fuegopeligro.AppPreferences;
import com.mygdx.fuegopeligro.Assets;
import com.mygdx.fuegopeligro.ai.msg.MessageType;
import com.mygdx.fuegopeligro.entity.Entity;

/**
 * @author JDEsguerra
 */
public class LevelAudioProcessor extends AppPreferences implements AudioProcessor, Telegraph {
    private static final String MUSIC_PROPERTY = "music";

    private Music theme;
    private Music gameOverMusic;
    private Music exitMusic;

    public LevelAudioProcessor(final AssetManager assets, final MapProperties properties) {
        if (properties == null) {
            theme = assets.get(Assets.NINJA_RABBIT_THEME);
        } else {
            theme = assets.get(properties.get(MUSIC_PROPERTY,
                    Assets.NINJA_RABBIT_THEME.fileName, String.class),
                    Music.class);
        }

        float THEME_VOLUME = this.getMusicVolume();
        theme.setVolume(THEME_VOLUME);
        theme.setLooping(true);
        if (!this.isMusicEnabled()) {
            theme.stop();
        } else {
            theme.play();
        }

        final Telegraph that = this;
        gameOverMusic = assets.get(Assets.GAME_OVER_FX);
        gameOverMusic.setOnCompletionListener(new OnCompletionListener() {
            @Override
            public void onCompletion(final Music music) {
                MessageManager.getInstance().dispatchMessage(that, MessageType.RESET.code());
            }
        });

        exitMusic = assets.get(Assets.VICTORY_FX);
        exitMusic.setOnCompletionListener(new OnCompletionListener() {
            @Override
            public void onCompletion(final Music music) {
                MessageManager.getInstance().dispatchMessage(that, MessageType.FINISH_LEVEL.code());
            }
        });
        MessageManager.getInstance().addListeners(this, MessageType.GAME_OVER.code(), MessageType.EXIT.code());
    }

    public LevelAudioProcessor(final AssetManager assets) {
        this(assets, null);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.mygdx.fuegopeligro.map.LevelProcessor#update(com.mygdx.fuegopeligro.entity.Entity)
     */
    @Override
    public void update(final Entity entity) {

    }

    @Override
    public boolean handleMessage(final Telegram msg) {
        float SOUND_VOLUME = this.getSoundVolume();
        float THEME_VOLUME = this.getMusicVolume();
        gameOverMusic.setVolume(SOUND_VOLUME);
        exitMusic.setVolume(THEME_VOLUME);
        if (msg.message == MessageType.GAME_OVER.code()) {
            theme.stop();
            gameOverMusic.play();
        } else if (msg.message == MessageType.EXIT.code()) {
            theme.stop();
            exitMusic.play();
        }
        return true;
    }

    @Override
    public void dispose() {

    }
}
